package com.cloudmachine.bean;

/**
 * Created by xiaojw on 2018/9/18.
 */

public class ElectronicFenceBean {


    /**
     * deviceId : 0
     * deviceName : 体验机
     * typeId : 1
     * typePicUrl : http://medias.cloudm.com/static/app/map/wa_jue_ji
     * category : 挖掘机
     * polygonFence : null
     * circleFence : {"lat":30.294088,"lng":120.101078,"radiu":150}
     */

    private int deviceId;
    private String deviceName;
    private int typeId;
    private String typePicUrl;
    private String category;
    private Object polygonFence;
    private CircleFenceBean circleFence;

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getTypePicUrl() {
        return typePicUrl;
    }

    public void setTypePicUrl(String typePicUrl) {
        this.typePicUrl = typePicUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Object getPolygonFence() {
        return polygonFence;
    }

    public void setPolygonFence(Object polygonFence) {
        this.polygonFence = polygonFence;
    }

    public CircleFenceBean getCircleFence() {
        return circleFence;
    }

    public void setCircleFence(CircleFenceBean circleFence) {
        this.circleFence = circleFence;
    }

    public static class CircleFenceBean {
        /**
         * lat : 30.294088
         * lng : 120.101078
         * radiu : 150
         */

        private double lat;
        private double lng;
        private String radiu;

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }

        public String getRadiu() {
            return radiu;
        }

        public void setRadiu(String radiu) {
            this.radiu = radiu;
        }
    }
}
