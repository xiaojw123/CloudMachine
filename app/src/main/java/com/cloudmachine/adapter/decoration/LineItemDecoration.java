package com.cloudmachine.adapter.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.cloudmachine.R;
import com.cloudmachine.ui.home.contract.PayInfoContract;

/**
 * Created by xiaojw on 2018/7/4.
 */

public class LineItemDecoration extends RecyclerView.ItemDecoration {
   private Paint mLinePaint;
    public LineItemDecoration(Context context){
        mLinePaint=new Paint();
        mLinePaint.setColor(context.getResources().getColor(R.color.cor12));
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c,parent,state);
        int left=parent.getPaddingLeft();
        int right=parent.getMeasuredWidth()-parent.getPaddingRight();
        for (int i=0;i<parent.getChildCount()-1;i++){
            View child=parent.getChildAt(i);
            RecyclerView.LayoutParams params= (RecyclerView.LayoutParams) child.getLayoutParams();
            int top=child.getBottom()+params.bottomMargin;
            int bottom=top+1;
            RectF rectF=new RectF(left,top,right,bottom);
            c.drawRect(rectF,mLinePaint);
        }
    }
}
