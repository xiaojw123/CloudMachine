package com.cloudmachine.ui.home.contract;

import com.cloudmachine.base.BaseModel;
import com.cloudmachine.base.BasePresenter;
import com.cloudmachine.base.BaseView;
import com.cloudmachine.ui.home.model.RoleBean;

import java.util.List;

import rx.Observable;

/**
 * Created by xiaojw on 2017/6/8.
 */

public interface RemarkInfoContract {
    interface View extends BaseView {
        void returnRoleListView(List<RoleBean> roleBeen);
        void updateRemarkSuccess();
        void updateRemarkFailed();
    }

    interface Model extends BaseModel {
        Observable<List<RoleBean>> getRoleList();
        Observable<String>  updateRemarkInfo(long fid, long memberId, long deviceId, String remark, long roleId);

    }

    abstract class Presenter extends BasePresenter<RemarkInfoContract.View, RemarkInfoContract.Model> {
        public abstract void updateRoleListView();
        public abstract void updateRemarkInfo(long fid,long memberId,long deviceId,String remark,long roleId);
    }


}
