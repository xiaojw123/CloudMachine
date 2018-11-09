package com.cloudmachine.ui.home.presenter;

import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.bean.RepairDetail;
import com.cloudmachine.ui.home.contract.RepairDetailContract;
import com.cloudmachine.bean.SiteBean;
import com.cloudmachine.utils.ToastUtils;

/**
 * Created by xiaojw on 2017/8/2.
 */

public class RepairDetailPresenter extends RepairDetailContract.Preseneter {


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


    @Override
    public void getRepairDetail(String orderNo) {
        mRxManage.add(mModel.getRepairDetail(orderNo).subscribe(new RxSubscriber<RepairDetail>(mContext) {
            @Override
            protected void _onNext(RepairDetail detail) {
                mView.updateRepairDetail(detail);

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(mContext,message);

            }
        }));


    }

}
