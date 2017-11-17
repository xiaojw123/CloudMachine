package com.cloudmachine.ui.home.model;

import com.cloudmachine.bean.McDeviceInfo;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.bean.McDeviceBasicsInfo;
import com.cloudmachine.ui.home.contract.DeviceDetailContract;

import rx.Observable;

/**
 * Created by xiaojw on 2017/5/25.
 */

public class DeviceDetailModel implements DeviceDetailContract.Model {
    @Override
    public Observable<McDeviceBasicsInfo> reqDeviceInfo(String deviceId, long memberId) {
            return Api.getDefault(HostType.HOST_CLOUDM_YJX).getDeviceInfo(deviceId, memberId).compose(RxHelper.<McDeviceBasicsInfo>handleResult());

    }

    @Override
    public Observable<McDeviceBasicsInfo> reqDeviceInfo(String deviceId) {

        return Api.getDefault(HostType.HOST_CLOUDM_YJX).getDeviceInfo(deviceId).compose(RxHelper.<McDeviceBasicsInfo>handleResult());
    }

    @Override
    public Observable<McDeviceInfo> reqDeviceNowData(String deviceId, long memberId) {
        return Api.getDefault(HostType.HOST_CLOUDM_YJX).getDeviceNowData(deviceId,memberId).compose(RxHelper.<McDeviceInfo>handleResult());
    }

    @Override
    public Observable<McDeviceInfo> reqDeviceNowData(String deviceId) {
        return Api.getDefault(HostType.HOST_CLOUDM_YJX).getDeviceNowData(deviceId).compose(RxHelper.<McDeviceInfo>handleResult());
    }


}
