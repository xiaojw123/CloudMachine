package com.cloudmachine.utils;


/**
 * Created by Administrator on 13-11-12.
 */
public class URLs {
//    public final static String HOST = "api.cloudm.com";
	//public final static String HOST = "192.168.1.114";
//    public final static String HTTP = "http://";
//    public final static int PORT = 8083;
    private final static String URL_SPLITTER = "/";
    private final static String URL_API_HOST = Constants.URL_MAIN+URL_SPLITTER;
    public static String UPDATE_VERSION = Constants.URL_GETVERSION;
    
    public static String UPLOAD_IMG_PATH = Constants.URL_IMAGE_HOST+"/kindEditorUpload";//URL_API_HOST +"kindEditorUpload";//
    
    public static String UPLOAD_AVATOR = Constants.URL_IMAGE_HOST+"/member/kindEditorUpload"; //URL_API_HOST + "member/kindEditorUpload";//
    public static String NEWS_URL = URL_API_HOST+"news/list";
    public static String COMMUNITY_URL = URL_API_HOST+"community/list";
    //发帖/feedback/addfeedback?memberId=&content=

    public static String SUGGESTION_BACK =URL_API_HOST+"feedback/addfeedback";
    
    public static String GET_AGENTS =URL_API_HOST+"device/getDeviceAgents";
    
    public static String NEW_BLOD = URL_API_HOST + "community/saveBlog";
    
    public static String GET_BLOD = URL_API_HOST + "community/getBlogDetails";
    
    public static String GET_BLODCOMMENT = URL_API_HOST + "community/getBlogcomment";    
    //回帖
    public static String COMMENT_BLOG = URL_API_HOST + "community/commentblog";
    
    public static String LOGIN_URL = URL_API_HOST + "member/login";
    //获取个人信息
    public static String MEMBER_INFO = URL_API_HOST + "member/getMemberInfoById";
    //机主
    public static String DEVICE_URL = URL_API_HOST +"device/list";
    //工程业主
    public static String PROJECT_MASTER = URL_API_HOST + "device/getDeviceByMaster";
    //与我相关
    public static String RELATE_ME = URL_API_HOST + "device/getDeviceRelateMe";
    public static String GETCODE_URL = URL_API_HOST + "member/getCode";
    public static String USERSCORE_URL = URL_API_HOST + "member/userScoreInfo";
    public static String INSERTSIGNPOINT_URL = URL_API_HOST + "member/insertSignPoint";

   public static String EDITPICINFO_URL = URL_API_HOST + "member/editIcphoto";
    public static String CHECKCODE_URL = URL_API_HOST + "member/checkCode";
    public static String EDITINFO_URL = URL_API_HOST + "member/editInfoByKey";

    public static String USER_URL = URL_API_HOST + "member/regist";
    public static String REGISTNEW_URL = URL_API_HOST + "member/registNew";
    public static String USER_EDITE = URL_API_HOST + "member/editInfo";
    public static String FORGETPWD_URL = URL_API_HOST + "member/forgetPwd";
    public static String UPDATEPWD_URL = URL_API_HOST + "member/modifyPwd";
    public static String GETROLS_URL = URL_API_HOST + "member/getRootNodes";
    public static String NEWS_KINDS = URL_API_HOST + "news/categorylist";
    public static String COMMUNITY_KINDS = URL_API_HOST + "community/categoryList";
    public static String BLOG_SEARCH = URL_API_HOST + "community/search";
    //删除设备
    public static String DELETE_DEVICE = URL_API_HOST + "device/delete";
    public static String GET_DEVICE = URL_API_HOST+"device/getDevice";
    //获取设备成员 memberId  deviceId
    public static String DEVICE_MEMBER = URL_API_HOST + "device/member";
    //与我相关的帖子memberId
    public static String BLOG_RELATE_ME = URL_API_HOST + "community/getBlogRelateMe";
    //我参与的帖子memberId
    public static String BLOG_JOIN = URL_API_HOST + "community/getMyblog";
    
    public static String BLOG_SUPPORT = URL_API_HOST + "community/praise";
    //获取所有的消息数量
    public static String GET_MESSAGECOUNT = URL_API_HOST + "device/getMessageUntreatedCount";
   //获取所有的消息
    public static String GET_MESSAGE = URL_API_HOST + "device/getAllMessages";
    public static String GET_MESSAGE_SYSTEM = URL_API_HOST + "device/getSystemMessages";
    //搜索设备成员
    public static String SEARCH_MEMBER = URL_API_HOST + "device/search";
    //添加设备成员
    public static String ADD_MEMBER_NEW = URL_API_HOST + "device/givePermissionNew";
    //删除设备成员
    public static String DELETE_MEMBER = URL_API_HOST+ "device/deleteMember";
    //添加设备
    public static String ADD_DEVICE = URL_API_HOST + "device/save";
    //权限菜单
    public static String PERMISSION_MENU = URL_API_HOST + "member/getPermissions";
    //接收邀请
    public static String ACCEPTE = URL_API_HOST+ "device/acceptMessage";
    //拒绝邀请
    public static String REFUSE = URL_API_HOST + "device/rejectMessage";
    //更新状态
    public static String UPDATEMESSAGESTATUS = URL_API_HOST + "device/updateMessageStatus";
    //添加成员
    public static String ADD_MEMBER = URL_API_HOST + "device/givePermission";
    //更换权限
    public static String UPDATE_PERMISSION = URL_API_HOST + "device/updatePermission";
    //搜索设备
    public static String SEARCH_DEVICE = URL_API_HOST + "device/searchDevice";
    //获取设备故障信息
    public static String FAULT_INFO = URL_API_HOST + "device/faultInfo";
    //获取设备故障点周边详细信息
    public static String FAULT_DITAILS = URL_API_HOST + "device/faultDitails";
    //获取历史轨迹
    public static String LOCUS = URL_API_HOST + "device/locus";
    //获取维修履历
    public static String REPAIRRECORD = URL_API_HOST + "device/repairRecord";
    //问卷调查消息
    public static String QUESTION = URL_API_HOST + "question/need"; 
    //获取报修历史
    public static String REPAIR_HISTORY = URL_API_HOST + "device/getRepairList"; 
    //获取单个报修记录的基本信息
    public static String REPAIR_RECORD_BASIC_INFOMATION = URL_API_HOST + "device/getOneRepairRecord"; 
    //获取评价标签
    public static String GET_TAG_INFO = URL_API_HOST + "device/getTagInfo";
    //获取工单详情
    public static String GET_WORK_DETAIL = URL_API_HOST + "/device/queryWorkDetail";
    //获取工作数据的统计
    public static String GET_WORK_STATISTICS = URL_API_HOST + "/device/getDataStatistic";
}
