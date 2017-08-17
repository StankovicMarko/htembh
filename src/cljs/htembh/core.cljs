(ns htembh.core
  (:require [reagent.core :as r]
            [reagent.session :as session]
            [secretary.core :as secretary :include-macros true]
            [goog.events :as events]
            [goog.history.EventType :as HistoryEventType]
            [markdown.core :refer [md->html]]
            [htembh.ajax :refer [load-interceptors!]]
            [ajax.core :refer [GET POST]]
            [htembh.components.registration :as reg]
            [htembh.components.login :as l]
            [htembh.components.questions :as qs])
  (:import goog.History))

(defn user-menu []
  (if-let [id (session/get :identity)]
    [:ul.nav.navbar-nav.pull-xs-right
     [:li.nav-item
      [:a.dropdown-item.btn
       {:on-click #(POST
                    "/api/logout"
                    {:handler (fn [] (session/remove! :identity))})}
       [:i.fa.fa-user] " " id " | sign out"]]]
    [:ul.nav.navbar-nav.pull-xs-right
     [:li.nav-item [l/login-button]]
     [:li.nav-item [reg/registration-button]]]))

;; (defn nav-link [uri title page collapsed?]
;;   [:li.nav-item
;;    {:class (when (= page (session/get :page)) "active")}
;;    [:a.nav-link
;;     {:href uri
;;      :on-click #(reset! collapsed? true)} title]])

;; (defn navbar []
;;   (let [collapsed? (r/atom true)]
;;     (fn []
;;       [:nav.navbar.navbar-dark.bg-primary
;;        [:button.navbar-toggler.hidden-sm-up
;;         {:on-click #(swap! collapsed? not)} "☰"]
;;        [:div.collapse.navbar-toggleable-xs
;;         (when-not @collapsed? {:class "in"})
;;         [:a.navbar-brand {:href "#/"} "htembh"]
;;         [:ul.nav.navbar-nav
;;          [nav-link "#/" "Home" :home collapsed?]
;;          [nav-link "#/about" "About" :about collapsed?]]]])))

(defn nav-link [uri title page collapsed?]
  [:ul.nav.navbar-nav>a.navbar-brand
   {:class (when (= page (session/get :page)) "active")
    :href uri
    :on-click #(reset! collapsed? true)}
   title])

(defn navbar []
  (let [collapsed? (r/atom true)]
    (fn []
      [:nav.navbar.navbar-light.bg-faded
       [:button.navbar-toggler.hidden-sm-up
        {:on-click #(swap! collapsed? not)} "☰"]
       [:div.collapse.navbar-toggleable-xs
        (when-not @collapsed? {:class "in"})
        [:a.navbar-brand {:href "/"} "htembh"]
        [:ul.nav.navbar-nav
         [nav-link "#/htembh" "HTMBH" :htembh collapsed?]
         [nav-link "#/ppd" "Primal Pattern Diet" :ppd collapsed?]
         [nav-link "#/about" "About This Project" :about collapsed?]]
        [user-menu]]])))

(defn ppd-page [] ;;;about page 
  [:div.container
   [:div.row
    [:div.col-md-12
     "this is the story of htembh... work in progress"]]])

(defn htembh-page []
  [:div.container
   (qs/get-questions-btn)])

(defn about-page []
  [:div.container
   [:div.row
    [:div.col-md-12
     "This is fan project. HTEMBH(link to amazon) changed my life.
The reason i wanted to make this is to make questions more accesable
to everyone. Sometimes it bothered me when i had to get pen and paper
to mark the score, add things up. Ain't no body got time for that :)
Hope this web app makes your life more easier and maybe helps you
 test yourself more often so you can track your progress responsibly"]]
   [:div.row
    [:div.col-md-12 "Information you give wont be sold or given to anyone"]]
   [:div.row
    [:div.col-md-12 "You will be able to track your progress and see your score history and see how much it improved. Of course the most important thing is that you are getting healthier, feel better, happier and enjoy life fully."]]])

(defn home-page []
  [:div.container
   [:div.row
    [:p "Welcome to site"]]
   [:div.row
    [:a {:href "/#/htembh"} "HTEMBH Questionnaire"]]
   [:div.row
    [:a {:href "/#/ppd"} "PPD Questionnaire"]]
   [:div.row
    [:a {:href "/#/about"} "About this site"]]])


;; (defn about-page []
;;   [:div.container
;;    [:div.row
;;     [:div.col-md-12
;;      [:img {:src (str js/context "/img/warning_clojure.png")}]]]])

;; (defn home-page []
;;   [:div.container
;;    (when-let [docs (session/get :docs)]
;;      [:div.row>div.col-sm-12
;;       [:div {:dangerouslySetInnerHTML
;;              {:__html (md->html docs)}}]])])

(def pages
  {:home #'home-page
   :htembh #'htembh-page
   :ppd #'ppd-page
   :about #'about-page
   })


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
                 ;; :on-change #(swap! checked conj (int (->  % .-target .-value)))
                 ;; :required :true
                 ;; :on-change #(do
                 ;;               (swap! checked assoc "q-id" q-id)
                 ;;               (swap! checked assoc "response" 
                 ;;                      (-> % .-target .-value)))
                 }]
     (str text)]))


(defn questions-form [questions]
  (let [checked (r/atom {:responses []
                         :user (session/get :identity)})
        errors (r/atom nil)]
    (fn []
      [:div.container
       ;; [:form]
       (for [{:keys [id text responses]}  questions]
         ^{:key id}
         [:div.row
          [:label [:strong text]]
          ;; [:p1 (str id)]
          ;; (.log js/console (session/get :questions))
          (print-responses responses id checked)
          ])
       [:button.btn.btn-primary
        {:on-click #(qs/send-responses checked errors)} "send responses"]])))



(defn modal []
  (when-let [session-modal (session/get :modal)]
    [session-modal]))

(defn page []
  [:div
   [modal]
   ;; (println (session/get :questions))
   (when-let [questions (session/get :questions)]
     [questions-form  questions])
   (if (not= (session/get :page) nil)
     [(pages (session/get :page))]
     [(pages :home)])])

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (session/put! :page :home))

(secretary/defroute "/htembh" []
  (session/put! :page :htembh))

(secretary/defroute "/ppd" []
  (session/put! :page :ppd))

(secretary/defroute "/about" []
  (session/put! :page :about))

;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (History.)
        (events/listen
          HistoryEventType/NAVIGATE
          (fn [event]
              (secretary/dispatch! (.-token event))))
        (.setEnabled true)))

;; -------------------------
;; Initialize app
(defn fetch-docs! []
  (GET "/docs" {:handler #(session/put! :docs %)}))

(defn mount-components []
  (r/render [#'navbar] (.getElementById js/document "navbar"))
  (r/render [#'page] (.getElementById js/document "app")))

(defn init! []
  (load-interceptors!)
  ;; (fetch-docs!)
  (hook-browser-navigation!)
  (session/put! :identity js/identity)
  (mount-components))
