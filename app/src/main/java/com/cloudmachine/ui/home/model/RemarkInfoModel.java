package com.cloudmachine.ui.home.model;

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
    public Observable<List<RoleBean>> getRoleList() {

        return Api.getDefault(HostType.HOST_CLOUDM_YJX).getRoleList().compose(RxHelper.<List<RoleBean>>handleResult());
    }

    @Override
    public Observable<String> updateRemarkInfo(long fid, long memberId, long deviceId, String remark, long roleId) {
        return Api.getDefault(HostType.HOST_CLOUDM_YJX).updateMemberRemark(fid,memberId,deviceId,remark,roleId).compose(RxHelper.<String>handleResult());
    }
}
