(ns maunaloa.views.charts.CT1
  ( :gen-class
    :prefix ct1-
    :init init
    :state state
    :implements [oahux.chart.MaunaloaChart])
  (:import [oahux.chart.MaunaloaChart])
  (:import
    [oahux.controllers MaunaloaChartViewModel]
    [javafx.scene.paint Color]
    [javafx.scene.input MouseEvent]
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
;;---------------------------- Mouse Events ------------------------------
;;------------------------------------------------------------------------


;;------------------------------------------------------------------------
;;---------------------------- Java methods ------------------------------
;;------------------------------------------------------------------------
(defn ct1-init []
  [[] (atom nil)])

;;------------------------------------------------------------------------
;;-------------------------- Interface methods ---------------------------
;;------------------------------------------------------------------------
(defn ct1-setPadding [this, ^double value])

(defn ct1-setViewModel [this, ^MaunaloaChartViewModel vm]
  (let [m (.state this)]
    (reset! m vm)))


(defn ct1-draw [this, ^Canvas c]
  (let [vm ^MaunaloaChartViewModel @(.state this)
        w (.getWidth c)
        h (.getHeight c)
        gc ^GraphicsContext (.getGraphicsContext2D c)
        mleft 60
        mtop 10
        mright 10
        mbtm 30
        beans (vec (.stockPrices vm 1))
        prices (U/vec-map-beans .getValue beans)
        dx (U/vec-map-beans .getDx beans)
        num-items 90
        cndl-plotter (CNDL/candlestick-plotter (take num-items (rseq beans)))
        itrend-block (CB/itrend-block prices dx 0.5 {:num-items num-items
                                                     :add-plotters [cndl-plotter]
                                                     :legend false
                                                     :snap-unit 1})

        cc-block (CB/cybercycle-block prices dx 0.25 {:num-items num-items
                                                      :legend false})
        volume-items (U/vec-map-beans .getVolume beans)
        vol-block (CB/volume-block volume-items dx 0.25)
        ]
    (doto gc
      (.setFill Color/WHITE)
      (.fillRect 0 0 w h)
      (.setStroke Color/BLACK)
      (.strokeRect (+ mleft 0.5) (+ mtop 0.5)
        (- w (+ mleft mright))
        (- h (+ mtop mbtm))))
    (let [qsx (B/block-chain
                :qs [itrend-block cc-block vol-block]
                :h (- h mtop mbtm)
                :x0 mleft
                :x1 (- w mright)
                :y0 mtop)]
      (let [[hr vr] (Q/plot-quadrant gc (first qsx))]
        (println (str "VR " vr))
        (println (str "HR " hr))
        (.setHRuler vm hr)
        (.setVRuler vm vr)
        )
      (doseq [q (rest qsx)]
        (Q/plot-quadrant gc q)))))


