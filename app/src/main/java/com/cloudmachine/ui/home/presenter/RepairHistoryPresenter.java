package com.cloudmachine.ui.home.presenter;

import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.struc.RepairListInfo;
import com.cloudmachine.ui.home.contract.RepairHistoryContract;

/**
 * Created by xiaojw on 2017/7/12.
 */

public class RepairHistoryPresenter extends RepairHistoryContract.Presenter {
    @Override
    public void getRepairList(long deviceId) {
        mRxManage.add(mModel.getRepairList(mContext,deviceId).subscribe(new RxSubscriber<RepairListInfo>(mContext,false) {
            @Override
            protected void _onNext(RepairListInfo info) {
                mView.returnGetRepairList(info);
            }

            @Override
            protected void _onError(String message) {
                mView.returnGetRepairError();
            }
        }));


    }

}
