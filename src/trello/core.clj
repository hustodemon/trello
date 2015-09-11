(ns trello.core
  "The client namespace contains the raw HTTP functions for accessing
  and parsing the responses from the Trello API."
  (:require [clojure.string :as string]))

(def consumer (atom {}))

(def ^:dynamic *oauth-token*)
(def ^:dynamic *oauth-secret*)

(defn callfn [f & args] (apply f args))

(defn make-consumer [key secret]
  {:author "Daniel Szmulewicz <https://github.com/danielsz>"}
  (reset! consumer {:key key :secret secret}))

(defn make-client [consumer-key consumer-secret]
  (make-consumer consumer-key consumer-secret)
  callfn)

(defmacro with-user
  "Sets the user OAuth access token for write access and for accessing private user data."
  {:author "Daniel Szmulewicz <https://github.com/danielsz>"}
  [oauth-token oauth-secret & body]
  `(binding [*oauth-token* ~oauth-token
             *oauth-secret* ~oauth-secret]
     (do
       ~@body)))

(defmacro with-exception-handling 
  "A helper macro for performing request with a try catch"
  [msg & forms]
  `(try (do ~@forms)
     (catch Exception ex# 
       {:error ~msg})))
