(ns maunaloa.service.mongodb.comments
  (:import
    [com.mongodb
      BasicDBObject
      DBCollection
      DBCursor]
    [org.bson.types ObjectId]
    [maunaloa.domain MongoDBResult])
  (:require
    (maunaloa.utils [commonutils :as util])
    (maunaloa.service.mongodb [common :as MONGO])))

(defn fetch [^String host
             ^ObjectId refid]
  (let [coll ^DBCollection (MONGO/get-collection host "comments")
        query (BasicDBObject. "refid" refid)]
    (.toArray ^DBCursor (.find coll query))))

(defn save [^String host
            ^ObjectId id
            ^String comment]
  (let [coll ^DBCollection (MONGO/get-collection host "comments")
       result (BasicDBObject. "refid" id)]
    (.append result "c" comment)
      (MongoDBResult. result (.save coll result))))
