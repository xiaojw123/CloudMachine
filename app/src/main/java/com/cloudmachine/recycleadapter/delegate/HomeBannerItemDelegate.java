package com.cloudmachine.recycleadapter.delegate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.cloudmachine.R;
import com.cloudmachine.activities.WebviewActivity;
import com.cloudmachine.loader.GlideImageLoader;
import com.cloudmachine.recyclerbean.HomeBannerTransfer;
import com.cloudmachine.recyclerbean.HomePageType;
import com.cloudmachine.utils.Banner.Banner;
import com.cloudmachine.utils.Constants;
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
    private HomeBannerTransfer mHomeBannerTransfer;

    @Override
    public int getItemViewLayoutId() {
        return R.layout.home_banner;
    }

    @Override
    public boolean isForViewType(HomePageType item, int position) {
        return item instanceof HomeBannerTransfer;
    }

    @Override
    public void convert(ViewHolder holder, HomePageType homeTypeItem, int position) {
        mContext = holder.getConvertView().getContext();
        Banner banner = holder.getView(R.id.banner);
        mHomeBannerTransfer = (HomeBannerTransfer) homeTypeItem;
        banner.setImages(mHomeBannerTransfer.images)
                .setBannerTitles(mHomeBannerTransfer.titles)
                .setImageLoader(GlideImageLoader.getInstance())
                .setOnBannerClickListener(this)
                .start();
    }

    @Override
    public void OnBannerClick(int position) {
        //对banner点击进行链接跳转
        Intent intent = new Intent(mContext, WebviewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.P_WebView_Url,mHomeBannerTransfer.jumpLinks.get(position));
        Constants.MyLog("打印到的跳转链接"+mHomeBannerTransfer.jumpLinks.get(position));
        //分享标题
        bundle.putString(Constants.P_WebView_Title,mHomeBannerTransfer.titles.get(position));
        bundle.putBoolean(Constants.HOME_BANNER_SHARE,true);
        //微信分享的链接
        bundle.putString(Constants.HOME_SHARE_URL,mHomeBannerTransfer.wxUrl.get(position));
        //微信分享的图标
        bundle.putString(Constants.HOME_SHARE_ICON,mHomeBannerTransfer.wxLogo.get(position));
        //微信分享描述
        bundle.putString(Constants.HOME_SHARE_DESCIRPTION,mHomeBannerTransfer.description.get(position));
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }
}
