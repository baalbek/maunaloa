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


(defn =z [a b delta]
  (let [diff (Math/abs (- a b))]
    (<= diff delta)))

(deftest simple-binary-search-inc []
  (let [inc-fn (fn [v] (* 1.1 v))
        tol 0.01]
    (is (=z (B/binary-search inc-fn 1.0 10.0 tol) 9.09 tol) "simple binary search 1")
    (is (=z (B/binary-search inc-fn 100.0 10.0 tol) 9.09 tol) "simple binary search 2")
    (is (=z (B/binary-search inc-fn 100.0 0.1 tol) 0.09 tol) "simple binary search 3")
    (is (=z (B/binary-search inc-fn 0.05 0.1 tol) 0.09 tol) "simple binary search 4")
    ))

(deftest simple-binary-search-dec []
  (let [dec-fn (fn [v] (/ v 1.1))
        tol 0.01]
    (is (=z (B/binary-search dec-fn 1.0 10.0 tol) 11.0 tol) "simple binary search 5")
    (is (=z (B/binary-search dec-fn 100.0 10.0 tol) 11.01 tol) "simple binary search 6")
    (is (=z (B/binary-search dec-fn 100.0 0.1 tol) 0.11 tol) "simple binary search 7")
    (is (=z (B/binary-search dec-fn 0.05 0.1 tol) 0.11 tol) "simple binary search 8")
    ))


(deftest binary-search-iv-calls-atm []
  (let [tol 0.01
        spot 100.0
        x 100.0
        t 0.5
        f (partial B/call-price spot x t)]
    (is (=z (B/binary-search f 0.4 6.88 tol) 0.2 tol) "call iv binary search 1")
    (is (=z (B/binary-search f 0.4 2.97 tol) 0.05 tol) "call iv binary search 2")
    (is (=z (B/binary-search f 0.4 13.78 tol) 0.45 tol) "call iv binary search 3")))

(deftest binary-search-iv-calls-itm []
  (let [tol 0.01
        spot 120.0
        x 100.0
        t 0.5
        f (partial B/call-price spot x t)]
    (is (=z (B/binary-search f 0.4 22.96 tol) 0.2 tol) "call iv binary search 4")
    (is (=z (B/binary-search f 0.4 22.4690 0.001) 0.05 tol) "call iv binary search 5")
    (is (=z (B/binary-search f 0.4 35.68 tol) 0.75 tol) "call iv binary search 6")))


(deftest binary-search-iv-calls-otm []
  (let [tol 0.01
        spot 80.0
        x 100.0
        t 0.5
        f (partial B/call-price spot x t)]
    (is (=z (B/binary-search f 0.4 0.4594 tol) 0.2 tol) "call iv binary search 7")
    (is (=z (B/binary-search f 0.4 0.0 0.001) 0.05 tol) "call iv binary search 8")
    (is (=z (B/binary-search f 0.4 11.06 tol) 0.75 tol) "call iv binary search 9")))



(deftest binary-search-iv-puts-atm []
  (let [tol 0.01
        spot 100.0
        x 100.0
        t 0.5
        f (partial B/put-price spot x t)]
    (is (=z (B/binary-search f 0.4 4.66 tol) 0.2 tol) "put iv binary search 1")
    (is (=z (B/binary-search f 0.4 0.73 tol) 0.05 0.011) "put iv binary search 2")
    (is (=z (B/binary-search f 0.4 19.7 tol) 0.75 tol) "put iv binary search 3")))

(deftest binary-search-iv-puts-otm []
  (let [tol 0.01
        spot 120.0
        x 100.0
        t 0.5
        f (partial B/put-price spot x t)]
    (is (=z (B/binary-search f 0.4 0.5 tol) 0.2 tol) "put iv binary search 4")
    (is (=z (B/binary-search f 0.4 0.0 0.001) 0.05 0.011) "put iv binary search 5")
    (is (=z (B/binary-search f 0.4 13.36 tol) 0.75 tol) "put iv binary search 6")))

(comment binary-search-iv-puts-itm []
  (let [tol 0.01
        spot 80.0
        x 100.0
        t 0.5
        f (partial B/put-price spot x t)]
    (is (=z (B/binary-search f 0.4 0.5 tol) 0.2 tol) "put iv binary search 7")
    (is (=z (B/binary-search f 0.4 0.0 0.001) 0.05 0.011) "put iv binary search 8")
    (is (=z (B/binary-search f 0.4 13.36 tol) 0.75 tol) "put iv binary search 9")))