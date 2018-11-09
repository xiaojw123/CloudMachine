package com.cloudmachine.ui.home.contract;

import com.cloudmachine.base.BaseModel;
import com.cloudmachine.base.BasePresenter;
import com.cloudmachine.base.BaseView;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.bean.MessageBO;

import java.util.List;

import rx.Observable;

/**
 * Created by xiaojw on 2017/7/18.
 */

public interface ViewMessageConract {
    interface View extends BaseView {
        void updateMessageList(List<MessageBO> messageItems,boolean isFirstPage,boolean isLastPage);
        void updateMessageError();
        void updateListAdapter();
        void returnDeleteMessage(MessageBO messageBO);
    }

    interface Model extends BaseModel {
        Observable<BaseRespose<List<MessageBO>>> getMessageList(int page);
        Observable<String> receiveMessage(int messageId,int status);
        Observable<String> deleteMessage(long messageId);
        Observable<String> updateMsgStatus(int messageId);
    }

     abstract class Presenter extends BasePresenter<ViewMessageConract.View, ViewMessageConract.Model> {
        public abstract void getMessageList(int page);
        public abstract void receiveMessage(final MessageBO item, final int status);

        public abstract void deleteMessage(MessageBO msgBo);


        public abstract void updateMsgStatus(MessageBO msgBO);




    }


}
