package com.cloudmachine.wxapi;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.cloudmachine.api.Api;
import com.cloudmachine.api.HostType;
import com.cloudmachine.app.MyApplication;
import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.base.baserx.RxManager;
import com.cloudmachine.base.baserx.RxSchedulers;
import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.cache.MySharedPreferences;
import com.cloudmachine.main.MainActivity;
import com.cloudmachine.net.task.GetAccessTokenAsync;
import com.cloudmachine.net.task.GetUserMsgAsync;
import com.cloudmachine.struc.Member;
import com.cloudmachine.struc.UserInfo;
import com.cloudmachine.ui.login.acticity.VerifyPhoneNumActivity;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MemeberKeeper;
import com.cloudmachine.utils.ToastUtils;
import com.cloudmachine.utils.WeChatShareUtil;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler, Handler.Callback {

    private IWXAPI api;
    private static final String APP_SECRET    = "3c69a7395f5e54009accf1e1194d553c";
    private static final int    MSG_SET_ALIAS = 1001;
    private Handler   mHandler;
    public  RxManager mRxManager;
    private Context   mContext;
    private String    mNickname;
    private String    mHeadimgurl;
    private int       mSex;
    private String    mUnionid;
    private String    mOpenId;
    private Member    mMember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRxManager = new RxManager();
        mHandler = new Handler(this);
        mContext = this;
        api = WXAPIFactory.createWXAPI(this, WeChatShareUtil.APP_ID, false);
        api.handleIntent(getIntent(), this);
        //是否调用finish（）方法
        // finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);//必须调用此句话
    }

    //微信发送的请求将回调到onReq方法
    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        String result;
        Constants.MyLog("获取返货成功");
        switch (baseResp.errCode) {

            case BaseResp.ErrCode.ERR_OK:
                result = "分享成功";
                //发送成功
                SendAuth.Resp sendResp = (SendAuth.Resp) baseResp;
                if (sendResp != null) {
                    String code = sendResp.code;
                    getAccess_token(code);
                }
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = null;
                finish();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                //发送被拒绝
                result = "分享失败";
                finish();
                break;
            default:
                result = "分享失败";
                finish();
                break;
        }
        if (result != null) {
            Toast.makeText(this, baseResp.errCode, Toast.LENGTH_SHORT).show();
            Constants.MyLog("微信分享返回码" + baseResp.errCode);
        }
    }

    /**
     * 获取openid accessToken值用于后期操作
     * @param code
     *         请求码
     */
    private void getAccess_token(String code) {

        new GetAccessTokenAsync(this, mHandler, Constants.APP_ID, APP_SECRET,
                code, "authorization_code").execute();
    }

    /**
     * 拿到 access_token,openid
     * @param message
     * @return
     */
    @Override
    public boolean handleMessage(Message message) {

        Bundle bundle = message.getData();

        switch (message.what) {
            case Constants.HANDLER_GETACCESSTOKEN_SUCCESS:
                String openid = bundle.getString("openid");
                String access_token = bundle.getString("access_token");
                if (!TextUtils.isEmpty(access_token) && !TextUtils.isEmpty(openid)) {
                    getUserMsg(access_token, openid);
                }
                break;
            case Constants.HANDLER_GETUSERMSG_SUCCESS:
                mNickname = bundle.getString("nickname");
                mHeadimgurl = bundle.getString("headimgurl");
                mSex = bundle.getInt("sex");
                mUnionid = bundle.getString("unionid");
                mOpenId = bundle.getString("openid");
                //是否请求服务器
                switchWXLogin(mUnionid, mOpenId, mNickname, mHeadimgurl, mSex);
                break;
            case MSG_SET_ALIAS:
                JPushInterface.setAliasAndTags(getApplicationContext(), (String) message.obj, null, mAliasCallback);
                break;
        }
        return false;
    }

    private void switchWXLogin(final String unionid, final String openId, final String nickname, final String headimgurl, final int sex) {

        mRxManager.add(Api.getDefault(HostType.CAITINGTING_HOST)
                        .wxLogin(unionid, openId, nickname, headimgurl)
                        .compose(RxSchedulers.<JsonObject>io_main()).subscribe(new RxSubscriber<JsonObject>(WXEntryActivity.this, false) {
                            @Override
                            protected void _onNext(JsonObject jsonObject) {
                                JsonElement jsonElement = jsonObject.get("code");
                                int code = jsonElement.getAsInt();
                                if (code == 16305) {
                                    Bundle b = new Bundle();
                                    b.putString("nickname", nickname);
                                    b.putString("unionid", unionid);
                                    b.putString("openid", openId);
                                    b.putString("headimgurl", headimgurl);
                                    b.putInt("sex", sex);
                                    Constants.toActivity(WXEntryActivity.this, VerifyPhoneNumActivity.class, b, true);
                                } else if (code == 800) {
                                    JsonElement resultElement = jsonObject.get("result");
                                    String result = resultElement.getAsString();
                                    Gson gson = new Gson();
                                    Member member = gson.fromJson(result, Member.class);
                                    if (member != null) {
                                        excamMaster(member.getId());
                                    }
                                    MemeberKeeper.saveOAuth(member, WXEntryActivity.this);
                                    MyApplication.getInstance().setLogin(true);
                                    MyApplication.getInstance().setFlag(true);
                                    Intent intent = new Intent(WXEntryActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                    MySharedPreferences.setSharedPInt(MySharedPreferences.key_login_type, 1);
                                    Constants.isMcLogin = true;
                                    //调用JPush API设置Alias
                                    mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, member.getId() + ""));
                                    MobclickAgent.onProfileSignIn(String.valueOf(member.getId()));
                                } else {
                                    JsonElement messageElement = jsonObject.get("message");
                                    String message = messageElement.getAsString();
                                    ToastUtils.error(message,true);
                                }

                            }

                            @Override
                            protected void _onError(String message) {

                            }
                        }));
    }


    //获取用户信息
    private void getUserMsg(String access_token, String openid) {
        new GetUserMsgAsync(this, mHandler, access_token, openid).execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // mRxManager.clear();
    }


    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    break;

                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    //                Log.i(TAG, logs);
                    //                if (ExampleUtil.isConnected(getApplicationContext())) {
                    //                	mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    //                } else {
                    //                	Log.i(TAG, "No network");
                    //                }
                    break;

                default:
                    //                logs = "Failed with errorCode = " + code;
                    //                Log.e(TAG, logs);
            }

            //            ExampleUtil.showToast(logs, getApplicationContext());
        }

    };


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Constants.MyLog("执行了");
        Constants.toActivity(WXEntryActivity.this, MainActivity.class, null, true);
    }

    private void excamMaster(Long id) {

        mRxManager.add(Api.getDefault(HostType.XIEXIN_HOSR).excamMaster(id)
                .compose(RxHelper.<UserInfo>handleResult())
                .subscribe(new RxSubscriber<UserInfo>(mContext, false) {
                    @Override
                    protected void _onNext(UserInfo userInfo) {
                        Long wjdsId = userInfo.userinfo.id;
                        Long status = userInfo.userinfo.status;
                        Long role_id = userInfo.userinfo.role_id;
                        mMember.setWjdsId(wjdsId);
                        mMember.setWjdsStatus(status);
                        mMember.setWjdsRole_id(role_id);
                        MemeberKeeper.saveOAuth(mMember, mContext);
                        WXEntryActivity.this.finish();
                    }

                    @Override
                    protected void _onError(String message) {

                    }
                }));
    }

    public static Member fromJsonToBean(String json) {
        return new Gson().fromJson(json, new TypeToken<Member>() {
        }.getType());
    }


}
