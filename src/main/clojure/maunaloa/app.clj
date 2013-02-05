(ns maunaloa.app
  (:import
    [org.springframework.context.support ClassPathXmlApplicationContext]
    [oahu.models MaunaloaFacade]
    [oahu.financial.beans StockBean]
    [maunaloa.utils DateUtils])
  (:require
    [maunaloa.views.charts.blocks :as CB]))

(comment
(defn main []
  (let [f ^ClassPathXmlApplicationContext (ClassPathXmlApplicationContext. "maunaloa.xml")
        facade ^MaunaloaFacade (.getBean f "facade")
        d0 (DateUtils/createDate 2012 1 1)
        beans (.stockPrices facade "YAR" d0 1)
        prices (vec (map #(.getValue ^StockBean %) beans))
        dx (vec (map #(.getDx ^StockBean %) beans))
        itrend-block (CB/itrend-block prices dx 0.5 90)]
    (println itrend-block)))
)

(defn scaffold[]
  (let [f ^ClassPathXmlApplicationContext (ClassPathXmlApplicationContext. "maunaloa.xml")
        facade ^MaunaloaFacade (.getBean f "facade")
        d0 (DateUtils/createDate 2010 1 1)
        beans (.stockPrices facade "YAR" d0 1)]
    beans))

;(main)
