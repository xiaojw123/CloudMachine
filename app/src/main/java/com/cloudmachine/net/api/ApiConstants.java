package com.cloudmachine.net.api;


import com.cloudmachine.BuildConfig;

public class ApiConstants {



    /* 测试环境*/
    public static String REMOTE_HOST1 =BuildConfig.IS_ONLINE?"http://api.cloudm.com/cloudm3/": "http://api.test.cloudm.com/cloudm3/";
    public static String REMOTE_HOST2 =BuildConfig.IS_ONLINE?"http://api.cloudm.com/cloudm3/yjx/": "http://api.test.cloudm.com/cloudm3/yjx/";
    public static String REMOTE_HOST3 =BuildConfig.IS_ONLINE?"http://ask.cloudm.com/": "http://ask.test.cloudm.com/";
    public static String REMOTE_HOST4 =BuildConfig.IS_ONLINE?"http://h5.cloudm.com/n/": "http://h5.test.cloudm.com/n/";
    public static String REMOTE_HOST5=BuildConfig.IS_ONLINE?"http://camera.cloudm.com:18089/":"http://183.129.196.42:18089/";//云黑子-高配-拍照

    /*测试环境-109/218*/
    private static final String T_HOST1 =BuildConfig.IS_REMOTE? REMOTE_HOST1:"http://192.168.1.109:18088/cloudm3/";
    private static final String T_HOST2 =BuildConfig.IS_REMOTE?REMOTE_HOST2: "http://192.168.1.109:18088/cloudm3/yjx/";
    private static final String T_HOST3 =BuildConfig.IS_REMOTE?REMOTE_HOST3: "http://ask.test.cloudm.com/";
    private static final String T_HOST4 =BuildConfig.IS_REMOTE?REMOTE_HOST4: "http://192.168.1.109:7718/";
    private static final String T_HOST5=BuildConfig.IS_REMOTE?REMOTE_HOST5:"http://192.168.1.176:18089/";//云黑子-高配-拍照
    /*线上环境*/
    public static final String CLOUDM_HOST =BuildConfig.DEBUG?T_HOST1: "http://api.cloudm.com/cloudm3/";
    public static final String CLOUDM_YJX_HOST =BuildConfig.DEBUG?T_HOST2 :"http://api.cloudm.com/cloudm3/yjx/";
    public static final String CLOUDM_ASK_HOST = BuildConfig.DEBUG?T_HOST3:"http://ask.cloudm.com/";
    private static final String H5_HOST =BuildConfig.DEBUG?T_HOST4: "http://h5.cloudm.com/n/";
    private static final String BOX_HIGH_COFIG_HOST =BuildConfig.DEBUG?T_HOST5: "http://camera.cloudm.com:18089/";




//    /*21-wnb*/
//    public static String CLOUDM_HOST = "http://192.168.1.109:18088/cloudm3/";
//    public static String CLOUDM_YJX_HOST = "http://192.168.10.46:8090/cloudm3/yjx/";
//    public static String CLOUDM_ASK_HOST = "http://ask.test.cloudm.com/";
//    public static String H5_HOST = "http://192.168.1.109:7718/";
//    public static String BOX_HIGH_COFIG_HOST="http://192.168.1.176:18089/";//云黑子-高配-拍照


    /*43*/
//public static String CLOUDM_HOST = "http://192.168.1.43:8090/cloudm3/";
//    public static String CLOUDM_YJX_HOST = "http://192.168.1.43:8090/cloudm3/yjx/";
//    public static String CLOUDM_ASK_HOST = "http://ask.test.cloudm.com/";
//    public static String H5_HOST = "http://h5.test.cloudm.com/";

    /*110*/
//    public static String CLOUDM_HOST = "http://192.168.1.110:8090/cloudm3/";
//    public static String CLOUDM_YJX_HOST = "http://192.168.1.110:8090/cloudm3/yjx/";
//    public static String CLOUDM_ASK_HOST = "http://ask.test.cloudm.com/";
//    public static String H5_HOST = "http://h5.test.cloudm.com/";
//    public static String BOX_HIGH_COFIG_HOST = "http://192.168.1.176:18089/";//云黑子-高配-拍照


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
    public static String AppFeedback = "https://h5.cloudm.com/n/feedback";
    public static String AppMachineKnowledge;
//    public static String AppWorkReport="http://192.168.1.109:7718/work_report?deviceId=795&week=2";


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
