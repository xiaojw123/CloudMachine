package com.cloudmachine.ui.repair.model;

import android.content.Context;

import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.bean.AllianceDetail;
import com.cloudmachine.bean.BOInfo;
import com.cloudmachine.bean.CWInfo;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.ui.repair.contract.RepairFinishContract;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by xiaojw on 2017/6/20.
 */

public class RepairFinishModel implements RepairFinishContract.Model {
    @Override
    public Observable<BOInfo> getBoInfo(Context context, String orderNum,String flag) {
        return Api.getDefault(HostType.HOST_CLOUDM_YJX).getBOInfo(getParamsMap(context, orderNum,flag)).compose(RxHelper.<BOInfo>handleResult());

    }

    @Override
    public Observable<CWInfo> getCWInfo(Context context, String orderNum,String flag) {
        return Api.getDefault(HostType.HOST_CLOUDM_YJX).getCWInfo(getParamsMap(context, orderNum,flag)).compose(RxHelper.<CWInfo>handleResult());
    }

    private Map<String, String> getParamsMap(Context context, String orderNum,String flag) {
        Map<String, String> map = new HashMap<>();
        map.put("orderNum", orderNum);
        map.put("flag", flag);
        if (UserHelper.isLogin(context)) {
            map.put("memberId", String.valueOf(UserHelper.getMemberId(context)));
        }
        return map;
    }

    @Override
    public Observable<AllianceDetail> getAllianceOrderDetail(long memberId, String orderNo) {
        return Api.getDefault(HostType.HOST_CLOUDM_YJX).getAllianceOrderDetail(memberId,orderNo).compose(RxHelper.<AllianceDetail>handleResult());
    }

}
