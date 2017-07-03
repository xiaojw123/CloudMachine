package com.cloudmachine.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cloudmachine.activities.WanaCloudBox;
import com.cloudmachine.activities.WebviewActivity;
import com.cloudmachine.recyclerbean.HomeBannerBean;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.UMengKey;
import com.github.mikephil.charting.utils.AppLog;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaojw on 2017/5/23.
 */

public class ActivitesAdapter extends RecyclerView.Adapter<ActivitesAdapter.ActivitiesHoler> {
    List<HomeBannerBean> mItems;
    Context mContext;

    public ActivitesAdapter(Context context) {
        mContext = context;
    }

    public void updateItems(ArrayList<HomeBannerBean> items) {
        mItems = items;
        notifyDataSetChanged();
    }


    @Override
    public ActivitiesHoler onCreateViewHolder(ViewGroup parent, int viewType) {
        ImageView img = new ImageView(mContext);
        img.setScaleType(ImageView.ScaleType.FIT_XY);
        img.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 400));
        return new ActivitiesHoler(img);
    }

    @Override
    public void onBindViewHolder(ActivitiesHoler holder, int position) {
        if (mItems != null && mItems.size() > 0) {
            HomeBannerBean bannerBean = mItems.get(position);
            if (bannerBean != null) {
                AppLog.print("siez__img udpate");
                ImageView itemImg = (ImageView) holder.itemView;
                Glide.with(mContext).load(bannerBean.picAddress).into(itemImg);
                itemImg.setTag(bannerBean);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mItems != null ? mItems.size() : 0;
    }

    public class ActivitiesHoler extends RecyclerView.ViewHolder implements View.OnClickListener {


        public ActivitiesHoler(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            MobclickAgent.onEvent(v.getContext(), UMengKey.count_ad_click);
            HomeBannerBean bean = (HomeBannerBean) v.getTag();
            if (bean != null) {
                String url= !TextUtils.isEmpty(bean.adsLink)?bean.adsLink:bean.picUrl;
                if (bean.skipType == 3) {
                    Intent wanaBoxIntent = new Intent(mContext, WanaCloudBox.class);
                    wanaBoxIntent.putExtra(Constants.P_WebView_Url, url);
                    mContext.startActivity(wanaBoxIntent);
                    return;

                }
                //对banner点击进行链接跳转
                Intent intent = new Intent(mContext, WebviewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(Constants.P_WebView_Url,url);
                //分享标题
                bundle.putString(Constants.P_WebView_Title, bean.adsTitle);
                bundle.putBoolean(Constants.HOME_BANNER_SHARE, true);
                //微信分享的链接
                bundle.putString(Constants.HOME_SHARE_URL, url);
                //微信分享的图标
                bundle.putString(Constants.HOME_SHARE_ICON, bean.shareAddress);
                //微信分享描述
                bundle.putString(Constants.HOME_SHARE_DESCIRPTION, bean.adsDescription);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }


        }
    }
}
