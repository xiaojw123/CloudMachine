package com.cloudmachine.ui.home.contract;

import com.cloudmachine.base.BaseModel;
import com.cloudmachine.base.BasePresenter;
import com.cloudmachine.base.BaseView;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.bean.ElectronicFenceBean;

import rx.Observable;

/**
 * Created by xiaojw on 2018/9/19.
 */

public interface MapOneContract {

    interface Model extends BaseModel {
        Observable<ElectronicFenceBean> getElecFence(long deviceId);

        Observable<String> addElecFence(double lat, double lng, String fenceRadium, int fenceType, long deviceId);

        Observable<String> deleteElecFence(long deviceId,int type);
    }

    interface View extends BaseView {
        void updateFence(ElectronicFenceBean fenceBean);
        void addFenceSuccess();
        void deleteFenceSuccess();
    }

    abstract class Presenter extends BasePresenter<MapOneContract.View, MapOneContract.Model> {
        public abstract void getElecFence(long deviceId);
        public abstract void addElecFence(double lat, double lng, String fenceRadium, int fenceType, long deviceId);
        public abstract void deleteElecFence(long deviceId,int type);


    }
}
