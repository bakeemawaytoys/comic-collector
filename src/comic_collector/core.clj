(ns comic-collector.core)

(def start-publishers-line "PREMIER PUBLISHERS")

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
                     "JUPITER'S LEGACY"})

(def item-types #{"HC" "TP" "SC" "POSTER" "GN" "STATUE" "T/S" "HOODIE" "CASE" "DVD" "AF" "FIGURE"} )

(declare handle-publisher)

(defn create-shopping-list [] [])

(defn add-to-shopping-list [item shopping-list]
  (conj shopping-list item))

(defn in-buy-list? [item]
  (contains? titles-to-buy (:name item)))

(defn stringify [tokens]
  (clojure.string/trim (clojure.string/join " " tokens)))

(defn classify [token attrs]
  (if (= \# (first token))
    (assoc attrs :type "ISSUE" :number token)
    (assoc attrs :type token)))

(defn parse-name [tokens attributes]
  (let [ [name other] (split-with
                        (fn [t]
                          (and
                            (not (= \# (first t)))
                            (not (contains? item-types t))))
                        tokens)]
    (classify (first other) (assoc attributes :name (stringify name) :notes (stringify (rest other))))))

(defn parse-item-line [line publisher]
  (let [tokens (clojure.string/split line #"\s")]
    (parse-name (drop-last (drop 1 tokens)) {:id (first tokens) :publisher publisher :cost (last tokens)})))

(defn check-list [item shopping-list]
  (if (in-buy-list? item)
    (add-to-shopping-list item shopping-list)
    shopping-list))

(defn handle-item [line publisher shopping-list]
  (let [item (parse-item-line line publisher)]
    (println item)
    (check-list item shopping-list)))

(defn handle-items [lines publisher shopping-list]
    (loop [title-lines lines bl shopping-list]
    (if (and title-lines (not (clojure.string/blank? (first title-lines))))
        (recur (rest title-lines) (handle-item (first title-lines) publisher bl))
      (handle-publisher title-lines bl))))

(defn handle-publisher [lines shopping-list]
  "Assumes that their are at least three lines.
  The first is blank, the second is the name
  of a publisher, and the third is another blank line."
  (if (seq lines)
    (let [
          publisher (first (rest lines))
          items (drop 3 lines)]
        (handle-items items publisher shopping-list))
    shopping-list))

(defn handle-preamble [lines]
  (if (= start-publishers-line (first lines))
    (handle-publisher (rest lines) (create-shopping-list) )
    (handle-preamble (rest lines))))

(defn -main
  "Main function"
  [& args]
  (println (with-open [reader (clojure.java.io/reader "http://www.previewsworld.com/shipping/newreleases.txt")]
    (handle-preamble (line-seq reader)))))