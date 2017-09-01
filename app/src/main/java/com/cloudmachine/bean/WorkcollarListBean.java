package com.cloudmachine.bean;

import java.io.Serializable;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/2/23 下午2:36
 * 修改人：shixionglu
 * 修改时间：2017/2/23 下午2:36
 * 修改备注：
 */

public class WorkcollarListBean implements Serializable {

    private String unit_PRICE;//配件单价
    private String part_NAME;//配件名称
    private String use_QTY;//使用数量
    private String part_CODE;//配件编号
    private String nispack;//是否三包
    private String npackpartnum;//三包使用数量

    public String getUnit_PRICE() {
        return unit_PRICE;
    }

    public void setUnit_PRICE(String unit_PRICE) {
        this.unit_PRICE = unit_PRICE;
    }

    public String getPart_NAME() {
        return part_NAME;
    }

    public void setPart_NAME(String part_NAME) {
        this.part_NAME = part_NAME;
    }

    public String getUse_QTY() {
        return use_QTY;
    }

    public void setUse_QTY(String use_QTY) {
        this.use_QTY = use_QTY;
    }

    public String getPart_CODE() {
        return part_CODE;
    }

    public void setPart_CODE(String part_CODE) {
        this.part_CODE = part_CODE;
    }

    public String getNispack() {
        return nispack;
    }

    public void setNispack(String nispack) {
        this.nispack = nispack;
    }

    public String getNpackpartnum() {
        return npackpartnum;
    }

    public void setNpackpartnum(String npackpartnum) {
        this.npackpartnum = npackpartnum;
    }

    @Override
    public String toString() {
        return "WorkcollarListBean{" +
                "unit_PRICE='" + unit_PRICE + '\'' +
                ", part_NAME='" + part_NAME + '\'' +
                ", use_QTY='" + use_QTY + '\'' +
                ", part_CODE='" + part_CODE + '\'' +
                ", nispack='" + nispack + '\'' +
                ", npackpartnum='" + npackpartnum + '\'' +
                '}';
    }
}
