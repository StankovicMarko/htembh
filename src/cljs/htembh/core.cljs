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
            [htembh.components.questions :as qs]
            [htembh.components.results :as res])
  (:import goog.History))

(def book-link "https://chekandpps.infusionsoft.com/app/storeFront/showProductDetail?productId=262")
(def inst-link "https://chekinstitute.com/")
(def blog-link "https://www.paulcheksblog.com/")
(def yt-link "https://www.youtube.com/user/PaulChekLive/videos")
(def recom-books-link "http://astore.amazon.com/paulchekseminars?_encoding=UTF8&node=1")

(defn user-menu []
  (if-let [id (session/get :identity)]
    [:ul.nav.navbar-nav.float-md-right.float-xs-left
     [:li.nav-item
      [:a.dropdown-item.btn
       {:on-click #(POST
                    "/api/logout"
                    {:handler (fn [] (do (session/remove! :identity)
                                         (session/remove! :highcharts-data)))})}
       [:i.fa.fa-user] " " id " | sign out"]]]
    [:ul.nav.navbar-nav.float-md-right.float-xs-left
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
        ;; [:a.navbar-brand {:href "/"} "Home"]
        [:ul.nav.navbar-nav
         [nav-link "#/home" "Home" :home collapsed?]
         [nav-link "#/htembh" "HTEMBH" :htembh collapsed?]
         [nav-link "#/ppd" "PPD" :ppd collapsed?]
         [nav-link "#/about" "About" :about collapsed?]]
        [user-menu]]])))

(defn ppd-page [] ;;;about page
  (do
    (let [email (session/get :identity)
          page (session/get :page)]
      (session/clear!)
      (session/put! :page page)
      (session/put! :identity email))
    (fn []
      (let [errors (r/atom nil)]
        (fn []
          (if (not (session/get :ppd-questions))
            [:div.container
             (when  (not (empty? @errors))
               [:div.alert.alert-danger @errors])
             [:div.row
              [:h4  "Start Primal Pattern Diet questionnaire from"]
              [:a {:href book-link
                   :target "_blank"} [:h4 "How to Eat, Move and Be Healthy"]]
              [:h5 "(Answer as truthfully as you can. This will help you determine your Primal Pattern Eating Type. Questionnaire usually"  [:font {:color "#0275d8"} " takes couple of minutes"] " to complete)"]
              [qs/get-ppd-questions-btn]
              [:br]
              [:hr]
              [:br]]
             [:div.row
              [:h4 "To get your most recent results press"]
              [res/get-ppd-results-btn errors]
              [:h5 "(To get the most accurate results retest again)"]]]))))))

(defn htembh-page []
  (do
    (let [email (session/get :identity)
          page (session/get :page)]
      (session/clear!)
      (session/put! :page page)
      (session/put! :identity email))
    (fn []
    (let [errors (r/atom nil)]
      (fn []
        (if (not (session/get :questions))
        [:div.container
         (when  (not (empty? @errors))
           [:div.alert.alert-danger @errors])
         [:div.row
          [:h4  "Start Nutrition and Lifestyle questionnaire from"]
          [:a {:href book-link
               :target "_blank"} [:h4 "How to Eat, Move and Be Healthy"]]
          [:h5 "(Answer as truthfully as you can. There are 6 topics with multiple questions related to them. Questionnaire usually"  [:font {:color "#0275d8"} " takes 10 minutes"] " to complete)"]
          [qs/get-questions-btn]
          [:br]
          [:hr]
          [:br]]
         [:div.row
          [:h4 "To get your most recent results press"]
          [res/get-results-btn errors]
          [:h5 "(Once you get results you should focus on left most column that is not colored in green. Once you heal that area it will have knock-on effect on other areas and you should retest again)"]]]))))))

