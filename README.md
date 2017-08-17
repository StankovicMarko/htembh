# htembh

Clojure web app for HTEMBH users

## Prerequisites

You will need [Leiningen][1] 2.0 or above installed.

MySQL with "htembh_dev" database created

[1]: https://github.com/technomancy/leiningen


##Breaking changed

htembh 2.0

Project is updated to newest version of luminus
follow instructions below to be able to start project properly


## Running

Add in root folder (luminus template):

    profiles.clj

    and in it:

    {:profiles/dev  {:env {:database-url "mysql://localhost:3306/htembh_dev?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&user=USERNAME&password=PASSWORD"}}
 :profiles/test {:env {:database-url "mysql://localhost:3306/htembh_test?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&user=USERNAME&password=PASSWORD"}}}

    

Run migrations:

    lein migratus migrate


Run sql scripts from resources/sql in this order:

    topic.sql > questions.sql > responses.sql > connection.sql


To start a web server for the application, run:

    lein run
    

To compile ClojureScript front-end, run:

    lein figwheel

## License

Copyright © 2017 FIXME