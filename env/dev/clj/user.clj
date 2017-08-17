(ns user
  (:require [mount.core :as mount]
            [htembh.figwheel :refer [start-fw stop-fw cljs]]
            htembh.core))

(defn start []
  (mount/start-without #'htembh.core/repl-server))

(defn stop []
  (mount/stop-except #'htembh.core/repl-server))

(defn restart []
  (stop)
  (start))


