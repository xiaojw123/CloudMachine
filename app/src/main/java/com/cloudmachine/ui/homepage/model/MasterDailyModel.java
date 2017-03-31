package com.cloudmachine.ui.homepage.model;

import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.ui.homepage.contract.MasterDailyContract;

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
    public Observable<BaseRespose> getMasterDaily(int page, int size) {
        return null;
    }
}
