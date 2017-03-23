package com.cloudmachine.ui.repair.contract;

import com.cloudmachine.base.BaseModel;
import com.cloudmachine.base.BasePresenter;
import com.cloudmachine.base.BaseView;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/3/18 上午9:14
 * 修改人：shixionglu
 * 修改时间：2017/3/18 上午9:14
 * 修改备注：
 */

public interface RepairContract {

    interface Model extends BaseModel {
    }

    interface View extends BaseView {

    }

    abstract static class Presenter extends BasePresenter {
    }
}
