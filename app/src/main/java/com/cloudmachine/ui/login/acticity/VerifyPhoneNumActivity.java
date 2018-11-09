package com.cloudmachine.ui.login.acticity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudmachine.MyApplication;
import com.cloudmachine.R;
import com.cloudmachine.autolayout.widgets.RadiusButtonView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.bean.CheckNumBean;
import com.cloudmachine.bean.Member;
import com.cloudmachine.cache.MySharedPreferences;
import com.cloudmachine.helper.MobEvent;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.ui.home.activity.HomeActivity;
import com.cloudmachine.ui.login.contract.VerifyPhoneNumContract;
import com.cloudmachine.ui.login.model.VerifyPhoneNumModel;
import com.cloudmachine.ui.login.presenter.VerifyPhoneNumPresenter;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MemeberKeeper;
import com.cloudmachine.utils.ToastUtils;
import com.cloudmachine.utils.widgets.ClearEditTextView;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;

/**
 * 项目名称：CloudMachine
 * 类描述：验证手机号页面
 * 创建人：shixionglu
 * 创建时间：2017/3/27 下午2:39
 * 修改人：shixionglu
 * 修改时间：2017/3/27 下午2:39
 * 修改备注：
 */

public class VerifyPhoneNumActivity extends BaseAutoLayoutActivity<VerifyPhoneNumPresenter, VerifyPhoneNumModel>
        implements VerifyPhoneNumContract.View, View.OnClickListener, Handler.Callback, TextWatcher {

    @BindView(R.id.ll_back)
    LinearLayout mLlBack;//返回按钮
    @BindView(R.id.phone_string)//手机号码
            ClearEditTextView mPhoneString;
    @BindView(R.id.validate_text)
    TextView mValidateText;
    @BindView(R.id.validate_layout)
    RelativeLayout mValidateLayout;
    @BindView(R.id.validate_code)
    ClearEditTextView mValidateCode;
    @BindView(R.id.code_layout)
    RelativeLayout mCodeLayout;
    @BindView(R.id.pwd_string)
    ClearEditTextView mPwdString;
    @BindView(R.id.agreement_text)
    TextView mAgreementText;
    @BindView(R.id.agreement_layout)
    LinearLayout mAgreementLayout;
    @BindView(R.id.ll_password)
    LinearLayout mLlPassword;
    private int mobileType = 2;
    private String mNickname;
    private String mUnionid;
    private String mOpenid;
    private int mSex;
    private String mAccount;
    private String mPwd;
    private String mHeadimgurl;
    private RadiusButtonView mFindBtn;
    private Handler mHandler;
    private static final int MSG_SET_ALIAS = 1001;
    private String mCode;
    private Member mMember;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phonenum);
        ButterKnife.bind(this);
        getIntentData();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onEvent(VerifyPhoneNumActivity.this, MobEvent.WXBIND);
    }

    private void getIntentData() {

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            mNickname = bundle.getString("nickname");
            mUnionid = bundle.getString("unionid");
            mOpenid = bundle.getString("openid");
            mHeadimgurl = bundle.getString("headimgurl");
            mSex = bundle.getInt("sex");
        }
    }

    private void initView() {
        mHandler = new Handler(this);
        mAgreementText.setOnClickListener(this);
        mValidateLayout.setOnClickListener(this);
        mLlBack.setOnClickListener(this);
        switchLayout();
        mFindBtn = (RadiusButtonView) findViewById(R.id.find_btn);
        mFindBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mPhoneString.getText().toString().trim())) {
                    ToastUtils.info("手机号码不能为空", true);
                    return;
                } else {
                    mAccount = mPhoneString.getText().toString().trim();
                }
                if (TextUtils.isEmpty(mValidateCode.getText().toString().trim())) {
                    ToastUtils.info("验证码不能为空", true);
                    return;
                } else {
                    mCode = mValidateCode.getText().toString().trim();
                }
                if (mobileType == 1) {
                    if (TextUtils.isEmpty(mPwdString.getText().toString().trim())) {
                        ToastUtils.info("密码不能为空", true);
                    } else {
                        mPwd = mPwdString.getText().toString().trim();
                    }
                } else {
                    mPwd = null;
                }
              /*  mPresenter.bindWx(mUnionid,mOpenid,mAccount
                ,mCode,mInvitationValue,mPwd,mNickname,mHeadimgurl,mobileType);*/
                mPresenter.wxBind(mUnionid, mOpenid, mAccount
                        , mCode, mPwd, mNickname, mHeadimgurl);

               /* mRxManager.add(Api.getDefault(HostType.CAITINGTING_HOST)
                .wxBind(mUnionid,mOpenid,mAccount
                        ,mCode,mInvitationValue,mPwd,mNickname,mHeadimgurl,mobileType)
                .compose(RxHelper.<Member>handleResult())
                .subscribe(new Subscriber<Member>() {
                    @Override
                    public void onCompleted() {
                        Constants.MyLog("1111111");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Constants.MyLog("2222222");
                        Constants.MyLog(e.toString());
                    }

                    @Override
                    public void onNext(Member member) {
                        Constants.MyLog("3333333");
                    }
                }));*/

            }
        });
        mPhoneString.addTextChangedListener(this);
        mValidateCode.addTextChangedListener(this);
        mPwdString.addTextChangedListener(this);
    }

    private void switchLayout() {

        if (mobileType == 1) {
            mLlPassword.setVisibility(View.VISIBLE);
        } else {
            mLlPassword.setVisibility(View.GONE);
        }
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    private void sendMessage() {
        final int count = 60;
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(count + 1)
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        return count - aLong;
                    }
                })
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mValidateLayout.setEnabled(false);
                        mValidateText.setTextColor(getResources().getColor(R.color.login_hint_text));
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onCompleted() {
                        mValidateText.setText("获取验证码");
                        mValidateLayout.setEnabled(true);
                        mValidateText.setTextColor(getResources().getColor(R.color.cor8));
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        mValidateText.setText("获取验证码(" + aLong + ")");
                        mValidateLayout.setEnabled(false);
                        mValidateText.setTextColor(getResources().getColor(R.color.login_hint_text));
                    }
                });

    }

    //拿到绑定成功的信息（失败结果已在presenter页面先行进行处理）
    @Override
    public void returnWXBindMobile(String message) {
        ToastUtils.success(message, true);
        sendMessage();
    }

    @Override
    public void returnCheckNum(Integer type) {

        //账号未注册
        if (type == 1) {
            changeMobileState(false, 1);
        }
        //账号已注册，但是未绑定微信号
        else if (type == 2) {
            changeMobileState(true, 2);
        }
        //账号已注册，绑定了其他微信
        else if (type == 3) {
            changeMobileState(true, 3);
        }
        //获取验证码
        mPresenter.wxBindMobile(Long.parseLong(mPhoneString.getText().toString().trim()), 3);

    }

    @Override
    public void returnBindWx(Member member) {
        mMember = member;
        MyApplication.getInstance().setLogin(true);
        MyApplication.getInstance().setFlag(true);
        Intent intent = new Intent(VerifyPhoneNumActivity.this, HomeActivity.class);
        startActivity(intent);
        MySharedPreferences.setSharedPInt(MySharedPreferences.key_login_type, 1);
        Constants.isMcLogin = true;
        //调用JPush API设置Alias
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, mMember.getId() + ""));
        MobclickAgent.onProfileSignIn(String.valueOf(mMember.getId()));
        finish();
    }


    //赋值状态，
    private void changeMobileState(boolean b, int mobileType) {
        this.mobileType = mobileType;
        switchLayout();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.validate_layout:
                if (TextUtils.isEmpty(mPhoneString.getText().toString().trim())) {
                    ToastUtils.info("手机号码不能为空", true);
                } else {
                    mPresenter.checkNum(Long.parseLong(mPhoneString.getText().toString().trim()));
                }
                break;
            case R.id.ll_back:
                finish();
                break;

        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_SET_ALIAS:
                JPushInterface.setAliasAndTags(getApplicationContext(), (String) msg.obj, null, mAliasCallback);
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

                    break;

                default:

            }


        }

    };


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String str1 = mPhoneString.getText().toString();
        String str2 = mValidateCode.getText().toString();
        String str3 = mPwdString.getText().toString();
        String valiateText = mValidateText.getText().toString();
        if ("获取验证码".equals(valiateText)) {
            if (str1.length() > 0) {
                mValidateLayout.setEnabled(true);
                mValidateText.setTextColor(getResources().getColor(R.color.cor8));
            } else {
                mValidateLayout.setEnabled(false);
                mValidateText.setTextColor(getResources().getColor(R.color.login_hint_text));
            }
        }
        if (mobileType == 1) {
            if (str1.length() > 0 && str2.length() > 0 && str3.length() > 0) {
                mFindBtn.setTextColor(getResources().getColor(R.color.cor15));
            } else {
                mFindBtn.setTextColor(getResources().getColor(R.color.cor2015));
            }
        } else {
            if (str1.length() > 0 && str2.length() > 0) {
                mFindBtn.setTextColor(getResources().getColor(R.color.cor15));
            } else {
                mFindBtn.setTextColor(getResources().getColor(R.color.cor2015));
            }
        }
    }
}
