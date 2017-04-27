package com.chen.develop.zhihudaily.Bean;

import java.util.List;

/**
 * Created by chen.zhiwei on 2016/6/24 0024.
 */
public class SplashBean {


    private List<CreativesBean> creatives;

    public List<CreativesBean> getCreatives() {
        return creatives;
    }

    public void setCreatives(List<CreativesBean> creatives) {
        this.creatives = creatives;
    }

    public static class CreativesBean {
        /**
         * url : https://pic4.zhimg.com/v2-e78323f847d52e2e3c7db6107b09f533.jpg
         * start_time : 1493274574
         * impression_tracks : ["https://sugar.zhihu.com/track?vs=1&ai=3957&ut=&cg=2&ts=1493274574.61&si=d419075e54af4f8e938d94f33120e40f&lu=0&hn=ad-engine.ad-engine.5d51e675&at=impression&pf=PC&az=11&sg=5e1ec17877916d0c3d15b052937c490a"]
         * type : 0
         * id : 3957
         */

        private String url;
        private int start_time;
        private int type;
        private String id;
        private List<String> impression_tracks;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getStart_time() {
            return start_time;
        }

        public void setStart_time(int start_time) {
            this.start_time = start_time;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<String> getImpression_tracks() {
            return impression_tracks;
        }

        public void setImpression_tracks(List<String> impression_tracks) {
            this.impression_tracks = impression_tracks;
        }
    }
}
