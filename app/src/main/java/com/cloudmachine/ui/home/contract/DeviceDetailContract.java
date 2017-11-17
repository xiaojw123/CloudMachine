package com.cloudmachine.ui.home.contract;

import com.cloudmachine.base.BaseModel;
import com.cloudmachine.base.BasePresenter;
import com.cloudmachine.base.BaseView;
import com.cloudmachine.bean.McDeviceBasicsInfo;
import com.cloudmachine.bean.McDeviceInfo;

import rx.Observable;

/**
 * Created by xiaojw on 2017/5/25.
 */

public interface DeviceDetailContract {
     interface View extends BaseView {
        void retrunDeviceInfo(McDeviceBasicsInfo info);
        void updateDeviceDetail(McDeviceInfo info);
        void updateDeviceDetailError(String message);
    }

     interface Model extends BaseModel {
        Observable<McDeviceBasicsInfo> reqDeviceInfo(String deviceId, long memberId);
        Observable<McDeviceBasicsInfo> reqDeviceInfo(String deviceId);
        Observable<McDeviceInfo> reqDeviceNowData(String deviceId, long memberId);
        Observable<McDeviceInfo> reqDeviceNowData(String deviceId);
    }

    abstract class Presenter extends BasePresenter<DeviceDetailContract.View, DeviceDetailContract.Model> {
        public abstract void getDeviceInfo(String deviceId, long memberId);
        public abstract void getDeviceInfo(String deviceId);
        public abstract void getDeviceNowData(String deviceId);
    }

}
