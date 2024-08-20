#!/usr/bin/env bb

(require '[babashka.pods :as pods])
(pods/load-pod 'rorokimdim/stash "0.3.1")
(require '[pod.rorokimdim.stash :as stash])

(stash/init {"encryption-key" "foo"
             "stash-path" "foo.stash"
             "create-stash-if-missing" true})

(stash/set 20221210092035 "dream entry")

