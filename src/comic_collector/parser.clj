(ns comic-collector.parser
  (:require [clojure.string :as str]))

(def ^:private start-publishers-line "PREMIER PUBLISHERS")

(def ^:private item-types #{"HC" "TP" "SC" "POSTER" "GN" "STATUE" "T/S" "HOODIE" "CASE" "DVD" "AF" "FIGURE"} )


(defn- stringify [tokens]
  (str/trim (str/join " " tokens)))

(defn- classify [token attrs]
  (cond (= \# (first token)) (assoc attrs :type "ISSUE" :number token)
        (contains? item-types token) (assoc attrs :type token)
        :else (assoc attrs :type "OTHER")))

(defn- parse-name [tokens attributes]
  (let [ [name other] (split-with
                        (fn [t]
                          (and
                            (not (= \# (first t)))
                            (not (contains? item-types t))))
                        tokens)]
    (classify (first other) (assoc attributes :name (stringify name) :notes (stringify (rest other))))))

(defn- parse-item-line [line publisher]
  (let [tokens (str/split line #"\s")]
    (parse-name (drop-last (drop 1 tokens)) {:id (first tokens) :publisher publisher :cost (last tokens)})))



(defn- handle-item [line publisher parsed-list]
  (let [item (parse-item-line line publisher)]
    (conj parsed-list item)))


(declare handle-publisher)

(defn- handle-items [lines publisher parsed-list]
  (loop [title-lines lines pl parsed-list]
    (if (and title-lines (not (str/blank? (first title-lines))))
      (recur (rest title-lines) (handle-item (first title-lines) publisher pl))
      (handle-publisher title-lines pl))))

(defn- handle-publisher [lines parsed-list]
  "Assumes that their are at least three lines.
  The first is blank, the second is the name
  of a publisher, and the third is another blank line."
  (if (seq lines)
    (let [
          publisher (first (rest lines))
          items (drop 3 lines)]
      (handle-items items publisher parsed-list))
    parsed-list))

(defn- handle-preamble [lines]
  (if (= start-publishers-line (first lines))
    (handle-publisher (rest lines) [])
    (handle-preamble (rest lines))))

(defn parse-file [lines]
  (handle-preamble lines))