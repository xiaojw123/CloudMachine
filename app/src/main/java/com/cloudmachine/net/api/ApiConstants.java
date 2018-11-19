package com.cloudmachine.net.api;


import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.cloudmachine.BuildConfig;
import com.cloudmachine.base.bean.BaseRespose;

public class ApiConstants {

    //线上环境
    public static String ONLINE_HOST1 = "http://api.cloudm.com/lark/";
    private static String ONLINE_HOST2 = "http://h5.cloudm.com/2n/";
    private static String ONLINE_HOST3 = "http://camera.cloudm.com:18089/";
    private static String ONLINE_HOST4 = "http://d.cloudm.com/configP";

    //预发布
    public static String PRE_HOST1 = "http://api.test.cloudm.com/lark/";
    private static String PRE_HOST2 = "http://h5.test.cloudm.com/2n/";
    private static String PRE_HOST3 = "http://183.129.196.42:18089/";
    private static String PRE_HOST4 = "http://d.cloudm.com/configD";

    //109
    public static String LOCAL_HOST1 = "http://192.168.1.109:19086/lark/";
    private static String LOCAL_HOST2 = "http://192.168.1.109:7718/";
    private static String LOCAL_HOST3 = "http://192.168.1.176:18089/";
    private static String LOCAL_HOST4 = "http://d.cloudm.com/configT";
    //179
    public static String INTEFACE_HOST1 = "http://192.168.1.179:58080/lark/";


    /* 测试环境*/
    private static String REMOTE_HOST0 = BuildConfig.IS_ONLINE ? ONLINE_HOST1 : PRE_HOST1;
    private static String REMOTE_HOST1 = BuildConfig.IS_ONLINE ? ONLINE_HOST2 : PRE_HOST2;
    private static String REMOTE_HOST2 = BuildConfig.IS_ONLINE ? ONLINE_HOST3 : PRE_HOST3;//云黑子-高配-拍照
    private static String REMOTE_HOST3 = BuildConfig.IS_ONLINE ? ONLINE_HOST4 : PRE_HOST4;
    private static final String T_HOST0 = BuildConfig.IS_REMOTE ? REMOTE_HOST0 : LOCAL_HOST1;
    private static final String T_HOST1 = BuildConfig.IS_REMOTE ? REMOTE_HOST1 : LOCAL_HOST2;
    private static final String T_HOST2 = BuildConfig.IS_REMOTE ? REMOTE_HOST2 : LOCAL_HOST3;//云黑子-高
    private static final String T_HOST3 = BuildConfig.IS_REMOTE ? REMOTE_HOST3 : LOCAL_HOST4;


    public static String LARK_HOST = BuildConfig.IS_INTERFACE ? INTEFACE_HOST1 : T_HOST0;
    private static String H5_HOST = BuildConfig.DEBUG ? T_HOST1 : ONLINE_HOST2;
    private static String BOX_HIGH_COFIG_HOST = BuildConfig.DEBUG ? T_HOST2 : ONLINE_HOST3;
    public static String H5_CONFIG_URL = BuildConfig.DEBUG ? T_HOST3 : ONLINE_HOST4;
    public static final String URL_H5_ARGUMENT = "http://www.cloudm.com/agreement";

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
    public static String AppUseHelper = "https://h5.cloudm.com/n/help/home";
    public static String AppWorkTimeStatistics = "https://h5.cloudm.com/n/time_statistics";
    public static String AppOrderList = "https://h5.cloudm.com/n/order/olist";
    public static String AppWalletHelper = "http://h5.cloudm.com/n/wallet_description";
    public static String AppFeedback = "https://h5.cloudm.com/n/feedback";
    public static String AppWagesLoan;
    public static String AppQR = "http://h5.cloudm.com/static/qr.html";
    public static String CLOCK = H5_HOST + "clock_login";
    public static String APPForgetPassword;
    public static String APPFwtk;
    public static String APPRzgj;
    public static String APPBmxy;
    public static String APPSjyysxy;
    public static String AppBankList;
    public static String AppBankCmVerify;
    public static String AppExChange;
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
            case HostType.HOST_LARK:
                host = LARK_HOST;
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

    public static void saveHostConfig(Context context, int pos) {
        SharedPreferences sp = context.getSharedPreferences("HOST_CONFIG", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        String host1, host2, host3, host4;
        switch (pos) {
            case 0://线上
                host1 = ONLINE_HOST1;
                host2 = ONLINE_HOST2;
                host3 = ONLINE_HOST3;
                host4 = ONLINE_HOST4;
                break;
            case 1://预发
                host1 = PRE_HOST1;
                host2 = PRE_HOST2;
                host3 = PRE_HOST3;
                host4 = PRE_HOST4;
                break;
            case 2://109
                host1 = LOCAL_HOST1;
                host2 = LOCAL_HOST2;
                host3 = LOCAL_HOST3;
                host4 = LOCAL_HOST4;
                break;
            case 3://179
                host1 = INTEFACE_HOST1;
                host2 = LOCAL_HOST2;
                host3 = LOCAL_HOST3;
                host4 = LOCAL_HOST4;
                break;
            default:
                host1 = host2 = host3 = host4 = "undefined";
                break;
        }
        editor.putString("host1", host1);
        editor.putString("host2", host2);
        editor.putString("host3", host3);
        editor.putString("host4", host4);
        editor.apply();
    }

    public static void initHost(Context context) {
        SharedPreferences sp = context.getSharedPreferences("HOST_CONFIG", Context.MODE_PRIVATE);
        String host1 = sp.getString("host1", null);
        String host2 = sp.getString("host2", null);
        String host3 = sp.getString("host3", null);
        String host4 = sp.getString("host4", null);
        if (!TextUtils.isEmpty(host1)) {
            LARK_HOST = host1;
            H5_HOST = host2;
            BOX_HIGH_COFIG_HOST = host3;
            H5_CONFIG_URL = host4;
        }
    }


}
