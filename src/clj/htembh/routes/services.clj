(ns htembh.routes.services
  (:require [ring.util.http-response :refer :all]
            [compojure.api.sweet :refer :all]
            [schema.core :as s]
            [compojure.api.meta :refer [restructure-param]]
            [buddy.auth.accessrules :refer [restrict]]
            [buddy.auth :refer [authenticated?]]
            [htembh.routes.auth :as auth]
            [htembh.routes.questions :as qs]
            [htembh.routes.results :as res]))

(defn access-error [_ _]
  (unauthorized {:error "unauthorized"}))

(defn wrap-restricted [handler rule]
  (restrict handler {:handler  rule
                     :on-error access-error}))

(defmethod restructure-param :auth-rules
  [_ rule acc]
  (update-in acc [:middleware] conj [wrap-restricted rule]))

(defmethod restructure-param :current-user
  [_ binding acc]
  (update-in acc [:letks] into [binding `(:identity ~'+compojure-api-request+)]))



(s/defschema UserRegistration {:email String
                               :pass String
                               :pass-confirm String
                               })


(s/defschema Result
  {:result s/Keyword
   (s/optional-key :message) String
   (s/optional-key :questions) String
   })

(defapi service-routes
  {:swagger {:ui "/swagger-ui"
             :spec "/swagger.json"
             :data {:info {:version "1.0.0"
                           :title "Sample API"
                           :description "Sample Services"}}}}
  
  (GET "/authenticated" []
       :auth-rules authenticated?
       :current-user user
       (ok {:user user}))
  (context "/api" []
    :tags ["API"]

    ;; (GET "/plus" []
    ;;      :return       Long
    ;;      :query-params [x :- Long, {y :- Long 1}]
    ;;      :summary      "x+y with query-parameters. y defaults to 1."
    ;;      (ok (+ x y)))

    ;; (POST "/minus" []
    ;;       :return      Long
    ;;       :body-params [x :- Long, y :- Long]
    ;;       :summary     "x-y with body-parameters."
    ;;       (ok (- x y)))
    
    ;; (GET "/times/:x/:y" []
    ;;      :return      Long
    ;;      :path-params [x :- Long, y :- Long]
    ;;      :summary     "x*y with path-parameters"
    ;;      (ok (* x y)))
    
    ;; (POST "/divide" []
    ;;       :return      Double
    ;;       :form-params [x :- Long, y :- Long]
    ;;       :summary     "x/y with form-parameters"
    ;;       (ok (/ x y)))
    
    ;; (GET "/power" []
    ;;      :return      Long
    ;;      :header-params [x :- Long, y :- Long]
    ;;      :summary     "x^y with header-parameters"
    ;;      (ok (long (Math/pow x y))))

    (POST "/register" req
          :return Result
          :body [user UserRegistration]
          :summary "register a new user"
          (auth/register! req user))

    (POST "/login" req
          :header-params [authorization :- String]
          :summary "login the user and create a session"
          :return Result
          (auth/login! req authorization))

    (POST "/logout" []
          :summary "remove user session"
          :return Result
          (auth/logout!))
    
    (GET "/questions/:topic" []
         :summary "get questions by topic id"
         :path-params [topic :- String]
         (qs/get-questions&responses (read-string topic)))
  
    (POST "/responses/:topic" [responses email topic]
          :summary "posting user responses"
          :body [rq s/Any]
          ;; :path-params [topic :- Long]
          ;; :return Result
          (qs/save-responses responses email (read-string topic)))

    (GET "/topic/:topic" [topic]
         :summary "returns name of topic"
         (ok (qs/get-topic-name (read-string topic))))

    (GET "/results" [email]
         :header-params [email :- String]
         :summary "returns his last results"
         (res/highcharts-data email))

    (GET "/results/ppd" [email]
         :header-params [email :- String]
         :summary "returns his last results"
         (res/ppd-results email))
    
))
