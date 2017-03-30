package com.cloudmachine.recycleadapter.delegate;

import android.view.View;
import android.widget.RelativeLayout;

import com.cloudmachine.R;
import com.cloudmachine.base.baserx.RxBus;
import com.cloudmachine.recyclerbean.HomeHotIssueBean;
import com.cloudmachine.recyclerbean.HomePageType;
import com.cloudmachine.utils.Constants;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/3/27 上午8:12
 * 修改人：shixionglu
 * 修改时间：2017/3/27 上午8:12
 * 修改备注：
 */

public class HomeIssueDelegate implements ItemViewDelegate<HomePageType>{
    @Override
    public int getItemViewLayoutId() {
        return R.layout.delegate_home_hotissue;
    }

    @Override
    public boolean isForViewType(HomePageType item, int position) {
        return item instanceof HomeHotIssueBean;
    }

    @Override
    public void convert(ViewHolder holder, HomePageType homePageType, int position) {
        //换一换按钮点击事件，需使用观察者模式
        RelativeLayout rlRefresh = (RelativeLayout) holder.getView(R.id.rl_refresh);
        rlRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxBus.getInstance().post(Constants.GET_HOTISSUE,"");
            }
        });
    }
}
