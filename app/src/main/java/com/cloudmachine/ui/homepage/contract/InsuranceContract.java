package com.cloudmachine.ui.homepage.contract;

import com.cloudmachine.base.BaseModel;
import com.cloudmachine.base.BasePresenter;
import com.cloudmachine.base.BaseView;
import com.cloudmachine.recyclerbean.HomeBannerBean;

import java.util.ArrayList;

import rx.Observable;

/**
 * Created by xiaojw on 2017/4/28.
 */

public class InsuranceContract {

    public  interface  View extends BaseView{

        void returnSharedInfo(ArrayList<HomeBannerBean> homeBannerBeen);

    }
   public interface  Model extends BaseModel{

        Observable<ArrayList<HomeBannerBean>> getSharedInfo();
    }
   public static abstract class Presenter extends BasePresenter<InsuranceContract.View,InsuranceContract.Model>{

        public abstract  void getSharedInfo();

    }

}
