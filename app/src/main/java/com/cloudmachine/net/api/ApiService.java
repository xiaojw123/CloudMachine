package com.cloudmachine.net.api;


import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.bean.AdBean;
import com.cloudmachine.bean.ArticleInfo;
import com.cloudmachine.bean.BOInfo;
import com.cloudmachine.bean.CWInfo;
import com.cloudmachine.bean.CheckNumBean;
import com.cloudmachine.bean.DepositItem;
import com.cloudmachine.bean.ForceVBean;
import com.cloudmachine.bean.HomeBannerBean;
import com.cloudmachine.bean.McDeviceBasicsInfo;
import com.cloudmachine.bean.McDeviceInfo;
import com.cloudmachine.bean.Member;
import com.cloudmachine.bean.MenuBean;
import com.cloudmachine.bean.MessageBO;
import com.cloudmachine.bean.PickItemBean;
import com.cloudmachine.bean.QiToken;
import com.cloudmachine.bean.RepairListInfo;
import com.cloudmachine.bean.ResonItem;
import com.cloudmachine.bean.UserInfo;
import com.cloudmachine.ui.home.model.CouponBean;
import com.cloudmachine.ui.home.model.OrderCouponBean;
import com.cloudmachine.ui.home.model.PopItem;
import com.cloudmachine.ui.home.model.RoleBean;
import com.cloudmachine.ui.home.model.SiteBean;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/3/12 下午6:19
 * 修改人：shixionglu
 * 修改时间：2017/3/12 下午6:19
 * 修改备注：
 */
public interface ApiService {
    @GET("message/getMessageByid")
    Observable<BaseRespose<MessageBO>> getMessageByid(@Query("id") String id);

    //获取首页菜单
    @GET("system/headMenu")
    Observable<BaseRespose<List<MenuBean>>> getHeadMenu();
    //获取APP广告
    @GET("system/startAd")
    Observable<BaseRespose<List<AdBean>>> getStartAd();
/**
 * status 订单状态 0：待支付 1：已支付 2：退款审核 3：已退款
 */
    @GET("app/pay/getCountByStatus")
    Observable<BaseRespose<Integer>> getCountByStatus(@Query("memberId") long memberId,@Query("status") int status);

    @GET("app/pay/cashOut")
    Observable<BaseRespose> cashOut(@Query("memberId") long memberId,@Query("type") int type);

    @GET("app/pay/walletAmount")
    Observable<BaseRespose<JsonObject>> getWalletAmount(@Query("memberId") long memberId);

    @GET("app/pay/getIdentifyCode")
    Observable<BaseRespose> getIdentifyCode(@Query("mobile") String mobile,@Query("code") String code);
    /**
     * @param memberId 用户id
     * @param type 类型  10:微信 11：支付宝
     */
    @GET("app/pay/Unbundled")
    Observable<BaseRespose>  unbundled(@Query("memberId") long memberId,@Query("type") int type);

    @GET("app/pay/weiXinUserInfo")
    Observable<BaseRespose> weiXinUserInfo(@QueryMap Map<String,String> parmasMap);

    @GET("app/pay/aliPayOpenAuth")
    Observable<BaseRespose<String>> aliPayOpenAuth(@Query("memberId") long memberId);
    @GET("app/pay/aliPayUserInfo")
    Observable<BaseRespose<JsonObject>> aliPayUserInfo(@Query("memberId") long memberId,@Query("authCode")   String authCode);



    //申请押金退款
    @GET("app/pay/updateStatusByOrder")
    Observable<BaseRespose>  rebundDesosit(@QueryMap Map<String,String> parmasMap);


    @GET("app/reason/getRefundReason")
    Observable<BaseRespose<List<ResonItem>>>  getRefundReasonItems(@Query("memberId") long memberId);

    @GET("app/pay/selectMyOrder")
    Observable<BaseRespose<List<DepositItem>>> getDepositList(@Query("memberId") long memberId);

    @GET("{key}")
    Observable<ResponseBody> downloadImg(@Path("key") String key);

    @GET
    Observable<ResponseBody> downloadFile(@Url String key);

    @GET("device/getImei")
    Observable<JsonObject> getImei(@Query("memberId") long memberId, @Query("sn") String sn);


    @GET("token/token/pageList")
    Observable<BaseRespose<List<PickItemBean>>> getQnDtuCameraImages(@Query("prefix") String prefix, @Query("page") int page, @Query("size") int pagesize, @Query("w") int width);


    @GET("version/forceUpdate?system=Android")
    Observable<BaseRespose<ForceVBean>> forceUpdate();

    @GET("api_qn/uptoken")
    Observable<BaseRespose<QiToken>> getQinuParams();

    @POST("member/registerNew")
    Observable<BaseRespose<JsonObject>> register(@FieldMap Map<String, String> parmasMap);

    @POST("member/forgetPwd")
    Observable<BaseRespose<JsonObject>> forgetPassword(@Field("mobile") String mobile, @Field("pwd") String pwd, @Field("code") String code);

