package com.cloudmachine.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.net.api.ApiConstants;
import com.cloudmachine.ui.homepage.activity.QuestionCommunityActivity;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.widget.CommonTitleView;

import butterknife.BindView;
import butterknife.ButterKnife;
/*删除旧版卡券相关*/
public class ViewCouponActivityNew extends BaseAutoLayoutActivity implements View.OnClickListener {

    @BindView(R.id.coupon_title_view)
    CommonTitleView mTitleView;
    @BindView(R.id.coupon__title_valid_tv)
    TextView validTitleTv;
    @BindView(R.id.coupon_title_invalid_tv)
    TextView invalidTitleTv;
    @BindView(R.id.coupon_fragment_cotainer)
    FrameLayout fragmentCotainer;
    Fragment validFragmet, invaildFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewcoupon_new);
        ButterKnife.bind(this);
        mTitleView.setRightImg(R.drawable.coupon_description_selector, rightImgClickListener);
        validTitleTv.setOnClickListener(this);
        invalidTitleTv.setOnClickListener(this);
        initFragment(validTitleTv);
    }
    private View.OnClickListener  rightImgClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Bundle bundle=new Bundle();
            bundle.putString(QuestionCommunityActivity.H5_URL, ApiConstants.AppCouponHelper);
            Constants.toActivity(ViewCouponActivityNew.this,QuestionCommunityActivity.class,bundle);
        }
    };
    public void setAvaidNum(int num){
        validTitleTv.setText("可用("+num+")");
    }
    public void setInvaildNum(int num){

        invalidTitleTv.setText("不可用("+num+")");
    }

    public void initFragment(View view) {
        if (view.isSelected()) {
            return;
        }
        view.setSelected(true);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (validFragmet == null) {
            validFragmet = new AvailableCouponsFragment();
        }
        if (invaildFragment == null) {
            invaildFragment = new InvalidCouponFragment();
        }
        if (!validFragmet.isAdded()){
            ft.add(R.id.coupon_fragment_cotainer,validFragmet);
        }
        if (!invaildFragment.isAdded()){
            ft.add(R.id.coupon_fragment_cotainer,invaildFragment);
        }
        if (view == validTitleTv) {
            invalidTitleTv.setSelected(false);
            ft.show(validFragmet);
            ft.hide(invaildFragment);
        } else if (view == invalidTitleTv) {
            validTitleTv.setSelected(false);
            ft.show(invaildFragment);
            ft.hide(validFragmet);
        }
        ft.commit();
    }


    @Override
    public void initPresenter() {

    }

    @Override
    public void onClick(View v) {
        initFragment(v);
    }
}
