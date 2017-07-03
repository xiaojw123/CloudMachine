package com.cloudmachine.ui.repair.model;

import com.cloudmachine.api.Api;
import com.cloudmachine.api.HostType;
import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.struc.BOInfo;
import com.cloudmachine.struc.CWInfo;
import com.cloudmachine.ui.repair.contract.RepairFinishContract;

import rx.Observable;

/**
 * Created by xiaojw on 2017/6/20.
 */

public class RepairFinishModel implements RepairFinishContract.Model{
    @Override
    public Observable<BOInfo> getBoInfo(long memberId, String orderNum) {
        return Api.getDefault(HostType.CLOUDM_HOST).getBOInfo(memberId,orderNum,"0").compose(RxHelper.<BOInfo>handleResult());
    }

    @Override
    public Observable<CWInfo> getCWInfo(long memberId, String orderNum) {
        return Api.getDefault(HostType.CLOUDM_HOST).getCWInfo(memberId,orderNum,"1").compose(RxHelper.<CWInfo>handleResult());
    }
}
