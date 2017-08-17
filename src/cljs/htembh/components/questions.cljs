(ns htembh.components.questions
  (:require [ajax.core :as ajax]
            [reagent.session :as session]
            [reagent.core :refer [atom]]
            [cognitect.transit :as t]))

(def r (t/reader :json))

(defn get-questions [topic]
  (ajax/GET "/api/questions/3"
            {
             ;; :headers {:Accept "applications/json"}
             ;; :format (ajax/json-request-format)
             ;; :response-format (ajax/json-response-format {:keywords? :true})
             ;; :response-format :json
             ;; :keywords? :true
             ;; :headers {:Accept :json}
             :handler #(session/put! :questions  %)
             }))

;; (defn get-questions [topic]
;;   (ajax/ajax-request {:uri "/questions/3"
;;                       :method :get
;;                       :format (ajax/json-request-format)
;;                       :response-format (json-response-format {:keywords? true})
;;                       :handler #(session/put! :questions %)}))


;; (defn get-questions [topic]
;;   (ajax/ajax-request
;;    {:uri "/questions/3"
;;     :method :get
;;     :headers {:Accept "application/json"}
;;     :handler #(session/put! :questions %)
;;     :format (ajax/json-request-format)
;;     :response-format (ajax/json-response-format {:keywords? true})} ))

(defn get-questions-btn []
  [:button.btn.btn-primary.btn-xl 
   {:on-click #(get-questions 1)}
   "get questions"])


;; (defn send-responses [checked errors]
;;   (ajax/POST "/responses" 
;;              {
;;               ;; :header {:Accept "application/json"}
;;               ;; :format (ajax/json-request-format)
;;               ;; :response-format (ajax/json-response-format {:keywords? true})
;;               :headers {:Content-Type "application/json"}
;;               :format :json
;;               :params {:responses @checked
;;                        :user (session/get :identity)}
;;               :handler #(.log js/console @checked)}))


(defn send-responses [checked errors]
  (ajax/POST "/api/responses"
             {
              ;; :header {:Content-Type "application/json"
              ;;          :Accept "application/json"}
              ;; :format (ajax/json-request-format)
              ;; :response-format (ajax/json-response-format)
              ;; :keyword? :true
              :params @checked
              :handler #(println (str %))
              :error-handler #(println @checked)}))
