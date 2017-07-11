package com.cloudmachine.api;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/3/12 下午10:03
 * 修改人：shixionglu
 * 修改时间：2017/3/12 下午10:03
 * 修改备注：
 */

public class ApiConstants {

/* 测试环境*/
//   public static final String CLOUDM_HOST = "http://api.test.cloudm.com/cloudm3/yjx/";
//    public static final String GUOSHUAI_HOST = "http://api.test.cloudm.com/cloudm3/";
//    public static final String CAITINGTING_HOST = "http://api.test.cloudm.com/cloudm3/";
//    public static final String XIEXIN_HOST = "http://ask.test.cloudm.com/";
//    public static final String H5_HOST = "http://h5.test.cloudm.com/";

/*线上环境*/
    public static final String CLOUDM_HOST = "http://api.cloudm.com/cloudm3/yjx/";
    public static final String GUOSHUAI_HOST = "http://api.cloudm.com/cloudm3/";
    public static final String CAITINGTING_HOST = "http://api.cloudm.com/cloudm3/";
    public static final String XIEXIN_HOST = "http://ask.cloudm.com/";
    public static final String H5_HOST = "http://h5.cloudm.com/";
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
            /*case HostType.GANK_GIRL_PHOTO:
                host = SINA_PHOTO_HOST;
                break;
            case HostType.NEWS_DETAIL_HTML_PHOTO:
                host = "http://kaku.com/";
                break;*/

            case HostType.GUOSHUAI_HOST:
                host = GUOSHUAI_HOST;
                break;
            case HostType.CAITINGTING_HOST:
                host = CAITINGTING_HOST;
                break;
            case HostType.XIEXIN_HOSR:
                host = XIEXIN_HOST;
                break;
//            case HostType.ZHIHU_HOST:
//                host = ZHIHU_HOST;
//                break;
//            case HostType.LIZHAOHUA_HOST:
//                host = LIZHAOHUA_HOST;
//                break;
            default:
                host = "";
                break;
        }
        return host;
    }
}
