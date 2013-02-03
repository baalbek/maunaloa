(ns maunaloa.views.charts.CT2
  ( :gen-class
    :prefix ct2-
    :init init
    :state state
    :implements [oahu.views.MaunaloaChart])
  (:import [oahu.views MaunaloaChart])
  (:import
    [oahu.controllers ChartViewModel]
    (oahu.financial.beans StockBean)
    [javafx.scene.paint Color]
    [javafx.scene.canvas Canvas GraphicsContext])
  (:require
    (waimea.plotters
      [candlestickplotter :as CNDL])
    (waimea.blocks
      [block :as B]
      [quadrant :as Q])
    [maunaloa.views.charts.blocks :as CB]))


;;------------------------------------------------------------------------
;;------------------------------- Macros ---------------------------------
;;------------------------------------------------------------------------

;;------------------------------------------------------------------------
;;---------------------------- Java methods ------------------------------
;;------------------------------------------------------------------------
(defn ct1-init []
  [[] (atom nil)])

;;------------------------------------------------------------------------
;;-------------------------- Interface methods ---------------------------
;;------------------------------------------------------------------------
(defn ct1-setViewModel [this, ^ChartViewModel vm]
  (let [m (.state this)]
    (reset! m vm)))


(defn ct1-draw [this, ^Canvas c])