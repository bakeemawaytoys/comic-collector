(ns comic-collector.core
  (:require [comic-collector.parser :as parser]))

(def titles-to-buy { "ISSUE" #{
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
                     "THOR"}})

(defn- in-buy-list? [item]
  (let [items-to-buy (get titles-to-buy (:type item))]
    (contains? items-to-buy (:name item))))

(defn -main
  "Main function"
  [& args]
  (println (filter in-buy-list?  (with-open [reader (clojure.java.io/reader "http://www.previewsworld.com/shipping/newreleases.txt")]
    (parser/parse-file (line-seq reader))))))