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
import com.cloudmachine.bean.AdBean;
import com.cloudmachine.helper.MobEvent;
import com.cloudmachine.ui.home.activity.QuestionCommunityActivity;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

/**
 * Created by xiaojw on 2017/5/23.
 */

public class ActivitesAdapter extends RecyclerView.Adapter<ActivitesAdapter.ActivitiesHoler> {
    private List<AdBean> mItems;
    Context mContext;

    public ActivitesAdapter(Context context) {
        mContext = context;
    }

    public void updateItems(List<AdBean> items) {
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
            AdBean bean = mItems.get(position);
            if (bean != null) {
                Glide.with(mContext).load(bean.getAdPicUrl()).into(holder.postImg);
                holder.titeTv.setText(bean.getAdTitle());
                holder.desTv.setText(bean.getAdDescription());
                holder.itemView.setTag(bean);
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
            MobclickAgent.onEvent(v.getContext(),MobEvent.COUNT_AD_CLICK);
            MobclickAgent.onEvent(v.getContext(), MobEvent.TIME_H5_AD_PAGE);
            AdBean bean = (AdBean) v.getTag();
            if (bean != null) {
                String url=bean.getAdJumpLink();
                String shareAddress=bean.getAdPicUrl();
                if (url != null && url.contains("?")) {
                    url += "&";
                } else {
                    url += "?";
                }
                url += "activityId=" + bean.getAdId();
                Intent intent = new Intent(mContext, QuestionCommunityActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(QuestionCommunityActivity.H5_URL, url);
                bundle.putString(QuestionCommunityActivity.SHARE_LINK,url);
                if (!TextUtils.isEmpty(shareAddress)){
                    bundle.putString(QuestionCommunityActivity.SHARE_PIC,shareAddress);
                }
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }


        }
    }
}
