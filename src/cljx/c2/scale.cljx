^:clj (ns c2.scale
        (:use [c2.util :only [c2-obj]]
              [c2.maths :only [log expt] :rename {log logg}]))
^:cljs (ns c2.scale
         (:use-macros [c2.util :only [c2-obj]])
         (:use [c2.maths :only [log expt] :rename {log logg}]))

(defprotocol IInvertable
  (invert [scale] "Inverted scale"))



;;Linear scale
;;
;;Kwargs:
;;> *:domain* domain of scale, default [0 1]
;;
;;> *:range* range of scale, default [0 1]
(c2-obj linear {:domain [0 1]
                :range  [0 1]}

        clojure.lang.IFn
        (invoke [_ x] (let [domain-length (- (last domain) (first domain))
                            range-length (- (last range) (first range))]
                        (+ (first range)
                           (* range-length
                              (/ (- x (first domain))
                                 domain-length)))))
        IInvertable
        (invert [this]
                (assoc this
                  :domain (:range this)
                  :range (:domain this))))

(declare log)

;;Power scale
;;
;;Kwargs:
;;> *:domain* domain of scale, default [0 1]
;;
;;> *:range* range of scale, default [0 1]
(c2-obj power {:domain [0 1]
               :range  [0 1]}
        
        clojure.lang.IFn
        (invoke [_ x]
                ((comp (linear :domain (map expt domain)
                               :range range)
                       expt) x)))

;;Logarithmic scale
;;
;;Kwargs:
;;> *:domain* domain of scale, default [1 10]
;;
;;> *:range* range of scale, default [0 1]
(c2-obj log {:domain [1 10]
             :range  [0 1]}
        
        clojure.lang.IFn
        (invoke [_ x]
                ((comp (linear :domain (map logg domain)
                               :range range)
                       logg) x)))

