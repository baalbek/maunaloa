(ns scaffold3
  (:import
    [oahux.chart MaunaloaChart]
    [oahux.controllers MaunaloaChartViewModel]
    [vega.filters Common]
    [vega.filters.ehlers Itrend CyberCycle]
    [java.time.temporal IsoFields]
    [org.springframework.context.support ClassPathXmlApplicationContext]
    [java.util Collections]
    [java.time LocalDate DayOfWeek])
  (:require
    [waimea.utils.commonutils :as U]
    [maunaloa.views.viewscommon :as VC]
    [maunaloa.models.candlestickmodel :as CM]
    [vega.financial.calculator.BlackScholes :as bs])
  (:use
    [clojure.string :only [split]]
    [clojure.algo.monads :only [domonad maybe-m]]))

(defmacro j1 [f1 items] `(map (fn [v#] (~f1 v#)) ~items))

(defmacro j2 [f1 f2] `(fn [v#] [(~f1 v#) (~f2 v#)]))

(defmacro j3 [f1 f2 f3] `(fn [v#] [(~f1 v#) (~f2 v#) (~f3 v#)]))

(def itr (Itrend.))
(def cc (CyberCycle.))

(def dx (LocalDate/of 2001 9 15))

(defn gf [& [xml]]
  (let [cur-xml (if (nil? xml)  "maunaloa2.xml" xml)
      f ^ApplicationContext (ClassPathXmlApplicationContext. cur-xml)]
    f))

(def gfm (memoize gf))

(defn bean-fn [bean-name]
  (memoize
    (fn []
      (.getBean (gfm) bean-name))))

(def srepos (bean-fn "stockRepository"))

(defn sp [ticker] (.findStockPrices (srepos) ticker dx))


(defn vm []
  (reify
    MaunaloaChartViewModel
    (stockPrices [this period] (sp "YAR"))))

(defn chart []
  (reify
    MaunaloaChart
    (getNumShiftWeeks [this] 4)))

(def crbund VC/create-bundle)


(defn filter-by-index [coll idxs]
  ((fn helper [coll idxs offset]
     (lazy-seq
       (when-let [idx (first idxs)]
         (if (= idx offset)
           (cons (first coll)
             (helper (rest coll) (rest idxs) (inc offset)))
           (helper (rest coll) idxs (inc offset))))))
    coll idxs 0))

(def cndl-weeks CM/candlestick-weeks)

