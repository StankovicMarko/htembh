(ns htembh.components.results
  (:require [ajax.core :as ajax]
            [reagent.session :as session]
            [reagent.core :refer [atom]]))

(def chart-config
  {:chart {:type "column"}
   :title {:text "Results of Nutrition and Lifestyle Questionnaire"}
   :subtitle {:text "Source: Your Latest Answers"}
   :xAxis {:categories ["You Are What You Eat" "Stress" "Sleep Wake Cycles"
                        "You Are When You Eat" "Digestion" "Fungus & Parasites" "TOTAL"]
           :title {:text nil}}
   :yAxis {:min 0
           :title {:text "Points"
                   :align "high"}
           :labels {:overflow "justify"}}
   :tooltip {:valueSuffix " points"}
   :plotOptions {:column {:dataLabels {:enabled true}}}
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


(defn get-results [email errors]
  (ajax/GET "/api/results" 
            {
             :headers {"email" email}
             :handler #(session/put! :highcharts-data %)
             :error-handler #(reset! errors (:message (:response %)))
             }))

(defn get-results-btn [errors]
  [:button.btn.btn-default.btn-xl 
   {:on-click #(get-results (session/get :identity) errors)}
   "Get Results"])


;;;;;;;;;; PPD results

;; (def ppd
;;   {:chart {:type "pie"}
;;    :title {:text "You are: Polar Type"}
;;    :subtitle {:text "Primal Pattern Diet Results. Source: Your Latest Answers"}
;;    :tooltip {:pointFormat "{series.name}: <b>{point.percentage:.f}%</b>"}
;;    :plotOptions {:pie {:dataLabels {:enabled true
;;                                     :format "<b>{point.name}</b>: {point.percentage:.f} %"
;;                                     :style {:color "black"}}}}
;;    :series [{:name "Type of Food"
;;              :colorByPoint true
;;              :data [{:name "Carbohydrates"
;;                      :y 35
;;                      :color "green"}
;;                     {:name "Protein"
;;                      :y 45
;;                      :color "red"}
;;                     {:name "Oils/Fats"
;;                      :y 20
;;                      :color "yellow"}]}]})


(def ppd-charts [
                 {:chart {:type "pie"}
                  :title {:text "You are: Equatorial Type"}
                  :subtitle {:text "Primal Pattern Diet Results based on: Your Latest Answers"}
                  :tooltip {:pointFormat "{series.name}: <b>{point.percentage:.f}%</b>"}
                  :plotOptions {:pie {:dataLabels {:enabled true
                                                   :format "<b>{point.name}</b>: {point.percentage:.f} %"
                                                   :style {:color "black"
                                                           :fontSize "1.2em"}}}}
                  :series [{:name "Meal Portion"
                            :colorByPoint true
                            :data [{:name "Carbohydrates"
                                    :y 70
                                    :color "green"}
                                   {:name "Protein"
                                    :y 20
                                    :color "red"}
                                   {:name "Oils/Fats"
                                    :y 10
                                    :color "yellow"}]}]}

                 {:chart {:type "pie"}
                  :title {:text "You are: Variable Type"}
                  :subtitle {:text "Primal Pattern Diet Results based on: Your Latest Answers"}
                  :tooltip {:pointFormat "{series.name}: <b>{point.percentage:.f}%</b>"}
                  :plotOptions {:pie {:dataLabels {:enabled true
                                                   :format "<b>{point.name}</b>: {point.percentage:.f} %"
                                                   :style {:color "black"
                                                           :fontSize "1.2em"
                                                           }}}}
                  :series [{:name "Meal Portion"
                            :colorByPoint true
                            :data [{:name "Carbohydrates"
                                    :y 50
                                    :color "green"}
                                   {:name "Protein"
                                    :y 40
                                    :color "red"}
                                   {:name "Oils/Fats"
                                    :y 10
                                    :color "yellow"}]}]}

                 {:chart {:type "pie"}
                  :title {:text "You are: Polar Type"}
                  :subtitle {:text "Primal Pattern Diet Results based on: Your Latest Answers"}
                  :tooltip {:pointFormat "{series.name}: <b>{point.percentage:.f}%</b>"}
                  :plotOptions {:pie {:dataLabels {:enabled true
                                                   :format "<b>{point.name}</b>: {point.percentage:.f} %"
                                                   :style {:color "black"
                                                           :fontSize "1.2em"}}}}
                  :series [{:name "Meal Portion"
                            :colorByPoint true
                            :data [{:name "Carbohydrates"
                                    :y 35
                                    :color "green"}
                                   {:name "Protein"
                                    :y 45
                                    :color "red"}
                                   {:name "Oils/Fats"
                                    :y 20
                                    :color "yellow"}]}]}
                 

                 ])


(defn get-ppd-results [email errors]
  (ajax/GET "/api/results/ppd" 
            {
             :headers {"email" email}
             :handler #(session/put! :highcharts-ppd-data (:graph-id %))
             :error-handler #(reset! errors (:message (:response %)))
             }))


(defn get-ppd-results-btn [errors]
  [:button.btn.btn-default.btn-xl 
   {:on-click #(get-ppd-results (session/get :identity) errors)}
   "Get Results"])

