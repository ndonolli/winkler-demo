(ns winkler-demo.state
  (:require [reagent.core :as r]))

(def roll
  "Sequence of dice rolls"
  (r/atom nil))

(def opts
  "Roll option map including :randomizer, :sides, :modifier, :times"
  (r/atom {:randomizer :winkler
           :sides 20
           :modifier 0
           :times 1}))

(def collapsed (r/atom false))