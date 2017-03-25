package com.cloudmachine.recycleadapter.delegate;

import com.cloudmachine.R;
import com.cloudmachine.itemtype.HomeTypeItem;
import com.cloudmachine.struc.HomeLocalBean;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * 项目名称：CloudMachine
 * 类描述：本地布局委托类
 * 创建人：shixionglu
 * 创建时间：2017/3/25 下午1:16
 * 修改人：shixionglu
 * 修改时间：2017/3/25 下午1:16
 * 修改备注：
 */

public class HomeLocalDelegate implements ItemViewDelegate<HomeTypeItem> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.delegate_item_homelocal;
    }

    @Override
    public boolean isForViewType(HomeTypeItem item, int position) {
        return item instanceof HomeLocalBean;
    }

    @Override
    public void convert(ViewHolder holder, HomeTypeItem homeTypeItem, int position) {

    }
}
