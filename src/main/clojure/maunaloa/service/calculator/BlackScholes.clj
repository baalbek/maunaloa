(ns maunaloa.service.calculator.BlackScholes
  ( :gen-class
    :implements [oahu.financial.OptionCalculator])
  (:import
    [cern.jet.random Normal]
    [cern.jet.random.engine MersenneTwister]
    [oahu.financial.beans DerivativeBean]
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
(defn #^{:fcat :incr}
      call-price [^double spot
                  ^double x
                  ^double t
                  ^double sigma]
  (let [
        d1 (calc-D1 spot x t sigma)
        d2 (calc-D2 d1 t sigma)
        cdf1 (.cdf norm d1)
        cdf2 (.cdf norm d2)
        xp (Math/exp
            (* t
              (- interest-rate)))
        a (* spot cdf1)
        b (* x xp cdf2)]
    (- a b)))

(defn #^{:fcat :decr}
      put-price [^double spot
                  ^double x
                  ^double t
                  ^double sigma]
  (let [d1 (calc-D1 spot x t sigma)
        d2 (calc-D2 d1 t sigma)
        cdf1 (.cdf norm (- d1))
        cdf2 (.cdf norm (- d2))
        xp (Math/exp
            (* t
              (- interest-rate)))
        a (* spot cdf1)
        b (* x xp cdf2)]

    (- b a)))


;(defmacro get-meta [fx] `(meta (var ~fx)))

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
      [:start 0.0 :end start-val :end-res r :fcat fcat])))

(comment
(defn find-bounds-dec [f start-val target]
  (let [r (f start-val)]
    (if (> r target)
      (loop [s (* start-val 2.0)]
        (let [r2 (f s)]
          (if (<= r2 target)
            [start-val s r2]
            (recur (* s 2.0)))))
      [0.0 start-val r])))

(defmacro find-bounds [f start-val target]
  (println (f 10))
  `(let [r# (~f ~start-val)]
    (if (< r# ~target)
      (loop [s# (* ~start-val 2.0)]
        (let [r2# (~f s#)]
          (if (>= r2# ~target)
            [~start-val s# r2#]
            (recur (* s# 2.0)))))
      [0.0 ~start-val r#])))
  )

(defn is-within-tolerance [v target tolerance]
  (< (Math/abs (- v target)) tolerance))

(defn binary-search-run [f bounds target tolerance]
  (let [cmp (if (= (:fcat bounds) :incr) < >)]
    (loop [lo (:start bounds)
           hi (:end bounds)]
      (prn lo hi)
      (let [mid (/ (+ hi lo) 2.0)
            mid-v (f mid)]
        (if (is-within-tolerance mid-v target tolerance)
          mid
          (if (cmp mid-v target)
            (recur mid hi)
            (recur lo mid)))))))

(defn binary-search [f start-val target tolerance]
  (let [bounds (find-bounds f start-val target)
        ;search-fn (if (= (:fcat bounds) :incr)
        ;            binary-search-inc
        ;            binary-search-dec)
        ]
    (if (is-within-tolerance (:end-res bounds) target tolerance)
      (:end bounds)
      (binary-search-run f bounds target tolerance))))

;;------------------------------------------------------------------------
;;-------------------------- Interface methods ---------------------------
;;------------------------------------------------------------------------

(defn -delta [this, ^DerivativeBean bean]
  0.0)


(defn -spread [this, ^DerivativeBean bean]
  0.0)


(defn -breakEven [this, ^DerivativeBean bean]
  0.0)



(defn -stockPriceFor [this
                    ^double optionPrice
                    ^DerivativeBean bean
                    priceType]
  0.0)


(defn -iv [this
          ^DerivativeBean bean
          priceType]
  (let [ calc-bean ^CalculatedDerivativeBean bean
         price (if (= DerivativeBean/BUY priceType) (.getBuy bean) (.getSell bean))]

    0.0))
