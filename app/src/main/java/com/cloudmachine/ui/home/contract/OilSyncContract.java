package com.cloudmachine.ui.home.contract;

import android.text.Spanned;

import com.cloudmachine.base.BaseModel;
import com.cloudmachine.base.BasePresenter;
import com.cloudmachine.base.BaseView;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.bean.OilSynBean;

import java.util.List;

import rx.Observable;

/**
 * Created by xiaojw on 2018/5/9.
 */

public interface OilSyncContract {

    interface Model extends BaseModel{

     Observable<List<OilSynBean>> getOilSyncList(long deviceId);
     Observable<BaseRespose<String>>  syncOilLevel(long deviceId,int oilPos);
     Observable<BaseRespose<String>>  restOilLevel(long deviceId);

    }

    interface View extends BaseView{
        void updateOilSynItemView(int oilPos, Spanned textSpanned);
        void showSyncSuccessDailog(String message);
        void retrunSyncFailed(String message);
        void showLoadingDailog();

    }

    abstract class Presenter extends BasePresenter<OilSyncContract.View,OilSyncContract.Model>{
        public abstract void getOilSyncList(long deviceId);
        public abstract void syncOilLevel(long deviceId,int oilPos);
        public abstract void  restOilLevel(long deviceId);

    }




}
