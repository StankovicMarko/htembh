(ns htembh.routes.questions
  (:require [htembh.db.core :as db]
            [ring.util.http-response :as response]
            [cognitect.transit :as transit]
            [cheshire.core :refer :all])
  (:import [java.io ByteArrayInputStream ByteArrayOutputStream]))


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

(defn get-q&r [topic-id]
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


(defn post-r [req]
  (let [body (slurp (:body req))]
      ;; (t/write writer body)
      ;; (println (t/read reader))
    ;; (println (get-response (first (:responses (parse-string body true)))))
    ;; (println (:body req))
    (println (:params req))
    ;; (println (decode-transit body))
    ;; (println (parse-string body true))
    ))
