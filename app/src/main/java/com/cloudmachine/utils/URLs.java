package com.cloudmachine.utils;


import com.cloudmachine.net.api.ApiConstants;

/**
 * Created by Administrator on 13-11-12.
 */
public class URLs {
//    public final static String HOST = "api.cloudm.com";
	//public final static String HOST = "192.168.1.114";
//    public final static String HTTP = "http://";
//    public final static int PORT = 8083;
    public static String UPDATE_VERSION = Constants.URL_GETVERSION;
    
    public static String UPLOAD_IMG_PATH = Constants.URL_IMAGE_HOST+"/kindEditorUpload";//URL_API_HOST +"kindEditorUpload";//
    
    public static String UPLOAD_AVATOR = Constants.URL_IMAGE_HOST+"/member/kindEditorUpload"; //URL_API_HOST + "member/kindEditorUpload";//
    public static String COMMUNITY_URL = ApiConstants.CLOUDM_HOST+"community/list";
    //发帖/feedback/addfeedback?memberId=&content=

    public static String SUGGESTION_BACK =ApiConstants.CLOUDM_HOST+"feedback/addfeedback";
    
    public static String GET_AGENTS =ApiConstants.CLOUDM_HOST+"device/getDeviceAgents";
    
    public static String NEW_BLOD = ApiConstants.CLOUDM_HOST + "community/saveBlog";
    
    public static String GET_BLOD = ApiConstants.CLOUDM_HOST + "community/getBlogDetails";
    
    public static String GET_BLODCOMMENT = ApiConstants.CLOUDM_HOST + "community/getBlogcomment";
    //回帖
    public static String COMMENT_BLOG = ApiConstants.CLOUDM_HOST + "community/commentblog";
    
    public static String LOGIN_URL = ApiConstants.CLOUDM_HOST + "member/login";
    //获取个人信息
    public static String MEMBER_INFO = ApiConstants.CLOUDM_HOST + "member/getMemberInfoById";
    //与我相关
    public static String GETCODE_URL = ApiConstants.CLOUDM_HOST + "member/getCode";
    public static String USERSCORE_URL = ApiConstants.CLOUDM_HOST + "member/userScoreInfo";
    public static String INSERTSIGNPOINT_URL = ApiConstants.CLOUDM_HOST + "member/insertSignPoint";

   public static String EDITPICINFO_URL = ApiConstants.CLOUDM_HOST + "member/editIcphoto";
    public static String CHECKCODE_URL = ApiConstants.CLOUDM_HOST + "member/checkCode";
    public static String EDITINFO_URL = ApiConstants.CLOUDM_HOST + "member/editInfoByKey";

    public static String USER_URL = ApiConstants.CLOUDM_HOST + "member/regist";
    public static String REGISTNEW_URL = ApiConstants.CLOUDM_HOST + "member/registNew";
    public static String FORGETPWD_URL = ApiConstants.CLOUDM_HOST + "member/forgetPwd";
    public static String UPDATEPWD_URL = ApiConstants.CLOUDM_HOST + "member/modifyPwd";
    public static String BLOG_SEARCH = ApiConstants.CLOUDM_HOST + "community/search";
    //与我相关的帖子memberId
    public static String BLOG_RELATE_ME = ApiConstants.CLOUDM_HOST + "community/getBlogRelateMe";
    //我参与的帖子memberId
    public static String BLOG_JOIN = ApiConstants.CLOUDM_HOST + "community/getMyblog";
    
    public static String BLOG_SUPPORT = ApiConstants.CLOUDM_HOST + "community/praise";
    //获取所有的消息数量
    public static String GET_MESSAGECOUNT = ApiConstants.CLOUDM_HOST + "device/getMessageUntreatedCount";
   //获取所有的消息
    public static String GET_MESSAGE = ApiConstants.CLOUDM_HOST + "device/getAllMessages";
    public static String GET_MESSAGE_SYSTEM = ApiConstants.CLOUDM_HOST + "device/getSystemMessages";
    //添加设备成员
    public static String ADD_MEMBER_NEW = ApiConstants.CLOUDM_HOST + "device/givePermissionNew";
    //接收邀请
    public static String ACCEPTE = ApiConstants.CLOUDM_HOST+ "device/acceptMessage";
    //拒绝邀请
    public static String REFUSE = ApiConstants.CLOUDM_HOST + "device/rejectMessage";
    //更新状态
    public static String UPDATEMESSAGESTATUS = ApiConstants.CLOUDM_HOST + "device/updateMessageStatus";
    //获取设备故障信息
    public static String FAULT_INFO = ApiConstants.CLOUDM_HOST + "device/faultInfo";
    //获取设备故障点周边详细信息
    public static String FAULT_DITAILS = ApiConstants.CLOUDM_HOST + "device/faultDitails";
    //获取历史轨迹
    public static String LOCUS = ApiConstants.CLOUDM_HOST + "device/locus";
    //获取维修履历
    public static String REPAIRRECORD = ApiConstants.CLOUDM_HOST + "device/repairRecord";
    //问卷调查消息
    public static String QUESTION = ApiConstants.CLOUDM_HOST + "question/need";
    //获取报修历史
    public static String REPAIR_HISTORY = ApiConstants.CLOUDM_HOST + "device/getRepairList";
    //获取单个报修记录的基本信息
    public static String REPAIR_RECORD_BASIC_INFOMATION = ApiConstants.CLOUDM_HOST + "device/getOneRepairRecord";
    //获取评价标签
    public static String GET_TAG_INFO = ApiConstants.CLOUDM_HOST + "device/getTagInfo";
    //获取工单详情
    public static String GET_WORK_DETAIL = ApiConstants.CLOUDM_HOST + "/device/queryWorkDetail";
    //获取工作数据的统计
    public static String GET_WORK_STATISTICS = ApiConstants.CLOUDM_HOST + "/device/getDataStatistic";
    //获取工单评价信息
    public static String GET_EVALUATE_INFO=ApiConstants.CLOUDM_HOST+"device/getEvaluateInfo";
}
