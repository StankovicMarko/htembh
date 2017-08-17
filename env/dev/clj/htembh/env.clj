(ns htembh.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [htembh.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[htembh started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[htembh has shut down successfully]=-"))
   :middleware wrap-dev})
