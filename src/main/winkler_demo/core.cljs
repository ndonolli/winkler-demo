(ns winkler-demo.core
  (:require [reagent.dom :as dom]
            [winkler-demo.views :as views]))

(defn ^:dev/after-load start []
  (dom/render
   [views/app]
   (.getElementById js/document "app")))

(defn ^:export main []
  (start))