(ns htembh.routes.results
  (:require [htembh.db.core :as db]
            [clj-time.format :as f]
            [clj-time.local :as l]
            [clj-time.core :as t]
            [ring.util.http-response :as response]
            [clojure.set :refer [rename-keys]]))

(def built-in-formatter (f/formatters :basic-date-time))
(def custom-formatter (f/formatter "dd-MM-yyyy"))

(def points-ranges [
                    {:topic 1
                     :green 30
                     :yellow 50}
                    {:topic 2
                     :green 20
                     :yellow 40}
                    {:topic 3
                     :green 30
                     :yellow 50}
                    {:topic 4
                     :green 10
                     :yellow 20}
                    {:topic 5
                     :green 20
                     :yellow 40}
                    {:topic 6
                     :green 40
                     :yellow 60}
                    {:topic "TOTAL"
                     :green 150
                     :yellow 260}
                    ])


(defn get-last-ones [seq]
  (loop [final-six [] batch seq]
    (if (= 6 (count final-six))
      final-six
      (let [topic-id (:topic (first batch))]
        (if (empty? (filter #(= topic-id (:topic %)) final-six))
          (recur (conj final-six (first batch)) (rest batch))
          (recur final-six (rest batch)))))))


(defn get-results [email]
  ;;;gets all results from db and removes topic 7 which is another(ppd) questionnaire
  (get-last-ones (filter #(not= 7 (:topic %)) (db/get-results {:email email}))))


(defn unparse-date [email]
  (map #(assoc % :date
               (f/unparse custom-formatter (:date %)))
       (sort-by :topic (get-results email))))


(defn add-color [map color]
  (assoc map :color color))


(defn calc-color [map]
  (let [p-range (first (filter #(= (:topic %) (:topic map)) points-ranges))]
    (let [res-p (:points map)
          green (:green p-range)
          yellow (:yellow p-range)]
      (cond
        (< res-p green) (add-color map "green")
        (and (>= res-p green) (< res-p yellow)) (add-color map "yellow")
        :else (add-color map "red")))))

(defn make-total [seq]
  (conj (vec seq) {:topic "TOTAL"
                   :points (reduce + (map #(:points %) seq))}))

(defn map-color [email]
  (let [without-total (unparse-date email)]
    (let [with-total (make-total without-total)]
      (map #(calc-color %) with-total))))


(defn transform-data [results]
  (vec (map #(assoc (assoc {} :y (:points %)) :color (:color %)) results)))


(defn verify-completed-qs [email]
  (when-let [available (map #(:topic %) (db/topics-user-results {:email email}))]
    (= [1 2 3 4 5 6] available)))

(defn highcharts-data [email]
  (if-let [user (db/get-user {:email email})]
    (if (verify-completed-qs email)
      (response/ok
       (let [results (map-color email)]
         (assoc (assoc {} :name (:date (first results))) :data (transform-data results))))
      (response/precondition-failed {:result :error
                                       :message "You must answer all questions before getting results"}))
    (response/unauthorized {:result :error
                            :message "You must be logged in"})))


;;;;;;; ppd results

(defn return-which-graph [points]
  (cond
    (<= points -3) 0
    (and (>= points -2) (<= points 2)) 1
    (>= points 3) 2))

(defn ppd-results [email]
  (if-let [user (db/get-user {:email email})]
    (if-let [result (first (reverse (sort-by :date (filter #(= 7 (:topic %)) (db/get-results {:email email})))))]
      (response/ok
       (let [graph-id (return-which-graph (:points result))]
         (assoc {} :graph-id graph-id)))
      (response/precondition-failed {:result :error
                                     :message "You must answer all questions before getting results"}))
    (response/unauthorized {:result :error
                            :message "You must be logged in"})))
