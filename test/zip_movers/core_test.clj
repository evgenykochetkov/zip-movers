(ns zip-movers.core-test
  (:require [clojure.test :refer :all]
            [clojure.zip :as zip]
            [zip-movers.core :refer :all]))

(def data '[[a * b] + [c * d]])
(def dz (zip/vector-zip data))
(def plus-loc (-> dz zip/down zip/right))

(deftest a-test
  (testing "remove-and-up"
    (def new-loc (remove-and-up plus-loc))
    (is (= '[[a * b] [c * d]] (zip/node new-loc)))
    (is (= '[[a * b] [c * d]] (zip/root new-loc))))

  (testing "remove-and-left"
    (def new-loc (remove-and-left plus-loc))
    (is (= '[a * b] (zip/node new-loc)))
    (is (= '[[a * b] [c * d]] (zip/root new-loc))))

  (testing "remove-and-right"
    (def new-loc (remove-and-right plus-loc))
    (is (= '[c * d] (zip/node new-loc)))
    (is (= '[[a * b] [c * d]] (zip/root new-loc))))

  (testing "move-left"
    (def new-loc (move-left plus-loc))
    (is (= '+ (zip/node new-loc)))
    (is (= '[+ [a * b] [c * d]] (zip/root new-loc))))

  (testing "move-right"
    (def new-loc (move-right plus-loc))
    (is (= '+ (zip/node new-loc)))
    (is (= '[[a * b] [c * d] +] (zip/root new-loc)))))
