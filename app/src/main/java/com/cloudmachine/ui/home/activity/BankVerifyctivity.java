package com.cloudmachine.ui.home.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.autolayout.widgets.RadiusButtonView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.utils.widgets.ClearEditTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BankVerifyctivity extends BaseAutoLayoutActivity implements View.OnClickListener, ClearEditTextView.OnTextChangeListener {


    @BindView(R.id.bank_verify_submit_btn)
    RadiusButtonView  submitBtn;
    @BindView(R.id.bank_verify_name)
    ClearEditTextView nameEdt;
    @BindView(R.id.bank_verify_mobile)
    ClearEditTextView mobileEdt;
    @BindView(R.id.bank_verify_cardno)
    ClearEditTextView cardNoEdt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_verify);
        ButterKnife.bind(this);
        nameEdt.setOnTextChangeListener(this);
        mobileEdt.setOnTextChangeListener(this);
        cardNoEdt.setOnTextChangeListener(this);
        submitBtn.setButtonClickEnable(false);
        submitBtn.setButtonClickListener(this);
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void textChanged(Editable s) {
      int nameLen=nameEdt.getText().toString().length();
      int mobielLen=mobileEdt.getText().toString().length();
      int cardLen=cardNoEdt.getText().toString().length();
        if (nameLen>0&&mobielLen>0&&cardLen>0){
            submitBtn.setButtonClickEnable(true);
        }else{
            submitBtn.setButtonClickEnable(false);
        }
    }
}
