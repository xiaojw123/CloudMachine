package com.cloudmachine.ui.home.presenter;

import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.bean.ElectronicFenceBean;
import com.cloudmachine.ui.home.contract.MapOneContract;
import com.cloudmachine.utils.ToastUtils;
import com.nostra13.universalimageloader.cache.disc.impl.TotalSizeLimitedDiscCache;

/**
 * Created by xiaojw on 2018/9/19.
 */

public class MapOnePresenter extends MapOneContract.Presenter {
    @Override
    public void getElecFence(long deviceId) {
        mRxManage.add(mModel.getElecFence(deviceId).subscribe(new RxSubscriber<ElectronicFenceBean>(mContext) {
            @Override
            protected void _onNext(ElectronicFenceBean electronicFenceBean) {
                  mView.updateFence(electronicFenceBean);

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(mContext,message);

            }
        }));


    }

    @Override
    public void addElecFence(double lat, double lng, String fenceRadium, int fenceType, long deviceId) {
        mRxManage.add(mModel.addElecFence(lat,lng,fenceRadium,fenceType,deviceId).subscribe(new RxSubscriber<String>(mContext) {
            @Override
            protected void _onNext(String s) {
                mView.addFenceSuccess();
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(mContext,message);

            }
        }));


    }

    @Override
    public void deleteElecFence(long deviceId, int type) {
        mRxManage.add(mModel.deleteElecFence(deviceId,type).subscribe(new RxSubscriber<String>(mContext) {
            @Override
            protected void _onNext(String s) {
                mView.deleteFenceSuccess();
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(mContext,message);

            }
        }));

    }
}
