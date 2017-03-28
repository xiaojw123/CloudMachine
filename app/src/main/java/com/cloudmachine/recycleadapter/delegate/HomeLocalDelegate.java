package com.cloudmachine.recycleadapter.delegate;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.api.Api;
import com.cloudmachine.api.HostType;
import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.base.baserx.RxManager;
import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.cache.MySharedPreferences;
import com.cloudmachine.recyclerbean.HomeLocalBean;
import com.cloudmachine.recyclerbean.HomePageType;
import com.cloudmachine.struc.Member;
import com.cloudmachine.struc.ScoreInfo;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MemeberKeeper;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import static com.cloudmachine.R.id.sign_text;

/**
 * 项目名称：CloudMachine
 * 类描述：本地布局委托类
 * 创建人：shixionglu
 * 创建时间：2017/3/25 下午1:16
 * 修改人：shixionglu
 * 修改时间：2017/3/25 下午1:16
 * 修改备注：
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
        checkSignInfo();
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
                if (signBetweenTime == 0) {
                    rxManager.add(Api.getDefault(HostType.CLOUDM_HOST)
                            .getUserInsertSignInfo(String.valueOf(MemeberKeeper.getOauth(context).getId()))
                    .compose(RxHelper.<ScoreInfo>handleResult())
                    .subscribe(new RxSubscriber<ScoreInfo>(context,false) {
                        @Override
                        protected void _onNext(ScoreInfo scoreInfo) {
                            Constants.MyLog("进入");
                            if (null != scoreInfo) {
                                Member mb = MemeberKeeper.getOauth(context);
                                if (null != mb) {
                                    MySharedPreferences.setSharedPString(MySharedPreferences.key_score_update_time +
                                                    String.valueOf(mb.getId()),
                                            scoreInfo.getServerTime());
                                }
                            }
                        }
                        @Override
                        protected void _onError(String message) {

                        }
                    }));
                }
            }
        });
    }

    private void checkSignInfo() {

        rxManager.add(Api.getDefault(HostType.CLOUDM_HOST)
                .getUserScoreInfo(String.valueOf(MemeberKeeper.getOauth(context).getId()))
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
                        } else {
                            signBetweenTime = 1;
                        }
                        if (signBetweenTime != 0) {
                            signText.setText("签到");
                            Constants.MyLog("未签到");
                        } else {
                            signText.setText("已签到");
                            Constants.MyLog("已签到");
                        }
                    }
                    @Override
                    protected void _onError(String message) {

                    }
                }));
    }

}
