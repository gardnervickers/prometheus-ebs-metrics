(ns prometheus-ebs-metrics.core
  (:require-macros [cljs.core.async.macros :refer [go go-loop]])
  (:require [cljs.core.async :as async]
            [clojure.tools.cli :refer [parse-opts]]
            [clojure.string]
            [clojure.set]
            [cljs.nodejs :as node]
            diskspace
            prom-client))

(set! *warn-on-infer* true)
(enable-console-print!)

(def Promclient prom-client)
(def http (node/require "http"))

(defn mount->gauge-map [mount->name]
  (reduce (fn [acc mount]
            (-> acc
                (assoc-in [mount "total"]
                          (new prom-client/Gauge
                               (str (get mount->name mount) "_total_bytes")
                               "Total Bytes"
                               #js []))
                (assoc-in [mount "used"]
                          (new prom-client/Gauge
                               (str (get mount->name mount) "_used_bytes")
                               "Used Bytes"
                               #js []))
                (assoc-in [mount "free"]
                          (new prom-client/Gauge
                               (str (get mount->name mount) "_free_bytes")
                               "Free Bytes"
                               #js []))
                (assoc-in [mount "status"]
                          (new prom-client/Gauge
                               (str (get mount->name mount) "_status")
                               "Status"
                               #js []))))
          {}
          (keys mount->name)))

(defn set-gauge-metric! [metrics-map mount k v]
  (.set ^js/Promclient.Gauge (get-in metrics-map [mount k]) v))

(defn update-metrics! [mount gauge-map results]
  (println "Updating metrics for: " mount " : " (get results mount))
  (set-gauge-metric! gauge-map mount "total" (or (get-in results [mount "total"]) 0))
  (set-gauge-metric! gauge-map mount "used" (or (get-in results [mount "used"]) 0))
  (set-gauge-metric! gauge-map mount "free" (or (get-in results [mount "free"]) 0)))

(defn start-collection-loop [timeout mounts mount->gauge]
  (go-loop []
           (println "Starting metrics gathering loop for mounts: " mounts)
           (async/<! (async/timeout timeout))
           (doseq [mount mounts]
             (diskspace/check mount
                              (fn [err result]
                                (if err
                                  (js/console.log err)
                                  (update-metrics! mount mount->gauge {mount (js->clj result)})))))
           (recur)))

(def metrics-handler
  (fn [req ^js/http.ServerResponse res]
    (.setHeader res "Content-Type" Promclient.register.contentType)
    (.end res (.metrics ^js/Promclient.register Promclient.register))))

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
    (println "Options: " options)
    (cond (:help options) (println summary)
          :else
          (let [mount->name (clojure.set/map-invert (:volume options))
                mounts (keys mount->name)
                mount->gauge (mount->gauge-map mount->name)
                server (.createServer http metrics-handler)]
            (println "Starting volume metrics: "  )
            (start-collection-loop 5000 mounts mount->gauge)
            (.listen server (:port options))))))

(set! *main-cli-fn* -main)