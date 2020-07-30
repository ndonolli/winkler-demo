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

(defn roller []
  (let [roll (r/atom nil)
        opts (r/atom {:randomizer :winkler
                      :sides 20
                      :times 1})]
    (fn []
      [:div
       [:div
        [:select {:value (:randomizer @opts)
                  :on-change #(swap! opts assoc :randomizer (-> % .-target .-value keyword))}
         [:option {:value :winkler} "Winkler"]
         [:option {:value :pseudo} "Psuedo"]
         [:option {:value :uniform} "Uniform"]]
        [:select {:value (:sides @opts)
                  :on-change #(swap! opts assoc :sides (-> % .-target .-value int))}
         [:option {:value 20} "d20"]
         [:option {:value 12} "d12"]
         [:option {:value 10} "d10"]
         [:option {:value 8} "d8"]
         [:option {:value 6} "d6"]
         [:option {:value 4} "d4"]]
        [:select {:value (:times @opts)
                  :on-change #(swap! opts assoc :times (-> % .-target .-value int))}
         (for [i (range 1 9)]
           ^{:key (str "times-" i)}
           [:option {:value i} i])]]
       [:div
        [:input {:type "button" :value "Click me!"
                 :on-click #(reset! roll (dice/roll (init-randomizer @opts)))}]]
       [roll-display @roll]])))

(defn app []
  [roller])

(def a (atom {:randomizer :uniform}))
(comment)