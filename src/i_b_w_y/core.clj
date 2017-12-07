(ns i-b-w-y.core
  (:gen-class)
  (:require [hawk.core :as hawk]))

(hawk/watch! [{:paths ["/home/morrisseymarr/watched"]
               :handler (fn [ctx e]
                          (println "event: " e)
                          (println "context: " ctx)
                          ctx)}])

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

