(ns maunaloax.scaffold
  (:import
    [org.springframework.context ApplicationContext]
    [org.springframework.context.support ClassPathXmlApplicationContext])
  (:require 
    (maunaloa.service.mongodb
      [common :as comm]
      [fibonacci :as fib])))
     


(defn gf [& [xml]]
  (let [cur-xml (if (nil? xml)  "maunaloax.xml" xml)
        f ^ApplicationContext (ClassPathXmlApplicationContext. cur-xml)]
    f))

(def model (.getBean (gf) "model"))

(def conn (comm/local-connection "xochitecatl"))

(def fife fib/fetch)
