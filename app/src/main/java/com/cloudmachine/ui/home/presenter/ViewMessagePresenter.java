package com.cloudmachine.ui.home.presenter;

import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.base.bean.PageBean;
import com.cloudmachine.bean.MessageBO;
import com.cloudmachine.ui.home.contract.ViewMessageConract;
import com.cloudmachine.utils.ToastUtils;

import java.util.List;

/**
 * Created by xiaojw on 2017/7/18.
 */

public class ViewMessagePresenter extends ViewMessageConract.Presenter {

    @Override
    public void getMessageList(int page) {
        mRxManage.add(mModel.getMessageList(page).subscribe(new RxSubscriber<BaseRespose<List<MessageBO>>>(mContext) {
            @Override
            protected void _onNext(BaseRespose<List<MessageBO>> br) {
                PageBean page = br.getPage();
                boolean isFirst = false;
                boolean isLast = false;
                if (page != null) {
                    isFirst = page.first;
                    isLast = page.last;
                }
                mView.updateMessageList(br.getResult(), isFirst, isLast);
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(mContext, message);
                mView.updateMessageError();
            }
        }));

    }

    @Override
    public void receiveMessage(final MessageBO item, final int status) {
        mRxManage.add(mModel.receiveMessage(item.getId(), status).subscribe(new RxSubscriber<String>(mContext) {
            @Override
            protected void _onNext(String s) {
                item.setStatus(status);
                mView.updateListAdapter();

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(mContext, message);

            }
        }));

    }

    @Override
    public void deleteMessage(final MessageBO msgBo) {
        mRxManage.add(mModel.deleteMessage(msgBo.getId()).subscribe(new RxSubscriber<String>(mContext, false) {
            @Override
            protected void _onNext(String s) {
                mView.returnDeleteMessage(msgBo);
            }

            @Override
            protected void _onError(String message) {

            }
        }));
    }


    @Override
    public void updateMsgStatus(final MessageBO msgBO) {
        mRxManage.add(mModel.updateMsgStatus(msgBO.getId()).subscribe(new RxSubscriber<String>(mContext) {
            @Override
            protected void _onNext(String s) {
                msgBO.setStatus(4);
                mView.updateListAdapter();
            }

            @Override
            protected void _onError(String message) {

            }
        }));
    }


}
