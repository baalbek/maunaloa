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

(defn xf [tix loc val]
  (let [ctx (ChartWindowsDressingContext. ChartWindowDressingModel/MONGO_FIBONACCI tix loc)]
    (.setValue ctx val)
    ctx))

(defn xl [tix loc val]
  (let [ctx (ChartWindowsDressingContext. ChartWindowDressingModel/MONGO_LEVELS tix loc)]
    (.setValue ctx val)
    ctx))

(defn xa [tix loc]
  (ChartWindowsDressingContext. ChartWindowDressingModel/MONGO_ALL tix loc))

(def c (comm/local-connection "xochitecatl"))

(def fiff (partial fib/fetch c))

(def leff (partial lvl/fetch c))

(def fiss (partial fib/save c))

(def less (partial lvl/save c))

(defn bitx [collection]
  (bit-and ChartWindowDressingModel/MONGO_FIBONACCI collection))



