(ns my-music.core
 (:require [edna.core :as edna]
           [edna.parse :refer [instruments]]
           [clojure.java.io :as io]
           [clojure.java.shell :refer [sh]]))

(defn read-music []
 (load-file "src/my_music/music.clj"))

(defonce state (atom nil))

(defmacro deftones
 [tune tune-data & {:keys [type play?]
                    :or   {play? true
                           type  :mp3}
                    :as   opts}]

 `(let [file# (io/file (str ~tune "." (name ~type)))
        exp# (edna/export! ~tune-data
                           {:type ~type
                            :out  file#})
        play-fn# (fn [] (future
                         (sh "rhythmbox"
                             (str (.getAbsolutePath exp#)))))]
   (when ~play? (play-fn#))
   (def ~(symbol tune) play-fn#)))





(def notes
 [:c :c#
  :d :d#
  :e
  :f :f#
  :g :g#
  :a :a#
  :b])

(comment
 (deftones
  "autumn-voyage"

  [[:tubular-bells {:tempo  110
           :volume 100
           :octave 4}
    [[:e 1/8 :+e :+d 1/2 :b]
     [1/4 :+c 1/8 :b :a 1/2 :b]
     [1/4 :+c 1/8 :b :a 1/2 :g 1/6 :f# :g :a 1/2 :b]
     [:e 1/8 :+e :+d 1/2 :b]
     [1/4 :+c 1/8 :b :a 1/2 :b]
     [1/4 :+c 1/8 :b :a 1/2 :g 1/6 :f# :g :f# 1/2 :e]]]
   ])

 (sort instruments)
(deftones "chords" [:percussion [(repeat 10 [:a :a 1/16 :b :b :c])]])
 (def cfg {:server-type :ion
           :region "us-east-1"
           :system "meu-datomico"
           :creds-profile "perfil-perfilado"
           :endpoint "http://entry.meu-datomico.us-east-1.datomic.net:8182/"
           :proxy-port 8182})

 (def client (d/client cfg))

 )