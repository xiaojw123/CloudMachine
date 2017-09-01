package com.cloudmachine.ui.home.model;

import android.content.Context;

import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.bean.RepairListInfo;
import com.cloudmachine.ui.home.contract.RepairHistoryContract;
import com.cloudmachine.utils.Constants;

import java.util.Map;

import rx.Observable;

/**
 * Created by xiaojw on 2017/7/12.
 */

public class RepairHistoryModel implements RepairHistoryContract.Model {

    @Override
    public Observable<RepairListInfo> getRepairList(Context context, long deviceId) {
        Map<String, String> paramsMap = Constants.getBasePraramsMap();
        if (deviceId == Constants.INVALID_DEVICE_ID) {
            if (UserHelper.isLogin(context)) {
                long memberId = UserHelper.getMemberId(context);
                paramsMap.put("memberId", String.valueOf(memberId));
            }
        } else {
            paramsMap.put("deviceId", String.valueOf(deviceId));
        }
        return Api.getDefault(HostType.CLOUDM_HOST).getRepairList(paramsMap).compose(RxHelper.<RepairListInfo>handleResult());

    }
}
