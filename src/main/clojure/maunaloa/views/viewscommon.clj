(ns maunaloa.views.viewscommon
  (:import
    [javafx.scene.paint Color]))

(def colors
  {
    :itrend-200 Color/BLUE
    :itrend-50 Color/RED
    :itrend-10 Color/DARKMAGENTA
    :stockprice Color/BLACK
  })

(defn get-color [category sub-cat]
  (let [kw (keyword (str category "-" sub-cat))]
    (kw colors)))

