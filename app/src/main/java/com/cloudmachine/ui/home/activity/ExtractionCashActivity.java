package com.cloudmachine.ui.home.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.AuthTask;
import com.cloudmachine.R;
import com.cloudmachine.autolayout.widgets.CustomDialog;
import com.cloudmachine.autolayout.widgets.RadiusButtonView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.base.baserx.RxSchedulers;
import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.bean.Member;
import com.cloudmachine.chart.utils.AppLog;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.ui.home.contract.ExtrContract;
import com.cloudmachine.ui.home.model.ExtrModel;
import com.cloudmachine.ui.home.presenter.ExtrPresenter;
import com.cloudmachine.utils.CommonUtils;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.InputMoney;
import com.cloudmachine.utils.MemeberKeeper;
import com.cloudmachine.utils.ToastUtils;
import com.cloudmachine.utils.widgets.ClearEditTextView;
import com.cloudmachine.widget.CustomBindDialog;
import com.google.gson.JsonObject;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExtractionCashActivity extends BaseAutoLayoutActivity<ExtrPresenter, ExtrModel> implements ExtrContract.View {
    private static final String FOMART_BALANCE="可用余额%1$s元";
    public static final int TYPE_ALIPAY = 11;
    public static final int TYPE_WX = 10;
    private static final int AUTH_SUCESS = 0x12;
    @BindView(R.id.extr_cash_alipay_cb)
    CheckBox extrCashAlipayCb;
    @BindView(R.id.extr_cash_alipay_rl)
    RelativeLayout extrCashAlipayRl;
    @BindView(R.id.extr_cash_wxpay_cb)
    CheckBox extrCashWxpayCb;
    @BindView(R.id.extr_cash_wxpay_rl)
    RelativeLayout extrCashWxpayRl;
    @BindView(R.id.extr_cash_sure_btn)
    RadiusButtonView extrCashSureBtn;
    @BindView(R.id.wx_acount_llt)
    LinearLayout wxAccountLlt;
    @BindView(R.id.alipay_account_llt)
    LinearLayout aliPayAccountLlt;
    @BindView(R.id.wx_bind_tv)
    TextView wxBindTv;
    @BindView(R.id.alipay_bind_tv)
    TextView alipayBindTv;
    @BindView(R.id.extr_cash_edt)
    ClearEditTextView cashEdt;
    @BindView(R.id.extr_cash_balance)
    TextView balanceTv;
    @BindView(R.id.extr_cash_all)
    TextView cashAllTv;





    int mType;
    String walletAmountStr;
    Member mMember;
    CustomBindDialog identyfDialog;
    Timer mTimer;
    int timeOut = 60;//定时时长60s
    UMShareAPI mShareAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extraction_cash);
        ButterKnife.bind(this);
        cashEdt.setFilters(new InputFilter[]{new InputMoney(cashEdt)});
        wxAccountLlt.setEnabled(false);
        aliPayAccountLlt.setEnabled(false);
        walletAmountStr = getIntent().getStringExtra(PurseActivity.KEY_WALLETAMOUNT);
        balanceTv.setText(String.format(FOMART_BALANCE,walletAmountStr));
        mMember = (Member) getIntent().getSerializableExtra(Constants.MC_MEMBER);
        mPresenter.getMemberInfo(mMember.getId());
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);

    }

    @OnClick({R.id.extr_cash_all,R.id.wx_acount_llt, R.id.alipay_account_llt, R.id.extr_cash_alipay_rl, R.id.extr_cash_wxpay_rl, R.id.extr_cash_sure_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.extr_cash_all:
                cashEdt.setText(walletAmountStr);
                break;

            case R.id.extr_cash_alipay_rl:
                String aliPayText = alipayBindTv.getText().toString();
                if (TextUtils.equals(getString(R.string.bind_alipay_by_click), aliPayText)) {
                    mPresenter.getVerfyCode(TYPE_ALIPAY, mMember.getMobile());
                } else {
                    setAliPaySelected();
                }
                break;
            case R.id.extr_cash_wxpay_rl:
                String wxPayText = wxBindTv.getText().toString();
                if (TextUtils.equals(getString(R.string.bind_wx_by_click), wxPayText)) {
                    mPresenter.getVerfyCode(TYPE_WX, mMember.getMobile());
                } else {
                    setWxPaySelected();
                }
                break;
            case R.id.wx_acount_llt:
                unBindAcount(TYPE_WX);
                break;
            case R.id.alipay_account_llt:
                unBindAcount(TYPE_ALIPAY);
                break;

            case R.id.radius_button_text:
                if (extrCashWxpayCb.isChecked() || extrCashAlipayCb.isChecked()) {
                    cashOutAmout();
                }else{
                    ToastUtils.showToast(mContext,"请选择提现账户");
                }
                break;
        }
    }

    private void unBindAcount(final int type) {
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        String message = "你确定要解绑";
        if (type == TYPE_ALIPAY) {
            message = message + "支付宝账号?";
        } else if (type == TYPE_WX) {
            message = message + "微信账号?";
        }
        builder.setMessage(message);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                mPresenter.unBind(mMember.getId(), type);
            }
        });
        builder.create().show();
    }

    private void cashOutAmout() {
       final String cashAmout=cashEdt.getText().toString();
        if (TextUtils.isEmpty(cashAmout)){
            ToastUtils.showToast(mContext,"提现金额不可为空!");
            return;
        }
        mRxManager.add(Api.getDefault(HostType.HOST_CLOUDM).cashOut(UserHelper.getMemberId(this), mType,cashAmout).compose(RxSchedulers.<BaseRespose>io_main()).subscribe(new RxSubscriber<BaseRespose>(mContext) {
            @Override
            protected void _onNext(BaseRespose baseRespose) {
                AppLog.print("Retrofit Res__"+baseRespose);
                if (baseRespose.success()) {
                    CommonUtils.showSuccessDialog(mContext, "提现成功", cashAmout, "退款将在3-7个中作日到账");
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


    @Override
    public void returnVerfyCode(final int type) {
        final CustomBindDialog.Builder builder = new CustomBindDialog.Builder(this);
        builder.setCodeGetListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                v.setEnabled(false);
                mPresenter.getVerfyCode(type, mMember.getMobile());
                startTimer(v);
                //获取验证码接口
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
                    mPresenter.identifyCode(type, mMember.getMobile(), code);
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

    private void cancelTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }


    @Override
    public void returnIdentifyCode(int type) {
        if (identyfDialog != null && identyfDialog.isShowing()) {
            identyfDialog.dismiss();
        }
        if (type == TYPE_ALIPAY) {
            bindAliPay();
        } else if (type == TYPE_WX) {
            bindWxPay();
        }

    }

    private void bindWxPay() {

        mShareAPI = UMShareAPI.get(this);
        mShareAPI.doOauthVerify(this, SHARE_MEDIA.WEIXIN, authListener);

    }

    private UMAuthListener authListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            mShareAPI.getPlatformInfo(ExtractionCashActivity.this, SHARE_MEDIA.WEIXIN, platformInfoListener);
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            ToastUtils.showToast(mContext, "授权失败！！" + throwable.getMessage());
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
            ToastUtils.showToast(mContext, "取消授权！！");
        }
    };


    private UMAuthListener platformInfoListener = new UMAuthListener() {

        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            final String nickName = map.get("name");
            final String openId = map.get("openid");
            String unionid = map.get("unionid");
            String headUrl = map.get("iconurl");
            mMember.setWecharNickname(nickName);
            mMember.setOpenId(openId);
            mMember.setUnionId(unionid);
            mMember.setWecharLogo(headUrl);
            Map<String, String> pm = new HashMap<>();
            pm.put("memberId", String.valueOf(mMember.getId()));
            pm.put("openId", openId);
            pm.put("unionId", unionid);
            pm.put("nickName", nickName);
            pm.put("headLogo", headUrl);
            mPresenter.bindWxUser(pm, nickName, openId);
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {

        }
    };

    @Override
    public void returnUnBind(int type, boolean isSuccess) {
        if (isSuccess) {
            ToastUtils.showToast(mContext, "解绑成功");
            if (type == TYPE_ALIPAY) {
                mMember.setAlipayUserId("");
                mMember.setAlipayNickname("");
                alipayBindTv.setText(getString(R.string.bind_alipay_by_click));
                aliPayAccountLlt.setEnabled(false);
                extrCashAlipayCb.setChecked(false);
            } else if (type == TYPE_WX) {
                mMember.setUnionId("");
                mMember.setOpenId("");
                mMember.setWecharNickname("");
                mMember.setWecharLogo("");
                wxBindTv.setText(getString(R.string.bind_wx_by_click));
                wxAccountLlt.setEnabled(false);
                extrCashWxpayCb.setChecked(false);
            }
            mType = 0;
        }

    }


    @Override
    public void updateBindWxUserView(String accountText) {
        wxBindTv.setText(accountText);
        wxAccountLlt.setEnabled(true);
        setWxPaySelected();
    }


    @Override
    public void returnMemberInfo(Member member) {
        if (member != null) {
            mMember = member;
            MemeberKeeper.saveOAuth(member, mContext);
            String wxName = member.getWecharNickname();
            String openId = member.getOpenId();
            String alipayName = member.getAlipayNickname();
            String alipayUerId = member.getAlipayUserId();
            if (!TextUtils.isEmpty(openId)) {
                if (!(TextUtils.isEmpty(wxName))) {
                    wxBindTv.setText(wxName);
                } else {
                    wxBindTv.setText(openId);
                }
                wxAccountLlt.setEnabled(true);
            }
            if (!TextUtils.isEmpty(alipayUerId)) {
                if (!TextUtils.isEmpty(alipayName)) {
                    alipayBindTv.setText(alipayName);
                } else {
                    alipayBindTv.setText(alipayUerId);
                }
                aliPayAccountLlt.setEnabled(true);
            }
        }


    }

    private void bindAliPay() {

        mRxManager.add(Api.getDefault(HostType.HOST_CLOUDM).aliPayOpenAuth(UserHelper.getMemberId(this)).compose(RxSchedulers.<BaseRespose<String>>io_main()).subscribe(new RxSubscriber<BaseRespose<String>>(mContext) {
            @Override
            protected void _onNext(BaseRespose<String> stringBaseRespose) {
                final String authResult = stringBaseRespose.getResult();
                AppLog.print("authResult__" + authResult);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        AuthTask task = new AuthTask(ExtractionCashActivity.this);
                        AppLog.print("authTask___start");
                        Map<String, String> map = task.authV2(authResult, true);
                        AppLog.print("authTask___authV2__map__" + map);
                        Message message = new Message();
                        message.what = AUTH_SUCESS;
                        message.obj = map;
                        mHandler.sendMessage(message);
                    }
                }).start();


            }

            @Override
            protected void _onError(String message) {

            }
        }));


    }


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == AUTH_SUCESS) {
                String authCode = null;
                Object obj = msg.obj;
                if (obj != null && obj instanceof Map) {
                    Map<String, String> map = (Map<String, String>) obj;
                    if (map.containsKey("result")) {
                        for (Map.Entry<String, String> entry : map.entrySet()) {
                            AppLog.print("\nkey：" + entry.getKey() + ", value=" + entry.getValue());
                        }
                        String reslutValue = map.get("result");
                        if (!TextUtils.isEmpty(reslutValue)) {
                            String[] result = reslutValue.split("&");
                            for (String r : result) {
                                if (r.contains("auth_code")) {
                                    String[] kv = r.split("=");
                                    authCode = kv[1];
                                    break;
                                }
                            }

                        }
                    }
                }
                AppLog.print("authCode__" + authCode);
                Api.getDefault(HostType.HOST_CLOUDM).aliPayUserInfo(UserHelper.getMemberId(mContext), authCode).compose(RxSchedulers.<BaseRespose<JsonObject>>io_main()).subscribe(new RxSubscriber<BaseRespose<JsonObject>>(mContext) {
                    @Override
                    protected void _onNext(BaseRespose<JsonObject> userResultRes) {
                        if (userResultRes != null) {
                            if (userResultRes.success()) {
                                JsonObject userResJobj = userResultRes.getResult();
                                if (userResJobj != null) {
                                    String nickName = userResJobj.get("nickName").getAsString();
                                    String userId = userResJobj.get("userId").getAsString();
                                    AppLog.print("nickName__" + nickName + ", userid__" + userId);
                                    String acount;
                                    if (!TextUtils.isEmpty(nickName)) {
                                        acount = nickName;
                                    } else {
                                        acount = userId;
                                    }
                                    alipayBindTv.setText(acount);
                                    aliPayAccountLlt.setEnabled(true);
                                    mMember.setAlipayNickname(nickName);
                                    mMember.setAlipayUserId(userId);
                                    setAliPaySelected();
                                }
                            } else {
                                ToastUtils.showToast(mContext, userResultRes.getMessage());
                            }
                        }
                    }

                    @Override
                    protected void _onError(String message) {
                        ToastUtils.showToast(mContext
                                , message);

                    }
                });
            }

        }
    };

    private void setAliPaySelected() {
        mType = TYPE_ALIPAY;
        extrCashAlipayCb.setChecked(true);
        if (extrCashWxpayCb.isChecked()) {
            extrCashWxpayCb.setChecked(false);
        }
    }

    private void setWxPaySelected() {
        mType = TYPE_WX;
        extrCashWxpayCb.setChecked(true);
        if (extrCashAlipayCb.isChecked()) {
            extrCashAlipayCb.setChecked(false);
        }
    }


}
