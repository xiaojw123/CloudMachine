package com.cloudmachine.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by xiaojw on 2017/5/24.
 */

public abstract class BaseHolder<T> extends RecyclerView.ViewHolder {
    public BaseHolder( View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
    public  abstract void initViewHolder(T item);
}
