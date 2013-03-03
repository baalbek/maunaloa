(ns maunaloa.scaffold3
  (:require [clojure.set :as S]))


(defn pascals-trapezoid [v]
  (iterate #(map + `(0 ~@%) `(~@% 0)) v))

(defn map-diff [m1 m2]
  (loop [m (transient {})
         ks (concat (keys m1) (keys m2))]
    (if-let [k (first ks)]
      (let [e1 (find m1 k)
            e2 (find m2 k)]
        (cond 
            (and e1 e2 (not= (e1 1) (e2 1))) (recur (assoc! m k (e1 1)) (next ks))
            (not e1) (recur (assoc! m k (e2 1)) (next ks))
            (not e2) (recur (assoc! m k (e1 1)) (next ks))
            :else (recur m (next ks))))
  (persistent! m))))

(defn map-difference [m1 m2]
  (let [ks1 (set (keys m1))
        ks2 (set (keys m2))
        ks1-ks2 (S/difference ks1 ks2)
        ks2-ks1 (S/difference ks2 ks1)
        ks1*ks2 (S/intersection ks1 ks2)]
    (merge  (select-keys m1 ks1-ks2)
            (select-keys m2 ks2-ks1)
            (select-keys m1
              (remove (fn [k] (= (m1 k) (m2 k)))
                ks1*ks2)))))




(defmacro jax2 [clazz & args]
    (let [constructor (symbol (str clazz "."))
          ;kw (filter keyword? (butlast args))
          mm (last args)]
    `(let [kwds# (filter keyword? [~@args])
           rest-map# (deflate-map kwds# ~mm)
           helper# (fn [v#] (if (keyword? v#) (v# ~mm) v#))
           fix-vals# (map helper# (butlast [~@args]))
           ]
           (println fix-vals# rest-map#))))
           ;(~constructor fix-vals# {} rest-map#))))
    ;`(let [rest-map# (deflate-map (println (:a ~mm))))
    ;`(~constructor 1 2)))
    ;`(println (class ~kw) ~mm)))

(comment
(defmacro jax [clazz & args]
  (let [
        mx (last args)
        kw (filter keyword? (butlast args))
        helper (fn [v] (if (keyword? v) (v mx) v))
        fix-vals (map helper (butlast args))
        ;rest-map (deflate-map kw mx)
        constructor (symbol (str clazz "."))
       ]
    (~constructor ~@fix-vals {} ~rest-map)))
)

(defrecord Howdy [x y])


(defmacro tryme [m]                                                                          ~
  (println m)
  `(println ~m))
    ;(jax Howdy 2 :b {:z 3 :#b 12.3})))

(defmacro tryme2 [v]
  (println `(~v))
  `(println ~v))

(defn kw-or-val [v m]
  (if (keyword? v) (v m) v))

(defmacro tryme3 [clazz m & rest]
  (let [ctr (symbol (str clazz "."))
        ;helper (fn [v] (if (keyword? v) (v m) v))
        ;fix-vals (map helper rest)
        ]
    `(let [kw# (filter keyword? [~@rest])
           rest-map# (deflate-map kw# ~m)
           helper# (fn [v#] (if (keyword? v#) (v# ~m) v#))
           fix-vals# (map helper# [~@rest])
           ]
       (println kw# "-" rest-map# "-" fix-vals#)
       (~ctr 2 3 {} rest-map#))))



(defmacro awhen [expr & body]
  `(let [~'it ~expr] ; refer to the expression as "it" inside the body
    (when ~'it
      (do ~@body))))

;(awhen [:a :b :c] (second it)) ; :b


(defn eval-howdy [v]
  (eval `(Howdy. ~@(take 2 v))))




(defmacro foo []
  `(println ~'x))

(defmacro t4 [clazz v]
  (let [ctr (symbol (str clazz "."))]
    `(let [v# ~v]
       (println v#))))
       ;(~ctr ~v))))

(defmacro t6 [clazz & rest]
  (let [ctr (symbol (str clazz "."))]
    `(do
        (println ~'it ~'it2)
        (~ctr 2 3))))

(defmacro t7 [& rest]
  (let [kw (filter keyword? rest)]
    `(let [rest-map# (deflate-map '~kw ~'it2)
           helper# (fn [v#] (if (keyword? v#) (v# ~'it2) v#))
           fix-vals# (map helper# [~@rest])
          ]
        (println rest-map# (class fix-vals#)))))
    ;`(println '~kw ~'it2)))


(defmacro t8 [& args]
  (let [m {:a 1 :b 2 :c 3}
        helper (fn [v] (if (keyword? v) (v m) v))
        fix-vals (map helper args)]
    `(Howdy. ~@fix-vals)))

(defn gfv [m & args]
  (let [helper (fn [v] (if (keyword? v) (v m) v))]
    (map helper args)))

(defn gdm [m & args]
  (let [kw (filter keyword? args)]
    (deflate-map kw m)))

(defn eval-howdy2 [clazz v m]
  (let [ctr (symbol (str clazz "."))]
    (eval `(~ctr ~@v {} ~m))))


(def m {:a 1 :b 2 :c 3 :e 4 :f 5})
(def v [3 2 4 5 :a :c])

;(def my-howdy (eval-howdy2 'Howdy (get-fix-vals m 2 3 :c) (get-defl-map m 2 3 :c)))


(defn deflate-map [kw m]
  (loop [yx kw mx m]
    (if-not (seq yx)
      mx
      (recur (rest yx) (dissoc mx (first yx))))))

(defn create-record-with-map [clazz m & args]
  (let [ctr (symbol (str clazz "."))
        helper (fn [v] (if (keyword? v) (v m) v))
        fix-vals (map helper args)
        kw (filter keyword? args)
        rest-map (deflate-map kw m)]
  (eval `(~ctr ~@fix-vals {} ~rest-map))))
