package com.cloudmachine.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cloudmachine.R;
import com.cloudmachine.adapter.holder.BaseHolder;
import com.cloudmachine.bean.PickItemBean;

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

        public WorkPicHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void initViewHolder(PickItemBean item) {
            if (item != null) {
                Glide.with(mContext).load(item.getImgUrl()).into(picImg);
                dateTv.setText(item.getPutTime());
            }

        }
    }
}
