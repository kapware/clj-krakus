(ns clj-krakus.story
  (:require [clojure.spec :as s]))

(s/def ::name (s/and string? not-empty))
(s/def ::health number?)

(s/def ::citizen (s/keys :req-un [::name ::health]))

(s/fdef citizen
        :args (s/cat :name ::name)
        :ret ::citizen)

(defn citizen [name]
  {:name name :health 100})

(defn promote
  ([citizen title]
   (assoc citizen :title title))
  ([citizen title power]
   (assoc (promote citizen title) :power power)))

(defn king [name]
  (promote (citizen name) :king))

(defn castle [name]
  {:name name})

(defn occupy
  ([structure citizen]
   (update structure :citizens (fnil conj []) citizen))
  ([structure citizen-fn & names]
   (reduce occupy structure
           (map citizen-fn names))))

(defn knight [name]
  (promote (citizen name) :knight 100))

(defn dragon [name]
  {:name name
   :health 1000
   :power 1000})

(defn food [modifier]
  {:energy modifier :size 10})

(defn ram [] (food 10))
(defn cow [] (food 20))
(defn water [] {:thirst -1 :size 10})

(defn eat [who what]
  (merge-with + who what))

(defn hungry? [who]
  (< (:energy who 0) 100))

(defn store [structure good]
  (update structure :goods (fnil conj []) good))

(defn vector-index [pred coll]
  (first (keep-indexed #(when (pred %2) %1) coll)))

(defn inhabitant-index-by-name [structure name]
  (vector-index #(= (:name %) name) (:citizens structure)))

(defn consume[structure inhabitant-name]
  (let [food (peek (:goods structure))
        inhabitant-num (inhabitant-index-by-name structure inhabitant-name)]
    (-> structure
        (update-in [:citizens inhabitant-num] eat food)
        (update :goods pop))))

(defn attack [target who]
  (update target :health (fnil - 0) (:power who 0)))

(defn alive? [who]
  (> (:health who 0) 0))

(defn dead? [who]
  (not (alive? who)))

(defn cobbler[name]
  (citizen name))

(defn sulfur[]
  {:thirst 500})

(defn tar[]
  {:size 500})

(defn ram-with-sulfur-and-tar[]
  (merge-with + (ram) (sulfur) (tar)))

(defn thirsty? [who]
  (> (:thirst who) 50))

(defn hangover?[who]
  (> (:thirst who) 100))

(defn vistula[]
  (repeatedly water))

(defn inhabitants-by-name [structure inhabitant-name]
  (filter #(= (:name %) inhabitant-name) (:citizens structure)))

(defn consume-all[structure]
  (let [who (last (filter #(and (hungry? %) (alive? %)) (:citizens structure)))
        what (first (:goods structure))]
    (if (and who what)
      (-> structure
          (consume (:name who))
          (recur))
      structure)))

(defn tire[who]
  (-> who
      (update :energy (fnil - 0) 10)
      (update :size (fnil - 0) 10)))

(defn attack-by-name[structure target-name attacker-name]
  (let [target-index (inhabitant-index-by-name structure target-name)
        attacker-index (inhabitant-index-by-name structure attacker-name)
        attacker (first (inhabitants-by-name structure attacker-name))]
    (-> structure
        (update-in [:citizens target-index] attack attacker)
        (update-in [:citizens attacker-index] tire ))))

(defn attack-some[structure target-name & attacker-names]
  (reduce #(attack-by-name %1 target-name %2) structure attacker-names))

(defn attack-back[structure attacker-name & target-names]
  (reduce #(attack-by-name %1 %2 attacker-name) structure target-names))

(defn vec-remove
  "remove elem in coll"
  [pos coll]
  (vec (concat (subvec coll 0 pos) (subvec coll (inc pos)))))

(defn pieceof [who no]
  (-> who
      (update :name str "-" no)
      (assoc :health -1000)))

(defn blow-to-pieces [structure target-name]
  (let [target-index (inhabitant-index-by-name structure target-name)]
    (as-> structure xs
     (update xs :citizens (partial vec-remove target-index))
     (reduce #(occupy %1 {:name (str "Smok-" %2) :health -100}) xs (range 1 100)))))

(defn collect-dead [structure]
  (update structure :citizens #(vec (remove dead? %))))

(defn store-all [structure coll]
  (reduce store structure coll))

(defn story []
  [#(occupy % king "Krakus")
   #(occupy % knight "Małowuj" "Minigniew" "Gromisław" "Nowosiodł" "Twardomir" "Włościobyt")
   #(store-all % (repeatedly 20 cow))
   #(store-all % (repeatedly 50 ram))
   #(occupy % dragon "Smok")
   #(consume-all %)
   #(attack-some % "Smok" "Małowuj" "Minigniew" "Gromisław" "Nowosiodł" "Twardomir" "Włościobyt")
   #(attack-back % "Smok" "Małowuj" "Minigniew" "Gromisław" "Nowosiodł" "Twardomir" "Włościobyt")
   #(collect-dead %)
   #(occupy % cobbler "Skuba")
   #(consume-all %)
   #(store % (ram-with-sulfur-and-tar))
   #(consume-all %)
   #(store-all % (take 100 (vistula)))
   #(consume-all %)
   #(blow-to-pieces % "Smok")])

(defn story-line[n]
  (reduce #(%2 %1) (castle "Wawel")
         (take n (story))))

