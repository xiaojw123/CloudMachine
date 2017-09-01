package com.cloudmachine.ui.homepage.presenter;

import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.bean.MessageBO;
import com.cloudmachine.ui.homepage.contract.SystemMessageContract;

import java.util.List;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/4/12 下午8:01
 * 修改人：shixionglu
 * 修改时间：2017/4/12 下午8:01
 * 修改备注：
 */

public class SystemMessagePresenter extends SystemMessageContract.Presenter{
    @Override
    public void getSystemMessage(Long memberId, int pageNo, int pageSize) {
        mRxManage.add(mModel.getSystemMessage(memberId,pageNo,pageSize)
        .subscribe(new RxSubscriber<List<MessageBO>>(mContext,false) {
            @Override
            protected void _onNext(List<MessageBO> messageBOs) {
                mView.returnSystemMessage(messageBOs);
            }

            @Override
            protected void _onError(String message) {

            }
        }));
    }

    @Override
    public void getMoreSystemMessage(Long memberId, int pageNo, int pageSize) {

        mRxManage.add(mModel.getSystemMessage(memberId,pageNo,pageSize)
                .subscribe(new RxSubscriber<List<MessageBO>>(mContext,false) {
                    @Override
                    protected void _onNext(List<MessageBO> messageBOs) {
                        mView.returnSystemMessage(messageBOs);
                    }

                    @Override
                    protected void _onError(String message) {

                    }
                }));
    }
}
