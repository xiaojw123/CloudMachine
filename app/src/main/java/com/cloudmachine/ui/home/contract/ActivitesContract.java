package com.cloudmachine.ui.home.contract;

import com.cloudmachine.base.BaseModel;
import com.cloudmachine.base.BasePresenter;
import com.cloudmachine.base.BaseView;
import com.cloudmachine.bean.HomeBannerBean;

import java.util.ArrayList;

import rx.Observable;

/**
 * Created by xiaojw on 2017/5/23.
 */

public interface ActivitesContract {
    interface  Model extends BaseModel{
        Observable<ArrayList<HomeBannerBean>> getHomeBannerInfo();
    }
    interface  View extends BaseView{
        void returnHomeBannerInfo(ArrayList<HomeBannerBean> homeBannerBeen);
        void loadBannerError();
    }
    public abstract class Presenter extends BasePresenter<ActivitesContract.View,ActivitesContract.Model>{
        public abstract void getHomeBannerInfo();
    }

}
