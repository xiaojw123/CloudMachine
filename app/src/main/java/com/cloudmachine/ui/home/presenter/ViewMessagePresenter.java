package com.cloudmachine.ui.home.presenter;

import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.bean.MessageBO;
import com.cloudmachine.ui.home.contract.ViewMessageConract;

import java.util.List;

/**
 * Created by xiaojw on 2017/7/18.
 */

public class ViewMessagePresenter extends ViewMessageConract.Presenter {
    @Override
    public void deleteMessage(long memberId, final MessageBO msgBo) {
      mRxManage.add(mModel.deleteMessage(memberId,msgBo.getId()).subscribe(new RxSubscriber<String>(mContext,false) {
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
    public void acceptMessage(long memberId, final MessageBO msgBO) {
        mRxManage.add(mModel.acceptMessage(memberId, String.valueOf(msgBO.getId())).subscribe(new RxSubscriber<String>(mContext,false) {
            @Override
            protected void _onNext(String stringBaseRespose) {
                msgBO.setStatus(3);
                mView.returnAcceptMessage();
            }

            @Override
            protected void _onError(String message) {

            }
        }));


    }

    @Override
    public void rejectMessage(long memberId, final MessageBO msgBO) {
        mRxManage.add(mModel.rejectMessage(memberId, String.valueOf(msgBO.getId())).subscribe(new RxSubscriber<String>(mContext,false) {
            @Override
            protected void _onNext(String stringBaseRespose) {
                msgBO.setStatus(2);
                mView.returnRejectMessage();
            }

            @Override
            protected void _onError(String message) {

            }
        }));


    }

    @Override
    public void updateMsgStatus(long memberId, final MessageBO msgBO) {
        mRxManage.add(mModel.updateMsgStatus(memberId,String.valueOf(msgBO.getId())).subscribe(new RxSubscriber<String>(mContext,false) {
            @Override
            protected void _onNext(String stringBaseRespose) {
                msgBO.setStatus(4);
                mView.updateMsgStatus();
            }

            @Override
            protected void _onError(String message) {

            }
        }));

    }

    @Override
    public void getSystemMsg(long memberId) {
        mRxManage.add(mModel.getSystemMsg(memberId).subscribe(new RxSubscriber<List<MessageBO>>(mContext, false) {
            @Override
            protected void _onNext(List<MessageBO> messageBOs) {
                mView.returnGetSystemMsg(messageBOs);
            }

            @Override
            protected void _onError(String message) {

            }
        }));

    }

    @Override
    public void getALLMsg(long memberId) {
        mRxManage.add(mModel.getALLMsg(memberId).subscribe(new RxSubscriber<List<MessageBO>>(mContext,false) {
            @Override
            protected void _onNext(List<MessageBO> messageBOs) {
                mView.retrunGetAllMsg(messageBOs);
            }

            @Override
            protected void _onError(String message) {

            }
        }));

    }

    @Override
    public void questionNeed(final long memberId) {
        mRxManage.add(mModel.questionNeed(memberId).subscribe(new RxSubscriber<MessageBO>(mContext,false) {
            @Override
            protected void _onNext(MessageBO messageBO) {
                messageBO.setMessageType(5);
                mView.retrunQuestionNeed(messageBO);
                getALLMsg(memberId);
            }

            @Override
            protected void _onError(String message) {
                getALLMsg(memberId);
            }
        }));

    }
}
