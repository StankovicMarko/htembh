(ns htembh.validation
  (:require [struct.core :as st]
            [bouncer.core :as b]
            [bouncer.validators :as v]))

(defn registration-errors [{:keys [pass-confirm] :as params}]
  (first
   (b/validate
    params
    :email [v/required
            v/email]
    :pass [v/required
           [v/min-count 7 :message "Password must contain at least 8 characters"]
           [= pass-confirm :message "Re-entered password does not match"]])))


