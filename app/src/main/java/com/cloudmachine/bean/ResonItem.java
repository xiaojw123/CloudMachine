package com.cloudmachine.bean;

/**
 * Created by xiaojw on 2017/12/4.
 */

public class ResonItem {


    /**
     * id : 1
     * description : 云盒子灯不亮了
     * gmtCreate : 1512375554000
     * gmtModified : 1512375544000
     * yn : 0
     */

    private int id;
    private String description;
    private long gmtCreate;
    private long gmtModified;
    private int yn;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(long gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public long getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(long gmtModified) {
        this.gmtModified = gmtModified;
    }

    public int getYn() {
        return yn;
    }

    public void setYn(int yn) {
        this.yn = yn;
    }
}
