package com.cloudmachine.ui.home.contract;

import android.view.KeyEvent;

import com.amap.api.location.AMapLocation;
import com.cloudmachine.base.BaseModel;
import com.cloudmachine.base.BasePresenter;
import com.cloudmachine.base.BaseView;
import com.cloudmachine.bean.H5Config;
import com.cloudmachine.bean.MenuBean;
import com.cloudmachine.bean.VersionInfo;
import com.google.gson.JsonObject;

import java.util.List;

import rx.Observable;

/**
 * Created by xiaojw on 2017/5/22.
 */

public interface HomeContract {
    interface View extends BaseView {
        void updateMessageCount(int msgCount,int orderCount);




        void updateH5View();

        void updateView(List<MenuBean> homeMenuBeans);
        void reloadUrl();
        void updateVersionRemind(boolean hasNewVersion);
    }

    interface Model extends BaseModel {
        Observable<VersionInfo> getVersionInfo();

        Observable<JsonObject> getMessageUntreatedCount();



        Observable<H5Config> getConfigInfo();



        Observable<List<MenuBean>> getHomeMenu();

    }

    abstract class Presenter extends BasePresenter<HomeContract.View, HomeContract.Model> {
        public abstract void initHomeMenu(final boolean isFlush);
        public abstract void  getVersionInfo();
        public abstract void updateUnReadMessage();
        public abstract void initH5Config();





    }


}
