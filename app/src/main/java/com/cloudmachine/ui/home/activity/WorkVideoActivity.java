package com.cloudmachine.ui.home.activity;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.base.baserx.RxSchedulers;
import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.bean.ScreenInfo;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.ui.home.activity.fragment.WorkPicFragment;
import com.cloudmachine.ui.home.activity.fragment.WorkVideoFragment;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.DensityUtil;
import com.cloudmachine.utils.ToastUtils;
import com.cloudmachine.widget.CommonTitleView;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WorkVideoActivity extends BaseAutoLayoutActivity {

    @BindView(R.id.work_video_ctv)
    CommonTitleView workVideoCtv;
    @BindView(R.id.work_pic_tv)
    TextView workPicTv;
    @BindView(R.id.work_video_tv)
    TextView workVideoTv;
    @BindView(R.id.work_video_tab)
    LinearLayout tabContainer;


    WorkVideoFragment mVideoFragment = new WorkVideoFragment();
    WorkPicFragment mPicFragment = new WorkPicFragment();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_video);
        ButterKnife.bind(this);
        boolean hasVideo=getIntent().getBooleanExtra(Constants.HAS_VIDEO,false);
        if (hasVideo){
            tabContainer.setVisibility(View.VISIBLE);
        }else{
            tabContainer.setVisibility(View.GONE);
        }
        workVideoCtv.setRightClickListener(rightClickListener);
        showFragment(workPicTv);
    }



    private View.OnClickListener rightClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mPicFragment.isVisible()) {
                mPicFragment.flushData();
            } else {
                mVideoFragment.flushData();
            }
        }
    };


    @Override
    public void initPresenter() {
    }

    @OnClick({R.id.work_pic_tv, R.id.work_video_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.work_pic_tv:
            case R.id.work_video_tv:
                showFragment(view);
                break;
        }
    }

    public void showFragment(View view) {
        if (view.isSelected()) {
            return;
        }
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        view.setSelected(true);
        if (view == workVideoTv) {
            workVideoCtv.setTitleName(getResources().getString(R.string.work_video));
            workPicTv.setSelected(false);
            if (mPicFragment.isVisible()) {
                ft.hide(mPicFragment);
            }
            if (mVideoFragment.isAdded()) {
                ft.show(mVideoFragment);
            } else {
                ft.add(R.id.work_video_fragment_cotainer, mVideoFragment);
            }
        } else if (view == workPicTv) {
            workVideoCtv.setTitleName(getResources().getString(R.string.work_pic));
            workVideoTv.setSelected(false);
            if (mVideoFragment.isVisible()) {
                ft.hide(mVideoFragment);
            }
            if (mPicFragment.isAdded()) {
                ft.show(mPicFragment);
            } else {
                ft.add(R.id.work_video_fragment_cotainer, mPicFragment);
            }
        }
        ft.commit();
    }


}
