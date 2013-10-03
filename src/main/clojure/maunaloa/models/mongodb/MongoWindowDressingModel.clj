(ns maunaloa.models.mongodb.MongoWindowDressingModel
  ( :gen-class
    :init init
    :state state
    :implements [maunaloa.models.ChartWindowDressingModel])
  (:require (maunaloa.service.mongodb [fibonacci :as fib])))

(defn -init []
  [[] (atom {})])

(defn -getMongodbHost [this]
  (let [s (.state this)]
    (:host @s)))

(defn -setMongodbHost [this host]
  (let [s (.state this)]
    (reset! s (assoc @s :host host))))

(defn -saveFibonacci [this ticker loc p1 p2]
  (let [s (.state this)
        host (:host @s)]
    (fib/save host ticker loc p1 p2))            )


(defn -fetchFibonacci [this ticker fromDate toDate]
  (let [s (.state this)
        host (:host @s)]
    (fib/fetch ticker fromDate toDate)))


