package com.cloudmachine.ui.home.contract;

import com.cloudmachine.base.BaseModel;
import com.cloudmachine.base.BasePresenter;
import com.cloudmachine.base.BaseView;
import com.cloudmachine.bean.MessageBO;

import java.util.List;

import rx.Observable;

/**
 * Created by xiaojw on 2017/7/18.
 */

public interface ViewMessageConract {
    interface View extends BaseView {
        void returnAcceptMessage();

        void returnRejectMessage();

        void updateMsgStatus();

        void returnGetSystemMsg(List<MessageBO> msgList);

        void retrunGetAllMsg(List<MessageBO> msgList);

        void retrunQuestionNeed(MessageBO messageBO);
        void returnDeleteMessage(MessageBO messageBO);
    }

    interface Model extends BaseModel {
        Observable<String> deleteMessage(long memberId, long messageId);
        Observable<List<MessageBO>> getSystemMsg(long memberId);

        Observable<List<MessageBO>> getALLMsg(long memberId,int pageNo);

        Observable<MessageBO> questionNeed(long memberId);


        Observable<String> acceptMessage(long memberId, String messageId);

        Observable<String> rejectMessage(long memberId, String messageId);

        Observable<String> updateMsgStatus(long memberId, String messageId);
    }

    public abstract class Presenter extends BasePresenter<ViewMessageConract.View, ViewMessageConract.Model> {
        public abstract void deleteMessage(long memberId,MessageBO msgBo);
        public abstract void acceptMessage(long memberId, final MessageBO msgBO);

        public abstract void rejectMessage(long memberId, final MessageBO msgBO);

        public abstract void updateMsgStatus(long memberId, MessageBO msgBO);

        public abstract void getSystemMsg(long memberId);

        public abstract void getALLMsg(long memberId, int pageNo);

        public abstract void questionNeed(long memberId);


    }


}
