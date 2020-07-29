(ns winkler-demo.dice
  (:require [winkler.core :refer [generate]]))

(defn random [] (take 3 (generate 100)))

(defn fake-random [n]
  (lazy-cat (shuffle (range n)) (fake-random n)))

(defn winkler-random [])

(def fake-random-20 (fake-random 20))

(defn stateful-seq [s]
  (let [cursor (atom s)]
    (fn hello [n]
      (let [amt (if (and (number? n) (pos? n)) n 1)
            ret (take amt @cursor)]
        (swap! cursor #(drop amt %))
        ret))))

(comment
  (do
    (def it! (stateful-seq (cycle (range 20))))
    (it! 39)))