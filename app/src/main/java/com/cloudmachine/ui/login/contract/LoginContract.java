package com.cloudmachine.ui.login.contract;

import com.cloudmachine.base.BaseModel;
import com.cloudmachine.base.BasePresenter;
import com.cloudmachine.base.BaseView;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/3/27 下午1:33
 * 修改人：shixionglu
 * 修改时间：2017/3/27 下午1:33
 * 修改备注：
 */

public interface LoginContract {

    interface Model extends BaseModel {
    }

    interface View extends BaseView {

    }

    abstract static class Presenter extends BasePresenter {
    }
}
