package com.cloudmachine.ui.login.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;

import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.bean.LarkMemberInfo;
import com.cloudmachine.bean.Member;
import com.cloudmachine.cache.MySharedPreferences;
import com.cloudmachine.chart.utils.AppLog;
import com.cloudmachine.helper.MobEvent;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.ui.login.acticity.FindPasswordActivity;
import com.cloudmachine.ui.login.acticity.VerifyPhoneNumActivity;
import com.cloudmachine.ui.login.contract.LoginContract;
import com.cloudmachine.utils.CommonUtils;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MemeberKeeper;
import com.cloudmachine.utils.ToastUtils;
import com.cloudmachine.utils.widgets.AppMsg;
import com.cloudmachine.utils.widgets.Dialog.LoadingDialog;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.HashMap;
import java.util.Map;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/3/27 下午1:35
 * 修改人：shixionglu
 * 修改时间：2017/3/27 下午1:35
 * 修改备注：
 */

public class LoginPresenter extends LoginContract.Presenter {
    private UMShareAPI mShareAPI;
    @Override
    public void login(String userName, String password) {
        if (userName.length() < 11 || !Constants.isMobileNO(userName)) {
            mView.setNameShakeAnimation();
            AppMsg appMsg = AppMsg.makeText((Activity) mContext, "请正确输入你的账号", AppMsg.STYLE_CO);
            appMsg.setLayoutGravity(Gravity.TOP);
            appMsg.show();
            return;
        }
        if (password.length() < 6) {
            AppMsg appMsg = AppMsg.makeText((Activity) mContext, "请正确输入你的密码", AppMsg.STYLE_CO);
            appMsg.setLayoutGravity(Gravity.TOP);
            appMsg.show();
            return;
        }
        show();
        mModel.login(userName, password).subscribe(new RxSubscriber<JsonObject>(mContext) {
            @Override
            protected void _onNext(JsonObject jsonObject) {
                try {
                    flushMember(jsonObject);
                } catch (Exception e) {
                    _onError("登录失败");
                }
            }

            @Override
            protected void _onError(String message) {
                disMiss();
                if (!TextUtils.isEmpty(message)) {
                    AppMsg appMsg = AppMsg.makeText((Activity) mContext,
                            message + "", AppMsg.STYLE_INFO);
                    appMsg.setLayoutGravity(Gravity.TOP);
                    appMsg.show();
                }
            }
        });
    }

    private void flushMember(JsonObject jsonObject) {
        String token = jsonObject.get(Constants.KEY_TOKEN).getAsString();
        String id = jsonObject.get(Constants.KEY_ID).getAsString();
        UserHelper.saveUserToken(mContext, token,id);
        getMemberInfo();
    }

    @Override
    public void getMemberInfo() {
        mModel.getMemberInfo().subscribe(new RxSubscriber<LarkMemberInfo>(mContext) {
            @Override
            protected void _onNext(LarkMemberInfo info) {
                disMiss();
                Member member= CommonUtils.convertMember(info);
                MemeberKeeper.saveOAuth(member, mContext);
                MySharedPreferences.setSharedPInt(MySharedPreferences.key_login_type, 0);//个人信息同步微信标记
                mView.loginSuccess(member);
//                mRxManage.post(Constants.FLUSH_TOKEN,null);
            }

            @Override
            protected void _onError(String message) {
                disMiss();
                ToastUtils.showToast(mContext, message);
            }
        });
    }

    @Override
    public void loginByWx(final String unionId, final String openId, final String nickName, final String headLogo, final int sex) {
        mRxManage.add(mModel.loginByWx(unionId,openId,nickName,headLogo).subscribe(new RxSubscriber<JsonObject>(mContext) {
            @Override
            protected void _onNext(JsonObject jsonObject) {
                JsonElement jsonElement = jsonObject.get("code");
                int code = jsonElement.getAsInt();
                AppLog.print("switchWxLogin code___" + code);
                if (code == 16305) {
                    AppLog.print("set 1");
                    Bundle b = new Bundle();
                    b.putString("nickname", nickName);
                    b.putString("unionid", unionId);
                    b.putString("openid", openId);
                    b.putString("headimgurl", headLogo);
                    b.putInt("sex", sex);
                    Constants.toActivity((Activity) mContext, VerifyPhoneNumActivity.class, b, true);
                } else if (code == 800) {
                    AppLog.print("set 2");
                    MySharedPreferences.setSharedPInt(MySharedPreferences.key_login_type, 1);
                    JsonObject resultJobj = jsonObject.getAsJsonObject("result");
                    flushMember(resultJobj);
                } else {
                    AppLog.print("set 3");
                    JsonElement messageElement = jsonObject.get("message");
                    String message = messageElement.getAsString();
                    ToastUtils.showToast(mContext, message);
                }

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(mContext, message);
                ((Activity)mContext).finish();
            }
        }));
    }


    private LoadingDialog progressDialog;

    public void show() {
        if (progressDialog == null) {
            progressDialog = LoadingDialog.createDialog(mContext);
        }
        progressDialog.setMessage("正在加载，请稍后");
        progressDialog.show();
    }

    public void disMiss() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    public void gotoRegisterPage() {
        MobclickAgent.onEvent(mContext, MobEvent.COUNT_REGISTER);
        Intent reigsterIntent = new Intent(mContext, FindPasswordActivity.class);
        reigsterIntent.putExtra("type", 3);
        ((Activity) mContext).startActivityForResult(reigsterIntent, 0);
    }

    public void gotoForgetPswPage() {
        MobclickAgent.onEvent(mContext, MobEvent.COUNT_FORGOTPASSWORD);
        Intent forgetIntent = new Intent(mContext, FindPasswordActivity.class);
        forgetIntent.putExtra(FindPasswordActivity.HASINVITATIONCODE, true);
        forgetIntent.putExtra("type", 1);
        mContext.startActivity(forgetIntent);
    }


    public void loginToWeiXin() {
        mShareAPI = UMShareAPI.get(mContext);
        mShareAPI.doOauthVerify((Activity) mContext, SHARE_MEDIA.WEIXIN, authListener);
    }
    private UMAuthListener authListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            mShareAPI.getPlatformInfo((Activity)mContext, SHARE_MEDIA.WEIXIN, platformInfoListener);
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
            if (map == null) {
                AppLog.print("第三方信息为空");
                return;
            }
            for (Map.Entry<String, String> entry : map.entrySet()) {
                AppLog.print("key:" + entry.getKey() + ", value:" + entry.getValue() + "\n");
            }
            final String unionid = map.get("unionid");
            final String openId = map.get("openid");
            final String nickname = map.get("name");
            final String headimgurl = map.get("iconurl");
            final int sex = "男".equals(map.get("gender")) ? 1 : 0;
            loginByWx(unionid,openId,nickname,headimgurl,sex);
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            ToastUtils.showToast(mContext, "授权失败！！" + throwable.getMessage());
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
            ToastUtils.showToast(mContext, "授权失败！！");
        }
    };


}

