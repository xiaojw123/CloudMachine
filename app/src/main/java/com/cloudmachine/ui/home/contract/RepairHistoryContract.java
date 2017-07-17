package com.cloudmachine.ui.home.contract;

import android.content.Context;

import com.cloudmachine.base.BaseModel;
import com.cloudmachine.base.BasePresenter;
import com.cloudmachine.base.BaseView;
import com.cloudmachine.struc.RepairListInfo;

import rx.Observable;

/**
 * Created by xiaojw on 2017/7/12.
 */

public interface RepairHistoryContract {
    interface View extends BaseView {
        void returnGetRepairList(RepairListInfo info);
        void returnGetRepairError();

    }

    interface Model extends BaseModel {

        Observable<RepairListInfo> getRepairList(Context context,long deviceId);
    }

    public abstract class Presenter extends BasePresenter<RepairHistoryContract.View, RepairHistoryContract.Model> {
        public abstract void getRepairList( long deviceId);

    }


}
