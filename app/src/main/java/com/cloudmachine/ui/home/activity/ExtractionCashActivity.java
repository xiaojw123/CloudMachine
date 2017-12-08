package com.cloudmachine.ui.home.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;

import com.cloudmachine.R;
import com.cloudmachine.autolayout.widgets.RadiusButtonView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.base.baserx.RxSchedulers;
import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.utils.CommonUtils;
import com.cloudmachine.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExtractionCashActivity extends BaseAutoLayoutActivity {

    @BindView(R.id.extr_cash_alipay_cb)
    CheckBox extrCashAlipayCb;
    @BindView(R.id.extr_cash_alipay_fl)
    FrameLayout extrCashAlipayFl;
    @BindView(R.id.extr_cash_wxpay_cb)
    CheckBox extrCashWxpayCb;
    @BindView(R.id.extr_cash_wxpay_fl)
    FrameLayout extrCashWxpayFl;
    @BindView(R.id.extr_cash_sure_btn)
    RadiusButtonView extrCashSureBtn;
    int type = PurseActivity.TYPE_ALIPAY;
    String walletAmountStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extraction_cash);
        ButterKnife.bind(this);
        walletAmountStr=getIntent().getStringExtra(PurseActivity.KEY_WALLETAMOUNT);
    }

    @Override
    public void initPresenter() {

    }

    @OnClick({R.id.extr_cash_alipay_fl, R.id.extr_cash_wxpay_fl, R.id.extr_cash_sure_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.extr_cash_alipay_fl:
                type = PurseActivity.TYPE_ALIPAY;
                extrCashAlipayCb.setChecked(true);
                if (extrCashWxpayCb.isChecked()) {
                    extrCashWxpayCb.setChecked(false);
                }
                break;
            case R.id.extr_cash_wxpay_fl:
                type = PurseActivity.TYPE_WX;
                extrCashWxpayCb.setChecked(true);
                if (extrCashAlipayCb.isChecked()) {
                    extrCashAlipayCb.setChecked(false);
                }
                break;
            case R.id.radius_button_text:
                cashOutAmout();
                break;
        }
    }

    private void cashOutAmout() {
        mRxManager.add(Api.getDefault(HostType.HOST_CLOUDM).cashOut(UserHelper.getMemberId(this), type).compose(RxSchedulers.<BaseRespose>io_main()).subscribe(new RxSubscriber<BaseRespose>(mContext) {
            @Override
            protected void _onNext(BaseRespose baseRespose) {
                if (baseRespose.success()) {
                    CommonUtils.showSuccessDialog(mContext, "提现成功", walletAmountStr, "退款将在3-7个中作日到账");
                } else {
                    ToastUtils.showToast(mContext, baseRespose.getMessage());
                }
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(mContext, "提现失败");

            }
        }));

    }


}
