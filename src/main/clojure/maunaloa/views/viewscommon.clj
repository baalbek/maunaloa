(ns maunaloa.views.viewscommon
  (:import
    [javafx.scene.paint Color]))

(def colors
  {
    :itrend-200 Color/BLUE
    :itrend-50 Color/RED
    :itrend-10 Color/DARKMAGENTA
    :stockprice Color/BLACK
    :volume Color/RED
  })

(defn get-color
  ([category]
    (let [kw (keyword category)]
      (kw colors)))
  ([category sub-cat]
    (let [kw (keyword (str category "-" sub-cat))]
      (kw colors))))


