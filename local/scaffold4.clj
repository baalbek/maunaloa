(ns scaffold4
  (:import
    [java.time LocalDate DayOfWeek]
    [org.springframework.context.support ClassPathXmlApplicationContext])
  (:require
    [maunaloa.models.candlestickmodel :as cm]))

(def gf
  (memoize 
    (fn [& [xml]](let [cur-xml (if (nil? xml)  "maunaloa2.xml" xml)
      f ^ApplicationContext (ClassPathXmlApplicationContext. cur-xml)]
      f))))

(defn bn [bean-name]
  (.getBean (gf) bean-name))

(defn srepos [] (bn "stockRepository"))

(def dx (LocalDate/of 2012 1 1))

(def dw (.adjustInto DayOfWeek/FRIDAY dx))

(defn sp [ticker] (.findStockPrices (srepos) ticker dx))

(def cndl cm/candlestick-weeks)



(comment et 
  (reify EtradeIndex
    (getSpot [this ticker] nil)
    (getSpots [this] nil)))
    

(comment filter-by-index [coll idxs]
  ((fn helper [coll idxs offset]
     (lazy-seq
       (when-let [idx (first idxs)]
         (if (= idx offset)
           (cons (first coll)
             (helper (rest coll) (rest idxs) (inc offset)))
           (helper (rest coll) idxs (inc offset))))))
    coll idxs 0))


