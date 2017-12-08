(ns i-b-w-y.core
  (:gen-class)
  (:require [hawk.core :as hawk]
            [clojure.java.io :as io]))

(defn filter-files-with-pattern [dir pattern except-for]
  (let [f (clojure.java.io/file dir)]
    (->> (file-seq f)
         (filter 
          #(let [f-name (.getName %)]
             (and
              (re-matches pattern f-name)
              ((complement contains?) except-for f-name)))))))

(defn delete-files [dir pattern except]
  (doseq [x (filter-files-with-pattern dir pattern except)]
    (io/delete-file x)))

(defn read-rule [rule-path]
  (read-string (slurp rule-path)))

(defn watch [{:keys [dir pattern exclude]}]
  (hawk/watch!
   [{:paths [dir]
     :handler (fn [ctx e]
                (delete-files dir pattern exclude))}]))

(defn -main [& [args]]
  (when args
    (watch (read-rule args))
    (println "ready to watch for you!")))

