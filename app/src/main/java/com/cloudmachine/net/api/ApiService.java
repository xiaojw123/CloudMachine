package com.cloudmachine.net.api;


import com.cloudmachine.base.Operator;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.bean.AdBean;
import com.cloudmachine.bean.AuthBean;
import com.cloudmachine.bean.AuthDeviceItem;
import com.cloudmachine.bean.AuthInfoDetail;
import com.cloudmachine.bean.DepositItem;
import com.cloudmachine.bean.DeviceAuthItem;
import com.cloudmachine.bean.ElectronicFenceBean;
import com.cloudmachine.bean.EmunBean;
import com.cloudmachine.bean.H5Config;
import com.cloudmachine.bean.LarkDeviceBasicDetail;
import com.cloudmachine.bean.LarkDeviceDetail;
import com.cloudmachine.bean.LarkDeviceInfo;
import com.cloudmachine.bean.LarkMemberInfo;
import com.cloudmachine.bean.LarkMemberItem;
import com.cloudmachine.bean.MachineBrandInfo;
import com.cloudmachine.bean.MachineModelInfo;
import com.cloudmachine.bean.McDeviceInfo;
import com.cloudmachine.bean.MenuBean;
import com.cloudmachine.bean.MessageBO;
import com.cloudmachine.bean.OilSynBean;
import com.cloudmachine.bean.PayCodeItem;
import com.cloudmachine.bean.PickItemBean;
import com.cloudmachine.bean.QiToken;
import com.cloudmachine.bean.RepairDetail;
import com.cloudmachine.bean.RepairHistoryInfo;
import com.cloudmachine.bean.ResonItem;
import com.cloudmachine.bean.SalaryHistoryItem;
import com.cloudmachine.bean.ScanningOilLevelInfoArray;
import com.cloudmachine.bean.TelBean;
import com.cloudmachine.bean.VersionInfo;
import com.cloudmachine.bean.VideoBean;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * 类描述：
 * 创建人：xiaojw
 * 创建时间：2018/9
 * 修改备注：LARK接口对接
 */
public interface ApiService {

    //登录
    @FormUrlEncoded
    @POST("login")
    Observable<JsonObject> login(@Field("userName") String userName, @Field("password") String password, @Header("timeStamp") String timeStamp, @Header("sign") String sign);


    /**
     * 检测该手机账号是否注册了云机械账户
     *
     * @param mobile
     * @return
     */
    @GET("member/checkNum")
    Observable<BaseRespose<Integer>> checkNum(@Query("mobile") long mobile);


    @GET("member/getIdentifyCode")
    Observable<BaseRespose> getIdentifyCode(@Query("mobile") String mobile, @Query("code") String code);





    /**
     * 微信登录绑定手机号
     *
     * @param unionId
     * @param openId
     * @param account
     * @param code
     * @param pwd
     * @param nickname
     * @param headLogo
     * @return
     */
    @GET("member/bindWeichatForLogin")
    Observable<JsonObject> wxBind(@Query("unionId") String unionId,
                                  @Query("openId") String openId,
                                  @Query("account") String account,
                                  @Query("code") String code,
                                  @Query("pwd") String pwd,
                                  @Query("nickName") String nickname,
                                  @Query("headLogo") String headLogo);


    @GET("member/bindWechatForCash")
    Observable<BaseRespose> weiXinUserInfo(@QueryMap Map<String, String> parmasMap);

    /*---Lark项目迁移接口----*/

    @GET("common/getConfigKV")
    Observable<BaseRespose<List<TelBean>>> getServiceTel(@Query("keys") String keys);

    /**
     * 微信登录验证
     *
     * @param unionId 微信授权个人
     * @param openId  微信授权的账号id
     * @return
     */
    @GET("member/wxLogin")
    Observable<JsonObject> wxLogin(@Query("unionId") String unionId, @Query("openId") String openId, @Query("nickName") String nickName, @Query("headLogo") String headLogo);


