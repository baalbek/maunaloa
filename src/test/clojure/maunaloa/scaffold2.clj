(ns maunaloa.scaffold2)

(defprotocol PP
  (foo [bar] [bar baz]))

(extend-protocol PP
        Object
        (foo 
          ([bar] 1)
          ([bar baz] 2)))
