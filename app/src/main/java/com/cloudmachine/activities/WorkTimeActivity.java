package com.cloudmachine.activities;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.net.api.ApiConstants;
import com.cloudmachine.ui.home.activity.fragment.StatisticsFragment;
import com.cloudmachine.ui.home.activity.fragment.WorkTimeFragment;
import com.cloudmachine.ui.homepage.activity.QuestionCommunityActivity;
import com.cloudmachine.utils.UMengKey;
import com.cloudmachine.widget.CommonTitleView;
import com.umeng.analytics.MobclickAgent;


/**
 * 项目名称：CloudMachine
 * 类描述：工作时长图表类页面
 * 创建人：shixionglu
 * 创建时间：2016/12/6 下午1:48
 * 修改人：shixionglu
 * 修改时间：2016/12/6 下午1:48
 * 修改备注：
 */

public class WorkTimeActivity extends BaseAutoLayoutActivity implements View.OnClickListener {
    CommonTitleView mTitleView;
    TextView workTimeTv;
    TextView statisticsTv;
    FrameLayout fragmentContainer;
    WorkTimeFragment workTimeFragment;
    StatisticsFragment statisticsFragment;
    RelativeLayout mGuideLayout;
    ImageView guide1_img1, guide1_img2;
    ImageView guide2_img1, guide2_img2, guide2_img3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worktime);
        initView();
    }

    private void initView() {
        mGuideLayout = (RelativeLayout) findViewById(R.id.worktime_guide_layout);
        guide1_img1 = (ImageView) findViewById(R.id.worktime_guide1_img1);
        guide1_img2 = (ImageView) findViewById(R.id.worktime_guide1_img2);
        guide2_img1 = (ImageView) findViewById(R.id.worktime_guide2_img1);
        guide2_img2 = (ImageView) findViewById(R.id.worktime_guide2_img2);
        guide2_img3 = (ImageView) findViewById(R.id.worktime_guide2_img3);
        mTitleView = (CommonTitleView) findViewById(R.id.worktime_title_ctv);
        workTimeTv = (TextView) findViewById(R.id.title_worktime_tv);
        statisticsTv = (TextView) findViewById(R.id.title_statistics_tv);
        fragmentContainer = (FrameLayout) findViewById(R.id.worktime_fragment_container);
        mTitleView.setRightImg(R.drawable.coupon_des_normal, rightImgClickListener);
        workTimeTv.setOnClickListener(this);
        statisticsTv.setOnClickListener(this);
        mGuideLayout.setOnClickListener(this);
        showFragment(workTimeTv);
    }

    public void showGuideView() {
        mGuideLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        if (workTimeFragment != null && workTimeFragment.isDailyPopVisible()) {
            workTimeFragment.dismissDailyPop();
        } else {
            super.onBackPressed();
        }

    }

    public void showFragment(TextView titleTv) {
        if (titleTv.isSelected()) {
            return;
        }
        titleTv.setSelected(true);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (workTimeFragment == null) {
            workTimeFragment = new WorkTimeFragment();
            ft.add(R.id.worktime_fragment_container, workTimeFragment);
        }
        if (statisticsFragment == null) {
            statisticsFragment = new StatisticsFragment();
            ft.add(R.id.worktime_fragment_container, statisticsFragment);
        }
        if (titleTv == workTimeTv) {//工时
            statisticsTv.setSelected(false);
            ft.hide(statisticsFragment);
            ft.show(workTimeFragment);
        } else if (titleTv == statisticsTv) {//统计
            workTimeTv.setSelected(false);
            ft.hide(workTimeFragment);
            ft.show(statisticsFragment);
        }
        ft.commit();
    }


    private View.OnClickListener rightImgClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MobclickAgent.onEvent(WorkTimeActivity.this, UMengKey.count_work_time_statistics_info);
            Intent statisticsIntent = new Intent(WorkTimeActivity.this, QuestionCommunityActivity.class);
            statisticsIntent.putExtra(QuestionCommunityActivity.H5_URL, ApiConstants.AppWorkTimeStatistics);
            startActivity(statisticsIntent);
        }
    };

    @Override
    public void initPresenter() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_worktime_tv:
            case R.id.title_statistics_tv:
                showFragment((TextView) v);
                break;
            case R.id.worktime_guide_layout:
                if (guide1_img1.getVisibility() == View.VISIBLE) {
                    guide1_img1.setVisibility(View.GONE);
                    guide1_img2.setVisibility(View.GONE);
                    guide2_img1.setVisibility(View.VISIBLE);
                    guide2_img2.setVisibility(View.VISIBLE);
                    guide2_img3.setVisibility(View.VISIBLE);
                } else {
                    mGuideLayout.setVisibility(View.GONE);
                }
                break;
        }

    }
}
