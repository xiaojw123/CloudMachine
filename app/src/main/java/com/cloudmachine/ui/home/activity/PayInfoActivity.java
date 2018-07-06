package com.cloudmachine.ui.home.activity;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.amap.api.maps.model.Text;
import com.cloudmachine.R;
import com.cloudmachine.activities.RepairPayDetailsActivity;
import com.cloudmachine.alipay.PayResult;
import com.cloudmachine.autolayout.widgets.RadiusButtonView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.base.Operator;
import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.bean.BaseBO;
import com.cloudmachine.bean.Member;
import com.cloudmachine.bean.SalaryPayInfo;
import com.cloudmachine.chart.utils.AppLog;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.net.ATask;
import com.cloudmachine.net.HttpURLConnectionImp;
import com.cloudmachine.net.IHttp;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.ui.home.contract.PayInfoContract;
import com.cloudmachine.ui.home.model.PayInfoModel;
import com.cloudmachine.ui.home.presenter.PayInfoPresenter;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.DensityUtil;
import com.cloudmachine.utils.MemeberKeeper;
import com.cloudmachine.utils.ToastUtils;
import com.cloudmachine.utils.URLs;
import com.cloudmachine.widget.CustomBindDialog;
import com.cloudmachine.zxing.decoding.Intents;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.cache.disc.impl.TotalSizeLimitedDiscCache;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.socialize.media.Base;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

