(ns htembh.components.questions
  (:require [ajax.core :as ajax]
            [reagent.session :as session]
            [reagent.core :refer [atom]]))


(defn get-questions [topic]
  (ajax/GET "/api/questions/3"
            {
             :handler #(session/put! :questions  %)
             :error-handler #(println %)
             }))

(defn get-questions-btn []
  [:button.btn.btn-primary.btn-xl 
   {:on-click #(get-questions 1)}
   "get questions"])

(defn send-responses [checked errors]
  (ajax/POST "/api/responses"
             {
              :params @checked
              :handler #(do
                          (reset! checked
                                  {:responses []
                                   :user (session/get :identity)})
                          (reset! errors nil))
              :error-handler #(println @checked)}))



(defn swap-checked-val [checked val]
  (swap! checked assoc :responses (conj (:responses @checked) val)))

(defn print-responses [responses q-id checked]
  (for [{:keys [id text points]} responses]
    ^{:key id}
    [:p [:input {:type :radio
                 :name  q-id
                 :value id
                 :on-change #(swap-checked-val checked
                                               (int (->  % .-target .-value)))
                 }]
     (str text)]))


(defn validate-num-responses [checked errors min-num]
  (let [clicked (count (:responses @checked))]
    (if (not= clicked min-num)
      (reset! errors "All questions must be answered")
      (send-responses checked errors))))

(defn questions-form [questions]
  (let [checked (atom {:responses []
                         :user (session/get :identity)})
        errors (atom nil)]
    (fn []
      [:div.container
       (for [{:keys [id text responses]}  questions]
         ^{:key id}
         [:div.row
          [:label [:strong text]]
          (print-responses responses id checked)
          ])
       (when-let [error @errors]
         [:div.alert.alert-danger error])
       [:button.btn.btn-primary
        {:on-click #(validate-num-responses checked errors (count questions))}
        "send responses"]])))


