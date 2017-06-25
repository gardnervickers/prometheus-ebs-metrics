(ns prometheus-ebs-metrics.core
  (:require-macros [cljs.core.async.macros :refer [go go-loop]])
  (:require [cljs.core.async :as async]
            [clojure.tools.cli :refer [parse-opts]]
            [clojure.string]
            [clojure.set]))

(set! *warn-on-infer* true)
(enable-console-print!)

(def diskspace (js/require "diskspace"))
(def Promclient (js/require "prom-client"))

(def express (js/require "express"))
(def server (express))

(defn mount->gauge-map [mount->name]
  (reduce (fn [acc mount]
            (-> acc
                (assoc-in [mount "total"]
                          (new Promclient.Gauge
                               #js {"name" (str (get mount->name mount) "_total_bytes")
                                    "help" "Total bytes"}))
                (assoc-in [mount "used"]
                          (new Promclient.Gauge
                               #js {"name" (str (get mount->name mount) "_used_bytes")
                                    "help" "Used bytes"}))
                (assoc-in [mount "free"]
                          (new Promclient.Gauge
                               #js {"name" (str (get mount->name mount) "_free_bytes")
                                    "help" "Free Bytes"}))
                (assoc-in [mount "status"]
                          (new Promclient.Gauge
                               #js {"name" (str (get mount->name mount) "_status")
                                    "help" "Status"}))))
          {}
          (keys mount->name)))

(defn set-guage-metric! [metrics-map mount k v]
  (.set ^js/Promclient.Gauge (get-in metrics-map [mount k]) v))

(defn update-metrics! [mount guage-map results]
  (println "Updating metrics for: " mount " : " (get results mount))
  (set-guage-metric! guage-map mount "total" (or (get-in results [mount "total"]) 0))
  (set-guage-metric! guage-map mount "used" (or (get-in results [mount "used"]) 0))
  (set-guage-metric! guage-map mount "free" (or (get-in results [mount "free"]) 0)))

(defn start-collection-loop [timeout mounts mount->gauge]
  (let [diskspace ^js/diskspace diskspace]
    (go-loop []
             (println "Starting metrics gathering loop for mounts: " mounts)
             (when true
               (async/<! (async/timeout timeout))
               (doseq [mount mounts]
                 (js/diskspace.check mount
                                     (fn [err result]
                                       (if err
                                         (js/console.log err)
                                         (update-metrics! mount mount->gauge {mount (js->clj result)})))))
               (recur))
             (println "Finished metrics gathering loop"))))

(def cli-opts
  [["-p" "--port PORT" "Port number"
    :default 1234
    :parse-fn js/parseInt
    :validate [#(< 0 % 0x10000) "Must be a number between 0 and 65536"]]
   ["-v" "--volume VOLUME" "Volume path to monitor, NAME:PATH"
    :assoc-fn (fn [m k [name volume]]
                (assoc-in m [k name] volume))
    :parse-fn (fn [v] (clojure.string/split v #":"))]
   ["-h" "--help"]])

(defn -main [& args]

  (Promclient.register.clear)
  (let [{:keys [options summary]} (parse-opts args cli-opts)]
    (cond (:help options) (println summary)
          :else
          (let [mount->name (clojure.set/map-invert (:volume options))
                mounts (keys mount->name)
                mount->gauge (mount->gauge-map mount->name)]
            (println "Starting volume metrics: "  )
            (start-collection-loop 5000 mounts mount->gauge)
            (.get server "/metrics" (fn [req res]
                                      (.set res "Content-Type" Promclient.register.contentType)
                                      (.end res (.metrics Promclient.register))))
            (.listen server (:port options))))))

(set! *main-cli-fn* -main)

