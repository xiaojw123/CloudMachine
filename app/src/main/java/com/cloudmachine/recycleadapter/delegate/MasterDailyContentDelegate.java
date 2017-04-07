package com.cloudmachine.recycleadapter.delegate;

import com.cloudmachine.R;
import com.cloudmachine.recyclerbean.MasterDailyBean;
import com.cloudmachine.recyclerbean.MasterDailyType;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * 项目名称：CloudMachine
 * 类描述：大师日报正文
 * 创建人：shixionglu
 * 创建时间：2017/4/7 上午9:19
 * 修改人：shixionglu
 * 修改时间：2017/4/7 上午9:19
 * 修改备注：
 */

public class MasterDailyContentDelegate implements ItemViewDelegate<MasterDailyType>{
    @Override
    public int getItemViewLayoutId() {
        return R.layout.recycler_item_masterdaily;
    }

    @Override
    public boolean isForViewType(MasterDailyType item, int position) {
        return item instanceof MasterDailyBean;
    }

    @Override
    public void convert(ViewHolder holder, MasterDailyType masterDailyType, int position) {


    }
}
