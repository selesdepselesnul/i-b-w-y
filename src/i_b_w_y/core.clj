(ns i-b-w-y.core
  (:gen-class)
  (:require [hawk.core :as hawk]))

(defn when-file-changed [ctx e]
  (println (:kind e))
  (println (.getName (:file e)))
  ctx)

(def dir-being-watched "/home/morrisseymarr/watched")

(defn watch [dir]
  (hawk/watch! [{:paths [dir]
                 :handler when-file-changed}]))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(defn filter-files-with-pattern [dir pattern]
  (let [f (clojure.java.io/file dir)]
    (->> (file-seq f)
         (filter #(re-matches pattern (.getName %))))))

(def pattern-to-filter #"^mantab.*$")

(filter-files-with-pattern dir-being-watched pattern-to-filter)





