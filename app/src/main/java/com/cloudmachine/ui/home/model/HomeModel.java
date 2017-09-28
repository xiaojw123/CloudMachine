package com.cloudmachine.ui.home.model;

import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.bean.HomeBannerBean;
import com.cloudmachine.bean.UnReadMessage;
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
        return Api.getDefault(HostType.GUOSHUAI_HOST).GetHomeBannerInfo(1,0)
                .compose(RxHelper.<ArrayList<HomeBannerBean>>handleResult());
    }

    @Override
    public Observable<UnReadMessage> getMessageUntreatedCount(long memberId) {

        return Api.getDefault(HostType.CLOUDM_HOST).getMessageUntreatedCount(memberId).compose(RxHelper.<UnReadMessage>handleResult());
    }


    @Override
    public Observable<List<PopItem>> getPromotionModel(long memberId) {

        return Api.getDefault(HostType.GUOSHUAI_HOST).getPopList(memberId).compose(RxHelper.<List<PopItem>>handleResult());
    }

    @Override
    public Observable<JsonObject> getH5ConfigInfo() {
        return Api.getDefault(HostType.H5_CONFIG_HOST).getH5ConfigInfo().compose(RxHelper.<JsonObject>handleResult());
    }

}
