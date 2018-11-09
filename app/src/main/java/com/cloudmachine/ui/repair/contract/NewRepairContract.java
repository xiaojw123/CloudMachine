package com.cloudmachine.ui.repair.contract;

import com.cloudmachine.base.BaseModel;
import com.cloudmachine.base.BasePresenter;
import com.cloudmachine.base.BaseView;
import com.cloudmachine.bean.LarkDeviceDetail;
import com.cloudmachine.bean.LarkLocBean;
import com.cloudmachine.bean.NewRepairInfo;
import com.google.gson.JsonObject;

import rx.Observable;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/3/22 下午2:45
 * 修改人：shixionglu
 * 修改时间：2017/3/22 下午2:45
 * 修改备注：
 */

public interface NewRepairContract {

    interface Model extends BaseModel {
        Observable<JsonObject> getWarnMessage(String tel);
        Observable<LarkDeviceDetail> getLocactionInfo(long deviceId);
    }


    interface View extends BaseView {
        void returnGetWarnMessage(NewRepairInfo info,String message);
        void returnLocatDetail(LarkLocBean loc);
    }

    abstract  class Presenter extends BasePresenter<View, Model> {

        public abstract void getWarnMessage(String tel,final NewRepairInfo info);
        public abstract void getLocactionInfo(long deviceId);
    }
}
