(ns maunaloa.service.mongodb.common
  (:import
    [com.mongodb MongoClient]))

;  mongo -u heroku -p 4a0b28228851cfa6ef121ee73560dd58 paulo.mongohq.com:10044/app19368679

;(require '[clojure.reflect :as r])
;(use '[clojure.pprint :only [print-table]])
;(print-table (sort-by :name (:members (r/reflect db))))

;(def clt (MongoClient. "paulo.mongohq.com" 10044))
;(def db (.getDB clt "app19368679"))
;(.authenticate db "heroku" (.toCharArray "4a0b28228851cfa6ef121ee73560dd58"))

(def cloud-connection
  (memoize
    (fn []
      (let [clt (MongoClient. "paulo.mongohq.com" 10044)
            db (.getDB clt "app19368679")
            auth-result (.authenticate db "heroku" (.toCharArray "4a0b28228851cfa6ef121ee73560dd58"))]
        (println auth-result) 
        db))))

(def local-connection
  (memoize
    (fn [^String host]
      (let [clt (MongoClient. host 27017)
            db (.getDB clt "maunaloa")]
        db))))

