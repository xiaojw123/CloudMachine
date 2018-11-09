package com.cloudmachine.ui.login.contract;

import com.cloudmachine.base.BaseModel;
import com.cloudmachine.base.BasePresenter;
import com.cloudmachine.base.BaseView;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.bean.CheckNumBean;
import com.cloudmachine.bean.Member;
import com.google.gson.JsonObject;

import rx.Observable;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/3/27 下午2:40
 * 修改人：shixionglu
 * 修改时间：2017/3/27 下午2:40
 * 修改备注：
 */

public interface VerifyPhoneNumContract {

    interface Model extends BaseModel {
        Observable<BaseRespose> wxBindMobile(long mobile, long type);

        Observable<Integer> checkNum(long mobile);

        Observable<JsonObject> wxBind(String unionId,
                                      String openId,
                                      String account,
                                      String code,
                                      String pwd,
                                      String nickname,
                                      String headLogo
        );
    }

    interface View extends BaseView {
        void returnWXBindMobile(String message);

        void returnCheckNum(Integer type);

        void returnBindWx(Member member);
    }

    abstract  class Presenter extends BasePresenter<View, Model> {

        public abstract void wxBindMobile(long mobile, long type);

        public abstract void checkNum(long mobile);

        public abstract void wxBind(String unionId,
                                    String openId,
                                    String account,
                                    String code,
                                    String pwd,
                                    String nickname,
                                    String headLogo
        );

    }
}
