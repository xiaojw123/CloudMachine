package com.cloudmachine.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.cloudmachine.R;
import com.cloudmachine.autolayout.widgets.TitleView;
import com.cloudmachine.base.BaseAutoLayoutActivity;

/**
 * 项目名称：CloudMachine
 * 类描述：支付结果页面
 * 创建人：shixionglu
 * 创建时间：2017/2/9 下午1:42
 * 修改人：shixionglu
 * 修改时间：2017/2/9 下午1:42
 * 修改备注：
 */

public class PaymentResultsActivity extends BaseAutoLayoutActivity implements View.OnClickListener {

    private String paymentResult;
    private LinearLayout llPaymentSuccess;
    private LinearLayout llPaymentFaild;
    private TitleView titleLayout;
    private Button retryPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymentresult);
        getIntentData();
        initView();
    }

    @Override
    public void initPresenter() {

    }

    private void getIntentData() {
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        paymentResult = bundle.getString("paymentResult");
    }

    private void initView() {
        initTitleLayout();
        llPaymentSuccess = (LinearLayout) findViewById(R.id.ll_payment_success);
        llPaymentFaild = (LinearLayout) findViewById(R.id.ll_payment_faild);
        if (null != paymentResult) {
            if (paymentResult.equals("支付成功")) {
                llPaymentSuccess.setVisibility(View.VISIBLE);
                llPaymentFaild.setVisibility(View.GONE);
            } else if (paymentResult.equals("支付失败")) {
                llPaymentFaild.setVisibility(View.VISIBLE);
                llPaymentSuccess.setVisibility(View.GONE);
            }
        }
        retryPay = (Button) findViewById(R.id.retry_pay);
        retryPay.setOnClickListener(this);

    }

    private void initTitleLayout() {


        titleLayout = (TitleView) findViewById(R.id.title_layout);
        titleLayout.setLeftImage(-1, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleLayout.setTitle("支付结果");
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
