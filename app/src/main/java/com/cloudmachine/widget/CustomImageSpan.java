package com.cloudmachine.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.text.style.ImageSpan;

/**
 * Created by xiaojw on 2018/8/23.
 */

public class CustomImageSpan extends ImageSpan {
    public CustomImageSpan(Context context, @DrawableRes int resourceId) {
        super(context, resourceId);
    }
//
//    public int getSize(Paint paint, CharSequence text, int start, int end,
//                       Paint.FontMetricsInt fm) {
//        Drawable d = getDrawable();
//        Rect rect = d.getBounds();
//        if (fm != null) {
//            Paint.FontMetricsInt fmPaint=paint.getFontMetricsInt();
//            int fontHeight = fmPaint.bottom - fmPaint.top;
//            int drHeight=rect.bottom-rect.top;
//
//            int top= drHeight/2 - fontHeight/4;
//            int bottom=drHeight/2 + fontHeight/4;
//
//            fm.ascent=-bottom;
//            fm.top=-bottom;
//            fm.bottom=top;
//            fm.descent=top;
//        }
//        return rect.right;
//    }
//
//    @Override
//    public void draw(Canvas canvas, CharSequence text, int start, int end,
//                     float x, int top, int y, int bottom, Paint paint) {
//        Drawable b = getDrawable();
//        canvas.save();
//        int transY = 0;
//        transY = ((bottom+24-top) - b.getBounds().bottom)/2+top;
//        canvas.translate(x, transY);
//        b.draw(canvas);
//        canvas.restore();
//    }


    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end,
                     float x, int top, int y, int bottom, Paint paint) {

        Paint.FontMetricsInt fm = paint.getFontMetricsInt();
        Drawable drawable = getDrawable();
        int transY = (y + fm.descent + y + fm.ascent) / 2
                - drawable.getBounds().bottom / 2;
        canvas.save();
        canvas.translate(x, transY);
        drawable.draw(canvas);
        canvas.restore();
    }

}