    /**
     * @param adType 广告类型 1.启动 2.滚动 3.弹窗 4.活动
     */
    @GET("system/getAdvertisements")
    Observable<BaseRespose<List<AdBean>>> getSystemAd(@Query("adType") int adType);


    @GET("reason/getRefundReason")
    Observable<BaseRespose<List<ResonItem>>> getRefundReasonItems();

    /**
     * 微信绑定手机号
     *
     * @param mobile
     * @param type   1:注册、 2:忘记密码、 3:登陆微信绑定 4：钱包支付 11：支付宝绑定 10：微信支付绑定 12：H5获取手机验证码
     * @return
     */
    @GET("member/getCode")
    Observable<BaseRespose> getCode(@Query("mobile") long mobile, @Query("type") long type);

    /**
     * @param type 类型  10:微信 11：支付宝
     */
    @GET("member/unbind")
    Observable<BaseRespose> unbundled(@Query("type") int type);


//    @POST("member/getCode")
//    Observable<BaseRespose<JsonObject>> getCode(@Field("mobile") String mobile, @Field("type") String type);


    //获取APP广告
    @GET("system/startAd")
    Observable<BaseRespose<List<AdBean>>> getStartAd();


    @GET("pay/getCountByStatus")
    Observable<BaseRespose<JsonObject>> getMessageUntreatedCount();


    //获取首页菜单
    @GET("system/headMenu")
    Observable<BaseRespose<List<MenuBean>>> getHeadMenu();

    //获取APP版本信息
    @GET("system/getVersion")
    Observable<BaseRespose<VersionInfo>> getVersion(@Query("system") String system, @Query("version") String version);
//
//    @GET("device/getRepairList")
//    Observable<BaseRespose<RepairListInfo>> getRepairList(@QueryMap Map<String, String> map);

    @GET("device/repair/getRepairList")
    Observable<BaseRespose<List<RepairHistoryInfo>>> getRepairList(@Query("deviceId") String deviceId, @Query("page") int page);

    @GET("device/repair/getRepairInfoDetail")
    Observable<BaseRespose<RepairDetail>> getRepairDetail(@Query("orderNo") String orderNo);


    @GET("device/getOwnDeviceList")
    Observable<BaseRespose<List<McDeviceInfo>>> getOwnDeviceList(@Query("page") int page);

    @GET("device/repair/warningMessageReturn")
    Observable<BaseRespose<JsonObject>> getWarnMessage(@Query("mobile") String mobile);

    @GET("device/repair/saveRepairOrder")
    Observable<BaseRespose> saveRepairOrder(@QueryMap Map<String, String> paramsMap);

    @GET("evaluate/getEvaluateTag")
    Observable<BaseRespose> getEvaluateTag();

    @GET("evaluate/getEvaluateInfo")
    Observable<BaseRespose> getEvaluateInfo(@Query("orderNo") String orderNo);

    @GET("evaluate/saveEvaluate")
    Observable<BaseRespose> saveEvaluate(@Query("orderNo") String orderNo, @Query("satisfaction") String satisfaction, @Query("evaluateTel") String evaluateTel, @Query("suggestion") String suggestion);

    @GET("pay/getPaySign")
    Observable<BaseRespose> getPaySign(@Query("payType") int payType, @Query("orderNo") String orderNo);

    @GET("pay/alliancePaySign")
    Observable<BaseRespose> alliancePaySign(@Query("payType") int payType, @Query("orderNo") String orderNo, @Query("token") String token);

    //小票
    @GET("ticket/memberAuthInfo")
    Observable<BaseRespose<AuthBean>> getAuthInfo();


    //个人信息详情
    @GET("loan/verification/memberAuthInfo")
    Observable<BaseRespose<JsonObject>> getMemberAuthInfo();

    //保存通讯录
    @FormUrlEncoded
    @POST("loan/verification/saveContacts")
    Observable<BaseRespose<String>> saveContacts(@Field("contactsJsonStr") String contactsJsonStr);

