(ns scaffold
  (:require
    [clojure.string :as cs]))

(def this (atom {}))

(defn as-get-set [p get-or-set prefix]
  (str prefix get-or-set (cs/upper-case (first p)) (.substring p 1)))

(defn p2kw [prop]
  (keyword (cs/lower-case prop)))


(defn getter [prop prefix default]
  (if (nil? default)
    `(def ~(symbol (as-get-set prop "get" prefix))
       (fn [this#]
         (let [cache# this#]
          (~(p2kw prop) @cache#))))
    `(def ~(symbol (as-get-set prop "get" prefix))
       (fn [this#]
         (let [cache# this#
               val# (~(p2kw prop) @cache#)]
           (if (nil? val#)
             (do
               (swap! cache# assoc ~(p2kw prop) ~default)
               ~default)
             val#))))))

(defn setter [prop prefix default]
  `(def ~(symbol (as-get-set prop "set" prefix))
     (fn [this# value#]
       (let [cache# this#]
        (swap! cache# assoc ~(p2kw prop) value#)))))

(defn getsetter [prop prefix default]
  `(do
     (def ~(symbol (as-get-set prop "set" prefix))
        (fn [this# value#]
          (let [cache# this#]
            (swap! cache# assoc ~(p2kw prop) value#))))
     (def ~(symbol (as-get-set prop "get" prefix))
        (fn [this#]
          (let [cache# this#
                val# (~(p2kw prop) @cache#)]
            (if (nil? val#)
              (do
                (swap! cache# assoc ~(p2kw prop) ~default)
                ~default)
              val#))))))

(defmacro defprop [variants prop &
                  { :keys [prefix default]
                    :or {prefix "-"
                         default nil}}]
  (cond
    (= variants :getset)
      (getsetter prop prefix default)
    (= variants :get)
      (getter prop prefix default)
    (= variants :set)
      (setter prop prefix default)))

(comment defprop [variants prop &
                    { :keys [prefix default]
                      :or {prefix "-"
                           default nil}}]
  (println prefix " " default)
  (cond
    (= variants :getset)
    `(do
       (def ~(symbol (as-get-set prop "get" prefix))
         (fn [this#] (let [cache# this#] (~(p2kw prop) @cache#))))
       (def ~(symbol (as-get-set prop "set" prefix))
         (fn [this# value#] (let [cache# this#] (swap! cache# assoc ~(p2kw prop) value#)))))
    (= variants :get)
    `(def ~(symbol (as-get-set prop "get" prefix))
       (fn [this#]
         (let [cache# this#]
           (~(p2kw prop) @cache#))))
    (= variants :set)
    `(def ~(symbol (as-get-set prop "set" prefix))
       (fn [this# value#]
         (let [cache# this#]
           (swap! cache# assoc ~(p2kw prop) value#))))))
