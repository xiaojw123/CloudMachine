package com.cloudmachine.ui.home.model;

import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.bean.ArticleInfo;
import com.cloudmachine.bean.DeviceItem;
import com.cloudmachine.bean.McDeviceInfo;
import com.cloudmachine.bean.McDeviceLocation;
import com.cloudmachine.bean.TelBean;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.ui.home.contract.DeviceContract;
import com.cloudmachine.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by xiaojw on 2017/7/19.
 */

public class DeviceModel implements DeviceContract.Model {


    @Override
    public Observable<BaseRespose<List<DeviceItem>>> getDeviceItems(String memberId, int page) {
        return Api.getDefault(HostType.HOST_CLOUDM_YJX).getDeviceList(memberId, page,Constants.MC_DevicesList_AllType).compose(RxHelper.<List<DeviceItem>>handleBaseResult());
    }

    @Override
    public Observable<List<McDeviceInfo>> getDevices(long memberId, int type) {
        return Api.getDefault(HostType.HOST_CLOUDM_YJX).getDevices(memberId, type).compose(RxHelper.<List<McDeviceInfo>>handleResult()).map(locFunc);
    }

    @Override
    public Observable<List<McDeviceInfo>> getDevices(int type) {
        return Api.getDefault(HostType.HOST_CLOUDM_YJX).getDevices(type).compose(RxHelper.<List<McDeviceInfo>>handleResult()).map(locFunc);
//
    }

    private Func1<List<McDeviceInfo>, List<McDeviceInfo>> locFunc = new Func1<List<McDeviceInfo>, List<McDeviceInfo>>() {
        @Override
        public List<McDeviceInfo> call(List<McDeviceInfo> mcDeviceInfos) {
            List<McDeviceInfo> myDevices = new ArrayList<>();
            if (mcDeviceInfos != null && mcDeviceInfos.size() > 0) {
                List<McDeviceInfo> dropItems = new ArrayList<>();
                for (McDeviceInfo info : mcDeviceInfos) {
                    if (info.getType() == 1) {
                        myDevices.add(info);
                    }
                    McDeviceLocation loc = info.getLocation();
                    if (loc != null) {
                        if (loc.getLat() == 0 || loc.getLng() == 0) {
                            dropItems.add(info);
                        }
                    } else {
                        dropItems.add(info);
                    }
                }
                if (dropItems.size() > 0) {
                    mcDeviceInfos.removeAll(dropItems);
                }
            }
//            UserHelper.setMyDevices(myDevices);
            return mcDeviceInfos;
        }
    };

    @Override
    public Observable<List<ArticleInfo>> getArticles() {
        return Api.getDefault(HostType.HOST_CLOUDM).getArticleList(0).compose(RxHelper.<List<ArticleInfo>>handleResult());
    }

    @Override
    public Observable<List<TelBean>> getServiceTel() {
        return Api.getDefault(HostType.HOST_CLOUDM_YJX).getServiceTel("BoxServiceTel,RepairTel").compose(RxHelper.<List<TelBean>>handleResult());
    }

    @Override
    public Observable<McDeviceInfo> getDeviceNowData(String deviceId, long memberId) {
        return Api.getDefault(HostType.HOST_CLOUDM_YJX).getDeviceNowData(deviceId,memberId).compose(RxHelper.<McDeviceInfo>handleResult());
    }

    @Override
    public Observable<McDeviceInfo> getDeviceNowData(String deviceId) {
        return Api.getDefault(HostType.HOST_CLOUDM_YJX).getDeviceNowData(deviceId).compose(RxHelper.<McDeviceInfo>handleResult());
    }

    @Override
    public Observable<BaseRespose<List<McDeviceInfo>>> getAllDeviceList(long memberId, int page, String key) {
        return Api.getDefault(HostType.HOST_CLOUDM_YJX).getAllDeviceList(memberId,page,key).compose(RxHelper.<List<McDeviceInfo>>handleBaseResult());
    }
}
