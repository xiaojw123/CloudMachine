package com.cloudmachine.recycleadapter;

import android.content.Context;

import com.cloudmachine.recycleadapter.delegate.HomeBannerItemDelegate;
import com.cloudmachine.recycleadapter.delegate.HomeDividerDelegate;
import com.cloudmachine.recycleadapter.delegate.HomeIssueDelegate;
import com.cloudmachine.recycleadapter.delegate.HomeIssueDetailDelegate;
import com.cloudmachine.recycleadapter.delegate.HomeLoadMoreDelegate;
import com.cloudmachine.recycleadapter.delegate.HomeLocalDelegate;
import com.cloudmachine.recycleadapter.delegate.HomeNewsDelegate;
import com.cloudmachine.recyclerbean.HomePageType;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.List;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/3/26 下午1:25
 * 修改人：shixionglu
 * 修改时间：2017/3/26 下午1:25
 * 修改备注：
 */

public class HomePageAdapter extends MultiItemTypeAdapter<HomePageType>{
    public   HomeLocalDelegate localDelegate;

    public HomePageAdapter(Context context, List<HomePageType> datas) {
        super(context, datas);

        addItemViewDelegate(new HomeBannerItemDelegate());
        localDelegate =new HomeLocalDelegate();
        addItemViewDelegate(localDelegate);
        addItemViewDelegate(new HomeDividerDelegate());
        addItemViewDelegate(new HomeNewsDelegate());
        addItemViewDelegate(new HomeIssueDelegate());
        addItemViewDelegate(new HomeIssueDetailDelegate());
        addItemViewDelegate(new HomeLoadMoreDelegate());
    }
}