(defn about-page []
  (do
    (let [email (session/get :identity)
          page (session/get :page)]
      (session/clear!)
      (session/put! :page page)
      (session/put! :identity email))
    (fn []
  [:div.container
   [:div.row
    [:div.col-md-12
     [:h3 "Basic Info: "]
     [:h5 "I've used book for 2 years now. After few times of testing myself i wanted to see my progress and have my results in one place. That is why i decided to make this small web application."]]]
   [:br]
   [:div.row
    [:div.col-md-12
     [:h3 "Tutorial:"]
     [:h5 "Register, Login and use application"]
     [:h5 "- HTEMBH tab is Nutrition and Lifestyle questionnaire from "
      [:a {:href book-link} "the book "] "and it is used to determine your over all stress levels and to show you what you need to focus on"]
     [:h5 "- PPD tab is for Primal Pattern® Diet questionnaire and it is used to determine your metabolic type"]
     [:h5 "- On every page(tab) there is button results which tells you your latest results based on answers. (plan is to have history of your answers in another chart that feature will come in future)"]]]
   [:br]
   [:div.row
    [:div.col-md-12
     [:h3 "Learn more:"]
     [:a {:href inst-link
          :target "_blank"} [:h5 "Chek Institute"]]
     [:a {:href blog-link
          :target "_blank"} [:h5 "Paul Chek's blog"]]
     [:a {:href yt-link
          :target "_blank"} [:h5 "Paul Chek's youtube"]]
     [:a {:href recom-books-link
          :target "_blank"} [:h5 "Recommended reading list"]]]]
   [:br]
   [:div.row
    [:div.col-md-12
     [:h3 "Contact me:"]
     [:a {:target "_blank"
          :href "https://www.linkedin.com/in/marko-stankovic-053552113/"}
      [:img {:src "../img/in.png"
             :title "Linkedin"
             :height "75px"}]]
     [:a {:target "_blank"
          :href "https://github.com/StankovicMarko"}
      [:img {:src "../img/github.png"
             :title "Github"
             :height "75px"}]]
     ]]
   ])))

(defn home-page []
  (do
    (let [email (session/get :identity)
          page (session/get :page)]
      (session/clear!)
      (session/put! :page page)
      (session/put! :identity email))
    (fn []
  [:div.container
   [:div.row
    [:h2 "Welcome"]
    [:h5 "This site was intended for book readers of: " 
     [:a {:target "_blank"
          :href book-link} "How to Eat, Move and Be Healthy"]]]
   [:div.row
    [:h5 "For more details read: " [:a {:href "/#/about"} "About this site"]]]
   [:div.row
    [:h5 "Start Nutrition & Lifestyle questionnaire by clicking: " [:a {:href "/#/htembh"} "here"]]]
   [:div.row
    [:h5 "Start Primal Pattern® Diet questionnaire by clicking: " [:a {:href "/#/ppd"} "here"]]]
   ])))


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


(defn modal []
  (when-let [session-modal (session/get :modal)]
    [session-modal]))

(defn page []
  [:div
   [modal]
   (when-let [questions (session/get :questions)]
     [qs/questions-form  questions])
   (when-let [ppd-questions (session/get :ppd-questions)]
     [qs/ppd-questions-form  ppd-questions])
   (if (not= (session/get :page) nil)
     [(pages (session/get :page))]
     [(pages :home)])
   [:button {:on-click qs/scroll-to-top
             :id "myBtn"
             :title "Go to top"} "Top"]])


(defn results-render []
  (if (and (not (session/get :questions)) (session/get :highcharts-data))
    [:div {:style {:min-width "310px" :max-width "1200px"
                   :height "600px" :margin "0 auto"}}]))


(defn results-did-mount [this]
  (if (and (not (session/get :questions)) (session/get :highcharts-data))
    (js/Highcharts.Chart. (r/dom-node this) (clj->js
                                             (assoc res/chart-config :series
                                                    (conj [] (session/get :highcharts-data)))))))

(defn results []
  (r/create-class {:reagent-render results-render
                   :component-did-update results-did-mount
                   :component-did-mount results-did-mount}))


(defn ppd-results-render []
  (if (and (not (session/get :ppd-questions)) (session/get :highcharts-ppd-data))
    [:div {:style {:min-width "310px" :max-width "1200px"
                   :height "600px" :margin "0 auto"}}]))


(defn ppd-results-did-mount [this]
  (if (and (not (session/get :ppd-questions)) (session/get :highcharts-ppd-data))
    (js/Highcharts.Chart. (r/dom-node this)
                          (clj->js (get res/ppd-charts (session/get :highcharts-ppd-data))))))

(defn ppd-results []
  (r/create-class {:reagent-render ppd-results-render
                   :component-did-update ppd-results-did-mount
                   :component-did-mount ppd-results-did-mount}))

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/home" []
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
  (r/render [#'page] (.getElementById js/document "app"))
  (r/render [#'results] (.getElementById js/document "results"))
  (r/render [#'ppd-results] (.getElementById js/document "ppd-results"))
  )


(defn init! []
  (load-interceptors!)
  ;; (fetch-docs!)
  (hook-browser-navigation!)
  (session/put! :identity js/identity)
  (mount-components))
