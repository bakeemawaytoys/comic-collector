(defproject comic-collector "2.0-SNAPSHOT"
  :description "Generates weekly comic book buy lists."
  :url "http://www.ghostpla.net"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :main comic-collector.core
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/test.check "0.9.0"]
                 [clj-time "0.13.0"]
                 [io.aviso/pretty "0.1.33"]])
