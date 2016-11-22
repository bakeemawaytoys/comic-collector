(ns comic-collector.core
  (:require [comic-collector.parser :as parser])
  (:import (java.time.format DateTimeFormatter FormatStyle)
           (java.time LocalDate DayOfWeek)))

(def titles-to-buy {"ISSUE" #{
                              "BLACK SCIENCE"
                              "EAST OF WEST"
                              "LAZARUS"
                              "SAGA"
                              "SEX CRIMINALS"
                              "JUPITER'S LEGACY"
                              "JUPITERS LEGACY"
                              "KILL OR BE KILLED"
                              "LOW"
                              "ODYC"
                              "ODY-C"
                              "SOUTHERN BASTARDS"
                              "AUTUMNLANDS TOOTH & CLAW"
                              "PAPER GIRLS"
                              "RAT QUEENS"
                              }})

(defn- in-buy-list? [item]
  (let [items-to-buy (get titles-to-buy (:type item))]
    (contains? items-to-buy (:name item))))

(defn calculate-release-date []
  (let [now (LocalDate/now)
        adjustment (- (.getValue (DayOfWeek/WEDNESDAY)) (.getValue (DayOfWeek/from now)))
        release-date (.plusDays now adjustment)
        formatter (DateTimeFormatter/ofLocalizedDate FormatStyle/SHORT)]
      (.format formatter release-date)
    ))

(defn -main
  "Main function"
  [& args]
  (println
    (filter in-buy-list?
            (with-open
              [reader (clojure.java.io/reader (str "https://www.previewsworld.com/NewReleases/Export?format=txt&releaseDate=" (calculate-release-date)))]
                                  (let [lines (line-seq reader)
                                        [tail date] (parser/parse-date-line lines)
                                        df DateTimeFormatter/ISO_DATE
                                        _ (println "Available on" (.format df date))]
                                    (parser/parse-file tail))))))
