package com.cloudmachine.ui.home.model;


import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.bean.AllianceItem;
import com.cloudmachine.bean.RepairListInfo;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.ui.home.contract.MSupervisorContract;

import java.util.List;

import rx.Observable;

/**
 * Created by xiaojw on 2017/6/7.
 */

public class MSupervisorModel implements MSupervisorContract.Model {
    @Override
    public Observable<SiteBean> getSiteStation(double lng, double lat) {
        return Api.getDefault(HostType.HOST_CLOUDM_YJX).getSitesInfo(lng,lat).compose(RxHelper.<SiteBean>handleResult());
    }

    @Override
    public Observable<RepairListInfo> getRepairList(long memberId) {
        return Api.getDefault(HostType.HOST_CLOUDM_YJX).getRepairList(memberId).compose(RxHelper.<RepairListInfo>handleResult());
    }

    @Override
    public Observable<List<AllianceItem>> getAllianceList(long memberId) {
        return Api.getDefault(HostType.HOST_CLOUDM_YJX).getAllianceListByMember(memberId).compose(RxHelper.<List<AllianceItem>>handleResult());
    }
}
