(ns winkler-demo.storage
  (:require [cljs.reader :as reader])
  (:refer-clojure :exclude [get]))

(defn set! [key obj]
  (.setItem js/localStorage key (prn-str obj)))

(defn get [key]
  (reader/read-string (.getItem js/localStorage key)))