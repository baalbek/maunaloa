(ns maunaloa.models.mongodb.MongoWindowDressingModel
  ( :gen-class
    :implements [maunaloa.models.ChartWindowDressingModel])
  (:require (maunaloa.service.mongodb [fibonacci :as fib])))


(defn -saveFibonacci [this ticker loc p1 p2]
  (fib/save ticker loc p1 p2)) 


(defn -fetchFibonacci [this ticker fromDate toDate]
  (fib/fetch ticker fromDate toDate))


