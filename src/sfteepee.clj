(ns sfteepee
  (:import [com.jcraft.jsch JSch]))

(declare *conn*)

(defn connect [user password host port]
  (let [session (-> (new com.jcraft.jsch.JSch) (.getSession user host port))]
    (doto session
       (.setConfig "StrictHostKeyChecking" "no")
       (.setPassword password)
       (.connect))
    (def *conn* (doto (.openChannel session "sftp") (.connect)))))

(defn disconnect []
  (.disconnect *conn*))

(defn pwd []
  (.pwd *conn*))

(defn lpwd []
  (.lpwd *conn*))

(defn cd [path]
  (.cd *conn* path))

(defn lcd [path]
  (.lcd *conn* path))

(defn mkdir [path]
  (.mkdir *conn* path))

(defn rmdir [path]
  (.rmdir *conn* path))

(defn chgrp [gid path]
  (.chgrp *conn* gid path))

(defn chmod [perms path]
  (.chmod *conn* perms path))

(defn chown [uid path]
  (.chown *conn* uid path))

(defn ls
  ([] (ls (pwd) #".*"))
  ([path] (ls path #".*"))
  ([path regex]
     (let [entries (map
                    (fn [x] {:attrs (.getAttrs x)
                             :filename (.getFilename x)
                             :longname (.getLongname x)})
                    (.ls *conn* path))]
       (filter (fn [item] (re-matches regex (:filename  item))) entries))))

(defn put
  ([src]
     (.put *conn* src))
  ([src dest]
     (.put *conn* src dest)))

(defn grab
  ([src]
     (.get *conn* src
           (str (lpwd) "/" (:filename (first (ls src))))))
  ([src dest]
     (.get *conn* src dest)))

(defn copy [src dest]
  (.get *conn* src dest))

(defn rm [path]
  (.rm *conn* path))

(defn rename [src dest]
  (.rename *conn* src dest))

(defn move [src dest]
  (copy *conn* src dest)
  (rm *conn* src))
