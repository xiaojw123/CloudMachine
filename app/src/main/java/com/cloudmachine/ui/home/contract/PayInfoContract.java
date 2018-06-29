package com.cloudmachine.ui.home.contract;

import com.cloudmachine.base.BaseModel;
import com.cloudmachine.base.BasePresenter;
import com.cloudmachine.base.BaseView;
import com.cloudmachine.base.bean.BaseRespose;

import rx.Observable;

/**
 * Created by xiaojw on 2018/6/15.
 */

public interface PayInfoContract {

    interface  Model extends BaseModel{
        Observable<String> getVerfyCode(String  mobile,int type);
    }
    interface  View extends BaseView{
        void returnVerfyCode();

    }

    public abstract class Presenter extends BasePresenter<PayInfoContract.View,PayInfoContract.Model>{

        public abstract void getVerfyCode(int type,String mobile);

    }



}
