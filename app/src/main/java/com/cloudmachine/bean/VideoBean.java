package com.cloudmachine.bean;

import java.util.List;

/**
 * Created by xiaojw on 2018/3/9.
 */

public class VideoBean {


    /**
     * liveUrl : rtmp://pili-publish.cloudm.com/dtuvideo/01000000000201710010000900
     * videoList : [{"id":7810,"name":"2018-03-09&15-38-13","time":"1:00","liveUrl":"rtmp://pili-publish.cloudm.com/dtuvideo/01000000000201710010000900"},{"id":7809,"name":"2018-03-09&15-37-10","time":"1:00","liveUrl":"rtmp://pili-publish.cloudm.com/dtuvideo/01000000000201710010000900"},{"id":7595,"name":"2018-03-08&17-41-40","time":"1:00","liveUrl":"rtmp://pili-publish.cloudm.com/dtuvideo/01000000000201710010000900"},{"id":7594,"name":"2018-03-08&17-40-39","time":"1:00","liveUrl":"rtmp://pili-publish.cloudm.com/dtuvideo/01000000000201710010000900"},{"id":7593,"name":"2018-03-08&17-39-35","time":"1:00","liveUrl":"rtmp://pili-publish.cloudm.com/dtuvideo/01000000000201710010000900"},{"id":7302,"name":"2018-03-08&15-50-34","time":"1:00","liveUrl":"rtmp://pili-publish.cloudm.com/dtuvideo/01000000000201710010000900"},{"id":7287,"name":"2018-03-08&15-49-33","time":"1:00","liveUrl":"rtmp://pili-publish.cloudm.com/dtuvideo/01000000000201710010000900"},{"id":7273,"name":"2018-03-08&15-48-30","time":"1:00","liveUrl":"rtmp://pili-publish.cloudm.com/dtuvideo/01000000000201710010000900"},{"id":7509,"name":"2018-03-08&15-42-27","time":"1:00","liveUrl":"rtmp://pili-publish.cloudm.com/dtuvideo/01000000000201710010000900"},{"id":7508,"name":"2018-03-08&15-41-25","time":"1:00","liveUrl":"rtmp://pili-publish.cloudm.com/dtuvideo/01000000000201710010000900"}]
     */

    private String liveUrl;
    private List<VideoListBean> videoList;

    public String getLiveUrl() {
        return liveUrl;
    }

    public void setLiveUrl(String liveUrl) {
        this.liveUrl = liveUrl;
    }

    public List<VideoListBean> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<VideoListBean> videoList) {
        this.videoList = videoList;
    }

    public static class VideoListBean {
        /**
         * id : 7810
         * name : 2018-03-09&15-38-13
         * time : 1:00
         * liveUrl : rtmp://pili-publish.cloudm.com/dtuvideo/01000000000201710010000900
         */

        private int id;
        private String name;
        private String time;
        private String liveUrl;

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        private boolean selected;


        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getLiveUrl() {
            return liveUrl;
        }

        public void setLiveUrl(String liveUrl) {
            this.liveUrl = liveUrl;
        }
    }
}
