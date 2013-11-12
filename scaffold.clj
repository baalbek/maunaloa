(ns maunaloax.scaffold
  (:import
    [org.springframework.beans.factory.xml XmlBeanFactory]
    [org.springframework.context.support ClassPathXmlApplicationContext]))

(defn gf [[xml]]
  (let [cur-xml (if (nil? xml)  "maunaloa.xml" xml)
        f ^XmlBeanFactory (XmlBeanFactory. (ClassPathXmlApplicationContext. cur-xml))]
    f))

(def jada 
  (memoize 
    (fn [a & [b c]] 
      (let [bx (if (nil? b) 1 b)
            cx (if (nil? c) 1 c)]
        (println (str "a: " a ", b: " b ", c: " c))
        (+ a bx cx)))))

(def joda 
  (memoize 
    (fn [a b
         & [{:keys [c d] :or {c 0 d 0}}]]
      (do
        (println (str a "," b "," c "," d))
        (+ a b c d)))))
            
(println "Not mem: " (joda 1 2))

(println "Mem: " (joda 1 2))

(println "Not mem: "(joda 1 2 {:c 10}))

(println "Mem: " (joda 1 2 {:c 10}))

(println "Not mem: "(joda 1 2 {:c 10 :d 20}))

(println "Mem: " (joda 1 2 {:c 10 :d 20}))

(println "Not mem: " (joda 1 2 {:d 20}))

(println "Mem: " (joda 1 2 {:d 20}))
