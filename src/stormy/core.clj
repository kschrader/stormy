(ns stormy.core
  (:require
    [http.async.client :as client])
  (:use
    [cheshire.core]
    [propertea.core]))

(def props (read-properties "twitter.properties"))

(def url "https://stream.twitter.com/1/statuses/sample.json")
(def cred {:user (:username props) :password (:password props)})

(defn filter-tweet [filter-word tweet user]
  (if tweet
    (if (.contains tweet filter-word)
      (println user "\t :: \t" tweet)))
  )

(with-open [client (client/create-client)]
  (let [resp (client/stream-seq client :get url :auth cred)
        strings (client/string resp)]
    (doseq [part strings]
        (filter-tweet "and" (:text (parse-string part true)) (:screen_name (:user (parse-string part true)))))))


