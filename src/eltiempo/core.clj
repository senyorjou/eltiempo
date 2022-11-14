(ns eltiempo.core
  (:require [clj-http.client :as client]
            [cheshire.core :as json])
  (:gen-class))




(def api-key (clojure.string/trim (slurp "resources/api-key.txt")))
(def url (str "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=" api-key))


(def cities ["barcelona" "reus" "london" "bilbao" "Saint Petersburg" "paris" "Cadiz"] )


(defn get-city-data [city]
  (let [url (format url city)]
    (println "Downloading data for city:" city)
    (client/get url)))


(defn k->c [kelvin]
  (int (- kelvin 273)))


(defn extract-temps [data]
  (let [json-body (json/parse-string data true)
        main (:main json-body)
        temp (k->c (:temp main))]
    temp))


(defn process-city [city]
  (let [temp (-> city
                 get-city-data
                 :body
                 extract-temps)]
    (format "%s: %d" city temp)))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println (map #(process-city %) cities)))
