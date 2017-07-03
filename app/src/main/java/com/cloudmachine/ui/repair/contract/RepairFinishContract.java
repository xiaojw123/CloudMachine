package com.cloudmachine.ui.repair.contract;

import com.cloudmachine.base.BaseModel;
import com.cloudmachine.base.BasePresenter;
import com.cloudmachine.base.BaseView;
import com.cloudmachine.struc.BOInfo;
import com.cloudmachine.struc.CWInfo;

import rx.Observable;

/**
 * Created by xiaojw on 2017/6/20.
 */

public interface RepairFinishContract {
    interface View extends BaseView {
        void returnDetailView(BOInfo boInfo);
        void returnDetailView(CWInfo boInfo);
    }

    interface Model extends BaseModel {

        Observable<BOInfo> getBoInfo(long memberId, String orderNum);

        Observable<CWInfo> getCWInfo(long memberId, String orderNum);

    }

    abstract class Presenter extends BasePresenter<RepairFinishContract.View,RepairFinishContract.Model> {

        public abstract void updateRepairFinishDetail(long memberId, String orderNum, String flag);

    }


}