public class PayInfoActivity extends BaseAutoLayoutActivity<PayInfoPresenter, PayInfoModel> implements PayInfoContract.View, View.OnClickListener {
    public static final String FINISH_PAY_SALALARY = "finish_pay_salary";
    public static final int SUCCESS_SALARY_PAY = 0x01;
    public static final int TYPE_PURSE_PAY = 4;
    public static final int SALARY_ALIPAYPAY = 1;
    public static final int SALARY_WXPAY = 2;
    public static final int PURSE_WXPAY = 3;
    //    @BindView(R.id.payinfo_pay_toal)
//    TextView mToalPayouont;
//    @BindView(R.id.payinfo_salary_amount)
//    TextView mSalaryAmount;
//    @BindView(R.id.payinfo_service_amount)
//    TextView mServiceaAmount;
//    @BindView(R.id.pay_info_spool_amount)
//    TextView mSpoolAmount;
//    @BindView(R.id.payinfo_toal_period)
//    TextView mToalPeriod;
//    @BindView(R.id.payinfo_period_amount)
//    TextView mPeriodAmount;
    @BindView(R.id.salary_pay_btn)
    RadiusButtonView payBtn;
    @BindView(R.id.payinfo_collection_cotainer)
    LinearLayout mCollectionCotainer;
    @BindView(R.id.payinfo_salary_amount)
    TextView salaryAmountTv;
    @BindView(R.id.payinfo_pay_type)
    TextView payTypeTv;
    int payType;
    String amountText;
    List<Operator> receiverList;
    JSONArray receiverInfoArray = new JSONArray();
    Member mMember;
    Timer mTimer;
    int timeOut = 60;//定时时长60s
    CustomBindDialog identyfDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_info);
        ButterKnife.bind(this);
        mMember = MemeberKeeper.getOauth(this);
        initView();
        initRxManager();
    }

    private void initRxManager() {
        mRxManager.on(FINISH_PAY_SALALARY, new Action1<String>() {
            @Override
            public void call(String o) {
                Constants.ToastAction(o);
                if ("支付成功".equals(o)) {
                    setResult(SUCCESS_SALARY_PAY);
                    finish();
                }
            }
        });
    }

    private void initView() {
        payBtn.setOnClickListener(this);
        amountText = getIntent().getStringExtra(Constants.P_PAYAMOUNT);
        payType = getIntent().getIntExtra(Constants.P_PAYTYPE, 0);
        receiverList = getIntent().getParcelableArrayListExtra(Constants.P_RECEIVERLIST);
        salaryAmountTv.setText(amountText);
        String payTypeStr = null;
        switch (payType) {
            case 1:
                payTypeStr = getString(R.string.alipay);
                break;
            case 2:
                payTypeStr = getString(R.string.wxPay);
                break;
            case 3:
                payTypeStr = getString(R.string.purse_pay);
                break;
        }
        payTypeTv.setText(payTypeStr);
        if (receiverList != null && receiverList.size() > 0) {
            for (Operator item : receiverList) {
                try {
                    JSONObject jobj = new JSONObject();
                    jobj.put("receiveMemberId", item.getId());
                    jobj.put("receiveMemberName", item.getName());
                    jobj.put("salaryAmount", item.getAmount());
                    receiverInfoArray.put(jobj);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mCollectionCotainer.addView(getCollectionItem(item.getName(), item.getAmount(), false));
            }
        }

    }


    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    public FrameLayout getCollectionItem(String key, String value, boolean isHeader) {
        FrameLayout itemLayout = new FrameLayout(this);
        itemLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(this, 50)));
        itemLayout.setBackgroundColor(getResources().getColor(R.color.white));
        TextView nameTv = new TextView(this);
        nameTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        nameTv.setTextColor(getResources().getColor(R.color.cor9));
        nameTv.setText(key);
        TextView valueTv = new TextView(this);
        valueTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        if (isHeader) {
            valueTv.setTextColor(getResources().getColor(R.color.c_ff8901));
        } else {
            valueTv.setTextColor(getResources().getColor(R.color.cor8));
        }
        valueTv.setText(value);
        FrameLayout.LayoutParams keyParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        keyParams.leftMargin = DensityUtil.dip2px(this, 20);
        keyParams.gravity = Gravity.CENTER_VERTICAL;
        FrameLayout.LayoutParams valueParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        valueParams.rightMargin = DensityUtil.dip2px(this, 16);
        valueParams.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
        itemLayout.addView(nameTv, keyParams);
        itemLayout.addView(valueTv, valueParams);
        return itemLayout;
    }


    @Override
    public void onClick(View v) {
        if (payType == 3) {
            mPresenter.getVerfyCode(TYPE_PURSE_PAY, mMember.getMobile());
        } else {
            new PayTask().execute();
        }
    }

    @Override
    public void returnVerfyCode() {
        final CustomBindDialog.Builder builder = new CustomBindDialog.Builder(this);
        builder.setCodeGetListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                v.setEnabled(false);
                mPresenter.getVerfyCode(TYPE_PURSE_PAY, mMember.getMobile());
                startTimer(v);
            }
        });
        builder.setNegativeButtonClickListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButtonClickListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String code = builder.getInputCode();
                if (!TextUtils.isEmpty(code)) {
                    if (identyfDialog != null && identyfDialog.isShowing()) {
                        identyfDialog.dismiss();
                    }
                    new PayTask().execute(code);
                } else {
                    ToastUtils.showToast(mContext, "验证码不能为空");
                }
            }
        });
        builder.setPhoneNum(mMember.getMobile());
        identyfDialog = builder.create();
        identyfDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                cancelTimer();
            }
        });
        identyfDialog.show();
        TextView codeTv = builder.getCodeTv();
        if (codeTv != null) {
            codeTv.setEnabled(false);
            startTimer(codeTv);
        }
    }

    private void cancelTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }


    private void startTimer(final View v) {
        timeOut = 60;
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AppLog.print("runOnUiThread___");
                        timeOut--;
                        String text;
                        if (timeOut <= 0) {
                            if (mTimer != null) {
                                mTimer.cancel();
                                mTimer = null;
                            }
                            text = "重新获取";
                            v.setEnabled(true);
                        } else {
                            text = timeOut + "S";
                        }
                        ((TextView) v).setText(text);
                    }
                });
            }
        }, 0, 1000);
    }


    public class PayTask extends ATask {

        @Override
        protected String doInBackground(String... params) {
            IHttp httpRequest = new HttpURLConnectionImp();
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("memberId", String.valueOf(mMember.getId())));
            list.add(new BasicNameValuePair("salaryCount", String.valueOf(receiverInfoArray.length())));
            list.add(new BasicNameValuePair("salaryTotalAmount", amountText));
            list.add(new BasicNameValuePair("payoffType", String.valueOf(payType)));
            list.add(new BasicNameValuePair("salaryInfoDetailJsonStr", receiverInfoArray.toString()));
            if (params != null && params.length > 0) {
                list.add(new BasicNameValuePair("verifyCode", params[0]));
            }
            try {
                return httpRequest.get(URLs.CONFIRMPAY_URL, list);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }


        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                Gson gson = new Gson();
                AppLog.print("String Restult___" + result);
                if (payType == 3) {
                    BaseRespose<String> bo = gson.fromJson(result, new TypeToken<BaseRespose<String>>() {
                    }.getType());
                    if (bo.success()) {
                        ToastUtils.showToast(mContext, bo.getResult());
                        setResult(SUCCESS_SALARY_PAY);
                        finish();
                    } else {
                        ToastUtils.showToast(mContext, bo.getMessage());
                    }
                } else {
                    BaseRespose<SalaryPayInfo> bo = gson.fromJson(result, new TypeToken<BaseRespose<SalaryPayInfo>>() {
                    }.getType());
                    if (bo.success()) {
                        SalaryPayInfo info = bo.getResult();
                        switch (payType) {
                            case 1:
                                payAli(info.getSign());
                                break;
                            case 2:
                                payWeiXin(info);
                                break;
                        }
                    } else {
                        ToastUtils.showToast(mContext, bo.getMessage());
                    }
                }

            }
        }


    }

    //微信支付
    public void payWeiXin(SalaryPayInfo info) {
        AppLog.print("payWx_start_");
        IWXAPI api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.registerApp(Constants.APP_ID);
        PayReq payRequest = new PayReq();
        payRequest.appId = Constants.APP_ID;
        payRequest.partnerId = info.getPartnerid();
        payRequest.prepayId = info.getPrepayid();
        payRequest.packageValue = info.getPackageValue();
        payRequest.nonceStr = info.getNoncestr();
        payRequest.timeStamp = info.getTimestamp();
        payRequest.sign = info.getSign();
        api.sendReq(payRequest);
    }

    public void payAli(final String orderInfo) {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                com.alipay.sdk.app.PayTask alipay = new com.alipay.sdk.app.PayTask(PayInfoActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);

                Message msg = new Message();
                msg.what = Constants.HANDLER_RESULT_APLIPAY;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();

    }

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == Constants.HANDLER_RESULT_APLIPAY) {
                PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                String resultStatus = payResult.getResultStatus();
                // 判断resultStatus 为9000则代表支付成功
                if (TextUtils.equals(resultStatus, "9000")) {
                    // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                    Toast.makeText(PayInfoActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    setResult(SUCCESS_SALARY_PAY);
                    finish();
                } else {
                    // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                    Toast.makeText(PayInfoActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
//                    finish();
                }
            }
        }
    };


}
