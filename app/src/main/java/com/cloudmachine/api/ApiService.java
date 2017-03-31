package com.cloudmachine.api;


import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.recyclerbean.HomeBannerBean;
import com.cloudmachine.recyclerbean.HomeIssueDetailBean;
import com.cloudmachine.recyclerbean.HomeNewsBean;
import com.cloudmachine.struc.LatestDailyEntity;
import com.cloudmachine.struc.McDeviceInfo;
import com.cloudmachine.struc.ScoreInfo;

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
     *拿到用户积分信息
     * @param memberId
     * @return
     */
    @GET("member/userScoreInfo")
    Observable<BaseRespose<ScoreInfo>> getUserScoreInfo(@Query("memberId") String memberId);

    /**
     * 拿到用户签到信息
     * @param memberId
     * @return
     */
    @GET("member/insertSignPoint")
    Observable<BaseRespose<ScoreInfo>>getUserInsertSignInfo(@Query("memberId") String memberId);

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
    Observable<BaseRespose> getMasterDaily(@Query("artType") int artType, @Query("page") int page, @Query("size") int size);



}

