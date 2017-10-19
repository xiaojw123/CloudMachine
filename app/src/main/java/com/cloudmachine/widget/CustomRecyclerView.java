package com.cloudmachine.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by xiaojw on 2017/6/19.
 * 基础RecyclerView封装
 * (item点击事件)
 */

public class CustomRecyclerView extends RecyclerView {
    public CustomRecyclerView(Context context) {
        this(context,null);
    }

    public CustomRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        addOnItemTouchListener(new ItemClickListener(this,listener,null));

    }
    public void setOnItemLongClickListener(OnItemLongClickListener listener){
        addOnItemTouchListener(new ItemClickListener(this,null,listener));
    }

    public interface OnItemLongClickListener{

        void onItemLongClick(View view, int position);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

    }

   private class ItemClickListener extends SimpleOnItemTouchListener {


        private OnItemClickListener clickListener;
        private OnItemLongClickListener lClickListener;
        private GestureDetectorCompat gestureDetector;



        private ItemClickListener(final RecyclerView recyclerView,
                                  final OnItemClickListener listener, OnItemLongClickListener lListener) {
            this.clickListener = listener;
            this.lClickListener =lListener;
            gestureDetector = new GestureDetectorCompat(recyclerView.getContext(),
                    new GestureDetector.SimpleOnGestureListener() {
                        @Override
                        public boolean onSingleTapUp(MotionEvent e) {
                            View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                            if (childView != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                                clickListener.onItemClick(childView, recyclerView.getChildAdapterPosition(childView));
                            }
                            return true;
                        }

                        @Override
                        public void onLongPress(MotionEvent e) {
                            View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                            if (childView != null && lClickListener != null) {
                                lClickListener.onItemLongClick(childView,
                                        recyclerView.getChildAdapterPosition(childView));
                            }
                        }
                    });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            gestureDetector.onTouchEvent(e);
            return false;
        }
    }
}
