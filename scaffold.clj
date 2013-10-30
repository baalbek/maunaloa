(ns maunaloa.scaffold
  (:import
    [org.springframework.beans.factory.xml XmlBeanFactory]
    [org.springframework.context.support ClassPathXmlApplicationContext]))

(defn gf [[xml]]
  (let [cur-xml (if (nil? xml)  "maunaloa.xml" xml)
        f ^XmlBeanFactory (XmlBeanFactory. (ClassPathXmlApplicationContext. cur-xml))]
    f))