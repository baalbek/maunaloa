(ns maunaloa.views.charts.DateRulerImpl
  ( :gen-class
    :prefix daterulerimpl-
    :implements [oahu.views.chart.IRuler])
  (:require
    [waimea.rulers.vruler :as VR])
  (:import [javafx.geometry Point2D]))



;;------------------------------------------------------------------------
;;-------------------------- Interface methods ---------------------------
;;------------------------------------------------------------------------
(defn daterulerimpl-calcPix [this, ^Object value]
  34.0)

(defn daterulerimpl-calcValue [this, ^double pix]
  nil)

(defn daterulerimpl-getLowerRight [this]
  (Point2D. 0 0))

(defn daterulerimpl-getUpperLeft [this]
  (Point2D. 0 0))

