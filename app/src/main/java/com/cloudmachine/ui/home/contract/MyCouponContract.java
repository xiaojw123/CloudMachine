package com.cloudmachine.ui.home.contract;

import com.cloudmachine.base.BaseModel;
import com.cloudmachine.base.BasePresenter;
import com.cloudmachine.base.BaseView;
import com.cloudmachine.bean.CouponBean;
import com.cloudmachine.bean.CouponItem;

import java.util.List;

import rx.Observable;

/**
 * Created by xiaojw on 2017/10/11.
 */

public interface MyCouponContract {
    interface Model extends BaseModel {
        Observable<CouponBean> getAvaildCouponList(long memberid);

        Observable<CouponBean> getInavaildCouponList(long memberid);

        Observable<CouponBean> getMyCouponDetailList(long memberid, int couponBaseId);
    }

    interface View extends BaseView {
        void updateAvaildCouponListView(int sumNum,int sumAmount, List<CouponItem> couponBOList);

        void updateInavaildCouponListView(int sumNum,List<CouponItem> couponBOList);

        void updateMyCouponDetailListView(int sumAmount, List<CouponItem> couponBOList);
    }


     abstract class Presenter extends BasePresenter<MyCouponContract.View, MyCouponContract.Model> {

        public  abstract void getAvaildCouponList(long memberid);

        public  abstract void getInavaildCouponList(long memberid);

        public  abstract void getMyCouponDetailList(long memberid, int couponBaseId);
    }


}
