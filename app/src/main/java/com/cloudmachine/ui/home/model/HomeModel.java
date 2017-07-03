package com.cloudmachine.ui.home.model;

import com.cloudmachine.api.Api;
import com.cloudmachine.api.HostType;
import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.recyclerbean.HomeBannerBean;
import com.cloudmachine.struc.McDeviceInfo;
import com.cloudmachine.struc.UnReadMessage;
import com.cloudmachine.ui.home.contract.HomeContract;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.VersionU;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by xiaojw on 2017/5/22.
 */

public class HomeModel implements HomeContract.Model {
    @Override
    public Observable<BaseRespose<UnReadMessage>> getMessageUntreatedCount(long memberId) {

        return  Api.getDefault(HostType.CLOUDM_HOST).getMessageUntreatedCount(memberId);
    }

    @Override
    public Observable<List<McDeviceInfo>> getDevices(long memberId, int type) {
        return Api.getDefault(HostType.CLOUDM_HOST).getDevices(Constants.OS_PLATFORM, VersionU.getVersionName(), memberId,type).compose(RxHelper.<List<McDeviceInfo>>handleResult());
    }

    @Override
    public Observable<List<McDeviceInfo>> getDevices(int type) {
        return Api.getDefault(HostType.CLOUDM_HOST).getDevices(Constants.OS_PLATFORM, VersionU.getVersionName(),type).compose(RxHelper.<List<McDeviceInfo>>handleResult());
    }

    @Override
    public Observable<BaseRespose<ArrayList<HomeBannerBean>>> getPromotionModel() {
        return Api.getDefault(HostType.GUOSHUAI_HOST).GetHomeBannerInfo(2,0);
    }

}
