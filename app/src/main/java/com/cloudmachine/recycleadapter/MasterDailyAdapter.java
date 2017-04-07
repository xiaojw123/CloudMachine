package com.cloudmachine.recycleadapter;

import android.content.Context;

import com.cloudmachine.recycleadapter.delegate.MasterDailyContentDelegate;
import com.cloudmachine.recyclerbean.MasterDailyType;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.List;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/4/7 上午9:13
 * 修改人：shixionglu
 * 修改时间：2017/4/7 上午9:13
 * 修改备注：
 */

public class MasterDailyAdapter extends MultiItemTypeAdapter<MasterDailyType> {

    public MasterDailyAdapter(Context context, List<MasterDailyType> datas) {
        super(context, datas);
        addItemViewDelegate(new MasterDailyContentDelegate());
    }
}
