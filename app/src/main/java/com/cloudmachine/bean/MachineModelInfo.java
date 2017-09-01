package com.cloudmachine.bean;

import java.io.Serializable;


public class MachineModelInfo  implements Serializable{
	/**
     * standard : 350
     * pk_PROD_DEF : PD000000000000000002
     * pk_BRAND : BD000000000000000019
     * pk_VHCL_MATERIAL : MA000000000000010527
     * resistanceUp : 85
     * resistanceDown : 7
     * resistanceLen : 0.22
     * levelType : 1
     * creator : null
     * modelName : 349D
     * typeId : 1
     * brandId : 1
     * createTime : null
     * updateTime : null
     * id : 61
     */

    private int standard;
    private String pk_PROD_DEF;
    private String pk_BRAND;
    private String pk_VHCL_MATERIAL;
    private double resistanceUp;
    private double resistanceDown;
    private double resistanceLen;
    private int levelType;
    private long creator;
    private String modelName;
    private long typeId;
    private long brandId;
    private String createTime;
    private String updateTime;
    private long id;

    public int getStandard() {
        return standard;
    }

    public void setStandard(int standard) {
        this.standard = standard;
    }

    public String getPk_PROD_DEF() {
        return pk_PROD_DEF;
    }

    public void setPk_PROD_DEF(String pk_PROD_DEF) {
        this.pk_PROD_DEF = pk_PROD_DEF;
    }

    public String getPk_BRAND() {
        return pk_BRAND;
    }

    public void setPk_BRAND(String pk_BRAND) {
        this.pk_BRAND = pk_BRAND;
    }

    public String getPk_VHCL_MATERIAL() {
        return pk_VHCL_MATERIAL;
    }

    public void setPk_VHCL_MATERIAL(String pk_VHCL_MATERIAL) {
        this.pk_VHCL_MATERIAL = pk_VHCL_MATERIAL;
    }

    public double getResistanceUp() {
        return resistanceUp;
    }

    public void setResistanceUp(double resistanceUp) {
        this.resistanceUp = resistanceUp;
    }

    public double getResistanceDown() {
        return resistanceDown;
    }

    public void setResistanceDown(double resistanceDown) {
        this.resistanceDown = resistanceDown;
    }

    public double getResistanceLen() {
        return resistanceLen;
    }

    public void setResistanceLen(double resistanceLen) {
        this.resistanceLen = resistanceLen;
    }

    public int getLevelType() {
        return levelType;
    }

    public void setLevelType(int levelType) {
        this.levelType = levelType;
    }

    public long getCreator() {
        return creator;
    }

    public void setCreator(long creator) {
        this.creator = creator;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public long getTypeId() {
        return typeId;
    }

    public void setTypeId(long typeId) {
        this.typeId = typeId;
    }

    public long getBrandId() {
        return brandId;
    }

    public void setBrandId(long brandId) {
        this.brandId = brandId;
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
