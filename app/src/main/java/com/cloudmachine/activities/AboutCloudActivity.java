package com.cloudmachine.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.autolayout.widgets.RadiusButtonView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.bean.VersionInfo;
import com.cloudmachine.helper.MobEvent;
import com.cloudmachine.net.api.ApiConstants;
import com.cloudmachine.net.task.GetVersionAsync;
import com.cloudmachine.ui.home.activity.HomeActivity;
import com.cloudmachine.ui.homepage.activity.QuestionCommunityActivity;
import com.cloudmachine.utils.CommonUtils;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.PermissionsChecker;
import com.cloudmachine.utils.ShareDialog;
import com.cloudmachine.utils.ToastUtils;
import com.cloudmachine.utils.UMengKey;
import com.cloudmachine.utils.VersionU;
import com.umeng.analytics.MobclickAgent;

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
    private boolean isAuto = false;
    private FrameLayout mFeedback;
    private boolean updateVersion = false;
    private FrameLayout mUseHelp, mShareApp;
    private String downLoadLink;
//    private Button envirBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_cloud);
        getIntentData();
        mContext = this;
        mHandler = new Handler(this);
        textView = (TextView) findViewById(R.id.version);
        textView.setText("V" + VersionU.getVersionName()+"("+VersionU.getVersionCode()+")");
        initView();
        new GetVersionAsync(mContext, mHandler).execute();
    }


    @Override
    public void initPresenter() {

    }

    private void initView() {

        mFeedback = (FrameLayout) findViewById(R.id.feedback_fl);
        mFeedback.setOnClickListener(this);
        mUseHelp = (FrameLayout) findViewById(R.id.use_help_fl);
        mUseHelp.setOnClickListener(this);
        mShareApp = (FrameLayout) findViewById(R.id.shareapp_fl);
        mShareApp.setOnClickListener(this);

    }


    @Override
    public boolean handleMessage(Message msg) {
        // TODO Auto-generated method stub
        switch (msg.what) {
            case Constants.HANDLER_GETVERSION_SUCCESS:
                VersionInfo vInfo = (VersionInfo) msg.obj;
                if (null != vInfo) {
                    boolean isUpdate = CommonUtils.checVersion(VersionU.getVersionName(), vInfo.getVersion());
                    if (isUpdate) {
                        Constants.updateVersion(this, mHandler,
                                vInfo.getMustUpdate(),vInfo.getMessage(),vInfo.getLink());
                    }

                }
                break;
            case Constants.HANDLER_GETVERSION_FAIL:
                Constants.MyToast((String) msg.obj,
                        getResources().getString(R.string.get_version_error));
                break;
            case Constants.HANDLER_VERSIONDOWNLOAD:
                downLoadLink=(String) msg.obj;
                PermissionsChecker checker = new PermissionsChecker(this);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checker.lacksPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    PermissionsActivity.startActivityForResult(this, HomeActivity.PEM_REQCODE_WRITESD,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE);
                } else {
                    Constants.versionDownload(this, downLoadLink);
                }
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
            }

        }
    }

    @Override
    protected void onResume() {
        //MobclickAgent.onPageStart(UMengKey.time_setting_aboat);
        super.onResume();
        MobclickAgent.onEvent(this, MobEvent.SETTING_ABOAT);
    }

    @Override
    protected void onPause() {
        //MobclickAgent.onPageEnd(UMengKey.time_setting_aboat);
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.feedback_fl:
                Constants.toActivity(AboutCloudActivity.this, SuggestBackActivity.class, null);
                break;
            case R.id.use_help_fl:
//                Constants.toActivity(AboutCloudActivity.this, UseHelpActivity.class, null);
                MobclickAgent.onEvent(this,MobEvent.TIME_H5_USE_HELP_PAGE);
                Bundle bundle=new Bundle();
                bundle.putString(QuestionCommunityActivity.H5_URL, ApiConstants.AppUseHelper);
                Constants.toActivity(AboutCloudActivity.this, QuestionCommunityActivity.class, bundle);
                break;
            case R.id.shareapp_fl:
                ShareDialog shareDialog = new ShareDialog(this, SESSIONURL, SESSIONTITLE, SESSIONDESCRIPTION, -1);
                shareDialog.show();
                MobclickAgent.onEvent(mContext, UMengKey.count_share_app);
                break;

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==PermissionsActivity.PERMISSIONS_DENIED){
            ToastUtils.showToast(this,"更新失败！！");
            CommonUtils.showPermissionDialog(this);
        }else{
            Constants.versionDownload(this, downLoadLink);
        }

    }

    //分享信息
    private static final String SESSIONTITLE = "云机械";
    private static final String SESSIONDESCRIPTION = "我的工程机械设备都在云机械APP，你的设备在哪里，赶紧加入吧！";
    private static final String SESSIONURL = "http://www.cloudm.com/yjx";

}
