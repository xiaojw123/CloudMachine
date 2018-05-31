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
import com.cloudmachine.bean.AliPayBean;
import com.cloudmachine.bean.AllianceDetail;
import com.cloudmachine.bean.CWInfo;
import com.cloudmachine.bean.WeiXinEntityBean;
import com.cloudmachine.bean.WorkDetailBean;
import com.cloudmachine.bean.WorkSettleBean;
import com.cloudmachine.helper.MobEvent;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.net.task.CWPayAsync;
import com.cloudmachine.net.task.GetWorkDetailAsync;
import com.cloudmachine.ui.home.contract.ViewRepairContract;
import com.cloudmachine.ui.home.model.CouponItem;
import com.cloudmachine.ui.home.model.OrderCouponBean;
import com.cloudmachine.ui.home.model.ViewRepairModel;
import com.cloudmachine.ui.home.presenter.ViewRepairPresenter;
import com.cloudmachine.ui.repair.activity.ViewRepairActivity;
import com.cloudmachine.utils.CommonUtils;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.ToastUtils;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
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

public class RepairPayDetailsActivity extends BaseAutoLayoutActivity<ViewRepairPresenter, ViewRepairModel> implements ViewRepairContract.View, Handler.Callback, View.OnClickListener {
    private static final int REQ_CODE_SEL_COUPON = 0x001;
    public static final String FINISH_PAY_DETAIL = "finish_pay_detail";
    private static final String FORMAT_REAL_PAY = "实付： ¥%s";
    public static final String PAY_TYPE_WECHAT = "10";
    public static final String PAY_TYPE_ALIPAY = "11";
    //    @BindView(R.id.pay_deviceinfo_fl)
//    FrameLayout infoFl;
    @BindView(R.id.tv_should_pay_price)
    TextView tvShouldPrice;
    @BindView(R.id.rl_weixin_pay)
    RelativeLayout mRlWeiXinPay;
    @BindView(R.id.rl_alipay)
    RelativeLayout mRlAliPay;
    @BindView(R.id.rl_coupon)
    RelativeLayout mRlCoupon;
    @BindView(R.id.cb_weixin_pay)
    CheckBox mCbWeiXinPay;
    @BindView(R.id.cb_alipay)
    CheckBox mCbAliPay;
    @BindView(R.id.tv_pay_price)
    TextView tvPayPrice;
    @BindView(R.id.btn_topay)
    Button btnToPay;
    @BindView(R.id.select3)
    TextView tvCoupon;
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
    private OrderCouponBean mOrderCouponBean;
    private CWInfo cwInfo;
    private Handler mHandler;
    private double realPayAmount;
    private String payType;
    private String partnerId;
    private String prepayId;
    private String nonceStr;
    private String timestamp;
    private String sign;
    private String useCouponId;
    private String orderNum;
    private String flag;
    private String formartPayAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_detail);
        ButterKnife.bind(this);
        initParams();
        initView();
        initRxManager();
        boolean isAlliance = getIntent().getBooleanExtra("isAlliance", false);
        if (isAlliance) {
            if (UserHelper.isLogin(this)) {
                mPresenter.updateAllianceDetail(UserHelper.getMemberId(this), orderNum);
            }
        } else {
            new GetWorkDetailAsync(mHandler, this, orderNum, flag).execute();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onEvent(this, MobEvent.REPAIR_PAY_ORDER);
    }

    private void initView() {
        mRlCoupon.setEnabled(false);
        tvShouldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
    }


    private void initParams() {
        mHandler = new Handler(this);
        Intent intent = this.getIntent();
        Bundle buldle = intent.getExtras();
        orderNum = buldle.getString("orderNum");
        flag = buldle.getString("flag");
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
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
                    Bundle b = new Bundle();
                    b.putString("paymentResult", "支付成功");
                    finish();
                } else {
                    // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                    Toast.makeText(RepairPayDetailsActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    Bundle b = new Bundle();
                    b.putString("paymentResult", "支付失败");
                    finish();
                }
                break;
            case Constants.HANDLER_GET_CWDETAIL_SUCCESS:
                cwInfo = (CWInfo) msg.obj;
                if (null != cwInfo) {
                    WorkSettleBean workSettle = cwInfo.getWorkSettle();
                    if (null != workSettle) {
                        String nrepairworkhourcost = workSettle.getNrepairworkhourcost();
                        String npartstotalamount = workSettle.getNpartstotalamount();
                        String ndiscounttotalamount = workSettle.getNdiscounttotalamount();
//                        tvDiscountAmount.setText("-¥" + ndiscounttotalamount);
                        tvShouldPrice.setText("¥" + workSettle.getNtotalamount());
                        formartPayAmount = workSettle.getNloanamount();
                        realPayAmount = Double.parseDouble(formartPayAmount);
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
                        String npaidinamount = workSettle.getNpaidinamount();
                        if (!TextUtils.isEmpty(npaidinamount)) {
                            double a4 = Double.parseDouble(npaidinamount);
                            if (a4 > 0) {
                                costLayout.setVisibility(View.VISIBLE);
                                costAmouTv.setText("¥" + npaidinamount);
                            }
                        }

                    }
                    updateDeviceInfo(cwInfo.getWorkDetail(), cwInfo.getLogoList(), null);
                }
                mPresenter.getOrderCoupon(mRlCoupon, UserHelper.getMemberId(this), orderNum);
                break;
            case Constants.HANDLER_GET_WORKDETAIL_FAILD:
                Constants.ToastAction((String) msg.obj);
                break;
            case Constants.HANDLER_GETCOUPONS_FAILD:
                Constants.ToastAction((String) msg.obj);
                break;
            case Constants.HANDLER_GETPAYPRICE_FAILD:
                Constants.ToastAction((String) msg.obj);
                break;
            case Constants.HANDLER_GETCWPAY_SUCCESS:
                if (payType.equals(PAY_TYPE_WECHAT)) {
                    WeiXinEntityBean entityBean = (WeiXinEntityBean) msg.obj;
                    partnerId = entityBean.getPartnerid();
                    prepayId = entityBean.getPrepayid();
                    nonceStr = entityBean.getNoncestr();
                    timestamp = entityBean.getTimestamp();
                    sign = entityBean.getSign();
                    payWeiXin();
                } else if (payType.equals(PAY_TYPE_ALIPAY)) {
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

    public void updateDeviceInfo(WorkDetailBean bean, ArrayList<String> logoList, String flag) {
        //刷新耗件详情列表
        if (bean == null) {
            return;
        }
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
        tvBrand.setText(bean.getVbrandname());
        //设置机器型号
        tvModel.setText(bean.getVmaterialname());
//        rdDevicenameTv.setText(detailBean.getVbrandname());
//        rdDevicenoTv.setText(detailBean.getVmaterialname());
//        rdDescriptionTv.setText(detailBean.getVdiscription());

        noTv.setText(bean.getVmachinenum());
        locTv.setText(bean.getVworkaddress());
        String description;
        if ("0".equals(flag)) {
            description = bean.getVdiscription();
        } else {
            description = bean.getCusdemanddesc();
        }
        desTv.setText(description);
    }


    @OnClick({R.id.rl_alipay, R.id.rl_weixin_pay, R.id.rl_coupon, R.id.btn_topay})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.rl_consumption_totalmount:
//                if (cwInfo == null) {
//                    ToastUtils.showToast(this, "设备信息为空");
//                    return;
//                }
//                Bundle bundle = new Bundle();
//                bundle.putSerializable(Constants.CWINFO, cwInfo);
//                Constants.toActivity(this, ConsumptionActivity.class, bundle);
//                break;
// TODO: 2017/12/4  设备信息改
//            case R.id.pay_deviceinfo_fl:
//                if (cwInfo == null) {
//                    ToastUtils.showToast(this, "设备信息为空");
//                    return;
//                }
//                Bundle dfbundle = new Bundle();
//                dfbundle.putSerializable(Constants.WORK_DETAIL, cwInfo.getWorkDetail());
//                dfbundle.putStringArrayList(Constants.LOGO_LIST, cwInfo.getLogoList());
//                Constants.toActivity(this, PayDeviceInfoActivity.class, dfbundle);
//                break;
            case R.id.rl_alipay:
                mCbAliPay.setChecked(!mCbAliPay.isChecked());
                mCbWeiXinPay.setChecked(!mCbAliPay.isChecked());
                break;
            case R.id.rl_weixin_pay:
                mCbWeiXinPay.setChecked(!mCbWeiXinPay.isChecked());
                mCbAliPay.setChecked(!mCbWeiXinPay.isChecked());
                break;
            case R.id.rl_coupon:
                if (mOrderCouponBean != null) {
                    Bundle cbundle = new Bundle();
                    cbundle.putParcelable(ViewRepairActivity.ORDER_COUPON, mOrderCouponBean);
                    if ("不使用".equals(tvCoupon.getText())) {
                        cbundle.putBoolean(ViewRepairActivity.KEY_IS_USED, false);
                    } else {
                        cbundle.putBoolean(ViewRepairActivity.KEY_IS_USED, true);
                    }
                    Constants.toActivityForR(RepairPayDetailsActivity.this
                            , ViewRepairActivity.class
                            , cbundle, REQ_CODE_SEL_COUPON);
                } else {
                    ToastUtils.showToast(this, "暂无可用优惠券");
                }
                break;
            case R.id.btn_topay:
                if (!mCbAliPay.isChecked() && !mCbWeiXinPay.isChecked()) {
                    Constants.ToastAction("请选择一种支付方式");
                    return;
                }
                if (mCbWeiXinPay.isChecked()) {
                    MobclickAgent.onEvent(this, MobEvent.COUNT_PAY_WECHAT);
                    payType = PAY_TYPE_WECHAT;
                } else if (mCbAliPay.isChecked()) {
                    MobclickAgent.onEvent(this, MobEvent.COUNT_PAY_ALIPAY);
                    payType = PAY_TYPE_ALIPAY;
                }
                new CWPayAsync(mHandler, this, payType, orderNum, useCouponId).execute();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_SEL_COUPON) {
            if (data != null) {
                boolean isCouponUsed = data.getBooleanExtra(ViewRepairActivity.KEY_IS_USED, false);
                if (isCouponUsed) {
                    useCouponId = data.getStringExtra("couponId");
                    int amount = data.getIntExtra("amount", 0);
                    tvCoupon.setText("-¥" + amount);
                    tvPayPrice.setText(String.format(FORMAT_REAL_PAY, CommonUtils.formartPrice(String.valueOf(CommonUtils.subtractDouble(realPayAmount, amount)))));
                } else {
                    tvCoupon.setText("不使用");
                    tvPayPrice.setText(String.format(FORMAT_REAL_PAY, formartPayAmount));
                }
            }
        }
    }

    @Override
    public void updateOrderCouponView(OrderCouponBean orderCouponBean) {
        if (orderCouponBean != null && orderCouponBean.getUseAmount() > 0) {
            useCouponId = "";
            mOrderCouponBean = orderCouponBean;
            for (CouponItem item : mOrderCouponBean.getCouponList()) {
                int useNum = item.getUseNum();
                if (useNum > 0) {
                    if (useCouponId.contains("_")) {
                        useCouponId += ",";
                    }
                    useCouponId += item.getCouponBaseId() + "_" + useNum;
                }
            }
            tvCoupon.setText("-¥" + String.valueOf(orderCouponBean.getUseAmount()));
            tvPayPrice.setText(String.format(FORMAT_REAL_PAY, CommonUtils.formartPrice(String.valueOf(CommonUtils.subtractDouble(realPayAmount, orderCouponBean.getUseAmount())))));
        } else {
            tvCoupon.setText("不使用");
            tvPayPrice.setText(String.format(FORMAT_REAL_PAY, formartPayAmount));
        }
    }

    @Override
    public void updateOrderCouponError() {
        tvPayPrice.setText(String.format(FORMAT_REAL_PAY, formartPayAmount));
    }

    @Override
    public void returnAllianceDetail(AllianceDetail detail) {
        CWInfo info = new CWInfo();
        WorkSettleBean settleBean = new WorkSettleBean();
        String toalAmount=detail.getTotalAmount();
        String debtAmount=detail.getDebtAmount();
        if (TextUtils.equals(toalAmount,debtAmount)){
            showPayContainer.setVisibility(View.GONE);
        }else{
            showPayContainer.setVisibility(View.VISIBLE);
        }
        settleBean.setNtotalamount(toalAmount);//应付
        settleBean.setNloanamount(debtAmount);
        List<String> urls = detail.getAttachmentUrls();
        if (urls != null && urls.size() > 0) {
            info.setLogoList((ArrayList<String>) urls);
        }
        info.setWorkSettle(settleBean);
        WorkDetailBean detailBean = new WorkDetailBean();
        detailBean.setVbrandname(detail.getBrandName());
        detailBean.setVmaterialname(detail.getModelName());
        detailBean.setVmachinenum(detail.getMachineNum());
        detailBean.setCusdemanddesc(detail.getDemandDescription());
        detailBean.setVworkaddress(detail.getAddressDetail());
        info.setWorkDetail(detailBean);
        Message msg = new Message();
        msg.obj = info;
        msg.what = Constants.HANDLER_GET_CWDETAIL_SUCCESS;
        mHandler.sendMessage(msg);
    }


}
