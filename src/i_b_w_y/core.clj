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
              ((complement contains?) except-for f-name))))
         (map #(.getPath %)))))

(defn delete-files [dir pattern except]
  (doseq [x (filter-files-with-pattern dir pattern except)]
    (io/delete-file x)))

(defn read-rule [rule-path]
  (read-string (slurp rule-path)))

(defn watch [rule]
  (let [remover (:remover rule) 
        mover (:mover rule) 
        not-nil? (comp not nil?)]
    (when (not-nil? remover)
      (let [{:keys [pattern dir]} remover]
        (when (and (not-nil? pattern) (not-nil? dir))
          (hawk/watch! 
           [{:paths [dir]
             :handler
             (fn [ctx e]
               (when (= :modify (:kind e))
                 (delete-files
                  dir
                  pattern
                  #{(.getName (:file e))})))}]))))))

(defn -main [& [args]]
  (when args
    (if (watch (read-rule args))
      (println "ready to watch for you!")
      (println "config is not valid"))))