    @POST("member/getCode")
    Observable<BaseRespose<JsonObject>> getCode(@Field("mobile") String mobile, @Field("type") String type);

    @GET("pay/getPaySign")
    Observable<BaseRespose<JsonObject>> getPaySign(@Query("memberId") long memberId, @Query("orderNum") String orderNum, @Query("payType") int payType, @Query("coupons") String coupons);

    @GET("pay/orderCoupon")
    Observable<BaseRespose<OrderCouponBean>> getOrderCouponList(@Query("memberId") long memberId, @Query("orderNum") String orderNum);

    //获取可用优惠券
    @GET("app/userCoupon/myCouponBase")
    Observable<BaseRespose<CouponBean>> getAvalidCouponList(@Query("memberId") long memberId);

    //获取不可用优惠券
    @GET("app/userCoupon/myInvalidCoupon")
    Observable<BaseRespose<CouponBean>> getInvaildCouponList(@Query("memberId") long memberId);

    //获取可用优惠券详情
    @GET("app/userCoupon/myCouponDetail")
    Observable<BaseRespose<CouponBean>> getMyCouponDetailList(@Query("memberId") long memberId, @Query("couponBaseId") int couponBaseId);

    @GET("art/queryArticles")
    Observable<BaseRespose<List<ArticleInfo>>> getArticleList(@Query("artStatus") int artStatus);

    @GET("message/warningMessageReturn")
    Observable<BaseRespose<JsonObject>> getWarnMessage(@Query("memberId") long memberId, @Query("mobile") String mobile);

    @GET("config/")
    Observable<BaseRespose<JsonObject>> getH5ConfigInfo();

    @GET("spread/getPopList")
    Observable<BaseRespose<List<PopItem>>> getPopList(@Query("memberId") long memberid);


    @GET("device/deleteMsg")
    Observable<BaseRespose<String>> deleteMsg(@Query("memberId") long memberId, @Query("messageId") long messageId);

    //获取系统消息
    @GET("device/getSystemMessages")
    Observable<BaseRespose<List<MessageBO>>> getSystemMsg(@Query("memberId") long memberId);

    //获取消息列表
    @GET("device/getAllMessages")
    Observable<BaseRespose<List<MessageBO>>> getAllMsg(@Query("memberId") long memberId);

    //问卷调查
    @GET("question/need")
    Observable<BaseRespose<MessageBO>> questionNeed(@Query("memberId") long memberId);


    @GET("device/acceptMessage")
    Observable<BaseRespose<String>> acceptMsg(@Query("memberId") long memberId, @Query("messageId") String messageId);

    @GET("device/rejectMessage")
    Observable<BaseRespose<String>> rejectMsg(@Query("memberId") long memberId, @Query("messageId") String messageId);

    @GET("device/updateMessageStatus")
    Observable<BaseRespose<String>> updateMsgStatus(@Query("memberId") long memberId, @Query("messageId") String messageId);


    @GET("device/queryWorkDetail")
    Observable<BaseRespose<BOInfo>> getBOInfo(@QueryMap Map<String, String> map);

    @GET("device/queryWorkDetail")
    Observable<BaseRespose<CWInfo>> getCWInfo(@QueryMap Map<String, String> map);

    @GET("repairStation/updateMemberRemark")
    Observable<BaseRespose<String>> updateMemberRemark(@Query("fid") long fid, @Query("memberId") long memberId, @Query("deviceId") long deviceId, @Query("remark") String remark, @Query("roleId") long roleId);

    @GET("repairStation/getRoleTypeList")
    Observable<BaseRespose<List<RoleBean>>> getRoleList();

    @GET("repairStation/favoriteSites")
    Observable<BaseRespose<SiteBean>> getSitesInfo(@Query("lng") double lng, @Query("lat") double lat);

    @GET("device/getRepairList")
    Observable<BaseRespose<RepairListInfo>> getRepairList(@QueryMap Map<String, String> map);

    @GET("device/getRepairList")
    Observable<BaseRespose<RepairListInfo>> getRepairList(@Query("memberId") long memberId);

    @GET("device/getRepairList")
    Observable<BaseRespose<RepairListInfo>> getRepairList(@Query("osPlatform") String osPlatform, @Query("osVersion") String osVersion, @Query("memberId") long memberId, @Query("deviceId") long deviceId);

    @GET("device/getDevice")
    Observable<BaseRespose<McDeviceBasicsInfo>> getDeviceInfo(@Query("deviceId") String deviceId, @Query("memberId") long memberId);

    @GET("device/getDevice")
    Observable<BaseRespose<McDeviceBasicsInfo>> getDeviceInfo(@Query("deviceId") String deviceId);

    @GET("device/getDeviceNowData")
    Observable<BaseRespose<McDeviceInfo>> getDeviceNowData(@Query("deviceId") String deviceId, @Query("memberId") long memberId);

    @GET("device/getDeviceNowData")
    Observable<BaseRespose<McDeviceInfo>> getDeviceNowData(@Query("deviceId") String deviceId);


    @GET("device/getDeviceByKey")
    Observable<BaseRespose<List<McDeviceInfo>>> getDevices(@Query("memberId") long memberId, @Query("type") int type);

