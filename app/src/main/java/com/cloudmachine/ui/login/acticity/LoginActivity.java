package com.cloudmachine.ui.login.acticity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudmachine.R;
import com.cloudmachine.activities.FindPasswordActivity;
import com.cloudmachine.app.MyApplication;
import com.cloudmachine.autolayout.widgets.CircleTextImageView;
import com.cloudmachine.autolayout.widgets.RadiusButtonView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.cache.MySharedPreferences;
import com.cloudmachine.main.MainActivity;
import com.cloudmachine.net.task.LoginAsync;
import com.cloudmachine.struc.Member;
import com.cloudmachine.ui.login.contract.LoginContract;
import com.cloudmachine.ui.login.model.LoginModel;
import com.cloudmachine.ui.login.presenter.LoginPresenter;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MemeberKeeper;
import com.cloudmachine.utils.UMengKey;
import com.cloudmachine.utils.Utils;
import com.cloudmachine.utils.widgets.AppMsg;
import com.cloudmachine.utils.widgets.ClearEditTextView;
import com.cloudmachine.utils.widgets.ClearEditTextView.ICoallBack;
import com.cloudmachine.utils.widgets.Dialog.LoadingDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class LoginActivity extends BaseAutoLayoutActivity<LoginPresenter,LoginModel> implements OnClickListener, Callback ,LoginContract.View{

    private static final int MSG_SET_ALIAS = 1001;

    private Context mContext;
    private Handler mHandler;
    private ClearEditTextView password_ed;
    private ClearEditTextView username_ed;
    private RadiusButtonView login_btn;
    private LoadingDialog progressDialog;
    private TextView forget_pw_tv;
    private int flag = 2;
    private View left_layout, right_layout;
    private CircleTextImageView userImage;
    private RelativeLayout mPwdSwitch;
    //是否是明文显示 默认为密文
    private boolean isExpress = false;
    private ImageView mIvVisible,mIvUnVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        mHandler = new Handler(this);
        this.mContext = this;
        getIntentData();
        initView();
    }

    private void getIntentData() {

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
          flag = bundle.getInt("flag");
        }
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this,mModel);
    }


    private void show() {
        if (progressDialog == null) {
            progressDialog = LoadingDialog.createDialog(this);
        }
        progressDialog.setMessage("正在加载，请稍后");
        progressDialog.show();
    }

    private void disMiss() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    @Override
    protected void onResume() {
        //MobclickAgent.onPageStart(UMengKey.time_login);
        super.onResume();
    }


    @Override
    protected void onPause() {
        //MobclickAgent.onPageEnd(UMengKey.time_login);
        super.onPause();
    }

    void initView() {
        TextView weixinLogin = (TextView) findViewById(R.id.weixin_login);
        RelativeLayout wechatLogin = (RelativeLayout) findViewById(R.id.rl_weixin_login);
        wechatLogin.setOnClickListener(this);
        weixinLogin.setOnClickListener(this);
        userImage = (CircleTextImageView) findViewById(R.id.user_image);
        forget_pw_tv = (TextView) findViewById(R.id.forget_pw_tv);
        left_layout = findViewById(R.id.left_layout);
        right_layout = findViewById(R.id.right_layout);
        left_layout.setOnClickListener(this);
        right_layout.setOnClickListener(this);
        //切换密码相对布局
        mPwdSwitch = (RelativeLayout) findViewById(R.id.rl_switch);
        mPwdSwitch.setOnClickListener(this);
        mIvVisible = (ImageView) findViewById(R.id.iv_visible);
        mIvUnVisible = (ImageView) findViewById(R.id.unvisible);

        username_ed = (ClearEditTextView) findViewById(R.id.username_ed);
        password_ed = (ClearEditTextView) findViewById(R.id.password_ed);
        switchPwd(isExpress);

        username_ed.setICoallBack(new ICoallBack() {

            @Override
            public void onClickButton(boolean b) {
                // TODO Auto-generated method stub
                if (b) {
                    String url = MySharedPreferences.getSharedPString(MySharedPreferences.key_user_image_ + username_ed.getText().toString().trim());
                    if (null != url && url.length() > 0)
                        ImageLoader.getInstance().displayImage(url, userImage, Utils.displayImageOptions);
                }
            }
        });
        login_btn = (RadiusButtonView) findViewById(R.id.login_btn);
        login_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(mContext, UMengKey.count_login);
                // TODO Auto-generated method stub
                doCheck();
            }
        });
        forget_pw_tv.setOnClickListener(this);
        /*person_regist.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it = new Intent(LoginActivity.this,InputPhoneNum.class);
				it.putExtra("flag", 1);
				startActivity(it);
				Constants.toActivity(LoginActivity.this,InputPhoneNum.class, null,true);
			}
		});*/
