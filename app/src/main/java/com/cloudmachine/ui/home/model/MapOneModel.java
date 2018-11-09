package com.cloudmachine.ui.home.model;

import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.bean.ElectronicFenceBean;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.ui.home.contract.MapOneContract;

import rx.Observable;

/**
 * Created by xiaojw on 2018/9/19.
 */

public class MapOneModel implements MapOneContract.Model {
    @Override
    public Observable<ElectronicFenceBean> getElecFence(long deviceId) {
        return Api.getDefault(HostType.HOST_LARK).getElectronicFence(deviceId).compose(RxHelper.<ElectronicFenceBean>handleResult());
    }

    @Override
    public Observable<String> addElecFence(double lat, double lng, String fenceRadium, int fenceType, long deviceId) {
        return Api.getDefault(HostType.HOST_LARK).addFence(lat, lng, fenceRadium, fenceType, deviceId).compose(RxHelper.<String>handleResult());
    }

    @Override
    public Observable<String> deleteElecFence(long deviceId, int type) {
        return Api.getDefault(HostType.HOST_LARK).deleteFence(deviceId,type).compose(RxHelper.<String>handleResult());
    }


}
