package com.cloudmachine.ui.home.model;

import com.cloudmachine.api.Api;
import com.cloudmachine.api.HostType;
import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.recyclerbean.HomeBannerBean;
import com.cloudmachine.ui.home.contract.ActivitesContract;

import java.util.ArrayList;

import rx.Observable;

/**
 * Created by xiaojw on 2017/5/23.
 */

public class ActvitiesModel  implements ActivitesContract.Model{
    @Override
    public Observable<ArrayList<HomeBannerBean>> getHomeBannerInfo() {
        return Api.getDefault(HostType.GUOSHUAI_HOST).GetHomeBannerInfo(1,0)
                .compose(RxHelper.<ArrayList<HomeBannerBean>>handleResult());
    }
}
