package com.cloudmachine.ui.homepage.model;

import com.cloudmachine.api.Api;
import com.cloudmachine.api.HostType;
import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.recyclerbean.HomeBannerBean;
import com.cloudmachine.recyclerbean.HomeIssueDetailBean;
import com.cloudmachine.recyclerbean.HomeNewsBean;
import com.cloudmachine.ui.homepage.contract.HomePageContract;

import org.json.JSONObject;

import java.util.ArrayList;

import rx.Observable;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/3/18 上午7:54
 * 修改人：shixionglu
 * 修改时间：2017/3/18 上午7:54
 * 修改备注：
 */

public class HomePageModel implements HomePageContract.Model{

    /**
     * 获取首页轮播条信息
     * @return
     */
    @Override
    public Observable<ArrayList<HomeBannerBean>> getHomeBannerInfo() {
        return Api.getDefault(HostType.GUOSHUAI_HOST).GetHomeBannerInfo(1,0)
                .compose(RxHelper.<ArrayList<HomeBannerBean>>handleResult());
    }

    /**
     * 获取中间广告位信息
     * @return
     */
    @Override
    public Observable<ArrayList<HomeNewsBean>> getHomeMidAdvertisement() {
        return Api.getDefault(HostType.GUOSHUAI_HOST).GetHomeMidAdvertisement(2)
                .compose(RxHelper.<ArrayList<HomeNewsBean>>handleResult());
    }

    @Override
    public Observable<HomeIssueDetailBean> getHotQuestion() {
        return Api.getDefault(HostType.CAITINGTING_HOST).getHotQuestion()
                .compose(RxHelper.<HomeIssueDetailBean>handleResult());
    }

    @Override
    public Observable<JSONObject> getMessageUntreated(long memberId) {
//        CLOUDM_HOST
//        return Api.getDefault(HostType.CLOUDM_HOST).getMessageUntreatedCount(memberId)
//                .compose(RxHelper.<JSONObject>handleResult());
        return null;
    }


}
