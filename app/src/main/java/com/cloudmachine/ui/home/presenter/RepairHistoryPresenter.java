package com.cloudmachine.ui.home.presenter;

import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.base.bean.PageBean;
import com.cloudmachine.bean.AllianceItem;
import com.cloudmachine.bean.RepairListInfo;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.ui.home.contract.RepairHistoryContract;
import com.cloudmachine.utils.Constants;

import java.util.List;

/**
 * Created by xiaojw on 2017/7/12.
 */

public class RepairHistoryPresenter extends RepairHistoryContract.Presenter {
    @Override
    public void getRepairList(final long deviceId, final boolean isRefresh) {
        mRxManage.add(mModel.getRepairList(mContext, deviceId).subscribe(new RxSubscriber<RepairListInfo>(mContext, false) {
            @Override
            protected void _onNext(RepairListInfo info) {
                mView.returnGetRepairList(info);
                if (deviceId == Constants.INVALID_DEVICE_ID) {
                    updateAlianceData(isRefresh);
                }else{
                    mView.returnGetAllianceError();
                }
            }

            @Override
            protected void _onError(String message) {
                if (deviceId == Constants.INVALID_DEVICE_ID) {
                    mView.returnGetRepairError();
                    updateAlianceData(isRefresh);
                }else{
                    mView.returnGetAllianceError();
                }
            }
        }));


    }

    private void updateAlianceData(boolean isRefresh) {
        if (!isRefresh) {
            if (UserHelper.isLogin(mContext)) {
                getAllianceList();
            } else {
                mView.retrunGetAllianceList(null, false, 1);
            }
        }
    }

    @Override
    public void getAllianceList() {
        mRxManage.add(mModel.getAllianceList(mContext).subscribe(new RxSubscriber<BaseRespose<List<AllianceItem>>>(mContext) {
            @Override
            protected void _onNext(BaseRespose<List<AllianceItem>> respose) {
                PageBean page = respose.getPage();
                boolean isLast = page != null && page.last;
                mView.retrunGetAllianceList(respose.getResult(), isLast, 1);
            }

            @Override
            protected void _onError(String message) {
                mView.returnGetAllianceError();
            }
        }));


    }

    @Override
    public void getAllianceList(final int pageNum) {
        mRxManage.add(mModel.getAllianceList(mContext, pageNum).subscribe(new RxSubscriber<BaseRespose<List<AllianceItem>>>(mContext) {
            @Override
            protected void _onNext(BaseRespose<List<AllianceItem>> respose) {
                PageBean page = respose.getPage();
                boolean isLast = page != null && page.last;
                mView.retrunGetAllianceList(respose.getResult(), isLast, pageNum);
            }

            @Override
            protected void _onError(String message) {
                mView.returnGetAllianceError();
            }
        }));


    }
}
