package com.cloudmachine.ui.homepage.contract;

import com.cloudmachine.base.BaseModel;
import com.cloudmachine.base.BasePresenter;
import com.cloudmachine.base.BaseView;

/**
 * 项目名称：CloudMachine
 * 类描述：问答社区
 * 创建人：shixionglu
 * 创建时间：2017/4/10 下午7:18
 * 修改人：shixionglu
 * 修改时间：2017/4/10 下午7:18
 * 修改备注：
 */

public interface QuestionCommunityContract {


    interface Model extends BaseModel {


    }

    interface View extends BaseView {


    }

    abstract static class Presenter extends BasePresenter<View, Model> {

    }
}
