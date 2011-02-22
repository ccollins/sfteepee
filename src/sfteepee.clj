(ns sfteepee
  (:use [clojure.contrib.def :only [defvar]])
  (:import [com.jcraft.jsch JSch]))

(defvar *channel*)

(defmacro with-connection [opts & body]
  `(let [user# (:user ~opts)
         password# (:password ~opts)
         keyfile# (:keyfile ~opts)
         passphrase# (:passphrase ~opts)
         host# (:host ~opts)
         port# (:port ~opts)]
     (let [jsch# (JSch.)]
       (if-not (nil? keyfile#)
         (if-not (nil? passphrase#)
           (let [bytes# (.getBytes passphrase#)]
             (.addIdentity jsch# keyfile# bytes#))
           (.addIdentity jsch# keyfile#)))
       (let [session# (.getSession jsch# user# host# port#)]
         (.setConfig session# "StrictHostKeyChecking" "no")
         (if-not (nil? password#)
           (.setPassword session# password#))
         (.connect session#)
         (binding [*channel* (doto (.openChannel session# "sftp")
                               (.connect))]
           (try
             ~@body
             (finally
              (.disconnect *channel*)
              (.disconnect session#))))))))

(defn pwd []
  (.pwd *channel*))

(defn lpwd []
  (.lpwd *channel*))

(defn cd [path]
  (.cd *channel* path))

(defn lcd [path]
  (.lcd *channel* path))

(defn mkdir [path]
  (.mkdir *channel* path))

(defn rmdir [path]
  (.rmdir *channel* path))

(defn chgrp [gid path]
  (.chgrp *channel* gid path))

(defn chmod [perms path]
  (.chmod *channel* perms path))

(defn chown [uid path]
  (.chown *channel* uid path))

(defn ls
  ([] (ls (pwd) #".*"))
  ([path] (ls path #".*"))
  ([path regex]
     (let [entries (map
                    (fn [x] {:attrs (.getAttrs x)
                            :filename (.getFilename x)
                            :longname (.getLongname x)})
                    (.ls *channel* path))]
       (filter (fn [item] (re-matches regex (:filename  item))) entries))))

(defn put
  ([src]
     (.put *channel* src))
  ([src dest]
     (.put *channel* src dest)))

(defn grab
  ([src]
     (.get *channel* src
           (str (lpwd) "/" (:filename (first (ls src))))))
  ([src dest]
     (.get *channel* src dest)))

(defn rm [path]
  (.rm *channel* path))

(defn move [src dest]
  (.rename *channel* src dest))
