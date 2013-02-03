(ns maunaloa.views.charts.CT1
  ( :gen-class
    :prefix ct1-
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
(defmacro vec-map-beans [map-fn beans]
  `(vec (map #(~map-fn ^StockBean %) ~beans)))

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


(defn ct1-draw [this, ^Canvas c]
  (let [vm ^ChartViewModel @(.state this)
        w (.getWidth c)
        h (.getHeight c)
        gc (.getGraphicsContext2D c)
        mleft 60
        mtop 10
        mright 10
        mbtm 30
        beans (vec (.stockPrices vm 1))
        prices (vec-map-beans .getValue beans)
        dx (vec-map-beans .getDx beans)
        num-items 90
        cndl-plotter (CNDL/candlestick-plotter (take num-items (rseq beans)))
        itrend-block (CB/itrend-block prices dx 0.5 {:num-items num-items
                                                     :add-plotters [cndl-plotter]
                                                     :legend false})
        cc-block (CB/cybercycle-block prices dx 0.25 {:num-items num-items
                                                      :legend false})
        volume-items (vec-map-beans .getVolume beans)
        vol-block (CB/volume-block volume-items dx 0.25)
        ]
    (doto gc
      (.setFill Color/WHITE)
      (.fillRect 0 0 w h)
      (.strokeRect (+ mleft 0.5) (+ mtop 0.5)
        (- w (+ mleft mright))
        (- h (+ mtop mbtm))))
    (let [qsx (B/block-chain
              :qs [itrend-block cc-block vol-block]
              :h (- h mtop mbtm)
              :x0 mleft
              :x1 (- w mright)
              :y0 mtop)]
      (doseq [q qsx]
        (Q/plot-quadrant gc q)))))


