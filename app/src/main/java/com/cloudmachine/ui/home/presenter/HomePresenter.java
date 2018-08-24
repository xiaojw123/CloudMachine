package com.cloudmachine.ui.home.presenter;

import android.text.TextUtils;

import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.bean.ForceVBean;
import com.cloudmachine.bean.HomeBannerBean;
import com.cloudmachine.bean.QiToken;
import com.cloudmachine.chart.utils.AppLog;
import com.cloudmachine.helper.QiniuManager;
import com.cloudmachine.net.api.ApiConstants;
import com.cloudmachine.ui.home.contract.HomeContract;
import com.cloudmachine.ui.home.model.PopItem;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;

/**
 * Created by xiaojw on 2017/5/22.
 */

public class HomePresenter extends HomeContract.Presenter {




    @Override
    public void updateUnReadMessage(long memberId) {
        mRxManage.add(mModel.getMessageUntreatedCount(memberId).subscribe(new RxSubscriber<String>(mContext, false) {
            @Override
            protected void _onNext(String unReadMessage) {
                AppLog.print("updateUnReadMessage____onNext___" + unReadMessage);
                if (TextUtils.isEmpty(unReadMessage)) {
                    return;
                }
                mView.updateMessageCount(Integer.parseInt(unReadMessage));
            }

            @Override
            protected void _onError(String message) {

            }
        }));


    }

    @Override
    public void getPromotionInfo(final long memberId) {


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
                        JsonElement j1 = reslutJob.get("AppBoxDetail");
                        if (j1!=null&&!j1.isJsonNull()) {
                            ApiConstants.AppBoxDetail = j1.getAsString();
                        }
                        JsonElement j2 = reslutJob.get("AppCouponHelper");
                        if (j2!=null&&!j2.isJsonNull()) {
                            ApiConstants.AppCouponHelper = j2.getAsString();
                        }
                        JsonElement j3 = reslutJob.get("AppCommunity");
                        if (j3!=null&&!j3.isJsonNull()) {
                            ApiConstants.AppCommunity = j3.getAsString();
                        }
                        JsonElement j4 = reslutJob.get("AppASKQuestion");
                        if (j4!=null&&!j4.isJsonNull()) {
                            ApiConstants.AppASKQuestion = j4.getAsString();
                        }
                        JsonElement j5 = reslutJob.get("AppMyQuestion");
                        if (j5!=null&&!j5.isJsonNull()) {
                            ApiConstants.AppMyQuestion = j5.getAsString();
                        }
                        JsonElement j6 = reslutJob.get("AppUseHelper");
                        if (j6!=null&&!j6.isJsonNull()) {
                            ApiConstants.AppUseHelper = j6.getAsString();
                        }
                        JsonElement j7 = reslutJob.get("AppWorkTimeStatistics");
                        if (j7!=null&&!j7.isJsonNull()) {
                            ApiConstants.AppWorkTimeStatistics = j7.getAsString();
                        }
                        JsonElement j8 = reslutJob.get("AppOrderList");
                        if (j8!=null&&!j8.isJsonNull()) {
                            ApiConstants.AppOrderList = j8.getAsString();
                        }
                        JsonElement j9 = reslutJob.get("AppWalletHelper");
                        if (j9!=null&&!j9.isJsonNull()) {
                            ApiConstants.AppWalletHelper = j9.getAsString();
                        }
                        JsonElement j10 = reslutJob.get("AppMachineKnowledge");
                        if (j10!=null&&!j10.isJsonNull()) {
                            ApiConstants.AppMachineKnowledge = j10.getAsString();
                        }
                        JsonElement j11 = reslutJob.get("AppFeedback");
                        if (j11!=null&&!j11.isJsonNull()) {
                            ApiConstants.AppFeedback = j11.getAsString();
                        }
                        JsonElement j12=reslutJob.get("AppWagesLoan");
                        if (j12!=null&&!j12.isJsonNull()){
                            ApiConstants.AppWagesLoan=j12.getAsString();
                        }
                        JsonElement j13=reslutJob.get("AppQR");
                        if (j13!=null&&!j13.isJsonNull()){
                            ApiConstants.AppQR=j13.getAsString();
                        }
                        JsonElement  j14=reslutJob.get("APPForgetPassword");
                        if (j14!=null&&!j14.isJsonNull()){
                            ApiConstants.APPForgetPassword=j14.getAsString();
                        }
                        JsonElement  j15=reslutJob.get("APPFwtk");
                        if (j15!=null&&!j15.isJsonNull()){
                            ApiConstants.APPFwtk=j15.getAsString();
                        }
                        JsonElement j16=reslutJob.get("APPRzgj");
                        if (j16!=null&&!j16.isJsonNull()){
                            ApiConstants.APPRzgj=j16.getAsString();
                        }
                        JsonElement j17=reslutJob.get("APPBmxy");
                        if (j17!=null&&!j17.isJsonNull()){
                            ApiConstants.APPBmxy=j17.getAsString();
                        }
                        JsonElement j18=reslutJob.get("APPSjyysxy");
                        if (j18!=null&&!j18.isJsonNull()){
                            ApiConstants.APPSjyysxy=j18.getAsString();
                        }

                    }
                }
                mView.updateH5View();

            }

            @Override
            protected void _onError(String message) {
                mView.updateH5View();

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

    @Override
    public void initQinuParams() {
        mRxManage.add(mModel.initQinuParams().subscribe(new RxSubscriber<QiToken>(mContext) {
            @Override
            protected void _onNext(QiToken token) {
                if (token != null) {
                    AppLog.print("token__" + token.getUptoken() + ", origin__" + token.getOrigin());
                    QiniuManager.origin = token.getOrigin();
                    QiniuManager.uptoken = token.getUptoken();

                }
            }

            @Override
            protected void _onError(String message) {
                AppLog.print("initQinuParams onError__" + message);

            }
        }));
    }

    @Override
    public void forceUpdate() {
        mRxManage.add(mModel.forceUpdate().subscribe(new RxSubscriber<ForceVBean>(mContext) {
            @Override
            protected void _onNext(ForceVBean forceVBean) {
                if (forceVBean != null) {
                    if ((!TextUtils.isEmpty(forceVBean.getVersion()))) {

                    }
                } else {

                }
            }

            @Override
            protected void _onError(String message) {

            }
        }));

    }

    @Override
    public void getCountByStatus(long memberId, int status) {
        mRxManage.add(mModel.getCountByStatus(memberId, status).subscribe(new RxSubscriber<Integer>(mContext) {
            @Override
            protected void _onNext(Integer integer) {
                if (integer > 0) {
                    mView.updateOrderView(true);
                } else {
                    mView.updateOrderView(false);
                }
            }

            @Override
            protected void _onError(String message) {

            }
        }));


    }

}
