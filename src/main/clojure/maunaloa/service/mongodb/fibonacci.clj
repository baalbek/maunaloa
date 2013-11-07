(ns maunaloa.service.mongodb.fibonacci
  (:import
    [java.util Date]
    [com.mongodb
      DBObject
      BasicDBObject
      DBCollection
      DBCursor]
    [org.bson.types ObjectId]
    [maunaloa.domain MongoDBResult])
  (:require
    (maunaloa.utils [commonutils :as util])
    (maunaloa.service.mongodb [common :as MONGO])))


(defn create-point
  ([year month day y-val]
    (let [dx (util/new-date year month day)]
      (create-point dx y-val)))
  ([dx y-val]
    (let [result (BasicDBObject. "x" dx)]
      (.append result "y" y-val)
      result)))

(defn create-item [^String tix
                   loc
                   ^BasicDBObject p1
                   ^BasicDBObject p2]
  (let [result (BasicDBObject. "tix" tix)]
    (doto result
      (.append "active" true)
      (.append "loc" loc)
      (.append "p1" p1)
      (.append "p2" p2))))

(defn fetch [^String host
             ^String ticker
             loc
             ^Date from-date
             ^Date to-date]
  (let [coll ^DBCollection (MONGO/get-collection host "fibonacci")
        query (BasicDBObject. "tix" ticker)]
    (.toArray ^DBCursor (.find coll query))))

(defn save [^String host
            ^String ticker
            loc
            ^BasicDBObject p1
            ^BasicDBObject p2]
  (let [coll (MONGO/get-collection host "fibonacci")
        result (create-item ticker loc p1 p2)
        server-result (.save coll result)]
    (MongoDBResult. result server-result)))


;db.fibonacci.update({_id : ObjectId("525d841b44ae19e5186a95c6")}, {$set : {loc: 3}})
(defn update-coord [^String host
                    ^ObjectId id
                    ^DBObject p1
                    ^DBObject p2]
  (let [coll ^DBCollection (MONGO/get-collection host "fibonacci")
        set-obj (BasicDBObject. "$set" (BasicDBObject. "p1" p1))
        ;query (BasicDBObject. "_id" (ObjectId. "525d841b44ae19e5186a95c6"))]
        query (BasicDBObject. "_id" id)]
    (.append set-obj "$set" (BasicDBObject. "p2" p2))
    (.update coll query set-obj)))




