  (ns stormy.core
    (:use
     [twitter.oauth]
     [twitter.callbacks]
     [twitter.callbacks.handlers]
     [twitter.api.streaming])
    (:require
     [clojure.data.json :as json]
     [http.async.client :as ac])
    (:import
     (twitter.callbacks.protocols AsyncStreamingCallback)))

  (def ^:dynamic *creds* (make-oauth-creds ))

  ; retrieves the user stream, waits 1 minute and then cancels the async call
  (def *response* (user-stream :oauth-creds *creds*))
  (Thread/sleep 60000)
  ((:cancel (meta *response*)))

  ; supply a callback that only prints the text of the status
  (def ^:dynamic
       *custom-streaming-callback*
       (AsyncStreamingCallback. (comp println #(:text %) json/read-json bodypart-print)
                        (comp println response-return-everything)
                    exception-print))

  (statuses-filter :params {:track "and"}
           :oauth-creds *creds*
           :callbacks *custom-streaming-callback*)


