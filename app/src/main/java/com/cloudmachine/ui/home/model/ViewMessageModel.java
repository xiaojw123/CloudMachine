package com.cloudmachine.ui.home.model;

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
    public Observable<String> deleteMessage(long memberId, long messageId) {
        return Api.getDefault(HostType.HOST_CLOUDM_YJX).deleteMsg(memberId,messageId).compose(RxHelper.<String>handleResult());
    }

    @Override
    public Observable<List<MessageBO>> getSystemMsg(long memberId) {
        return Api.getDefault(HostType.HOST_CLOUDM_YJX).getSystemMsg(memberId).compose(RxHelper.<List<MessageBO>>handleResult());
    }

    @Override
    public Observable<List<MessageBO>> getALLMsg(long memberId,int pageNo) {
        return Api.getDefault(HostType.HOST_CLOUDM_YJX).getAllMsg(memberId,pageNo,20).compose(RxHelper.<List<MessageBO>>handleResult());
    }

    @Override
    public Observable<MessageBO> questionNeed(long memberId) {
        return Api.getDefault(HostType.HOST_CLOUDM_YJX).questionNeed(memberId).compose(RxHelper.<MessageBO>handleResult());
    }

    @Override
    public Observable<String> acceptMessage(long memberId, String messageId) {
        return Api.getDefault(HostType.HOST_CLOUDM_YJX).acceptMsg(memberId,messageId).compose(RxHelper.<String>handleResult());
    }

    @Override
    public Observable<String> rejectMessage(long memberId, String messageId) {
        return Api.getDefault(HostType.HOST_CLOUDM_YJX).rejectMsg(memberId,messageId).compose(RxHelper.<String>handleResult());
    }

    @Override
    public Observable<String> updateMsgStatus(long memberId, String messageId) {
        return Api.getDefault(HostType.HOST_CLOUDM_YJX).updateMsgStatus(memberId,messageId).compose(RxHelper.<String>handleResult());
    }
}