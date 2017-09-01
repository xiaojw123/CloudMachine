package com.cloudmachine.bean;

import java.io.Serializable;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/2/16 下午2:23
 * 修改人：shixionglu
 * 修改时间：2017/2/16 下午2:23
 * 修改备注：
 */

public class DeliveryMethodInfo implements Serializable {

    private String creator;
    private int isDelete;
    private String name;
    private String createTime;
    private String updateTime;
    private long id;

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
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

    @Override
    public String toString() {
        return "DeliveryMethodInfo{" +
                "creator='" + creator + '\'' +
                ", isDelete=" + isDelete +
                ", name='" + name + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", id=" + id +
                '}';
    }
}
