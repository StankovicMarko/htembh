(ns htembh.routes.auth
  (:require [htembh.db.core :as db]
            [ring.util.http-response :as response]
            [buddy.hashers :as hashers]
            [clojure.tools.logging :as log]
            [htembh.validation :refer [registration-errors]]
            [clj-time.local :as l]
            [clj-time.jdbc]))

(defn handle-registration-error [e]
  (if (-> e (.getMessage)
         (.startsWith "duplicate entry"))
    (response/precondition-failed
     {:result :error
      :message "user with the selected email already exists"})
    (do
      (log/error e)
      (response/internal-server-error
       {:result :error
        :message "server error occurred while adding the user"}))))


(defn register! [{:keys [session]} user]
  (if (registration-errors user)
    (response/precondition-failed {:result :error})
    (try
      (db/create-user!
       (-> user
          (dissoc :pass-confirm)
          (assoc :registered (l/local-now))
          (update :pass hashers/encrypt)))
      (-> {:result :ok}
         (response/ok)
         (assoc :session (assoc session :identity (:email user))))
      (catch Exception e
        (handle-registration-error e)))))


(defn decode-auth [encoded]
  (let [auth (second (.split encoded " "))]
    (-> (.decode (java.util.Base64/getDecoder) auth)
       (String. (java.nio.charset.Charset/forName "UTF-8"))
       (.split ":"))))

(defn authenticate [[email pass]]
  (when-let [user (db/get-user {:email email})]
    (when (hashers/check pass (:pass user))
      email)))

(defn login! [{:keys [session]} auth]
  (if-let [email (authenticate (decode-auth auth))]
    (-> {:result :ok}
       (response/ok)
       (assoc :session (assoc session :identity email)))
    (response/unauthorized {:result :unauthorized
                            :message "login failure"})))

(defn logout! []
  (-> {:result :ok}
     (response/ok)
     (assoc :session nil)))


