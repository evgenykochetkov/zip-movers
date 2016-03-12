(ns zip-movers.core
  (:require [clojure.zip :as zip]))

(defn remove-and-up
  "(c) Alex Dowad (see https://groups.google.com/forum/#!topic/clojure/8HI1-Pi3efA)"
  [loc]
  (let [[node {:keys [l r ppath pnodes] :as path}] loc]
    (if (nil? path)
      (throw (new #?(:clj Exception :cljs js/Error) "Remove at top"))
      (if (pos? (count l))
        (zip/up (with-meta [(peek l)
                            (assoc path :l (pop l) :changed? true)]
                           (meta loc)))
        (with-meta [(zip/make-node loc (peek pnodes) r)
                    (and ppath (assoc ppath :changed? true))]
                   (meta loc))))))

(defn remove-and-left
  [loc]
  (let [[node {:keys [l r ppath pnodes] :as path}] loc]
    (if (nil? path)
      (throw (new #?(:clj Exception :cljs js/Error) "Remove at top"))
      (if (pos? (count l))
        (with-meta [(peek l) (assoc path :l (pop l) :changed? true)] (meta loc))
        (throw (new #?(:clj Exception :cljs js/Error) "No left sibling"))))))

(defn remove-and-right
  [loc]
  (let [[node {:keys [l r ppath pnodes] :as path}] loc]
    (if (nil? path)
      (throw (new #?(:clj Exception :cljs js/Error) "Remove at top"))
      (if (pos? (count r))
        (with-meta [(first r) (assoc path :r (rest r) :changed? true)] (meta loc))
        (throw (new #?(:clj Exception :cljs js/Error) "No right sibling"))))))

(defn move-left
  [loc]
  (when (zip/left loc))
  (let [node (zip/node loc)]
    (-> loc
      (remove-and-left)
      (zip/insert-left node)
      (zip/left))))

(defn move-right
  [loc]
  (when (zip/right loc)
    (let [node (zip/node loc)]
      (-> loc
        (remove-and-right)
        (zip/insert-right node)
        (zip/right)))))
