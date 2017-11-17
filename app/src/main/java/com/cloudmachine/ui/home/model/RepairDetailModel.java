package com.cloudmachine.ui.home.model;

import android.content.Context;

import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.bean.BOInfo;
import com.cloudmachine.bean.CWInfo;
import com.cloudmachine.ui.home.contract.RepairDetailContract;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by xiaojw on 2017/8/2.
 */

public class RepairDetailModel implements RepairDetailContract.Model {
    @Override
    public Observable<SiteBean> getSiteStation(double lng, double lat) {
        return Api.getDefault(HostType.HOST_CLOUDM_YJX).getSitesInfo(lng,lat).compose(RxHelper.<SiteBean>handleResult());
    }
    @Override
    public Observable<BOInfo> getBoInfo(Context context, String orderNum,String flag) {
        return Api.getDefault(HostType.HOST_CLOUDM_YJX).getBOInfo(getParamsMap(context, orderNum,flag)).compose(RxHelper.<BOInfo>handleResult());

    }

    @Override
    public Observable<CWInfo> getCWInfo(Context context, String orderNum,String flag) {
        return Api.getDefault(HostType.HOST_CLOUDM_YJX).getCWInfo(getParamsMap(context, orderNum,flag)).compose(RxHelper.<CWInfo>handleResult());
    }

    private Map<String, String> getParamsMap(Context context, String orderNum, String flag) {
        Map<String, String> map = new HashMap<>();
        map.put("orderNum", orderNum);
        map.put("flag", flag);
        if (UserHelper.isLogin(context)) {
            map.put("memberId", String.valueOf(UserHelper.getMemberId(context)));
        }
        return map;
    }
}
