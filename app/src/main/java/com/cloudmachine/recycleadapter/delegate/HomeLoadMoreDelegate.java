package com.cloudmachine.recycleadapter.delegate;

import android.view.View;
import android.widget.RelativeLayout;

import com.cloudmachine.R;
import com.cloudmachine.recyclerbean.HomeLoadMoreBean;
import com.cloudmachine.recyclerbean.HomePageType;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/3/27 上午11:42
 * 修改人：shixionglu
 * 修改时间：2017/3/27 上午11:42
 * 修改备注：
 */

public class HomeLoadMoreDelegate implements ItemViewDelegate<HomePageType>{
    @Override
    public int getItemViewLayoutId() {
        return R.layout.delegate_home_more_questions;
    }

    @Override
    public boolean isForViewType(HomePageType item, int position) {
        return item instanceof HomeLoadMoreBean;
    }

    @Override
    public void convert(ViewHolder holder, HomePageType homePageType, int position) {
        RelativeLayout rlLoadMore = (RelativeLayout) holder.getView(R.id.rl_loadmore);
        //加载更多点击
        rlLoadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
