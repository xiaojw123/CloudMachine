package com.cloudmachine.ui.adapter;

import android.content.Context;

import com.cloudmachine.ui.delegate.HomeBannerItemDelegate;
import com.cloudmachine.ui.itemtype.HomeTypeItem;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.List;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/3/20 下午3:28
 * 修改人：shixionglu
 * 修改时间：2017/3/20 下午3:28
 * 修改备注：
 */

public class HomePageAdapter extends MultiItemTypeAdapter<HomeTypeItem> {

    public HomePageAdapter(Context context, List<HomeTypeItem> datas) {
        super(context, datas);
        addItemViewDelegate(new HomeBannerItemDelegate());

    }
}
