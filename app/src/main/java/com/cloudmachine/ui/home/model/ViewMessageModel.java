package com.cloudmachine.ui.home.model;

import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.bean.MessageBO;
import com.cloudmachine.ui.home.contract.ViewMessageConract;

import java.util.List;

import rx.Observable;

/**
 * Created by xiaojw on 2017/7/18.
 */

public class ViewMessageModel implements ViewMessageConract.Model {

    @Override
    public Observable<BaseRespose<List<MessageBO>>> getMessageList(int page) {
        return Api.getDefault(HostType.HOST_LARK).getMessageList(page,20).compose(RxHelper.<List<MessageBO>>handleBaseResult());
    }

    @Override
    public Observable<String> receiveMessage(int messageId, int status) {
        return Api.getDefault(HostType.HOST_LARK).receiveMessage(messageId,status).compose(RxHelper.<String>handleResult());
    }
    @Override
    public Observable<String> updateMsgStatus(int messageId) {
        return Api.getDefault(HostType.HOST_LARK).updateMsgStatus(messageId).compose(RxHelper.<String>handleResult());
    }
    @Override
    public Observable<String> deleteMessage(long messageId) {
        return Api.getDefault(HostType.HOST_LARK).deleteMsg(messageId).compose(RxHelper.<String>handleResult());
    }




}
