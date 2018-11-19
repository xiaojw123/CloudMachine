package com.cloudmachine.ui.login.acticity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cloudmachine.R;
import com.cloudmachine.autolayout.widgets.CircleTextImageView;
import com.cloudmachine.autolayout.widgets.RadiusButtonView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.bean.Member;
import com.cloudmachine.helper.MobEvent;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.ui.login.contract.LoginContract;
import com.cloudmachine.ui.login.model.LoginModel;
import com.cloudmachine.ui.login.presenter.LoginPresenter;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.widgets.ClearEditTextView;
import com.umeng.analytics.MobclickAgent;

import cn.jpush.android.api.JPushInterface;

public class LoginActivity extends BaseAutoLayoutActivity<LoginPresenter, LoginModel> implements OnClickListener, LoginContract.View, TextWatcher {
    private ClearEditTextView password_ed;
    private ClearEditTextView username_ed;

    private RadiusButtonView login_btn;
    private CircleTextImageView userImage;
    private Member mMember;


    //flag等于3跳转问答社区页面
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        initView();
        MobclickAgent.onEvent(this, MobEvent.TIME_LOGIN);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (UserHelper.isLogin(mContext)) {
            Constants.exitAccount();
        }
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }


    @Override
    protected void onDestroy() {
        mPresenter.disMiss();
        super.onDestroy();
    }


    void initView() {
        TextView wxLogin = (TextView) findViewById(R.id.weixin_login);
        RelativeLayout wechatLogin = (RelativeLayout) findViewById(R.id.rl_weixin_login);
        wechatLogin.setOnClickListener(this);
        wxLogin.setOnClickListener(this);
        userImage = (CircleTextImageView) findViewById(R.id.user_image);
        TextView forget_pw_tv = (TextView) findViewById(R.id.forget_pw_tv);
        View left_layout = findViewById(R.id.left_layout);
        View right_layout = findViewById(R.id.right_layout);
        left_layout.setOnClickListener(this);
        right_layout.setOnClickListener(this);
        username_ed = (ClearEditTextView) findViewById(R.id.username_ed);
        password_ed = (ClearEditTextView) findViewById(R.id.password_ed);
        ImageView password_switch_img = (ImageView) findViewById(R.id.password_switch_img);
        password_switch_img.setOnClickListener(this);
        username_ed.addTextChangedListener(this);
        password_ed.addTextChangedListener(this);
        login_btn = (RadiusButtonView) findViewById(R.id.login_btn);
        login_btn.setButtonEnable(false);
        login_btn.setOnClickListener(this);
        forget_pw_tv.setOnClickListener(this);
    }

    @Override
    public void setNameShakeAnimation() {
        username_ed.setShakeAnimation();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.radius_button_text:
                login();
                break;

            case R.id.password_switch_img:
                onDrawableRightClickListener(v);
                break;

            case R.id.left_layout:
                setResult(RESULT_OK);
                finish();
                break;
            case R.id.right_layout:
                mPresenter.gotoRegisterPage();
                break;
            case R.id.forget_pw_tv:
                mPresenter.gotoForgetPswPage();
                break;
            case R.id.weixin_login:
            case R.id.rl_weixin_login:
                mPresenter.loginToWeiXin();
                break;
        }
    }

    private void login() {
        if (login_btn.isButtonEanble()) {
            MobclickAgent.onEvent(mContext, MobEvent.COUNT_LOGIN);
            String nameStr = username_ed.getText().toString().trim();
            String pswStr = password_ed.getText().toString().trim();
            mPresenter.login(nameStr, pswStr);
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            setResult(RESULT_OK);
            finish();
        }
        return true;
    }


    private void jumpNextPage() {
        if (mMember != null) {
            //调用JPush API设置Alias
            JPushInterface.setAliasAndTags(getApplicationContext(), String.valueOf(mMember.getId()), null, null);
            MobclickAgent.onProfileSignIn(String.valueOf(mMember.getId()));
        }
        setResult(RESULT_OK);
        finish();
    }


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
                Glide.with(this).load(logoUrl).placeholder(R.drawable.ic_logo).error(R.drawable.ic_logo).into(userImage);
            } else {
                userImage.setImageResource(R.drawable.ic_logo);
            }
        } else {
            userImage.setImageResource(R.drawable.ic_logo);
        }
        if (pswStr.length() > 0 && usernameStr.length() > 0) {
            login_btn.setTextColor(getResources().getColor(R.color.cor15));
            login_btn.setButtonEnable(true);
        } else {
            login_btn.setTextColor(getResources().getColor(R.color.cor2015));
            login_btn.setButtonEnable(false);
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

    @Override
    public void loginSuccess(Member member) {
        if (member != null) {
            mMember = member;
        }
        jumpNextPage();
    }


}
