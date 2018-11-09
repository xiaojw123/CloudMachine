package com.cloudmachine.ui.home.model;

import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.bean.LarkDeviceDetail;
import com.cloudmachine.bean.LarkDeviceInfo;
import com.cloudmachine.bean.McDeviceInfo;
import com.cloudmachine.bean.McDeviceLocation;
import com.cloudmachine.bean.RedPacket;
import com.cloudmachine.bean.TelBean;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.ui.home.contract.DeviceContract;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by xiaojw on 2017/7/19.
 */

public class DeviceModel implements DeviceContract.Model {


    @Override
    public Observable<RedPacket> getReadPacketConfig() {
        return Api.getDefault(HostType.HOST_LARK).getRedPacketConfig().compose(RxHelper.handleCommonResult(RedPacket.class));
    }

    @Override
    public Observable<BaseRespose<List<LarkDeviceDetail>>> getAllDeviceList(int page, String deviceName) {
        return Api.getDefault(HostType.HOST_LARK).getAllDeviceList(page,deviceName).compose(RxHelper.<List<LarkDeviceDetail>>handleBaseResult());
    }

    @Override
    public Observable<LarkDeviceDetail> getDeviceNowData(long deviceId) {
        return Api.getDefault(HostType.HOST_LARK).getLarkDeviceNowData(deviceId).compose(RxHelper.<LarkDeviceDetail>handleResult());
    }

    @Override
    public Observable<BaseRespose<List<LarkDeviceInfo>>> getDeviceMapList(int page) {
        String pageStr = page == -1 ? null : String.valueOf(page);
        return Api.getDefault(HostType.HOST_LARK).getDeviceMapList(pageStr).compose(RxHelper.<List<LarkDeviceInfo>>handleBaseResult());
    }
//
//    private Func1<BaseRespose<List<LarkDeviceInfo>, List<LarkDeviceInfo>> larkLocFunc = new Func1<List<LarkDeviceInfo>, List<LarkDeviceInfo>>() {
//        @Override
//        public List<LarkDeviceInfo> call(List<LarkDeviceInfo> mcDeviceInfos) {
//            if (mcDeviceInfos != null && mcDeviceInfos.size() > 0) {
//                List<LarkDeviceInfo> dropItems = new ArrayList<>();
//                for (LarkDeviceInfo info : mcDeviceInfos) {
//                    if (info.getLat() == 0 || info.getLng() == 0) {
//                        dropItems.add(info);
//                    }
//                }
//                if (dropItems.size() > 0) {
//                    mcDeviceInfos.removeAll(dropItems);
//                }
//            }
//            return mcDeviceInfos;
//        }
//    };



    @Override
    public Observable<List<TelBean>> getServiceTel() {
        return Api.getDefault(HostType.HOST_LARK).getServiceTel("BoxServiceTel,RepairTel").compose(RxHelper.<List<TelBean>>handleResult());
    }
}
