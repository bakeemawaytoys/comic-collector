(ns comic-collector.core
  (:require [comic-collector.parser :as parser]))



(def titles-to-buy #{
                     "BLACK SCIENCE"
                     "HAWKEYE"
                     "EAST OF WEST"
                     "MANHATTAN PROJECTS"
                     "LAZARUS"
                     "SECRET"
                     "SAGA"
                     "SEX CRIMINALS"
                     "MASSIVE"
                     "JUPITER'S LEGACY"
                     "LOW"
                     "MEN OF WRATH"
                     "THOR"})



(defn create-shopping-list [] [])

(defn- add-to-shopping-list [item shopping-list]
  (conj shopping-list item))

(defn- in-buy-list? [item]
  (contains? titles-to-buy (:name item)))

(defn- check-list [item shopping-list]
  (if (in-buy-list? item)
    (add-to-shopping-list item shopping-list)
    shopping-list))

(defn -main
  "Main function"
  [& args]
  (println (with-open [reader (clojure.java.io/reader "http://www.previewsworld.com/shipping/newreleases.txt")]
    (parser/parse-file (line-seq reader)))))