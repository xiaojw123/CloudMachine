package com.cloudmachine.ui.home.presenter;

import com.cloudmachine.net.api.ApiConstants;
import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.bean.UnReadMessage;
import com.cloudmachine.ui.home.contract.HomeContract;
import com.cloudmachine.ui.home.model.PopItem;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaojw on 2017/5/22.
 */

public class HomePresenter extends HomeContract.Presenter {


    @Override
    public void updateUnReadMessage(long memberId) {
        mRxManage.add(mModel.getMessageUntreatedCount(memberId).subscribe(new RxSubscriber<UnReadMessage>(mContext, false) {
            @Override
            protected void _onNext(UnReadMessage unReadMessage) {
                if (unReadMessage == null) {
                    return;
                }
                mView.updateMessageCount(unReadMessage.getCount());
            }

            @Override
            protected void _onError(String message) {

            }
        }));


    }

    @Override
    public void getPromotionInfo(long memberId) {


        mRxManage.add(mModel.getPromotionModel(memberId).subscribe(new RxSubscriber<List<PopItem>>(mContext, false) {
            @Override
            protected void _onNext(List<PopItem> items) {
                ArrayList<PopItem> beanList = (ArrayList<PopItem>) items;
                mView.updatePromotionInfo(beanList);
            }

            @Override
            protected void _onError(String message) {

            }
        }));

    }

    @Override
    public void getH5ConfigInfo() {
        mRxManage.add(mModel.getH5ConfigInfo().subscribe(new RxSubscriber<JSONObject>(mContext, false) {
            @Override
            protected void _onNext(JSONObject jsonObject) {
                if (jsonObject != null) {
                    JSONObject pageJobj = jsonObject.optJSONObject("pages");
                    if (pageJobj != null) {
                        ApiConstants.AppBoxDetail = pageJobj.optString("AppBoxDetail");
                        ApiConstants.AppCouponHelper = pageJobj.optString("AppCouponHelper");
                        ApiConstants.AppCommunity = pageJobj.optString("AppCommunity");
                        ApiConstants.AppASKQuestion = pageJobj.optString("AppASKQuestion");
                        ApiConstants.AppMyQuestion = pageJobj.optString("AppMyQuestion");
                        ApiConstants.AppUseHelper = pageJobj.optString("AppUseHelper");
                        ApiConstants.AppWorkTimeStatistics = pageJobj.optString("AppWorkTimeStatistics");
                        ApiConstants.AppOrderList = pageJobj.optString("AppOrderList");
                    }
                }


            }

            @Override
            protected void _onError(String message) {

            }
        }));
    }

}
