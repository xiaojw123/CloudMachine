package com.cloudmachine.utils;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.cloudmachine.MyApplication;
import com.cloudmachine.R;
import com.cloudmachine.activities.GalleryActivity;
import com.cloudmachine.activities.ImagePagerActivity;
import com.cloudmachine.autolayout.widgets.CustomDialog;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.bean.McDeviceBasicsInfo;
import com.cloudmachine.cache.MySharedPreferences;
import com.cloudmachine.chart.utils.AppLog;
import com.cloudmachine.net.api.ApiConstants;
import com.cloudmachine.net.task.ImageUploadAsync;
import com.cloudmachine.net.task.PermissionsListAsync;
import com.cloudmachine.ui.home.activity.HomeActivity;
import com.cloudmachine.ui.login.acticity.LoginActivity;
import com.cloudmachine.utils.photo.util.ImageItem;
import com.cloudmachine.utils.widgets.BadgeView;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Constants {
    public static final int HANDLER_CHANGE_MAP = 1;
    public static final int HANDLER_GETDEVICELIST_SUCCESS = HANDLER_CHANGE_MAP + 1;
    public static final int HANDLER_GETDEVICELIST_FAIL = HANDLER_GETDEVICELIST_SUCCESS + 1;
    public static final int HANDLER_GETDEVICEINFO_SUCCESS = HANDLER_GETDEVICELIST_FAIL + 1;
    public static final int HANDLER_GETDEVICEINFO_FAIL = HANDLER_GETDEVICEINFO_SUCCESS + 1;
    public static final int HANDLER_SCROLLVIEW_DOWN = HANDLER_GETDEVICEINFO_FAIL + 1;
    public static final int HANDLER_GETDEVICEBASICSINFO_SUCCESS = HANDLER_SCROLLVIEW_DOWN + 1;
    public static final int HANDLER_GETDEVICEBASICSINFO_FAIL = HANDLER_GETDEVICEBASICSINFO_SUCCESS + 1;
    public static final int HANDLER_GETDEVICEMEMBER_SUCCESS = HANDLER_GETDEVICEBASICSINFO_FAIL + 1;
    public static final int HANDLER_GETDEVICEMEMBER_FAIL = HANDLER_GETDEVICEMEMBER_SUCCESS + 1;
    public static final int HANDLER_DELETEMEMBER_SUCCESS = HANDLER_GETDEVICEMEMBER_FAIL + 1;
    public static final int HANDLER_DELETEMBER_FAIL = HANDLER_DELETEMEMBER_SUCCESS + 1;
    public static final int HANDLER_SEARCHMEMBER_SUCCESS = HANDLER_DELETEMBER_FAIL + 1;
    public static final int HANDLER_SEARCHMEMBER_FAIL = HANDLER_SEARCHMEMBER_SUCCESS + 1;
    public static final int HANDLER_ADDMEMBER_SUCCESS = HANDLER_SEARCHMEMBER_FAIL + 1;
    public static final int HANDLER_ADDMEMBER_FAIL = HANDLER_ADDMEMBER_SUCCESS + 1;
    public static final int HANDLER_GETROOTNODES_SUCCESS = HANDLER_ADDMEMBER_FAIL + 1;
    public static final int HANDLER_GETROOTNODES_FAIL = HANDLER_GETROOTNODES_SUCCESS + 1;
    public static final int HANDLER_UPDATEDEVICEBYKEY_SUCCESS = HANDLER_GETROOTNODES_FAIL + 1;
    public static final int HANDLER_UPDATEDEVICEBYKEY_FAIL = HANDLER_UPDATEDEVICEBYKEY_SUCCESS + 1;
    public static final int HANDLER_ADDDEVICE_SUCCESS = HANDLER_UPDATEDEVICEBYKEY_FAIL + 1;
    public static final int HANDLER_ADDDEVICE_FAIL = HANDLER_ADDDEVICE_SUCCESS + 1;
    public static final int HANDLER_DELETEDEVICE_SUCCESS = HANDLER_ADDDEVICE_FAIL + 1;
    public static final int HANDLER_DELETEDEVICE_FAIL = HANDLER_DELETEDEVICE_SUCCESS + 1;
    public static final int HANDLER_SWITCH_GUIDACTIVITY = HANDLER_DELETEDEVICE_FAIL + 1;
    public static final int HANDLER_SWITCH_MAINACTIVITY = HANDLER_SWITCH_GUIDACTIVITY + 1;
    public static final int HANDLER_FINISH_IMAGEPAGERACTIVITY = HANDLER_SWITCH_MAINACTIVITY + 1;
    public static final int HANDLER_GETPERMISSIONS_SUCCESS = HANDLER_FINISH_IMAGEPAGERACTIVITY + 1;
    public static final int HANDLER_GETPERMISSIONS_FAIL = HANDLER_GETPERMISSIONS_SUCCESS + 1;
    public static final int HANDLER_GETMACHINETYPES_SUCCESS = HANDLER_GETPERMISSIONS_FAIL + 1;
    public static final int HANDLER_GETMACHINETYPES_FAIL = HANDLER_GETMACHINETYPES_SUCCESS + 1;
    public static final int HANDLER_GETMACHINEBRAND_SUCCESS = HANDLER_GETMACHINETYPES_FAIL + 1;
    public static final int HANDLER_GETMACHINEBRAND_FAIL = HANDLER_GETMACHINEBRAND_SUCCESS + 1;
    public static final int HANDLER_GETMACHINEMODEL_SUCCESS = HANDLER_GETMACHINEBRAND_FAIL + 1;
    public static final int HANDLER_GETMACHINEMODEL_FAIL = HANDLER_GETMACHINEMODEL_SUCCESS + 1;
    public static final int HANDLER_TIMER = HANDLER_GETMACHINEMODEL_FAIL + 1;
    public static final int HANDLER_GETVERSION_SUCCESS = HANDLER_TIMER + 1;
    public static final int HANDLER_GETVERSION_FAIL = HANDLER_GETVERSION_SUCCESS + 1;
    public static final int HANDLER_VERSIONDOWNLOAD = HANDLER_GETVERSION_FAIL + 1;
    public static final int HANDLER_GETCOMMUNITYLIST_SUCCESS = HANDLER_VERSIONDOWNLOAD + 1;
    public static final int HANDLER_GETALLMESSAGECOUNT_FAIL = HANDLER_GETCOMMUNITYLIST_SUCCESS + 1;
    public static final int HANDLER_GETALLMESSAGELIST_SUCCESS = HANDLER_GETALLMESSAGECOUNT_FAIL + 1;
    public static final int HANDLER_GETALLMESSAGELIST_FAIL = HANDLER_GETALLMESSAGELIST_SUCCESS + 1;
    public static final int HANDLER_BLOGSUPPORT_SUCCESS = HANDLER_GETALLMESSAGELIST_FAIL + 1;
    public static final int HANDLER_BLOGSUPPORT_FAIL = HANDLER_BLOGSUPPORT_SUCCESS + 1;
    public static final int HANDLER_BLOGSUPPORTLIST_SUCCESS = HANDLER_BLOGSUPPORT_FAIL + 1;
    public static final int HANDLER_BLOGSUPPORTLIST_FAIL = HANDLER_BLOGSUPPORTLIST_SUCCESS + 1;
    public static final int HANDLER_BLOGDETAIL_SUCCESS = HANDLER_BLOGSUPPORTLIST_FAIL + 1;
    public static final int HANDLER_BLOGDETAIL_FAIL = HANDLER_BLOGDETAIL_SUCCESS + 1;
    public static final int HANDLER_BLOGCOMMENTSLIST_SUCCESS = HANDLER_BLOGDETAIL_FAIL + 1;
    public static final int HANDLER_BLOGCOMMENTSLIST_FAIL = HANDLER_BLOGCOMMENTSLIST_SUCCESS + 1;
    public static final int HANDLER_GETSENSORPOSITION_SUCCESS = HANDLER_BLOGCOMMENTSLIST_FAIL + 1;
    public static final int HANDLER_GETSENSORPOSITION_FAIL = HANDLER_GETSENSORPOSITION_SUCCESS + 1;
    public static final int HANDLER_GETSENSORLIST_SUCCESS = HANDLER_GETSENSORPOSITION_FAIL + 1;
    public static final int HANDLER_GETSENSORLIST_FAIL = HANDLER_GETSENSORLIST_SUCCESS + 1;
    public static final int HANDLER_SAVESENSOR_SUCCESS = HANDLER_GETSENSORLIST_FAIL + 1;
    public static final int HANDLER_SAVESENSOR_FAIL = HANDLER_SAVESENSOR_SUCCESS + 1;
    public static final int HANDLER_DELETESENSOR_SUCCESS = HANDLER_SAVESENSOR_FAIL + 1;
    public static final int HANDLER_DELETESENSOR_FAIL = HANDLER_DELETESENSOR_SUCCESS + 1;
    public static final int HANDLER_GETCODE_SUCCESS = HANDLER_DELETESENSOR_FAIL + 1;
    public static final int HANDLER_GETCODE_FAIL = HANDLER_GETCODE_SUCCESS + 1;
    public static final int HANDLER_GETVIEWHW = HANDLER_GETCODE_FAIL + 1;
    public static final int HANDLER_UPCLIENTID_SUCCESS = HANDLER_GETVIEWHW + 1;
    public static final int HANDLER_UPCLIENTID_FAIL = HANDLER_UPCLIENTID_SUCCESS + 1;
    public static final int HANDLER_GETMEMBERINFO_SUCCESS = HANDLER_UPCLIENTID_FAIL + 1;
    public static final int HANDLER_GETMEMBERINFO_FAIL = HANDLER_GETMEMBERINFO_SUCCESS + 1;
    public static final int HANDLER_MESSAGEACCEPTE_SUCCESS = HANDLER_GETMEMBERINFO_FAIL + 1;
    public static final int HANDLER_MESSAGEREFUSE_SUCCESS = HANDLER_MESSAGEACCEPTE_SUCCESS + 1;
    public static final int HANDLER_MESSAGEUPSTATE_SUCCESS = HANDLER_MESSAGEREFUSE_SUCCESS + 1;
    public static final int HANDLER_MESSAGEACTION_FAIL = HANDLER_MESSAGEUPSTATE_SUCCESS + 1;
    public static final int HANDLER_LOGIN_SUCCESS = HANDLER_MESSAGEACTION_FAIL + 1;
    public static final int HANDLER_LOGIN_FAIL = HANDLER_LOGIN_SUCCESS + 1;
    public static final int HANDLER_UPDATEMEMBERINFO_SUCCESS = HANDLER_LOGIN_FAIL + 1;
    public static final int HANDLER_UPDATEMEMBERINFO_FAIL = HANDLER_UPDATEMEMBERINFO_SUCCESS + 1;
    public static final int HANDLER_FORGETPWD_SUCCESS = HANDLER_UPDATEMEMBERINFO_FAIL + 1;
    public static final int HANDLER_FORGETPWD_FAIL = HANDLER_FORGETPWD_SUCCESS + 1;
    public static final int HANDLER_UPDATEPWD_SUCCESS = HANDLER_FORGETPWD_FAIL + 1;
    public static final int HANDLER_UPDATEPWD_FAIL = HANDLER_UPDATEPWD_SUCCESS + 1;
    public static final int HANDLER_REGISTER_SUCCESS = HANDLER_UPDATEPWD_FAIL + 1;
    public static final int HANDLER_REGISTER_FAIL = HANDLER_REGISTER_SUCCESS + 1;
    public static final int HANDLER_CHECKCODE_SUCCESS = HANDLER_REGISTER_FAIL + 1;
    public static final int HANDLER_CHECKCODE_FAIL = HANDLER_CHECKCODE_SUCCESS + 1;
    public static final int HANDLER_SEARCHBLOG_SUCCESS = HANDLER_CHECKCODE_FAIL + 1;
    public static final int HANDLER_SEARCHBLOG_FAIL = HANDLER_SEARCHBLOG_SUCCESS + 1;
    public static final int HANDLER_GETMYBLOG_SUCCESS = HANDLER_SEARCHBLOG_FAIL + 1;
    public static final int HANDLER_GETMYBLOG_FAIL = HANDLER_GETMYBLOG_SUCCESS + 1;
    public static final int HANDLER_NEWBLOG_SUCCESS = HANDLER_GETMYBLOG_FAIL + 1;
    public static final int HANDLER_NEWBLOG_FAIL = HANDLER_NEWBLOG_SUCCESS + 1;
    public static final int HANDLER_COMMENTBLOG_SUCCESS = HANDLER_NEWBLOG_FAIL + 1;
    public static final int HANDLER_COMMENTBLOG_FAIL = HANDLER_COMMENTBLOG_SUCCESS + 1;
    public static final int HANDLER_FEEDBACK_SUCCESS = HANDLER_COMMENTBLOG_FAIL + 1;
    public static final int HANDLER_FEEDBACK_FAIL = HANDLER_FEEDBACK_SUCCESS + 1;
    public static final int HANDLER_GOTO_MESSAGECONTENT = HANDLER_FEEDBACK_FAIL + 1;
    public static final int HANDLER_GETAGENTS_SUCCESS = HANDLER_GOTO_MESSAGECONTENT + 1;
    public static final int HANDLER_GETAGENTS_FAIL = HANDLER_GETAGENTS_SUCCESS + 1;
    public static final int HANDLER_ADDFENCE_SUCCESS = HANDLER_GETAGENTS_FAIL + 1;
    public static final int HANDLER_ADDFENCE_FAIL = HANDLER_ADDFENCE_SUCCESS + 1;
    public static final int HANDLER_DELETEFENCE_SUCCESS = HANDLER_ADDFENCE_FAIL + 1;
    public static final int HANDLER_DELETEFENCE_FAIL = HANDLER_DELETEFENCE_SUCCESS + 1;
    public static final int HANDLER_UPLOADCARDPIC_SUCCESS = HANDLER_DELETEFENCE_FAIL + 1;
    public static final int HANDLER_UPLOADCARDPIC_FAIL = HANDLER_UPLOADCARDPIC_SUCCESS + 1;
    public static final int HANDLER_DAILYWORK_SUCCESS = HANDLER_UPLOADCARDPIC_FAIL + 1;
    public static final int HANDLER_DAILYWORK_FAIL = HANDLER_DAILYWORK_SUCCESS + 1;
    public static final int HANDLER_FAULTINFO_SUCCESS = HANDLER_DAILYWORK_FAIL + 1;
    public static final int HANDLER_FAULTINFO_FAIL = HANDLER_FAULTINFO_SUCCESS + 1;
    public static final int HANDLER_FAULTDITAILS_SUCCESS = HANDLER_FAULTINFO_FAIL + 1;
    public static final int HANDLER_FAULTDITAILS_FAIL = HANDLER_FAULTDITAILS_SUCCESS + 1;
    public static final int HANDLER_LOCUS_SUCCESS = HANDLER_FAULTDITAILS_FAIL + 1;
    public static final int HANDLER_LOCUS_FAIL = HANDLER_LOCUS_SUCCESS + 1;
    public static final int HANDLER_REPAIRRECORD_SUCCESS = HANDLER_LOCUS_FAIL + 1;
    public static final int HANDLER_REPAIRRECORD_FAIL = HANDLER_REPAIRRECORD_SUCCESS + 1;
    public static final int HANDLER_INTEGRAL_SUCCESS = HANDLER_REPAIRRECORD_FAIL + 1;
    public static final int HANDLER_INTEGRAL_FAIL = HANDLER_INTEGRAL_SUCCESS + 1;
    public static final int HANDLER_CHART_MARKER_TIME = HANDLER_INTEGRAL_FAIL + 1;
    public static final int HANDLER_QUESTION_SUCCESS = HANDLER_CHART_MARKER_TIME + 1;
    public static final int HANDLER_QUESTION_FAIL = HANDLER_QUESTION_SUCCESS + 1;
    public static final int HANDLER_REPAIR_RECORD_BASIC_INFOMATION_SUCCESS = HANDLER_QUESTION_FAIL + 1;
    public static final int HANDLER_REPAIR_RECORD_BASIC_INFOMATION_FAILED = HANDLER_REPAIR_RECORD_BASIC_INFOMATION_SUCCESS + 1;
    public static final int HANDLER_GETMACHINEDETAIL_SUCCESS = HANDLER_REPAIR_RECORD_BASIC_INFOMATION_FAILED + 1;
    public static final int HANDLER_GETMACHINEDETAIL_FAILD = HANDLER_GETMACHINEDETAIL_SUCCESS + 1;
    public static final int HANDLE_GETCHECKREPORT_SUCCESS = HANDLER_GETMACHINEDETAIL_FAILD + 1;
    public static final int HANDLE_GETCHECKREPORT_FAILD = HANDLE_GETCHECKREPORT_SUCCESS + 1;
    public static final int HANDLE_GETOILLIST_SUCCESS = HANDLE_GETCHECKREPORT_FAILD + 1;
    public static final int HANDLE_GETOILLIST_FAILD = HANDLE_GETOILLIST_SUCCESS + 1;
    public static final int HANDLE_GETWORKTIMELIST_SUCCESS = HANDLE_GETOILLIST_FAILD + 1;
    public static final int HANDLE_GETWORKTIMELIST_FAILD = HANDLE_GETWORKTIMELIST_SUCCESS + 1;
    public static final int HANDLER_GETMACHINETYPE_SUCCESS = HANDLE_GETWORKTIMELIST_FAILD + 1;
    public static final int HANDLER_GETMACHINETYPE_FAILD = HANDLER_GETMACHINETYPE_SUCCESS + 1;
    public static final int HANDLER_GETTAGINFO_SUCCESS = HANDLER_GETMACHINETYPE_FAILD + 1;
    public static final int HANDLER_GETTAGINFO_FAILD = HANDLER_GETTAGINFO_SUCCESS + 1;
    public static final int HANDLER_ADDMEMBER = HANDLER_GETTAGINFO_FAILD + 1;
    public static final int REQUEST_SELECTCITY = HANDLER_ADDMEMBER + 1;
    public static final int REQUEST_MAPCHOOSE = REQUEST_SELECTCITY + 1;
    public static final int HANDLER_NEWREPAIR_SUCCESS = HANDLER_ADDMEMBER + 1;
    public static final int HANDLER_NEWREPAIR_FAILD = HANDLER_NEWREPAIR_SUCCESS + 1;
    public static final int HANDLER_GET_BODETAIL_SUCCESS = HANDLER_NEWREPAIR_FAILD + 1;
    public static final int HANDLER_GET_CWDETAIL_SUCCESS = HANDLER_GET_BODETAIL_SUCCESS + 1;
    public static final int HANDLER_GET_WORKDETAIL_FAILD = HANDLER_GET_CWDETAIL_SUCCESS + 1;
    public static final int HANDLER_SUBMITCOMMENT_SUCCESS = HANDLER_GET_WORKDETAIL_FAILD + 1;
    public static final int HANDLER_SUBMITCOMMENT_FAILD = HANDLER_SUBMITCOMMENT_SUCCESS + 1;
    public static final int HANDLER_ADDCIRCLEFENCH_SUCCESS = HANDLER_SUBMITCOMMENT_FAILD + 1;
    public static final int HANDLER_ADDCIRCLEFENCH_FAILD = HANDLER_ADDCIRCLEFENCH_SUCCESS + 1;
    public static final int HANDLER_GETDATASTATISTICS_SUCCESS = HANDLER_ADDCIRCLEFENCH_FAILD + 1;
    public static final int HANDLER_GETDATASTATISTICS_FAILD = HANDLER_GETDATASTATISTICS_SUCCESS + 1;
    public static final int HANDLER_GETCOUPON_SUCCESS = HANDLER_GETDATASTATISTICS_FAILD + 1;
    public static final int HANDLER_GETCOUPON_FAILD = HANDLER_GETCOUPON_SUCCESS + 1;
    public static final int HANDLER_GETDELIVERYMETHOD_SUCCESS = HANDLER_GETCOUPON_FAILD + 1;
    public static final int HANDLER_GETDELIVERYMETHOD_FAILD = HANDLER_GETDELIVERYMETHOD_SUCCESS + 1;
    public static final int HANDLER_GETSETUPTIME_SUCCESS = HANDLER_GETDELIVERYMETHOD_FAILD + 1;
    public static final int HANDLER_GETSETUPTIME_FAILD = HANDLER_GETSETUPTIME_SUCCESS + 1;
    public static final int HANDLER_GETYUNBOXPAY_SUCCESS = HANDLER_GETSETUPTIME_FAILD + 1;
    public static final int HANDLER_GETYUNBOXPAY_FAILD = HANDLER_GETYUNBOXPAY_SUCCESS + 1;
    public static final int HANDLER_GETCOUPONS_SUCCESS = HANDLER_GETYUNBOXPAY_FAILD + 1;
    public static final int HANDLER_GETCOUPONS_FAILD = HANDLER_GETCOUPONS_SUCCESS + 1;
    public static final int HANDLER_GETPAYPRICE_SUCCESS = HANDLER_GETCOUPONS_FAILD + 1;
    public static final int HANDLER_GETPAYPRICE_FAILD = HANDLER_GETPAYPRICE_SUCCESS + 1;
    public static final int HANDLER_GETCWPAY_SUCCESS = HANDLER_GETPAYPRICE_FAILD + 1;
    public static final int HANDLER_GETCWPAY_FAILD = HANDLER_GETCWPAY_SUCCESS + 1;
    public static final int HANDLER_CHECKPAY_SUCCESS = HANDLER_GETCWPAY_FAILD + 1;
    public static final int HANDLER_CHECKPAY_FAIDL = HANDLER_CHECKPAY_SUCCESS + 1;
    public static final int HANDLER_YUNBOXSTOREVOLUME_SUCCESS = HANDLER_CHECKPAY_FAIDL + 1;
    public static final int HANDLER_YUNBOXSTOREVOLUME_FAILD = HANDLER_YUNBOXSTOREVOLUME_SUCCESS + 1;
    public static final int HANDLER_SAVEARRIVALNOTICE_SUCCESS = HANDLER_YUNBOXSTOREVOLUME_FAILD + 1;
    public static final int HANDLER_SAVEARRIVALNOTICE_FAILD = HANDLER_SAVEARRIVALNOTICE_SUCCESS + 1;
    public static final int HANDLER_GETACCESSTOKEN_SUCCESS = HANDLER_SAVEARRIVALNOTICE_FAILD + 1;
    public static final int HANDLER_GETUSERMSG_SUCCESS = HANDLER_GETACCESSTOKEN_SUCCESS + 1;
    public static final int HANDLER_UPLOAD_SUCCESS = HANDLER_GETUSERMSG_SUCCESS + 1;
    public static final int HANDLER_UPLOAD_FAILD = HANDLER_UPLOAD_SUCCESS + 1;
    public static final int HANDLER_UPDATE_INFO_SUCCESS = HANDLER_UPLOAD_FAILD + 1;
    public static final int HANDLER_UPDATE_INFO_FAILD = HANDLER_UPDATE_INFO_SUCCESS + 1;
    public static final int HANDLER_JS_BODY = HANDLER_UPDATE_INFO_FAILD + 1;
    public static final int HANDLER_JS_JUMP = HANDLER_JS_BODY + 1;
    public static final int HANDLER_JS_ALERT = HANDLER_JS_JUMP + 1;
    public static final int HANDLER_GET_EVALUATE_INFO_SUCCESS = HANDLER_JS_ALERT + 1;
    public static final int HANDLER_GET_EVALUATE_INFO_FAILED = HANDLER_GET_EVALUATE_INFO_SUCCESS + 1;
    public static final int HANDLER_REPAIR = HANDLER_GET_EVALUATE_INFO_FAILED + 1;
    public static final int HANDLER_SHOW_CLOSE_BTN = HANDLER_REPAIR + 1;
    public static final int HANDLER_HIDEN_CLOSE_BTN = HANDLER_SHOW_CLOSE_BTN + 1;
    public static final int HANDLER_ALIPAY_RESULT = HANDLER_HIDEN_CLOSE_BTN + 1;
    public static final int HANDLER_JUMP_MY_ORDER = HANDLER_ALIPAY_RESULT + 1;
    public static final int HANDLER_H5_JUMP = HANDLER_JUMP_MY_ORDER + 1;
    public static final int HANDLER_CHANGE_BOX_ACT = HANDLER_H5_JUMP + 1;
    public static final int HANDLER_RESULT_APLIPAY = HANDLER_CHANGE_BOX_ACT + 1;
    public static final int HANDLER_UPDATE_PROGRESS = HANDLER_RESULT_APLIPAY + 1;
    public static final int HANDLER_BACk_LOCATION=HANDLER_UPDATE_PROGRESS+1;

    public static final String URL_H5_ARGUMENT = "http://www.cloudm.com/agreement";
    public static final String URL_LOGOCLOUDM = "https://f1.cloudm.com/logocloudm.png";
    //修改全局ip地址
//    public static final String URL_MAIN = "http://api.test.cloudm.com";//"http://api.test.cloudm.com";//http://192.168.1.8:8083//http://192.168.1.13:8090/cloudm3

    public static final String URL_IMAGE_HOST = ApiConstants.CLOUDM_YJX_HOST;// 文件上传地址
    public static final String URL_MyDevices = ApiConstants.CLOUDM_YJX_HOST
            + "device/getDeviceByKey"; // 获取设备列表
    public static final String URL_Devices = ApiConstants.CLOUDM_YJX_HOST + "device/getDevice";// 获取设备基本信息
    public static final String URL_MEMBERLIST = ApiConstants.CLOUDM_YJX_HOST + "device/member";// 获取设备成员列表
    public static final String URL_OILLEVELIST = ApiConstants.CLOUDM_YJX_HOST + "device/oilLevelList";// 获取油位列表
    public static final String URL_WORKTIMELIST = ApiConstants.CLOUDM_YJX_HOST + "device/whdList";// 获取工作时长列表
    public static final String URL_DELETEMEMBER = ApiConstants.CLOUDM_YJX_HOST
            + "device/deleteMember";// 删除设备成员
    public static final String URL_DELETEFENCE = ApiConstants.CLOUDM_YJX_HOST
            + "device/deleteFence";// 删除围栏
    public static final String URL_SEARCHMEMBER = ApiConstants.CLOUDM_YJX_HOST + "device/search";// 搜索设备成员
    public static final String URL_GETROOTNODES = ApiConstants.CLOUDM_YJX_HOST
            + "member/getRootNodes";// 获取角色岗位名称
    public static final String URL_UPDATEDEVICEINFOBYKEY = ApiConstants.CLOUDM_YJX_HOST
            + "device/updateDeviceInfoByKey";// 获取角色岗位名称
    public static final String URL_GETPERMISSIONS = ApiConstants.CLOUDM_YJX_HOST
            + "member/getPermissions";// 获取权限列表
    public static final String URL_GETMACHINETYPES = ApiConstants.CLOUDM_YJX_HOST
            + "device/getMachineTypes";// 获取机器种类列表
    public static final String URL_GETMACHINEBRAND = ApiConstants.CLOUDM_YJX_HOST
            + "device/getMachineBrand";// 获取机器品牌列表
    public static final String URL_GETMACHINEMODEL = ApiConstants.CLOUDM_YJX_HOST
            + "device/getMachineModel";// 获取机器型号列表
    public static final String URL_UPDATEDEVICEINFO = ApiConstants.CLOUDM_YJX_HOST
            + "device/updateDeviceInfo";// 获取机器型号列表
    public static final String URL_GETVERSION = ApiConstants.CLOUDM_YJX_HOST
            + "version/getVersion";// 软件更新
    public static final String URL_DAILYWORKDITAILS = ApiConstants.CLOUDM_YJX_HOST
            + "device/dailyWorkDitails";// 当日工作区间分布
    public static final String URL_SAVEVBUSINESS = ApiConstants.CLOUDM_YJX_HOST +    //上传新增维修
            "device/saveVbusiness";
    public static final String URL_SAVEEVALUATE = ApiConstants.CLOUDM_YJX_HOST
            + "device/saveEvaluate";
    public static final String URL_SAVEEVALUATE_ALLIANCE=ApiConstants.CLOUDM_YJX_HOST+"device/saveAllianceEvaluate";
    public static final String URL_ADDCIRCLEFENCH = ApiConstants.CLOUDM_YJX_HOST
            + "device/addCricleFence";
    public static final String URL_CWPAY = ApiConstants.CLOUDM_YJX_HOST + "pay/getPaySign";
    public static final String P_DEVICEID = "DeviceId";
    public static final String P_DEVICETYPE = "DeviceType";
    public static final String P_DEVICENAME = "DeviceName";
    public static final String P_OILLAVE = "oillave";
    public static final String P_MCDEVICEBASICSINFO = "mcDeviceBasicsInfo";
    public static final String P_ADDMCDEVICETYPE = "addDeviceType";
    public static final String P_SEARCHLISTTYPE = "searchListType";
    public static final String P_TITLETEXT = "titleText";
    public static final String P_TITLENAME = "titleName";
    public static final String P_EDITTYPE = "editType";
    public static final String P_ITEMTYPE = "itemType";
    public static final String P_TYPEID = "typeid";
    public static final String P_BRANDID = "brandid";
    public static final String P_MODELID = "modelid";
    public static final String P_PAYTYPE="payType";
    public static final String P_PAYAMOUNT="payAmount";
    public static final String P_RECEIVERLIST="receiverList";
    public static final String P_EDITRESULTSTRING = "editResultString";
    public static final String P_EDITRESULTITEM = "editResultItem";
    public static final String P_IMAGEBROWERDELETE = "imageBrowerDelete";
    public static final String P_EDIT_LIST_VALUE1 = "editListValue1";
    public static final String P_EDIT_LIST_VALUE2 = "editListValue2";
    public static final String P_EDIT_LIST_VALUE3 = "editListValue3";
    public static final String P_EDIT_LIST_ITEM_NAME = "editlistItemName";

    public static final String P_DEVICEINFO_deviceName = "deviceName";
    public static final String P_DEVICEINFO_category = "category";
    public static final String P_DEVICEINFO_brand = "brand";
    public static final String P_DEVICEINFO_model = "model";
    public static final String P_DEVICEINFO_typeId = "typeId";
    public static final String P_DEVICEINFO_brandId = "brandId";
    public static final String P_DEVICEINFO_modelId = "modelId";
    public static final String P_CITYNAME = "city_name";
    public static final String P_SEARCHINFO = "searchInfo";
    public static final String S_FG = ":&&:";
    public static final String S_DEVICE_LOCATION_NO = "暂无";
    public static final String S_TIME_FG = "-";
    public static final String S_UPDATEDEVICEKEY_FG = ",";

    public static final int E_DEVICE_TEXT = 10;
    public static final int E_DEVICE_DATA = E_DEVICE_TEXT + 1;
    public static final int E_DEVICE_LIST = E_DEVICE_DATA + 1;

    public static final int E_ITEMS_deviceName = 50;// 设备名称
    public static final int E_ITEMS_category = E_ITEMS_deviceName + 1;// 品类
    public static final int E_ITEMS_brand = E_ITEMS_category + 1;// 品牌
    public static final int E_ITEMS_model = E_ITEMS_brand + 1;// 型号
    public static final int E_ITEMS_factoryTime = E_ITEMS_model + 1;// 新机出厂时间
    public static final int E_ITEMS_deviceType = E_ITEMS_factoryTime + 1;// 机器类型
    // 1：新机
    // 2：二手
    public static final int E_ITEMS_buyTime = E_ITEMS_deviceType + 1;// 购机时间
    public static final int E_ITEMS_buyPlace = E_ITEMS_buyTime + 1;// 购机所在地区
    public static final int E_ITEMS_buyPrice = E_ITEMS_buyPlace + 1;// 购机价格
    public static final int E_ITEMS_sellerName = E_ITEMS_buyPrice + 1;// 卖家名称
    public static final int E_ITEMS_sellerPlace = E_ITEMS_sellerName + 1;// 卖家地址
    public static final int E_ITEMS_sellerContacts = E_ITEMS_sellerPlace + 1;// 卖家联系人姓名
    public static final int E_ITEMS_sellerMobi = E_ITEMS_sellerContacts + 1;// 卖家联系人手机
    public static final int E_ITEMS_sellerEmail = E_ITEMS_sellerMobi + 1;// 卖家联系人邮箱
    public static final int E_ITEMS_insurer = E_ITEMS_sellerEmail + 1;// 保险公司
    public static final int E_ITEMS_insurerNo = E_ITEMS_insurer + 1;// 保险单号
    public static final int E_ITEMS_company = E_ITEMS_insurerNo + 1;// 融资银行或公司
    public static final int E_ITEMS_contractNo = E_ITEMS_company + 1;// 融资协议合同号
    public static final int E_ITEMS_serviceName = E_ITEMS_contractNo + 1;// 维保单位名称
    public static final int E_ITEMS_serviceMobi = E_ITEMS_serviceName + 1;// 维保单位联系电话
    public static final int E_ITEMS_servicePlace = E_ITEMS_serviceMobi + 1;// 维保单位地址
    public static final int E_ITEMS_oldId = E_ITEMS_servicePlace + 1;// 原来的设备ID号
    // 特指该机器为二手
    public static final int E_ITEMS_isDelete = E_ITEMS_oldId + 1;// 逻辑有效 1：有效
    // 0：无效
    public static final int E_ITEMS_checkType = E_ITEMS_isDelete + 1;// 审核状态
    // 1：审核中
    // 2：审核通过
    // 3：审核拒绝
    public static final int E_ITEMS_deviceId = E_ITEMS_checkType + 1;// 设备基础信息有效表id
    public static final int E_ITEMS_type = E_ITEMS_deviceId + 1;// 待审核数据的类型
    // 1:新增设备
    // 2：修改设备信息 3：过户
    // 4：删除
    public static final int E_ITEMS_idCard = E_ITEMS_type + 1;// 身份证号
    public static final int E_ITEMS_filtNumber = E_ITEMS_idCard + 1;// 网关编号

    public static final int E_ITEMS_role_remark = E_ITEMS_filtNumber + 1;// 成员备注
//    public static final int E_ITEMS_role_list = E_ITEMS_role_remark + 1;// 成员备注

    public static final int REQUEST_EditActivity = 0;
    public static final int REQUEST_ImageActivity = 100;
    public static final int REQUEST_BLOGCOMMENT = REQUEST_ImageActivity + 1;
    public static final int REQUEST_ToSearchActivity = REQUEST_BLOGCOMMENT + 1;
    public static final int REQUEST_ToSearchDeviceActivity = REQUEST_ToSearchActivity + 1;
    public static final int MC_Simulation_DeviceId = 0;
    public static final int MC_DevicesList_AllType = 15;

    public static final String KEY_isHomeGuide = "isHomeGuide"; // 是否首页引导过
    public static final String KEY_DownloadId = "DownloadID"; // 下载的ID
    public static final String KEY_NewVersion = "NewVersion"; // 最新版本
    public static final String KEY_NewMessageSize = "NewMessageSize"; // 新邀请消息条数

    public static final int KEY_ImageUpload_Kinds_m1 = 2; // 机博上传照片
    public static final int CLICK_POSITION = REQUEST_BLOGCOMMENT;
    public static final java.lang.String APP_ID = "wxfb6afbcc23f867df";

    /**
     * The constant DEBUG_TAG.
     */
    public static final String DEBUG_TAG = "logger";// LogCat的标记

    public static boolean isChangeDevice;
    public static boolean isMcLogin;
    public static boolean isGetScore;

    public static BadgeView badgeMessage;

    public static Class<?> photoParent;
    public static Bitmap photoBimap;
    public static CustomDialog alertDialog;
    public static final int CODE_license = 10;
    public static final int CODE_snNum = CODE_license + 1;

    public static final String DateFormat1 = "yyyy-MM-dd";
    public static final String DateFormat2 = "yyyy-MM-dd HH:mm:ss";
    public static final String DateFormat3 = "HH:mm";

    public static final int[] MESSAGETYPE = {0, 1, 2, 3, 4, 5};

    public static List<Activity> activityList = new ArrayList<Activity>();

    public static final void addActivity(Activity ac) {
        activityList.add(ac);
    }

    public static final void removeActivity(Activity ac) {
        activityList.remove(ac);
    }

    public static Activity getLastActivity() {
        Activity ac = null;
        try {
            int size = activityList.size();
            if (size > 0)
                ac = activityList.get(size - 1);
        } catch (Exception e) {

        }
        return ac;
    }

    public static final void exit() {
        try {
            for (Activity activity : activityList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // System.exit(0);
        }
    }


    public static final DisplayImageOptions displayMcImageOptions = new DisplayImageOptions.Builder()
            .showImageOnFail(R.drawable.mc_default_icon)
            .showImageForEmptyUri(R.drawable.mc_default_icon)
            .showImageOnFail(R.drawable.mc_default_icon).cacheInMemory(true)
            .cacheOnDisc(true).displayer(new RoundedBitmapDisplayer(5)).build();
    public static DisplayImageOptions displayDeviceImageOptions = new DisplayImageOptions.Builder()
            .showImageOnFail(R.drawable.mc_default_icon)
            .showImageForEmptyUri(R.drawable.mc_default_icon)
            .showImageOnFail(R.drawable.mc_default_icon).cacheInMemory(true)
            .cacheOnDisc(true).displayer(new RoundedBitmapDisplayer(10))
            .build();

    public static DisplayImageOptions displayListImageOptions = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.mc_default_icon) // 设置图片在下载期间显示的图片
            .showImageForEmptyUri(R.drawable.mc_default_icon)// 设置图片Uri为空或是错误的时候显示的图片
            .showImageOnFail(R.drawable.mc_default_icon) // 设置图片加载/解码过程中错误时候显示的图片
            .cacheInMemory(true)// 设置下载的图片是否缓存在内存中
            .cacheOnDisc(true)// 设置下载的图片是否缓存在SD卡中
            .imageScaleType(ImageScaleType.IN_SAMPLE_INT)// 设置图片以如何的编码方式显示
            .considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型
            .build();// 构建完成

    public static ImageLoaderConfiguration getConfiguration(Context context) {
        File cacheDir = StorageUtils.getOwnCacheDirectory(context,
                "imageloader/Cache");
        return new ImageLoaderConfiguration.Builder(context)
                .memoryCacheExtraOptions(480, 800)
                // max width, max height，即保存的每个缓存文件的最大长宽
                .discCacheExtraOptions(480, 800, CompressFormat.JPEG, 75, null)
                // Can slow ImageLoader, use it carefully (Better don't use
                // it)/设置缓存的详细信息，最好不要设置这个
                .threadPoolSize(3)
                // 线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
                // You can pass your own memory cache
                // implementation/你可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024)
                .discCacheSize(50 * 1024 * 1024)
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                // 将保存的时候的URI名称用MD5 加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .discCacheFileCount(100)
                // 缓存的文件数量
                .discCache(new UnlimitedDiscCache(cacheDir))
                // 自定义缓存路径
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(
                        new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout
                // (5
                // s),
                // readTimeout
                // (30
                // s)超时时间
                .writeDebugLogs() // Remove for release app
                .build();// 开始构建
    }

    public static final String IMAGE_PATH = Environment
            .getExternalStorageDirectory().toString()
            + File.separator
            + "cloudmachine" + File.separator + "images" + File.separator;

    public static final String SYS_IMG_PATH=Environment
            .getExternalStorageDirectory().toString()+File.separator+"DCIM"+File.separator;

    public static void ToastAction(String msg) {
        if (!TextUtils.isEmpty(msg))
            Toast.makeText(MyApplication.mContext, msg, Toast.LENGTH_LONG)
                    .show();
    }

    public static void MyToast(String msg) {
        if (null != MyApplication.mContext && null != msg) {
            Toast.makeText(MyApplication.mContext, msg, Toast.LENGTH_LONG)
                    .show();
        }
    }

    public static void MyToast(String msg, String def) {
        if (!TextUtils.isEmpty(msg))
            Toast.makeText(MyApplication.mContext, msg, Toast.LENGTH_LONG)
                    .show();
        else
            Toast.makeText(MyApplication.mContext, def, Toast.LENGTH_LONG)
                    .show();
    }

    public static String float2String(float f) {
        /*
         * String s = String.valueOf(f); if(s.lastIndexOf("0") !=-1){ s =
		 * s.substring(0, s.length()-1); if(s.lastIndexOf(".") !=-1){ s =
		 * s.substring(0, s.length()-1); } } return s;
		 */
        BigDecimal b = new BigDecimal(f);
        return float2String(b.setScale(2, BigDecimal.ROUND_HALF_UP)
                .floatValue(), 2);
    }

    public static String float2String(float f, int pSize) {
        /*
         * String zStr ="0"; String fStr = "."; String s = String.valueOf(f);
		 * for(int i=0; i<pSize; i++){ fStr += zStr; } if(fStr.length()>2){
		 * DecimalFormat decimalFormat=new
		 * DecimalFormat(fStr);//构造方法的字符格式这里如果小数不足2位,会以0补足.
		 * s=decimalFormat.format(f);//format 返回的是字符串 }
		 */
        String s = String.valueOf(f);
        while (s.lastIndexOf("0") == s.length() - 1) {
            s = s.substring(0, s.length() - 1);
        }
        while (s.lastIndexOf(".") == s.length() - 1) {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }

    public static String toUtf(String str) {
        try {
            str = URLDecoder.decode(str, "UTF-8");
        } catch (Exception e) {
        }
        return str;
    }

    public static final String getNowData() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }

    public static final String getNowTimeAll() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");// yyyy-MM-dd
        return simpleDateFormat.format(date);
    }

    public static String getDateBefore(int day) {
        Date d = new Date();
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
        return formatMonth(now.get(Calendar.MONTH))
                + now.get(Calendar.DAY_OF_MONTH) + "日" + " "
                + formatWeek(now.get(Calendar.DAY_OF_WEEK));
        // Date outDate = now.getTime();
        // SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");//
        // 可以方便地修改日期格式
        // String hehe = dateFormat.format(outDate);
        // return hehe;
    }

    public static String getDateBefore2(int day) {
        Date d = new Date();
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
        Date outDate = now.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");// 可以方便地修改日期格式
        String hehe = dateFormat.format(outDate);
        return hehe;
    }

    public static final String formatWeek(int week) {
        switch (week) {
            case Calendar.SUNDAY:
                return "周日";
            case Calendar.MONDAY:
                return "周一";
            case Calendar.TUESDAY:
                return "周二";
            case Calendar.WEDNESDAY:
                return "周三";
            case Calendar.THURSDAY:
                return "周四";
            case Calendar.FRIDAY:
                return "周五";
            case Calendar.SATURDAY:
                return "周六";
            default:
                return "";
        }
    }

    public static final String formatMonth(int month) {
        switch (month) {
            case Calendar.JANUARY:
                return "1月";
            case Calendar.FEBRUARY:
                return "2月";
            case Calendar.MARCH:
                return "3月";
            case Calendar.APRIL:
                return "4月";
            case Calendar.MAY:
                return "5月";
            case Calendar.JUNE:
                return "6月";
            case Calendar.JULY:
                return "7月";
            case Calendar.AUGUST:
                return "8月";
            case Calendar.SEPTEMBER:
                return "9月";
            case Calendar.OCTOBER:
                return "10月";
            case Calendar.NOVEMBER:
                return "11月";
            case Calendar.DECEMBER:
                return "12月";
            default:
                return "";
        }
    }

    public static final int getDateDays(String date1, String date2,
                                        String format) {
        long betweenTime = 0;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            Date date = sdf.parse(date1);// 通过日期格式的parse()方法将字符串转换成日期
            Date dateBegin = sdf.parse(date2);
            betweenTime = date.getTime() - dateBegin.getTime();
            betweenTime = betweenTime / 1000 / 60 / 60 / 24;
        } catch (Exception e) {
            return -1;

        }
        return (int) betweenTime;
    }

    public static final int getDateDays(Date date, Date dateBegin) {
        long betweenTime = 0;
        // SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            // Date date = sdf.parse(date1);// 通过日期格式的parse()方法将字符串转换成日期
            // Date dateBegin = sdf.parse(date2);
            betweenTime = date.getTime() - dateBegin.getTime();
            betweenTime = betweenTime / 1000 / 60 / 60 / 24;
        } catch (Exception e) {
        }
        return (int) betweenTime;
    }

    public static final String changeDateFormat(String time, String oldStr,
                                                String newStr) {
        SimpleDateFormat oldsdf = new SimpleDateFormat(oldStr);
        SimpleDateFormat newsdf = new SimpleDateFormat(newStr);
        Date d = null;
        try {
            d = oldsdf.parse(time); // 将给定的字符串中的日期提取出来
        } catch (Exception e) { // 如果提供的字符串格式有错误，则进行异常处理
            e.printStackTrace(); // 打印异常信息
            return time;
        }
        return newsdf.format(d);
    }

    public static final String changeDateFormat2(String time, String oldStr,
                                                 String newStr, int[] b, String[] str) {
        if (null != b && null != str && b.length == str.length) {
            try {
                int len = b.length;
                int btween = Constants.getDateDays(Calendar.getInstance()
                        .getTime(), new SimpleDateFormat(oldStr).parse(time));
                for (int i = 0; i < len; i++) {
                    if (b[i] == btween) {
                        return str[i];
                    }
                }
            } catch (Exception e) {
            }
        }
        SimpleDateFormat oldsdf = new SimpleDateFormat(oldStr);
        SimpleDateFormat newsdf = new SimpleDateFormat(newStr);
        Date d = null;
        try {
            d = oldsdf.parse(time); // 将给定的字符串中的日期提取出来
        } catch (Exception e) { // 如果提供的字符串格式有错误，则进行异常处理
            e.printStackTrace(); // 打印异常信息
            return time;
        }
        return newsdf.format(d);
    }

    public static final long getDatetolong(String date, String format) {
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(new SimpleDateFormat(format).parse(date));
            return c.getTimeInMillis();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

    public static final String getTimetoString(String date, int[] b,
                                               String[] str, String format) {
        if (null != b) {

        }
        return date;

    }

    public static final double[] arrayCopyDouble(double[] a, double b) {
        double[] rdb;
        if (a != null && a.length > 0) {
            rdb = new double[a.length + 1];
            System.arraycopy(a, 0, rdb, 0, a.length);
            rdb[a.length] = b;
        } else {
            rdb = new double[1];
            rdb[0] = b;
        }
        return rdb;
    }

    public static final String[] arrayCopyString(String[] a, String b) {
        String[] rdb;
        if (a != null && a.length > 0) {
            rdb = new String[a.length + 1];
            System.arraycopy(a, 0, rdb, 0, a.length);
            rdb[a.length] = b;
        } else {
            rdb = new String[1];
            rdb[0] = b;
        }
        return rdb;
    }

    public static final String[] arrayDeleteSring(String[] a, int index) {
        String[] rdb = a;
        if (a != null && a.length > index) {
            int len = a.length;
            String[] rdb1 = new String[index];
            String[] rdb2 = new String[len - index - 1];
            System.arraycopy(a, 0, rdb1, 0, rdb1.length);
            System.arraycopy(a, index + 1, rdb2, 0, rdb2.length);
            rdb = new String[len - 1];
            System.arraycopy(rdb1, 0, rdb, 0, rdb1.length);
            System.arraycopy(rdb2, 0, rdb, rdb1.length, rdb2.length);
        }
        return rdb;
    }

    public static final void toActivity(Activity activity, Class cl,
                                        Bundle bundle) {
        Intent intent = new Intent(activity, cl);
        if (null != bundle)
            intent.putExtras(bundle);
        activity.startActivity(intent);
//        activity.overridePendingTransition(R.anim.slide_right_in,
//                R.anim.slide_left_out);
    }

    public static final void toActivity(Activity activity, Class cl,
                                        Bundle bundle, boolean finish) {
        Intent intent = new Intent(activity, cl);
        if (null != bundle)
            intent.putExtras(bundle);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_right_in,
                R.anim.slide_left_out);
        if (finish) {
            activity.finish();
        }
    }

    public static final void toActivityForR(Activity activity, Class cl,
                                            Bundle bundle) {
        Intent intent = new Intent(activity, cl);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        activity.startActivityForResult(intent, 0);
        activity.overridePendingTransition(R.anim.slide_right_in,
                R.anim.slide_left_out);
    }

    public static final void toActivityForR(Activity activity, Class cl,
                                            Bundle bundle, int requestCode) {
        Intent intent = new Intent(activity, cl);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        activity.startActivityForResult(intent, requestCode);
        activity.overridePendingTransition(R.anim.slide_right_in,
                R.anim.slide_left_out);
    }

    public static final void toLoginActivity(Activity activity, int flag) {
        Bundle bundle = new Bundle();
        bundle.putInt("flag", flag);
        Constants.toActivity(activity, LoginActivity.class, bundle);
    }

    public static final String savePhotoToSDCard(Bitmap bitmap) {
        if (!FileUtils.isSdcardExist()) {
            return null;
        }
        FileOutputStream fileOutputStream = null;
        FileUtils.createDirFile(IMAGE_PATH);

        String fileName = UUID.randomUUID().toString() + ".jpg";
        String newFilePath = IMAGE_PATH + fileName;
        File file = FileUtils.createNewFile(newFilePath);
        if (file == null) {
            return null;
        }
        try {
            fileOutputStream = new FileOutputStream(newFilePath);
            bitmap.compress(CompressFormat.JPEG, 100, fileOutputStream);
        } catch (FileNotFoundException e1) {
            return null;
        } finally {
            try {
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (IOException e) {
                return null;
            }
        }
        return newFilePath;
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public static final void startPhotoZoom(Activity activity, Uri uri, int code) {
        if (uri == null) {
            Log.i("tag", "The uri is not exist.");
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        intent.putExtra("return-data", true);
        activity.startActivityForResult(intent, code);
    }

    public static final void setImageToView(Intent data, Handler handler) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            String imString = savePhotoToSDCard(photo);
            new ImageUploadAsync(handler, imString).execute();
        }
    }

    public static final void gotoImageBrower(Activity activity, int position,
                                             String[] urls, boolean isDelete) {
        int len = urls.length;
        ArrayList<ImageItem> dataList = new ArrayList<ImageItem>();
        for (int i = 0; i < len; i++) {
            ImageItem item = new ImageItem();
            item.setImageUrl(urls[i]);
            dataList.add(item);
        }

        Intent intent = new Intent(activity, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_ITEM, dataList);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        intent.putExtra(Constants.P_IMAGEBROWERDELETE, isDelete);
        activity.startActivityForResult(intent, Constants.REQUEST_ImageActivity);
        // activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.zoomin, R.anim.zoomno);
    }

    public static final void gotoImageBrowerType(Activity activity,
                                                 int position, String[] urls, boolean isDelete, int type) {
        int len = urls.length;
        ArrayList<ImageItem> dataList = new ArrayList<ImageItem>();
        for (int i = 0; i < len; i++) {
            ImageItem item = new ImageItem();
            item.setImageUrl(urls[i]);
            dataList.add(item);
        }
        Intent intent = new Intent(activity, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_ITEM, dataList);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_TYPE, type);
        intent.putExtra(Constants.P_IMAGEBROWERDELETE, isDelete);
        activity.startActivityForResult(intent, Constants.REQUEST_ImageActivity);
        // activity.startActivity(intent);
        switch (type) {
            case 2:
                activity.overridePendingTransition(R.anim.zoomin_2, R.anim.zoomno);
                break;
            default:
                activity.overridePendingTransition(R.anim.zoomin, R.anim.zoomno);
                break;
        }

    }

    public static final void SimulationNoChangeMessage() {
        Constants.MyToast(MyApplication.mContext.getResources().getString(
                R.string.simulation_no_change_message));
    }

    public static final String stringArray2string(String[] str) {
        String rStr = "";
        int len = str.length;
        for (int i = 0; i < len; i++) {
            rStr += "," + str[i];
        }
        if (rStr.length() > 0) {
            rStr = rStr.substring(1);
        }
        return rStr;
    }

    public static final void setObjcet2File(Object data, String key) {
        try {
            FileOutputStream stream = MyApplication.mContext.openFileOutput(
                    key, MyApplication.mContext.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(stream);
            oos.writeObject(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static final Object getObjcet2File(String key) {
        Object ob = null;
        try {
            FileInputStream stream = MyApplication.mContext.openFileInput(key);
            ObjectInputStream ois = new ObjectInputStream(stream);
            ob = ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ob;
    }

    public static final void getPermissionsList(Context mContext,
                                                Handler mHandler) {
        new PermissionsListAsync(mContext, mHandler).execute();
    }

    public static final boolean isNoEditInAdd(int addDeviceType,
                                              McDeviceBasicsInfo mcDeviceBasicsInfo) {
        boolean rb = false;
        if (addDeviceType != 1
                && null != mcDeviceBasicsInfo
                && (mcDeviceBasicsInfo.getId() == Constants.MC_Simulation_DeviceId || mcDeviceBasicsInfo
                .getType() == 4)) {
            rb = true;
        } else {
            rb = false;
        }
        return rb;
    }

    public static final boolean isNoEditInMcMember(long deviceId, int deviceType) {
        boolean rb;
        if (deviceId != MC_Simulation_DeviceId && deviceType == 1) {
            rb = false;
        } else {
            rb = true;
        }
        return rb;
    }

    public static final boolean isNoEditInMc(long deviceId) {
        boolean rb = false;
        if (deviceId == Constants.MC_Simulation_DeviceId) {
            rb = true;
        } else {
            rb = false;
        }
        return rb;
    }

    public static final void updateVersion(final Context context,
                                           final Handler handler, int mustUpdate, String message, final String link) {
        if (mustUpdate == 0 || mustUpdate == 1) {
            message = message.replace("|", "\n");
            if (null == alertDialog) {
                showDialog(context, handler, mustUpdate, message, link);
            } else {
                if (!alertDialog.isShowing()) {
                    alertDialog.dismiss();
                    alertDialog.cancel();
                    alertDialog = null;
                    showDialog(context, handler, mustUpdate, message, link);
                }
            }
        }
    }

    private static final void showDialog(final Context context,
                                         final Handler handler, int mustUpdate, String message, final String link) {

        CustomDialog.Builder builder = new CustomDialog.Builder(context);
        builder.setAlertIcon(R.drawable.icon_update);
        builder.setGravityLeft(true);
        builder.setMessage(message);
        if (mustUpdate == 1 && context instanceof HomeActivity) {
//            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                    ((Activity) context).finish();
//                    MobclickAgent.onKillProcess(context);
//                    System.exit(0);
//                }
//            });
            builder.setNeutralButton("好的", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Message msg = Message.obtain();
                    msg.what = Constants.HANDLER_VERSIONDOWNLOAD;
                    msg.obj = link;
                    handler.sendMessage(msg);
                }
            });
        } else {
            builder.setNegativeButton(context.getResources().getColor(R.color.cor10), "稍后再说", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (context instanceof BaseAutoLayoutActivity) {
                        ((BaseAutoLayoutActivity) context).mRxManager.post(HomeActivity.RXEVENT_UPDATE_REMIND, null);
                    }
                    dialog.dismiss();

                }
            });
            builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Message msg = Message.obtain();
                    msg.what = Constants.HANDLER_VERSIONDOWNLOAD;
                    msg.obj = link;
                    handler.sendMessage(msg);
                }
            });
        }
        alertDialog = builder.create();
        if (mustUpdate == 1) {
            alertDialog.setCancelable(false);
        }
        alertDialog.show();
