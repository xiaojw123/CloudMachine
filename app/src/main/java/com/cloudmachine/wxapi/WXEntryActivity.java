package com.cloudmachine.wxapi;


import com.umeng.weixin.callback.WXCallbackActivity;


public class WXEntryActivity extends WXCallbackActivity{

//    private IWXAPI api;
//    private static final String APP_SECRET = "3c69a7395f5e54009accf1e1194d553c";
//    private static final int MSG_SET_ALIAS = 1001;
//    private Handler mHandler;
//    public RxManager mRxManager;
//    private Context mContext;
//    private String mNickname;
//    private String mHeadimgurl;
//    private int mSex;
//    private String mUnionid;
//    private String mOpenId;
//    private Member member;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_wxentry);
//        AppLog.print("wxentryActivity__oncreate_");
//        mRxManager = new RxManager();
//        mHandler = new Handler(this);
//        mContext = this;
//        api = WXAPIFactory.createWXAPI(this, WeChatShareUtil.APP_ID, false);
//        api.handleIntent(getIntent(), this);
//        //是否调用finish（）方法
//        // finish();
//    }
//
//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        setIntent(intent);
//        api.handleIntent(intent, this);//必须调用此句话
//    }
//
//    //微信发送的请求将回调到onReq方法
//    @Override
//    public void onReq(BaseReq baseReq) {
//
//    }
//
//    @Override
//    public void onResp(BaseResp baseResp) {
//        String result;
//        switch (baseResp.errCode) {
//            case BaseResp.ErrCode.ERR_OK:
//                result = "分享成功";
//                //发送成功
//                if (baseResp instanceof SendAuth.Resp) {
//                    SendAuth.Resp sendResp = (SendAuth.Resp) baseResp;
//                    AppLog.print("sendResp");
//                    String code = sendResp.code;
//                    getAccess_token(code);
//                } else {
//                    finish();
//                }
//                break;
//            case BaseResp.ErrCode.ERR_USER_CANCEL:
//                result = null;
//                finish();
//                break;
//            case BaseResp.ErrCode.ERR_AUTH_DENIED:
//                //发送被拒绝
//                result = "分享失败";
//                finish();
//                break;
//            default:
//                result = "分享失败";
//                finish();
//                break;
//        }
//        if (result != null) {
//            Toast.makeText(this, baseResp.errCode, Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    /**
//     * 获取openid accessToken值用于后期操作
//     *
//     * @param code 请求码
//     */
//    private void getAccess_token(String code) {
//
//        new GetAccessTokenAsync(this, mHandler, Constants.APP_ID, APP_SECRET,
//                code, "authorization_code").execute();
//    }
//
//    /**
//     * 拿到 access_token,openid
//     *
//     * @param message
//     * @return
//     */
//    @Override
//    public boolean handleMessage(Message message) {
//
//        Bundle bundle = message.getData();
//
//        switch (message.what) {
//            case Constants.HANDLER_GETACCESSTOKEN_SUCCESS:
//                AppLog.print("HANDLER_GETACCESSTOKEN_SUCCESS_____");
//                String openid = bundle.getString("openid");
//                String access_token = bundle.getString("access_token");
//                AppLog.print("access_token___" + access_token + ", openid___" + openid);
//                if (!TextUtils.isEmpty(access_token) && !TextUtils.isEmpty(openid)) {
//                    AppLog.print("getUserMsg____");
//                    getUserMsg(access_token, openid);
//                } else {
//                    AppLog.print("finish____");
//                }
//                break;
//            case Constants.HANDLER_GETUSERMSG_SUCCESS:
//                AppLog.print("HANDLER_GETUSERMSG_SUCCESS_____");
//                mNickname = bundle.getString("nickname");
//                mHeadimgurl = bundle.getString("headimgurl");
//                mSex = bundle.getInt("sex");
//                mUnionid = bundle.getString("unionid");
//                mOpenId = bundle.getString("openid");
//                //是否请求服务器
//                switchWXLogin(mUnionid, mOpenId, mNickname, mHeadimgurl, mSex);
//                break;
//            case MSG_SET_ALIAS:
//                JPushInterface.setAliasAndTags(getApplicationContext(), (String) message.obj, null, mAliasCallback);
//                break;
//        }
//        return false;
//    }
//
//    private void switchWXLogin(final String unionid, final String openId, final String nickname, final String headimgurl, final int sex) {
//
//        mRxManager.add(Api.getDefault(HostType.CAITINGTING_HOST)
//                .wxLogin(unionid, openId, nickname, headimgurl)
//                .compose(RxSchedulers.<JsonObject>io_main()).subscribe(new RxSubscriber<JsonObject>(WXEntryActivity.this, false) {
//                    @Override
//                    protected void _onNext(JsonObject jsonObject) {
//                        JsonElement jsonElement = jsonObject.get("code");
//                        int code = jsonElement.getAsInt();
//                        AppLog.print("switchWxLogin code___" + code);
//                        if (code == 16305) {
//                            AppLog.print("set 1");
//                            Bundle b = new Bundle();
//                            b.putString("nickname", nickname);
//                            b.putString("unionid", unionid);
//                            b.putString("openid", openId);
//                            b.putString("headimgurl", headimgurl);
//                            b.putInt("sex", sex);
//                            Constants.toActivity(WXEntryActivity.this, VerifyPhoneNumActivity.class, b, true);
//                        } else if (code == 800) {
//                            AppLog.print("set 2");
//                            JsonElement resultElement = jsonObject.get("result");
//                            String result = resultElement.toString();
//                            try {
//                                Gson gson = new Gson();
//                                member = gson.fromJson(result, Member.class);
//                            } catch (Exception e) {
//                            }
//                            if (member != null) {
//                                excamMaster(member.getId());
//                            }
//                        } else {
//                            AppLog.print("set 3");
//                            JsonElement messageElement = jsonObject.get("message");
//                            String message = messageElement.getAsString();
//                            ToastUtils.showToast(WXEntryActivity.this,message);
//                            finish();
//                        }
//
//                    }
//
//
//
//                    @Override
//                    protected void _onError(String message) {
//                        ToastUtils.showToast(WXEntryActivity.this,message);
//                        finish();
//                    }
//                }));
//    }
//
//
//    //获取用户信息
//    private void getUserMsg(String access_token, String openid) {
//        new GetUserMsgAsync(this, mHandler, access_token, openid).execute();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        // mRxManager.clear();
//    }
//
//
//    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
//
//        @Override
//        public void gotResult(int code, String alias, Set<String> tags) {
//            String logs;
//            switch (code) {
//                case 0:
//                    logs = "Set tag and alias success";
//                    break;
//
//                case 6002:
//                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
//                    //                Log.i(TAG, logs);
//                    //                if (ExampleUtil.isConnected(getApplicationContext())) {
//                    //                	mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
//                    //                } else {
//                    //                	Log.i(TAG, "No network");
//                    //                }
//                    break;
//
//                default:
//                    //                logs = "Failed with errorCode = " + code;
//                    //                Log.e(TAG, logs);
//            }
//
//            //            ExampleUtil.showToast(logs, getApplicationContext());
//        }
//
//    };
//
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Constants.toActivity(WXEntryActivity.this, HomeActivity.class, null, true);
//    }
//    private void loginComplete() {
//        MemeberKeeper.saveOAuth(member, WXEntryActivity.this);
//        MyApplication.getInstance().setLogin(true);
//        MyApplication.getInstance().setFlag(true);
//        Intent intent = new Intent(WXEntryActivity.this, HomeActivity.class);
//        startActivity(intent);
//        finish();
//        MySharedPreferences.setSharedPInt(MySharedPreferences.key_login_type, 1);
//        Constants.isMcLogin = true;
//        //调用JPush API设置Alias
//        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, member.getId() + ""));
//        MobclickAgent.onProfileSignIn(String.valueOf(member.getId()));
//    }
//
//    private void excamMaster(Long id) {
//
//        mRxManager.add(Api.getDefault(HostType.XIEXIN_HOSR).excamMaster(id)
//                .compose(RxHelper.<UserInfo>handleResult())
//                .subscribe(new RxSubscriber<UserInfo>(mContext, false) {
//                    @Override
//                    protected void _onNext(UserInfo userInfo) {
//                        AppLog.print("_onNext___userinfo"+userInfo+"___mUserinfo___"+userInfo.userinfo);
//                        Long wjdsId = userInfo.userinfo.id;
//                        Long status = userInfo.userinfo.status;
//                        Long role_id = userInfo.userinfo.role_id;
//                        member.setWjdsId(wjdsId);
//                        member.setWjdsStatus(status);
//                        member.setWjdsRole_id(role_id);
////                        MemeberKeeper.saveOAuth(member, mContext);
//                        loginComplete();
//                    }
//
//                    @Override
//                    protected void _onError(String message) {
//                        AppLog.print("_onError___"+message);
//                        ToastUtils.showToast(WXEntryActivity.this,"挖机数据同步失败！"+message);
//                        loginComplete();
//
//                    }
//                }));
//    }
//
//    public static Member fromJsonToBean(String json) {
//        return new Gson().fromJson(json, new TypeToken<Member>() {
//        }.getType());
//    }


}
