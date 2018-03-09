package com.cloudmachine.ui.repair.contract;

import android.content.Context;

import com.cloudmachine.base.BaseModel;
import com.cloudmachine.base.BasePresenter;
import com.cloudmachine.base.BaseView;
import com.cloudmachine.bean.AllianceDetail;
import com.cloudmachine.bean.BOInfo;
import com.cloudmachine.bean.CWInfo;

import rx.Observable;

/**
 * Created by xiaojw on 2017/6/20.
 */

public interface RepairFinishContract {
    interface View extends BaseView {
        void returnDetailView(BOInfo boInfo);
        void returnDetailView(CWInfo boInfo);
        void returnAllianceDetail(AllianceDetail detail);
    }

    interface Model extends BaseModel {

        Observable<BOInfo> getBoInfo(Context context, String orderNum,String flag);

        Observable<CWInfo> getCWInfo(Context context, String orderNum,String flag);
        Observable<AllianceDetail> getAllianceOrderDetail(long memberId, String orderNo);

    }

    abstract class Presenter extends BasePresenter<RepairFinishContract.View,RepairFinishContract.Model> {

        public abstract void updateRepairFinishDetail(String orderNum, String flag);
        public abstract void updateAllianceDetail(long memberId,String orderNo);

    }


}
