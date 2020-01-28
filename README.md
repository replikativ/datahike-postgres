# datahike-postgres

[Datahike](https://github.com/replikativ/datahike) with [Postgres](https://www.postgresql.org/) as data storage.


## Prerequisits

For datahike-postgres you will need to know the configuration of a running Postgres server as well as the name of an existing Postgres user.

On Ubuntu, there are at least two ways to prepare a Postgres server for datahike:

1. The easy way: Use a docker image
2. Install and prepare Postgres on your native system

For instructions, see below.


## Usage

Add to your leiningen dependencies:

[![Clojars Project](http://clojars.org/io.replikativ/datahike-postgres/latest-version.svg)](http://clojars.org/io.replikativ/datahike-postgres)

You can use the postgres backend now using the keyword `:pg` in the backend

```clojure
(ns project.core
  (:require [datahike.api :as d]
            [datahike-postgres.core]))

;; Create a config map with postgres as storage medium
(def config {:backend :pg
             :host "localhost"
             :port 5432
             :username "alice"
             :password "foo"
             :path "/example-db"})

;; Alternatively, use an URI as configuration
;; (def config "datahike:pg://alice:foo@localhost:5432/config-test")

;; Create a database at this place, by default configuration we have a strict
;; schema and temporal index
(d/create-database config)

(def conn (d/connect config))

;; The first transaction will be the schema we are using:
(d/transact conn [{:db/ident :name
                   :db/valueType :db.type/string
                   :db/cardinality :db.cardinality/one }
                  {:db/ident :age
                   :db/valueType :db.type/long
                   :db/cardinality :db.cardinality/one }])

;; Let's add some data and wait for the transaction
(d/transact conn [{:name  "Alice", :age   20 }
                  {:name  "Bob", :age   30 }
                  {:name  "Charlie", :age   40 }
                  {:age 15 }])

;; Search the data
(d/q '[:find ?e ?n ?a
       :where
       [?e :name ?n]
       [?e :age ?a]]
  @conn)
;; => #{[3 "Alice" 20] [4 "Bob" 30] [5 "Charlie" 40]}

;; Clean up the database if it is not needed any more
(d/delete-database config)
```


### Postgres via Docker image


Install [Docker](https://www.docker.com/)
```bash
sudo apt install docker
```

Pull the docker image
```bash
sudo docker pull postgres
```

Configure the server
```bash
sudo docker run --name test-pg -p <PORT>:<PORT> -e POSTGRES_PASSWORD=<PASSWORD> -e POSTGRES_USER=<USER> -e POSTGRES_DB=<DATABASENAME> -d postgres
```


### Native Postgres Installation and Configuration

In order to use Postgres as backend, you need a current Postgres installation on your target system.
See the [Postgres](https://www.postgresql.org/download/) website for installation instructions for any target system.

<!-- [PGAdmin](https://www.pgadmin.org/download/) is a useful tool to interact with Postgres.-->

For Ubuntu, you can simply use the package manager:

```bash
sudo apt install postgresql
```

**Controlling the server**

The Postgres server on Ubuntu is controlled with commands like the following:

```bash
sudo service postgresql start
sudo service postgresql stop
```

**Check or Change Port**

Port 5432 is the default port for Postgres.

To be sure which port is being used, you can try the following command:

```bash
sudo netstat -plunt |grep postgres
# => tcp 0 0 127.0.0.1:<PORT> 0.0.0.0:* LISTEN 1235/postgres
```

You can change the port by editing the configuration file, found with

```bash
locate postgresql.conf
# => something like /etc/postgresql/10/main/postgresql.conf
```

After the editing, you will have to restart the server:

```bash
sudo service postgresql restart
```

**Create User**

A user without a password should suffice:

```bash
sudo -u postgres createuser <USER>
```

For adding a password, do
```bash
sudo -u postgres psql
```
And then, on the psql prompt
```bash
alter user <USER> with encrypted password '<PASSWORD>';
```


## Run tests

For the tests to be working, you need to
- use **port** **5432**
- create a **user** named **alice**
- with **password** **foo**


If you have Docker installed on Ubuntu, you can simply do:
```bash
sudo docker run --name test-pg -p 5432:5432 -e POSTGRES_PASSWORD=foo -e POSTGRES_USER=alice -e POSTGRES_DB=config-test -d postgres
```

Otherwise, follow the instructions above to configure your server correctly.


## License

Copyright © 2019  lambdaforge UG (haftungsbeschränkt)

This program and the accompanying materials are made available under the terms of the Eclipse Public License 1.0.
