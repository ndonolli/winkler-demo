(ns winkler-demo.views
  (:require [winkler-demo.dice :refer [Pseudo Uniform WinklerCrypto] :as dice]
            [reagent.core :as r]))

(defn init-randomizer [opts]
  (let [{:keys [randomizer sides times]} opts
        randomizers {:winkler (WinklerCrypto. sides times)
                     :pseudo (Pseudo. sides times)
                     :uniform (Uniform. sides times)}]
       (get randomizers randomizer)))

(defn roll-display [rolls]
  [:p "your rolls are: " (apply str (interpose ", " rolls))])


(defn header []
  [:section.hero.is-dark
   [:div.hero-body
    [:div.container
     [:h1.title "Winkler's DnD Dice"]
     [:h2.subtitle "Making random rolls somehow more random"]]]])

(defn roller []
  (let [roll (r/atom nil)
        opts (r/atom {:randomizer :winkler
                      :sides 20
                      :times 1})]
    (fn []
      [:section.section
       [:div.container
        [:div.field.is-horizontal
         [:div.field-label.is-normal
          [:label.label "Randomizer"]]
         [:div.field-body
          [:div.field
           [:div.control
            [:div.select
             [:select {:value (:randomizer @opts)
                       :on-change #(swap! opts assoc :randomizer (-> % .-target .-value keyword))}
              [:option {:value :winkler} "Winkler"]
              [:option {:value :pseudo} "Psuedo"]
              [:option {:value :uniform} "Uniform"]]]]]]]
        [:div.field.is-horizontal
         [:div.field-label.is-normal
          [:label.label "Die type"]]
         [:div.field-body
          [:div.field
           [:div.control
            [:div.select
             [:select {:value (:sides @opts)
                       :on-change #(swap! opts assoc :sides (-> % .-target .-value int))}
              [:option {:value 20} "d20"]
              [:option {:value 12} "d12"]
              [:option {:value 10} "d10"]
              [:option {:value 8} "d8"]
              [:option {:value 6} "d6"]
              [:option {:value 4} "d4"]]]]]]]
        [:div.field.is-horizontal
         [:div.field-label.is-normal
          [:label.label "Rolls"]]
         [:div.field-body
          [:div.field
           [:div.control
            [:div.select
             [:select {:value (:times @opts)
                       :on-change #(swap! opts assoc :times (-> % .-target .-value int))}
              (for [i (range 1 9)]
                ^{:key (str "times-" i)}
                [:option {:value i} i])]]]]]]
        [:div.field.is-horizontal
         [:div.field-label]
         [:div.field-body
          [:div.field
           [:div.control
            [:button.button.is-link
             {:on-click #(reset! roll (dice/roll (init-randomizer @opts)))}
             "Roll"]]]]]
        [roll-display @roll]]])))

(defn app []
  [:<>
   [header]
   [roller]])

(def a (atom {:randomizer :uniform}))
(comment)