package com.cloudmachine.ui.home.contract;

import com.cloudmachine.base.BaseModel;
import com.cloudmachine.base.BasePresenter;
import com.cloudmachine.base.BaseView;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.bean.LarkDeviceDetail;
import com.cloudmachine.bean.LarkDeviceInfo;
import com.cloudmachine.bean.McDeviceInfo;
import com.cloudmachine.bean.RedPacket;
import com.cloudmachine.bean.TelBean;

import java.util.List;

import rx.Observable;

/**
 * Created by xiaojw on 2017/7/19.
 */

public interface DeviceContract {
    interface View extends BaseView {
        void retrunAllDeviceError();

        void returnAllDeviceList(List<LarkDeviceDetail> deviceList, boolean isFirst, boolean isLast, int toalSize);

        void updateLarkDevices(List<LarkDeviceInfo> deviceList);

        void updateDevicesError(String errorMsg);

        void retrunGetServiceTel(String boxTel, String repairTel);

        void retrunPacketConfig(String imgUrl ,String jumpUrl,boolean isNoOpen);
        void hidenPacket();
    }

    interface Model extends BaseModel {
        Observable<RedPacket> getReadPacketConfig();

        Observable<BaseRespose<List<LarkDeviceDetail>>> getAllDeviceList(int page, String deviceName);

        Observable<LarkDeviceDetail> getDeviceNowData(long deviceId);

        Observable<BaseRespose<List<LarkDeviceInfo>>> getDeviceMapList(int page);


        Observable<List<TelBean>> getServiceTel();

    }

    abstract class Prensenter extends BasePresenter<DeviceContract.View, DeviceContract.Model> {
        public abstract void getReadPacketConfig();

        public abstract void getAllDeviceList(int page, String deviceName);

        public abstract void updatePagerItem(long deviceId, android.view.View itemView);

        public abstract void getDeviceMapList(int page);


        public abstract void getServiceTel();

    }


}
