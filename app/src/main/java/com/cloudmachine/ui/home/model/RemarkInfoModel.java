package com.cloudmachine.ui.home.model;

import com.cloudmachine.bean.EmunBean;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.ui.home.contract.RemarkInfoContract;

import java.util.List;

import rx.Observable;

/**
 * Created by xiaojw on 2017/6/8.
 */

public class RemarkInfoModel implements RemarkInfoContract.Model {
    @Override
    public Observable<List<EmunBean>> getRoleList() {
        return Api.getDefault(HostType.HOST_LARK).getEnum("roleType").compose(RxHelper.<List<EmunBean>>handleResult());
    }

    @Override
    public Observable<String> updateRemarkInfo(int groupId, long deviceId, int roleId, String remark) {
        return Api.getDefault(HostType.HOST_LARK).updateDeviceMemberRemark(groupId,deviceId,roleId,remark).compose(RxHelper.<String>handleResult());
    }

}
