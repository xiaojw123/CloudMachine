package com.cloudmachine.ui.home.model;


import com.cloudmachine.api.Api;
import com.cloudmachine.api.HostType;
import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.struc.RepairListInfo;
import com.cloudmachine.ui.home.contract.MSupervisorContract;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.VersionU;

import java.net.URLEncoder;

import rx.Observable;

/**
 * Created by xiaojw on 2017/6/7.
 */

public class MSupervisorModel implements MSupervisorContract.Model {
    @Override
    public Observable<SiteBean> getSiteStation(double lng, double lat) {
        return Api.getDefault(HostType.CLOUDM_HOST).getSitesInfo(lng,lat).compose(RxHelper.<SiteBean>handleResult());
    }

    @Override
    public Observable<RepairListInfo> getRepairList(long memberId) {
        return Api.getDefault(HostType.CLOUDM_HOST).getRepairList(Constants.OS_PLATFORM, URLEncoder.encode(VersionU.getVersionName()),memberId).compose(RxHelper.<RepairListInfo>handleResult());
    }
}
