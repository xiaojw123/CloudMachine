package com.cloudmachine.ui.repair.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.adapter.ValidCouponAdapter;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.helper.MobEvent;
import com.cloudmachine.ui.home.model.CouponItem;
import com.cloudmachine.ui.home.model.OrderCouponBean;
import com.cloudmachine.widget.CommonTitleView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/4/6 上午10:23
 * 修改人：shixionglu
 * 修改时间：2017/4/6 上午10:23
 * 修改备注：
 */

public class ViewRepairActivity extends BaseAutoLayoutActivity {
    public static final String ORDER_COUPON = "order_coupon";
    public static final String KEY_IS_USED = "key_is_used";
    private static final String FORMAT_DISCOUONT = "总优惠：%s元";
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.vp_empt_tv)
    TextView mEmptTv;
    @BindView(R.id.available_toalamount_tv)
    TextView mAmoutTv;
    @BindView(R.id.viewcoupon_no_use)
    CheckBox mNoUseCb;
    @BindView(R.id.viewcoupon_use)
    CheckBox mUseCb;
    @BindView(R.id.viewcoupon_ctv)
    CommonTitleView mTitleView;
    @BindView(R.id.vp_content_view)
    ViewGroup vpContentLayout;


    String useCouponsId = "";


    private ValidCouponAdapter mValidCouponAdapter;
    int discountAmout;
    List<CouponItem> useItems = new ArrayList<>();
    List<CouponItem> toalItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewrepair);
        ButterKnife.bind(this);
        mContext = this;
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onEvent(this, MobEvent.REPAIR_PAY_COUPON);
    }

    @Override
    public void initPresenter() {

    }

    public String getOrderNum() {
        return getIntent().getStringExtra("orderNum");
    }


    private void initView() {
        OrderCouponBean bean = getIntent().getParcelableExtra(ORDER_COUPON);
        mTitleView.setRightClickListener(titleRightClickListener);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        toalItems = bean.getCouponList();
        if (toalItems != null && toalItems.size() > 0) {
            vpContentLayout.setVisibility(View.VISIBLE);
            mEmptTv.setVisibility(View.GONE);
            discountAmout = bean.getUseAmount();
            for (CouponItem item : toalItems) {
                int useNum = item.getUseNum();
                if (useNum > 0) {
                    useItems.add(item);
                    if (useCouponsId.contains("_")) {
                        useCouponsId += ",";
                    }
                    useCouponsId += item.getCouponBaseId() + "_" + useNum;
                }
            }
            if (isCouponUsed()) {
                mAmoutTv.setText(String.format(FORMAT_DISCOUONT, discountAmout));
                mNoUseCb.setChecked(false);
                mUseCb.setChecked(true);
                mValidCouponAdapter = new ValidCouponAdapter(useItems, ViewRepairActivity.this);
            } else {
                mAmoutTv.setText(String.format(FORMAT_DISCOUONT, 0));
                mNoUseCb.setChecked(true);
                mUseCb.setChecked(false);
                mValidCouponAdapter = new ValidCouponAdapter(toalItems, ViewRepairActivity.this);
            }
            mRecyclerView.setAdapter(mValidCouponAdapter);
        } else {
            vpContentLayout.setVisibility(View.GONE);
            mEmptTv.setVisibility(View.VISIBLE);
        }
    }

    private boolean isCouponUsed() {
        return getIntent().getBooleanExtra(KEY_IS_USED, false);
    }

    private View.OnClickListener titleRightClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            if (mNoUseCb.isChecked()) {
                intent.putExtra(KEY_IS_USED, false);
            } else {
                intent.putExtra(KEY_IS_USED, true);
                intent.putExtra("couponId", useCouponsId);
                intent.putExtra("amount", discountAmout);
            }
            setResult(RESULT_OK, intent);
            finish();
        }
    };


    @OnClick({R.id.viewcoupon_no_use, R.id.viewcoupon_use})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.viewcoupon_no_use:
                if (mNoUseCb.isChecked()) {
                    mUseCb.setChecked(false);
                    mAmoutTv.setText(String.format(FORMAT_DISCOUONT, 0));
                    mValidCouponAdapter.setUseAble(false);
                    mValidCouponAdapter.updateItems(toalItems);
                } else {
                    mUseCb.setChecked(true);
                    mAmoutTv.setText(String.format(FORMAT_DISCOUONT, 0));
                    mValidCouponAdapter.setUseAble(true);
                    mValidCouponAdapter.updateItems(useItems);
                }
                break;
            case R.id.viewcoupon_use:
                if (mUseCb.isChecked()) {
                    mNoUseCb.setChecked(false);
                    mAmoutTv.setText(String.format(FORMAT_DISCOUONT, discountAmout));
                    mValidCouponAdapter.setUseAble(true);
                    mValidCouponAdapter.updateItems(useItems);
                } else {
                    mNoUseCb.setChecked(true);
                    mAmoutTv.setText(String.format(FORMAT_DISCOUONT, 0));
                    mValidCouponAdapter.setUseAble(false);
                    mValidCouponAdapter.updateItems(toalItems);
                }
                break;
        }
    }
}
