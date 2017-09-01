package com.cloudmachine.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.cloudmachine.R;
import com.cloudmachine.adapter.GetCouponAdapter;
import com.cloudmachine.alipay.PayResult;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.bean.AliPayBean;
import com.cloudmachine.bean.CWInfo;
import com.cloudmachine.bean.CouponInfo;
import com.cloudmachine.bean.PayPriceInfo;
import com.cloudmachine.bean.WeiXinEntityBean;
import com.cloudmachine.bean.WorkSettleBean;
import com.cloudmachine.chart.utils.AppLog;
import com.cloudmachine.net.task.CWPayAsync;
import com.cloudmachine.net.task.GetCouponAsync;
import com.cloudmachine.net.task.GetWorkDetailAsync;
import com.cloudmachine.net.task.PayPriceAsync;
import com.cloudmachine.ui.home.activity.ConsumptionActivity;
import com.cloudmachine.ui.home.activity.PayDeviceInfoActivity;
import com.cloudmachine.ui.repair.activity.ViewRepairActivity;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.ToastUtils;
import com.rey.material.app.BottomSheetDialog;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.ArrayList;
import java.util.Map;

import rx.functions.Action1;

/**
 * 项目名称：CloudMachine
 * 类描述：报修支付详情页面
 * 创建人：shixionglu
 * 创建时间：2017/2/23 下午2:08
 * 修改人：shixionglu
 * 修改时间：2017/2/23 下午2:08
 * 修改备注：
 */

