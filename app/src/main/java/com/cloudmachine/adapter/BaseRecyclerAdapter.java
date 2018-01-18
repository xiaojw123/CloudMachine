package com.cloudmachine.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cloudmachine.adapter.holder.BaseHolder;

import java.util.List;

/**
 * Created by xiaojw on 2017/5/24.
 */

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter{
    Context mContext;
    List<T> mItems;
    OnItemClickListener listener;
    int position;

    public BaseRecyclerAdapter(Context context) {
        this(context, null);
    }


    public BaseRecyclerAdapter(Context context, List<T> items) {
        mContext = context;
        mItems = items;
    }


    public void updateItems(List<T> items) {
        mItems = items;
        notifyDataSetChanged();
    }
    public void updateItem(T item){
        mItems.add(0,item);
        notifyItemChanged(0);
    };

    public List<T> getItems(){
        return  mItems;
    }

    public T getItem(int position) {

        return mItems.get(position);
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        this.position=position;
        if (mItems != null && mItems.size() > 0) {
            T item = mItems.get(position);
            if (item != null) {
                holder.itemView.setTag(item);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener!=null){
                            listener.onItemClick(holder.itemView,position);
                        }

                    }
                });
                ((BaseHolder) holder).initViewHolder(item);
                ((BaseHolder)holder).initViewHolder(item,position);
            }
        }

    }


    @Override
    public int getItemCount() {
        return mItems != null ? mItems.size() : 0;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener=listener;
    }


    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }
}
