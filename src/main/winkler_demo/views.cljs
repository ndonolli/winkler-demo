(ns winkler-demo.views
  (:require [winkler-demo.dice :as dice]
            [winkler-demo.state :as state]
            [clojure.string :refer [capitalize]]))

(defn roll-display [dice modifier]
  (if-let [dice* (seq (interpose "+" dice))]
     [:div.container.dice-container
      (for [die dice*]
        ^{:key (str "die-" (js/Math.random))}
        [:div.ml-1.mr-1.mb-5 {:class (if (= die "+")
                                  "plus"
                                  (str "die" " " (case die 20 "nat-20" 1 "nat-1" "")))}
         [:span.die-body.bg-number die]])
      (if-not (zero? (int modifier))
        [:span.ml-1.mr-1.mb-5.bg-number (str "+ " modifier)])
      [:span.ml-1.mr-1.mb-5.bg-number " = " [:strong (reduce + modifier dice)]]]))

(defn roll-display-list [rolls]
  [:section.section.roll-container
   (for [roll rolls]
     [roll-display (:roll roll) (:modifier roll)])])

(defn header []
  [:section.hero.is-dark
   [:div.hero-body
    [:div.container.has-text-centered
     [:h1.title "Winkler's DnD Dice"]
     [:h2.subtitle "Making random rolls somehow more random"]]]])

(defn roll-form []
  [:div.container
   [:div.field.is-horizontal
    [:div.field-label.is-normal
     [:label.label "Randomizer"]]
    [:div.field-body
     [:div.field
      [:div.control
       [:div.select
        [:select {:value (:randomizer @state/opts)
                  :on-change #(swap! state/opts assoc :randomizer (-> % .-target .-value keyword))}
         [:option {:value :winkler} "Winkler"]
         [:option {:value :pseudo} "Psuedo (default)"]
         [:option {:value :uniform} "Uniform (fake random)"]]]]]]]
   [:div.field.is-horizontal
    [:div.field-label.is-normal
     [:label.label "Dice"]]
    [:div.field-body
     [:div.field.has-addons
      [:div.control
       [:div.select
        [:select {:value (:times @state/opts)
                  :on-change #(swap! state/opts assoc :times (-> % .-target .-value int))}
         (for [i (range 1 11)]
           ^{:key (str "times-" i)}
           [:option {:value i} i])]]]
      [:div.control
       [:div.select
        [:select {:value (:sides @state/opts)
                  :on-change #(swap! state/opts assoc :sides (-> % .-target .-value int))}
         [:option {:value 20} "d20"]
         [:option {:value 12} "d12"]
         [:option {:value 10} "d10"]
         [:option {:value 8} "d8"]
         [:option {:value 6} "d6"]
         [:option {:value 4} "d4"]]]]
      [:div.control
       [:div.select
        [:select {:value (:modifier @state/opts)
                  :on-change #(swap! state/opts assoc :modifier (-> % .-target .-value int))}
         (for [i (range 21)]
           ^{:key (str "modifier-" i)}
           [:option {:value i} (str "+ " i)])]]]]]]
   [:div.field.is-horizontal
    [:div.field-label]
    [:div.field-body
     [:div.field
      [:div.control
       [:button.button.is-link
        {:on-click #(swap! state/rolls conj (merge {:roll (dice/roll @state/opts)} @state/opts))}
        "Roll"]]]]]])

(defn randomizer-desc []
  (let [randomizer (:randomizer @state/opts)]
    [:div.container
     [:h4.is-size-4 (capitalize (name randomizer))]
     (if-not @state/collapsed
       (list
        (case randomizer
          :winkler [:p.mb-3 "This RNG utilizes the cryptographically secure browser API methods found in the " [:code "window.crypto"] " object. "
                    "It additionally uses " [:a {:href "https://github.com/ndonolli/winkler"} "Winkler"] ", an open source library for generating "
                    "client-side entropy by measuring fluctuations in the machine's state.  It intends to produce a mathematically random "
                    "sequence suitable enough for encryption."]
          :uniform [:p.mb-3 "This produces a sequence that is intentionally not random, but seemingly so to most of us. For a die with " [:code "n"] " sides, "
                    "each number will appear once for every " [:code "n"] " rolls. This ensures that numbers rarely repeat and the distribution of values is always uniform. "
                    "While this generator is not random at all, it is designed to produce a sequence we interpret as highly random."]
          :pseudo [:p.mb-3 "This utilizes the standard " [:code "Math.random()"] " javascript method found in most browser engines.  The common implementation is a " [:code "Xorshift128+"]
                   " LSFR, which is very fast and produces an adequate random sequence. While this is great for games (and honestly, dice rolls) it is still deterministic and thus "
                   "only a pseudo-random generator - unfit for secure cryptography."])
        [:a.mt-2.is-size-7 {:on-click #(swap! state/collapsed not)} "I don't care"])
       [:a.mt-2 {:on-click #(swap! state/collapsed not)} "Tell me more!"])
     ]))

(defn about []
  [:section.hero.is-dark
   [:div.hero-body
    [:div.container
     [:h4.is-size-3.mb-5 "What is this?"]
     [:p.mb-5 "This is a simple dice roller for use with tabletop games. It is also a demonstration of different random number generators (RNGs) and various algorithmic techniques "
      "used to achieve as close to mathematical randomness as possible, using a computer. This is not a straightforward task, and is studied well in the field of cryptography - "
      "its application ever crucial in the fast-paced world of cybersecurity."]
     [:p.mb-5 "This weird foray into cryptographically sound dice stemmed from real-world frustration with physical DnD dice. I created an open-sourced clojure(script) library called "
      [:a {:href "https://github.com/ndonolli/winkler"} "Winkler"] " as a solution for client-side entropy generation, and wrote more thoughts about it here. "
      "Sure, maybe a demonstration with dice is overkill, but would you trust your character's fate to anything less than the power of chaotic indifference that is a truly random dice roll?  I think not."]
     [:p.is-size-7 "View this page's source on "[:a {:href "https://github.com/ndonolli/winkler-demo"} "github"]". Made with <3 by " [:a {:href "https://imaginathan.space"} "Nathan"]]]]])

(defn app []
  [:<>
   [header]
   [:div.container
    [:section.section
     [:div.columns
      [:div.column
       [roll-form]]
      [:div.column
       [randomizer-desc]]]]]
   [roll-display-list (take 5 @state/rolls)]
   [about]])