package com.cloudmachine.bean;

/**
 * Created by xiaojw on 2018/6/13.
 */

public class SalaryPayInfo {


    /**
     * sign : null
     * signAgain : {"appid":"wxfb6afbcc23f867df","noncestr":"vjtBpiSnmV5UAppjcu4GDbGeZl7vtLIm","packageValue":"Sign=WXPay","partnerid":"1433562402","prepayid":"wx31134933401635d6a914f9d21704085629","sign":"DCE9C15291FD9FF0A97EFAD7C715FB0787922AF4F15D71058C645A31E1FDA125","timestamp":"1540964973"}
     * outTradeNo : LARK_20181031134933217005
     * url : null
     */

    private String sign;
    private SignAgainBean signAgain;
    private String outTradeNo;
    private String url;


    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public SignAgainBean getSignAgain() {
        return signAgain;
    }

    public void setSignAgain(SignAgainBean signAgain) {
        this.signAgain = signAgain;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static class SignAgainBean {
        /**
         * appid : wxfb6afbcc23f867df
         * noncestr : vjtBpiSnmV5UAppjcu4GDbGeZl7vtLIm
         * packageValue : Sign=WXPay
         * partnerid : 1433562402
         * prepayid : wx31134933401635d6a914f9d21704085629
         * sign : DCE9C15291FD9FF0A97EFAD7C715FB0787922AF4F15D71058C645A31E1FDA125
         * timestamp : 1540964973
         */

        private String appid;
        private String noncestr;
        private String packageValue;
        private String partnerid;
        private String prepayid;
        private String sign;
        private String timestamp;

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
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

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }
    }
}
