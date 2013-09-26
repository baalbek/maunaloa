(ns maunaloa.service.mongodb.MongoWindowDressingModel
  ( :gen-class
    :implements [maunaloa.models.ChartWindowDressingModel])
  (:require (maunaloa.service.mongodb [fibonacci :as fib])))


(defn -saveFibonacci [this ticker d0 val0 d1 val1]
  (fib/save ticker d0 val0 d1 val1))


(defn -fetchFibonacci [this ticker fromDate toDate]
  (fib/fetch ticker fromDate toDate))


