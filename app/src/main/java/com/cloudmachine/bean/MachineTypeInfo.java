package com.cloudmachine.bean;

import java.io.Serializable;

/**
 * Created by shixionglu on 2016/10/27.
 */

public class MachineTypeInfo implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 491261839428848894L;
	/**
     * name : 挖掘机
     * pk_PROD_DEF : PD000000000000000002
     * id : 1
     * createTime : null
     * updateTime : null
     */

    private String name;
    private String pk_PROD_DEF;
    private long id;
    private String createTime;
    private String updateTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPk_PROD_DEF() {
        return pk_PROD_DEF;
    }

    public void setPk_PROD_DEF(String pk_PROD_DEF) {
        this.pk_PROD_DEF = pk_PROD_DEF;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

	@Override
	public String toString() {
		return "MachineTypeInfo [name=" + name + ", pk_PROD_DEF=" + pk_PROD_DEF
				+ ", id=" + id + ", createTime=" + createTime + ", updateTime="
				+ updateTime + "]";
	}
    
    
}
