(ns scaffold3
  (:import
    [org.springframework.context.support ClassPathXmlApplicationContext]
    [java.util Collections]
    [java.time LocalDate DayOfWeek]))

(defn gf [& [xml]]
  (let [cur-xml (if (nil? xml)  "maunaloa2.xml" xml)
      f ^ApplicationContext (ClassPathXmlApplicationContext. cur-xml)]
    f))

(def gfm (memoize gf))

(defn bean-fn [bean-name]
  (memoize
    (fn []
      (.getBean (gfm) bean-name))))

(def srepos (bean-fn "stockRepository"))

(def dx (LocalDate/of 2001 9 15))

(defn sp [ticker] (.findStockPrices (srepos) ticker dx))

(def yar (map #(.getDx %) (sp "YAR")))

(defn binary-search-dx [dxx loc-d]
  (let [result (Collections/binarySearch dxx loc-d compare)]
    (println "Checking for " loc-d)
    (if (< result 0)
      nil
      result)))

(defn find-dx-index [dxx head-date]
  (let [find-fn (partial binary-search-dx dxx)]
    (loop [sds (map #(java.sql.Date/valueOf (.minusDays head-date %)) (range 7))
           result nil]
      (if (or (nil? sds) (not= nil result))
        result
        (recur (next sds) (find-fn (first sds)))))))