    //运营商相关
    @FormUrlEncoded
    @POST("loan/verification/operatorAuth")
    Observable<JsonObject> authOperator(@Field("servicePwd") String servicePwd);

    @FormUrlEncoded
    @POST("loan/verification/operatorCodeValidRetry")
    Observable<BaseRespose<String>> retryOperatorCode(@Field("taskId") String taskId, @Field("internalTimeMinutes") int internalTimeMinutes);

    @GET("loan/verification/operatorCodeValid")
    Observable<BaseRespose<String>> checkOperatorCode(@Query("taskId") String taskId, @Query("smsCode") String smsCode);

    /**
     * 获取设备品牌列表
     *
     * @param typeId    车辆类别
     * @param brandName 品牌名称(可选)
     */
    @GET("common/getMachineBrand")
    Observable<BaseRespose<List<MachineBrandInfo>>> getBrandList(@Query("typeId") String typeId, @Query("brandName") String brandName);

    /**
     * 获取车辆型号列表
     *
     * @param typeId    同上
     * @param brandId   车辆品牌id
     * @param modelName 型号名称(可选)
     */
    @GET("common/getMachineModel")
    Observable<BaseRespose<List<MachineModelInfo>>> getModelList(@Query("typeId") String typeId, @Query("brandId") String brandId, @Query("modelName") String modelName);


    @GET("device/updateDeviceMemberRemark")
    Observable<BaseRespose<String>> updateDeviceMemberRemark(@Query("groupId") int groupId, @Query("deviceId") long deviceId, @Query("roleId") int roleId, @Query("remark") String remark);

    @GET("common/getEnum")
    Observable<BaseRespose<List<EmunBean>>> getEnum(@Query("enumType") String enumType);


    @GET("device/deleteFence")
    Observable<BaseRespose<String>> deleteFence(@Query("deviceId") long deviceId, @Query("type") int type);

    @GET("device/deleteDeviceMember")
    Observable<BaseRespose<String>> deleteDeviceMember(@Query("deviceId") long deviceId, @Query("deleteMemberId") int deleteMemberId);

    @GET("device/memberGroup")
    Observable<BaseRespose<List<LarkMemberItem>>> getMemberGroup(@Query("deviceId") long deviceId);

    @GET("device/addFence")
    Observable<BaseRespose<String>> addFence(@Query("lat") double lat, @Query("lng") double lng, @Query("fenceRadium") String fenceRadium, @Query("fenceType") int dfenceType, @Query("deviceId") long deviceId);

    @GET("device/electronicFence")
    Observable<BaseRespose<ElectronicFenceBean>> getElectronicFence(@Query("deviceId") long deviceId);


    @GET("device/getDeviceBasicDetail")
    Observable<BaseRespose<LarkDeviceBasicDetail>> getDeviceBasicDetail(@Query("deviceId") long deviceId);

    @GET("device/getDeviceAllList")
    Observable<BaseRespose<List<LarkDeviceDetail>>> getAllDeviceList(@Query("page") int page, @Query("deviceName") String deviceName);

    @GET("device/getDeviceNowData")
    Observable<BaseRespose<LarkDeviceDetail>> getLarkDeviceNowData(@Query("deviceId") long deviceId);

    @GET("device/getDeviceMapList")
    Observable<BaseRespose<List<LarkDeviceInfo>>> getDeviceMapList(@Query("page") String page);

    @FormUrlEncoded
    @POST("member/updateMemberInfo")
    Observable<BaseRespose<String>> updateMemberInfo(@Field("nickName") String nickName, @Field("logo") String logo);

    @GET("member/getMemberInfoById")
    Observable<BaseRespose<LarkMemberInfo>> getLarkMemberInfo();

