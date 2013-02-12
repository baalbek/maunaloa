(ns maunaloa.models.candlestickmodel
  (:import
    [oahu.financial.beans StockBean]
    [java.util Calendar GregorianCalendar])
  (:require
    [waimea.utils.commonutils :as U]))

(def cal (GregorianCalendar.))

(defn extract-year [price]
  (let [unix-year (.. price getDx getYear)]
    (+ unix-year 1900)))

(defn extract-week [price]
  (let [cur-date (.getDx price)]
    (.setTime cal cur-date)
    (.get cal (Calendar/WEEK_OF_YEAR))))

(defn get-year [prices year]
  (filter #(= (extract-year %) year) prices))

(defn get-week [prices week]
  (filter #(= (extract-week %) week) prices))

(defn by-week [prices-year]
  (map #(get-week prices-year %) (range 1 54)))

(defn get-year-week [beans year week]
  (let [year-beans (get-year beans year)
        result (get-week year-beans week)]
    result))

(defn candlestick-week [w]
  (let [lp (last w)
        dx (.getDx lp)
        opn (.getCls (first w))
        cls (.getCls lp)
        hi (apply max (map #(.getCls %) w))
        lo (apply min (map #(.getCls %) w))
        vol (apply + (map #(.getVolume %) w))]
        (StockBean. opn hi lo cls vol dx)))
    ;(MyStockprice. dx opn hi lo cls 0)))

(defn candlestick-weeks-helper [prices-year]
  (map #(candlestick-week %) (filter #(seq %) (by-week prices-year))))

(defn candlestick-weeks [beans]
  (let [years (distinct (map #(extract-year %) beans))
        p-years (map #(get-year beans %) years)]
    (reduce concat () (map #(candlestick-weeks-helper %) p-years))))

(def candlestick-weeks-mem
  (U/memoize-arg0 
    (fn [ticker beans]
      (candlestick-weeks beans))))
