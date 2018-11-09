package com.cloudmachine.ui.login.presenter;

import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.bean.CheckNumBean;
import com.cloudmachine.bean.LarkMemberInfo;
import com.cloudmachine.bean.Member;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.ui.login.contract.VerifyPhoneNumContract;
import com.cloudmachine.utils.CommonUtils;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MemeberKeeper;
import com.cloudmachine.utils.ToastUtils;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/3/27 下午2:49
 * 修改人：shixionglu
 * 修改时间：2017/3/27 下午2:49
 * 修改备注：
 */

public class VerifyPhoneNumPresenter extends VerifyPhoneNumContract.Presenter {


    @Override
    public void wxBindMobile(long mobile, long type) {
        mRxManage.add(mModel.wxBindMobile(mobile, type)
                .compose(RxHelper.handleBooleanResult())
                .subscribe(new RxSubscriber<String>(mContext, false) {
                    @Override
                    protected void _onNext(String s) {
                        mView.returnWXBindMobile(s);
                    }

                    @Override
                    protected void _onError(String message) {
                        ToastUtils.error(message, true);
                    }
                })
        );
    }

    @Override
    public void checkNum(long mobile) {

        mRxManage.add(mModel.checkNum(mobile)
                .subscribe(new RxSubscriber<Integer>(mContext, false) {
                    @Override
                    protected void _onNext(Integer type) {
                        mView.returnCheckNum(type);
                    }

                    @Override
                    protected void _onError(String message) {
                        ToastUtils.error(message, true);
                    }
                })
        );
    }

    @Override
    public void wxBind(String unionId, String openId, String account, String code, String pwd, String nickname, String headLogo) {
        mRxManage.add(mModel.wxBind(unionId,
                openId,
                account,
                code,
                pwd,
                nickname,
                headLogo).flatMap(new Func1<JsonObject, Observable<LarkMemberInfo>>() {
            @Override
            public Observable<LarkMemberInfo> call(JsonObject jsonObject) {
                String token = jsonObject.get(Constants.KEY_TOKEN).getAsString();
                String id = jsonObject.get(Constants.KEY_ID).getAsString();
                UserHelper.TOKEN = token;
                UserHelper.ID = id;
                Map<String, String> data = new HashMap<>();
                data.put(Constants.KEY_TOKEN, token);
                data.put(Constants.KEY_ID, id);
                UserHelper.saveKeyValue(mContext, data);
                return Api.getDefault(HostType.HOST_LARK).getLarkMemberInfo().compose(RxHelper.<LarkMemberInfo>handleResult());
            }
        }).subscribe(new RxSubscriber<LarkMemberInfo>(mContext) {
            @Override
            protected void _onNext(LarkMemberInfo info) {
                Member member = CommonUtils.convertMember(info);
                MemeberKeeper.saveOAuth(member, mContext);
                mView.returnBindWx(member);
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.error(message, true);
            }
        }));
//        mRxManage.add(mModel.wxBind(unionId,
//                openId,
//                account,
//                code,
//                pwd,
//                nickname,
//                headLogo).flatMap(new Func1<JsonObject, Observable<Member>() {
//            @Override
//            public Observable<?> call(JsonObject jsonObject) {
//
//                return null;
//            }
//        }).subscribe(new RxSubscriber<JsonObject>(mContext, false) {
//            @Override
//            protected void _onNext(JsonObject resulJobj) {
//
//                mView.returnBindWx(member);
//            }
//
//            @Override
//            protected void _onError(String message) {
//                ToastUtils.error(message, true);
//            }
//        }));
    }


}
