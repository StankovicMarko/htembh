(ns htembh.components.results
  (:require [ajax.core :as ajax]
            [reagent.session :as session]
            [reagent.core :refer [atom]]))

(def chart-config
  {:chart {:type "column"}
   :title {:text "Results of all your scores"}
   :subtitle {:text "Source: Your answers"}
   :xAxis {:categories ["You Are What You Eat" "Stress" "Sleep Wake Cycles"
                        "You Are When You Eat" "Digestion" "Fungus & Parasites" "TOTAL"]
           :title {:text nil}}
   :yAxis {:min 0
           :title {:text "Points"
                   :align "high"}
           :labels {:overflow "justify"}}
   :tooltip {:valueSuffix " points"}
   :plotOptions {:bar {:dataLabels {:enabled true}}}
   :legend {:layout "vertical"
            :align "center"
            :verticalAlign "top"
            :x -40
            :y 100
            :floating true
            :borderWidth 1
            :shadow true}
   :credits {:enabled false}
   :series [
            ]})


(defn get-results [email]
  (ajax/GET "/api/results" 
            {
             :headers {"email" email}
             :handler #(session/put! :highcharts-data %)
             :error-handler #(println (str "error" %))
             }))

(defn get-results-btn []
  [:button.btn.btn-default.btn-xl 
   {:on-click #(do (get-results (session/get :identity)))}
   "Get Results"])