//		backView.setOnClickListener(this);
        //regidter_tv.setOnClickListener(this);
    }


    /**
     * 登录信息的验证
     */
    private void doCheck() {
        String usernameString = username_ed.getText().toString();
        String passwordString = password_ed.getText().toString().trim();
        // if(!isMobileNO(usernameString)){
        if (usernameString.length() < 11 || !Constants.isMobileNO(usernameString)) {
            username_ed.setShakeAnimation();
            AppMsg appMsg = AppMsg.makeText(this, "请正确输入你的账号", AppMsg.STYLE_CO);
            appMsg.setLayoutGravity(Gravity.TOP);
            appMsg.show();
            return;
        }
        if (passwordString.length() < 6) {
            password_ed.setShakeAnimation();
            AppMsg appMsg = AppMsg.makeText(this, "请正确输入你的密码", AppMsg.STYLE_CO);
            appMsg.setLayoutGravity(Gravity.TOP);
            appMsg.show();
            return;
        }
        show();
        new LoginAsync(usernameString, passwordString, mContext, mHandler).execute();

    }


    @Override
    public void onClick(View v) {
        Intent it = null;
        switch (v.getId()) {
            case R.id.left_layout:
                finish();
                break;
            case R.id.right_layout:
                MobclickAgent.onEvent(mContext, UMengKey.count_register);
                it = new Intent(this, FindPasswordActivity.class);
                it.putExtra("type", 3);
                startActivity(it);
                break;
            case R.id.forget_pw_tv:
                MobclickAgent.onEvent(mContext, UMengKey.count_forgotpassword);
                it = new Intent(this, FindPasswordActivity.class);
                it.putExtra("type", 1);
                startActivity(it);
                break;
            case R.id.rl_switch:
                isExpress = !isExpress;
                switchPwd(isExpress);
                break;
            case R.id.weixin_login:
                loginToWeiXin();
                break;
            case R.id.rl_weixin_login:
                loginToWeiXin();
                break;
            default:
                break;
        }
    }

    //跳转微信登录授权
    private void loginToWeiXin() {
        IWXAPI mApi = WXAPIFactory.createWXAPI(this,Constants.APP_ID, true);
        mApi.registerApp(Constants.APP_ID);
        if (mApi != null && mApi.isWXAppInstalled()) {
            SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "wechat_sdk_demo_test_neng";
            mApi.sendReq(req);
            finish();
        } else
            Toast.makeText(this, "用户未安装微信", Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN && flag == 1) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            LoginActivity.this.finish();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            LoginActivity.this.finish();
        }
        return true;
    }

    @Override
    public boolean handleMessage(Message msg) {
        // TODO Auto-generated method stub
        switch (msg.what) {
            case MSG_SET_ALIAS:
                JPushInterface.setAliasAndTags(getApplicationContext(), (String) msg.obj, null, mAliasCallback);
                break;
            case Constants.HANDLER_LOGIN_SUCCESS:
                disMiss();
                Constants.isGetScore = true;
                Member member = (Member) msg.obj;
                Constants.MyLog("云机械登录获取到的账号信息"+member.toString());
                MemeberKeeper.saveOAuth(member, LoginActivity.this);
                MyApplication.getInstance().setLogin(true);
                MyApplication.getInstance().setFlag(true);
                if (flag == 2) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else if (flag == 1) {
                }

                MySharedPreferences.setSharedPInt(MySharedPreferences.key_login_type,0);

                LoginActivity.this.finish();
                Constants.isMcLogin = true;
                //调用JPush API设置Alias
                mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, member.getId() + ""));
                MobclickAgent.onProfileSignIn(String.valueOf(member.getId()));
                break;
            case Constants.HANDLER_LOGIN_FAIL:
                disMiss();
                String message = (String) msg.obj;
                AppMsg appMsg = AppMsg.makeText(LoginActivity.this,
                        message + "", AppMsg.STYLE_INFO);
                appMsg.setLayoutGravity(Gravity.TOP);
                appMsg.show();
                break;
        }
        return false;
    }

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    break;

                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
//                Log.i(TAG, logs);
//                if (ExampleUtil.isConnected(getApplicationContext())) {
//                	mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
//                } else {
//                	Log.i(TAG, "No network");
//                }
                    break;

                default:
//                logs = "Failed with errorCode = " + code;
//                Log.e(TAG, logs);
            }

//            ExampleUtil.showToast(logs, getApplicationContext());
        }

    };

    public void switchPwd(boolean isExpress) {
        if (isExpress) {
            password_ed.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            mIvVisible.setVisibility(View.VISIBLE);
            mIvUnVisible.setVisibility(View.GONE);
        } else {
            password_ed.setTransformationMethod(PasswordTransformationMethod.getInstance());
            mIvVisible.setVisibility(View.GONE);
            mIvUnVisible.setVisibility(View.VISIBLE);
        }
    }
}
