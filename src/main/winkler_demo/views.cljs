(ns winkler-demo.views
  (:require [winkler-demo.dice :as dice]
            [reagent.core :as r]))

(defn roller []
  (let [roll (r/atom nil)]
    (fn []
      [:div
       [:p "your rolls are: " @roll]
       [:input {:type "button" :value "Click me!"
                :on-click #(swap! roll dice/random)}]])))

(defn app []
  [:h1 "Hello world!"]
  [roller])