(defproject clj-krakus "0.1.0-SNAPSHOT"
  :description "Krakus story written in functions"
  :dependencies [[org.clojure/clojure "1.9.0-alpha13"]
                 [metosin/compojure-api "1.1.8"]
                 [http-kit "2.2.0"]]
  :min-lein-version "2.6.1"
  :ring {:handler clj-krakus.handler/app}
  :uberjar-name "server.jar"
  :profiles {:uberjar {:aot :all
                       :main clj-krakus.main}
             :dev {:dependencies [[javax.servlet/javax.servlet-api "3.1.0"]
                                  [cider/cider-nrepl "0.14.0-SNAPSHOT"]
                                  [cheshire "5.5.0"]
                                  [ring/ring-mock "0.3.0"]
                                  [midje "1.9.0-alpha5"]
                                  [org.clojure/test.check "0.9.0"]]
                   :plugins [[ikitommi/lein-ring "0.9.8-FIX"]
                             [lein-midje "3.2"]]
                   :ring {:handler clj-krakus.handler/app
                          :nrepl {:start? true}}
                   :repl-options {:nrepl-middleware
                                  [cider.nrepl.middleware.apropos/wrap-apropos
                                   cider.nrepl.middleware.classpath/wrap-classpath
                                   cider.nrepl.middleware.complete/wrap-complete
                                   cider.nrepl.middleware.info/wrap-info
                                   cider.nrepl.middleware.inspect/wrap-inspect
                                   cider.nrepl.middleware.macroexpand/wrap-macroexpand
                                   cider.nrepl.middleware.ns/wrap-ns
                                   cider.nrepl.middleware.resource/wrap-resource
                                   cider.nrepl.middleware.stacktrace/wrap-stacktrace
                                   cider.nrepl.middleware.test/wrap-test
                                   cider.nrepl.middleware.trace/wrap-trace
                                   cider.nrepl.middleware.undef/wrap-undef]}}})
