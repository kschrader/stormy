(ns stormy.core
  (:require
    [http.async.client :as client]
    [cheshire.core :as json]))

(def url "https://stream.twitter.com/1/statuses/sample.json")
(def cred {:user "kurt" :password "password"})

(with-open [client (client/create-client)] ;; Create client
  (let [resp (client/stream-seq client :get url :auth cred) ; The http response
        strings (client/string resp)] ; Turn the response into a string
    (doseq [part strings] ; Since this is a stream, will keep adding new tweets
        (println (:text (json/parse-string part true)))))) ; Print the tweet


