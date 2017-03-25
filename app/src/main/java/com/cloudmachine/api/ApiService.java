package com.cloudmachine.api;


import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.struc.McDeviceInfo;
import com.cloudmachine.struc.LatestDailyEntity;

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

}
