(ns maunaloa.utils.commonutils
  (:import
    [oahu.financial StockPrice]))

;;------------------------------------------------------------------------
;;------------------------------- Macros ---------------------------------
;;------------------------------------------------------------------------
(defmacro vec-map-beans [map-fn beans]
  `(vec (map #(~map-fn ^StockPrice %) ~beans)))