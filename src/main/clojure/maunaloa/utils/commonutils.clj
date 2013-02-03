(ns maunaloa.utils.commonutils
  (:import
    [oahu.financial.beans StockBean]))

;;------------------------------------------------------------------------
;;------------------------------- Macros ---------------------------------
;;------------------------------------------------------------------------
(defmacro vec-map-beans [map-fn beans]
  `(vec (map #(~map-fn ^StockBean %) ~beans)))