package com.cloudmachine.activities;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.cloudmachine.R;
import com.cloudmachine.adapter.PhotoListAdapter;
import com.cloudmachine.adapter.decoration.SpaceItemDecoration;
import com.cloudmachine.alipay.PayResult;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.bean.AliPayBean;
import com.cloudmachine.bean.RepairDetail;
import com.cloudmachine.bean.WeiXinEntityBean;
import com.cloudmachine.helper.MobEvent;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.net.task.CWPayAsync;
import com.cloudmachine.utils.CommonUtils;
import com.cloudmachine.utils.Constants;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
    private static final String FORMAT_REAL_PAY = "实付： ¥%s";
    @BindView(R.id.tv_should_pay_price)
    TextView tvShouldPrice;
    @BindView(R.id.rl_weixin_pay)
    RelativeLayout mRlWeiXinPay;
    @BindView(R.id.rl_alipay)
    RelativeLayout mRlAliPay;
    @BindView(R.id.cb_weixin_pay)
    CheckBox mCbWeiXinPay;
    @BindView(R.id.cb_alipay)
    CheckBox mCbAliPay;
    @BindView(R.id.tv_pay_price)
    TextView tvPayPrice;
    @BindView(R.id.btn_topay)
    Button btnToPay;
    @BindView(R.id.part_discount_layout)
    FrameLayout partLayout;
    @BindView(R.id.worktime_discount_layout)
    FrameLayout worktimeLayout;
    @BindView(R.id.ticket_discount_layout)
    FrameLayout tickeLayout;
    @BindView(R.id.cost_discount_layout)
    FrameLayout costLayout;
    @BindView(R.id.part_discount_amout)
    TextView partAmoutTv;
    @BindView(R.id.worktime_discount_amout)
    TextView worktimeAmoutTv;
    @BindView(R.id.ticket_discount_amout)
    TextView ticketAmoutTv;
    @BindView(R.id.cost_discount_amout)
    TextView costAmouTv;
    @BindView(R.id.tv_device_brand)
    TextView tvBrand;
    @BindView(R.id.tv_device_model)
    TextView tvModel;
    @BindView(R.id.pay_di_des)
    TextView desTv;
    @BindView(R.id.pay_di_dno)
    TextView noTv;
    @BindView(R.id.pay_di_loc)
    TextView locTv;
    @BindView(R.id.device_info_rlv)
    RecyclerView infoImgRlv;
    @BindView(R.id.device_info_title)
    TextView imgTitleTv;
    @BindView(R.id.tv_should_pay_cotainer)
    LinearLayout showPayContainer;
    private Handler mHandler;
    private int payType=-1;
    private String partnerId;
    private String prepayId;
    private String nonceStr;
    private String timestamp;
    private String sign;
    private String orderNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_detail);
        ButterKnife.bind(this);
        initParams();
        initRxManager();
        getPayDetail();
    }

    private void getPayDetail() {
        tvShouldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        mRxManager.add(Api.getDefault(HostType.HOST_LARK).getRepairDetail(orderNum)
                .compose(RxHelper.<RepairDetail>handleResult()).subscribe(new RxSubscriber<RepairDetail>(mContext) {
                    @Override
                    protected void _onNext(RepairDetail detail) {
                        updateDetail(detail);
                    }

                    @Override
                    protected void _onError(String message) {

                    }
                }));


    }


    public void updateDetail(RepairDetail detail) {
        if (null != detail) {
            List<String> logoList = detail.getAttachmentUrls();
            if (logoList != null && logoList.size() > 0) {
                imgTitleTv.setVisibility(View.VISIBLE);
                infoImgRlv.setVisibility(View.VISIBLE);
                infoImgRlv.setNestedScrollingEnabled(false);
                infoImgRlv.setLayoutManager(new GridLayoutManager(this, 3));
                infoImgRlv.addItemDecoration(new SpaceItemDecoration(this, 5));
                PhotoListAdapter adapter = new PhotoListAdapter();
                adapter.updateItems(logoList);
                infoImgRlv.setAdapter(adapter);
            }
            //设置机器品牌
            tvBrand.setText(detail.getBrandName());
            //设置机器型号
            tvModel.setText(detail.getModelName());
            noTv.setText(detail.getRackId());
            locTv.setText(detail.getAddressDetail());
            desTv.setText(detail.getServiceDesc());
            tvPayPrice.setText(String.format(FORMAT_REAL_PAY, CommonUtils.formartPrice(String.valueOf(detail.getTotalAmount()))));
            tvShouldPrice.setText("¥"+detail.getDebtAmount());
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onEvent(this, MobEvent.REPAIR_PAY_ORDER);
    }



    private void initParams() {
        mHandler = new Handler(this);
        Intent intent = this.getIntent();
        Bundle buldle = intent.getExtras();
        orderNum = buldle.getString(Constants.ORDER_NO);
    }

    @Override
    public void initPresenter() {
    }

    private void initRxManager() {
        mRxManager.on(FINISH_PAY_DETAIL, new Action1<String>() {
            @Override
            public void call(String o) {
                Constants.ToastAction(o);
                finish();
            }
        });
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case Constants.HANDLER_RESULT_APLIPAY:
                PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                String resultStatus = payResult.getResultStatus();
                // 判断resultStatus 为9000则代表支付成功
                if (TextUtils.equals(resultStatus, "9000")) {
                    // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                    Toast.makeText(RepairPayDetailsActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                    Toast.makeText(RepairPayDetailsActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            case Constants.HANDLER_GETCWPAY_SUCCESS:
                if (payType==Constants.PAY_TYPE_WX) {
                    WeiXinEntityBean entityBean = (WeiXinEntityBean) msg.obj;
                    if (entityBean != null) {
                        WeiXinEntityBean.SignAgain signAgain = entityBean.getSignAgain();
                        if (signAgain != null) {
                            partnerId = signAgain.getPartnerid();
                            prepayId = signAgain.getPrepayid();
                            nonceStr = signAgain.getNoncestr();
                            timestamp = signAgain.getTimestamp();
                            sign = signAgain.getSign();
                            payWeiXin();
                        }
                    }
                } else if (payType==Constants.PAY_TYPE_ALIPAY) {
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



    @OnClick({R.id.rl_alipay, R.id.rl_weixin_pay, R.id.btn_topay})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_alipay:
                mCbAliPay.setChecked(!mCbAliPay.isChecked());
                mCbWeiXinPay.setChecked(!mCbAliPay.isChecked());
                break;
            case R.id.rl_weixin_pay:
                mCbWeiXinPay.setChecked(!mCbWeiXinPay.isChecked());
                mCbAliPay.setChecked(!mCbWeiXinPay.isChecked());
                break;
            case R.id.btn_topay:
                if (!mCbAliPay.isChecked() && !mCbWeiXinPay.isChecked()) {
                    Constants.ToastAction("请选择一种支付方式");
                    return;
                }
                if (mCbWeiXinPay.isChecked()) {
                    MobclickAgent.onEvent(this, MobEvent.COUNT_PAY_WECHAT);
                    payType = Constants.PAY_TYPE_WX;
                } else if (mCbAliPay.isChecked()) {
                    MobclickAgent.onEvent(this, MobEvent.COUNT_PAY_ALIPAY);
                    payType = Constants.PAY_TYPE_ALIPAY;
                }
                new CWPayAsync(mHandler, payType, orderNum).execute();
                break;
        }
    }


    public void payV2(final String orderInfo) {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(RepairPayDetailsActivity.this);
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





}
