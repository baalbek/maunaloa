(ns maunaloa.app
  (:import
    [org.springframework.context.support ClassPathXmlApplicationContext]
    [oahu.models MaunaloaFacade]
    [oahu.financial.beans StockBean]
    [maunaloa.utils DateUtils]
    [java.util Calendar GregorianCalendar])
  (:require
    [maunaloa.views.charts.blocks :as CB]
    [maunaloa.models.candlestickmodel :as CM]))

(defn scaffold[]
  (let [f ^ClassPathXmlApplicationContext (ClassPathXmlApplicationContext. "maunaloa.xml")
        facade ^MaunaloaFacade (.getBean f "facade")
        d0 (DateUtils/createDate 2010 1 1)
        beans (.stockPrices facade "YAR" 1)]
    beans))

(defn cndl-sticks []
  (let [beans (scaffold)]
     (CM/candlestick-weeks beans)))

(defn print-stockprice [bean]
  (println (.getDx bean))
  (println "opn: " (.getOpn bean))
  (println "hi: " (.getHi bean))
  (println "lo: " (.getLo bean))
  (println "cls: " (.getCls bean)))
;(main)
