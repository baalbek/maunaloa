(ns maunaloa.views.charts.CT4
  ( :gen-class
    :prefix ct2-
    :init init
    :state state
    :implements [oahux.chart.MaunaloaChart])
  (:import
    [oahux.controllers MaunaloaChartViewModel]
    [oahux.chart MaunaloaChart]
    [javafx.scene.paint Color]
    [javafx.scene.canvas Canvas GraphicsContext])
  (:require
    (waimea.plotters
      [candlestickplotter :as CNDL])
    (waimea.blocks
      [block :as B]
      [quadrant :as Q])
    [maunaloa.utils.commonutils :as U]
    [maunaloa.models.candlestickmodel :as CM]
    [maunaloa.views.charts.blocks :as CB]))


;;------------------------------------------------------------------------
;;------------------------------- Macros ---------------------------------
;;------------------------------------------------------------------------

;;------------------------------------------------------------------------
;;--------------------------- Clojure functions --------------------------
;;------------------------------------------------------------------------

;;------------------------------------------------------------------------
;;---------------------------- Java methods ------------------------------
;;------------------------------------------------------------------------
(defn ct2-init []
  [[] (atom nil)])

;;------------------------------------------------------------------------
;;-------------------------- Interface methods ---------------------------
;;------------------------------------------------------------------------
(defn ct2-setViewModel [this, ^MaunaloaChartViewModel vm]
  (let [m (.state this)]
    (reset! m vm)))


(defn ct2-draw [this, ^Canvas c]
  (let [vm ^MaunaloaChartViewModel @(.state this)
        w (.getWidth c)
        h (.getHeight c)
        gc (.getGraphicsContext2D c)
        mleft 60
        mtop 10
        mright 10
        mbtm 30
        beans (CM/candlestick-weeks-mem
                (.getTicker vm) 
                (vec (.stockPrices vm 1)))
        dx (U/vec-map-beans .getDx beans)

        prices (U/vec-map-beans .getValue beans)
        num-items (- (count beans) 20)
        cndl-plotter (CNDL/candlestick-plotter (take num-items (rseq (vec beans))))
        itrend-block (CB/itrend-block prices dx 0.5 {:num-items num-items
                                                     :add-plotters [cndl-plotter]
                                                     :legend true
                                                     :freqs [50 10]})

        cc-block (CB/cybercycle-block prices dx 0.25 {:num-items num-items
                                                      :legend true
                                                      :freqs [50 10]})

        ]
    (doto gc
      (.setFill Color/WHITE)
      (.fillRect 0 0 w h)
      (.setStroke Color/BLACK)
      (.strokeRect (+ mleft 0.5) (+ mtop 0.5)
        (- w (+ mleft mright))
        (- h (+ mtop mbtm))))
    (let [qsx (B/block-chain
      :qs [itrend-block cc-block]
      :h (- h mtop mbtm)
      :x0 mleft
      :x1 (- w mright)
      :y0 mtop)]
      (let [[hr vr] (Q/plot-quadrant gc (first qsx))]
        (.setRuler vm vr)
        )
      (doseq [q (rest qsx)]
        (Q/plot-quadrant gc q)))))
