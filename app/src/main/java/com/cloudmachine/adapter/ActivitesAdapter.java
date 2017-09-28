package com.cloudmachine.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cloudmachine.R;
import com.cloudmachine.bean.HomeBannerBean;
import com.cloudmachine.ui.homepage.activity.QuestionCommunityActivity;
import com.cloudmachine.utils.UMengKey;
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
        View itmeView = LayoutInflater.from(mContext).inflate(R.layout.list_item_activites, parent, false);
        return new ActivitiesHoler(itmeView);
    }

    @Override
    public void onBindViewHolder(ActivitiesHoler holder, int position) {
        if (mItems != null && mItems.size() > 0) {
            HomeBannerBean bannerBean = mItems.get(position);
            if (bannerBean != null) {
                Glide.with(mContext).load(bannerBean.picAddress).into(holder.postImg);
                holder.titeTv.setText(bannerBean.adsTitle);
                holder.desTv.setText(bannerBean.adsDescription);
                holder.itemView.setTag(bannerBean);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mItems != null ? mItems.size() : 0;
    }

    public class ActivitiesHoler extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView postImg;
        TextView titeTv;
        TextView desTv;

        public ActivitiesHoler(View itemView) {
            super(itemView);
            postImg = (ImageView) itemView.findViewById(R.id.activites_post_img);
            titeTv = (TextView) itemView.findViewById(R.id.activites_title_tv);
            desTv = (TextView) itemView.findViewById(R.id.activites_des_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            MobclickAgent.onEvent(v.getContext(), UMengKey.count_ad_click);
            HomeBannerBean bean = (HomeBannerBean) v.getTag();
            if (bean != null) {
//                String url = !TextUtils.isEmpty(bean.adsLink) ? bean.adsLink : ;
                String url=bean.picUrl;
                String adsLink=bean.adsLink;
                String shareAddress=bean.shareAddress;

//                if (bean.skipType == 3) {
//                    Intent wanaBoxIntent = new Intent(mContext, WanaCloudBox.class);
//                    wanaBoxIntent.putExtra(Constants.P_WebView_Url, url);
//                    mContext.startActivity(wanaBoxIntent);
//                    return;
//
//                }
                //对banner点击进行链接跳转
//                if ("推广活动".equals(bean.adsTitle)) {
//                    if (url != null && url.contains("?")) {
//                        url += "&";
//                    } else {
//                        url += "?";
//                    }
//                    url += "activityId=" + bean.id;
//                }

                if (url != null && url.contains("?")) {
                    url += "&";
                } else {
                    url += "?";
                }
                url += "activityId=" + bean.id;
                Intent intent = new Intent(mContext, QuestionCommunityActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(QuestionCommunityActivity.H5_URL, url);
                if (!TextUtils.isEmpty(adsLink)){
                bundle.putString(QuestionCommunityActivity.SHARE_LINK,adsLink);
                }
                if (!TextUtils.isEmpty(shareAddress)){
                    bundle.putString(QuestionCommunityActivity.SHARE_PIC,shareAddress);
                }
//                //分享标题
//                bundle.putString(Constants.P_WebView_Title, bean.adsTitle);
//                bundle.putBoolean(Constants.HOME_BANNER_SHARE, true);
//                //微信分享的链接
//                bundle.putString(Constants.HOME_SHARE_URL, url);
//                //微信分享的图标
//                bundle.putString(Constants.HOME_SHARE_ICON, bean.shareAddress);
//                //微信分享描述
//                bundle.putString(Constants.HOME_SHARE_DESCIRPTION, bean.adsDescription);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }


        }
    }
}
