Clojure wrapper for JSch SFTP 
=====

Building/Distributing
---------------------

requires [leiningen](http://github.com/technomancy/leiningen)

Full build: 
lein clean && lein deps && lein compile && lein jar

(with-connection [opts & body])

Example:
(with-connection
  {:user "foo"
   :keyfile "/path/to/my/keyfile"
   :host "my.sftp.host.example.com"}
  (cd "somewhere")
  (grab "aFile.txt"))

Available options:
user
password
keyfile
passphrase
host
port (default 22)