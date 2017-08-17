(ns htembh.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[htembh started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[htembh has shut down successfully]=-"))
   :middleware identity})
