(ns maunaloa.views.charts.blocks
  (:import
    [javafx.scene.paint Color])
  (:require
    [maunaloa.views.viewscommon :as VC]
    [waimea.utils.commonutils :as U]
    [waimea.plotters.lineplotter :as LP]
    (waimea.blocks
      [block :as B])
    (waimea.filters.ehlers
      [itrend :as ITR])))

(defn create-itrends-freqs [data-values freqs num-items]
  (map
    #(take num-items
      (rseq
        (ITR/calc-itrend data-values %))) freqs))

(defn itrend-block [data-values data-dx pct
                     {:keys [
                             num-items
                             add-plotters
                             freqs]
                      :or {
                          num-items 90
                          add-plotters (LP/single-line-plotter data-values data-dx (:stockprice VC/colors))
                          freqs [10 50 200]}}]
  (let [dx (take num-items (rseq data-dx))
        itrends (create-itrends-freqs data-values freqs num-items)
        itrend-plotters (map #(LP/single-line-plotter %1 dx (VC/get-color "itrend" %2))  itrends freqs)
        [data-min data-max] (U/find-min-max itrends)]
    (B/foundation
      {
        :data-min (/ data-min 1.1)
        :data-max (* data-max 1.1)
        :start-date (last dx)
        :end-date (first dx)
        :pct pct
        :legend :true
        :plotters (concat itrend-plotters add-plotters)
      })))

(comment
(defn itrend-candlestick-block [beans pct
                                { :keys [num-items
                                        freqs]
                                  :or {num-items 90
                                       freqs [10 50 200]}
                                 }]))
