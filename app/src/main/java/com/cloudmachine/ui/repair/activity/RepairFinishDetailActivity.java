package com.cloudmachine.ui.repair.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.activities.EvaluationActivity;
import com.cloudmachine.adapter.PhotoListAdapter;
import com.cloudmachine.adapter.decoration.SpaceItemDecoration;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.bean.CWInfo;
import com.cloudmachine.bean.RepairDetail;
import com.cloudmachine.bean.WorkDetailBean;
import com.cloudmachine.helper.MobEvent;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.widget.CommonTitleView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RepairFinishDetailActivity extends BaseAutoLayoutActivity implements View.OnClickListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reparir_finsih_detail);
        ButterKnife.bind(this);
        orderNum = getIntent().getStringExtra(Constants.ORDER_NO);
        finishCtv.setRightClickListener(this);
        updateDetail();
    }


    private void updateDetail() {
        mRxManager.add(Api.getDefault(HostType.HOST_LARK).getRepairDetail(orderNum).compose(RxHelper.<RepairDetail>handleResult()).subscribe(new RxSubscriber<RepairDetail>(mContext) {
            @Override
            protected void _onNext(RepairDetail detail) {
                if (detail != null) {
                    amountTv.setText("实付:¥ " + detail.getActualPayAmount());
                    List imgList = detail.getAttachmentUrls();
                    if (imgList != null && imgList.size() > 0) {
                        imgTitleTv.setVisibility(View.VISIBLE);
                        infoImgRlv.setVisibility(View.VISIBLE);
                        infoImgRlv.setNestedScrollingEnabled(false);
                        infoImgRlv.setLayoutManager(new GridLayoutManager(mContext, 3));
                        infoImgRlv.addItemDecoration(new SpaceItemDecoration(mContext, 5));
                        PhotoListAdapter adapter = new PhotoListAdapter();
                        adapter.updateItems(imgList);
                        infoImgRlv.setAdapter(adapter);
                    }
                    //设置机器品牌
                    tvBrand.setText(detail.getBrandName());
                    //设置机器型号
                    tvModel.setText(detail.getModelName());
                    noTv.setText(detail.getRackId());
                    locTv.setText(detail.getAddressDetail());
                    desTv.setText(detail.getServiceDesc());
                }


            }

            @Override
            protected void _onError(String message) {

            }
        }));
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onEvent(this, MobEvent.REPAIR_PAY_FINISHED);
    }

    @Override
    public void initPresenter() {

    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.common_titleview_right_tv:
                Bundle bundle0 = new Bundle();
                bundle0.putInt(EvaluationActivity.ACTION_TYPE, 1);
                bundle0.putString(Constants.ORDER_NO, orderNum);
                Constants.toActivity(this, EvaluationActivity.class, bundle0);
                break;
        }

    }


}
