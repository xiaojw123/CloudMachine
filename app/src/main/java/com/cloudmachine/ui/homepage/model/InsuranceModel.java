package com.cloudmachine.ui.homepage.model;

import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.bean.HomeBannerBean;
import com.cloudmachine.ui.homepage.contract.InsuranceContract;

import java.util.ArrayList;

import rx.Observable;

/**
 * Created by xiaojw on 2017/4/28.
 */

public class InsuranceModel implements InsuranceContract.Model{

    @Override
    public Observable<ArrayList<HomeBannerBean>> getSharedInfo() {
        return Api.getDefault(HostType.GUOSHUAI_HOST).GetHomeInsurance(4)
                .compose(RxHelper.<ArrayList<HomeBannerBean>>handleResult());
    }
}
