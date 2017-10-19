package com.cloudmachine.net.api;



public class ApiConstants {

    /* 测试环境*/
//    public static String CLOUDM_HOST = "http://api.test.cloudm.com/cloudm3/yjx/";
//    public static String GUOSHUAI_HOST = "http://api.test.cloudm.com/cloudm3/";
//    public static String CAITINGTING_HOST = "http://api.test.cloudm.com/cloudm3/";
//    public static String XIEXIN_HOST = "http://ask.test.cloudm.com/";
//    public static String H5_HOST = "http://h5.test.cloudm.com/";


    //http://192.168.1.109:18088/

/*线上环境*/
    public static  String CLOUDM_HOST = "http://api.cloudm.com/cloudm3/yjx/";
    public static  String GUOSHUAI_HOST = "http://api.cloudm.com/cloudm3/";
    public static String CAITINGTING_HOST = "http://api.cloudm.com/cloudm3/";
    public static String XIEXIN_HOST = "http://ask.cloudm.com/";
    public static String H5_HOST = "http://h5.cloudm.com/";

    /*109*/
//    public static  String CLOUDM_HOST = "http://192.168.1.109:18088/cloudm3/yjx/";
//    public static  String GUOSHUAI_HOST = "http://192.168.1.109:18088/cloudm3/";
//    public static String CAITINGTING_HOST = "http://192.168.1.109:18088/cloudm3/";
//    public static String XIEXIN_HOST = "http://ask.cloudm.com/";
//    public static String H5_HOST = "http://h5.cloudm.com/";






    /*H5配置URL*/
    public static String AppBoxDetail = "https://h5.cloudm.com/n/order/yunbox";
    public static String AppCouponHelper = "https://h5.cloudm.com/n/coupon_description";
    public static String AppCommunity = "https://h5.cloudm.com/n/ask_qlist";
    public static String AppASKQuestion = "https://h5.cloudm.com/n/ask_qsubmit";
    public static String AppMyQuestion = "https://h5.cloudm.com/n/ask_myq";
    public static String AppUseHelper = "https://h5.cloudm.com/n/help/home";
    public static String AppWorkTimeStatistics = "https://h5.cloudm.com/n/time_statistics";
    public static String AppOrderList = "https://h5.cloudm.com/n/order/olist";

    /**
     * 获取对应的host
     *
     * @param hostType host类型
     * @return host
     */
    public static String getHost(int hostType) {
        String host;
        switch (hostType) {
            case HostType.CLOUDM_HOST:
                host = CLOUDM_HOST;
                break;
            case HostType.GUOSHUAI_HOST:
                host = GUOSHUAI_HOST;
                break;
            case HostType.CAITINGTING_HOST:
                host = CAITINGTING_HOST;
                break;
            case HostType.XIEXIN_HOSR:
                host = XIEXIN_HOST;
                break;
            case HostType.H5_CONFIG_HOST:
                host=H5_HOST;
                break;
            default:
                host = "";
                break;
        }
        return host;
    }
}
