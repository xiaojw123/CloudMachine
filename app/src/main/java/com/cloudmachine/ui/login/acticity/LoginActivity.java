package com.cloudmachine.ui.login.acticity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cloudmachine.MyApplication;
import com.cloudmachine.R;
import com.cloudmachine.autolayout.widgets.CircleTextImageView;
import com.cloudmachine.autolayout.widgets.RadiusButtonView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.base.baserx.RxSchedulers;
import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.bean.Member;
import com.cloudmachine.bean.UserInfo;
import com.cloudmachine.cache.MySharedPreferences;
import com.cloudmachine.chart.utils.AppLog;
import com.cloudmachine.helper.MobEvent;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.ApiConstants;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.net.task.LoginAsync;
import com.cloudmachine.ui.home.activity.HomeActivity;
import com.cloudmachine.ui.homepage.activity.QuestionCommunityActivity;
import com.cloudmachine.ui.login.contract.LoginContract;
import com.cloudmachine.ui.login.model.LoginModel;
import com.cloudmachine.ui.login.presenter.LoginPresenter;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MemeberKeeper;
import com.cloudmachine.utils.ToastUtils;
import com.cloudmachine.utils.Utils;
import com.cloudmachine.utils.widgets.AppMsg;
import com.cloudmachine.utils.widgets.ClearEditTextView;
import com.cloudmachine.utils.widgets.ClearEditTextView.ICoallBack;
import com.cloudmachine.utils.widgets.Dialog.LoadingDialog;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import rx.functions.Action1;

public class LoginActivity extends BaseAutoLayoutActivity<LoginPresenter, LoginModel> implements OnClickListener, Callback, LoginContract.View,  TextWatcher {
    public static final String RX_LOGIN = "rx_login";
    private static final int MSG_SET_ALIAS = 1001;
    private Context mContext;
    private Handler mHandler;
    private ClearEditTextView password_ed;
    private ImageView password_switch_img;
    private ClearEditTextView username_ed;

    private RadiusButtonView login_btn;
    private LoadingDialog progressDialog;
    private TextView forget_pw_tv;
    //    private int flag = 2;
    private int flag;
    private View left_layout, right_layout;
    private CircleTextImageView userImage;
    private Member mMember;
    private UMShareAPI mShareAPI;


