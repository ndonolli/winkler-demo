(ns winkler-demo.state
  (:require [reagent.core :as r]))

(def roll (r/atom nil))

(def opts (r/atom {:randomizer :winkler
                   :sides 20
                   :modifier 0
                   :times 1}))