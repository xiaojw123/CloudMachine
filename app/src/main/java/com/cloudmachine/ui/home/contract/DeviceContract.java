package com.cloudmachine.ui.home.contract;

import android.view.View;

import com.cloudmachine.base.BaseModel;
import com.cloudmachine.base.BasePresenter;
import com.cloudmachine.base.BaseView;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.bean.ArticleInfo;
import com.cloudmachine.bean.DeviceItem;
import com.cloudmachine.bean.McDeviceInfo;
import com.cloudmachine.bean.TelBean;

import java.util.List;

import rx.Observable;

/**
 * Created by xiaojw on 2017/7/19.
 */

public interface DeviceContract {
    interface  View extends BaseView{
        void updateDevices(List<McDeviceInfo> deviceList);
        void updateArticles(List<ArticleInfo> articleInfoList);
        void retrunGetServiceTel(String boxTel,String repairTel);
        void updateDeviceItems(List<DeviceItem> items,int pageNum);
        void updateDeviceItemError();
        void updatePager(McDeviceInfo info);
        void updateAllDeviceList(BaseRespose<List<McDeviceInfo>> respose,int pageNum);
        void updateAllDeviceError(String message);

    }
    interface Model extends BaseModel{
        Observable<BaseRespose<List<DeviceItem>>> getDeviceItems(String memberId,int page);
        Observable<List<McDeviceInfo>> getDevices(long memberId, int type);
        Observable<List<McDeviceInfo>> getDevices(int type);
        Observable<List<ArticleInfo>> getArticles();
        Observable<List<TelBean>> getServiceTel();
        Observable<McDeviceInfo> getDeviceNowData(String deviceId, long memberId);
        Observable<McDeviceInfo> getDeviceNowData(String deviceId);
        Observable<BaseRespose<List<McDeviceInfo>>> getAllDeviceList(long memberId,int page,String key);


    }

    public abstract  class Prensenter extends BasePresenter<DeviceContract.View,DeviceContract.Model> {
//        public abstract void getDevices(long memberId, int type);

//        public abstract void getDevices(int type);
        public abstract void getDeivceItems(String memberId,int page);

        public abstract void getArticleList();

        public abstract void getServiceTel();

        public abstract void getDeviceNowData(String deviceId, android.view.View itemView);
        public abstract void getAllDeviceList(long memberId,int page,String key);

    }


}
