package com.cloudmachine.ui.homepage.contract;

import com.cloudmachine.base.BaseModel;
import com.cloudmachine.base.BasePresenter;
import com.cloudmachine.base.BaseView;
import com.cloudmachine.base.bean.BaseRespose;

import rx.Observable;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/3/30 下午5:02
 * 修改人：shixionglu
 * 修改时间：2017/3/30 下午5:02
 * 修改备注：
 */

public interface MasterDailyContract {

    interface Model extends BaseModel {
        Observable<BaseRespose> getMasterDaily(int page, int size);
    }

    interface View extends BaseView {
    }

    abstract static class Presenter extends BasePresenter<View, Model> {

    }

}
