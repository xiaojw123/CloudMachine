package com.cloudmachine.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cloudmachine.R;
import com.cloudmachine.adapter.holder.BaseHolder;
import com.cloudmachine.bean.PickItemBean;
import com.cloudmachine.ui.home.activity.WorkPickItemActivity;
import com.cloudmachine.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xiaojw on 2017/11/9.
 * TODO:V3.4.1
 */

public class WorkPicListAdapter extends BaseRecyclerAdapter<PickItemBean> {
    public WorkPicListAdapter(Context context) {
        super(context);
    }

    public WorkPicListAdapter(Context context, List<PickItemBean> items) {
        super(context, items);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.list_item_work_pic, parent, false);
        return new WorkPicHolder(itemView);
    }

    class WorkPicHolder extends BaseHolder<PickItemBean> {
        @BindView(R.id.item_work_pic_img)
        ImageView picImg;
        @BindView(R.id.item_work_pic_date)
        TextView dateTv;
        @BindView(R.id.item_work_pic_day)
        TextView dayTv;
        @BindView(R.id.item_work_pic_day_rlt)
        RelativeLayout dayRlt;
        @BindView(R.id.item_work_pic_layout)
        FrameLayout   picFlt;

        public WorkPicHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void initViewHolder(final PickItemBean item, final int position) {
            if (item != null) {
                picFlt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList(WorkPickItemActivity.KEY_PIC_ITEM_lIST, (ArrayList<PickItemBean>)mItems);
                        bundle.putInt(WorkPickItemActivity.KEY_PIC_ITEM_POSITOIN, position);
                        Constants.toActivity((Activity) mContext, WorkPickItemActivity.class, bundle);
                    }
                });
                Glide.with(mContext).load(item.getImgUrl()).placeholder(R.drawable.icon_item_defalut).error(R.drawable.icon_item_defalut).crossFade().into(picImg);
                String putTime = item.getPutTime();
                dateTv.setText(putTime);
                String day = putTime != null ? putTime.substring(0, 10) : null;
                if (position == 0) {
                    dayRlt.setVisibility(View.VISIBLE);
                    dayTv.setText(day);
                } else {
                    PickItemBean lastItem = mItems.get(position - 1);
                    if (lastItem != null) {
                        String lastDay = lastItem.getPutTime() != null ? lastItem.getPutTime().substring(0, 10) : null;
                        if (day != null && !day.equals(lastDay)) {
                            dayRlt.setVisibility(View.VISIBLE);
                            dayTv.setText(day);
                            return;
                        }
                    }
                    dayRlt.setVisibility(View.GONE);
                }


            }

        }

        @Override
        public void initViewHolder(PickItemBean item) {

        }
    }
}
