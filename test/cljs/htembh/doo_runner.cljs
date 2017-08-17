(ns htembh.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [htembh.core-test]))

(doo-tests 'htembh.core-test)

