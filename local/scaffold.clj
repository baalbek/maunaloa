(ns scaffold
  (:import
    [org.springframework.context ApplicationContext]
    [org.springframework.context.support ClassPathXmlApplicationContext])
  (:require
    (maunaloa.service.mongodb
      [common :as comm]
      [fibonacci :as fib]
      [levels :as lvl])))



(defn gf [& [xml]]
  (let [cur-xml (if (nil? xml)  "test-maunaloa.xml" xml)
      f ^ApplicationContext (ClassPathXmlApplicationContext. cur-xml)]
    f))

(def gfm (memoize gf))

