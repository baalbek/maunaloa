(ns scaffold4
  (:import
    [org.springframework.context.support ClassPathXmlApplicationContext]))

(def gf
  (memoize 
    (fn [& [xml]](let [cur-xml (if (nil? xml)  "maunaloa2.xml" xml)
      f ^ApplicationContext (ClassPathXmlApplicationContext. cur-xml)]
      f))))

(defn bn [bean-name]
  (.getBean (gf) bean-name))


(comment et 
  (reify EtradeIndex
    (getSpot [this ticker] nil)
    (getSpots [this] nil)))
    

(comment filter-by-index [coll idxs]
  ((fn helper [coll idxs offset]
     (lazy-seq
       (when-let [idx (first idxs)]
         (if (= idx offset)
           (cons (first coll)
             (helper (rest coll) (rest idxs) (inc offset)))
           (helper (rest coll) idxs (inc offset))))))
    coll idxs 0))


