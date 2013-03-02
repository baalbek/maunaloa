(ns maunaloa.scaffold)

(defn pix-1 
  ([this v] (+ 2.0 (.x this)))
  ([this v f] 
    (f (pix-1 v))))

(defprotocol Jada 
  (pix 
    [this v]
    [this v f]))

(defrecord Jadax [x y])

(extend-protocol Jada
 	Jadax
 	(pix 
	  ([this v] 
	    (let [a (+ 2.0 (.x this))
                  b (* (:jitter this) a)]
              b)) 
	  ([this v f] 
	    (f (pix this v)))))

