package com.cloudmachine.ui.home.presenter;

import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.ui.home.contract.RemarkInfoContract;
import com.cloudmachine.ui.home.model.RoleBean;
import com.cloudmachine.utils.ToastUtils;

import java.util.List;

/**
 * Created by xiaojw on 2017/6/8.
 */

public class RemarkInfoPresenter extends RemarkInfoContract.Presenter {
    @Override
    public void updateRoleListView() {
        mRxManage.add(mModel.getRoleList().subscribe(new RxSubscriber<List<RoleBean>>(mContext,false) {
            @Override
            protected void _onNext(List<RoleBean> roleBeen) {
                mView.returnRoleListView(roleBeen);
            }

            @Override
            protected void _onError(String message) {

            }
        }));
    }

    @Override
    public void updateRemarkInfo(long fid, long memberId, long deviceId, String remark, long roleId) {
//        mRxManage.add(mModel.updateRemarkInfo(fid,memberId,deviceId,remark,roleId).subscribe(new RxSubscriber<JSONObject>(mContext,false) {
//            @Override
//            protected void _onNext(JSONObject jsonObjectBaseRespose) {
//                mView.updateRemarkSuccess();
//            }
//
//            @Override
//            protected void _onError(String message) {
////                ToastUtils.showToast(mContext,message);
//                mView.updateRemarkFailed();
//            }
//        }));
        mRxManage.add(mModel.updateRemarkInfo(fid,memberId,deviceId,remark,roleId).subscribe(new RxSubscriber<String>(mContext,false) {
            @Override
            protected void _onNext(String result) {
                mView.updateRemarkSuccess();
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(mContext,message);
                mView.updateRemarkFailed();
            }
        }));

    }

}
