(ns maunaloa.service.mongodb.fibonacci
  (:import [com.mongodb MongoClient BasicDBObject])
  (:require (maunaloa.utils [commonutils :as util])))


(def get-collection
  (memoize
    (fn [collection]
      (let [clt (MongoClient. "xochitecatl" 27017)
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

(defn create-item [tix p1 p2]
  (let [result (BasicDBObject. "tix" tix)]
    (doto result
      (.append "active" true)
      (.append "p0" p1)
      (.append "p1" p2))))

(defn fetch [ticker from-date to-date]
  (let [coll (get-collection "fibonacci")
        query (BasicDBObject. "tix" ticker)]
    (.find coll query)))

(defn save [ticker from-date val-1 to-date val-2]
  (let [coll (get-collection "fibonacci")
        p1 (create-point from-date val-1)
        p2 (create-point to-date val-2)
        result (create-item ticker p1 p2)]
    (.save coll result)))






