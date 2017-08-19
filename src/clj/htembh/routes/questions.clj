(ns htembh.routes.questions
  (:require [htembh.db.core :as db]
            [ring.util.http-response :as response]
            [clj-time.local :as l]))


;; (def out (ByteArrayOutputStream. 4096))
;; (def writer (t/writer out :json))

;; (def in (ByteArrayInputStream. (.toByteArray out)))
;; (def reader (t/reader in :json))

;; (defn get-questions [topic-id]
;;   (-> 
;;    {:questions (db/get-questions {:topic topic-id})}
;;    (response/ok)))


;; (defn encode-transit [message]
;;   (let [out
;;         (java.io.ByteArrayOutputStream. 4096)
;;         writer (transit/writer out :json)]
;;     (transit/write writer message)
;;     (.toString out)))

;; (defn decode-transit [req]
;;   (let [in (java.io.ByteArrayInputStream. (.getBytes req))
;;         reader (transit/reader in :json)]
;;     (transit/read reader)))


(defn get-questions [topic-id]
  (db/get-questions {:topic topic-id}))

;; (defn get-questions [topic-id]
;;   (assoc {} :questions (db/get-questions {:topic topic-id})))

(defn get-answers [question-id]
  (db/get-answers {:question question-id}))

(defn get-response [id]
  (db/get-response {:id id}))

(defn get-questions&responses [topic-id]
  (map #(assoc % :responses (get-answers (:id %))) (get-questions topic-id)))

;; (defn get-q&r [topic-id]
;;   (-> {:questions (map #(assoc % :responses (get-answers (:id %))) (get-questions topic-id))}
;;      (response/ok)))

;; (defn get-q&r [topic-id]
;;   (reduce into '() (map #(assoc % :responses (get-answers (:id %))) (get-questions topic-id))))

;; (defn get-q&r [topic-id]
;;   (let [questions (get-questions topic-id)]
;;     (for [question questions]
;;       (assoc question :responses (get-answers (:id question))))))

;; (defn post-r [req]
;;   (do
;;     (let [body (slurp (:body req))]
;;       ;; (println (slurp (:body req)))
;;       (transit/write writer body)
;;       (prn (transit/read reader))))
;;   (-> {:result :ok}
;;      (response/ok)
;;      ))

(defn validate-num-responses [responses topic]
  (let [sent (count responses)
        min-num (count (db/get-questions {:topic topic}))]
    (= sent min-num)))


(defn save-responses [responses user topic]
  ;; (println responses user topic)
  (if (validate-num-responses responses topic)
    (let [points (int
                  (reduce +
                          (map #(:points %)
                               (db/calculate-points {:ids responses}))))
          date (l/local-now)]
      (try
        (db/create-user-response {:user user
                                  :topic topic
                                  :points points
                                  :date date})
        (-> {:result :ok}
           (response/ok))
        (catch Exception e
          {:result :error
           :message "Error saving response"})))
    (response/bad-request))
  )

;; (defn post-r [req topic]
;;   (let [body (slurp (:body req))]
;;       ;; (t/write writer body)
;;       ;; (println (t/read reader))
;;     ;; (println (get-response (first (:responses (parse-string body true)))))
;;     ;; (println (:body req))
;;     (println (:params req))
;;     ;; (println (decode-transit body))
;;     ;; (println (parse-string body true))
;;     ))
