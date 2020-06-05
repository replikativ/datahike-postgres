(ns datahike-postgres.core
  (:require [datahike.store :refer [empty-store delete-store connect-store scheme->index default-config config-spec]]
            [datahike.config :refer [int-from-env bool-from-env]]
            [hitchhiker.tree.bootstrap.konserve :as kons]
            [konserve-pg.core :as kp]
            [environ.core :refer [env]]
            [clojure.spec.alpha :as s]
            [superv.async :refer [<?? S]]))

(defmethod empty-store :pg [config]
  (kons/add-hitchhiker-tree-handlers
   (<?? S (kp/new-pg-store config))))

(defmethod delete-store :pg [config]
  (kp/delete-store config))

(defmethod connect-store :pg [config]
  (<?? S (kp/new-pg-store config)))

(defmethod scheme->index :pg [_]
  :datahike.index/hitchhiker-tree)

(defmethod default-config :pg [config]
  (merge
   {:dbtype "postgresql"
    :user (:datahike-store-user env)
    :password (:datahike-store-password env)
    :host (:datahike-store-host env)
    :port (int-from-env :datahike-store-port nil)
    :dbname (:datahike-store-dbname env)
    :ssl (bool-from-env (:datahike-store-ssl env) nil)
    :sslfactory (:datahike-store-ssl-factory env)}
   config))

(s/def :datahike.store.pg/backend #{:pg})
(s/def :datahike.store.pg/user string?)
(s/def :datahike.store.pg/password string?)
(s/def :datahike.store.pg/host string?)
(s/def :datahike.store.pg/dbtype string?)
(s/def :datahike.store.pg/port int?)
(s/def :datahike.store.pg/dbname string?)
(s/def :datahike.store.pg/ssl boolean?)
(s/def :datahike.store.pg/sslfactory string?)
(s/def ::pg (s/keys :req-un [:datahike.store.pg/backend
                             :datahike.store.pg/dbname
                             :datahike.store.pg/user
                             :datahike.store.pg/password
                             :datahike.store.pg/dbtype
                             :datahike.store.pg/host
                             :datahike.store.pg/port]
                    :opt-un [:datahike.store.pg/ssl
                             :datahike.store.pg/sslfactory]))

(defmethod config-spec :pg [_] ::pg)

