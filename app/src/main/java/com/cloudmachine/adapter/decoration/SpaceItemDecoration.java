package com.cloudmachine.adapter.decoration;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    int mSpace;


    public SpaceItemDecoration(Context context, int space) {
        this.mSpace = UIUtil.dip2px(context, space);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int itemCount = parent.getAdapter().getItemCount();
        int pos = parent.getChildAdapterPosition(view);
        outRect.left = 0;
        outRect.top = 0;
        outRect.bottom = mSpace;
//        if (pos != (itemCount - 1)) {
            outRect.right = mSpace;
//        } else {
//            outRect.right = 0;
//        }

    }
}