package com.cloudmachine.ui.home.model;

import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.bean.H5Config;
import com.cloudmachine.bean.MenuBean;
import com.cloudmachine.bean.VersionInfo;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.ApiConstants;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.ui.home.contract.HomeContract;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.VersionU;
import com.google.gson.JsonObject;

import java.util.List;

import rx.Observable;

/**
 * Created by xiaojw on 2017/5/22.
 */

public class HomeModel implements HomeContract.Model {
    @Override
    public Observable<VersionInfo> getVersionInfo() {
        return Api.getDefault(HostType.HOST_LARK).getVersion(Constants.ANDROID, VersionU.getVersionName()).compose(RxHelper.<VersionInfo>handleResult());
    }
    @Override
    public Observable<List<MenuBean>> getHomeMenu() {
        return Api.getDefault(HostType.HOST_LARK).getHeadMenu().compose(RxHelper.<List<MenuBean>>handleResult());
    }

    @Override
    public Observable<JsonObject> getMessageUntreatedCount() {

        return Api.getDefault(HostType.HOST_LARK).getMessageUntreatedCount().compose(RxHelper.<JsonObject>handleResult());
    }

    @Override
    public Observable<H5Config> getConfigInfo() {
        return Api.getDefault(HostType.HOST_H5).getConfigInfo(ApiConstants.H5_CONFIG_URL).compose(RxHelper.<H5Config>handleResult());
    }

}
