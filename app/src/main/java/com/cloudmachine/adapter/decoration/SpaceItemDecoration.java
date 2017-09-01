package com.cloudmachine.adapter.decoration;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cloudmachine.utils.DensityUtil;

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    int mSpace;
    boolean singleDirection;


    public SpaceItemDecoration(Context context, int space) {
        this.mSpace = DensityUtil.dip2px(context, space);
    }

    public SpaceItemDecoration(Context context, int space, boolean singleDirection) {
        this.mSpace = DensityUtil.dip2px(context, space);
        this.singleDirection = singleDirection;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int itemCount = parent.getAdapter().getItemCount();
        int pos = parent.getChildAdapterPosition(view);
        outRect.left = 0;
        outRect.top = 0;
        outRect.bottom = mSpace;
//        if (pos != (itemCount - 1)) {
        if (!singleDirection) {
            outRect.right = mSpace;
        }
//        } else {
//            outRect.right = 0;
//        }

    }
}