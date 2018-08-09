package com.cloudmachine.ui.home.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.autolayout.widgets.CustomDialog;
import com.cloudmachine.autolayout.widgets.RadiusButtonView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.chart.utils.AppLog;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.utils.CommonUtils;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.ToastUtils;
import com.cloudmachine.utils.widgets.ClearEditTextView;
import com.google.gson.JsonElement;
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
    long memberId;
    CustomDialog submitDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_verify);
        ButterKnife.bind(this);
        memberId = UserHelper.getMemberId(this);
        boolean isComplelte = getIntent().getBooleanExtra(InfoManagerActivity.KEY_COMPLETED, false);
        String name = getIntent().getStringExtra(Constants.REAL_NAME);
        if (isComplelte) {
            describeTv.setText("已验证银行卡信息");
            submitBtn.setVisibility(View.GONE);
            cardNoEdt.setEnabled(false);
            cardNoEdt.setNoClearIcon(true);
            mobileEdt.setEnabled(false);
            mobileEdt.setNoClearIcon(true);
            updateCardInfo();
        } else {
            nameEdt.setText(name);
            nameEdt.setOnTextChangeListener(this);
            mobileEdt.setOnTextChangeListener(this);
            cardNoEdt.setOnTextChangeListener(this);
            submitBtn.setButtonClickEnable(false);
            submitBtn.setButtonClickListener(this);
        }
        nameEdt.setEnabled(false);
        nameEdt.setNoClearIcon(true);
    }

    private void updateCardInfo() {
        mRxManager.add(Api.getDefault(HostType.HOST_CLOUDM_YJX).getBankCardInfo(memberId).compose(RxHelper.<JsonObject>handleResult()).subscribe(new RxSubscriber<JsonObject>(mContext) {
            @Override
            protected void _onNext(JsonObject jsonObject) {
                JsonElement j1 = jsonObject.get("realName");
                JsonElement j2 = jsonObject.get("reserveMobile");
                JsonElement j3 = jsonObject.get("bankCardNo");
                if (j1 != null) {
                    nameEdt.setText(j1.getAsString());
                }
                if (j2 != null) {
                    mobileEdt.setText(j2.getAsString());
                }
                if (j3 != null) {
                    cardNoEdt.setText(j3.getAsString());
                }
            }

            @Override
            protected void _onError(String message) {

            }
        }));

    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void onClick(View v) {
        if (submitDialog == null) {
            CustomDialog.Builder builder = new CustomDialog.Builder(this);
            builder.setMessage("这张银行卡将作为您提现、借款、还款的资金账号, 只能绑定一张，请认真填写。");
            builder.setNegativeButton("返回修改", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton("提交", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                    String cardNo = cardNoEdt.getText().toString();
                    String mobile = mobileEdt.getText().toString();
                    String name = nameEdt.getText().toString();

                    mRxManager.add(Api.getDefault(HostType.HOST_CLOUDM_YJX).authBankCard(memberId, cardNo, mobile, name).compose(RxHelper.<String>handleResult()).subscribe(new RxSubscriber<String>(mContext) {
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

                }
            });
            submitDialog=builder.create();
        }
        submitDialog.show();
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
