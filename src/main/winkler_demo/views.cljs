(ns winkler-demo.views
  (:require [winkler-demo.dice :refer [Pseudo Uniform WinklerCrypto] :as dice]
            [reagent.core :as r]))

(defn roller []
  (let [roll (r/atom 0)]
    (fn []
      [:div
       [:p "your rolls are: " @roll]
       [:input {:type "button" :value "Click me!"
                :on-click #(swap! roll dice/roll (Pseudo. 20 2))}]])))

(defn app []
  [:h1 "Hello world!"]
  [roller])