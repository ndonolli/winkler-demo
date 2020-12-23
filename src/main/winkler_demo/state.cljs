(ns winkler-demo.state
  (:require [reagent.core :as r]
            [winkler-demo.storage :as store]))

(defonce STORAGE_KEY "winkler.saved-rolls")

(def rolls
  "Sequence of dice rolls"
  (r/atom nil))

(def opts
  "Roll option map including :randomizer, :sides, :modifier, :times"
  (r/atom {:randomizer :winkler
           :sides 20
           :modifier 0
           :times 1}))

(def collapsed (r/atom false))

(def saved-rolls (r/atom (set (store/get STORAGE_KEY))))

(add-watch saved-rolls :save-db
           (fn [key this old-state new-state]
             (tap> @this)
             (store/set! STORAGE_KEY @this)))

(add-watch rolls :new-roll
           (fn [key this old-state new-state]
             (tap> @this)))