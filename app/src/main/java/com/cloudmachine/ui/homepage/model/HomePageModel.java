package com.cloudmachine.ui.homepage.model;

import com.cloudmachine.api.Api;
import com.cloudmachine.api.HostType;
import com.cloudmachine.base.baserx.RxSchedulers;
import com.cloudmachine.struc.LatestDailyEntity;
import com.cloudmachine.ui.homepage.contract.HomePageContract;

import rx.Observable;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/3/18 上午7:54
 * 修改人：shixionglu
 * 修改时间：2017/3/18 上午7:54
 * 修改备注：
 */

public class HomePageModel implements HomePageContract.Model{


    @Override
    public Observable<LatestDailyEntity> getLatestDaily() {
        return Api.getDefault(HostType.ZHIHU_HOST).getLatestDaily()
                .compose(RxSchedulers.<LatestDailyEntity>io_main());
    }
}
