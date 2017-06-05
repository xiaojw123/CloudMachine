package com.cloudmachine.ui.home.model;

import com.cloudmachine.api.Api;
import com.cloudmachine.api.HostType;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.struc.McDeviceBasicsInfo;
import com.cloudmachine.ui.home.contract.DeviceDetailContract;

import rx.Observable;

/**
 * Created by xiaojw on 2017/5/25.
 */

public class DeviceDetailModel  implements DeviceDetailContract.Model{
    @Override
    public Observable<BaseRespose<McDeviceBasicsInfo>> reqDeviceInfo(String deviceId, long memberId) {
        return Api.getDefault(HostType.CLOUDM_HOST).getDeviceInfo(deviceId,memberId);
    }
}
