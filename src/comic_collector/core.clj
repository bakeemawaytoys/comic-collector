(ns comic-collector.core
  (:require [comic-collector.parser :as parser]
            [io.aviso.columns :as c]
            [io.aviso.ansi :as ansi]
            [clj-time.core :as t]
            [clj-time.format :as fmt]))

(def titles-to-buy {"ISSUE" #{
                              "BLACK SCIENCE"
                              "EAST OF WEST"
                              "LAZARUS"
                              "SAGA"
                              "SEX CRIMINALS"
                              "KILL OR BE KILLED"
                              "LOW"
                              "ODYC"
                              "ODY-C"
                              "SOUTHERN BASTARDS"
                              "AUTUMNLANDS TOOTH & CLAW"
                              "PAPER GIRLS"
                              "RAT QUEENS"
                              "OLD GUARD"
                              "CROSSWIND"
                              "DYING AND THE DEAD"
                              "RUNAWAYS"
                              }})

(defn- in-buy-list? [item]
  (let [items-to-buy (get titles-to-buy (:type item))]
    (contains? items-to-buy (:name item))))

(defn calculate-release-date []
  (let [now (t/now)
        wednesday 3
        adjustment (- wednesday (t/day-of-week now))
        release-date (t/plus now (t/days adjustment))
        formatter (fmt/formatter "MM/dd/yyyy")]
    (fmt/unparse formatter release-date)
    ))


(defn table [elements] (let [separator (ansi/bold " | ")
                          formatter (c/format-columns
                                         [:left (c/max-value-length elements :name)]
                                         separator
                                         5
                                         separator
                                         6
                                         separator
                                         [:left (c/max-value-length elements :publisher)]
                                         separator
                                         :none)]
                         (c/write-rows *out* formatter
                                       [(fn [m] (ansi/cyan (:name m)))
                                        (fn [m] (ansi/yellow (:number m)))
                                        (fn [m] (ansi/blue (:cost m)))
                                        (fn [m] (ansi/green (:publisher m)))
                                        :notes]
                                       elements)))

(defn -main
  "Main function"
  [& args]
  (table
    (filter in-buy-list?
            (with-open
              [reader (clojure.java.io/reader (str "https://www.previewsworld.com/NewReleases/Export?format=txt&releaseDate=" (calculate-release-date)))]
              (let [lines (line-seq reader)
                    [tail date] (parser/parse-date-line lines)
                    formatter (:date fmt/formatters)
                    _ (println (ansi/bold "Available on") (ansi/bold-red (fmt/unparse-local-date formatter date)))]
                (parser/parse-file tail))))))
