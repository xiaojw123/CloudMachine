package com.cloudmachine.ui.homepage.model;

import com.cloudmachine.api.Api;
import com.cloudmachine.api.HostType;
import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.struc.MessageBO;
import com.cloudmachine.ui.homepage.contract.SystemMessageContract;

import java.util.List;

import rx.Observable;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/4/12 下午8:00
 * 修改人：shixionglu
 * 修改时间：2017/4/12 下午8:00
 * 修改备注：
 */

public class SystemMessageModel implements SystemMessageContract.Model {
    @Override
    public Observable<List<MessageBO>> getSystemMessage(Long memberId, int pageNo, int pageSize) {
        return Api.getDefault(HostType.CLOUDM_HOST).getSystemMessage(memberId,pageNo,pageSize)
                .compose(RxHelper.<List<MessageBO>>handleResult());
    }
}
