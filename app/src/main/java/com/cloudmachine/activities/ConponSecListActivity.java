package com.cloudmachine.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.adapter.ValidCouponAdapter;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.ui.home.contract.MyCouponContract;
import com.cloudmachine.ui.home.model.CouponItem;
import com.cloudmachine.ui.home.model.MyCouponModel;
import com.cloudmachine.ui.home.presenter.MyCouponPresenter;
import com.cloudmachine.widget.CommonTitleView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConponSecListActivity extends BaseAutoLayoutActivity<MyCouponPresenter, MyCouponModel> implements MyCouponContract.View {
    public static final String COUPON_BASE_ID = "coupon_base_id";
    public static final String COUPON_AMOUNT = "amount";
    @BindView(R.id.available_toalamount_tv)
    TextView mToalAmountTv;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.empt_tv)
    TextView mEmptTv;
    @BindView(R.id.couponseclist_ctv)
    CommonTitleView mTitleView;
    ValidCouponAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_couponseclist);
        ButterKnife.bind(this);
        mRecyclerView.setVisibility(View.VISIBLE);
        ((ViewGroup) mToalAmountTv.getParent()).setVisibility(View.VISIBLE);
        mEmptTv.setVisibility(View.GONE);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ValidCouponAdapter(null, this);
        mRecyclerView.setAdapter(adapter);
        mTitleView.setTitleName(getCouponAmount() + "元代金券");
        mPresenter.getMyCouponDetailList(UserHelper.getMemberId(this), getCouponBaseId());
    }

    public int getCouponBaseId() {
        return getIntent().getIntExtra(COUPON_BASE_ID, 0);
    }

    public int getCouponAmount() {
        return getIntent().getIntExtra(COUPON_AMOUNT, 0);
    }


    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void updateAvaildCouponListView(int sumNum, int sumAmount, List<CouponItem> couponBOList) {

    }

    @Override
    public void updateInavaildCouponListView(int sumNum, List<CouponItem> couponBOList) {

    }

    @Override
    public void updateMyCouponDetailListView(int sumAmount, List<CouponItem> couponBOList) {
        mToalAmountTv.setText("总金额：" + sumAmount + "元");
        adapter.updateItems(couponBOList);
    }
}
