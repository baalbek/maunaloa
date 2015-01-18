(ns scaffold
  (:import
    [org.springframework.context ApplicationContext]
    [org.springframework.context.support ClassPathXmlApplicationContext]
    [java.time.temporal IsoFields]
    [java.time LocalDate])
  (:require
    [maunaloa.utils.commonutils :as U]
    [waimea.filters.ehlers.itrend :as ITR]
    ;[waimea.plotters.candlestickplotter :as CNDL]
    [maunaloa.models.candlestickmodel :as CM]
    [net.cgrand.enlive-html :as html]))

(def dx (LocalDate/of 2012 1 1))

(defn gf [& [xml]]
  (let [cur-xml (if (nil? xml)  "maunaloa.xml" xml)
      f ^ApplicationContext (ClassPathXmlApplicationContext. cur-xml)]
    f))

(def gfm (memoize gf))

(defn bean-fn [bean-name]
  (memoize
    (fn []
      (.getBean (gfm) bean-name))))

(def srepos (bean-fn "stockRepository"))

(def drepos (bean-fn "derivativeRepository"))

(def etrade (bean-fn "etrade"))

(def sel html/select)

(defn fstock [ticker] (.findStock (srepos) ticker))

(def cndl-weeks-fn CM/candlestick-weeks-mem)

(defn prices [ticker]
  (.findStockPrices (srepos) ticker dx))

(defn cndl-weeks [ticker]
  (cndl-weeks-fn (.findStock (srepos) ticker) (prices ticker)))

;(defn cndl-plotter [num-items beans]
;  (CNDL/candlestick-plotter (take num-items (rseq (vec beans)))))

(defn itrend [prices]
  (let [data-values (U/vec-map-beans .getValue prices)]
    (ITR/calc-itrend data-values 200)))

(defn week-of [price]
  [(.getLocalDx price) (CM/extract-week price)])

(defn year-of [prices year]
  (CM/get-year prices year))

(def by-week CM/by-week)

(def y14 (year-of (prices "YAR") 2014))

(def yw (filter #(seq %) (by-week y14)))

(def yw1 (first yw))
(def yw2 (last yw))


(defn week-num [price]
    (let [dx (.getLocalDx price)]
      (.get dx IsoFields/WEEK_OF_WEEK_BASED_YEAR)))

(defn list-week-num [prices]
  (map  week-num prices))
