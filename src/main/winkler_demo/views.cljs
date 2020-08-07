(ns winkler-demo.views
  (:require [winkler-demo.dice :refer [Pseudo Uniform WinklerCrypto] :as dice]
            [winkler-demo.state :as state]))

(defn init-randomizer [opts]
  (let [{:keys [randomizer sides times]} opts
        randomizers {:winkler (WinklerCrypto. sides times)
                     :pseudo (Pseudo. sides times)
                     :uniform (Uniform. sides times)}]
       (get randomizers randomizer)))

(defn roll-display [rolls modifier]
  (if-let [rolls* (seq (interpose "+" rolls))]
    [:section.section
     [:div.container.dice-container
      (for [roll rolls*]
        ^{:key (str "roll-" (js/Math.random))}
        [:div.ml-1.mr-1.mb-5 {:class (if (= roll "+")
                                  "plus"
                                  (str "die" " " (case roll 20 "nat-20" 1 "nat-1" "")))}
         [:span.die-body.bg-number roll]])
      (if-not (zero? (int modifier))
        [:span.ml-1.mr-1.mb-5.bg-number (str "+ " modifier)])
      [:span.ml-1.mr-1.mb-5.bg-number " = " [:strong (reduce + modifier rolls)]]]]))


(defn header []
  [:section.hero.is-dark
   [:div.hero-body
    [:div.container.has-text-centered
     [:h1.title "Winkler's DnD Dice"]
     [:h2.subtitle "Making random rolls somehow more random"]]]])

(defn roll-form []
  [:section.section
   [:div.container
    [:div.field.is-horizontal
     [:div.field-label.is-normal
      [:label.label "Randomizer"]]
     [:div.field-body
      [:div.field
       [:div.control
        [:div.select
         [:select {:value (:randomizer @state/opts)
                   :on-change #(swap! state/opts assoc :randomizer (-> % .-target .-value keyword))}
          [:option {:value :winkler} "Winkler"]
          [:option {:value :pseudo} "Psuedo"]
          [:option {:value :uniform} "Uniform"]]]]]]]
    [:div.field.is-horizontal
     [:div.field-label.is-normal
      [:label.label "Dice"]]
     [:div.field-body
      [:div.field.has-addons
       [:div.control
        [:div.select
         [:select {:value (:times @state/opts)
                   :on-change #(swap! state/opts assoc :times (-> % .-target .-value int))}
          (for [i (range 1 11)]
            ^{:key (str "times-" i)}
            [:option {:value i} i])]]]
       [:div.control
        [:div.select
         [:select {:value (:sides @state/opts)
                   :on-change #(swap! state/opts assoc :sides (-> % .-target .-value int))}
          [:option {:value 20} "d20"]
          [:option {:value 12} "d12"]
          [:option {:value 10} "d10"]
          [:option {:value 8} "d8"]
          [:option {:value 6} "d6"]
          [:option {:value 4} "d4"]]]]
       [:div.control
        [:div.select
         [:select {:value (:modifier @state/opts)
                   :on-change #(swap! state/opts assoc :modifier (-> % .-target .-value int))}
          (for [i (range 21)]
            ^{:key (str "modifier-" i)}
            [:option {:value i} (str "+ " i)])]]]]]]
    [:div.field.is-horizontal
     [:div.field-label]
     [:div.field-body
      [:div.field
       [:div.control
        [:button.button.is-link
         {:on-click #(reset! state/roll (dice/roll (init-randomizer @state/opts)))}
         "Roll"]]]]]]])

(defn app []
  [:<>
   [header]
   [roll-form]
   [roll-display @state/roll (:modifier @state/opts)]])

(def a (atom {:randomizer :uniform}))
(comment
  (interpose ", " [2 3]))