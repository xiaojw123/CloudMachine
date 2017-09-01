package com.cloudmachine.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cloudmachine.activities.BigPicActivity;

import java.util.ArrayList;

/**
 * Created by xiaojw on 2017/4/25.
 */

public class PhotoListAdapter extends RecyclerView.Adapter<PhotoListAdapter.PhotoHolder> {
    Context mContext;
    ArrayList<String> mItems = new ArrayList<>();


    public void updateItems(ArrayList<String> items) {
        mItems = items;
        notifyDataSetChanged();
    }


    @Override
    public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        ImageView img = new ImageView(mContext);
        img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        img.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300));
        return new PhotoHolder(img);
    }

    @Override
    public void onBindViewHolder(final PhotoHolder holder, final int position) {
        if (mItems.size() > 0) {
            String url=mItems.get(position);
            Glide.with(mContext).load(url).into((ImageView) holder.itemView);
            holder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(mContext, BigPicActivity.class);
                    intent.putExtra(BigPicActivity.POSITION,position);
                    intent.putStringArrayListExtra(BigPicActivity.BIG_PIC_URLS,mItems);
                    mContext.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mItems!=null?mItems.size():0;
    }

    class PhotoHolder extends RecyclerView.ViewHolder{

        public PhotoHolder(View itemView) {
            super(itemView);
        }

    }




}
