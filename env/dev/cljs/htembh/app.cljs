(ns ^:figwheel-no-load htembh.app
  (:require [htembh.core :as core]
            [devtools.core :as devtools]))

(enable-console-print!)

(devtools/install!)

(core/init!)
