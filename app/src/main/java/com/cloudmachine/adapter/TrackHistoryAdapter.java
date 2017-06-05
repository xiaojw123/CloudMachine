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

import butterknife.BindView;

/**
 * Created by xiaojw on 2017/5/31.
 */

public class TrackHistoryAdapter extends BaseRecyclerAdapter {
    public TrackHistoryAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TrackHoler(LayoutInflater.from(mContext).inflate(R.layout.list_item_trackhistory,null));
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class TrackHoler extends BaseHolder {

        @BindView(R.id.item_trackhistory_icon1)
        ImageView itemTrackhistoryIcon1;
        @BindView(R.id.item_trackhistory_time)
        TextView itemTrackhistoryTime;
        @BindView(R.id.item_trackhistory_locaction)
        TextView itemTrackhistoryLocaction;
        @BindView(R.id.item_trackhistory_len)
        TextView itemTrackhistoryLen;
        @BindView(R.id.item_trackhistory_icon2)
        ImageView itemTrackhistoryIcon2;
        @BindView(R.id.item_trackhistory_end_time)
        TextView itemTrackhistoryEndTime;
        @BindView(R.id.item_trackhistory_end_locaction)
        TextView itemTrackhistoryEndLocaction;
        @BindView(R.id.item_len_text)
        TextView itemLenText;

        public TrackHoler(View itemView) {
            super(itemView);

        }

        @Override
        public void initViewHolder(Object item) {

        }
    }

}
