package com.cloudmachine.bean;

import java.util.List;

/**
 * Created by xiaojw on 2017/6/7.
 */

public class SiteBean {

    private List<RepairStationListBean> repairStationList;
    private List<RepairStationListBean> serviceSiteList;

    public List<RepairStationListBean> getRepairStationList() {
        return repairStationList;
    }

    public void setRepairStationList(List<RepairStationListBean> repairStationList) {
        this.repairStationList = repairStationList;
    }

    public List<RepairStationListBean> getServiceSiteList() {
        return serviceSiteList;
    }

    public void setServiceSiteList(List<RepairStationListBean> serviceSiteList) {
        this.serviceSiteList = serviceSiteList;
    }

    public static class RepairStationListBean {
        /**
         * position : 浙江省杭州市余杭区五常街道浙江海外高层次人才创新园18幢浙江海外高层次人才创新园
         * lat : 30.280662
         * lng : 120.016404
         */

        private String position;
        private double lat;
        private double lng;

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

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
    }
}
