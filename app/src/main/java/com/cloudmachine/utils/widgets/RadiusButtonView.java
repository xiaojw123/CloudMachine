package com.cloudmachine.utils.widgets;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.utils.Utils;


public class RadiusButtonView extends RelativeLayout {
	private Context context;
	private View radius_button_layout;
	private TextView radius_button_text;
	private int strokeWidth = Utils.dip2px(0.5f); // 0.5dp 边框宽度
    private int roundRadius = Utils.dip2px(5f);// 5dp 圆角半径
    private int strokeColor = Color.parseColor("#68a009");//边框颜色
    private int fillColor_nm = Color.parseColor("#72ba2d");//内部填充颜色
    private int fillColor_dw = Color.parseColor("#33ffffff");//内部填充颜色  彩色33ffffff=20%  灰白19000000=10%
    private int textColor = getResources().getColor(R.color.white);
    private float textSize = 0;
    private GradientDrawable gd_nm,gd_dw;
    private StateListDrawable bg_drawable = new StateListDrawable();

    public RadiusButtonView(Context context) {
        super(context);
        initView(context);
    }
    /**
     * @param context
     * @param attrs
     */
    public RadiusButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, 
        		R.styleable.radiusbut_view); 
        String text = a.getString(R.styleable.radiusbut_view_rbtn_text); 
        strokeColor = a.getColor(R.styleable.radiusbut_view_rbtn_strokeColor, strokeColor);		//设置自定义控件的时候，设置默认的值,(布局文件里面不设置属性，通过设置的默认值拿值) 
        fillColor_nm = a.getColor(R.styleable.radiusbut_view_rbtn_fillColor_nm, fillColor_nm); 
        fillColor_dw = a.getColor(R.styleable.radiusbut_view_rbtn_fillColor_dw, fillColor_dw); 
        textColor = a.getColor(R.styleable.radiusbut_view_rbtn_textColor, textColor); 
        textSize = a.getDimension(R.styleable.radiusbut_view_rbtn_textSize, textSize); 
        roundRadius = (int)a.getDimension(R.styleable.radiusbut_view_rbtn_roundRadius, roundRadius); 
        initView(context);
        setText(text);
        setColor(strokeColor,fillColor_nm,fillColor_dw);
        setTextColor(textColor);
        if(textSize>0)
        	setTextSize(textSize);
        inToButton();
    }

    private void initView(Context context) {
    	this.context = context;
    	LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_radius_button, this);
//    	RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//        mContainer = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.view_temperature, null);
//        addView(mContainer, lp);

        radius_button_layout = findViewById(R.id.radius_button_layout);
        radius_button_text = (TextView) findViewById(R.id.radius_button_text);
        
        gd_nm = new GradientDrawable();
        gd_dw = new GradientDrawable();
    }
    public void setText(String text){
    	radius_button_text.setText(text);
    }
    public void setTextColor(int textColor){
    	radius_button_text.setTextColor(textColor);
    }
    public void setTextSize(float sizef){
    	//TypedValue.COMPLEX_UNIT_SP 
    	//TypedValue.COMPLEX_UNIT_DIP
    	radius_button_text.setTextSize(TypedValue.COMPLEX_UNIT_PX,sizef);
    }
    public void setColor(int strokeColor, int fillColor_nm, int fillColor_dw){
    	this.strokeColor = strokeColor;
    	this.fillColor_nm = fillColor_nm;
    	this.fillColor_dw = fillColor_dw;
    }
    public void setStrokeWidth(Float strokeWidthDip){
    	this.strokeWidth = Utils.dip2px(strokeWidthDip);
    }
    public void setRoundRadius(Float roundRadiusDip){
    	this.roundRadius = Utils.dip2px(roundRadiusDip);
    }
    public void setOnClickListener(OnClickListener listener){
    	radius_button_text.setOnClickListener(listener);
    }


    public void inToButton(){
    	gd_nm.setColor(fillColor_nm);
    	gd_nm.setCornerRadius(roundRadius);
    	gd_nm.setStroke(strokeWidth, strokeColor);
    	
    	gd_dw.setColor(fillColor_dw);
    	gd_dw.setCornerRadius(roundRadius);
    	gd_dw.setStroke(strokeWidth, strokeColor);
    	
    	radius_button_layout.setBackgroundDrawable(gd_nm);
        //Non focused states
    	bg_drawable.addState(new int[]{-android.R.attr.state_focused, -android.R.attr.state_selected, -android.R.attr.state_pressed},
        		gd_nm);
//    	bg_drawable.addState(new int[]{-android.R.attr.state_focused, android.R.attr.state_selected, -android.R.attr.state_pressed},
//        		gd_dw);
        //Focused states
    	bg_drawable.addState(new int[]{android.R.attr.state_focused,-android.R.attr.state_selected, -android.R.attr.state_pressed},
        		gd_dw);
//    	bg_drawable.addState(new int[]{android.R.attr.state_focused,android.R.attr.state_selected, -android.R.attr.state_pressed},
//        		gd_dw);
        //Pressed
//    	bg_drawable.addState(new int[]{android.R.attr.state_selected, android.R.attr.state_pressed},
//        		gd_dw);
    	bg_drawable.addState(new int[]{android.R.attr.state_pressed},
        		gd_dw);
          
    	radius_button_text.setBackgroundDrawable(bg_drawable);
//        radius_button_text.setCompoundDrawablesWithIntrinsicBounds(null, bg_drawable, null, null);
    }
    

}
