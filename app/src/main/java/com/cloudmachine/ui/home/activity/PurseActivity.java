package com.cloudmachine.ui.home.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alipay.sdk.app.AuthTask;
import com.cloudmachine.R;
import com.cloudmachine.activities.ViewCouponActivityNew;
import com.cloudmachine.autolayout.widgets.CustomDialog;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.base.baserx.RxSchedulers;
import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.bean.Member;
import com.cloudmachine.chart.utils.AppLog;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.ApiConstants;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.ui.home.contract.PurseContract;
import com.cloudmachine.ui.home.model.PurseModel;
import com.cloudmachine.ui.home.presenter.PursePresenter;
import com.cloudmachine.ui.homepage.activity.QuestionCommunityActivity;
import com.cloudmachine.utils.CommonUtils;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MemeberKeeper;
import com.cloudmachine.utils.ToastUtils;
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

/**
 * 钱包 1余额、押金 2.短信验证 3.支付宝/微信绑定
 */

public class PurseActivity extends BaseAutoLayoutActivity<PursePresenter, PurseModel> implements PurseContract.View {
    public static final String KEY_WALLETAMOUNT = "key_walletamout";
    public static final String KEY_DEPOSITAMOUNT = "key_depositamount";
    public static final int TYPE_ALIPAY = 11;
    public static final int TYPE_WX = 10;
    @BindView(R.id.purse_back_img)
    ImageView purseBackImg;
    @BindView(R.id.purse_balance_tv)
    TextView purseBalanceTv;
    @BindView(R.id.purse_aliplay_bind_tv)
    TextView purseAliplayBindTv;
    @BindView(R.id.purse_aplay_fl)
    FrameLayout purseAplayFl;
    @BindView(R.id.purse_wx_bind_tv)
    TextView purseWxBindTv;
    @BindView(R.id.purse_wx_fl)
    FrameLayout purseWxFl;
    @BindView(R.id.purse_coupon_num_tv)
    TextView purseCouponNumTv;
    @BindView(R.id.purse_coupon_fl)
    FrameLayout purseCouponFl;
    @BindView(R.id.purse_extraction_cash_btn)
    Button purseExtractionCashBtn;
    @BindView(R.id.purse_deposit_tv)
    TextView depositTv;
    @BindView(R.id.purse_instruction_img)
    ImageView instructionImg;
    UMShareAPI mShareAPI;
    String doBind;
    Member mMember;
    Timer mTimer;
    int timeOut = 60;//定时时长60s
    String walletAmountStr;
    CustomBindDialog identyfDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purse);
        ButterKnife.bind(this);
        initView();
    }


    private void initView() {
        doBind = getResources().getString(R.string.do_bind);
        double walletAmount = getIntent().getDoubleExtra(KEY_WALLETAMOUNT, 0);
        double despoitAmout = getIntent().getDoubleExtra(KEY_DEPOSITAMOUNT, 0);
        walletAmountStr = CommonUtils.formartPrice(String.valueOf(walletAmount));
        purseBalanceTv.setText(walletAmountStr);
        if (despoitAmout > 0) {
            depositTv.setVisibility(View.VISIBLE);
            depositTv.setText("押金" + CommonUtils.formartPrice(String.valueOf(despoitAmout)) + "元");
        }
        mMember = MemeberKeeper.getOauth(mContext);
        mPresenter.getMemberInfo(mMember.getId());
        mPresenter.getAvaildCouponList(mMember.getId());
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @OnClick({R.id.purse_instruction_img, R.id.purse_deposit_tv, R.id.purse_back_img, R.id.purse_aplay_fl, R.id.purse_wx_fl, R.id.purse_coupon_fl, R.id.purse_extraction_cash_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.purse_instruction_img:
                Bundle bundle = new Bundle();
                bundle.putString(QuestionCommunityActivity.H5_URL, ApiConstants.AppWalletHelper);
                Constants.toActivity(this, QuestionCommunityActivity.class, bundle);
                break;

            case R.id.purse_deposit_tv:
                Constants.toActivity(this, DepositActivity.class, null);
                break;


            case R.id.purse_back_img://返回
                finish();
                break;
            case R.id.purse_aplay_fl://支付宝绑定
                if (doBind.equals(purseAliplayBindTv.getText().toString())) {
                    mPresenter.getVerfyCode(TYPE_ALIPAY, mMember.getMobile());
                } else {
                    unBindAcount(TYPE_ALIPAY);
                }
                break;
            case R.id.purse_wx_fl://微信绑定
                if (doBind.equals(purseWxBindTv.getText().toString())) {
                    mPresenter.getVerfyCode(TYPE_WX, mMember.getMobile());
                } else {
                    unBindAcount(TYPE_WX);
                }
                break;
            case R.id.purse_coupon_fl://卡券
                Constants.toActivity(this, ViewCouponActivityNew.class, null, false);
                break;
            case R.id.purse_extraction_cash_btn://提现
                String text1 = purseAliplayBindTv.getText().toString();
                String text2 = purseWxBindTv.getText().toString();
                if (doBind.equals(text1) && doBind.equals(text2)) {

                    CustomDialog.Builder builder = new CustomDialog.Builder(this);
                    builder.setMessage("你还未绑定微信或支付宝账户?");
                    builder.setNeutralButton("知道了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                } else {
                    Bundle eb = new Bundle();
                    eb.putString(KEY_WALLETAMOUNT, walletAmountStr);
                    Constants.toActivity(this, ExtractionCashActivity.class, eb, false);
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
            mShareAPI.getPlatformInfo(PurseActivity.this, SHARE_MEDIA.WEIXIN, platformInfoListener);
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

    private void bindAliPay() {
//
//        mRxManager.add(Api.getDefault(HostType.HOST_CLOUDM).aliPayOpenAuth(UserHelper.getMemberId(this)).subscribeOn(Schedulers.io()).flatMap(new Func1<BaseRespose<String>, Observable<BaseRespose<JsonObject>>>() {
//            @Override
//            public Observable<BaseRespose<JsonObject>> call(BaseRespose<String> stringBaseRespose) {
//                String authResult = stringBaseRespose.getResult();
//                AppLog.print("authResult__" + authResult);
//                String authCode = null;
//                try {
//                    AuthTask task = new AuthTask(PurseActivity.this);
//                    Map<String, String> map = task.authV2(authResult, true);
//                    Thread.sleep(2000);
//                    if (map != null && map.containsKey("result")) {
//                        for (Map.Entry<String, String> entry : map.entrySet()) {
//                            AppLog.print("\nkey：" + entry.getKey() + ", value=" + entry.getValue());
//                        }
//                        String reslutValue = map.get("result");
//                        if (!TextUtils.isEmpty(reslutValue)) {
//                            String[] result = reslutValue.split("&");
//                            for (String r : result) {
//                                if (r.contains("auth_code")) {
//                                    String[] kv = r.split("=");
//                                    authCode = kv[1];
//                                    break;
//                                }
//                            }
//
//                        }
//                    }
//                } catch (Exception e) {
//
//                }
//                AppLog.print("authCode__" + authCode);
//                return Api.getDefault(HostType.HOST_CLOUDM).aliPayUserInfo(UserHelper.getMemberId(PurseActivity.this), authCode);
//            }
//        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new RxSubscriber<BaseRespose<JsonObject>>(mContext) {
//            @Override
//            protected void _onNext(BaseRespose<JsonObject> userResultRes) {
//                if (userResultRes != null) {
//                    JsonObject userResJobj = userResultRes.getResult();
//                    if (userResJobj != null) {
//                        String nickName = userResJobj.get("nickName").getAsString();
//                        String userId = userResJobj.get("userId").getAsString();
//                        AppLog.print("nickName__" + nickName + ", userid__" + userId);
//                        String acount;
//                        if (!TextUtils.isEmpty(nickName)) {
//                            acount = nickName;
//                        } else {
//                            acount = userId;
//                        }
//                        purseAliplayBindTv.setText(acount);
//                        mMember.setAlipayNickname(nickName);
//                        mMember.setAlipayUserId(userId);
//                    }
//                }
//
//            }
//
//            @Override
//            protected void _onError(String message) {
//                ToastUtils.showToast(mContext, "绑定失败, 请确认是否已安装支付且授权");
//
//            }
//        }));

        mRxManager.add(Api.getDefault(HostType.HOST_CLOUDM).aliPayOpenAuth(UserHelper.getMemberId(this)).compose(RxSchedulers.<BaseRespose<String>>io_main()).subscribe(new RxSubscriber<BaseRespose<String>>(mContext) {
            @Override
            protected void _onNext(BaseRespose<String> stringBaseRespose) {
                final String authResult = stringBaseRespose.getResult();
                AppLog.print("authResult__" + authResult);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        AuthTask task = new AuthTask(PurseActivity.this);
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

    private static final int AUTH_SUCESS = 0x12;

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
                Api.getDefault(HostType.HOST_CLOUDM).aliPayUserInfo(UserHelper.getMemberId(PurseActivity.this), authCode).compose(RxSchedulers.<BaseRespose<JsonObject>>io_main()).subscribe(new RxSubscriber<BaseRespose<JsonObject>>(mContext) {
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
                                    purseAliplayBindTv.setText(acount);
                                    mMember.setAlipayNickname(nickName);
                                    mMember.setAlipayUserId(userId);
                                }
                            } else {
                                ToastUtils.showToast(PurseActivity.this, userResultRes.getMessage());
                            }
                        }
                    }

                    @Override
                    protected void _onError(String message) {
                        ToastUtils.showToast(PurseActivity.this, message);

                    }
                });
            }

        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelTimer();

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


    @Override
    public void returnVerfyCode(final int type) {
        final CustomBindDialog.Builder builder = new CustomBindDialog.Builder(this);
        builder.setCodeGetListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                v.setEnabled(false);
                mPresenter.getVerfyCode(-1, mMember.getMobile());
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

    @Override
    public void returnToastError(String message) {
        ToastUtils.showToast(PurseActivity.this, message);
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


    @Override
    public void returnUnBind(int type, boolean isSuccess) {
        if (isSuccess) {
            ToastUtils.showToast(mContext, "解绑成功");
            if (type == TYPE_ALIPAY) {
                mMember.setAlipayUserId("");
                mMember.setAlipayNickname("");
                purseAliplayBindTv.setText(doBind);
            } else if (type == TYPE_WX) {
                mMember.setUnionId("");
                mMember.setOpenId("");
                mMember.setWecharNickname("");
                mMember.setWecharLogo("");
                purseWxBindTv.setText(doBind);
            }
        }

    }

    @Override
    public void updateBindWxUserView(String accountText) {
        purseWxBindTv.setText(accountText);
    }

    @Override
    public void updateAvaildCouponSumNum(int sumNum) {
        purseCouponNumTv.setText(sumNum + "张");

    }

    @Override
    public void returnMemberInfo(Member member) {
        if (member != null) {
            mMember = member;
            MemeberKeeper.saveOAuth(mMember, mContext);
        }
        String wxName = mMember.getWecharNickname();
        String openId = mMember.getOpenId();
        String alipayName = mMember.getAlipayNickname();
        String alipayUerId = mMember.getAlipayUserId();
        if (!TextUtils.isEmpty(openId)) {
            if (!(TextUtils.isEmpty(wxName))) {
                purseWxBindTv.setText(wxName);
            } else {
                purseWxBindTv.setText(openId);
            }
        }
        if (!TextUtils.isEmpty(alipayUerId)) {
            if (!TextUtils.isEmpty(alipayName)) {
                purseAliplayBindTv.setText(alipayName);
            } else {
                purseAliplayBindTv.setText(alipayUerId);
            }
        }


    }
}
