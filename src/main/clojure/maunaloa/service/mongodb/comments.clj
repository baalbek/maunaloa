(ns maunaloa.service.mongodb.comments
  (:require
    (maunaloa.utils [commonutils :as util])
    (maunaloa.service.mongodb [common :as MONGO])))

(defn fetch [^String host
             ^ObjectId id]
  (let [coll ^DBCollection (MONGO/get-collection host "comments")
        query (BasicDBObject. "_id" id)]
    (.toArray ^DBCursor (.find coll query))))

(defn save [^String host
            ^String comment])