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
    [maunaloa.utils.commonutils :as U]
    [maunaloa.views.charts.blocks :as CB]))


;;------------------------------------------------------------------------
;;------------------------------- Macros ---------------------------------
;;------------------------------------------------------------------------

;;------------------------------------------------------------------------
;;---------------------------- Java methods ------------------------------
;;------------------------------------------------------------------------
(defn ct2-init []
  [[] (atom nil)])

;;------------------------------------------------------------------------
;;-------------------------- Interface methods ---------------------------
;;------------------------------------------------------------------------
(defn ct2-setViewModel [this, ^ChartViewModel vm]
  (let [m (.state this)]
    (reset! m vm)))


(defn ct2-draw [this, ^Canvas c]
  (let [vm ^ChartViewModel @(.state this)
        w (.getWidth c)
        h (.getHeight c)
        gc (.getGraphicsContext2D c)
        mleft 60
        mtop 10
        mright 10
        mbtm 30
        beans (vec (.stockPrices vm 1))
        dx (U/vec-map-beans .getDx beans)
        num-items 90
        volume-items (U/vec-map-beans .getVolume beans)
        vol-block (CB/volume-block volume-items dx 0.25)
        ]
    (doto gc
      (.setFill Color/WHITE)
      (.fillRect 0 0 w h)
      (.strokeRect (+ mleft 0.5) (+ mtop 0.5)
        (- w (+ mleft mright))
        (- h (+ mtop mbtm))))
    (let [qsx (B/block-chain
      :qs [vol-block]
      :h (- h mtop mbtm)
      :x0 mleft
      :x1 (- w mright)
      :y0 mtop)]
      (doseq [q qsx]
        (Q/plot-quadrant gc q)))))