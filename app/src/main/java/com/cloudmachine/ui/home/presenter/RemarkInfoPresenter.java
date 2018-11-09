package com.cloudmachine.ui.home.presenter;

import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.bean.EmunBean;
import com.cloudmachine.ui.home.contract.RemarkInfoContract;
import com.cloudmachine.utils.ToastUtils;

import java.util.List;

/**
 * Created by xiaojw on 2017/6/8.
 */

public class RemarkInfoPresenter extends RemarkInfoContract.Presenter {
    @Override
    public void updateRoleListView() {
        mRxManage.add(mModel.getRoleList().subscribe(new RxSubscriber<List<EmunBean>>(mContext) {
            @Override
            protected void _onNext(List<EmunBean> emunBeen) {
                mView.returnRoleListView(emunBeen);
            }

            @Override
            protected void _onError(String message) {


            }
        }));


    }

    @Override
    public void updateRemarkInfo(int  groupId, long deviceId, int roleId, String remark) {
        mRxManage.add(mModel.updateRemarkInfo(groupId,deviceId,roleId,remark).subscribe(new RxSubscriber<String>(mContext) {
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
