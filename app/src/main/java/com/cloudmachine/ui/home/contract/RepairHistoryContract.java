package com.cloudmachine.ui.home.contract;

import android.content.Context;

import com.cloudmachine.base.BaseModel;
import com.cloudmachine.base.BasePresenter;
import com.cloudmachine.base.BaseView;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.bean.AllianceItem;
import com.cloudmachine.bean.RepairListInfo;

import java.util.List;

import rx.Observable;

/**
 * Created by xiaojw on 2017/7/12.
 */

public interface RepairHistoryContract {
    interface View extends BaseView {
        void returnGetRepairList(RepairListInfo info);
        void returnGetRepairError();
        void retrunGetAllianceList(List<AllianceItem> itemList,boolean isLast, int pageNum);
        void returnGetAllianceError();

    }

    interface Model extends BaseModel {

        Observable<RepairListInfo> getRepairList(Context context,long deviceId);

        Observable<BaseRespose<List<AllianceItem>>> getAllianceList(Context context);
        Observable<BaseRespose<List<AllianceItem>>> getAllianceList(Context context, int pageNum);
    }

    public abstract class Presenter extends BasePresenter<RepairHistoryContract.View, RepairHistoryContract.Model> {
        public abstract void getRepairList( long deviceId,boolean isRefresh);
        public abstract void getAllianceList();
        public abstract void getAllianceList(int pageNum);

    }


}
