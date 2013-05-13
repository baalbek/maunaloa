(ns maunaloa.app
  (:import
    [org.springframework.context.support ClassPathXmlApplicationContext]
    [oahu.models MaunaloaFacade]
    [oahu.financial.beans StockBean]
    [maunaloa.utils DateUtils]
    [java.util Calendar GregorianCalendar])
  (:require
    [waimea.utils.commonutils :as U]
    [maunaloa.views.charts.blocks :as CB]
    [maunaloa.models.candlestickmodel :as CM]))

(defn fetch-beans[ticker]
  (let [f ^ClassPathXmlApplicationContext (ClassPathXmlApplicationContext. "maunaloa.xml")
        facade ^MaunaloaFacade (.getBean f "facade")
        d0 (DateUtils/createDate 2010 1 1)
        beans (.stockPrices facade ticker 1)]
    beans))

(defn sf []
  (let [f ^ClassPathXmlApplicationContext (ClassPathXmlApplicationContext. "maunaloa.xml")]
    f))

(def week-stix 
  (U/memoize-arg0 
    (fn [ticker beans]
      (CM/candlestick-weeks beans))))


(defn cndl-sticks [ticker]
  (let [beans (fetch-beans ticker)]
     (week-stix ticker beans)))


(defn print-stockprice [bean]
  (println (.getDx bean))
  (println "opn: " (.getOpn bean))
  (println "hi: " (.getHi bean))
  (println "lo: " (.getLo bean))
  (println "cls: " (.getCls bean)))
;(main)

