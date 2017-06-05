package com.cloudmachine.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.cloudmachine.R;

/**
 * Created by xiaojw on 2017/6/5.
 */

public class NotfyImgView extends ImageView {
    int centerX, centerY, radius;
    boolean isNotifyVisbile;

    public NotfyImgView(Context context) {
        this(context, null);
    }

    public NotfyImgView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NotfyImgView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.NotifyTextView);
        centerX = (int) ta.getDimension(R.styleable.NotfyImgView_point_center_x, 0);
        centerY = (int) ta.getDimension(R.styleable.NotfyImgView_point_center_y, 25);
        radius = (int) ta.getDimension(R.styleable.NotfyImgView_point_radius, 10);
        ta.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isNotifyVisbile) {
            Paint paint = new Paint();
            paint.setColor(getResources().getColor(R.color.oval_message_color));
            Drawable drawable = getDrawable();
            canvas.drawCircle(drawable.getIntrinsicWidth() - centerX, centerY, radius, paint);
        }
    }

    public void setNotifyPointVisible(boolean isVisible) {
        isNotifyVisbile = isVisible;
        invalidate();
    }
}
