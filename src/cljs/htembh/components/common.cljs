(ns htembh.components.common)

(defn modal [header body footer]
  [:div
   [:div.modal-dialog
    [:div.modal-content
     [:div.modal-header [:h3 header]]
     [:div.modal-body body]
     [:div.modal-footer
      [:div.bootstrap-dialog-footer
       footer]]]]
   [:div.modal-backdrop.fade.in]])


(defn input [type email placeholder fields]
  [:input.form-control.input-lg
   {:type type
    :placeholder placeholder
    :value (email @fields)
    :on-change #(swap! fields assoc email (-> % .-target .-value))}])

(defn form-input [type label email placeholder fields optional?]
  [:div.form-group
   [:label label]
   (if optional?
     [input type email placeholder fields]
     [:div.input-group
      [input type email placeholder fields]
      [:span.input-group-addon
       "✱"]])])

(defn text-input [label email placeholder fields & [optional?]]
  (form-input :text label email placeholder fields optional?))

(defn password-input [label email placeholder fields & [optional?]]
  (form-input :password label email placeholder fields optional?))
