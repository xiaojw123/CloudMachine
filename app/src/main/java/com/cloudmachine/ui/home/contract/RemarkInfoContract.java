package com.cloudmachine.ui.home.contract;

import com.cloudmachine.base.BaseModel;
import com.cloudmachine.base.BasePresenter;
import com.cloudmachine.base.BaseView;
import com.cloudmachine.bean.EmunBean;

import java.util.List;

import rx.Observable;

/**
 * Created by xiaojw on 2017/6/8.
 */

public interface RemarkInfoContract {
    interface View extends BaseView {
        void returnRoleListView(List<EmunBean> roleBeen);
        void updateRemarkSuccess();
        void updateRemarkFailed();
    }

    interface Model extends BaseModel {
        Observable<List<EmunBean>> getRoleList();
        Observable<String>  updateRemarkInfo(int  groupId, long deviceId, int roleId, String remark);

    }

    abstract class Presenter extends BasePresenter<RemarkInfoContract.View, RemarkInfoContract.Model> {
        public abstract void updateRoleListView();
        public abstract void updateRemarkInfo(int  groupId, long deviceId, int roleId, String remark);
    }


}
