package com.cloudmachine.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.cloudmachine.R;
import com.cloudmachine.bean.ScreenInfo;
import com.cloudmachine.chart.utils.AppLog;
import com.cloudmachine.utils.DensityUtil;

/**
 * Created by xiaojw on 2018/6/27.
 */

public class LetterIndexView extends View {
    private String[] letterArray = new String[]{"A", "B", "C", "D", "E", "F", "G",
            "H", "I", "J", "K", "L", "M", "N",
            "O", "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z"};
    int height, bound;
    int textSize;
    Paint paint = new Paint();
    Paint bgPaint = new Paint();
    int itemHeight, itemWidth;
    int touchIndex = -1;
    WordChangeListener listener;

    public LetterIndexView(Context context) {
        this(context, null);
    }

    public LetterIndexView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LetterIndexView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        height = DensityUtil.dip2px(context, 14);
        bound = DensityUtil.dip2px(context, 4);
        bgPaint.setColor(getResources().getColor(R.color.c_ff8901));
        paint.setColor(getResources().getColor(R.color.cor9));
        paint.setTextSize(DensityUtil.dip2px(context, 10));
    }

    public void setOnWordChangeListener(WordChangeListener listener) {
        this.listener = listener;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        itemWidth = getMeasuredWidth();
        itemHeight = getMeasuredHeight() / 26;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < letterArray.length; i++) {
            String word = letterArray[i];
            Rect rect = new Rect();
            int wordWidth = rect.width();
//            int wordHeight=rect.height();
            paint.getTextBounds(word, 0, 1, rect);
//            if (touchIndex == i) {
//                canvas.drawCircle(itemWidth / 2, i * itemHeight+itemWidth/2+wordHeight/2-itemWidth/5, itemWidth / 5, bgPaint);
//                paint.setColor(Color.WHITE);
//            } else {
//                paint.setColor(getResources().getColor(R.color.cor9));
//            }
            canvas.drawText(word, itemWidth / 2 - wordWidth / 2, i * itemHeight + itemWidth/ 2, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                AppLog.print("ACTION_DOWN Y__" + event.getY());
                int index = (int) event.getY() / itemHeight;
                if (index >= 0 && index != touchIndex) {
                    touchIndex = index;
                }
                if (listener != null) {
                    if (letterArray.length > touchIndex) {
                        listener.wordChagne(letterArray[touchIndex]);
//                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;

    }

    public interface WordChangeListener {

        void wordChagne(String word);
    }


}
