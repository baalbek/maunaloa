(ns maunaloa.scaffold
  (:import
    [org.springframework.beans.factory.xml XmlBeanFactory]
    [org.springframework.core.io FileSystemResource]))

(defn get-factory []
  (let [f ^XmlBeanFactory (XmlBeanFactory. (FileSystemResource. "maunaloa.xml"))]
    f))