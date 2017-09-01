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
        return Api.getDefault(HostType.CLOUDM_HOST).deleteMsg(memberId,messageId).compose(RxHelper.<String>handleResult());
    }

    @Override
    public Observable<List<MessageBO>> getSystemMsg(long memberId) {
        return Api.getDefault(HostType.CLOUDM_HOST).getSystemMsg(memberId).compose(RxHelper.<List<MessageBO>>handleResult());
    }

    @Override
    public Observable<List<MessageBO>> getALLMsg(long memberId) {
        return Api.getDefault(HostType.CLOUDM_HOST).getAllMsg(memberId).compose(RxHelper.<List<MessageBO>>handleResult());
    }

    @Override
    public Observable<MessageBO> questionNeed(long memberId) {
        return Api.getDefault(HostType.CLOUDM_HOST).questionNeed(memberId).compose(RxHelper.<MessageBO>handleResult());
    }

    @Override
    public Observable<String> acceptMessage(long memberId, String messageId) {
        return Api.getDefault(HostType.CLOUDM_HOST).acceptMsg(memberId,messageId).compose(RxHelper.<String>handleResult());
    }

    @Override
    public Observable<String> rejectMessage(long memberId, String messageId) {
        return Api.getDefault(HostType.CLOUDM_HOST).rejectMsg(memberId,messageId).compose(RxHelper.<String>handleResult());
    }

    @Override
    public Observable<String> updateMsgStatus(long memberId, String messageId) {
        return Api.getDefault(HostType.CLOUDM_HOST).updateMsgStatus(memberId,messageId).compose(RxHelper.<String>handleResult());
    }
}
