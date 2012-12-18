(ns maunaloa.app
  (:import
    [org.springframework.beans.factory.xml XmlBeanFactory]
    [org.springframework.core.io FileSystemResource]
    [oahu.models MaunaloaFacade]
    [oahu.financial.beans StockBean]
    [maunaloa.utils DateUtils])
  (:require
    [maunaloa.views.charts.blocks :as CB]))

(defn main []
  (let [f ^XmlBeanFactory (XmlBeanFactory. (FileSystemResource. "maunaloa.xml"))
        facade ^MaunaloaFacade (.getBean f "facade")
        d0 (DateUtils/createDate 2012 1 1)
        beans (.stockPrices facade "YAR" d0 1)
        prices (vec (map #(.getValue ^StockBean %) beans))
        dx (vec (map #(.getDx ^StockBean %) beans))
        itrend-block (CB/itrend-block prices dx 0.5 90)]
    (println itrend-block)))

(defn scaffold[]
  (let [f ^XmlBeanFactory (XmlBeanFactory. (FileSystemResource. "maunaloa.xml"))
        facade ^MaunaloaFacade (.getBean f "facade")
        d0 (DateUtils/createDate 2012 1 1)
        beans (.stockPrices facade "YAR" d0 1)]
    beans))

;(main)
