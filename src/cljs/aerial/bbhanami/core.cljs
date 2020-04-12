(ns  aerial.bbhanami.core
  (:require
   [cljs.core.async
    :as async
    :refer (<! >! put! chan)
    :refer-macros [go go-loop]]
   [clojure.string :as cljstr]

   [aerial.hanami.core :as hmi]
   [aerial.hanami.common :as hc]
   [re-com.core :refer [gap title h-box]]

   ))




;;; Startup ============================================================== ;;;

;;; Get rid of the multi-session based default header
(defn bbh-header-fn []
  [h-box :gap "10px" :max-height "30px"
   :children [[gap :size "5px"]
              [:img {:src (hmi/get-adb [:main :logo])}]
              [title
               :level :level3
               :label [:span.bold (hmi/get-adb [:main :title])]]
              [gap :size "5px"]
              [title
               :level :level3
               :label [:span.bold (hmi/get-adb [:main :uid :name])]]]])

(when-let [elem (js/document.querySelector "#app")]
  (hc/update-defaults
   :USERDATA {:tab {:id :TID, :label :TLBL, :opts :TOPTS}
              :frame {:top :TOP, :bottom :BOTTOM,
                      :left :LEFT, :right :RIGHT
                      :fid :FID :at :AT :pos :POS}
              :opts :OPTS
              :vid :VID,
              :msgop :MSGOP,
              :session-name :SESSION-NAME}
   :AT :end :POS :after
   :MSGOP :tabs, :SESSION-NAME "Exploring"
   :TID #(hmi/get-cur-tab :id)
   :TLBL #(-> (let [tid (% :TID)] (if (fn? tid) (tid) tid))
              name cljstr/capitalize)
   :OPTS (hc/default-opts :vgl), :TOPTS (hc/default-opts :tab))
  (hmi/start :elem elem
             :port js/location.port
             :host js/location.hostname
             :header-fn bbh-header-fn))




(comment


  )
