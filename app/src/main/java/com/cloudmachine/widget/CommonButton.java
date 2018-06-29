package com.cloudmachine.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by xiaojw on 2018/6/25.
 */

public class CommonButton extends Button {
    public CommonButton(Context context) {
        this(context,null);
    }

    public CommonButton(Context context, AttributeSet attrs) {
        super(context, attrs,0);
    }

    public CommonButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {



    }
}
