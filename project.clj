(defproject io.replikativ/datahike-postgres "0.0.1-SNAPSHOT"
  :description "Datahike with Postgres as data storage."
  :license {:name "Eclipse"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :url "https://github.com/replikativ/datahike-postgres"

  :dependencies [[org.clojure/clojure       "1.10.1"   :scope "provided"]
                 [org.clojure/clojurescript "1.10.520" :scope "provided"]
                 [org.clojars.mihaelkonjevic/konserve-pg "0.1.2" :scope "provided"]
                 [io.replikativ/datahike "0.2.0"]]

  :plugins [[lein-cljsbuild "1.1.7"]]

  :global-vars {*warn-on-reflection*   true
                *print-namespace-maps* false}
  :jvm-opts ["-Xmx2g" "-server"]

  :aliases {"test-clj"     ["run" "-m" "datahike-postgres.test/test-clj"]
            "test-cljs"    ["do" ["cljsbuild" "once" "release" "advanced"]
                           ["run" "-m" "datahike-postgres.test/test-node" "--all"]]
            "node-repl"    ["run" "-m" "user/node-repl"]
            "browser-repl" ["run" "-m" "user/browser-repl"]
            "test-all"     ["do" ["clean"] ["test-clj"] ["test-cljs"]]}

  :profiles {:1.9 {:dependencies [[org.clojure/clojure         "1.10.1"   :scope "provided"]
                                  [org.clojure/clojurescript   "1.10.520" :scope "provided"]]}
             :dev {:source-paths ["bench/src" "test" "dev"]
                   :dependencies [[org.clojure/tools.nrepl     "0.2.13"]
                                  [org.clojure/tools.namespace "0.3.1"]]}
             :aot {:aot [#"datahike-postgres\.(?!query-v3).*"]
                   :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}}

  :clean-targets ^{:protect false} ["target"
                                    "release-js/datahike-postgres.bare.js"
                                    "release-js/datahike-postgres.js"])

