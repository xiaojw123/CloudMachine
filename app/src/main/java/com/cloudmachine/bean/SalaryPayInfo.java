package com.cloudmachine.bean;

/**
 * Created by xiaojw on 2018/6/13.
 */

public class SalaryPayInfo {


    /**
     * signAgain : null
     * out_trade_no : MON_20180614123235114054
     * timestamp : 1528950755
     * partnerid : 1433562402
     * noncestr : 061412323574678
     * packageValue : Sign=WXPay
     * sign : 9515E9AEB6BEA45BFCBE6388E2189A42
     * appid : wxfb6afbcc23f867df
     * prepayid : wx14123235327240befda6440d3816862880
     */

    private String signAgain;
    private String out_trade_no;
    private String timestamp;
    private String partnerid;
    private String noncestr;
    private String packageValue;
    private String sign;
    private String appid;
    private String prepayid;

    public String getParaTemp() {
        return paraTemp;
    }

    public void setParaTemp(String paraTemp) {
        this.paraTemp = paraTemp;
    }

    private String paraTemp;

    public String getSignAgain() {
        return signAgain;
    }

    public void setSignAgain(String signAgain) {
        this.signAgain = signAgain;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getPackageValue() {
        return packageValue;
    }

    public void setPackageValue(String packageValue) {
        this.packageValue = packageValue;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    @Override
    public String toString() {
        return "SalaryPayInfo[sign="+sign+" ,appid="+appid+"]";
    }
}
