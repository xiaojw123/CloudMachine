package com.cloudmachine.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudmachine.BuildConfig;
import com.cloudmachine.R;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.bean.VersionInfo;
import com.cloudmachine.helper.MobEvent;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.ApiConstants;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.ui.home.activity.HostConfigActivity;
import com.cloudmachine.ui.home.activity.QuestionCommunityActivity;
import com.cloudmachine.utils.CommonUtils;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.ShareDialog;
import com.cloudmachine.utils.UMengKey;
import com.cloudmachine.utils.VersionU;
import com.umeng.analytics.MobclickAgent;

/**
 * 关于与帮助页面
 */
public class AboutCloudActivity extends BaseAutoLayoutActivity implements OnClickListener {

    int clickTime, selPos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_cloud);
        TextView textView = (TextView) findViewById(R.id.version);
        String reVersion = "";
        if (TextUtils.equals(ApiConstants.LARK_HOST, ApiConstants.ONLINE_HOST1)) {
            selPos = 0;
            reVersion = BuildConfig.RELEASE_TIME;
        } else if (TextUtils.equals(ApiConstants.LARK_HOST, ApiConstants.PRE_HOST1)) {
            selPos = 1;
            reVersion = "218_" + BuildConfig.RELEASE_TIME;
        } else if (TextUtils.equals(ApiConstants.LARK_HOST, ApiConstants.LOCAL_HOST1)) {
            selPos = 2;
            reVersion = "109_" + BuildConfig.RELEASE_TIME;
        } else if (TextUtils.equals(ApiConstants.LARK_HOST, ApiConstants.INTEFACE_HOST1)) {
            selPos = 3;
            reVersion = "179_" + BuildConfig.RELEASE_TIME;
        } else {
            reVersion = "un";
        }
        textView.setText("V" + VersionU.getVersionName() + "." + reVersion);
        if (BuildConfig.DEBUG) {
            textView.setOnClickListener(this);
        }
        initView();
    }


    @Override
    public void initPresenter() {

    }

    private void initView() {

        FrameLayout mFeedback = (FrameLayout) findViewById(R.id.feedback_fl);
        mFeedback.setOnClickListener(this);
        FrameLayout mUseHelp = (FrameLayout) findViewById(R.id.use_help_fl);
        mUseHelp.setOnClickListener(this);
        FrameLayout mShareApp = (FrameLayout) findViewById(R.id.shareapp_fl);
        mShareApp.setOnClickListener(this);

    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onEvent(this, MobEvent.SETTING_ABOAT);
        checkVersionUpdate();
    }

    private void checkVersionUpdate() {
        mRxManager.add(Api.getDefault(HostType.HOST_LARK).getVersion(Constants.ANDROID, VersionU.getVersionName()).compose(RxHelper.<VersionInfo>handleResult()).subscribe(new RxSubscriber<VersionInfo>(mContext) {
            @Override
            protected void _onNext(VersionInfo info) {
                if (null != info) {
                    boolean isCanUpdate = CommonUtils.checVersion(VersionU.getVersionName(), info.getVersion());
                    if (isCanUpdate) {
                        Constants.updateVersion(mContext,
                                info.getMustUpdate(), info.getMessage(), info.getLink());
                    }
                }
            }

            @Override
            protected void _onError(String message) {

            }
        }));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.feedback_fl:
                Bundle fBundle = new Bundle();
                fBundle.putString(QuestionCommunityActivity.H5_URL, ApiConstants.AppFeedback);
                Constants.toActivity(AboutCloudActivity.this, QuestionCommunityActivity.class, fBundle);
                break;
            case R.id.use_help_fl:
                MobclickAgent.onEvent(this, MobEvent.TIME_H5_USE_HELP_PAGE);
                Bundle bundle = new Bundle();
                bundle.putString(QuestionCommunityActivity.H5_URL, ApiConstants.AppUseHelper);
                Constants.toActivity(AboutCloudActivity.this, QuestionCommunityActivity.class, bundle);
                break;
            case R.id.shareapp_fl:
                ShareDialog shareDialog = new ShareDialog(this, SESSIONURL, SESSIONTITLE, SESSIONDESCRIPTION, -1);
                shareDialog.show();
                MobclickAgent.onEvent(mContext, UMengKey.count_share_app);
                break;
            case R.id.version:
                clickTime++;
                if (clickTime == 2) {
                    clickTime = 0;
                    Bundle data=new Bundle();
                    data.putInt(BuildConfig.BUILD_TYPE,selPos);
                    Constants.toActivity(this, HostConfigActivity.class, data);
                }
                break;

        }
    }

    //分享信息
    private static final String SESSIONTITLE = "云机械";
    private static final String SESSIONDESCRIPTION = "我的工程机械设备都在云机械APP，你的设备在哪里，赶紧加入吧！";
    private static final String SESSIONURL = "http://www.cloudm.com/yjx";

}
