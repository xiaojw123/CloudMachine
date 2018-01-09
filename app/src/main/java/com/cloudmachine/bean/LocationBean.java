package com.cloudmachine.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by xiaojw on 2017/12/27.
 */

public class LocationBean  extends DataSupport {
    private String lat;
    private String lng;
    private String address;
    private String province;
    private String city;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    private String district;

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


    @Override
    public String toString() {
        return "LocationBean[lat="+lat+", lng="+lng+", address="+address+"province="+province+", city="+city+", district="+district+"]";
    }
}
