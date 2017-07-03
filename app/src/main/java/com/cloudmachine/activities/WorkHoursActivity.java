package com.cloudmachine.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.UMengKey;
import com.umeng.analytics.MobclickAgent;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2016/12/6 上午9:53
 * 修改人：shixionglu
 * 修改时间：2016/12/6 上午9:53
 * 修改备注：
 */

public class WorkHoursActivity extends BaseAutoLayoutActivity implements View.OnClickListener {

    private Context mContext;
    private View[] linearLayouts = new View[2];
    private ImageView[] imageViews = new ImageView[2];
    private TextView[] textViews = new TextView[2];
    private Fragment mFragments[] = new Fragment[2]; // 存储页面的数组
    private boolean isInitFragment[] = new boolean[2];
    private Fragment mContentFragment; // 当前fragment
    private int currentFragment = 0; // 当前Fragment的索引
    private long deviceId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_hours);
        mContext = this;
        getIntentData();
        initViews();
        initFragmentS(); // 初始化fragment
    }

    @Override
    public void initPresenter() {

    }

    private void getIntentData() {
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            try{
                deviceId = bundle.getLong(Constants.P_DEVICEID);

            }catch(Exception e){
                Constants.MyLog(e.getMessage());
            }

        }
    }

    public long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(long deviceId) {
        this.deviceId = deviceId;
    }

    private void initFragmentS() {

        mFragments[0] = new WorkTimeFragment();
        mFragments[1] = new StatisticsFragment();
        mContentFragment = null;
        switchContent(currentFragment);
    }

    private void initViews() {

        linearLayouts[0] = findViewById(R.id.tab_one_layout);
        linearLayouts[1] = findViewById(R.id.tab_two_layout);
        imageViews[0] = (ImageView) findViewById(R.id.tab_one_image);
        imageViews[1] = (ImageView) findViewById(R.id.tab_two_image);
        textViews[0] = (TextView) findViewById(R.id.tab_one_text);
        textViews[1] = (TextView) findViewById(R.id.tab_two_text);
        linearLayouts[0].setOnClickListener(this);
        linearLayouts[1].setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab_one_layout:
                switchContent(0);
                MobclickAgent.onEvent(mContext, UMengKey.count_work_time);
                break;
            case R.id.tab_two_layout:
                switchContent(1);
                MobclickAgent.onEvent(mContext,UMengKey.count_work_time_statistics);
                break;
        }
    }

    public void switchContent(int n) {

        currentFragment = n;
        int len = mFragments.length;
        if (n < len && n >= 0) {
            int size = imageViews.length;
            for (int i = 0; i < size; i++) {
                if (i == n) {
                    imageViews[i].setSelected(true);
                    linearLayouts[i].setSelected(false);
                    textViews[i].setTextColor(getResources().getColor(
                            R.color.main_bar_text_dw));
                } else {
                    imageViews[i].setSelected(false);
                    linearLayouts[i].setSelected(false);
                    textViews[i].setTextColor(getResources().getColor(
                            R.color.main_bar_text_nm));
                }
            }

            Fragment to = mFragments[n];
            if (mContentFragment != to) {
                FragmentTransaction fragmentTransaction = this
                        .getFragmentManager().beginTransaction();
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
//				if(mContentFragment == mFragments[3]){
//					Constants.MyLog("联网获取数据");													//联网获取报修历史数据
//					if (MemeberKeeper.getOauth(mContext) != null) {
//						new AllRepairHistoryAsync(mContext, mHandler).execute();
//					}
//				}
            }
        }
    }
}
