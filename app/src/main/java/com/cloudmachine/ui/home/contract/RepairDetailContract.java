package com.cloudmachine.ui.home.contract;

import com.cloudmachine.base.BaseModel;
import com.cloudmachine.base.BasePresenter;
import com.cloudmachine.base.BaseView;
import com.cloudmachine.bean.RepairDetail;
import com.cloudmachine.bean.SiteBean;

import rx.Observable;

/**
 * Created by xiaojw on 2017/8/2.
 */

public interface RepairDetailContract {

    interface View extends BaseView {
        void returnStationView(SiteBean siteBean);
        void returnStationError();
        void updateRepairDetail(RepairDetail detail);

    }

    public interface Model extends BaseModel {
        Observable<SiteBean> getSiteStation(double lng, double lat);
        Observable<RepairDetail> getRepairDetail(String orderNo);
    }

     abstract class Preseneter extends BasePresenter<RepairDetailContract.View, RepairDetailContract.Model> {
        public abstract void updateStationView(double lng,double lat);
        public abstract void getRepairDetail(String orderNo);
    }

}
