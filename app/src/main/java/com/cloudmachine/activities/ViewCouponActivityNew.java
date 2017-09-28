package com.cloudmachine.activities;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.widget.CommonTitleView;

import butterknife.BindView;

public class ViewCouponActivityNew extends BaseAutoLayoutActivity {

    @BindView(R.id.coupon_title_view)
    CommonTitleView mTitleView;
    @BindView(R.id.coupon__title_valid_tv)
    TextView  validTitleTv;
    @BindView(R.id.coupon_title_invalid_tv)
    TextView invalidTitleTv;
    @BindView(R.id.coupon_fragment_cotainer)
    FrameLayout fragmentCotainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewcoupon_new);

    }

    @Override
    public void initPresenter() {

    }
}
