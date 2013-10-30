(ns maunaloa.models.mongodb.MongoWindowDressingModel
  (:gen-class
   :init init
   :state state
   :implements [maunaloa.models.ChartWindowDressingModel]
   :methods [[getMongodbHost [] String] [setMongodbHost [String] void]])
  (:import
    [java.util Date]
    [org.bson.types ObjectId]
    [com.mongodb MongoClient DBObject BasicDBObject])
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

;MongoDBResult saveFibonacci(String ticker, int location, DBObject p1, DBObject p2);
(defn -saveFibonacci [this
                      ^String ticker
                      loc
                      ^BasicDBObject p1
                      ^BasicDBObject p2]
  (let [s (.state this)
        host (:host @s)]
    (fib/save host ticker loc p1 p2)))

;List<DBObject> fetchFibonacci(String ticker, Date fromDate, Date toDate);
(defn -fetchFibonacci [this
                       ^String ticker
                       loc
                       ^Date fromDate
                       ^Date toDate]
  (let [s (.state this)
        host (:host @s)]
    (fib/fetch host ticker loc fromDate toDate)))


;WriteResult updateCoord(ObjectId id, DBObject p1, DBObject p2);
(defn -updateCoord [this
                    ^ObjectId id
                    ^DBObject p1
                    ^DBObject p2]
  (let [s (.state this)
        host (:host @s)]
    (fib/update-coord host id p1 p2)))

;Tuple<WriteResult,List<DBObject>> fetchComments(ObjectId id);
(defn -fetchComments [this, ^ObjectId id]
  )

;WriteResult addComment(ObjectId id, DBObject comment);
(defn -addComment [this, ^ObjectId id, ^DBObject comment])