public class RepairPayDetailsActivity extends BaseAutoLayoutActivity implements Handler.Callback, View.OnClickListener {
    public static final String FINISH_PAY_DETAIL = "finish_pay_detail";
    private String orderNum;
    private String flag;
    private Context context;
    private Handler mHandler;
    private FrameLayout infoFl;
    private TextView tvShouldPrice;
    private RelativeLayout mRlWeiXinPay;
    private RelativeLayout mRlAliPay;
    private RelativeLayout mRlCoupon;
    private TextView tvManHour;
    private CheckBox mCbWeiXinPay;
    private CheckBox mCbAliPay;
    private TextView tvPayPrice;
    private Button btnToPay;
    int couponCount = 1;
    private ArrayList<CouponInfo> couponData;
    private int height;
    private int couponHeight;
    private RelativeLayout root;
    private Button btnCancel;
    private BottomSheetDialog couponDialog;
    private ListView lvCoupon;
    private GetCouponAdapter getCouponAdapter;
    private long couponId = -1;
    private TextView tvCoupon;
    private String payType;
    private String partnerId;
    private String prepayId;
    private String nonceStr;
    private String timestamp;
    private String sign1;
    private String sign;
    private static final int SDK_PAY_FLAG = 1;
    private boolean hasCouponData = false;//是否获得优惠券数据
    private int selectCouponCode = 0x001;
    private RelativeLayout cons_totalmount_rl;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(RepairPayDetailsActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        Bundle b = new Bundle();
                        b.putString("paymentResult", "支付成功");
                        finish();
//                        Constants.toActivity(RepairPayDetailsActivity.this, PaymentResultsActivity.class, b, true);
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(RepairPayDetailsActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                        Bundle b = new Bundle();
                        b.putString("paymentResult", "支付失败");
                        finish();
//                        Constants.toActivity(RepairPayDetailsActivity.this, PaymentResultsActivity.class, b, false);
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };
    private TextView tvConsumptionTotalAmount;
    private CWInfo cwInfo;
    private FrameLayout partLayout, worktimeLayout, tickeLayout;
    private TextView partAmoutTv, worktimeAmoutTv, ticketAmoutTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_detail);
        context = this;
        mHandler = new Handler(this);
        initIntentData();
        initView();
        couponData = new ArrayList<>();
        initRxManager();
        //拿到工单详情
        new GetWorkDetailAsync(mHandler, context, orderNum, flag).execute();
        if (null != orderNum) {
            //拿到可用优惠券
            new GetCouponAsync(mHandler, "0", orderNum, context).execute();
        }


    }

    private void initRxManager() {
        mRxManager.on(FINISH_PAY_DETAIL, new Action1<Object>() {
            @Override
            public void call(Object o) {
                finish();
            }
        });
    }

    @Override
    public void initPresenter() {

    }

    private void initView() {
        partLayout = (FrameLayout) findViewById(R.id.part_discount_layout);
        tickeLayout = (FrameLayout) findViewById(R.id.ticket_discount_layout);
        worktimeLayout = (FrameLayout) findViewById(R.id.worktime_discount_layout);
        partAmoutTv = (TextView) findViewById(R.id.part_discount_amout);
        worktimeAmoutTv = (TextView) findViewById(R.id.worktime_discount_amout);
        ticketAmoutTv = (TextView) findViewById(R.id.ticket_discount_amout);
        cons_totalmount_rl = (RelativeLayout) findViewById(R.id.rl_consumption_totalmount);
        cons_totalmount_rl.setOnClickListener(this);
        tvShouldPrice = (TextView) findViewById(R.id.tv_should_pay_price);
        infoFl = (FrameLayout) findViewById(R.id.pay_deviceinfo_fl);
        mRlWeiXinPay = (RelativeLayout) findViewById(R.id.rl_weixin_pay);
        infoFl.setOnClickListener(this);
        mRlWeiXinPay.setOnClickListener(this);
        mRlAliPay = (RelativeLayout) findViewById(R.id.rl_alipay);
        mRlAliPay.setOnClickListener(this);
        mRlCoupon = (RelativeLayout) findViewById(R.id.rl_coupon);
        mRlCoupon.setOnClickListener(this);
        tvManHour = (TextView) findViewById(R.id.tv_man_hour);
        mCbWeiXinPay = (CheckBox) findViewById(R.id.cb_weixin_pay);
        mCbAliPay = (CheckBox) findViewById(R.id.cb_alipay);
        tvPayPrice = (TextView) findViewById(R.id.tv_pay_price);
        btnToPay = (Button) findViewById(R.id.btn_topay);
        btnToPay.setOnClickListener(this);
        tvCoupon = (TextView) findViewById(R.id.select3);
        tvConsumptionTotalAmount = (TextView) findViewById(R.id.tv_consumption_totalmount);
        tvShouldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
    }


    private void initIntentData() {
        Intent intent = this.getIntent();
        Bundle buldle = intent.getExtras();
        orderNum = buldle.getString("orderNum");
        flag = buldle.getString("flag");
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case Constants.HANDLER_GET_CWDETAIL_SUCCESS:
                cwInfo = (CWInfo) msg.obj;
                if (null != cwInfo) {
                    WorkSettleBean workSettle = cwInfo.getWorkSettle();
                    if (null != workSettle) {
                       String nrepairworkhourcost = workSettle.getNrepairworkhourcost();
                        tvManHour.setText(nrepairworkhourcost);
                      String  npartstotalamount = workSettle.getNpartstotalamount();
                       String ndiscounttotalamount = workSettle.getNdiscounttotalamount();
                        tvConsumptionTotalAmount.setText("¥" + npartstotalamount);
//                        tvDiscountAmount.setText("-¥" + ndiscounttotalamount);
                        tvShouldPrice.setText("¥" + workSettle.getNloanamount());
                        if (!TextUtils.isEmpty(ndiscounttotalamount)) {
                            double a1 = Double.parseDouble(ndiscounttotalamount);
                            if (a1 > 0) {
                                partLayout.setVisibility(View.VISIBLE);
                                partAmoutTv.setText("-¥" + ndiscounttotalamount);
                            }
                        }
                        String hourCost = workSettle.getNdiscountworkhourcost();
                        if (!TextUtils.isEmpty(hourCost)) {
                            double a2 = Double.parseDouble(hourCost);
                            if (a2 > 0) {
                                worktimeLayout.setVisibility(View.VISIBLE);
                                worktimeAmoutTv.setText("-¥" + hourCost);
                            }
                        }
                        String nmaxamount = workSettle.getNmaxamount();
                        if (!TextUtils.isEmpty(nmaxamount)) {
                            double a3 = Double.parseDouble(nmaxamount);
                            if (a3 > 0) {
                                tickeLayout.setVisibility(View.VISIBLE);
                                ticketAmoutTv.setText("¥" + nmaxamount);
                            }
                        }
                    }
                }
                break;
            case Constants.HANDLER_GET_WORKDETAIL_FAILD:
                Constants.ToastAction((String) msg.obj);
                break;
            case Constants.HANDLER_GETCOUPONS_SUCCESS:
                couponData.clear();
                ArrayList<CouponInfo> data = (ArrayList<CouponInfo>) msg.obj;
                if (null != data && data.size() > 0) {
                    couponData.addAll(data);
                    hasCouponData = true;
                    couponCount = couponData.size();
                    couponId = couponData.get(0).getId();
                    if (data.size() > 0) {
                        tvCoupon.setText("-¥" + String.valueOf(data.get(0).getAmount()));
                    } else {
                        tvCoupon.setText("无可用");
                    }
                } else {
                    tvCoupon.setText("无可用");
                }
                //拿到需要支付金额
                new PayPriceAsync(context, mHandler, orderNum, "302", String.valueOf(couponId)).execute();
//                tvCoupon.setText("请选择");
                break;
            case Constants.HANDLER_GETCOUPONS_FAILD:
                Constants.ToastAction((String) msg.obj);
                break;
            case Constants.HANDLER_GETPAYPRICE_SUCCESS:
                PayPriceInfo payPriceInfo = (PayPriceInfo) msg.obj;
                String amount = payPriceInfo.getAmount();
                tvPayPrice.setText("实付： ¥" + amount);
                break;
            case Constants.HANDLER_GETPAYPRICE_FAILD:
                Constants.ToastAction((String) msg.obj);
                break;
            case Constants.HANDLER_GETCWPAY_SUCCESS:
                if (payType.equals("101")) {
                    WeiXinEntityBean entityBean = (WeiXinEntityBean) msg.obj;
                    partnerId = entityBean.getPartnerid();
                    prepayId = entityBean.getPrepayid();
                    nonceStr = entityBean.getNoncestr();
                    timestamp = entityBean.getTimestamp();
                    sign = entityBean.getSign();
                    payWeiXin();
                } else if (payType.equals("102")) {
                    AliPayBean aliPayBean = (AliPayBean) msg.obj;
                    String signInfo = aliPayBean.getSign();
                    payV2(signInfo);
                }
                break;
            case Constants.HANDLER_GETCWPAY_FAILD:
                Constants.ToastAction((String) msg.obj);
                break;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_consumption_totalmount:
                if (cwInfo == null) {
                    ToastUtils.showToast(this, "设备信息为空");
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.CWINFO, cwInfo);
                Constants.toActivity(this, ConsumptionActivity.class, bundle);
                break;

            case R.id.pay_deviceinfo_fl:
                if (cwInfo == null) {
                    ToastUtils.showToast(this, "设备信息为空");
                    return;
                }
                Bundle dfbundle = new Bundle();
                dfbundle.putSerializable(Constants.WORK_DETAIL, cwInfo.getWorkDetail());
                dfbundle.putStringArrayList(Constants.LOGO_LIST, cwInfo.getLogoList());
                Constants.toActivity(this, PayDeviceInfoActivity.class, dfbundle);
                break;
            case R.id.rl_alipay:
                mCbAliPay.setChecked(!mCbAliPay.isChecked());
                mCbWeiXinPay.setChecked(!mCbAliPay.isChecked());
                break;
            case R.id.rl_weixin_pay:
                mCbWeiXinPay.setChecked(!mCbWeiXinPay.isChecked());
                mCbAliPay.setChecked(!mCbWeiXinPay.isChecked());
                break;
            case R.id.rl_coupon:
                //showCounponDialog();
                Bundle cbundle = new Bundle();
                cbundle.putSerializable("couponData", couponData);
                Constants.toActivityForR(RepairPayDetailsActivity.this
                        , ViewRepairActivity.class
                        , cbundle, selectCouponCode);
                break;
            case R.id.btn_topay:
                if (!mCbAliPay.isChecked() && !mCbWeiXinPay.isChecked()) {
                    Constants.ToastAction("请选择一种支付方式");
                    return;
                }
                if (mCbWeiXinPay.isChecked()) {
                    payType = "101";
                } else if (mCbAliPay.isChecked()) {
                    payType = "102";
                }
                new CWPayAsync(mHandler, context, payType, orderNum, couponId).execute();
                break;
        }
    }

    //弹出优惠券列表
    private void showCounponDialog() {
        //查看优惠券弹窗除listview其余部分的高度
        int measureHeight = GetViewHeight(R.layout.dialog_counpon);
        //listview单个条目的高度
        int itemCouponHeight = GetViewHeight(R.layout.list_item_coupon);
        //Constants.MyLog("条目数量"+couponCount);
        if (couponCount <= 0) {
            return;
        }

        //总体高度
        int totalHeight = measureHeight + couponCount * itemCouponHeight + dip2px(60);
        //屏幕高度
        height = getScreenHeight();
        if (totalHeight > (height / 3) * 2) {
            couponHeight = (height / 3) * 2;
        } else {
            couponHeight = totalHeight;
        }

        initCouponView();
        couponDialog = new BottomSheetDialog(RepairPayDetailsActivity.this);
        couponDialog.contentView(root)
                .heightParam(couponHeight)
                .inDuration(500)
                .outDuration(500)
                .inInterpolator(new BounceInterpolator())
                .outInterpolator(new AnticipateInterpolator())
                .show();
    }

    private void initCouponView() {

        root = (RelativeLayout) LayoutInflater.from(RepairPayDetailsActivity.this).
                inflate(R.layout.dialog_counpon, null);
        btnCancel = (Button) root.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                couponDialog.dismiss();
            }
        });
        lvCoupon = (ListView) root.findViewById(R.id.lv_coupon);
        getCouponAdapter = new GetCouponAdapter(context, couponData, 0);
        lvCoupon.setAdapter(getCouponAdapter);
        getCouponAdapter.notifyDataSetChanged();
        lvCoupon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                couponId = couponData.get(position).getId();
                tvCoupon.setText("-¥" + String.valueOf(couponData.get(position).getAmount()));
                new PayPriceAsync(context, mHandler, orderNum, "302", String.valueOf(couponId)).execute();
                couponDialog.dismiss();
            }
        });
    }

    public int GetViewHeight(int resource) {

        View view = LayoutInflater.from(context).inflate(resource, null);
        view.setLayoutParams(new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT));
        int parameterW = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int parameterH = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(parameterW, parameterH);
        int measuredHeight = view.getMeasuredHeight();
        int measuredWidth = view.getMeasuredWidth();
        return measuredHeight;
    }

    public int getScreenHeight() {
        WindowManager wm = this.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        return height;
    }

    public int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void payV2(final String orderInfo) {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(RepairPayDetailsActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        };


        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();

    }

    //微信支付
    private void payWeiXin() {

        IWXAPI api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.registerApp(Constants.APP_ID);
        PayReq payRequest = new PayReq();
        payRequest.appId = Constants.APP_ID;
        payRequest.partnerId = partnerId;
        payRequest.prepayId = prepayId;
        payRequest.packageValue = "Sign=WXPay";
        payRequest.nonceStr = nonceStr;
        payRequest.timeStamp = timestamp;
        payRequest.sign = sign;
        api.sendReq(payRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        AppLog.print("onActivityResult____recode__" + requestCode + ", rescode__" + resultCode);
        if (requestCode == selectCouponCode) {
            if (data != null) {
                long couponId = data.getLongExtra("couponId", -1);
                String amount = data.getStringExtra("amount");
                tvCoupon.setText("-¥" + amount);
                new PayPriceAsync(context, mHandler, orderNum, "302", String.valueOf(couponId)).execute();
            }
        }
    }
}
