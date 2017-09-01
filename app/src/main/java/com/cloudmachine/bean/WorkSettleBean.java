package com.cloudmachine.bean;

import com.cloudmachine.utils.CommonUtils;

import java.io.Serializable;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/2/23 下午2:48
 * 修改人：shixionglu
 * 修改时间：2017/2/23 下午2:48
 * 修改备注：
 */

public class WorkSettleBean implements Serializable {

    private String nreceiveamount;//应收金额
    private String pk_CSS_WORKSETTLE;//结算帐单主键ID
    private String pk_CSS_WORK;//工单主键ID
    private String vworknum;//工单号
    private String nrepairunitprice;//维修工时单价
    private String nrepairworkhours;//NREPAIRWORKHOURS
    private String nrepairworkhourcost;//NREPAIRWORKHOURCOST
    private String ndiscountworkhourcost;//打折工时费
    private String npaidinamount;//实收金额
    private String nloanamount;//欠款金额
    private String ntotalamount;//总金额
    private String nisspecial;//是否使用券
    private String nmaxamount;//使用额度
    private String npartstotalamount;//配件总额合计
    private String ndiscounttotalamount;//配件折让合计
    private String dr;//数据类型
    private String creationtime;//创建时间
    private String ts;//最后更新时间
    private String dsicountamount;

    public String getDsicountamount() {
        return CommonUtils.formartPrice(dsicountamount);
    }

    public void setDsicountamount(String dsicountamount) {
        this.dsicountamount = dsicountamount;
    }

    public String getNreceiveamount() {
        return CommonUtils.formartPrice(nreceiveamount);
    }

    public void setNreceiveamount(String nreceiveamount) {
        this.nreceiveamount = nreceiveamount;
    }

    public String getPk_CSS_WORKSETTLE() {
        return pk_CSS_WORKSETTLE;
    }

    public void setPk_CSS_WORKSETTLE(String pk_CSS_WORKSETTLE) {
        this.pk_CSS_WORKSETTLE = pk_CSS_WORKSETTLE;
    }

    public String getPk_CSS_WORK() {
        return pk_CSS_WORK;
    }

    public void setPk_CSS_WORK(String pk_CSS_WORK) {
        this.pk_CSS_WORK = pk_CSS_WORK;
    }

    public String getVworknum() {
        return vworknum;
    }

    public void setVworknum(String vworknum) {
        this.vworknum = vworknum;
    }

    public String getNrepairunitprice() {
        return CommonUtils.formartPrice(nrepairunitprice);
    }

    public void setNrepairunitprice(String nrepairunitprice) {
        this.nrepairunitprice = nrepairunitprice;
    }

    public String getNrepairworkhours() {
        return nrepairworkhours;
    }

    public void setNrepairworkhours(String nrepairworkhours) {
        this.nrepairworkhours = nrepairworkhours;
    }

    public String getNrepairworkhourcost() {
        return CommonUtils.formartPrice(nrepairworkhourcost);
    }

    public void setNrepairworkhourcost(String nrepairworkhourcost) {
        this.nrepairworkhourcost = nrepairworkhourcost;
    }

    public String getNdiscountworkhourcost() {
        return CommonUtils.formartPrice(ndiscountworkhourcost);
    }

    public void setNdiscountworkhourcost(String ndiscountworkhourcost) {
        this.ndiscountworkhourcost = ndiscountworkhourcost;
    }

    public String getNpaidinamount() {
        return CommonUtils.formartPrice(npaidinamount);
    }

    public void setNpaidinamount(String npaidinamount) {
        this.npaidinamount = npaidinamount;
    }

    public String getNloanamount() {
        return CommonUtils.formartPrice(nloanamount);
    }

    public void setNloanamount(String nloanamount) {
        this.nloanamount = nloanamount;
    }

    public String getNtotalamount() {
        return CommonUtils.formartPrice(ntotalamount);
    }

    public void setNtotalamount(String ntotalamount) {
        this.ntotalamount = ntotalamount;
    }

    public String getNisspecial() {
        return nisspecial;
    }

    public void setNisspecial(String nisspecial) {
        this.nisspecial = nisspecial;
    }

    public String getNmaxamount() {
        return CommonUtils.formartPrice(nmaxamount);
    }

    public void setNmaxamount(String nmaxamount) {
        this.nmaxamount = nmaxamount;
    }

    public String getNpartstotalamount() {
        return CommonUtils.formartPrice(npartstotalamount);
    }

    public void setNpartstotalamount(String npartstotalamount) {
        this.npartstotalamount = npartstotalamount;
    }

    public String getNdiscounttotalamount() {
        return CommonUtils.formartPrice(ndiscounttotalamount);
    }

    public void setNdiscounttotalamount(String ndiscounttotalamount) {
        this.ndiscounttotalamount = ndiscounttotalamount;
    }

    public String getDr() {
        return dr;
    }

    public void setDr(String dr) {
        this.dr = dr;
    }

    public String getCreationtime() {
        return creationtime;
    }

    public void setCreationtime(String creationtime) {
        this.creationtime = creationtime;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    @Override
    public String toString() {
        return "WorkSettleBean{" +
                "nreceiveamount='" + nreceiveamount + '\'' +
                ", pk_CSS_WORKSETTLE='" + pk_CSS_WORKSETTLE + '\'' +
                ", pk_CSS_WORK='" + pk_CSS_WORK + '\'' +
                ", vworknum='" + vworknum + '\'' +
                ", nrepairunitprice='" + nrepairunitprice + '\'' +
                ", nrepairworkhours='" + nrepairworkhours + '\'' +
                ", nrepairworkhourcost='" + nrepairworkhourcost + '\'' +
                ", ndiscountworkhourcost='" + ndiscountworkhourcost + '\'' +
                ", npaidinamount='" + npaidinamount + '\'' +
                ", nloanamount='" + nloanamount + '\'' +
                ", ntotalamount='" + ntotalamount + '\'' +
                ", nisspecial='" + nisspecial + '\'' +
                ", nmaxamount='" + nmaxamount + '\'' +
                ", npartstotalamount='" + npartstotalamount + '\'' +
                ", ndiscounttotalamount='" + ndiscounttotalamount + '\'' +
                ", dr='" + dr + '\'' +
                ", creationtime='" + creationtime + '\'' +
                ", ts='" + ts + '\'' +
                '}';
    }
}
