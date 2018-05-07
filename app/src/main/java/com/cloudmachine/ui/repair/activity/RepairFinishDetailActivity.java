package com.cloudmachine.ui.repair.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.activities.EvaluationActivity;
import com.cloudmachine.adapter.PhotoListAdapter;
import com.cloudmachine.adapter.decoration.SpaceItemDecoration;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.bean.AllianceDetail;
import com.cloudmachine.bean.BOInfo;
import com.cloudmachine.bean.CWInfo;
import com.cloudmachine.bean.WorkDetailBean;
import com.cloudmachine.bean.WorkSettleBean;
import com.cloudmachine.helper.MobEvent;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.ui.repair.contract.RepairFinishContract;
import com.cloudmachine.ui.repair.model.RepairFinishModel;
import com.cloudmachine.ui.repair.presenter.RepairFinishPresenter;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.widget.CommonTitleView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RepairFinishDetailActivity extends BaseAutoLayoutActivity<RepairFinishPresenter, RepairFinishModel> implements RepairFinishContract.View, View.OnClickListener {

    @BindView(R.id.finish_consumption_tv)
    TextView finishConsumptionTv;
    @BindView(R.id.finish_coupon_tv)
    TextView finishCouponTv;
    CWInfo mBoInfo;
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
    @BindView(R.id.tv_device_brand)
    TextView tvBrand;
    @BindView(R.id.tv_device_model)
    TextView tvModel;
    @BindView(R.id.pay_di_des)
    TextView desTv;
    @BindView(R.id.pay_di_dno)
    TextView noTv;
    @BindView(R.id.pay_di_loc)
    TextView locTv;
    @BindView(R.id.device_info_rlv)
    RecyclerView infoImgRlv;
    @BindView(R.id.device_info_title)
    TextView imgTitleTv;
    String orderNum;
    boolean isAlliance;
    String tel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reparir_finsih_detail);
        ButterKnife.bind(this);
        tel = getIntent().getStringExtra("tel");// 评价人电话
        orderNum = getIntent().getStringExtra("orderNum");
        isAlliance = getIntent().getBooleanExtra("isAlliance", false);
        String flag = getIntent().getStringExtra("flag");
        finishCtv.setRightClickListener(this);
        if (isAlliance) {
            if (UserHelper.isLogin(this)) {
                mPresenter.updateAllianceDetail(UserHelper.getMemberId(this), orderNum);
            }
        } else {
            mPresenter.updateRepairFinishDetail(orderNum, flag);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onEvent(this, MobEvent.REPAIR_PAY_FINISHED);
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
        updateDeviceInfo(mBoInfo.getWorkDetail(), mBoInfo.getLogoList(), null);
    }

    @Override
    public void returnAllianceDetail(AllianceDetail detail) {
        CWInfo info = new CWInfo();
        WorkSettleBean settleBean = new WorkSettleBean();
        settleBean.setNpaidinamount(detail.getActualAmount());
        WorkDetailBean detailBean = new WorkDetailBean();
        detailBean.setVbrandname(detail.getBrandName());
        detailBean.setVmaterialname(detail.getModelName());
        detailBean.setVmachinenum(detail.getMachineNum());
        detailBean.setVworkaddress(detail.getAddressDetail());
        detailBean.setCusdemanddesc(detail.getDemandDescription());
        info.setWorkSettle(settleBean);
        info.setWorkDetail(detailBean);
        List<String> urls = detail.getAttachmentUrls();
        if (urls != null && urls.size() > 0) {
            info.setLogoList((ArrayList<String>) urls);
        }
        returnDetailView(info);
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.common_titleview_right_tv:
                Bundle bundle0 = new Bundle();
                bundle0.putInt(EvaluationActivity.ACTION_TYPE, 1);
                bundle0.putString("orderNum", orderNum);
                bundle0.putBoolean("isAlliance", isAlliance);
                bundle0.putString("tel", tel);
                Constants.toActivity(this, EvaluationActivity.class, bundle0);
                break;
//            case R.id.finish_deviceinfo_fl:
//                if (mBoInfo != null) {
//                    Bundle bundle1 = new Bundle();
//                    bundle1.putSerializable(Constants.WORK_DETAIL, mBoInfo.getWorkDetail());
//                    bundle1.putStringArrayList(Constants.LOGO_LIST, mBoInfo.getLogoList());
//                    Constants.toActivity(this, PayDeviceInfoActivity.class, bundle1);
//                }
//                break;
//            case R.id.finish_consumption_fl:
//                Bundle bundle2 = new Bundle();
//                bundle2.putSerializable(Constants.CWINFO, mBoInfo);
//                Constants.toActivity(this, ConsumptionActivity.class, bundle2);
//                break;

        }

    }

    public void updateDeviceInfo(WorkDetailBean bean, ArrayList<String> logoList, String flag) {
        //刷新耗件详情列表
        if (bean == null) {
            return;
        }
        if (logoList != null && logoList.size() > 0) {
            imgTitleTv.setVisibility(View.VISIBLE);
            infoImgRlv.setVisibility(View.VISIBLE);
            infoImgRlv.setNestedScrollingEnabled(false);
            infoImgRlv.setLayoutManager(new GridLayoutManager(this, 3));
            infoImgRlv.addItemDecoration(new SpaceItemDecoration(this, 5));
            PhotoListAdapter adapter = new PhotoListAdapter();
            adapter.updateItems(logoList);
            infoImgRlv.setAdapter(adapter);
        }
        //设置机器品牌
        tvBrand.setText(bean.getVbrandname());
        //设置机器型号
        tvModel.setText(bean.getVmaterialname());
//        rdDevicenameTv.setText(detailBean.getVbrandname());
//        rdDevicenoTv.setText(detailBean.getVmaterialname());
//        rdDescriptionTv.setText(detailBean.getVdiscription());

        noTv.setText(bean.getVmachinenum());
        locTv.setText(bean.getVworkaddress());
        String description;
        if ("0".equals(flag)) {
            description = bean.getVdiscription();
        } else {
            description = bean.getCusdemanddesc();
        }
        desTv.setText(description);


    }


}
