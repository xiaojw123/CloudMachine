package com.cloudmachine.ui.home.presenter;

import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.ui.home.contract.PayInfoContract;
import com.cloudmachine.utils.ToastUtils;

/**
 * Created by xiaojw on 2018/6/15.
 */

public class PayInfoPresenter extends PayInfoContract.Presenter {
    @Override
    public void getVerfyCode( int type, String mobile) {
        mRxManage.add(mModel.getVerfyCode(mobile,type).subscribe(new RxSubscriber<String>(mContext) {
            @Override
            protected void _onNext(String s) {
                mView.returnVerfyCode();
            }

            @Override
            protected void _onError(String message) {

            }
        }));

    }

}