    @FormUrlEncoded
    @POST("wallet/cashOut")
    Observable<JsonObject> cashOut(@Field("type") int type, @Field("money") String money, @Header("timeStamp") String timeStamp, @Header("sign") String sign);

    @GET("wallet/walletAmount")
    Observable<BaseRespose<JsonObject>> getWalletAmount();

    @GET("message/getMessageList")
    Observable<BaseRespose<List<MessageBO>>> getMessageList(@Query("page") int page, @Query("size") int size);

    /**
     * @param messageId 消息id
     * @param status    2:拒绝邀请 3:同意邀请
     */
    @FormUrlEncoded
    @POST("device/receive/message")
    Observable<BaseRespose<String>> receiveMessage(@Field("messageId") int messageId, @Field("status") int status);


    @FormUrlEncoded
    @POST("message/updateMessageStatus")
    Observable<BaseRespose<String>> updateMsgStatus(@Field("messageId") int messageId);

    @FormUrlEncoded
    @POST("message/deleteMessage")
    Observable<BaseRespose<String>> deleteMsg(@Field("messageId") long messageId);


    //开启状态-油位定制
    @GET("device/oil/isWork")
    Observable<JsonObject> queryWorkStatus(@Query("deviceId") long deviceId);

    @GET("device/oil/oilLevelList")
    Observable<BaseRespose<ScanningOilLevelInfoArray>> getTodayOil(@Query("deviceId") long deviceId);

    @GET("device/oil/oilLevelListWeekly")
    Observable<BaseRespose<ScanningOilLevelInfoArray>> getWeeklyOil(@Query("deviceId") long deviceId);

    //油位列表-油位定制
    @GET("device/oil/oilSynList")
    Observable<BaseRespose<List<OilSynBean>>> getOilSynList(@Query("deviceId") long deviceId);

    //复位-油位定制
    @GET("device/oil/reset")
    Observable<BaseRespose<String>> resetOilLevel(@Query("deviceId") long deviceId);

    //油位同步-油位定制
    @GET("device/oil/synSubmit")
    Observable<BaseRespose<String>> syncOil(@Query("deviceId") long deviceId, @Query("oilPosition") int oilPosition);


    @GET("order/listBoxCodeHistory")
    Observable<BaseRespose<List<PayCodeItem>>> getCodeHistoryList(@Query("page") int page, @Query("size") int size);

    @GET("order/listBoxCode")
    Observable<BaseRespose<List<PayCodeItem>>> getBoxCodeList(@Query("page") int page, @Query("size") int size);


    @GET("pay/depositList")
    Observable<BaseRespose<List<DepositItem>>> getDepositList();


    @GET("salary/salaryHistoryRecords")
    Observable<BaseRespose<List<SalaryHistoryItem>>> getSalaryHistoryRecords(@Query("type") int type, @Query("year") String year, @Query("month") String month, @Query("page") int page, @Query("size") int size);

    //机器所有权审核列表
    @GET("ticket/deviceAuthInfoList")
    Observable<BaseRespose<List<DeviceAuthItem>>> getDeviceAuthList();


    //获取机主成员
    @GET("member/getMachineOperator")
    Observable<BaseRespose<List<Operator>>> getMchineOperators();

    //银行卡验证
    @FormUrlEncoded
    @POST("loan/verification/authenticateBankCard")
    Observable<JsonObject> authBankCard(@Field("bankCardNo") String bankCardNo, @Field("reserveMobile") String reserveMobile, @Field("realName") String realName);

    //身份证照片识别
    @FormUrlEncoded
    @POST("loan/verification/OCR")
    Observable<JsonObject> verifyOcr(@Field("imgUrl") String imgUrl, @Field("redisUserId") String redisUserId);

    //身份证信息提交
    @FormUrlEncoded
    @POST("loan/verification/userInfoSubmit")
    Observable<BaseRespose<String>> submitIdUserInfo(@Field("redisUserId") String redisUserId);


    @GET("device/video/getImei")
    Observable<JsonObject> getImei(@Query("sn") String sn);

