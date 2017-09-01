package com.cloudmachine.ui.home.contract;

import android.content.Context;

import com.cloudmachine.base.BaseModel;
import com.cloudmachine.base.BasePresenter;
import com.cloudmachine.base.BaseView;
import com.cloudmachine.bean.BOInfo;
import com.cloudmachine.bean.CWInfo;
import com.cloudmachine.ui.home.model.SiteBean;

import rx.Observable;

/**
 * Created by xiaojw on 2017/8/2.
 */

public interface RepairDetailContract {

    interface View extends BaseView {
        void returnStationView(SiteBean siteBean);
        void returnStationError();
        void returnDetailView(BOInfo boInfo);
        void returnDetailView(CWInfo boInfo);
    }

    public interface Model extends BaseModel {
        Observable<SiteBean> getSiteStation(double lng, double lat);
        Observable<BOInfo> getBoInfo(Context context, String orderNum, String flag);

        Observable<CWInfo> getCWInfo(Context context, String orderNum,String flag);
    }

     abstract class Preseneter extends BasePresenter<RepairDetailContract.View, RepairDetailContract.Model> {
        public abstract void updateRepairFinishDetail(String orderNum, String flag);
        public abstract void updateStationView(double lng,double lat);
    }

}
