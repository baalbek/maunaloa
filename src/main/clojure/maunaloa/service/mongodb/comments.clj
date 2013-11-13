(ns maunaloa.service.mongodb.comments
  (:import
    [com.mongodb
      BasicDBObject
      DBCursor
      DBApiLayer]
    [org.bson.types ObjectId]
    [maunaloa.domain MongoDBResult])
  (:require
    (maunaloa.utils [commonutils :as util])))

(defn fetch [^DBApiLayer conn
             ^ObjectId refid]
  (let [coll (.getCollection conn "comments")
        query (BasicDBObject. "refid" refid)]
    (.toArray ^DBCursor (.find coll query))))

(defn save [^DBApiLayer conn
            ^ObjectId id
            ^String comment]
  (let [coll (.getCollection conn "comments")
       result (BasicDBObject. "refid" id)]
    (.append result "c" comment)
    (.save coll result)))
