package com.cloudmachine.bean;

import java.io.Serializable;

/**
 * 项目名称：CloudMachine
 * 类描述： 圆形电子围栏
 * 创建人：shixionglu
 * 创建时间：2016/11/30 上午10:32
 * 修改人：shixionglu
 * 修改时间：2016/11/30 上午10:32
 * 修改备注：
 */

public class McDeviceCircleFence implements Serializable {

    private String lat;
    private String lng;
    private String radiu;

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getRadiu() {
        return radiu;
    }

    public void setRadiu(String radiu) {
        this.radiu = radiu;
    }
}
