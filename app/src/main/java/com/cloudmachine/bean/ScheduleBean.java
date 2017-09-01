package com.cloudmachine.bean;

import java.io.Serializable;

public class ScheduleBean implements Serializable {
    private String createTime;
    private String desc;
    private String key;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "ScheduleBean{" +
                "createTime='" + createTime + '\'' +
                ", desc='" + desc + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}