package com.cloudmachine.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cloudmachine.R;
import com.cloudmachine.bean.ScreenInfo;
import com.cloudmachine.utils.CommonUtils;
import com.cloudmachine.utils.DensityUtil;

import java.util.List;

/**
 * Created by xiaojw on 2018/8/2.
 */

public class PicListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_ADD = 0x110;
    public static final int TYPE_PIC = 0x111;
    int itemHeight;
    Context mContext;
    List<String> mItems;
    OnItemClickListener listener;


    public PicListAdapter(Context context, List<String> items) {
        mContext = context;
        mItems = items;
        itemHeight = (ScreenInfo.screen_width - DensityUtil.dip2px(mContext, 50)) / 3;
    }

//    public PicListAdapter(Context context) {
//        super(context);

    //    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemDeleteClick(String picKey);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.LayoutParams cr = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight);
        cr.leftMargin = DensityUtil.dip2px(mContext, 5);
        cr.rightMargin = cr.leftMargin;
        cr.topMargin = cr.leftMargin;
        cr.bottomMargin = cr.leftMargin;
        if (viewType == TYPE_ADD) {
            FrameLayout addContainer=new FrameLayout(mContext);
            addContainer.setBackgroundColor(mContext.getResources().getColor(R.color.cor13));
            ImageView img=new ImageView(mContext);
            img.setImageResource(R.drawable.ic_pic_add);
            int width=DensityUtil.dip2px(mContext,18);
            FrameLayout.LayoutParams imgParams=new FrameLayout.LayoutParams(width,width);
            imgParams.gravity=Gravity.CENTER;
            addContainer.setLayoutParams(cr);
            addContainer.addView(img,imgParams);
            return new AddHolder(addContainer);
        } else {
            FrameLayout picLayout = new FrameLayout(mContext);
            picLayout.setLayoutParams(cr);
            ImageView img = new ImageView(mContext);
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            img.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            ImageView deleteImg = new ImageView(mContext);
            deleteImg.setImageResource(R.drawable.ic_person_delete);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.RIGHT;
            params.topMargin = DensityUtil.dip2px(mContext, 10);
            params.rightMargin = params.topMargin;
            deleteImg.setLayoutParams(params);
            picLayout.addView(img);
            picLayout.addView(deleteImg);
            return new PicHolder(picLayout);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        String url = "";
        if (mItems.size() > position) {
            url = mItems.get(position);
        }
        holder.itemView.setTag(url);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(holder.itemView, position);
                }
            }
        });
        if (holder instanceof PicHolder) {
            if (!TextUtils.isEmpty(url)) {
                Glide.with(mContext).load(url).into(((PicHolder) holder).picImg);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mItems.size() && position != 9) {
            return TYPE_ADD;
        } else {
            return TYPE_PIC;
        }
    }

    @Override
    public int getItemCount() {
        int count = mItems.size() + 1;
        count = Math.min(count, 9);
        return count;
    }

   private class AddHolder extends RecyclerView.ViewHolder {
       private AddHolder(View itemView) {
            super(itemView);

        }
    }

    private    class PicHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView picImg;
        ImageView delteImg;

        private PicHolder(View itemView) {
            super(itemView);
            ViewGroup parent = ((ViewGroup) itemView);
            picImg = (ImageView) parent.getChildAt(0);
            delteImg = (ImageView) parent.getChildAt(1);
            delteImg.setOnClickListener(this);
        }



        @Override
        public void onClick(final View v) {

            CommonUtils.showDeletePicDialog(mContext, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ViewGroup parent = (ViewGroup) v.getParent();
                    String tag = (String) parent.getTag();
                    mItems.remove(tag);
                    if (listener!=null){
                        listener.onItemDeleteClick(tag);
                    }
                    notifyDataSetChanged();
                }
            });
        }
    }


}
