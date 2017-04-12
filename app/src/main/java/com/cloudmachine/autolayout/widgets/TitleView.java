package com.cloudmachine.autolayout.widgets;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudm.autolayout.AutoRelativeLayout;
import com.cloudmachine.R;


public class TitleView extends AutoRelativeLayout {
    private Context context;

    private TextView title_name;
    private ImageView title_left_button, title_right_button;
    private TextView title_left_text, title_right_text, title_right_text2;
    private String text, left_text, right_text;
    private Drawable left_drawable, right_drawable;
    private boolean rightTextEdit = true;
    private View left_layout, right_layout;

    public TitleView(Context context) {
        super(context);
        initView(context);
    }

    /**
     * @param context
     * @param attrs   remarks
     */
    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.title_view);
        text = a.getString(R.styleable.title_view_text);
        left_drawable = a.getDrawable(R.styleable.title_view_left_drawable);
        right_drawable = a.getDrawable(R.styleable.title_view_right_drawable);
        left_text = a.getString(R.styleable.title_view_left_text);
        right_text = a.getString(R.styleable.title_view_right_text);
        rightTextEdit = a.getBoolean(R.styleable.title_view_right_text_edit, true);
        initView(context);
    }

    private void initView(Context context) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_title_auto, this);

        title_name = (TextView) findViewById(R.id.title_name);
        title_left_text = (TextView) findViewById(R.id.title_left_text);
        title_right_text = (TextView) findViewById(R.id.title_right_text);
        title_right_text2 = (TextView) findViewById(R.id.title_right_text2);
        title_left_button = (ImageView) findViewById(R.id.title_left_button);
        title_right_button = (ImageView) findViewById(R.id.title_right_button);
        left_layout = findViewById(R.id.left_layout);
        right_layout = findViewById(R.id.right_layout);
        if (null != text) {
            setTitle(text);
        }
        if (null != left_text) {
            setLeftText(-1, left_text);
        } else if (null != left_drawable) {
            setLeftImage(left_drawable);
        }
        if (null != right_text) {
            setRightText(-1, right_text);
        } else if (null != right_drawable) {
            setRightImage(right_drawable);
        }

    }

    //设置标题文本
    public void setTitle(String title) {
        title_name.setText(title);

    }

    public void setLeftLayoutVisibility(int visibility) {
        left_layout.setVisibility(visibility);
    }

    //设置左边图片和点击事件
    public void setLeftImage(int srcId, OnClickListener listener) {
        if (srcId != -1)
            title_left_button.setBackgroundResource(srcId);
        if (listener != null) {
            left_layout.setOnClickListener(listener);
            title_left_button.setOnClickListener(listener);
            title_left_text.setOnClickListener(listener);
        }
//    	title_left_button.setVisibility(View.VISIBLE);
//    	title_left_text.setVisibility(View.GONE);
    }

    //设置左边的图片和文字的点击事件
    public void setLeftOnClickListener(OnClickListener listener) {
        if (listener != null) {
            left_layout.setOnClickListener(listener);
            title_left_button.setOnClickListener(listener);
            title_left_text.setOnClickListener(listener);
        }
    }

    //设置右边的图片和文字的点击事件
    public void setRightOnClickListener(OnClickListener listener) {
        if (listener != null) {
            right_layout.setOnClickListener(listener);
            title_right_button.setOnClickListener(listener);
            title_right_text.setOnClickListener(listener);
            title_right_text2.setOnClickListener(listener);
        }
    }

    //设置左边的图片，内部使用
    private void setLeftImage(Drawable drawable) {
//    	if(srcId != -1)
        title_left_button.setImageDrawable(drawable);
        title_left_button.setVisibility(View.VISIBLE);
        title_left_text.setVisibility(View.GONE);
    }

    //设置右边图片和点击事件
    public void setRightImage(int srcId, OnClickListener listener) {
        if (srcId != -1)
            title_right_button.setImageResource(srcId);
        if (listener != null) {
            right_layout.setOnClickListener(listener);
            title_right_button.setOnClickListener(listener);
        }
        title_right_button.setVisibility(View.VISIBLE);
        title_right_text.setVisibility(View.GONE);
    }

    public void setRightImageResource(int srcId) {
        if (srcId != -1)
            title_right_button.setImageResource(srcId);
    }

    //设置右边的图片，内部使用
    private void setRightImage(Drawable drawable) {
        title_right_button.setImageDrawable(drawable);
        title_right_button.setVisibility(View.VISIBLE);
        title_right_text.setVisibility(View.GONE);
    }

    //设置左边的文字、文字颜色、点击事件
    public void setLeftText(int textColorId, String text, OnClickListener listener) {
        if (textColorId != -1)
            title_left_text.setTextColor(textColorId);
        if (text != null)
            title_left_text.setText(text);
        if (listener != null) {
            title_left_text.setOnClickListener(listener);
            left_layout.setOnClickListener(listener);
        }
        title_left_text.setVisibility(View.VISIBLE);
        title_left_button.setVisibility(View.GONE);

//    	getResources().getDrawable(id)
    }

    //设置左边的文字和文字颜色，内部使用
    private void setLeftText(int textColorId, String text) {
        if (textColorId != -1)
            title_left_text.setTextColor(textColorId);
        if (text != null)
            title_left_text.setText(text);
        title_left_text.setVisibility(View.VISIBLE);
        title_left_button.setVisibility(View.GONE);

//    	getResources().getDrawable(id)
    }

    //设置右边的文字、文字颜色、点击事件
    public void setRightText(int textColorId, String text, OnClickListener listener) {
        if (textColorId != -1)
            title_right_text.setTextColor(textColorId);
        title_right_text2.setTextColor(textColorId);
        if (text != null) {
            title_right_text.setText(text);
            title_right_text2.setText(text);
        }
        if (listener != null) {
            title_right_text.setOnClickListener(listener);
            right_layout.setOnClickListener(listener);
        }
        title_right_text2.setVisibility(View.VISIBLE);
        title_right_text.setVisibility(View.GONE);
        title_right_button.setVisibility(View.GONE);
    }

    //设置右边的文字、文字颜色，内部使用
    public void setRightText(int textColorId, String text) {
        if (textColorId != -1)
            title_right_text.setTextColor(textColorId);
        if (text != null) {
            title_right_text.setText(text);
            title_right_text2.setText(text);
        }
        title_right_text2.setVisibility(View.VISIBLE);
        title_right_text.setVisibility(View.GONE);
        title_right_button.setVisibility(View.GONE);
        setRightTextEdit(rightTextEdit);
    }

    /* public void setLeftTextEdit(boolean b){
         if(b){

             title_left_text.setTextColor(getResources().getColor(R.drawable.bg_text_mc_title));
         }else{
             title_left_text.setTextColor(getResources().getColor(R.drawable.mc_title_dw));
         }
     }*/
    //设置右边是否可以被点击状态
    public void setRightTextEdit(boolean b) {
        rightTextEdit = b;
        if (b) {
            title_right_text2.setVisibility(View.GONE);
            title_right_text.setVisibility(View.VISIBLE);
//    		title_right_text.setTextColor(getResources().getColor(R.drawable.bg_text_mc_title));
        } else {
            title_right_text2.setVisibility(View.VISIBLE);
            title_right_text.setVisibility(View.GONE);
//    		title_right_text.setTextColor(getResources().getColor(R.drawable.mc_title_dw));
        }
    }

    public boolean getRightTextEdit() {
        return rightTextEdit;
    }

    public void setLeftGone() {
        left_layout.setVisibility(View.GONE);
    }
}
