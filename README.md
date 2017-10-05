# htembh

Clojure web app for HTEMBH users

## Prerequisites

You will need:

[Leiningen][1] 2.0 or above installed.

[Postgresql][2] 9.3 or above installed
with "htembh_dev" database created
and user that can select and insert on all tables.

[1]: https://github.com/technomancy/leiningen
[2]: https://www.postgresql.org/

## Breaking changed

htembh 0.3.0 -
Project has been updated to be used on postgresql db

htembh 0.2.0 -
Project is updated to newest version of luminus
follow instructions below to be able to start project properly


## Running

Add in root folder (luminus template):

    profiles.clj

    and in it:

    {:profiles/dev {:env {:database-url "jdbc:postgresql://localhost/htembh_dev?user=(USERNAME)password=(PASSWORD)"}}
     :profiles/test {:env {:database-url "jdbc:postgresql://localhost/htembh_dev?user=(USERNAME)&password=(PASSWORD)"}}}

    

Run migrations:

    lein migratus migrate


Run sql scripts from resources/sql in this order:

    topic.sql > questions.sql > responses.sql > qu_re_connection.sql


To start a web server for the application, run:

    lein run
    

To compile ClojureScript front-end, run:

    lein figwheel

## License

Copyright © 2017 Marko Stankovic
