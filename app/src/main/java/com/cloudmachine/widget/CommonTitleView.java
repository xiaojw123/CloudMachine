package com.cloudmachine.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cloudmachine.R;
import com.cloudmachine.chart.utils.AppLog;
import com.cloudmachine.ui.home.contract.ExtrContract;

/**
 * Created by xiaojw on 2017/6/1.
 */

public class CommonTitleView extends FrameLayout implements View.OnClickListener {
    TextView rightTv;
    TextView titleTv;
    ImageView backImg;
    ImageView closeImg;
    ImageView rightImg;

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
        boolean showVisble = a.getBoolean(R.styleable.CommonTitleView_common_shodow_visible, true);
        boolean rightTextEanlbe = a.getBoolean(R.styleable.CommonTitleView_common_right_text_enable, true);
        int color = a.getColor(R.styleable.CommonTitleView_common_right_textcolor, getResources().getColor(R.color.cor8));
        View view = LayoutInflater.from(context).inflate(R.layout.common_title_view, null);
        int height;
        if (showVisble) {
            height = (int) getResources().getDimension(R.dimen.dimen_size_49);
        } else {
            height = (int) getResources().getDimension(R.dimen.dimen_size_45);
            view.setBackground(getResources().getDrawable(R.drawable.bg_home_title));
        }
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
        addView(view);
        closeImg = (ImageView) view.findViewById(R.id.common_titleview_close);
        backImg = (ImageView) view.findViewById(R.id.common_titleview_back_img);
        titleTv = (TextView) view.findViewById(R.id.common_titleview_title);
        rightTv = (TextView) view.findViewById(R.id.common_titleview_right_tv);
        rightImg = (ImageView) view.findViewById(R.id.common_titleview_right_img);
        rightTv.setEnabled(rightTextEanlbe);
        rightTv.setTextColor(color);
        titleTv.setText(titleName);
        if (!TextUtils.isEmpty(rightText)) {
            rightTv.setVisibility(VISIBLE);
            rightTv.setText(rightText);
        } else {
            rightTv.setVisibility(GONE);
        }
        closeImg.setOnClickListener(this);
        backImg.setOnClickListener(this);
        a.recycle();
    }

    public void setLeftOnClickListener(OnClickListener listener) {
        if (listener != null) {
            backImg.setOnClickListener(listener);
        }
    }

    public void setRightImg(int resId, OnClickListener listener) {
        if (resId == 0) {
            rightImg.setVisibility(GONE);
            rightImg.setOnClickListener(null);
            return;
        }
        rightImg.setVisibility(VISIBLE);
        rightImg.setImageResource(resId);
        if (listener != null) {
            rightImg.setOnClickListener(listener);
        }


    }

    public void setRightImg(String imgUrl, OnClickListener listener) {
        if (TextUtils.isEmpty(imgUrl)) {
            rightImg.setVisibility(GONE);
            rightImg.setOnClickListener(null);
            return;
        }
        rightImg.setVisibility(VISIBLE);
        Glide.with(getContext()).load(imgUrl).into(rightImg);
        if (listener != null) {
            rightImg.setOnClickListener(listener);
        }


    }

    public void setRightImg(int resId) {
        rightImg.setVisibility(VISIBLE);
        rightImg.setImageResource(resId);
    }

    public void setRightTextColor(int color) {
        rightTv.setTextColor(color);
    }

    public void setRighteTextEnalbe(boolean flag) {
        rightTv.setEnabled(flag);
    }

    public void setTitleName(String name) {
        titleTv.setText(name);
    }


    public void setRightText(String text, OnClickListener listener) {
        if (!TextUtils.isEmpty(text)) {
            rightTv.setVisibility(VISIBLE);
            rightTv.setText(text);
            if (listener != null) {
                rightTv.setOnClickListener(listener);
            }
        } else {
            rightTv.setVisibility(GONE);
        }
    }

    public void setRightTextEnable(boolean enable) {
        rightTv.setEnabled(enable);
    }

    public String getRightText() {
        return rightTv.getText().toString();
    }


    public void setRightClickListener(OnClickListener listener) {
        if (listener != null) {
            rightTv.setOnClickListener(listener);
        }
    }


    public void setRightText(String text) {
        if (!TextUtils.isEmpty(text)){
            rightTv.setVisibility(VISIBLE);
            rightTv.setText(text);
        }else{
            rightTv.setVisibility(GONE);
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

    public void setLeftClickListener(OnClickListener listener) {
        if (listener != null) {
            backImg.setOnClickListener(listener);
        }
    }

    public void setCloseVisible(int visibility) {
        closeImg.setVisibility(visibility);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.common_titleview_back_img:
                closePage(v);
                break;
            case R.id.common_titleview_close:
                closePage(v);
                break;
        }
    }

    private void closePage(View v) {
        Context context = v.getContext();
        AppLog.print("context____" + context);
        if (context instanceof Activity) {
            ((Activity) context).finish();
        }
    }
}
