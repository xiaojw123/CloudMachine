package com.cloudmachine.ui.device.contract;

import com.cloudmachine.base.BaseModel;
import com.cloudmachine.base.BasePresenter;
import com.cloudmachine.base.BaseView;

/**
 * 项目名称：CloudMachine
 * 类描述：首页主页面契约类
 * 创建人：shixionglu
 * 创建时间：2017/3/17 下午10:34
 * 修改人：shixionglu
 * 修改时间：2017/3/17 下午10:34
 * 修改备注：
 */

public interface DeviceContract {

    interface Model extends BaseModel {

    }

    interface View extends BaseView {

    }

    abstract static class Presenter extends BasePresenter<View, Model> {

    }
}
