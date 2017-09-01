package com.cloudmachine.activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.net.api.ApiConstants;
import com.cloudmachine.ui.homepage.activity.QuestionCommunityActivity;
import com.cloudmachine.widget.CommonTitleView;

/**
 * 项目名称：CloudMachine
 * 类描述：查看卡券页面
 * 创建人：shixionglu
 * 创建时间：2017/2/22 上午9:39
 * 修改人：shixionglu
 * 修改时间：2017/2/22 上午9:39
 * 修改备注：
 */

public class ViewCouponActivity extends BaseAutoLayoutActivity implements View.OnClickListener {

    private Context mContext;
    private  View[]     linearLayouts    = new View[2];
    private TextView[] textViews        = new TextView[2];
    private Fragment mFragments[]     = new Fragment[2]; // 存储页面的数组
    private  boolean    isInitFragment[] = new boolean[2];
    private Fragment mContentFragment; // 当前fragment
    private int currentFragment = 0; // 当前Fragment的索引
    private CommonTitleView titleLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewcoupon);
        mContext = this;
        initViews();
        initFragmentS(); // 初始化fragment
    }

    @Override
    public void initPresenter() {

    }

    private void initFragmentS() {

        mFragments[0] = new AvailableCouponsFragment();
        mFragments[1] = new InvalidCouponFragment();
        mContentFragment = null;
        switchContent(currentFragment);
    }

    private void switchContent(int n) {

        currentFragment = n;
        int len = mFragments.length;
        if (n < len && n >= 0) {
           // int size = imageViews.length;
            for (int i = 0; i < len; i++) {
                if (i == n) {
                   // imageViews[i].setSelected(true);
                    linearLayouts[i].setSelected(false);
                    textViews[i].setTextColor(getResources().getColor(
                            R.color.cor8));
                } else {
                   // imageViews[i].setSelected(false);
                    linearLayouts[i].setSelected(false);
                    textViews[i].setTextColor(getResources().getColor(
                            R.color.cor10));
                }
            }

            Fragment to = mFragments[n];
            if (mContentFragment != to) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                if (mContentFragment != null) {
                    if (!to.isAdded()) { // 先判断是否被add过
                        fragmentTransaction.hide(mContentFragment)
                                .add(R.id.frame_content, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
                    } else {
                        fragmentTransaction.hide(mContentFragment).show(to)
                                .commit(); // 隐藏当前的fragment，显示下一个
                        if (to == mFragments[1]) {
                            to.onResume();
                        }
                    }
                } else {
                    fragmentTransaction.add(R.id.frame_content, to).commit();
                }
                mContentFragment = to;
            }
        }
    }

    private void initViews() {
        linearLayouts[0] = findViewById(R.id.tab_one_layout);
        linearLayouts[1] = findViewById(R.id.tab_two_layout);
        textViews[0] = (TextView) findViewById(R.id.tab_one_text);
        textViews[1] = (TextView) findViewById(R.id.tab_two_text);
        linearLayouts[0].setOnClickListener(this);
        linearLayouts[1].setOnClickListener(this);
        titleLayout = (CommonTitleView) findViewById(R.id.title_layout);
        titleLayout.setRightImg(R.drawable.coupon_description_selector, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, QuestionCommunityActivity.class);
                intent.putExtra(QuestionCommunityActivity.H5_TITLE,"说明");
                intent.putExtra(QuestionCommunityActivity.H5_URL, ApiConstants.AppCouponHelper);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab_one_layout:
                switchContent(0);
                break;
            case R.id.tab_two_layout:
                switchContent(1);
                break;
        }

    }
}
