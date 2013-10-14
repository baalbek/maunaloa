(ns maunaloa.models.mongodb.MongoWindowDressingModel
  (:gen-class
   :init init
   :state state
   :implements [maunaloa.models.ChartWindowDressingModel]
   :methods [[getMongodbHost [] String] [setMongodbHost [String] void]])
  (:import
    [java.util Date]
    [com.mongodb MongoClient BasicDBObject])
  (:require
    (maunaloa.service.mongodb [fibonacci :as fib])))

(defn -init []
  [[] (atom {})])

(defn -getMongodbHost [this]
  (let [s (.state this)]
    (:host @s)))

(defn -setMongodbHost [this host]
  (let [s (.state this)]
    (reset! s (assoc @s :host host))))

(defn -saveFibonacci [this
                      ^String ticker
                      loc
                      ^BasicDBObject p1
                      ^BasicDBObject p2]
  (let [s (.state this)
        host (:host @s)]
    (fib/save host ticker loc p1 p2)))

(defn -fetchFibonacci [this
                       ^String ticker
                       ^Date fromDate
                       ^Date toDate]
  (let [s (.state this)
        host (:host @s)]
    (fib/fetch host ticker fromDate toDate)))


