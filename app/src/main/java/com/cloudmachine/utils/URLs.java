package com.cloudmachine.utils;


import com.cloudmachine.net.api.ApiConstants;

/**
 * Created by Administrator on 13-11-12.
 */
public class URLs {
    public static String UPLOAD_IMG_PATH = Constants.URL_IMAGE_HOST+"kindEditorUpload";//URL_API_HOST +"kindEditorUpload";//
    
    public static String UPLOAD_AVATOR = Constants.URL_IMAGE_HOST+"member/kindEditorUpload"; //URL_API_HOST + "member/kindEditorUpload";//
    public static String SUGGESTION_BACK =ApiConstants.CLOUDM_YJX_HOST+"feedback/addfeedback";
    

    public static String LOGIN_URL = ApiConstants.CLOUDM_YJX_HOST + "member/login";
    //与我相关
    public static String GETCODE_URL = ApiConstants.CLOUDM_HOST + "member/getCode";
    public static String EDITINFO_URL = ApiConstants.CLOUDM_YJX_HOST + "member/editInfoByKey";

    public static String REGISTNEW_URL = ApiConstants.CLOUDM_HOST + "member/registerNew";
    public static String FORGETPWD_URL = ApiConstants.CLOUDM_YJX_HOST + "member/forgetPwd";
    public static String UPDATEPWD_URL = ApiConstants.CLOUDM_YJX_HOST + "member/modifyPwd";
    //添加设备成员
    public static String ADD_MEMBER_NEW = ApiConstants.CLOUDM_YJX_HOST + "device/givePermissionNew";
    //获取历史轨迹
    public static String LOCUS = ApiConstants.CLOUDM_YJX_HOST + "device/locus";
    //获取评价标签
    public static String GET_TAG_INFO = ApiConstants.CLOUDM_YJX_HOST + "device/getTagInfo";
    //获取加盟站评价标签
    public static String GET_TAG_INFO_ALLIANCE=ApiConstants.CLOUDM_YJX_HOST+"device/getEvaluateTagFromAlliance";

    //获取工单详情
    public static String GET_WORK_DETAIL = ApiConstants.CLOUDM_YJX_HOST + "device/queryWorkDetail";
    //获取工作数据的统计
    public static String GET_WORK_STATISTICS = ApiConstants.CLOUDM_YJX_HOST + "device/getDataStatistic";
    //获取工单评价信息
    public static String GET_EVALUATE_INFO=ApiConstants.CLOUDM_YJX_HOST+"device/getEvaluateInfo";

    public static String GET_EVALUATE_INFO_ALLIANCE=ApiConstants.CLOUDM_YJX_HOST+"device/getEvaluateInfoFromAlliance";

}
