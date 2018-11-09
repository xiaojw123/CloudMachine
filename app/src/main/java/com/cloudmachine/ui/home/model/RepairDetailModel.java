package com.cloudmachine.ui.home.model;

import com.cloudmachine.bean.RepairDetail;
import com.cloudmachine.bean.SiteBean;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.ui.home.contract.RepairDetailContract;

import rx.Observable;

/**
 * Created by xiaojw on 2017/8/2.
 */

public class RepairDetailModel implements RepairDetailContract.Model {
    @Override
    public Observable<SiteBean> getSiteStation(double lng, double lat) {
//        return Api.getDefault(HostType.HOST_CLOUDM_YJX).getSitesInfo(lng,lat).compose(RxHelper.<SiteBean>handleResult());
        return null;
    }

    @Override
    public Observable<RepairDetail> getRepairDetail(String orderNo) {
        return Api.getDefault(HostType.HOST_LARK).getRepairDetail(orderNo).compose(RxHelper.<RepairDetail>handleResult());
    }

}
