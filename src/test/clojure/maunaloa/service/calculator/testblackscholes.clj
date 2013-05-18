(ns maunaloa.service.calculator.testblackscholes
  (:require [maunaloa.service.calculator.BlackScholes :as B])
  (:use clojure.test))


(defn calc-diff [expected func spot x t sigma]
  (let [bs (func spot x t sigma)
        diff (Math/abs (- bs expected))]
    ;(prn func "option price: " bs ", sigma: " sigma ", diff: " diff)
    diff))

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


    (comment
(deftest call-put-2 []
  (let [spot 120
        x 100
        t 0.5
        sigma 0.2]
    (is (<= (calc-diff 6.88 B/call-price spot x t sigma) 0.01) "Call price 1")
    (is (<= (calc-diff 4.42 B/put-price spot x t sigma) 0.01) "Put price 1")))

(deftest call-put-3 []
  (let [spot 140
        x 100
        t 0.5
        sigma 0.2]
    (is (<= (calc-diff 6.88 B/call-price spot x t sigma) 0.01) "Call price 1")
    (is (<= (calc-diff 4.42 B/put-price spot x t sigma) 0.01) "Put price 1")))
  )