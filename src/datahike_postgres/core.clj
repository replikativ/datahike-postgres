(ns datahike-postgres.core
  (:require [datahike.store :refer [empty-store delete-store connect-store scheme->index]]
            [hitchhiker.tree.bootstrap.konserve :as kons]
            [konserve-pg.core :as kp]
            [superv.async :refer [<?? S]]))

(defn convert-config
  "convert datahike config to clojure.java.jdbc config"
  [{:keys [username password host port dbname ssl sslfactory]}]
  {:dbtype "postgresql"
   :user username
   :password password
   :host host
   :port port
   :dbname dbname
   :ssl ssl
   :sslfactory sslfactory})

(defmethod empty-store :pg [config]
  (kons/add-hitchhiker-tree-handlers
   (<?? S (kp/new-pg-store (convert-config config)))))

(defmethod delete-store :pg [config]
  (kp/delete-store (convert-config config)))

(defmethod connect-store :pg [config]
  (<?? S (kp/new-pg-store (convert-config config))))

(defmethod scheme->index :pg [_]
  :datahike.index/hitchhiker-tree)
