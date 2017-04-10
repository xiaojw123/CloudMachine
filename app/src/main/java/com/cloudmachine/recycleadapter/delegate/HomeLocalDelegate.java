package com.cloudmachine.recycleadapter.delegate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.api.Api;
import com.cloudmachine.api.HostType;
import com.cloudmachine.base.baserx.RxBus;
import com.cloudmachine.base.baserx.RxManager;
import com.cloudmachine.base.baserx.RxSchedulers;
import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.cache.MySharedPreferences;
import com.cloudmachine.recyclerbean.HomeLocalBean;
import com.cloudmachine.recyclerbean.HomePageType;
import com.cloudmachine.struc.Member;
import com.cloudmachine.struc.ScoreInfo;
import com.cloudmachine.ui.homepage.activity.InsuranceActivity;
import com.cloudmachine.ui.login.acticity.LoginActivity;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MemeberKeeper;
import com.cloudmachine.utils.ToastUtils;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import rx.functions.Action1;

import static com.cloudmachine.R.id.sign_text;
import static com.cloudmachine.app.MyApplication.mContext;

/**
 * 项目名称：CloudMachine
 * 类描述：本地布局委托类
 * 创建人：shixionglu
 * 创建时间：2017/3/25 下午1:16
 * 修改人：shixionglu
 * 修改时间：2017/3/25 下午1:16
 * 修改备注：
 */

/**
 * 进入此页面先判断是否登录，若登录则判断签到状态，若未签到点击签到后跳出对话框，刷新页面  若已签到则不响应点击
 */

public class HomeLocalDelegate implements ItemViewDelegate<HomePageType> {

    private RxManager rxManager;
    private int       signBetweenTime;
    private TextView  signText;//签到字段信息
    private Context   context;

    @Override
    public int getItemViewLayoutId() {
        return R.layout.delegate_item_homelocal;
    }

    @Override
    public boolean isForViewType(HomePageType item, int position) {
        return item instanceof HomeLocalBean;
    }

    @Override
    public void convert(ViewHolder holder, HomePageType homePageType, int position) {
        context = holder.getConvertView().getContext();
        signText = (TextView) holder.getView(sign_text);
        rxManager = new RxManager();
        //监听刷新状态()
        rxManager.on(Constants.SIGN_OR_NOTSIGN, new Action1<Integer>() {

            @Override
            public void call(Integer integer) {
                Constants.MyLog("接受到刷新事件");
                signBetweenTime = integer;
                if (integer != 0) {
                    signText.setText("签到");
                } else {
                    signText.setText("已签到");
                }
                RxBus.getInstance().post(Constants.REFRESH_SIGN_STATE, "");
            }
        });

        // TODO: 2017/3/30 暂时注释掉
        LinearLayout llShoppingCenter = (LinearLayout) holder.getView(R.id.ll_shoppingcenter);
        //商城点击事件
        llShoppingCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //车险报价点击事件
        LinearLayout llInsurance = (LinearLayout) holder.getView(R.id.ll_Insurance);
        llInsurance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, InsuranceActivity.class);
                context.startActivity(intent);
            }
        });

        //问答点击事件
        LinearLayout llQuestions = (LinearLayout) holder.getView(R.id.questions);
        llQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //报修点击事件
        LinearLayout llRepair = (LinearLayout) holder.getView(R.id.repair);
        llRepair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //签到点击事件
        LinearLayout llSign = (LinearLayout) holder.getView(R.id.sign);
        llSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkSignInfo();
            }
        });
    }


    private void checkSignInfo() {
        if (MemeberKeeper.getOauth(mContext) == null) {
            Intent intent = new Intent(mContext, LoginActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("flag", 2);
            intent.putExtras(bundle);
            mContext.startActivity(intent);
        } else {
            sign();
        }

    }

    private void sign() {
        Constants.MyLog("签到时间状态值"+signBetweenTime);
        if (signBetweenTime != 0) {
            rxManager.add(Api.getDefault(HostType.CLOUDM_HOST)
                    .getUserInsertSignInfo(String.valueOf(MemeberKeeper.getOauth(context).getId()))
                    .compose(RxSchedulers.<BaseRespose>io_main()).subscribe(new RxSubscriber<BaseRespose>(context, false) {
                        @Override
                        protected void _onNext(BaseRespose baseRespose) {
                            if (baseRespose.code == 800) {
                                signText.setText("已签到");
                                ScoreInfo scoreInfo = (ScoreInfo) baseRespose.result;
                                Member mb = MemeberKeeper.getOauth(context);
                                if (null != mb) {
                                    Constants.MyLog("设置时间");
                                    MySharedPreferences.setSharedPString(MySharedPreferences.key_score_update_time +
                                                    String.valueOf(mb.getId()),
                                            scoreInfo.getServerTime());
                                }

                            } else if (baseRespose.code == 200) {
                                ToastUtils.info("您已经签到过", true);
                                signText.setText("已签到");
                            }
                            RxBus.getInstance().post(Constants.REFRESH_SIGN_STATE, "");
                        }

                        @Override
                        protected void _onError(String message) {

                        }
                    }));
        }

    }

}
















/*Subscription subscribe = Api.getDefault(HostType.CLOUDM_HOST)
                    .getUserScoreInfo(MemeberKeeper.getOauth(context).getId())
                    .compose(RxHelper.<ScoreInfo>handleResult())
                    .subscribe(new RxSubscriber<ScoreInfo>(context, false) {
                        @Override
                        protected void _onNext(ScoreInfo scoreInfo) {
                            if (null != scoreInfo
                                    && !TextUtils.isEmpty(scoreInfo.getServerTime())
                                    && null != MemeberKeeper.getOauth(context)) {
                                //上一次签到的时间
                                String oldTime = MySharedPreferences
                                        .getSharedPString(MySharedPreferences.key_score_update_time
                                                + String.valueOf(MemeberKeeper.getOauth(
                                                context).getId()));
                                //签到时间间隔
                                signBetweenTime = Constants.getDateDays(Constants
                                                .changeDateFormat(scoreInfo.getServerTime(),
                                                        Constants.DateFormat2, Constants.DateFormat1),
                                        Constants.changeDateFormat(oldTime,
                                                Constants.DateFormat2, Constants.DateFormat1),
                                        Constants.DateFormat1);
                                if (oldTime == null) {
                                    signBetweenTime = 1;
                                }
                            } else {
                                signBetweenTime = 1;
                            }
                            if (signBetweenTime != 0) {
                                signText.setText("签到");
                            } else {
                                signText.setText("已签到");
                            }
                            //执行签到
                            sign();
                            RxBus.getInstance().post(Constants.REFRESH_SIGN_STATE, "");
                        }

                        @Override
                        protected void _onError(String message) {

                        }
                    });
            //subscribe.unsubscribe();*/