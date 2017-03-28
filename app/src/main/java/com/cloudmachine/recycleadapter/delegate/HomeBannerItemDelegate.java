package com.cloudmachine.recycleadapter.delegate;

import android.content.Context;

import com.cloudmachine.R;
import com.cloudmachine.loader.GlideImageLoader;
import com.cloudmachine.recyclerbean.HomeBannerTransfer;
import com.cloudmachine.recyclerbean.HomePageType;
import com.cloudmachine.utils.Banner.Banner;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * 项目名称：CloudMachine
 * 类描述：轮播图委托类型
 * 创建人：shixionglu
 * 创建时间：2017/3/20 下午5:45
 * 修改人：shixionglu
 * 修改时间：2017/3/20 下午5:45
 * 修改备注：
 */

public class HomeBannerItemDelegate implements ItemViewDelegate<HomePageType>, Banner.OnBannerClickListener{

    private static final String TAG = "HomeHeaderItemDelegate";

    private Context mContext;

    @Override
    public int getItemViewLayoutId() {
        return R.layout.banner;
    }

    @Override
    public boolean isForViewType(HomePageType item, int position) {
        return item instanceof HomeBannerTransfer;
    }

    @Override
    public void convert(ViewHolder holder, HomePageType homeTypeItem, int position) {
        mContext = holder.getConvertView().getContext();
        Banner banner = holder.getView(R.id.banner);
        HomeBannerTransfer homeBannerTransfer = (HomeBannerTransfer) homeTypeItem;
        banner.setImages(homeBannerTransfer.images)
               /* .setBannerTitles(homeBannerTransfer.titles)*/
                .setImageLoader(GlideImageLoader.getInstance())
                .setOnBannerClickListener(this)
                .start();
    }

    @Override
    public void OnBannerClick(int position) {
        //对banner点击进行链接跳转
    }
}
