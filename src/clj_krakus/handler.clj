(ns clj-krakus.handler
  (:require [compojure.api.sweet :refer :all]
            [ring.util.http-response :refer :all]
            [schema.core :as s]
            [clj-krakus.story :as story]))

(def swagger-meta {:swagger
                   {:ui "/"
                    :spec "/swagger.json"
                    :data {:info {:title "clj-krakus"
                                  :description "Krakus api"}
                           :tags [{:name "api", :description "story"}]}}})

(def app
  (api swagger-meta
    (context "/api" [] :tags ["api"]
       (GET "/story/:line" [line]
         :summary "story by line"
         (ok (story/story-line (Integer/parseInt line)))))))
