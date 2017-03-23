package com.cloudmachine.ui.repair.presenter;

import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.ui.repair.contract.NewRepairContract;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/3/22 下午3:14
 * 修改人：shixionglu
 * 修改时间：2017/3/22 下午3:14
 * 修改备注：
 */

public class NewRepairPresenter extends NewRepairContract.Presenter {

    @Override
    public void upLoadPhotoRequest(String filename) {
        mRxManage.add(mModel.upLoadPhoto(filename).subscribe(new RxSubscriber<String>(mContext) {
            @Override
            protected void _onNext(String s) {
                mView.returnUploadPhoto(s);
            }

            @Override
            protected void _onError(String message) {

            }
        }));
    }
}
