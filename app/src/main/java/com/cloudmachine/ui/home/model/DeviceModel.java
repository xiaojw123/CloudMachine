package com.cloudmachine.ui.home.model;

import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.bean.ArticleInfo;
import com.cloudmachine.bean.McDeviceInfo;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.ui.home.contract.DeviceContract;

import java.util.List;

import rx.Observable;

/**
 * Created by xiaojw on 2017/7/19.
 */

public class DeviceModel implements DeviceContract.Model {
    @Override
    public Observable<List<McDeviceInfo>> getDevices(long memberId, int type) {
        return Api.getDefault(HostType.HOST_CLOUDM_YJX).getDevices(memberId,type).compose(RxHelper.<List<McDeviceInfo>>handleResult());
    }

    @Override
    public Observable<List<McDeviceInfo>> getDevices(int type) {
        return Api.getDefault(HostType.HOST_CLOUDM_YJX).getDevices(type).compose(RxHelper.<List<McDeviceInfo>>handleResult());
    }
    @Override
    public Observable<List<ArticleInfo>> getArticles() {
        return Api.getDefault(HostType.HOST_CLOUDM).getArticleList(0).compose(RxHelper.<List<ArticleInfo>>handleResult());
    }
}
