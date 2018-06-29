package com.cloudmachine.ui.home.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.cloudmachine.R;
import com.cloudmachine.autolayout.widgets.RadiusButtonView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.chart.utils.AppLog;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.utils.ToastUtils;
import com.cloudmachine.utils.widgets.ClearEditTextView;
import com.google.gson.JsonObject;

public class AuthNameActivity extends BaseAutoLayoutActivity implements View.OnClickListener, TextWatcher {
    public static final int AUTH_SUCCESS=0x13;
    ClearEditTextView nameEdt;
    ClearEditTextView idCardEdt;
    RadiusButtonView submtiBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        initView();
    }

    private void initView() {
        nameEdt = (ClearEditTextView) findViewById(R.id.auth_name_edt);
        idCardEdt = (ClearEditTextView) findViewById(R.id.auth_idcard_edt);
        submtiBtn = (RadiusButtonView) findViewById(R.id.auth_sbumit_rb);
        submtiBtn.setButtonEnable(false);
        nameEdt.addTextChangedListener(this);
        idCardEdt.addTextChangedListener(this);
        submtiBtn.setOnClickListener(this);
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void onClick(View v) {
        if (submtiBtn.isButtonEanble()) {
            String nameStr = nameEdt.getText().toString();
            String idCardStr = idCardEdt.getText().toString();
            mRxManager.add(Api.getDefault(HostType.HOST_CLOUDM).realNameAuth(UserHelper.getMemberId(this), nameStr, idCardStr).compose(RxHelper.<String>handleBaseResult()).subscribe(new RxSubscriber<BaseRespose<String>>(mContext) {
                @Override
                protected void _onNext(BaseRespose<String> respose) {
                    ToastUtils.showToast(mContext,"认证成功！");
                    setResult(AUTH_SUCCESS);
                    finish();
                }

                @Override
                protected void _onError(String message) {
                    ToastUtils.showToast(mContext,message);

                }
            }));
        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String str1=nameEdt.getText().toString();
        String str2=idCardEdt.getText().toString();
        if (str1.length() > 0 && str2.length() > 0) {
            submtiBtn.setTextColor(getResources().getColor(R.color.cor15));
            submtiBtn.setButtonEnable(true);
        } else {
            submtiBtn.setTextColor(getResources().getColor(R.color.cor2015));
            submtiBtn.setButtonEnable(false);
        }
    }
}