//        alertDialog = new CustomDialog(context, R.style.MyDialog,

//                new MyDialog.LeaveMyDialogListener() {
//                    @Override
//                    public void onClick(View view) {
//                        switch (view.getId()) {
//                            case R.id.negative_button:
//                                Message msg = Message.obtain();
//                                msg.what = Constants.HANDLER_VERSIONDOWNLOAD;
//                                msg.obj = link;
//                                handler.sendMessage(msg);
//                                break;
//                            case R.id.positive_button:
//
//                                break;
//                            default:
//                                break;
//                        }
//                    }
//                });
//        alertDialog.setText(message);
//        alertDialog.setNegativeText("确定");
//        alertDialog.setPositiveText("稍后");
//        alertDialog.show();

		/*alertDialog = new AlertDialog.Builder(context).setTitle("版本更新")
                .setMessage(message)
				// .setIcon(R.drawable.ic_launcher)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						Message msg = Message.obtain();
						msg.what = Constants.HANDLER_VERSIONDOWNLOAD;
						msg.obj = link;
						handler.sendMessage(msg);
						//

					}
				})
				.setNegativeButton("稍后", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).create();
		alertDialog.show();*/
    }

    public final static void versionDownload(Context context, String link) {
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(link);
                intent.setData(content_url);
                intent.setClassName("com.android.browser",
                        "com.android.browser.BrowserActivity");
                context.startActivity(intent);
            } else {
                clearApk();
                DownloadManager downloadManager = (DownloadManager) context
                        .getSystemService(context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(link);
                Request request = new Request(uri);
                request.setAllowedOverRoaming(false);
                //通知栏显示
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                // 设置允许使用的网络类型，这里是移动网络和wifi都可以
                request.setAllowedNetworkTypes(Request.NETWORK_MOBILE
                        | Request.NETWORK_WIFI);
                // request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                // 禁止发出通知，既后台下载，如果要使用这一句必须声明一个权限：android.permission.DOWNLOAD_WITHOUT_NOTIFICATION
                // request.setShowRunningNotification(false);
                // 不显示下载界面
                request.setTitle("云机械");
                request.setDescription("正在下载中...");
                request.setVisibleInDownloadsUi(true);
                //设置下载的路径
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, Constants.APK_NAME);
                //设置下载的路径
                // 设置下载后文件存放的位置,如果sdcard不可用，那么设置这个将报错，因此最好不设置如果sdcard可用，下载后的文件
                // 在/mnt/sdcard/Android/data/packageName/files目录下面，如果sdcard不可用,设置了下面这个将报错，不设置，下载后的文件在/cache这个
                // 目录下面
                // request.setDestinationInExternalFilesDir(this, null,
                // "tar.apk");
                long id = downloadManager.enqueue(request);
                // TODO 把id保存好，在接收者里面要用，最好保存在Preferences里面
                MySharedPreferences
                        .setSharedPLong(Constants.KEY_DownloadId, id);
            }
        } catch (Exception e) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(link);
            intent.setData(content_url);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            context.startActivity(intent);
        }

    }

    private static void clearApk() {
        File downloadDic = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if (downloadDic.exists()) {
            for (File f : downloadDic.listFiles()) {
                if (f.getName().contains("cloudm")) {
                    f.delete();
                }
            }
        }
    }

    public final static int getSensorType(String sensorNum) {
        try {
            return Integer.valueOf(sensorNum.substring(2, 4));
        } catch (Exception e) {
            return -1;
        }

    }

    public final static String getSensorName(String sensorNum) {
        switch (getSensorType(sensorNum)) {
            // 节点类型：0x00=温度 0x01=液位 0x02=压力 0x03=振动
            case 0:
                return "温度";
            case 1:
                return "液位";
            case 2:
                return "压力";
            case 3:
                return "振动";
        }
        return "未知";
    }

    public final static boolean initSensor(String sensorNum) {
        String h = sensorNum.substring(0, 2);
        if (!h.equals("5A")) {
            return false;
        }
        switch (getSensorType(sensorNum)) {
            case 0:
            case 1:
            case 2:
            case 3:
                return true;
            default:
                return false;
        }

    }

    public final static boolean isNewMessage(int type) {
        if (type == 1) {
            if (MySharedPreferences.getSharedPInt(Constants.KEY_NewMessageSize) > 0) {
                return true;
            }
        } else if (type == 2) {
            if (!MySharedPreferences.getSharedPString(Constants.KEY_NewVersion,
                    VersionU.getVersionName())
                    .equals(VersionU.getVersionName())) {
                return true;
            }
        } else {// 0：消息和更新 1：消息 2：更新
            if (MySharedPreferences.getSharedPInt(Constants.KEY_NewMessageSize) > 0) {
                return true;
            }
            // if(!MySharedPreferences.getSharedPString(Constants.KEY_NewVersion,VersionU.getVersionName()).equals(VersionU.getVersionName())){
            // return true;
            // }
        }

        return false;
    }

    public final static void showTips(View tips, int type) {
        if (Constants.isNewMessage(type)) {
            tips.setVisibility(View.VISIBLE);
        } else {
            tips.setVisibility(View.GONE);
        }
    }

    public final static void showTipsNum(View view, int num) {
        badgeMessage = new BadgeView(
                MyApplication.mContext, view);
        badgeMessage.setText(String.valueOf(num));
        // badgeMessage.setBadgeMargin(1, 10);
        // badgeMessage.setBackgroundResource(R.drawable.tips_new);
        badgeMessage.setTextSize(8);
        if (num > 0)
            badgeMessage.show();
        else
            badgeMessage.hide();
    }

    public static final void gotoImageBrower(Activity activity,
                                             String position, int id) {
        // position 1:选中的预览 2:所有预览

        Intent intent = new Intent(activity, GalleryActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("ID", id);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.zoomin, R.anim.zoomno);
    }

    public static final void gotoImageBrower(Activity activity, int position,
                                             ArrayList<ImageItem> dataList, boolean isDelete) {
        try {
            Intent intent = new Intent(activity, ImagePagerActivity.class);
            // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
            intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_ITEM, dataList);
            intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
            intent.putExtra(Constants.P_IMAGEBROWERDELETE, isDelete);
            activity.startActivityForResult(intent,
                    Constants.REQUEST_ImageActivity);
            // activity.startActivity(intent);
            activity.overridePendingTransition(R.anim.zoomin, R.anim.zoomno);
        } catch (Exception e) {
        }

    }

    public static final void gotoImageBrowerType(Activity activity,
                                                 int position, ArrayList<ImageItem> dataList, boolean isDelete,
                                                 int type) {
        try {
            Intent intent = new Intent(activity, ImagePagerActivity.class);
            // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
            intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_ITEM, dataList);
            intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
            intent.putExtra(Constants.P_IMAGEBROWERDELETE, isDelete);
            activity.startActivityForResult(intent,
                    Constants.REQUEST_ImageActivity);
            // activity.startActivity(intent);
            switch (type) {
                case 2:
                    activity.overridePendingTransition(R.anim.zoomin_2,
                            R.anim.zoomno);
                    break;
                default:
                    activity.overridePendingTransition(R.anim.zoomin, R.anim.zoomno);
                    break;
            }
        } catch (Exception e) {
        }

    }

    public static final boolean checkIdCard(String idNum) {
        // 定义判别用户身份证号的正则表达式（要么是15位，要么是18位，最后一位可以为字母）
        Pattern idNumPattern = Pattern
                .compile("(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])");
        // 通过Pattern获得Matcher
        Matcher idNumMatcher = idNumPattern.matcher(idNum);
        // 判断用户输入是否为身份证号
        return idNumMatcher.matches();
    }

    public static final boolean checkEmail(String email) {
        String str = "^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public static final boolean isMobileNO(String mobiles) {

		/*
         * Pattern p = Pattern
		 * .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$"); Matcher m =
		 * p.matcher(mobiles); return m.matches();
		 */
        String telRegex = "[1]\\d{10}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);

    }

    public static final String getNotNullString(String str) {
        return null != str ? str : "";
    }

    public static final void TEL(Activity activity, String tel) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    public static final String toViewString(String str) {
        return null != str ? str : "";
    }

    public static final String toViewString(String str, String dev) {
        if (TextUtils.isEmpty(dev))
            return null != str ? str : "";
        return null != str ? str : dev;
    }

    public static final String FLAG = "flag";
    public static final String CWINFO = "cwinfo";
    public static final String WORK_DETAIL = "work_detail";
    public static final String LOGO_LIST = "logo_list";
    public static final String CUSTOMER_PHONE_BOX = "400-008-0581";
    public static final String CUSTOMER_PHONE_REPAIR = "400-816-9911";
    public static final String H5_URL = "H5_url";
    public static final String MC_DEVICEINFO = "McDeviceInfo";
    public static final String MC_DEVICEID = "McDeviceId";
    public static final String MC_MEMBER = "Member";
    public static final String MC_LOC_NOW = "McLocNow";
    public static final String MEMBER_ID = "memberid";
    public static final int RESULT_QUERY_BY_TIME = 0x34;
    public static final int ANIMATION_DURACTION = 200;
    public static final int MACHINE_ICON_WIDTH = 25;
    public static final int MACHINE_ICON_HEIGHT = 18;
    public static final int INVALID_DEVICE_ID = -1;
    public static final String UPDATE_DEVICE_LIST = "updateDeviceList";
    public static final String UPDATE_DEVICE_NAME = "updateDeviceName";
    public static final String DEVICE_ID = "device_id";
    public static final String DEVICE_DETAIL_NOW = "DEVICE_DETAIL_NOW";
    public static final String PAGET_TYPE = "page_type";
    public static final String SN_ID = "snid";
    public static final String VIDEO_ID="video_id";
    public static final String IS_ONLINE ="is_online";
    public static final String IS_VIDEO ="is_video";
    public static final String IMEI="imei";
    public static final String ERROR_MESSAGE="error_message";
    public static final String OPERATOR_LIST="operator_list";
    public static final String NAME="name";
    public static final String MOBILE="mobie";
    public static final String RELATION="relation";
    public static final String RELATION_POSITION="relation_position";

    public static void callJsMethod(WebView webView, String jsParams) {
        AppLog.print("call jsMethod__"+jsParams);
        webView.loadUrl("javascript:" + jsParams);
    }

    public static final String CURRENT_LOC = "当前位置";
    public static final String APK_NAME = "cloudm.apk";

    public  interface IPageType {
        String PAGE_DEVICE_INFO = "p_device_info";//设备基本信息
        String PAGE_INFO_MANAGER="p_info_manger";

    }

    public interface PermissionType {
        int CAMERA = 1;
        int STORAGE = 2;
        int LOCATION = 3;
    }
    public static final String[] PERMISSIONS_CAMER_SD={Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};


}