    //获取视频列表(直播流地址+点播列表)
    @GET("device/video/getVideoList")
    Observable<BaseRespose<VideoBean>> getVideoList(@Query("deviceId") String deviceId, @Query("startTime") String startTime, @Query("endTime") String endTime);


    //视频地址下发接口
    @GET("device/video/videoUpload")
    Observable<BaseRespose<String>> videoUpload(@Query("deviceId") String deviceId, @Query("id") String id);
    @GET
    Observable<BaseRespose<H5Config>> getConfigInfo(@Url String url);



    //申请押金退款
    @GET("pay/updateStatusByOrder")
    Observable<BaseRespose> rebundDesosit(@QueryMap Map<String, String> parmasMap);

    @GET("message/getMessageByid")
    Observable<BaseRespose<MessageBO>> getMessageByid(@Query("id") String id);

    /*小票2.0新增接口, 待接入*/

    @GET("borrowBill/getPersonalInformation")
    Observable<BaseRespose<AuthInfoDetail>> getPersonalInformation(@Query("uniqueId") String uniqueId, @Query("bnsType") int bnsType);


    @GET("borrowBill/getDeviceAudit")
    Observable<BaseRespose<List<AuthDeviceItem>>> getDeviceAudit(@Query("uniqueId") String uniqueId, @Query("deviceId") int deviceId);


    @FormUrlEncoded
    @POST("borrowBill/updatePersonalInformation")
    Observable<BaseRespose<String>> updatePersonalInformation(@Field("bnsType") int bnsType, @Field("uniqueId") String uniqueId,
                                                              @Field("resideAddress") String resideAddress, @Field("annualIncome") String annualIncome, @Field("deviceId") String deviceId,
                                                              @Field("listUrl") String listUrl);


    @FormUrlEncoded
    @POST("picture/applyResideAddress")
    Observable<BaseRespose<String>> submitHomeAddress(@Field("uniqueId") String uniqueId, @Field("resideAddress") String resideAddress);


    /**
     * 机器图片上传
     *
     * @param uniqueId    用户id
     * @param pictureUrls 图片路径集合 以逗号隔开
     * @param deviceId    机器id
     */
    @FormUrlEncoded
    @POST("picture/machineImageUpload")
    Observable<BaseRespose<String>> machineImgUpload(@Field("uniqueId") String uniqueId, @Field("pictureUrls") String pictureUrls, @Field("deviceId") int deviceId);

    /**
     * 个人信息图片上传
     *
     * @param bnsType 业务类型：0:手持身份证 1:工程合同 2:收入证明
     */
    @FormUrlEncoded
    @POST("picture/personalImageUpload")
    Observable<BaseRespose<String>> perImgUpload(@Field("uniqueId") String uniqueId, @Field("pictureUrls") String pictureUrls, @Field("bnsType") int bnsType);



    @GET("app/pay/aliPayOpenAuth")
    Observable<BaseRespose<String>> aliPayOpenAuth(@Query("memberId") long memberId);

    @GET("app/pay/aliPayUserInfo")
    Observable<BaseRespose<JsonObject>> aliPayUserInfo(@Query("memberId") long memberId, @Query("authCode") String authCode);


    @GET
    Observable<ResponseBody> downloadFile(@Url String key);


    @GET("token/token/pageList")
    Observable<BaseRespose<List<PickItemBean>>> getQnDtuCameraImages(@Query("prefix") String prefix, @Query("page") int page, @Query("size") int pagesize, @Query("w") int width);


    @GET("api_qn/uptoken")
    Observable<BaseRespose<QiToken>> getQinuParams();


    @GET("redPacketRecord/getActivityConfig")
    Observable<JsonObject>   getRedPacketConfig();


    @GET("ticket/openAccountUrl")
    Observable<BaseRespose<String>>  getOpenAccountUrl(@Query("uniqueId") String uniqueId);










}


