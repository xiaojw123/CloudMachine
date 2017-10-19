package com.cloudmachine.ui.repair.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.activities.EvaluationActivity;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.bean.BOInfo;
import com.cloudmachine.bean.CWInfo;
import com.cloudmachine.bean.WorkSettleBean;
import com.cloudmachine.helper.MobEvent;
import com.cloudmachine.ui.home.activity.ConsumptionActivity;
import com.cloudmachine.ui.home.activity.PayDeviceInfoActivity;
import com.cloudmachine.ui.repair.contract.RepairFinishContract;
import com.cloudmachine.ui.repair.model.RepairFinishModel;
import com.cloudmachine.ui.repair.presenter.RepairFinishPresenter;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.widget.CommonTitleView;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RepairFinishDetailActivity extends BaseAutoLayoutActivity<RepairFinishPresenter, RepairFinishModel> implements RepairFinishContract.View, View.OnClickListener {

    @BindView(R.id.finish_deviceinfo_fl)
    FrameLayout finishDeviceinfoFl;
    @BindView(R.id.finish_consumption_tv)
    TextView finishConsumptionTv;
    @BindView(R.id.finish_coupon_tv)
    TextView finishCouponTv;
    @BindView(R.id.finish_workhours_tv)
    TextView finishWorkhoursTv;
    CWInfo mBoInfo;
    @BindView(R.id.finish_consumption_fl)
    FrameLayout finishConsumptionFl;
    @BindView(R.id.finish_titleview)
    CommonTitleView finishCtv;
    @BindView(R.id.finish_deviceinfo_amount)
    TextView amountTv;
    @BindView(R.id.part_discount_layout)
    RelativeLayout partlayout;
    @BindView(R.id.part_discount_amout)
    TextView partAmoutTv;
    @BindView(R.id.worktime_discount_layout)
    RelativeLayout workTimeLayout;
    @BindView(R.id.worktime_discount_amout)
    TextView worktimeAmoutTv;
    @BindView(R.id.ticket_discount_layout)
    RelativeLayout tickeLayout;
    @BindView(R.id.ticket_discount_amout)
    TextView ticketAmoutTv;


    String orderNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reparir_finsih_detail);
        ButterKnife.bind(this);
        MobclickAgent.onEvent(this, MobEvent.REPAIR_PAY_FINISHED);
        orderNum = getIntent().getStringExtra("orderNum");
        String flag = getIntent().getStringExtra("flag");
        finishCtv.setRightClickListener(this);
        mPresenter.updateRepairFinishDetail(orderNum, flag);
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void returnDetailView(BOInfo boInfo) {

    }

    @Override
    public void returnDetailView(CWInfo boInfo) {
        mBoInfo = boInfo;
        WorkSettleBean settleBean = boInfo.getWorkSettle();
        finishConsumptionTv.setText(settleBean.getNpartstotalamount());
        finishCouponTv.setText("-¥" + settleBean.getDsicountamount());
        finishWorkhoursTv.setText("¥" + settleBean.getNrepairworkhourcost());
        amountTv.setText("实付:¥ " + settleBean.getNpaidinamount());
        String ndiscounttotalamount = settleBean.getNdiscounttotalamount();
        if (!TextUtils.isEmpty(ndiscounttotalamount)) {
            double a1 = Double.parseDouble(ndiscounttotalamount);
            if (a1 > 0) {
                partlayout.setVisibility(View.VISIBLE);
                partAmoutTv.setText("-¥" + ndiscounttotalamount);
            }
        }
        String hourCost = settleBean.getNdiscountworkhourcost();
        if (!TextUtils.isEmpty(hourCost)) {
            double a2 = Double.parseDouble(hourCost);
            if (a2 > 0) {
                workTimeLayout.setVisibility(View.VISIBLE);
                worktimeAmoutTv.setText("-¥" + hourCost);
            }
        }
        String nmaxamount = settleBean.getNmaxamount();
        if (!TextUtils.isEmpty(nmaxamount)) {
            double a3 = Double.parseDouble(nmaxamount);
            if (a3 > 0) {
                tickeLayout.setVisibility(View.VISIBLE);
                ticketAmoutTv.setText("¥" + nmaxamount);
            }
        }


    }


    @OnClick({R.id.finish_deviceinfo_fl, R.id.finish_consumption_fl})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.common_titleview_right_tv:
                Bundle bundle0 = new Bundle();
                bundle0.putInt(EvaluationActivity.ACTION_TYPE, 1);
                bundle0.putString("orderNum", orderNum);
                Constants.toActivity(this, EvaluationActivity.class, bundle0);
                break;
            case R.id.finish_deviceinfo_fl:
                if (mBoInfo != null) {
                    Bundle bundle1 = new Bundle();
                    bundle1.putSerializable(Constants.WORK_DETAIL, mBoInfo.getWorkDetail());
                    bundle1.putStringArrayList(Constants.LOGO_LIST, mBoInfo.getLogoList());
                    Constants.toActivity(this, PayDeviceInfoActivity.class, bundle1);
                }
                break;
            case R.id.finish_consumption_fl:
                Bundle bundle2 = new Bundle();
                bundle2.putSerializable(Constants.CWINFO, mBoInfo);
                Constants.toActivity(this, ConsumptionActivity.class, bundle2);
                break;

        }

    }
}
