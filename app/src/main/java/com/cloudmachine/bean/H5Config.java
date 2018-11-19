package com.cloudmachine.bean;

/**
 * Created by xiaojw on 2018/11/5.
 */

public class H5Config {


    /**
     * pages : {"AppQR":"http://test.cloudm.com/static/qr.html","AppBoxDetail":"http://h5.test.cloudm.com/2n/order/yunbox","AppCouponHelper":"http://h5.test.cloudm.com/2n/coupon_description","AppWalletHelper":"http://h5.test.cloudm.com/2n/wallet_description","AppUseHelper":"http://h5.test.cloudm.com/2s/help/list.html","AppHunterUseHelper":"http://h5.test.cloudm.com/2s/help/hunter.html","AppWorkTimeStatistics":"http://h5.test.cloudm.com/2n/time_statistics","AppOrderList":"http://h5.test.cloudm.com/2n/order/olist","AppFeedback":"http://h5.test.cloudm.com/2n/feedback","AppAlliancePayByOthers":"http://h5.test.cloudm.com/2n/alliance/payByOthers","AppAlliancePayByKJ":"http://h5.test.cloudm.com/2n/alliance/payByKJ","AppFittingMall":"http://h5.test.cloudm.com/2n/fittingMall/fm_index","AppBankList":"http://h5.test.cloudm.com/2n/help/bankList","APPForgetPassword":"http://h5.test.cloudm.com/2n/xiaopiao/forgetPassword.html","APPFwtk":"http://h5.test.cloudm.com/2n/xiaopiao/fwtk.html","APPRzgj":"https://loan.jyc99.com/login?source=F7AFFB7F28A81AD8","APPBmxy":"http://h5.test.cloudm.com/2n/xiaopiao/bmxy.html","APPSjyysxy":"http://h5.test.cloudm.com/2n/xiaopiao/sjyysxy.html","QnUptoken":"http://h5.test.cloudm.com/2n/api_qn/uptoken"}
     * apis : {"QnUptoken":"http://h5.test.cloudm.com/2n/api_qn/uptoken"}
     */

    private PagesBean pages;
    private ApisBean apis;

    public PagesBean getPages() {
        return pages;
    }

    public void setPages(PagesBean pages) {
        this.pages = pages;
    }

    public ApisBean getApis() {
        return apis;
    }

    public void setApis(ApisBean apis) {
        this.apis = apis;
    }

    public static class PagesBean {
        /**
         * AppQR : http://test.cloudm.com/static/qr.html
         * AppBoxDetail : http://h5.test.cloudm.com/2n/order/yunbox
         * AppCouponHelper : http://h5.test.cloudm.com/2n/coupon_description
         * AppWalletHelper : http://h5.test.cloudm.com/2n/wallet_description
         * AppUseHelper : http://h5.test.cloudm.com/2s/help/list.html
         * AppHunterUseHelper : http://h5.test.cloudm.com/2s/help/hunter.html
         * AppWorkTimeStatistics : http://h5.test.cloudm.com/2n/time_statistics
         * AppOrderList : http://h5.test.cloudm.com/2n/order/olist
         * AppFeedback : http://h5.test.cloudm.com/2n/feedback
         * AppAlliancePayByOthers : http://h5.test.cloudm.com/2n/alliance/payByOthers
         * AppAlliancePayByKJ : http://h5.test.cloudm.com/2n/alliance/payByKJ
         * AppFittingMall : http://h5.test.cloudm.com/2n/fittingMall/fm_index
         * AppBankList : http://h5.test.cloudm.com/2n/help/bankList
         * APPForgetPassword : http://h5.test.cloudm.com/2n/xiaopiao/forgetPassword.html
         * APPFwtk : http://h5.test.cloudm.com/2n/xiaopiao/fwtk.html
         * APPRzgj : https://loan.jyc99.com/login?source=F7AFFB7F28A81AD8
         * APPBmxy : http://h5.test.cloudm.com/2n/xiaopiao/bmxy.html
         * APPSjyysxy : http://h5.test.cloudm.com/2n/xiaopiao/sjyysxy.html
         * QnUptoken : http://h5.test.cloudm.com/2n/api_qn/uptoken
         */

        private String AppQR;
        private String AppBoxDetail;
        private String AppCouponHelper;
        private String AppWalletHelper;
        private String AppUseHelper;
        private String AppHunterUseHelper;
        private String AppWorkTimeStatistics;
        private String AppOrderList;
        private String AppFeedback;
        private String AppAlliancePayByOthers;
        private String AppAlliancePayByKJ;
        private String AppFittingMall;
        private String AppBankList;
        private String AppBankCmVerify;
        private String APPForgetPassword;
        private String APPFwtk;
        private String APPRzgj;
        private String APPBmxy;
        private String AppExChange;

