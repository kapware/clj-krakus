(ns clj-krakus.main
     (:use clj-krakus.handler
           [org.httpkit.server :only [run-server]]
           [ring.middleware file-info file])
     (:gen-class))

(defn -main [& [port]]
    (let [port (if port (Integer/parseInt port) 5000)]
          (run-server app {:port port})))
