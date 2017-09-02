(ns htembh.components.questions
  (:require [ajax.core :as ajax]
            [reagent.session :as session]
            [reagent.core :refer [atom]]))

(def topic-num (atom 1))

;; - idea to scroll up after clicking next
;;try adding this to whatever function you are calling to click next
(defn scroll-to-top []
 (.scroll js/window 0 0))

(defn get-questions [topic]
  (ajax/GET (str "/api/questions/" topic)
            {
             :handler #(do
                         (scroll-to-top)
                         (session/put! :questions  %)
                           )
             :error-handler #(println %)
             }))

(defn get-topic-name [topic]
  (ajax/GET (str "/api/topic/" topic)
            {:handler #(session/put! :topic-name %)
             :error-handler #(println %)}))


(defn get-questions-btn []
  [:button.btn.btn-default.btn-xl 
   {:on-click #(do (get-questions @topic-num)
                   (get-topic-name @topic-num))}
   "get questions"])

(defn get-next-questions []
  (do
    (session/remove! :questions)
    (if (<= @topic-num 6)
      (get-questions @topic-num))))


(defn get-next-topic []
  (if (<= @topic-num 6)
    (do
      (session/remove! :topic-name)
      (get-topic-name @topic-num))))


(defn reset-topic-num []
  (if (>= @topic-num 6)
    (do
      (session/remove! :questions)
      (reset! topic-num 1))
    (do (swap! topic-num inc)
        (get-next-questions)
        (get-next-topic))))

(defn send-responses [checked errors topic]
  (ajax/POST (str "/api/responses/" topic)
             {
              :params {:responses (vec (map #(second %) @checked))
                       :user (session/get :identity)}
              :handler #(do
                          (reset! checked
                                  {:responses []
                                   :user (session/get :identity)})
                          (reset! errors nil)
                          (reset-topic-num)
                          
                          )
              :error-handler #(reset! errors "There was a problem with server")
              }))



(defn swap-checked-val [checked name val]
  (swap! checked assoc name val))

(defn print-responses [responses q-id checked]
  (fn []
    [:div
     (for [{:keys [id text points]} responses]
       ^{:key id}
       [:div.li [:input {:type :radio
                         :name  q-id
                         :value id
                         :id id
                         :on-change
                         #(swap-checked-val checked
                                            (-> % .-target .-name)
                                            (int (->  % .-target .-value)))
                         }]
        [:label {:for id} (str text)]
        [:div.check
         [:div.inside]]])]))



(defn validate-num-responses [checked errors min-num]
  (let [clicked (count @checked)]
    (if (not= clicked min-num)
      (reset! errors "All questions must be answered")
      (send-responses checked errors @topic-num))))

(defn questions-form [questions]
  (let [checked (atom {})
        errors (atom nil)]
    (fn []
      [:div.container
       (when-let [topic-name (:name (session/get :topic-name))]
         [:h1 topic-name])
       (for [{:keys [id text responses]}  questions]
         ^{:key id}
         [:div.container
          [:h2 text]
          [:div.ul
           [print-responses responses id checked]]])
       (when-let [error @errors]
         [:div.alert.alert-danger error])
       [:button.btn.btn-default
        {:on-click #(if (not= (session/get :identity) nil)
                      (validate-num-responses checked errors (count questions))
                      (reset! errors "You must login first"))}
        "send responses"]])))


