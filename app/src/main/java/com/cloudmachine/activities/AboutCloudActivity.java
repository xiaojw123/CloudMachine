package com.cloudmachine.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.autolayout.widgets.RadiusButtonView;
import com.cloudmachine.autolayout.widgets.TitleView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.net.task.GetVersionAsync;
import com.cloudmachine.struc.VersionInfo;
import com.cloudmachine.utils.CommonUtils;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.VersionU;

import cn.jpush.android.api.JPushInterface;

/**
 * 关于与帮助页面
 */
public class AboutCloudActivity extends BaseAutoLayoutActivity implements
        Callback, OnClickListener {

    private Context mContext;
    private Handler mHandler;
    private RadiusButtonView button1;
    private TextView textView;
    // private ImageView backImg;
    private TitleView title_layout_about;
    private boolean isAuto = false;
    private RelativeLayout mFeedback;
    private RelativeLayout mCheckUpdate;
    private TextView mTvUpdateIcon;
    private TextView mTvCanbeUpdate;
    private boolean updateVersion = false;
    private RelativeLayout mUseHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_cloud);
        getIntentData();
        mContext = this;
        mHandler = new Handler(this);
        initTitleLayout();
        // backImg = (ImageView)findViewById(R.id.backImg);

        textView = (TextView) findViewById(R.id.version);

        /*button1 = (RadiusButtonView) findViewById(R.id.checkBtn);
        button1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new GetVersionAsync(mContext, mHandler).execute();
            }
        });*/


        // backImg.setOnClickListener(this);
        String isTest = "";
        if (Constants.URL_MAIN.indexOf("test") != -1) {
            isTest = "测试服务器";
        }
        textView.setText("Android版    " + VersionU.getVersionName() + " "
                + isTest + "版本");
        /*if (isAuto) {
            new GetVersionAsync(mContext, mHandler).execute();
        }*/
        new GetVersionAsync(mContext, mHandler).execute();
        initView();
    }

    @Override
    public void initPresenter() {

    }

    private void initView() {

        mFeedback = (RelativeLayout) findViewById(R.id.feedback);
        mFeedback.setOnClickListener(this);
        mCheckUpdate = (RelativeLayout) findViewById(R.id.check_version);
        mCheckUpdate.setOnClickListener(this);
        mTvUpdateIcon = (TextView) findViewById(R.id.update_icon);
        mTvCanbeUpdate = (TextView) findViewById(R.id.tv_canbe_update);
        mUseHelp = (RelativeLayout) findViewById(R.id.use_help);
        mUseHelp.setOnClickListener(this);
    }

    private void initTitleLayout() {
        title_layout_about = (TitleView) findViewById(R.id.title_layout_about);
        title_layout_about.setTitle(getResources().getString(R.string.about_title_text));
        title_layout_about.setLeftOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
    }

    @Override
    public boolean handleMessage(Message msg) {
        // TODO Auto-generated method stub
        switch (msg.what) {
            case Constants.HANDLER_GETVERSION_SUCCESS:
                VersionInfo vInfo = (VersionInfo) msg.obj;
                if (null != vInfo) {
                    if (vInfo.getMustUpdate() == -1) {
                        mTvUpdateIcon.setVisibility(View.GONE);
                        mTvCanbeUpdate.setVisibility(View.GONE);
                        Constants.MyToast(vInfo.getMessage());
                    } else if (vInfo.getMustUpdate() == 0) {
                        boolean isUpdate = CommonUtils.checVersion(VersionU.getVersionName(), vInfo.getVersion());
                        if (isUpdate) {
                            mTvUpdateIcon.setVisibility(View.VISIBLE);
                            mTvCanbeUpdate.setVisibility(View.VISIBLE);
                            if (updateVersion) {
                                Constants.updateVersion(AboutCloudActivity.this, mHandler,
                                        vInfo.getMessage(), vInfo.getLink());
                            }
                        }
                    }
                }
                break;
            case Constants.HANDLER_GETVERSION_FAIL:
                Constants.MyToast((String) msg.obj,
                        getResources().getString(R.string.get_version_error));
                break;
            case Constants.HANDLER_VERSIONDOWNLOAD:
                Constants
                        .versionDownload(AboutCloudActivity.this, (String) msg.obj);
                Constants.MyToast((String) msg.obj,
                        getResources().getString(R.string.version_downloading));
                finish();
                break;
        }
        return false;
    }




    private void getIntentData() {
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            try {
                String ectra = bundle.getString(JPushInterface.EXTRA_EXTRA);
                if (!TextUtils.isEmpty(ectra)) {
                    isAuto = true;
                }
            } catch (Exception e) {
                Constants.MyLog(e.getMessage());
            }

        }
    }

    @Override
    protected void onResume() {
        //MobclickAgent.onPageStart(UMengKey.time_setting_aboat);
        super.onResume();
    }

    @Override
    protected void onPause() {
        //MobclickAgent.onPageEnd(UMengKey.time_setting_aboat);
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.feedback:
                Constants.toActivity(AboutCloudActivity.this, SuggestBackActivity.class, null);
                break;
            case R.id.check_version:
                new GetVersionAsync(mContext, mHandler).execute();
                updateVersion = true;
                break;
            case R.id.use_help:
                Constants.toActivity(AboutCloudActivity.this, UseHelpActivity.class, null);
                break;
            default:
                break;

        }
    }
}
