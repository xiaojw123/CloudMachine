package com.cloudmachine.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.cloudmachine.R;
import com.github.mikephil.charting.utils.AppLog;

/**
 * Created by xiaojw on 2017/6/1.
 */

public class CommonTitleView extends FrameLayout implements View.OnClickListener {
    TextView rightTv;
    TextView titleTv;

    public CommonTitleView(@NonNull Context context) {
        this(context, null);
    }

    public CommonTitleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonTitleView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CommonTitleView);
        String titleName = a.getString(R.styleable.CommonTitleView_common_title_name);
        String rightText = a.getString(R.styleable.CommonTitleView_common_right_text);
        View view = LayoutInflater.from(context).inflate(R.layout.common_title_view, this);
        TextView backTv = (TextView) view.findViewById(R.id.common_titleview_back_tv);
        titleTv = (TextView) view.findViewById(R.id.common_titleview_title);
        rightTv = (TextView) view.findViewById(R.id.common_titleview_right_tv);
        titleTv.setText(titleName);
        rightTv.setText(rightText);
        backTv.setOnClickListener(this);
        a.recycle();
    }


    public void setTitleNmae(String name) {
        titleTv.setText(name);
    }

    public void setRightText(String text, OnClickListener listener) {
        rightTv.setText(text);
        if (listener != null) {
            rightTv.setOnClickListener(listener);
        }
    }

    public void setRightTextVisible(int visibility) {
        rightTv.setVisibility(visibility);
    }

    public void setRightText(OnClickListener listener) {
        if (listener != null) {
            rightTv.setOnClickListener(listener);
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.common_titleview_back_tv:
                Context context = v.getContext();
                AppLog.print("context____" + context);
                if (context instanceof Activity) {
                    ((Activity) context).finish();
                }
                break;
        }
    }
}
