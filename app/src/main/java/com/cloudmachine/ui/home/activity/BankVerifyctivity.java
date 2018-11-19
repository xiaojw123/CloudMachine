package com.cloudmachine.ui.home.activity;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.autolayout.widgets.RadiusButtonView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.ApiConstants;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.ToastUtils;
import com.cloudmachine.utils.widgets.ClearEditTextView;
import com.google.gson.JsonObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BankVerifyctivity extends BaseAutoLayoutActivity implements View.OnClickListener, ClearEditTextView.OnTextChangeListener {


    @BindView(R.id.bank_verify_submit_btn)
    RadiusButtonView submitBtn;
    @BindView(R.id.bank_verify_name)
    ClearEditTextView nameEdt;
    @BindView(R.id.bank_verify_mobile)
    ClearEditTextView mobileEdt;
    @BindView(R.id.bank_verify_cardno)
    ClearEditTextView cardNoEdt;
    @BindView(R.id.bank_describe_tv)
    TextView describeTv;
    @BindView(R.id.bank_verify_question)
    ImageView questImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_verify);
        ButterKnife.bind(this);
        String name = getIntent().getStringExtra(Constants.REAL_NAME);
        nameEdt.setText(name);
        nameEdt.setOnTextChangeListener(this);
        mobileEdt.setOnTextChangeListener(this);
        cardNoEdt.setOnTextChangeListener(this);
        submitBtn.setButtonClickEnable(false);
        submitBtn.setButtonClickListener(this);
        questImg.setOnClickListener(this);
        nameEdt.setEnabled(false);
        nameEdt.setNoClearIcon(true);
    }


    @Override
    public void initPresenter() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.radius_button_text:

                String cardNo = cardNoEdt.getText().toString();
                String mobile = mobileEdt.getText().toString();
                String name = nameEdt.getText().toString();

                mRxManager.add(Api.getDefault(HostType.HOST_LARK).authBankCard(cardNo, mobile, name).compose(RxHelper.handleCommonResult(String.class)).subscribe(new RxSubscriber<String>(mContext) {
                    @Override
                    protected void _onNext(String s) {
                        ToastUtils.showToast(mContext, "验证成功");
                        finish();
                    }

                    @Override
                    protected void _onError(String message) {
                        ToastUtils.showToast(mContext, message);

                    }
                }));
                break;
            case R.id.bank_verify_question:
                Bundle data=new Bundle();
                data.putString(QuestionCommunityActivity.H5_URL, ApiConstants.AppBankCmVerify);
                Constants.toActivity(this,QuestionCommunityActivity.class,data);
                break;

        }

    }

    @Override
    public void textChanged(Editable s) {
        int nameLen = nameEdt.getText().toString().length();
        int mobielLen = mobileEdt.getText().toString().length();
        int cardLen = cardNoEdt.getText().toString().length();
        if (nameLen > 0 && mobielLen > 0 && cardLen > 0) {
            submitBtn.setButtonClickEnable(true);
        } else {
            submitBtn.setButtonClickEnable(false);
        }
    }
}
