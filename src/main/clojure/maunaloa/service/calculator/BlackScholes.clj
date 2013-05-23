(ns maunaloa.service.calculator.BlackScholes
  ( :gen-class
    :implements [oahu.financial.OptionCalculator])
  (:import
    [cern.jet.random Normal]
    [cern.jet.random.engine MersenneTwister]
    [oahu.financial.beans DerivativeBean]
    [oahu.exceptions BinarySearchException]
    [maunakea.financial.beans CalculatedDerivativeBean]))

(def norm (Normal. 0.0 1.0 (MersenneTwister.)))

(def interest-rate 0.05)


(defn calc-D2 [^double d1
               ^double t
               ^double sigma]
  (- d1
    (* sigma
      (Math/sqrt t))))

(defn calc-D1 [^double spot
              ^double x
              ^double t
              ^double sigma]
  (let [
        a (Math/log (/ spot x))
        b (* t
            (+ interest-rate
              (* sigma (/ sigma 2.0))))
        c (* sigma (Math/sqrt t))]
    (/ (+ a b) c)))


;  (S * norm.cdf(d1)) - (X * Math.exp(-r * T) * norm.cdf(d2))
;  (X * Math.exp(-r * T) * norm.cdf(-d2)) - (S * norm.cdf(-d1))
(defn call-price [^double spot
                  ^double x
                  ^double t
                  ^double sigma]
  (let [
        d1 (calc-D1 spot x t sigma)
        d2 (calc-D2 d1 t sigma)
        cdf1 (.cdf ^Normal norm d1)
        cdf2 (.cdf ^Normal norm d2)
        xp (Math/exp
            (* t
              (- interest-rate)))
        a (* spot cdf1)
        b (* x xp cdf2)]
    (- a b)))

(defn put-price [^double spot
                  ^double x
                  ^double t
                  ^double sigma]
  (let [d1 (calc-D1 spot x t sigma)
        d2 (calc-D2 d1 t sigma)
        cdf1 (.cdf ^Normal norm (- d1))
        cdf2 (.cdf ^Normal norm (- d2))
        xp (Math/exp
            (* t
              (- interest-rate)))
        a (* spot cdf1)
        b (* x xp cdf2)]

    (max (- x spot) (- b a))))

(defn find-bounds [f start-val target]
  (let [r (f start-val)
        fcat  (let [r-1 (f (+ start-val 1))]
                (if (> r-1 r) :incr :decr))
        cmp-1 (if (= fcat :incr) < >)
        cmp-2 (if (= fcat :incr) >= <=)]
    (if (cmp-1 r target)
      (loop [s (* start-val 2.0)]
        (let [r2 (f s)]
          (if (cmp-2 r2 target)
            {:start start-val :end s :end-res r2 :fcat fcat}
            (recur (* s 2.0)))))
      {:start 0.0 :end start-val :end-res r :fcat fcat})))

(defn is-within-tolerance [v target tolerance]
  (< (Math/abs (- v target)) tolerance))

(defn binary-search-run [f bounds target tolerance]
  (let [cmp (if (= (:fcat bounds) :incr) < >)
        max-iter 25]
    (loop [lo (:start bounds)
           hi (:end bounds)
           counter 0]
      ;(prn lo hi)
      (if (> counter max-iter)
        (throw (BinarySearchException. (str "Max iterations (" max-iter ") reached"))))
      (let [mid (/ (+ hi lo) 2.0)
            mid-v (f mid)]
        (if (is-within-tolerance mid-v target tolerance)
          mid
          (if (cmp mid-v target)
            (recur mid hi (inc counter))
            (recur lo mid (inc counter))))))))

(defn binary-search [f start-val target tolerance]
  (let [bounds (find-bounds f start-val target)]
    (if (is-within-tolerance (:end-res bounds) target tolerance)
      (:end bounds)
      (binary-search-run f bounds target tolerance))))

(defn price-fn [^DerivativeBean deriv]
  (if (= DerivativeBean/CALL (.getOpType deriv))
        call-price
        put-price))


(defn spot-finder [^DerivativeBean d]
  (let [f (price-fn d)
        x (.getX d)
        t (.getYears d)
        sigma (.getIvBuy d)]
    (fn [spot]
      (f spot x t sigma))))

;;------------------------------------------------------------------------
;;-------------------------- Interface methods ---------------------------
;;------------------------------------------------------------------------

(defn -delta [this, ^DerivativeBean deriv]
  (let [ cb ^CalculatedDerivativeBean deriv
         iv (.getIvSell cb)
         new-spot (+ 1.0 (-> cb .getParent .getValue))
         new-price ((price-fn deriv)
                     new-spot
                     (.getX cb)
                     (.getYears cb)
                     iv)]
    (- new-price (.getSell deriv))))


(defn -spread [this, ^DerivativeBean deriv]
  0.0)


(defn -breakEven [this, ^DerivativeBean deriv]
  0.0)



(defn -stockPriceFor [this
                    ^double optionPrice
                    ^DerivativeBean deriv]
  (let [f (spot-finder deriv)
        start-val (-> deriv .getParent .getCls)]
    (binary-search f start-val optionPrice 0.1)))


(defn -iv [this
          ^DerivativeBean deriv
          priceType]
  (let [ cb ^CalculatedDerivativeBean deriv
         price (if (= DerivativeBean/BUY priceType)
                     (.getBuy cb)
                     (.getSell cb))
         opx-f (partial
                 (price-fn cb)
                  (-> cb .getParent .getCls)
                  (.getX cb)
                  (/ (.getDays cb) 365.0))
           ]
    (binary-search opx-f 0.4 price 0.01)
  ))
