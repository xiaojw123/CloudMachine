package com.cloudmachine.ui.home.presenter;

import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.bean.BOInfo;
import com.cloudmachine.bean.CWInfo;
import com.cloudmachine.ui.home.contract.RepairDetailContract;
import com.cloudmachine.ui.home.model.SiteBean;
import com.cloudmachine.utils.ToastUtils;

/**
 * Created by xiaojw on 2017/8/2.
 */

public class RepairDetailPresenter extends RepairDetailContract.Preseneter {

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
    public void updateStationView(double lng, double lat) {
        mRxManage.add(mModel.getSiteStation(lng, lat).subscribe(new RxSubscriber<SiteBean>(mContext, false) {
            @Override
            protected void _onNext(SiteBean siteBean) {
                mView.returnStationView(siteBean);

            }

            @Override
            protected void _onError(String message) {
                mView.returnStationError();

            }
        }));

    }

}
