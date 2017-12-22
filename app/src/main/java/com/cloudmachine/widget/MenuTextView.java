package com.cloudmachine.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.cloudmachine.utils.DensityUtil;

/**
 * Created by xiaojw on 2017/12/20.
 */

public class MenuTextView extends TextView {
    private boolean mIsHot;
    private int mRaudis;
    private int yOffset;
    private int yTextOffset;
    private Paint mPaint;
    private Paint mTextPaint;
    private int mTextSize;
    private int mRightOffset;

    public MenuTextView(Context context) {
        this(context, null);
    }

    public MenuTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MenuTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRightOffset = DensityUtil.dip2px(context, 14);
        yOffset = DensityUtil.dip2px(context, 5);
        mRaudis = DensityUtil.dip2px(context, 8);
        mTextSize = DensityUtil.dip2px(context, 10);
        yTextOffset = DensityUtil.dip2px(context, 1.2f);
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mTextPaint = new Paint();
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(Color.WHITE);
    }

    public void setMenuHot(boolean isHot) {
        mIsHot = isHot;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mIsHot) {
            setMeasuredDimension(getMeasuredWidth() + mRightOffset, getMeasuredHeight());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mIsHot) {
            float cx = getMeasuredWidth() - mRaudis;
            float cy = yOffset + mRaudis;
            canvas.drawCircle(cx, cy, mRaudis, mPaint);
            canvas.drawText("新", cx - mTextSize / 2, cy + mTextSize / 2 - yTextOffset, mTextPaint);
        }
        super.onDraw(canvas);

    }
}
