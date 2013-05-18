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
(defn call-price [^double spot
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

(defn put-price [^double spot
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
