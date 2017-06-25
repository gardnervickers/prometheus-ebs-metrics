 (ns user
   (:require
     [tubular.core]))

(defn connect []
  (tubular.core/connect "127.0.0.1" 5555))