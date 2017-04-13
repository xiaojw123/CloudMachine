package com.cloudmachine.ui.homepage.model;

import com.cloudmachine.api.Api;
import com.cloudmachine.api.HostType;
import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.recyclerbean.MasterDailyBean;
import com.cloudmachine.ui.homepage.contract.MasterDailyContract;

import java.util.List;

import rx.Observable;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/3/30 下午5:05
 * 修改人：shixionglu
 * 修改时间：2017/3/30 下午5:05
 * 修改备注：
 */

public class MasterDailyModel implements MasterDailyContract.Model{
    @Override
    public Observable<BaseRespose<List<MasterDailyBean>>> getMasterDaily(int page, int size,Integer artStatus) {
        return Api.getDefault(HostType.GUOSHUAI_HOST).getMasterDaily( page, size,artStatus)
                .compose(RxHelper.<List<MasterDailyBean>>handleBaseResult());
    }
}
