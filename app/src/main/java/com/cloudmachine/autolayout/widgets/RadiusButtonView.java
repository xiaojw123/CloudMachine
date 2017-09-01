package com.cloudmachine.autolayout.widgets;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.cloudmachine.autolayout.AutoRelativeLayout;
import com.cloudmachine.autolayout.utils.AutoUtils;
import com.cloudmachine.R;
import com.cloudmachine.chart.utils.AppLog;


public class RadiusButtonView extends AutoRelativeLayout {
	private Context context;
	private View radius_button_layout;
	private TextView radius_button_text;
	private int strokeWidth = (int)getResources().getDimension(R.dimen.radius_button_strokeWidth); // 0.5dp 边框宽度
    private int roundRadius = (int)getResources().getDimension(R.dimen.radius_button_roundRadius);;// 5dp 圆角半径
    private int strokeColor = getResources().getColor(R.color.public_blue_bg);//边框颜色
    private int fillColor_nm = getResources().getColor(R.color.public_white);//内部填充颜色
    private int fillColor_dw = getResources().getColor(R.color.radius_button_fill_white_dw);//内部填充颜色  彩色33ffffff=20%  灰白19000000=10%
    private int textColor = getResources().getColor(R.color.public_blue_bg);
    private float textSize = getResources().getDimension(R.dimen.radius_button_text_size);
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
        inflater.inflate(R.layout.view_radius_button_auto, this);
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
//    	AutoUtils.autoTextSize(radius_button_text, (int)sizef);
        AppLog.print("setTextSize sizef____"+sizef);
    	radius_button_text.setTextSize(TypedValue.COMPLEX_UNIT_PX,sizef);
//    	radius_button_text.setTextSize(TypedValue.COMPLEX_UNIT_PX,AutoUtils.getPercentHeightSizeBigger((int)sizef));
    }
    public void setColor(int strokeColor, int fillColor_nm, int fillColor_dw){
    	this.strokeColor = strokeColor;
    	this.fillColor_nm = fillColor_nm;
    	this.fillColor_dw = fillColor_dw;
    }
    public void setStrokeWidth(float strokeWidthDip){
    	this.strokeWidth = AutoUtils.getPercentHeightSizeBigger((int)strokeWidthDip);
    }
    public void setRoundRadius(float roundRadiusDip){
    	this.roundRadius = AutoUtils.getPercentHeightSizeBigger((int)roundRadiusDip);
    }
    public void setOnClickListener(OnClickListener listener){
    	radius_button_text.setOnClickListener(listener);
    }
    
    public void inToButton(){
    	
    	
    	gd_nm.setColor(fillColor_nm);
    	gd_nm.setCornerRadius(roundRadius);
    	gd_nm.setStroke(AutoUtils.getPercentHeightSizeBigger((int)strokeWidth),
    			strokeColor);
    	
    	gd_dw.setColor(fillColor_dw);
    	gd_dw.setCornerRadius(roundRadius);
    	gd_dw.setStroke(AutoUtils.getPercentHeightSizeBigger((int)strokeWidth),
    			strokeColor);
    	
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