    //flag等于3跳转问答社区页面
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        mHandler = new Handler(this);
        this.mContext = this;
        getIntentData();
        initView();
        MobclickAgent.onEvent(this,MobEvent.TIME_LOGIN);
//        initRxManager();
    }

    private void initRxManager() {
        mRxManager.on(RX_LOGIN, new Action1<Map<String, String>>() {
            @Override
            public void call(Map<String, String> params) {
                Set<Map.Entry<String, String>> entryParams = params.entrySet();
                String userName = null;
                String password = null;
                for (Map.Entry<String, String> entry : entryParams) {
                    userName = entry.getKey();
                    password = entry.getValue();

                }
                if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
                    return;
                }
                new LoginAsync(userName, password, mContext, mHandler).execute();

            }
        });

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
        mPresenter.setVM(this, mModel);
    }


    private void show() {
        if (progressDialog == null) {
            progressDialog = LoadingDialog.createDialog(this);
        }
        progressDialog.setMessage("正在加载，请稍后");
        progressDialog.show();
    }

    private void disMiss() {
        if (progressDialog != null&&progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    @Override
    protected void onDestroy() {
        disMiss();
        super.onDestroy();
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

        username_ed = (ClearEditTextView) findViewById(R.id.username_ed);
        password_ed = (ClearEditTextView) findViewById(R.id.password_ed);
        password_switch_img= (ImageView) findViewById(R.id.password_switch_img);
        password_switch_img.setOnClickListener(this);
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
        username_ed.addTextChangedListener(this);
        password_ed.addTextChangedListener(this);
        login_btn = (RadiusButtonView) findViewById(R.id.login_btn);
        login_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(mContext, MobEvent.COUNT_LOGIN);
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
            case R.id.password_switch_img:
                onDrawableRightClickListener(v);
                break;

            case R.id.left_layout:
                finish();
                break;
            case R.id.right_layout:
                MobclickAgent.onEvent(mContext, MobEvent.COUNT_REGISTER);
                it = new Intent(this, FindPasswordActivity.class);
                it.putExtra("type", 3);
                startActivityForResult(it, 0);
                break;
            case R.id.forget_pw_tv:
                MobclickAgent.onEvent(mContext, MobEvent.COUNT_FORGOTPASSWORD);
                it = new Intent(this, FindPasswordActivity.class);
                it.putExtra(FindPasswordActivity.HASINVITATIONCODE, true);
                it.putExtra("type", 1);
                startActivity(it);
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
//
//    //跳转微信登录授权
//    private void loginToWeiXin() {
//        IWXAPI mApi = WXAPIFactory.createWXAPI(this, Constants.APP_ID, true);
//        mApi.registerApp(Constants.APP_ID);
//        if (mApi.isWXAppInstalled()) {
//            SendAuth.Req req = new SendAuth.Req();
//            req.scope = "snsapi_userinfo";
//            req.state = "wechat_sdk_demo_test_neng";
//            mApi.sendReq(req);
//            finish();
//        } else
//            Toast.makeText(this, "用户未安装微信", Toast.LENGTH_SHORT).show();
//    }

    private UMAuthListener authListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            mShareAPI.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.WEIXIN, platformInfoListener);
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            ToastUtils.showToast(mContext,"授权失败！！"+throwable.getMessage());
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
            ToastUtils.showToast(mContext,"取消授权！！");
        }
    };

    private UMAuthListener platformInfoListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            if (map == null) {
                AppLog.print("第三方信息为空");
                return;
            }
            for (Map.Entry<String, String> entry : map.entrySet()) {
                AppLog.print("key:" + entry.getKey() + ", value:" + entry.getValue() + "\n");
            }
            final String unionid = map.get("unionid");
            final String openId = map.get("openid");
            final String nickname = map.get("name");
            final String headimgurl = map.get("iconurl");
            final int sex="男".equals(map.get("gender"))?1:0;
            mRxManager.add(Api.getDefault(HostType.HOST_CLOUDM)
                    .wxLogin(unionid, openId, nickname, headimgurl)
                    .compose(RxSchedulers.<JsonObject>io_main()).subscribe(new RxSubscriber<JsonObject>(mContext, false) {
                        @Override
                        protected void _onNext(JsonObject jsonObject) {
                            JsonElement jsonElement = jsonObject.get("code");
                            int code = jsonElement.getAsInt();
                            AppLog.print("switchWxLogin code___" + code);
                            if (code == 16305) {
                                AppLog.print("set 1");
                                Bundle b = new Bundle();
                                b.putString("nickname", nickname);
                                b.putString("unionid", unionid);
                                b.putString("openid", openId);
                                b.putString("headimgurl", headimgurl);
                                b.putInt("sex", sex);
                                Constants.toActivity(LoginActivity.this, VerifyPhoneNumActivity.class, b, true);
                            } else if (code == 800) {
                                AppLog.print("set 2");
                                MySharedPreferences.setSharedPInt(MySharedPreferences.key_login_type, 1);
                                JsonElement resultElement = jsonObject.get("result");
                                String result = resultElement.toString();
                                try {
                                    Gson gson = new Gson();
                                    mMember = gson.fromJson(result, Member.class);
                                } catch (Exception e) {
                                }
                                if (mMember != null) {
                                    excamMaster(mMember.getId());
                                }
                            } else {
                                AppLog.print("set 3");
                                JsonElement messageElement = jsonObject.get("message");
                                String message = messageElement.getAsString();
                                ToastUtils.showToast(LoginActivity.this, message);
                            }

                        }


                        @Override
                        protected void _onError(String message) {
                            ToastUtils.showToast(LoginActivity.this, message);
                            finish();
                        }
                    }));


        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            ToastUtils.showToast(mContext,"授权失败！！"+throwable.getMessage());
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
            ToastUtils.showToast(mContext,"授权失败！！");
        }
    };



    private void loginToWeiXin() {
        mShareAPI = UMShareAPI.get(this);
        mShareAPI.doOauthVerify(this, SHARE_MEDIA.WEIXIN, authListener);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN && flag == 1) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
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
                AppLog.print("loginScuess___");
                disMiss();
                Constants.isGetScore = true;
                MySharedPreferences.setSharedPInt(MySharedPreferences.key_login_type, 0);
                mMember = (Member) msg.obj;
                if (mMember != null) {
                    excamMaster(mMember.getId());
                } else {
                    jumpNextPage();
                }
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

    private void jumpNextPage() {
        MyApplication.getInstance().setLogin(true);
        MyApplication.getInstance().setFlag(true);
        Constants.isMcLogin = true;
        if (mMember != null) {
            //调用JPush API设置Alias
            mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, mMember.getId() + ""));
            MobclickAgent.onProfileSignIn(String.valueOf(mMember.getId()));
        }
        if (flag == 4) {
            setResult(RESULT_OK);
            finish();
        } else if (flag == 2) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
        } else if (flag == 1) {
        } else if (flag == 3) {
            Intent intent = new Intent(LoginActivity.this, QuestionCommunityActivity.class);
            Bundle bundle = new Bundle();
//                    bundle.putString("url","http://h5.test.cloudm.com/n/ask_qlist");
            bundle.putString(QuestionCommunityActivity.H5_URL, ApiConstants.AppCommunity);
            intent.putExtras(bundle);
            mContext.startActivity(intent);
            finish();
        } else {
            finish();
        }
    }

    private void excamMaster(Long id) {

        mRxManager.add(Api.getDefault(HostType.HOST_CLOUDM_ASK).excamMaster(id)
                .compose(RxHelper.<UserInfo>handleResult())
                .subscribe(new RxSubscriber<UserInfo>(mContext, false) {
                    @Override
                    protected void _onNext(UserInfo userInfo) {
                        Long wjdsId = userInfo.userinfo.id;
                        Long status = userInfo.userinfo.status;
                        Long role_id = userInfo.userinfo.role_id;
                        if (mMember != null) {
                            mMember.setWjdsId(wjdsId);
                            mMember.setWjdsStatus(status);
                            mMember.setWjdsRole_id(role_id);
                            mMember.setNum(2L);
                        }
                        MemeberKeeper.saveOAuth(mMember, mContext);
                        jumpNextPage();
                    }

                    @Override
                    protected void _onError(String message) {
                        MemeberKeeper.saveOAuth(mMember, mContext);
                        jumpNextPage();
                    }
                }));
    }

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            AppLog.print("gotResult___" + code);
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

    public void onDrawableRightClickListener(View view) {

        if (view.isSelected()) {
            view.setSelected(false);
            password_ed.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else {
            view.setSelected(true);
            password_ed.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }
        String contentStr = password_ed.getText().toString();
        password_ed.setSelection(contentStr.length());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {


    }

    @Override
    public void afterTextChanged(Editable s) {
        String pswStr = password_ed.getText().toString();
        String usernameStr = username_ed.getText().toString();
        if (usernameStr.length() > 0) {
            String logoUrl = UserHelper.getLogo(this, usernameStr);
            if (!TextUtils.isEmpty(logoUrl)) {
                Glide.with(this).load(logoUrl).into(userImage);
            }
        }
        if (pswStr.length() > 0 && usernameStr.length() > 0) {
            login_btn.setTextColor(getResources().getColor(R.color.cor15));
        } else {
            login_btn.setTextColor(getResources().getColor(R.color.cor2015));
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == FindPasswordActivity.FP_LOGIN) {
            mMember = (Member) data.getSerializableExtra(Constants.MC_MEMBER);
            jumpNextPage();
        }
    }
}
