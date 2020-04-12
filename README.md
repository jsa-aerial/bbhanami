# bbhanami
Bare Bones example Hanami client/server app


## Default install is via git clone and lein install

`git clone https://github.com/jsa-aerial/bbhanami`
`cd bbhanami`
`lein install`

Of course you can do whatever else fits your work flow


## Example project and core using it...

```Clojure

(defproject tryhanami "0.1.0"
  :description "Try out bare bones Hanami..."
  :url "http://example.com/FIXME"
  :license {:name "MIT"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [aerial.bbhanami "0.1.0"]])

```

We need a simple core for it:

```Clojure
(ns tryhanami.core
  (:require [aerial.bbhanami.core :as bbh]
            [aerial.hanami.common :as hc :refer [RMV]]
            [aerial.hanami.templates :as ht]
            [aerial.hanami.core :as hmi]))


(defn startit
  [port]
  ;;;...
  (bbh/start port))


```

## A few very simple examples

```Clojure
  (bbh/start 3003)

  (->> (hc/xform
        ht/point-chart
        :UDATA "data/cars.json"
        :X "Horsepower" :Y "Miles_per_Gallon" :COLOR "Origin")
       hmi/sv!)

  ;; Same as above but 'template' == base vega-lite spec
  (->>
   (hc/xform
    {:usermeta :USERDATA
     :data {:url "data/cars.json"},
     :mark "point",
     :encoding {:x {:field "Horsepower", :type "quantitative"},
                :y {:field "Miles_per_Gallon", :type "quantitative"},
                :color {:field "Origin", :type "nominal"}}})
   hmi/sv!)


  ;; Add some markdown and hiccup
  (->> (hc/xform
        ht/point-chart
        :TOP '[[gap :size "50px"]
               [md "### This is the 'standard' IDL scatter plot"]]
        :BOTTOM '[[gap :size "50px"]
                  [:p "the bottom element of this picture frame"]]
        :UDATA "data/cars.json"
        :X "Horsepower" :Y "Miles_per_Gallon" :COLOR "Origin")
       hmi/sv!)



  ;; Paint them both side by side
  (hmi/sv!
   [(hc/xform ht/point-chart
              :UDATA "data/cars.json"
              :X "Horsepower" :Y "Miles_per_Gallon" :COLOR "Origin")
    (hc/xform
     {:usermeta :USERDATA
      :data {:url "data/cars.json"},
      :mark "point",
      :encoding {:x {:field "Horsepower", :type "quantitative"},
                 :y {:field "Miles_per_Gallon", :type "quantitative"},
                 :color {:field "Origin", :type "nominal"}}})])



  ;; Paint them both, but in different tabs
  (hmi/sv!
   [(hc/xform ht/point-chart
              :UDATA "data/cars.json"
              :X "Horsepower" :Y "Miles_per_Gallon" :COLOR "Origin")
    (hc/xform
     {:usermeta :USERDATA
      :data {:url "data/cars.json"},
      :mark "point",
      :encoding {:x {:field "Horsepower", :type "quantitative"},
                 :y {:field "Miles_per_Gallon", :type "quantitative"},
                 :color {:field "Origin", :type "nominal"}}}
     ;; NOTE: the _above_ is the 'template' to transform and this tab id
     ;; substitution key will be used to gen a new tab
     :TID :vgl)])



  ;; Contour map example - NOTE uses :vega MODE
  ;; This is painted in default tab Expl
  (->>
   (hc/xform
    ht/contour-plot
    :MODE :vega
    :HEIGHT 400, :WIDTH 500
    :X "Horsepower", :XTITLE "Engine Horsepower"
    :Y "Miles_per_Gallon" :YTITLE "Miles/Gallon"
    :UDATA "data/cars.json"
    :XFORM-EXPR #(let [d1 (% :X)
                       d2 (% :Y)]
                   (format "datum['%s'] != null && datum['%s'] !=null" d1 d2)))
   hmi/sv!)

```