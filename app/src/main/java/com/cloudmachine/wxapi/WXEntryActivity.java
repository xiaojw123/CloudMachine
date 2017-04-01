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
import com.cloudmachine.base.baserx.RxManager;
import com.cloudmachine.base.baserx.RxSchedulers;
import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.net.task.GetAccessTokenAsync;
import com.cloudmachine.net.task.GetUserMsgAsync;
import com.cloudmachine.ui.login.acticity.VerifyPhoneNumActivity;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.ToastUtils;
import com.cloudmachine.utils.WeChatShareUtil;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler ,Handler.Callback{

    private IWXAPI api;
    private static final String APP_SECRET = "3c69a7395f5e54009accf1e1194d553c";
    private Handler   mHandler;
    public  RxManager mRxManager;
    private Context mContext;

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
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                //发送被拒绝
                result = "分享失败";
                break;
            default:
                result = "分享失败";
                break;
        }
        if (result != null) {
            Toast.makeText(this, baseResp.errCode, Toast.LENGTH_SHORT).show();
            Constants.MyLog("微信分享返回码" + baseResp.errCode);
        }
    }

    /**
     * 获取openid accessToken值用于后期操作
     * @param code 请求码
     */
    private void getAccess_token(String code) {

        /*String path = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
                + Constants.APP_ID
                + "&secret="
                + APP_SECRET
                + "&code="
                + code
                + "&grant_type=authorization_code";*/
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
                String nickname = bundle.getString("nickname");
                String headimgurl = bundle.getString("headimgurl");
                int sex = bundle.getInt("sex");
                String unionid = bundle.getString("unionid");
                String openId = bundle.getString("openid");
                //是否请求服务器
                switchWXLogin(unionid,openId,nickname,headimgurl);
                break;
        }
        return false;
    }

    private void switchWXLogin(String unionid, String openId, String nickname, String headimgurl) {

        mRxManager.add(Api.getDefault(HostType.CAITINGTING_HOST)
                .wxLogin(unionid,openId,nickname,headimgurl)
        .compose(RxSchedulers.<BaseRespose>io_main())
        .subscribe(new RxSubscriber<BaseRespose>(mContext,false) {
            @Override
            protected void _onNext(BaseRespose baseRespose) {
                if (baseRespose.code == 16305) {
                    Constants.toActivity(WXEntryActivity.this, VerifyPhoneNumActivity.class,null,true);

                } else if (baseRespose.code == 800) {

                } else {
                    ToastUtils.error(baseRespose.message,true);
                }
            }

            @Override
            protected void _onError(String message) {

            }
        }));
    }


    //获取用户信息
    private void getUserMsg(String access_token, String openid) {
        new GetUserMsgAsync(this,mHandler,access_token,openid).execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       // mRxManager.clear();
    }
}
