(ns maunaloa.service.mongodb.fibonacci
  (:import
    [com.mongodb MongoClient BasicDBObject]
    [maunaloa.domain MongoDBResult])
  (:require (maunaloa.utils [commonutils :as util])))


(def get-collection
  (memoize
    (fn [host collection]
      (let [clt (MongoClient. host 27017)
            db (.getDB clt "maunaloa")
            result (.getCollection db collection)]
        result))))

(defn create-point
  ([year month day y-val]
    (let [dx (util/new-date year month day)]
      (create-point dx y-val)))
  ([dx y-val]
    (let [result (BasicDBObject. "x" dx)]
      (.append result "y" y-val)
      result)))

(defn create-item [tix loc p1 p2]
  (let [result (BasicDBObject. "tix" tix)]
    (doto result
      (.append "active" true)
      (.append "loc" loc)
      (.append "p0" p1)
      (.append "p1" p2))))

(defn fetch [host ticker from-date to-date]
  (let [coll (get-collection host "fibonacci")
        query (BasicDBObject. "tix" ticker)]
    (.toArray (.find coll query))))

(defn save [host ticker loc p1 p2]
  (let [coll (get-collection host "fibonacci")
        result (create-item ticker loc p1 p2)
        server-result (.save coll result)]
    (MongoDBResult. result server-result)))