    @GET("device/getDeviceByKey")
    Observable<BaseRespose<List<McDeviceInfo>>> getDevices(@Query("type") int type);

    /**
     * 测试获取设置详细信息
     *
     * @param type
     * @param memberId
     * @param key
     * @return
     */
    @GET("device/getDeviceByKey")
    Observable<BaseRespose<List<McDeviceInfo>>> getDeviceList
    (@Query("type") int type, @Query("memberId") long memberId, @Query("key") String key);


    /**
     * 多张图片上传
     *
     * @param
     * @return
     */
    @Multipart
    @POST("kindEditorUpload")
    Observable<BaseRespose<String>> upLoadPhoto(@Part("file\"; filename=\"avatar.png\"") RequestBody file);


    /**
     * 获得轮播图
     *
     * @param adsType
     * @return
     */
    @GET("ads/getAdvertisements")
    Observable<BaseRespose<ArrayList<HomeBannerBean>>> GetHomeBannerInfo(@Query("adsType") int adsType, @Query("adsStatus") int adsStatus);

    /**
     * 获得车险
     *
     * @param adsType
     * @return
     */
    @GET("ads/getAdvertisements")
    Observable<BaseRespose<ArrayList<HomeBannerBean>>> GetHomeInsurance(@Query("adsType") int adsType);


    /**
     * 获取热门问题
     *
     * @return
     */

    @GET("device/getMessageUntreatedCount")
    Observable<BaseRespose<String>> getMessageUntreatedCount(@Query("memberId") long memberId);


    /**
     * 微信登录验证（是否绑定了云机械的账户）
     *
     * @param unionId
     * @param openId
     * @return
     */
    @GET("member/wxLogin")
    Observable<JsonObject> wxLogin(@Query("unionId") String unionId, @Query("openId") String openId, @Query("nickName") String nickName, @Query("headLogo") String headLogo);

    /**
     * 微信绑定手机号
     *
     * @param mobile
     * @param type
     * @return
     */
    @GET("member/getCode")
    Observable<BaseRespose> wxBindMobile(@Query("mobile") long mobile, @Query("type") long type);

    /**
     * 检测该手机账号是否注册了云机械账户
     *
     * @param mobile
     * @return
     */
    @GET("member/checkNum")
    Observable<BaseRespose<CheckNumBean>> checkNum(@Query("mobile") long mobile);


    /**
     * 微信登录绑定手机号
     *
     * @param unionId
     * @param openId
     * @param account
     * @param code
     * @param inviteCode
     * @param pwd
     * @param nickname
     * @param headLogo
     * @param type
     * @return
     */
    @GET("member/wxBind")
    Observable<BaseRespose<Member>> wxBind(@Query("unionId") String unionId,
                                           @Query("openId") String openId,
                                           @Query("account") String account,
                                           @Query("code") String code,
                                           @Query("inviteCode") String inviteCode,
                                           @Query("pwd") String pwd,
                                           @Query("nickName") String nickname,
                                           @Query("headLogo") String headLogo,
                                           @Query("type") Integer type);

    /**
     * 根据id获取到用户信息
     *
     * @param memberId
     * @return
     */
    @GET("member/getMemberInfoById")
    Observable<BaseRespose<Member>> getMemberInfoById(@Query("memberId") long memberId);

    /**
     * 获取用户积分
     *
     * @param memberId
     * @return
     */
//    @GET("member/userScoreInfo")
//    Observable<BaseRespose<ScoreInfo>> getUserScoreInfo(@Query("memberId") Long memberId);

    /**
     * 修改用户信息
     *
     * @param memberId
     * @param key
     * @param value
     * @return
     */
    @GET("member/editInfoByKey")
    Observable<BaseRespose> modifySignature(@Query("memberId") long memberId,
                                            @Query("key") String key,
                                            @Query("value") String value);

    /**
     * 根据云机械id获取对应用户的挖机大师id
     *
     * @param yjxid
     * @return
     */
    @GET("excamaster/yjxapi/yjxuser")
    Observable<BaseRespose<UserInfo>> excamMaster(@Query("yjxid") Long yjxid);

    /**
     * 测试李兆华test请求]
     *
     * @param sysCode
     * @param sysName
     * @param sysDes
     * @param idxShow
     * @return
     */
    @POST("admin/sysdef/add")
    Observable<BaseRespose> test(@Query("sysCode") String sysCode
            , @Query("sysName") String sysName, @Query("sysDes") String sysDes, @Query("idxShow") String idxShow);

    /**
     * 大师日报点击增加阅读量
     *
     * @param id
     * @return
     */
    @GET("art/readCount")
    Observable<BaseRespose> readCount(@Query("id") Integer id);


    /**
     * 获取到系统消息
     *
     * @return
     */
//    @GET("device/getSystemMessages")
//    Observable<BaseRespose<List<MessageBO>>> getSystemMessage(@Query("memberId") Long memberId
//            , @Query("pageNo") int pageNo, @Query("pageSize") int pageSize);

}


