package com.cloudmachine.ui.home.model;

import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.bean.OilSynBean;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.ui.home.contract.OilSyncContract;

import java.util.List;

import rx.Observable;

/**
 * Created by xiaojw on 2018/5/9.
 */

public class OilSyncModel  implements OilSyncContract.Model{
    @Override
    public Observable<List<OilSynBean>> getOilSyncList(long deviceId) {
        return Api.getDefault(HostType.HOST_CLOUDM_YJX).getOilSynList(deviceId).compose(RxHelper.<List<OilSynBean>>handleResult());
    }

    @Override
    public Observable<BaseRespose<String>> syncOilLevel(long deviceId, int oilPos,long memberId) {
        return Api.getDefault(HostType.HOST_CLOUDM_YJX).syncOil(deviceId,oilPos,memberId).compose(RxHelper.<String>handleBaseResult());
    }

    @Override
    public Observable<BaseRespose<String>> restOilLevel(long deviceId) {
        return Api.getDefault(HostType.HOST_CLOUDM_YJX).resetOilLevel(deviceId).compose(RxHelper.<String>handleBaseResult());
    }
}
