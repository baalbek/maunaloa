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

(defn create-itrends-freqs [data-values freqs num-items]
  (map
    #(take num-items
      (rseq
        (ITR/calc-itrend data-values %))) freqs))

(defn create-cc-freqs [data-values freqs num-items]
  (map
    #(take num-items
       (rseq
         (CC/cybercycle data-values %))) freqs))


(defn itrend-block [data-values data-dx pct
                     {:keys [
                             num-items
                             add-plotters
                             freqs
                             legend]
                      :or {
                          num-items 90
                          add-plotters (LP/single-line-plotter data-values data-dx (:stockprice VC/colors))
                          freqs [10 50 200]
                          legend :true}}]
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
        :legend legend
        :plotters (concat itrend-plotters add-plotters)
      })))

(defn cybercycle-block [data-values data-dx pct
                       {:keys [num-items
                               freqs
                               legend]
                       :or {num-items 90
                            freqs [10 50 200]
                            legend :true}}]
  (let [dx (take num-items (rseq data-dx))
        cybercycles (map #(U/norm-v %) (create-cc-freqs data-values freqs num-items))
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


