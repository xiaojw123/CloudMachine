package com.cloudmachine.ui.home.model;

import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.bean.McDeviceInfo;
import com.cloudmachine.ui.home.contract.DeviceContract;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.VersionU;

import java.util.List;

import rx.Observable;

/**
 * Created by xiaojw on 2017/7/19.
 */

public class DeviceModel implements DeviceContract.Model {
    @Override
    public Observable<List<McDeviceInfo>> getDevices(long memberId, int type) {
        return Api.getDefault(HostType.CLOUDM_HOST).getDevices(Constants.OS_PLATFORM, VersionU.getVersionName(), memberId,type).compose(RxHelper.<List<McDeviceInfo>>handleResult());
    }

    @Override
    public Observable<List<McDeviceInfo>> getDevices(int type) {
        return Api.getDefault(HostType.CLOUDM_HOST).getDevices(Constants.OS_PLATFORM, VersionU.getVersionName(),type).compose(RxHelper.<List<McDeviceInfo>>handleResult());
    }
}
