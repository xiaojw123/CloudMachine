package com.cloudmachine.ui.home.contract;

import com.cloudmachine.base.BaseModel;
import com.cloudmachine.base.BasePresenter;
import com.cloudmachine.base.BaseView;
import com.cloudmachine.bean.RepairListInfo;
import com.cloudmachine.bean.UnfinishedBean;
import com.cloudmachine.ui.home.model.SiteBean;

import rx.Observable;

/**
 * Created by xiaojw on 2017/6/7.
 */

public interface MSupervisorContract{

    interface View extends BaseView {
        void returnStationView(SiteBean siteBean);
        void returnRepairItemView(UnfinishedBean infoo,String status);

    }

    interface Model extends BaseModel {
        Observable<SiteBean> getSiteStation(double lng, double lat);
        Observable<RepairListInfo> getRepairList(long memberId);
    }

    abstract class Preseneter extends BasePresenter<MSupervisorContract.View, MSupervisorContract.Model> {
       public abstract void updateStationView(double lng,double lat);
       public abstract void getRepairItemView(long memberId);
    }

}
