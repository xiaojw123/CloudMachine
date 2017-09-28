package com.cloudmachine.ui.home.presenter;

import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.bean.HomeBannerBean;
import com.cloudmachine.bean.UnReadMessage;
import com.cloudmachine.net.api.ApiConstants;
import com.cloudmachine.ui.home.contract.HomeContract;
import com.cloudmachine.ui.home.model.PopItem;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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
        mRxManage.add(mModel.getH5ConfigInfo().subscribe(new RxSubscriber<JsonObject>(mContext, false) {
            @Override
            protected void _onNext(JsonObject jsonObject) {
                if (jsonObject != null) {
//                    JSONObject pageJobj = jsonObject.get("pages");
                    JsonElement pageJelemnt = jsonObject.get("pages");
                    JsonObject reslutJob = pageJelemnt.getAsJsonObject();
                    if (reslutJob != null) {
                        ApiConstants.AppBoxDetail = reslutJob.get("AppBoxDetail").getAsString();
                        ApiConstants.AppCouponHelper = reslutJob.get("AppCouponHelper").getAsString();
                        ApiConstants.AppCommunity = reslutJob.get("AppCommunity").getAsString();
                        ApiConstants.AppASKQuestion = reslutJob.get("AppASKQuestion").getAsString();
                        ApiConstants.AppMyQuestion = reslutJob.get("AppMyQuestion").getAsString();
                        ApiConstants.AppUseHelper = reslutJob.get("AppUseHelper").getAsString();
                        ApiConstants.AppWorkTimeStatistics = reslutJob.get("AppWorkTimeStatistics").getAsString();
                        ApiConstants.AppOrderList = reslutJob.get("AppOrderList").getAsString();
                    }
                }


            }

            @Override
            protected void _onError(String message) {

            }
        }));
    }

    @Override
    public void getHomeBannerInfo() {
        mRxManage.add(mModel.getHomeBannerInfo().subscribe(new RxSubscriber<ArrayList<HomeBannerBean>>(mContext, false) {
            @Override
            protected void _onNext(ArrayList<HomeBannerBean> homeBannerBeen) {
                if (homeBannerBeen != null) {
                    mView.updateActivitySize(homeBannerBeen.size());
                }

            }

            @Override
            protected void _onError(String message) {
            }
        }));
    }

}
