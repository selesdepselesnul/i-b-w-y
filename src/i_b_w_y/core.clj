(ns i-b-w-y.core
  (:gen-class)
  (:require [hawk.core :as hawk]
            [clojure.java.io :as io]))



(def dir-being-watched "/home/morrisseymarr/watched")

(defn filter-files-with-pattern [dir pattern except-for]
  (let [f (clojure.java.io/file dir)]
    (->> (file-seq f)
         (filter 
          #(let [f-name (.getName %)]
             (and
              (re-matches pattern f-name)
              (not= except-for f-name)))))))

(def pattern-to-filter #"^mantab.*$")

(defn delete-files [dir pattern except]
  (doseq [x (filter-files-with-pattern dir pattern except)]
    (io/delete-file x)))

(defn when-file-changed [ctx e]
  (delete-files dir-being-watched pattern-to-filter "mantab.jpg")
  ctx)

(defn watch [dir]
  (hawk/watch! [{:paths [dir]
                 :handler when-file-changed}]))

(watch dir-being-watched)

