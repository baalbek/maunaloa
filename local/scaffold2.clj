(ns scaffold2
  (:import
    [vega.filters Common]
    [vega.filters.ehlers Itrend CyberCycle]
    [java.time.temporal IsoFields]
    [org.springframework.context.support ClassPathXmlApplicationContext]
    [java.util Collections]
    [java.time LocalDate DayOfWeek])
  (:require
    [vega.financial.calculator.BlackScholes :as bs])
  (:use
    [clojure.string :only [split]]
    [clojure.algo.monads :only [domonad maybe-m]]))

(def itr (Itrend.))
(def cc (CyberCycle.))

(def cp bs/call-price)

(defmacro j1 [f1] `(fn [v#] (~f1 v#)))

(defmacro j2 [f1 f2] `(fn [v#] [(~f1 v#) (~f2 v#)]))

(defmacro j3 [f1 f2 f3] `(fn [v#] [(~f1 v#) (~f2 v#) (~f3 v#)]))

(def dx (LocalDate/of 2012 1 1))

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

(defn sp [ticker] (.findStockPrices (srepos) ticker dx))



;(def d0 (-> (last yar) .getDx .toLocalDate))

(def d0 (LocalDate/of 2014 8 1))

(def d1 (LocalDate/of 2015 2 5))

(def d2 (LocalDate/of 2014 10 22))

(defn week-of [dx]
  (.get dx IsoFields/WEEK_OF_WEEK_BASED_YEAR))


(defmacro in? [v items]
  `(some #(= ~v %) ~items))

(defn flatten-1
  [x]
  (filter #(and (sequential? %) (not-any? sequential? %))
    (rest (tree-seq #(and (sequential? %) (some sequential? %)) seq x))))


(defn correct-date? [d m]
  (let [short-months [4 6 9 11]]
    (cond
      (in? m short-months) (<= d 30)
      (= m 2) (<= d 28)
      :else true)))

(defn all-days [y]
  (fn [m]
    (for [dx (range 1 32) :when (correct-date? dx m)]
      [y m dx])))

(defn full-year [y]
  (for [mx (range 1 13) dx (range 1 32) :when (correct-date? dx mx)] [y mx dx]))

(defn pm_ [range-fn rr y m d]
  (for [dx (range-fn d rr) :when (correct-date? dx m)]
    [y m dx]))

(def month-end (partial pm_ drop (range 32)))

(def month-begin (partial pm_ take (range 1 32)))

(defn year-end  [y m d]
  (let [a (month-end y m d)
        b (for [mx (range (+ m 1) 13)
                dx (range 1 32) :when (correct-date? dx mx)]
            [y mx dx])]
    (concat a b)))

(defn year-begin [y m d]
  (let [a (for [mx (range 1 m)
                dx (range 1 32) :when (correct-date? dx mx)]
            [y mx dx])
        b (month-begin y m d)]
    (concat a b)))

(defn items-between-dates [from-date to-date]
  (let [pfn
        (fn [v]
          [
            (.getYear v)
            (-> v .getMonth .getValue)
            (.getDayOfMonth v)
            ])
        [y1 m1 d1] (pfn from-date)
        [y2 m2 d2] (pfn to-date)
        items (cond
                (and (= y1 y2) (= m1 m2) (= d1 d2))
                [[y1 m1 d1]]
                (and (= y1 y2) (= m1 m2))
                (let [days (range d1 (+ d2 1))]
                  (for [d days]
                    [y1 m1 d]))
                (= y1 y2)
                (let [months (drop 1 (range m1 m2))
                      a (month-end y1 m1 d1)
                      b (flatten-1 (map (all-days y1) months))
                      c (month-begin y2 m2 d2)]
                  (concat a b c))
                :else
                (let [years (drop 1 (range y1 y2))
                      a (year-end y1 m1 d1)
                      b (flatten-1 (map full-year years))
                      c (year-begin y2 m2 d2)]
                  (concat a b c)))]
    items))

(defn search-dates [head-date]
  (let [d1 (.adjustInto DayOfWeek/FRIDAY head-date)
        d0 (.minusWeeks d1 2)
        date-nums (items-between-dates d0 d1)]
    (rseq (vec date-nums))))


(defn binary-search-dx [dxx [year month day]]
  (let [loc-d (LocalDate/of year month day)
         result (Collections/binarySearch dxx loc-d compare)]
    (println "Checking for " loc-d)
    (if (< result 0)
      nil
      result)))

(defn find-dx-index [dxx head-date]
  (let [find-fn (partial binary-search-dx dxx)]
    (loop [sds (search-dates head-date)
           result nil]
      (if (or (nil? sds) (not= nil result))
        result
        (recur (next sds) (find-fn (first sds)))))))

(comment find-dx [dxx head-date]
  (let [sds (search-dates head-date)
        find-fn (partial binary-search-dx dxx)]
    (or (map find-fn sds))))


(comment sieve [s]
  (swap! counter inc)
  (cons (first s)
    (lazy-seq (sieve (filter #(not= 0 (mod % (first s)))
                       (rest s))))))


(comment
  (defn find-dx-in-week [dxx d]
    (or
      (binary-search-dx dxx d DayOfWeek/FRIDAY)
      (binary-search-dx dxx d DayOfWeek/THURSDAY)
      (binary-search-dx dxx d DayOfWeek/WEDNESDAY)
      (binary-search-dx dxx d DayOfWeek/TUESDAY)
      (binary-search-dx dxx d DayOfWeek/MONDAY)))

  (defn year-weeks [y w])

  (defn find-dx [dxx d]
    (let [find-fn (partial find-dx-in-week dxx)
          first-year (.getYear (first dxx))
          cur-year (.getYear d)
          cur-week (.get d IsoFields/WEEK_OF_WEEK_BASED_YEAR)]))
  )

  (def yar (sp "YAR"))

  (def yarx (map #(.getValue %) yar))

  (def yard (map #(.getLocalDx %) yar))

  ;(Collections/binarySearch (map #(.getLocalDx %) spots) (.adjustInto java.time.DayOfWeek/THURSDAY d2) compare)

(comment
  (defn calc-alpha [days]
    (/ 2.0 (+ days 1.0)))


  (defn calc-smooth [data]
    (loop [result (vec (take 4 data))
           datax (drop 1 data)]
      (let [[a b c d] (take 4 datax)]
        (if (nil? d)
          result
          (recur (conj result (/ (+ a (* 2.0 b) (* 2.0 c) d) 6.0)) (rest datax))))))

  (defn ave [lx, ^Double cc-val, ^Double f1, ^Double f2]
    (let [[a b] (take 2 lx)
          c (* f2 cc-val)
          d (* f1 (- b a))]
      (+ c d)))

  (defn calc-cycle-fn [^Double f12, ^Double f2, ^Double f22]
    (fn [^Double a,
         ^Double b,
         ^Double c,
         ^Double d,
         ^Double e]
      (let [data-val (* f12 (- (+ e c) (* 2.0 d)))
            cycle-val (- (* 2.0 f2 b) (* f22 a))]
        (+ data-val cycle-val))))

  (defn cybercycle [data, ^Integer days]
    ;{:pre [(>= (count data) 20)]
    ;:post [= (count %) (count data)]}
    (let [
           alpha (calc-alpha days)
           smooth (calc-smooth data)
           f1 (- 1.0 (* alpha 0.5))
           f12 (* f1 f1)
           f2 (- 1.0 alpha)
           f22 (* f2 f2)
           cycle (loop [result [0.0] sx (take 20 smooth)]
                   (if (< (count sx) 2)
                     result
                     (recur (conj result (ave sx (last result) f1 f2)) (rest sx))))
           [a b] (drop (- (count cycle) 2) cycle)
           calc-cycle (calc-cycle-fn f12 f2 f22)
           result (loop [cyclex cycle, datax (drop 18 smooth), ax a, bx b]
                    (let [[c d e] (take 3 datax)]
                      (if (nil? e)
                        cyclex
                        (let [cycle-val (calc-cycle ax bx c d e)]
                          (recur (conj cyclex cycle-val) (rest datax) bx cycle-val)))))
           ]
      result))

  (def yarx (map #(.getValue %) (sp "YAR")))

  (def cc1 (cc yarx 200))
  (def cc2 (cybercycle yarx 200))
  )
