package com.cloudmachine.ui.repair.presenter;

import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.bean.AllianceDetail;
import com.cloudmachine.bean.BOInfo;
import com.cloudmachine.bean.CWInfo;
import com.cloudmachine.ui.repair.contract.RepairFinishContract;
import com.cloudmachine.utils.ToastUtils;

/**
 * Created by xiaojw on 2017/6/20.
 */

public class RepairFinishPresenter extends RepairFinishContract.Presenter {
    @Override
    public void updateRepairFinishDetail(String orderNum, String flag) {
        if ("0".equals(flag)) {
            mRxManage.add(mModel.getBoInfo(mContext, orderNum,flag).subscribe(new RxSubscriber<BOInfo>(mContext, false) {
                @Override
                protected void _onNext(BOInfo boInfo) {
                    mView.returnDetailView(boInfo);
                }

                @Override
                protected void _onError(String message) {
                    ToastUtils.showToast(mContext,message);
                }
            }));

        }else if ("1".equals(flag)){
            mRxManage.add(mModel.getCWInfo(mContext,orderNum,flag).subscribe(new RxSubscriber<CWInfo>(mContext,false) {
                @Override
                protected void _onNext(CWInfo cwInfo) {
                    mView.returnDetailView(cwInfo);
                }

                @Override
                protected void _onError(String message) {
                    ToastUtils.showToast(mContext,message);

                }
            }));


        }
    }

    @Override
    public void updateAllianceDetail(long memberId, String orderNo) {
        mRxManage.add(mModel.getAllianceOrderDetail(memberId,orderNo).subscribe(new RxSubscriber<AllianceDetail>(mContext) {
            @Override
            protected void _onNext(AllianceDetail detail) {
                mView.returnAllianceDetail(detail);
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(mContext,message);

            }
        }));
    }
}
