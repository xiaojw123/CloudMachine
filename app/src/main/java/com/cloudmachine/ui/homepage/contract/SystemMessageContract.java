package com.cloudmachine.ui.homepage.contract;

import com.cloudmachine.base.BaseModel;
import com.cloudmachine.base.BasePresenter;
import com.cloudmachine.base.BaseView;
import com.cloudmachine.bean.MessageBO;

import java.util.List;

import rx.Observable;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/4/12 下午8:00
 * 修改人：shixionglu
 * 修改时间：2017/4/12 下午8:00
 * 修改备注：
 */

public interface SystemMessageContract {

    interface Model extends BaseModel {

        Observable<List<MessageBO>> getSystemMessage(Long memberId,int pageNo,int pageSize);
    }

    interface View extends BaseView {

        void returnSystemMessage(List<MessageBO> messageBOs);

        void returnMoreSystemMessage(List<MessageBO> messageBOs);
    }

    abstract static class Presenter extends BasePresenter<View, Model> {

        public abstract void getSystemMessage(Long memberId, int pageNo, int pageSize);

        public abstract void getMoreSystemMessage(Long memberId, int pageNo, int pageSize);
    }
}
