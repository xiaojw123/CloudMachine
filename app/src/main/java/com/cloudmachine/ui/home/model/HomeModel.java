package com.cloudmachine.ui.home.model;

import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.bean.ForceVBean;
import com.cloudmachine.bean.HomeBannerBean;
import com.cloudmachine.bean.QiToken;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.ui.home.contract.HomeContract;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by xiaojw on 2017/5/22.
 */

public class HomeModel implements HomeContract.Model {
    @Override
    public Observable<ArrayList<HomeBannerBean>> getHomeBannerInfo() {
        return Api.getDefault(HostType.HOST_CLOUDM).GetHomeBannerInfo(1,0)
                .compose(RxHelper.<ArrayList<HomeBannerBean>>handleResult());
    }

    @Override
    public Observable<Integer> getCountByStatus(long memberId, int status) {
        return Api.getDefault(HostType.HOST_CLOUDM).getCountByStatus(memberId,status).compose(RxHelper.<Integer>handleResult());
    }




    @Override
    public Observable<String> getMessageUntreatedCount(long memberId) {

        return Api.getDefault(HostType.HOST_CLOUDM_YJX).getMessageUntreatedCount(memberId).compose(RxHelper.<String>handleResult());
    }


    @Override
    public Observable<List<PopItem>> getPromotionModel(long memberId) {

        return Api.getDefault(HostType.HOST_CLOUDM).getPopList(memberId).compose(RxHelper.<List<PopItem>>handleResult());
    }

    @Override
    public Observable<JsonObject> getH5ConfigInfo() {
        return Api.getDefault(HostType.HOST_H5).getH5ConfigInfo().compose(RxHelper.<JsonObject>handleResult());
    }

}
