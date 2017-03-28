package com.cloudmachine.recycleadapter.delegate;

import com.cloudmachine.R;
import com.cloudmachine.recyclerbean.HomeNewsBean;
import com.cloudmachine.recyclerbean.HomePageType;
import com.cloudmachine.utils.Constants;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/3/26 下午4:46
 * 修改人：shixionglu
 * 修改时间：2017/3/26 下午4:46
 * 修改备注：
 */

public class HomeNewsDelegate implements ItemViewDelegate<HomePageType> {


    @Override
    public int getItemViewLayoutId() {
        Constants.MyLog("进来了");
        return R.layout.delegate_item_news;
    }

    @Override
    public boolean isForViewType(HomePageType item, int position) {
        return item instanceof HomeNewsBean;
    }

    @Override
    public void convert(ViewHolder holder, HomePageType homePageType, int position) {

    }
}
