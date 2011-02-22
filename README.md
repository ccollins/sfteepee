Clojure wrapper for JSch SFTP 
=====

JSch can be found [here](http://www.jcraft.com/jsch/). It is also in clojars and can be added to your project (if using leiningen) like this:
    (defproject example_project "0.0.1"
      :description "Example"
      :dependencies [[org.clojure/clojure "1.2.0"]
                     [org.clojure/clojure-contrib "1.2.0"]
                     [com.jcraft/jsch "0.1.42"]])

Building
----------
requires [leiningen](http://github.com/technomancy/leiningen)
    lein clean && lein deps && lein compile && lein jar

Usage
----------
    (with-connection [opts & body])

Examples
----------
    (with-connection
      {:user "foo"
       :keyfile "/path/to/my/keyfile"
       :host "my.sftp.host.example.com"}
      (cd "somewhere")
      (grab "aFile.txt"))

    (with-connection
      {:user "foo"
       :keyfile "/path/to/my/keyfile"
       :passphrase "opensesame"
       :host "my.sftp.host.example.com"}
      (cd "somewhere")
      (grab "aFile.txt"))

    (with-connection
      {:user "foo"
       :password "bar"
       :host "my.sftp.host.example.com"}
      (cd "somewhere")
      (grab "aFile.txt"))

Available options:
 * user
 * password
 * keyfile
 * passphrase
 * host
 * port (default 22)