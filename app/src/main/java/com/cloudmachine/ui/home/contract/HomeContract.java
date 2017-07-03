package com.cloudmachine.ui.home.contract;

import com.cloudmachine.base.BaseModel;
import com.cloudmachine.base.BasePresenter;
import com.cloudmachine.base.BaseView;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.recyclerbean.HomeBannerBean;
import com.cloudmachine.struc.McDeviceInfo;
import com.cloudmachine.struc.UnReadMessage;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by xiaojw on 2017/5/22.
 */

public interface HomeContract {
    interface View extends BaseView {

        void updateMessageCount(int count);

        void updateDevices(List<McDeviceInfo> deviceList);
        void updatePromotionInfo(HomeBannerBean promotionBean);


    }

    interface Model extends BaseModel {
        Observable<BaseRespose<UnReadMessage>> getMessageUntreatedCount(long memberId);

        Observable<List<McDeviceInfo>> getDevices(long memberId, int type);
        Observable<List<McDeviceInfo>> getDevices(int type);
        Observable<BaseRespose<ArrayList<HomeBannerBean>>> getPromotionModel();

    }

    public abstract class Presenter extends BasePresenter<HomeContract.View, HomeContract.Model> {
        public abstract void updateUnReadMessage(long memberId);

        public abstract void getDevices(long memberId, int type);
        public abstract void getDevices(int type);

        public abstract void getPromotionInfo();

    }


}
