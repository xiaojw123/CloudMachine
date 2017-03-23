package com.cloudmachine.ui.delegate;

import android.content.Context;

import com.cloudmachine.R;
import com.cloudmachine.loader.GlideImageLoader;
import com.cloudmachine.ui.bean.HomeBannerBean;
import com.cloudmachine.ui.itemtype.HomeTypeItem;
import com.cloudmachine.ui.widget.Banner.Banner;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/3/20 下午5:45
 * 修改人：shixionglu
 * 修改时间：2017/3/20 下午5:45
 * 修改备注：
 */

public class HomeBannerItemDelegate implements ItemViewDelegate<HomeTypeItem>, Banner.OnBannerClickListener{

    private static final String TAG = "HomeBannerItemDelegate";
    private Context       mContext;
    private List<Integer> mIds;

    @Override
    public void OnBannerClick(int position) {

    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.home_banner;
    }

    @Override
    public boolean isForViewType(HomeTypeItem item, int position) {
        return item instanceof HomeBannerBean;
    }

    @Override
    public void convert(ViewHolder holder, HomeTypeItem homeTypeItem, int position) {
        mContext = holder.getConvertView().getContext();
        Banner banner = holder.getView(R.id.banner);
        HomeBannerBean item = (HomeBannerBean) homeTypeItem;
        mIds = item.getIds();
        banner.setImages(item.getImages())
                .setBannerTitles(item.getTitles())
                .setImageLoader(GlideImageLoader.getInstance())
                .setOnBannerClickListener(this)
                .start();
    }
}
