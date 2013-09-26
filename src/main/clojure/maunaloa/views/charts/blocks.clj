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
      [cc :as CC]
      [itrend :as ITR])))

(defn create-freqs [f data-values freqs num-items]
  (map
    #(take num-items
       (rseq
         (f data-values %))) freqs))

(defn itrend-block [data-values data-dx pct
                    & [{:keys [
                             num-items
                             add-plotters
                             freqs
                             legend
                             padding]
                      :or {
                          num-items 90
                          add-plotters nil
                          freqs [10 50 200]
                          legend true
                          padding 1.05
                          }}]]
  (let [dx (take num-items (rseq data-dx))
        itrends (create-freqs ITR/calc-itrend data-values freqs num-items)
        itrend-plotters (map #(LP/single-line-plotter %1 dx (VC/get-color "itrend" %2))  itrends freqs)
        [data-min data-max] (U/find-min-max itrends)
        cur-add-plotters (if (nil? add-plotters)
                            [(LP/single-line-plotter
                              (take num-items (rseq data-values))
                              (take num-items (rseq data-dx))
                              (:stockprice VC/colors))]
                            add-plotters)
                        ]
    (B/foundation
      {
        :data-min (/ data-min padding)
        :data-max (* data-max padding)
        :start-date (last dx)
        :end-date (first dx)
        :pct pct
        :legend legend
        :plotters (concat itrend-plotters cur-add-plotters)
      })))

(defn cybercycle-block [data-values data-dx pct
                       & [{:keys [num-items
                               freqs
                               legend]
                       :or {num-items 90
                            freqs [10 50 200]
                            legend true}}]]
  (let [dx (take num-items (rseq data-dx))
        cybercycles (map #(U/norm-v %) (create-freqs CC/cybercycle data-values freqs num-items))
        cc-plotters (map #(LP/single-line-plotter %1 dx (VC/get-color "itrend" %2))  cybercycles freqs)]
    (B/foundation
      {
        :data-min -1.0
        :data-max 1.0
        :start-date (last dx)
        :end-date (first dx)
        :pct pct
        :legend legend
        :plotters cc-plotters})))

(defn volume-block [data-values data-dx pct
                    & [{:keys [num-items
                            legend]
                     :or {num-items 90
                          legend true}}]]
  (let [dx (take num-items (rseq data-dx))
        volume (U/norm-v (take num-items (rseq data-values)))
        plotters [(LP/volume-plotter volume dx (VC/get-color "volume"))]]
    (B/foundation
      {
        :data-min 0.0
        :data-max 1.0
        :start-date (last dx)
        :end-date (first dx)
        :pct pct
        :legend legend
        :plotters plotters})))

