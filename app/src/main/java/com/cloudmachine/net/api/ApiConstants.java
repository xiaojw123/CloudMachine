package com.cloudmachine.net.api;


public class ApiConstants {

    /* 测试环境*/
//    public static String CLOUDM_HOST = "http://api.test.cloudm.com/cloudm3/";
//    public static String CLOUDM_YJX_HOST = "http://api.test.cloudm.com/cloudm3/yjx/";
//    public static String CLOUDM_ASK_HOST = "http://ask.test.cloudm.com/";
//    public static String H5_HOST = "http://h5.test.cloudm.com/n/";
//    public static String BOX_HIGH_COFIG_HOST="http://183.129.196.42:18089/";//云黑子-高配-拍照


    /*线上环境*/
        public static String CLOUDM_YJX_HOST = "http://api.cloudm.com/cloudm3/yjx/";
        public static String CLOUDM_HOST = "http://api.cloudm.com/cloudm3/";
        public static String CLOUDM_ASK_HOST = "http://ask.cloudm.com/";
        public static String H5_HOST = "http://h5.cloudm.com/n/";
        public static String BOX_HIGH_COFIG_HOST = "http://camera.cloudm.com:18089/";


    /*测试环境-109*/
//    public static String CLOUDM_HOST = "http://192.168.1.109:18088/cloudm3/";
//    public static String CLOUDM_YJX_HOST = "http://192.168.1.109:18088/cloudm3/yjx/";
//    public static String CLOUDM_ASK_HOST = "http://ask.test.cloudm.com/";
//    public static String H5_HOST = "http://192.168.1.109:7718/";
//    public static String BOX_HIGH_COFIG_HOST="http://192.168.1.176:18089/";//云黑子-高配-拍照


    /*21-wnb*/
//    public static String CLOUDM_HOST = "http://192.168.1.21:8090/cloudm3/";
//    public static String CLOUDM_YJX_HOST = "http://192.168.1.21:8090/cloudm3/yjx/";
//    public static String CLOUDM_ASK_HOST = "http://ask.test.cloudm.com/";
//    public static String H5_HOST = "http://192.168.1.109:7718/";
//    public static String BOX_HIGH_COFIG_HOST="http://192.168.1.176:18089/";//云黑子-高配-拍照


    /*43*/
//public static String CLOUDM_HOST = "http://192.168.1.43:8090/cloudm3/";
//    public static String CLOUDM_YJX_HOST = "http://192.168.1.43:8090/cloudm3/yjx/";
//    public static String CLOUDM_ASK_HOST = "http://ask.test.cloudm.com/";
//    public static String H5_HOST = "http://h5.test.cloudm.com/";


    /*H5配置URL*/
    public static String AppBoxDetail = "https://h5.cloudm.com/n/order/yunbox";
    public static String AppCouponHelper = "https://h5.cloudm.com/n/coupon_description";
    public static String AppCommunity = "https://h5.cloudm.com/n/ask_qlist";
    public static String AppASKQuestion = "https://h5.cloudm.com/n/ask_qsubmit";
    public static String AppMyQuestion = "https://h5.cloudm.com/n/ask_myq";
    public static String AppUseHelper = "https://h5.cloudm.com/n/help/home";
    public static String AppWorkTimeStatistics = "https://h5.cloudm.com/n/time_statistics";
    public static String AppOrderList = "https://h5.cloudm.com/n/order/olist";
    public static String AppWalletHelper = "http://h5.cloudm.com/n/wallet_description";
    public static String AppFeedback="https://h5.cloudm.com/n/feedback";
    public static String AppMachineKnowledge;
    public static String AppWorkReport;


    /**
     * 获取对应的host
     *
     * @param hostType host类型
     * @return host
     */
    public static String getHost(int hostType) {
        String host;
        switch (hostType) {
            case HostType.HOST_CLOUDM_YJX:
                host = CLOUDM_YJX_HOST;
                break;
            case HostType.HOST_CLOUDM:
                host = CLOUDM_HOST;
                break;
            case HostType.HOST_CLOUDM_ASK:
                host = CLOUDM_ASK_HOST;
                break;
            case HostType.HOST_H5:
                host = H5_HOST;
                break;
            case HostType.HOST_BH_COFIG:
                host = BOX_HIGH_COFIG_HOST;
                break;
            default:
                host = "";
                break;
        }
        return host;
    }
}
