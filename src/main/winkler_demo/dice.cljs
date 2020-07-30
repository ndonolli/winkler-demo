(ns winkler-demo.dice
  (:require [winkler.core :refer [generate]]))

(defn stateful-seq
  "Takes a sequence s and returns an \"iterator\" function which takes the first (or n, if provided) value(s) from s. Subsequent calls will take from the current position of the cursor and will return an empty seq if s is exhausted.

   Ex:
   (def iter! (stateful-seq (range)))
   (iter!) => (0)
   (iter! 3) => (1 2 3)"
  [s]
  (let [cursor (atom s)]
    (fn hello [n]
      (let [amt (if (and (number? n) (pos? n)) n 1)
            ret (take amt @cursor)]
        (swap! cursor #(drop amt %))
        ret))))

;; "Stateful" helpers for Uniform Randomizer type
(defn uniform-random
  "Returns a lazy seq of an even distribution of range n, randomized."
  [n]
  (lazy-cat (shuffle (range 1 (inc n))) (uniform-random n)))

(def uniform-iters
  "Runtime collection of stateful iterators used by Uniform Randomizer type."
  (atom {}))

;;;;;;;;;;;;;;;;;;;;;;
;; Randomizer types ;;
;;;;;;;;;;;;;;;;;;;;;;
(defprotocol Randomizer
  "Die roll interface for random number sequence generators"
  (roll [this]))

;; Standard RNG sequence implemented by most browsers.
(defrecord Pseudo [sides times]
  Randomizer
  (roll [this]
    (take times (repeatedly #(js/Math.ceil (* sides (js/Math.random)))))))

;; Intentionally uniform distribution of probability for seemingly random effect.
(defrecord Uniform [sides times]
  Randomizer
  (roll [this]
    (let [iter! (get uniform-iters sides)]
      (if iter!
        (iter! times)
        (let [iters (swap! uniform-iters
                           assoc sides (stateful-seq (uniform-random sides)))
              new-iter! (get iters sides)]
          (new-iter! times))))))

;; Cryptographically secure random sequence based off crypto web API salted with Winkler.
(defrecord WinklerCrypto [sides times]
  Randomizer
  (roll [this]
    (->> (js/Uint32Array. times) js/crypto.getRandomValues
          (js/Array.from)
          (map + (take times (generate 10000)))
          (map #(inc (mod % sides))))))

(comment
  (roll (Uniform. 20 10))
  (get @uniform-iters 20))