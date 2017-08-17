(ns htembh.components.registration
  (:require [reagent.core :refer [atom]]
            [reagent.session :as session]
            [htembh.components.common :as c]
            [htembh.validation :refer [registration-errors]]
            [ajax.core :as ajax]
            ))


(defn register! [fields errors]
  (reset! errors (registration-errors @fields))
  (when-not @errors
    (ajax/POST "/api/register"
               {:params @fields
                :handler #(do
                            (session/put! :identity (:email @fields))
                            (reset! fields {})
                            (session/remove! :modal))
                :error-handler #(reset!
                                 errors
                                 {:server-error (get-in % [:response :message])})})))


(defn registration-form []
  (let [fields (atom {})
        error (atom nil)]
    (fn []
      [c/modal
       [:div "User Registration"]
       [:div
        [:div.well.well-sm
         [:strong "✱ Required Field"]]
        [c/text-input "Username" :email "enter a email" fields]
        (when-let [error (first (:email @error))]
          [:div.alert.alert-danger error])
        [c/password-input "Password" :pass "enter a password" fields]
        (when-let [error (first (:pass @error))]
          [:div.alert.alert-danger error])
        [c/password-input "Password" :pass-confirm "repeat password" fields]
        (when-let [error (:server-error @error)]
          [:div.alert.alert-danger error])]
       [:div
        [:button.btn.btn-primary
         {:on-click #(register! fields error)} "Register" ]
        [:button.btn.btn-danger
         {:on-click #(session/remove! :modal)} "Cancel"]]])))


(defn registration-button []
  [:a.btn
   {:on-click #(session/put! :modal registration-form)}
   "Register"])
