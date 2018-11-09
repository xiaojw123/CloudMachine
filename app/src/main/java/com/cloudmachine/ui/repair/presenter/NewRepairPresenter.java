package com.cloudmachine.ui.repair.presenter;

import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.bean.LarkDeviceDetail;
import com.cloudmachine.bean.NewRepairInfo;
import com.cloudmachine.ui.repair.contract.NewRepairContract;
import com.google.gson.JsonObject;

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
    public void getWarnMessage(String tel, final NewRepairInfo info) {
        mRxManage.add(mModel.getWarnMessage(tel).subscribe(new RxSubscriber<JsonObject>(mContext) {
            @Override
            protected void _onNext(JsonObject jsonObject) {
                if (jsonObject != null) {
                    mView.returnGetWarnMessage(info, jsonObject.get("warningMessage").getAsString());
                } else {
                    mView.returnGetWarnMessage(info, null);
                }
            }

            @Override
            protected void _onError(String message) {
                mView.returnGetWarnMessage(info, null);
            }
        }));

    }

    @Override
    public void getLocactionInfo(long deviceId) {
        mRxManage.add(mModel.getLocactionInfo(deviceId).subscribe(new RxSubscriber<LarkDeviceDetail>(mContext) {
            @Override
            protected void _onNext(LarkDeviceDetail detail) {
                if (detail!=null){
                    mView.returnLocatDetail(detail.getLocation());
                }else{
                    mView.returnLocatDetail(null);
                }
            }

            @Override
            protected void _onError(String message) {
                mView.returnLocatDetail(null);



            }
        }));

    }
}
