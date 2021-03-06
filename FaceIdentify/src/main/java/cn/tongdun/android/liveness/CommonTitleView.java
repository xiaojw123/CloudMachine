package cn.tongdun.android.liveness;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import cc.gnaixx.sample_liveness.R;


/**
 * Created by xiaojw on 2017/6/1.
 */

public class CommonTitleView extends FrameLayout implements View.OnClickListener {
    TextView rightTv;
    TextView titleTv;
    ImageView backImg;
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
        boolean showVisble=a.getBoolean(R.styleable.CommonTitleView_common_shodow_visible,true);
        boolean rightTextEanlbe = a.getBoolean(R.styleable.CommonTitleView_common_right_text_enable,true);
        int color = a.getColor(R.styleable.CommonTitleView_common_right_textcolor, getResources().getColor(R.color.cor8));
        View view = LayoutInflater.from(context).inflate(R.layout.common_title_view, null);
        int height;
        if (showVisble){
            height=(int) getResources().getDimension(R.dimen.dimen_size_49);
        }else{
            height= (int) getResources().getDimension(R.dimen.dimen_size_45);
            view.setBackground(getResources().getDrawable(R.drawable.bg_home_title));
        }
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
        addView(view);
        backImg = (ImageView) view.findViewById(R.id.common_titleview_back_img);
        titleTv = (TextView) view.findViewById(R.id.common_titleview_title);
        rightTv = (TextView) view.findViewById(R.id.common_titleview_right_tv);
        rightImg = (ImageView) view.findViewById(R.id.common_titleview_right_img);
        rightTv.setEnabled(rightTextEanlbe);
        rightTv.setTextColor(color);
        titleTv.setText(titleName);
        rightTv.setText(rightText);
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
    public void setRightImg(int resId){
        rightImg.setVisibility(VISIBLE);
        rightImg.setImageResource(resId);
    }

    public void setRightTextColor(int color) {
        rightTv.setTextColor(color);
    }
    public void setRighteTextEnalbe(boolean flag){
        rightTv.setEnabled(flag);
    }

    public void setTitleName(String name) {
        titleTv.setText(name);
    }


    public void setRightText(String text, OnClickListener listener) {
        rightTv.setText(text);
        if (listener != null) {
            rightTv.setOnClickListener(listener);
        }
    }
    public void setRightTextEnable(boolean enable){
        rightTv.setEnabled(enable);
    }
    public String getRightText(){
        return rightTv.getText().toString();
    }


    public void setRightClickListener(OnClickListener listener) {
        if (listener != null) {
            rightTv.setOnClickListener(listener);
        }
    }


    public void setRightText(String text) {
        rightTv.setText(text);
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


    @Override
    public void onClick(View v) {
                closePage(v);
    }

    private void closePage(View v) {
        Context context = v.getContext();
        if (context instanceof Activity) {
            ((Activity) context).finish();
        }
    }
}
