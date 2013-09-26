(ns maunaloa.utils.commonutils
  (:import
    [java.util Date]
    [oahu.financial StockPrice]))

(defn new-date [y m d]
  (Date. (- y 1900) (- m 1) d))

;;------------------------------------------------------------------------
;;------------------------------- Macros ---------------------------------
;;------------------------------------------------------------------------
(defmacro vec-map-beans [map-fn beans]
  `(vec (map #(~map-fn ^StockPrice %) ~beans)))