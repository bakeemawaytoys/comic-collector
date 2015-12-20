(ns comic-collector.core
  (:require [comic-collector.parser :as parser]))

(def titles-to-buy {"ISSUE" #{
                              "BLACK SCIENCE"
                              "EAST OF WEST"
                              "MANHATTAN PROJECTS"
                              "MANHATTAN PROJECTS SUN BEYOND THE STARS"
                              "LAZARUS"
                              "SECRET"
                              "SAGA"
                              "SEX CRIMINALS"
                              "JUPITER'S LEGACY"
                              "JUPITERS LEGACY"
                              "LOW"
                              "ODYC"
                              "ODY-C"
                              "SOUTHERN BASTARDS"
                              "NAMELESS"
                              "DYING AND THE DEAD"
                              "INVISIBLE REPUBLIC"
                              "AUTUMNLANDS TOOTH & CLAW"
                              "AUTUMNLANDS"
                              "MS MARVEL"
                              "INJECTION"
                              "PAPER GIRLS"
                              "WE STAND ON GUARD"
                              "TOKYO GHOST"
                              }})

(defn- in-buy-list? [item]
  (let [items-to-buy (get titles-to-buy (:type item))]
    (contains? items-to-buy (:name item))))

(defn -main
  "Main function"
  [& args]
  (println
    (filter in-buy-list?
            (with-open
              [reader (clojure.java.io/reader "http://www.previewsworld.com/shipping/newreleases.txt")]
                                  (let [lines (line-seq reader)
                                        [tail date] (parser/parse-date-line lines)
                                        df java.time.format.DateTimeFormatter/ISO_DATE
                                        _ (println "Available on" (.format df date))]
                                    (parser/parse-file tail))))))
