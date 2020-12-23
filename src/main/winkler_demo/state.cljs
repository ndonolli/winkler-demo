(ns winkler-demo.state
  (:require [reagent.core :as r]
            [winkler-demo.storage :as store]))

(defonce SAVED_ROLLS_DB "winkler.saved-rolls")
(defonce ROLLS_DB "winkler.rolls")
(defonce CACHE_LIMIT 50)

(def rolls
  "Sequence of dice rolls"
  (r/atom (store/get ROLLS_DB)))

(def opts
  "Roll option map including :randomizer, :sides, :modifier, :times"
  (r/atom {:randomizer :winkler
           :sides 20
           :modifier 0
           :times 1}))

(def collapsed (r/atom false))

(def saved-rolls (r/atom (set (store/get SAVED_ROLLS_DB))))

(def view-limit (r/atom 5))

(add-watch saved-rolls :saved-rolls-db
           (fn [key this old-state new-state]
             (tap> @this)
             (store/set! SAVED_ROLLS_DB @this)))

(add-watch rolls :rolls-db
           (fn [key this old-state new-state]
             (tap> @this)
             (store/set! ROLLS_DB (take CACHE_LIMIT @this))))