(assembly-load "System.Xml")

(ns clasp.core
  (:require [clojure.string :as str])
  (:import [System.IO StreamReader]
           [System.Xml XmlReader XmlTextReader]))

(def input-file "C:\\mski\\dev\\org.net\\PeoplePage.aspx")

(def lines-to-swap (atom []))

(defn read-xml [file]
  (let [sr (StreamReader. file)]
    (dotimes [n 5]
      (.ReadLine sr))
    (let [rd (XmlTextReader. sr)]
      (try
        (.set_Namespaces rd false)
        ;;(println (.ReadToEnd sr))
        (.ReadToFollowing rd "asp:FormView")
        (.MoveToFirstAttribute rd)
        (println (. rd Value))
        (finally 
         (.Dispose sr)
         (.Dispose rd))))))

(defn gen-pattern [property elem]
  (re-pattern (str "<asp:" elem " ID=\"" property elem "\".*/>")))

(defn textbox->hidden [file property]
  (let [text (slurp file)
        push-lines (fn [pattern]
                     (loop [matcher (re-matcher pattern text)]
                       (when-let [line (re-find matcher)]
                         (swap! lines-to-swap conj
                                {:orig line
                                 :new (-> line 
                                          (str/replace "asp:TextBox" "asp:HiddenField")
                                          (str/replace "Text=" "Value="))})
                         (recur matcher))))]
    (push-lines (gen-pattern property "TextBox"))))
