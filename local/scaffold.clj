(ns scaffold
  (:import
    [org.springframework.context ApplicationContext]
    [org.springframework.context.support ClassPathXmlApplicationContext]
    [maunaloax.models ChartWindowDressingModel]
    [maunaloax.domain ChartWindowsDressingContext]
    [maunaloa.models.mongodb MongoWindowDressingModel])
  (:require 
    (maunaloa.service.mongodb
      [common :as comm]
      [fibonacci :as fib]
      [levels :as lvl])))



(defn gf [& [xml]]
  (let [cur-xml (if (nil? xml)  "maunaloa.xml" xml)
      f ^ApplicationContext (ClassPathXmlApplicationContext. cur-xml)]
    f))

(def gfm (memoize gf))

(defn model[] (.getBean (gfm) "windowdressingmodel"))

(defn xf [tix loc]
  (ChartWindowsDressingContext. ChartWindowDressingModel/MONGO_FIBONACCI tix loc))

(defn xl [tix loc]
  (ChartWindowsDressingContext. ChartWindowDressingModel/MONGO_LEVELS tix loc))

(defn xa [tix loc]
  (ChartWindowsDressingContext. ChartWindowDressingModel/MONGO_ALL tix loc))

(def c (comm/local-connection "xochitecatl"))

(def fiff (partial fib/fetch c))

(def leff (partial lvl/fetch c))

(defn bitx [collection]
  (bit-and ChartWindowDressingModel/MONGO_FIBONACCI collection))

(def mfib ChartWindowDressingModel/MONGO_FIBONACCI)

(def mlvl ChartWindowDressingModel/MONGO_LEVELS)

(def mall ChartWindowDressingModel/MONGO_ALL)