        public String getAppExChange() {
            return AppExChange;
        }

        public void setAppExChange(String appExChange) {
            AppExChange = appExChange;
        }

        public String getAppBankCmVerify() {
            return AppBankCmVerify;
        }

        public void setAppBankCmVerify(String appBankCmVerify) {
            AppBankCmVerify = appBankCmVerify;
        }

        private String APPSjyysxy;
        private String QnUptoken;

        public String getAppQR() {
            return AppQR;
        }

        public void setAppQR(String AppQR) {
            this.AppQR = AppQR;
        }

        public String getAppBoxDetail() {
            return AppBoxDetail;
        }

        public void setAppBoxDetail(String AppBoxDetail) {
            this.AppBoxDetail = AppBoxDetail;
        }

        public String getAppCouponHelper() {
            return AppCouponHelper;
        }

        public void setAppCouponHelper(String AppCouponHelper) {
            this.AppCouponHelper = AppCouponHelper;
        }

        public String getAppWalletHelper() {
            return AppWalletHelper;
        }

        public void setAppWalletHelper(String AppWalletHelper) {
            this.AppWalletHelper = AppWalletHelper;
        }

        public String getAppUseHelper() {
            return AppUseHelper;
        }

        public void setAppUseHelper(String AppUseHelper) {
            this.AppUseHelper = AppUseHelper;
        }

        public String getAppHunterUseHelper() {
            return AppHunterUseHelper;
        }

        public void setAppHunterUseHelper(String AppHunterUseHelper) {
            this.AppHunterUseHelper = AppHunterUseHelper;
        }

        public String getAppWorkTimeStatistics() {
            return AppWorkTimeStatistics;
        }

        public void setAppWorkTimeStatistics(String AppWorkTimeStatistics) {
            this.AppWorkTimeStatistics = AppWorkTimeStatistics;
        }

        public String getAppOrderList() {
            return AppOrderList;
        }

        public void setAppOrderList(String AppOrderList) {
            this.AppOrderList = AppOrderList;
        }

        public String getAppFeedback() {
            return AppFeedback;
        }

        public void setAppFeedback(String AppFeedback) {
            this.AppFeedback = AppFeedback;
        }

        public String getAppAlliancePayByOthers() {
            return AppAlliancePayByOthers;
        }

        public void setAppAlliancePayByOthers(String AppAlliancePayByOthers) {
            this.AppAlliancePayByOthers = AppAlliancePayByOthers;
        }

        public String getAppAlliancePayByKJ() {
            return AppAlliancePayByKJ;
        }

        public void setAppAlliancePayByKJ(String AppAlliancePayByKJ) {
            this.AppAlliancePayByKJ = AppAlliancePayByKJ;
        }

        public String getAppFittingMall() {
            return AppFittingMall;
        }

        public void setAppFittingMall(String AppFittingMall) {
            this.AppFittingMall = AppFittingMall;
        }

        public String getAppBankList() {
            return AppBankList;
        }

        public void setAppBankList(String AppBankList) {
            this.AppBankList = AppBankList;
        }

        public String getAPPForgetPassword() {
            return APPForgetPassword;
        }

        public void setAPPForgetPassword(String APPForgetPassword) {
            this.APPForgetPassword = APPForgetPassword;
        }

        public String getAPPFwtk() {
            return APPFwtk;
        }

        public void setAPPFwtk(String APPFwtk) {
            this.APPFwtk = APPFwtk;
        }

        public String getAPPRzgj() {
            return APPRzgj;
        }

        public void setAPPRzgj(String APPRzgj) {
            this.APPRzgj = APPRzgj;
        }

        public String getAPPBmxy() {
            return APPBmxy;
        }

        public void setAPPBmxy(String APPBmxy) {
            this.APPBmxy = APPBmxy;
        }

        public String getAPPSjyysxy() {
            return APPSjyysxy;
        }

        public void setAPPSjyysxy(String APPSjyysxy) {
            this.APPSjyysxy = APPSjyysxy;
        }

        public String getQnUptoken() {
            return QnUptoken;
        }

        public void setQnUptoken(String QnUptoken) {
            this.QnUptoken = QnUptoken;
        }
    }

    public static class ApisBean {
        /**
         * QnUptoken : http://h5.test.cloudm.com/2n/api_qn/uptoken
         */

        private String QnUptoken;

        public String getQnUptoken() {
            return QnUptoken;
        }

        public void setQnUptoken(String QnUptoken) {
            this.QnUptoken = QnUptoken;
        }
    }
}
