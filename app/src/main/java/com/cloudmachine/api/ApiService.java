package com.cloudmachine.api;


import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.recyclerbean.CheckNumBean;
import com.cloudmachine.recyclerbean.HomeBannerBean;
import com.cloudmachine.recyclerbean.HomeIssueDetailBean;
import com.cloudmachine.recyclerbean.HomeNewsBean;
import com.cloudmachine.recyclerbean.MasterDailyBean;
import com.cloudmachine.struc.LatestDailyEntity;
import com.cloudmachine.struc.McDeviceInfo;
import com.cloudmachine.struc.Member;
import com.cloudmachine.struc.ScoreInfo;
import com.cloudmachine.struc.UserInfo;

import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
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

    /**
     * 测试获取设置详细信息
     * @param type
     * @param memberId
     * @param key
     * @return
     */
    @GET("device/getDeviceByKey")
    Observable<BaseRespose<List<McDeviceInfo>>> getDeviceList
    (@Query("type") int type, @Query("memberId") long memberId, @Query("key") String key);

    /**
     * 获取最新文章列表（知乎数据测试Banner图）
     * @return
     */
    @GET("news/latest")
    Observable<LatestDailyEntity> getLatestDaily();

    /**
     * 多张图片上传
     * @param imags
     * @return
     */
    @Multipart
    @POST("kindEditorUpload")
    Observable<BaseRespose<String>> upLoadPhoto(@Part("file\"; filename=\"avatar.png\"") RequestBody file);

    /**
     * 拿到用户签到信息
     * @param memberId
     * @return
     */
    @GET("member/insertSignPoint")
    Observable<BaseRespose/*<ScoreInfo>*/> getUserInsertSignInfo(@Query("memberId") String memberId);

    /**
     * 获得轮播图
     * @param adsType
     * @return
     */
    @GET("ads/getAdvertisements")
    Observable<BaseRespose<ArrayList<HomeBannerBean>>> GetHomeBannerInfo(@Query("adsType") int adsType);

    /**
     * 获取中间广告位信息
     * @param adsType
     * @return
     */
    @GET("ads/getAdvertisements")
    Observable<BaseRespose<ArrayList<HomeNewsBean>>> GetHomeMidAdvertisement(@Query("adsType") int adsType);

    /**
     * 获取热门问题
     * @return
     */
    @GET("device/getHotQuestion")
    Observable<BaseRespose<HomeIssueDetailBean>> getHotQuestion();

    /**
     * 获取大师日报列表
     * @param artType
     * @param page
     * @param size
     * @return
     */
    @GET("art/getArticles")
    Observable<BaseRespose<List<MasterDailyBean>>> getMasterDaily(@Query("artType") int artType, @Query("page") int page, @Query("size") int size,@Query("artStatus") Integer artStatus);

    /**
     * 微信登录验证（是否绑定了云机械的账户）
     * @param unionId
     * @param openId
     * @return
     */
    @GET("member/wxLogin")
    Observable<BaseRespose> wxLogin(@Query("unionId") String unionId, @Query("openId") String openId, @Query("nickName") String nickName, @Query("headLogo") String headLogo);

    /**
     * 微信绑定手机号
     * @param mobile
     * @param type
     * @return
     */
    @GET("member/getCode")
    Observable<BaseRespose> wxBindMobile(@Query("mobile") long mobile, @Query("type") long type);

    /**
     * 检测该手机账号是否注册了云机械账户
     * @param mobile
     * @return
     */
    @GET("member/checkNum")
    Observable<BaseRespose<CheckNumBean>> checkNum(@Query("mobile") long mobile);


    /**
     * 微信登录绑定手机号
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
                                           @Query("nickname") String nickname,
                                           @Query("headLogo") String headLogo,
                                           @Query("type") Integer type);

    /**
     * 根据id获取到用户信息
     * @param memberId
     * @return
     */
    @GET("member/getMemberInfoById")
    Observable<BaseRespose<Member>> getMemberInfoById(@Query("memberId") Long memberId);

    /**
     * 获取用户积分
     * @param memberId
     * @return
     */
    @GET("member/userScoreInfo")
    Observable<BaseRespose<ScoreInfo>> getUserScoreInfo(@Query("memberId") Long memberId);

    /**
     * 修改用户信息
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
     * @param yjxid
     * @return
     */
    @GET("excamaster/yjxapi/yjxuser")
    Observable<BaseRespose<UserInfo>> excamMaster(@Query("yjxid") Long yjxid);

    /**
     * 测试李兆华test请求
     * @param sysCode
     * @param sysName
     * @param sysDes
     * @param idxShow
     * @return
     */
    @POST("admin/sysdef/add")
    Observable<BaseRespose> test(@Query("sysCode") String sysCode
            , @Query("sysName") String sysName, @Query("sysDes") String sysDes, @Query("idxShow") String idxShow);

}



















    /**
     * 拿到用户积分信息
     * @param memberId
     * @return
     */
   /* @GET("member/userScoreInfo")
    Observable<BaseRespose<ScoreInfo>> getUserScoreInfo(@Query("memberId") String memberId);*/
