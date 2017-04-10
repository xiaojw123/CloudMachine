package com.cloudmachine.ui.homepage.presenter;

import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.recyclerbean.MasterDailyBean;
import com.cloudmachine.ui.homepage.contract.MasterDailyContract;
import com.cloudmachine.utils.ToastUtils;

import java.util.List;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/3/30 下午5:06
 * 修改人：shixionglu
 * 修改时间：2017/3/30 下午5:06
 * 修改备注：
 */

public class MasterDailyPresenter extends MasterDailyContract.Presenter {

    @Override
    public void getMasterDailyInfo(int page,int size ,Integer artStatus) {
       mRxManage.add(mModel.getMasterDaily(page,size,artStatus).subscribe(new RxSubscriber<BaseRespose<List<MasterDailyBean>>>(mContext,false) {
           @Override
           protected void _onNext(BaseRespose<List<MasterDailyBean>> listBaseRespose) {
               mView.returnMasterDailyInfo(listBaseRespose);
           }

           @Override
           protected void _onError(String message) {
               ToastUtils.error(message,true);
           }
       }));


    }

    @Override
    public void loadMoreDailyInfo(int page, int size,Integer artStatus) {
        mRxManage.add(mModel.getMasterDaily(page,size, artStatus).subscribe(new RxSubscriber<BaseRespose<List<MasterDailyBean>>>(mContext,false) {
            @Override
            protected void _onNext(BaseRespose<List<MasterDailyBean>> listBaseRespose) {
                mView.returnLoadMoreDailyInfo(listBaseRespose);
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.error(message,true);
            }
        }));
    }
}
