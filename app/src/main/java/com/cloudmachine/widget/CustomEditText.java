package com.cloudmachine.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by xiaojw on 2017/6/23.
 * 为EditText注入left/right/top/bottom图标点击事件
 */

public class CustomEditText extends AppCompatEditText {
    private static final int LEFT_INDEX = 0;
    private static final int TOP_INDEX = 1;
    private static final int RIGHT_INDEX = 2;
    private static final int BOTTOM_INDEX = 3;
    DrawableRightClickListener drClickListener;
    DrawableLeftClickListener dlClickListener;
    DrawableTopClickListener dtClickListener;
    DrawableBottomClickListener dbClickListener;
    boolean isShowPassword;

    public CustomEditText(Context context) {
        super(context);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setDrawableRightClickListener(DrawableRightClickListener listener) {
        drClickListener = listener;
    }

    public void setDrawableLeftClickListener(DrawableLeftClickListener listener) {
        dlClickListener = listener;
    }

    public void setDrawableTopClickListener(DrawableTopClickListener listener) {
        dtClickListener = listener;
    }

    public void setDrawableBottomClickListener(DrawableBottomClickListener listener) {
        dbClickListener = listener;
    }

    public interface DrawableRightClickListener {
        void onDrawableRightClickListener(View view);
    }

    public interface DrawableLeftClickListener {
        void onDrawableLeftClickListener(View view);
    }

    public interface DrawableTopClickListener {
        void onDrawableTopClickListener(View view);
    }

    public interface DrawableBottomClickListener {
        void onDrawableBottomClickListener(View view);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            Drawable[] drawable = getCompoundDrawables();
            Drawable ld = drawable[LEFT_INDEX];
            Drawable rd = drawable[RIGHT_INDEX];
            Drawable td = drawable[TOP_INDEX];
            Drawable bd = drawable[BOTTOM_INDEX];
            float rawX = event.getRawX();
            float rawY = event.getRawY();
            if (drClickListener != null) {
                if (rd != null && rawX >= (getRight() - rd.getBounds().width())) {
                    drClickListener.onDrawableRightClickListener(this);
                }
            }
            if (dlClickListener != null) {
                if (ld != null && rawX <= (getLeft() + ld.getBounds().width())) {
                    dlClickListener.onDrawableLeftClickListener(this);
                }
            }
            if (dtClickListener != null) {
                if (td != null && rawY <= (getTop() - td.getBounds().height())) {
                    dtClickListener.onDrawableTopClickListener(this);
                }
            }
            if (dbClickListener != null) {
                if (bd != null && rawX >= (getBottom() + bd.getBounds().height())) {
                    dbClickListener.onDrawableBottomClickListener(this);
                }
            }

        }
        return super.onTouchEvent(event);
    }
}
