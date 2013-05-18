(ns maunaloa.service.calculator.testblackscholes
  (:require [maunaloa.service.calculator.BlackScholes :as B])
  (:use clojure.test))


(defn calc-diff [expected func spot x t sigma]
  (let [bs (func spot x t sigma)
        diff (Math/abs (- bs expected))]
    ;(prn func "option price: " bs ", sigma: " sigma ", diff: " diff)
    diff))

(comment

(deftest call-put-1 []
  (let [spot 100
        x 100
        t 0.5
        sigma 0.2]
    (is (<= (calc-diff 6.88 B/call-price spot x t sigma) 0.01) "Call atm 1")
    (is (<= (calc-diff 2.97 B/call-price spot x t (- sigma 0.15)) 0.01) "Call atm 2")
    (is (<= (calc-diff 13.78 B/call-price spot x t (+ sigma 0.25)) 0.03) "Call atm 3")
    (is (<= (calc-diff 21.94 B/call-price spot x t (+ sigma 0.55)) 0.03) "Call atm 4")
    (is (<= (calc-diff 4.66 B/put-price spot x t sigma) 0.241) "Put atm")
    (is (<= (calc-diff 0.73 B/put-price spot x t (- sigma 0.15)) 0.238) "Put atm 2")
    (is (<= (calc-diff 11.53 B/put-price spot x t (+ sigma 0.25)) 0.242) "Put atm 3")
    (is (<= (calc-diff 19.7 B/put-price spot x t (+ sigma 0.55)) 0.259) "Put atm 4")))

(deftest call-put-2 []
  (let [spot 120
        x 100
        t 0.5
        sigma 0.2]
    (is (<= (calc-diff 22.96 B/call-price spot x t sigma) 0.01) "Call itm 1")
    (is (<= (calc-diff 22.4787 B/call-price spot x t (- sigma 0.15)) 0.01) "Call itm 2")
    (is (<= (calc-diff 35.6815 B/call-price spot x t (+ sigma 0.55)) 0.034) "Call itm 3")
    (is (<= (calc-diff 0.5 B/put-price spot x t sigma) 0.017) "Put otm 1")
    (is (<= (calc-diff 0.0 B/put-price spot x t (- sigma 0.15)) 0.01) "Put otm 2")
    (is (<= (calc-diff 13.3617 B/put-price spot x t (+ sigma 0.55)) 0.183) "Put otm 3")
    ))
  )


(deftest call-put-3 []
  (let [spot 80
        x 100
        t 0.5
        sigma 0.2]
    (is (<= (calc-diff 0.4594 B/call-price spot x t sigma) 0.01) "Call otm 1")
    (is (<= (calc-diff 0.0 B/call-price spot x t (- sigma 0.15)) 0.01) "Call otm 2")
    (is (<= (calc-diff 11.0684 B/call-price spot x t (+ sigma 0.55)) 0.034) "Call otm 3")
    (is (<= (calc-diff 20.0 B/put-price spot x t sigma) 2.1) "Put itm 1")
    (is (<= (calc-diff 20.0 B/put-price spot x t (- sigma 0.15)) 2.47) "Put itm 2")
    (is (<= (calc-diff 29.0415 B/put-price spot x t (+ sigma 0.55)) 0.469) "Put itm 3")
    ))

