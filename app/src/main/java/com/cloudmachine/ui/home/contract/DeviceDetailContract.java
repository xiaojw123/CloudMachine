package com.cloudmachine.ui.home.contract;

import com.cloudmachine.base.BaseModel;
import com.cloudmachine.base.BasePresenter;
import com.cloudmachine.base.BaseView;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.struc.McDeviceBasicsInfo;

import rx.Observable;

/**
 * Created by xiaojw on 2017/5/25.
 */

public interface DeviceDetailContract {
     interface View extends BaseView {
        void retrunDeviceInfo(McDeviceBasicsInfo info);
    }

     interface Model extends BaseModel {
        Observable<BaseRespose<McDeviceBasicsInfo>> reqDeviceInfo(String deviceId, long memberId);
    }

    abstract class Presenter extends BasePresenter<DeviceDetailContract.View, DeviceDetailContract.Model> {
        public abstract void getDeviceInfo(String deviceId, long memberId);
    }

}
