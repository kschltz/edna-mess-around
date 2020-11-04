(defmulti task first)

(defmethod task :default
  [[task-name]]
  (println "Unknown task:" task-name)
  (System/exit 1))

(require
  '[edna.core :as edna]
  '[my-music.core :as c]
  '[cljs.build.api :as api]
  '[clojure.java.io :as io])

(defn delete-children-recursively! [f]
  (when (.isDirectory f)
    (doseq [f2 (.listFiles f)]
      (delete-children-recursively! f2)))
  (when (.exists f) (io/delete-file f)))

(defmethod task nil
  [_]
  (let [out-file "resources/public/main.js"
        out-dir "resources/public/main.out"]
    (println "Building main.js")
    (delete-children-recursively! (io/file out-dir))
    (api/build "src" {:main          'my-music.core
                      :optimizations :advanced
                      :output-to     out-file
                      :output-dir    out-dir
                      :infer-externs true})
    (delete-children-recursively! (io/file out-dir))
    (println "Build complete:" out-file)
    (System/exit 0)))

(defmethod task "mp3"
  [_]
  (let [mp3-name "my-music.mp3"]
    (println "Building" mp3-name)
    (edna/export! (c/read-music) {:type :mp3, :out (io/file mp3-name)})
    (println "Build complete:" mp3-name)
    (System/exit 0)))

(task *command-line-args*)
