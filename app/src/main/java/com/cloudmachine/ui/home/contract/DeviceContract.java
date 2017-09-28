package com.cloudmachine.ui.home.contract;

import com.cloudmachine.base.BaseModel;
import com.cloudmachine.base.BasePresenter;
import com.cloudmachine.base.BaseView;
import com.cloudmachine.bean.ArticleInfo;
import com.cloudmachine.bean.McDeviceInfo;

import java.util.List;

import rx.Observable;

/**
 * Created by xiaojw on 2017/7/19.
 */

public interface DeviceContract {
    interface  View extends BaseView{
        void updateDevices(List<McDeviceInfo> deviceList);
        void updateArticles(List<ArticleInfo> articleInfoList);
    }
    interface Model extends BaseModel{

        Observable<List<McDeviceInfo>> getDevices(long memberId, int type);
        Observable<List<McDeviceInfo>> getDevices(int type);
        Observable<List<ArticleInfo>> getArticles();
    }

    public abstract  class Prensenter extends BasePresenter<DeviceContract.View,DeviceContract.Model> {
        public abstract void getDevices(long memberId, int type);

        public abstract void getDevices(int type);

        public abstract void getArticleList();

    }


}
