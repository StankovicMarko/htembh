(ns htembh.handler
  (:require [compojure.core :refer [routes wrap-routes]]
            [htembh.layout :refer [error-page]]
            [htembh.routes.home :refer [home-routes]]
            [htembh.routes.services :refer [service-routes]]
            [compojure.route :as route]
            [htembh.env :refer [defaults]]
            [mount.core :as mount]
            [htembh.middleware :as middleware]))

(mount/defstate init-app
                :start ((or (:init defaults) identity))
                :stop  ((or (:stop defaults) identity)))

(def app-routes
  (routes
    (-> #'home-routes
        (wrap-routes middleware/wrap-csrf)
        (wrap-routes middleware/wrap-formats))
    #'service-routes
    (route/not-found
      (:body
        (error-page {:status 404
                     :title "page not found"})))))


(defn app [] (middleware/wrap-base #'app-routes))
