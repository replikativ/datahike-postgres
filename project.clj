(defproject io.replikativ/datahike-postgres "0.1.0-SNAPSHOT"
  :description "Datahike with Postgres as data storage."
  :license {:name "Eclipse"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :url "https://github.com/replikativ/datahike-postgres"

  :dependencies [[org.clojure/clojure       "1.10.1"   :scope "provided"]
                 [environ "1.1.0"]
                 [org.clojars.mihaelkonjevic/konserve-pg "0.1.2"]
                 [io.replikativ/datahike "0.3.0"]]

  :aliases {"test-clj"     ["run" "-m" "datahike-postgres.test/core_test-clj"]
            "test-all"     ["do" ["clean"] ["test-clj"]]}

  :profiles {:dev {:source-paths ["test"]
                   :dependencies [[org.clojure/tools.nrepl     "0.2.13"]
                                  [org.clojure/tools.namespace "0.3.1"]]}})

