package com.cloudmachine.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.adapter.holder.BaseHolder;
import com.cloudmachine.bean.VideoBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xiaojw on 2018/3/12.
 */

public class VideoListAdapter extends BaseRecyclerAdapter<VideoBean.VideoListBean> {

    public VideoListAdapter(Context context) {
        super(context);
    }

    public VideoListAdapter(Context context, List<VideoBean.VideoListBean> items) {
        super(context, items);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_video, parent, false);
        return new VieoListHolder(itemView);
    }

    public class VieoListHolder extends BaseHolder<VideoBean.VideoListBean> {


        @BindView(R.id.item_video_time_start)
        TextView itemVideoTimeStart;
        @BindView(R.id.item_video_time_len)
        TextView itemVideoTimeLen;
        @BindView(R.id.item_video_playstatus)
        ImageView itemVideoPlaystatus;

        public VieoListHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void initViewHolder(VideoBean.VideoListBean item) {
            itemVideoTimeStart.setText(item.getName());
            itemVideoTimeLen.setText(item.getTime());
            itemVideoPlaystatus.setSelected(item.isSelected());
        }
    }


}
