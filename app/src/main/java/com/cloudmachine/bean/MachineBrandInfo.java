package com.cloudmachine.bean;

import java.io.Serializable;


public class MachineBrandInfo  implements Serializable{

    /**
     * pk_BRAND : BD000000000000000019
     * name : 卡特
     * createTime : null
     * updateTime : null
     * id : 1
     */

    private String pk_BRAND;
    private String name;
    private String createTime;
    private String updateTime;
    private long id;

    public String getPk_BRAND() {
        return pk_BRAND;
    }

    public void setPk_BRAND(String pk_BRAND) {
        this.pk_BRAND = pk_BRAND;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
	
	
	
}
