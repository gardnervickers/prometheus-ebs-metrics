(defproject prometheus-ebs-metrics "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.562"]
                 [andare "0.7.0"]
                 [org.clojure/tools.cli "0.3.5"]]
  :plugins [[lein-cljsbuild "1.1.6"]]
  :cljsbuild {
              :repl-listen-port 10555
              :builds {:dev {:source-paths ["src"]
                             :incremental true
                             :assert true
                             :compiler {:output-to "target/dev/main.js"
                                        :warnings true
                                        :optimizations :none
                                        :pretty-print true
                                        :target :nodejs
                                        :source-map true
                                        :parallel-build true
                                        :main prometheus-ebs-metrics.core
                                        :output-dir "target/dev"

                                        :infer-externs true
                                        :npm-deps {"diskspace" "2.0.0"
                                                   "prom-client" "9.1.1"}
                                        :closure-warnings {:non-standard-jsdoc :off
                                                           :internet-explorer-checks :off}}}
                       :advanced {:source-paths ["src"]
                                  :incremental true
                                  :assert true
                                  :compiler {:output-to "target/main.js"
                                             :warnings true
                                             :optimizations :advanced
                                             :target :nodejs
                                             :parallel-build true
                                             :externs ["externs.js"]
                                             :main prometheus-ebs-metrics.core
                                             :infer-externs true
                                             :npm-deps {"diskspace" "2.0.0"
                                                        "prom-client" "9.1.1"}
                                             :closure-warnings {:non-standard-jsdoc :off
                                                                :internet-explorer-checks :off}}}}}

  :profiles {:dev {:source-paths ["dev"]
                   :dependencies [[tubular "1.1.1"]]}})
