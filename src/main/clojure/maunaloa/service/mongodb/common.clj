(ns maunaloa.service.mongodb.common
  (:import
    [com.mongodb MongoClient]))

(def get-collection
  (memoize
    (fn [^String host
         ^String collection]
      (let [clt (MongoClient. host 27017)
            db (.getDB clt "maunaloa")
            result (.getCollection db collection)]
        result))))