(ns sfteepee
  (:use [clojure.contrib.def :only [defvar]])
  (:import [com.jcraft.jsch JSch]))

(defvar *channel*)

(defmacro with-connection [user password host port & body]
  `(let [session# (doto (.getSession (JSch.) ~user ~host ~port)
                    (.setConfig "StrictHostKeyChecking" "no")
                    (.setPassword ~password)
                    (.connect))]
     (binding [*channel* (doto (.openChannel session# "sftp")
                           (.connect))]
       (try
         ~@body
         (finally
          (.disconnect *channel*)
          (.disconnect session#))))))

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
