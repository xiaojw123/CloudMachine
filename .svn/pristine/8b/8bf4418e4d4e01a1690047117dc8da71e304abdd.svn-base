package com.cloudmachine.utils.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.struc.MeterData;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.Utils;


/**
 * @author Maxwin
 * @file XHeaderView.java
 * @create Apr 18, 2012 5:22:27 PM
 * @description XListView's header
 */
public class MeterView extends RelativeLayout {
	private Context context;
    private RelativeLayout mContainer;
    private  View parent ;
    private ImageView meter_bg,meter_pointer1,meter_pointer2;
    
    private View meter_layout,text_layout2;
    private TextView meter1_text,meter1_text_2,meter1_text2,meter1_text2_2
    ,meter1_text2_num,meter1_text2_num_2,meter1_text3,meter1_text3_2,
    meter1_com,meter1_com_2;
    
    private int pointerNum = 1;
    private MeterData oneData,twoData;
    public MeterView(Context context) {
        super(context);
        initView(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public MeterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
    	this.context = context;
        // 初始情况，设置下拉刷新view高度为0
//    	LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        inflater.inflate(R.layout.three_color, this);
        mContainer = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.view_meter, null);
        meter_layout = mContainer.findViewById(R.id.meter_layout);
        text_layout2 = mContainer.findViewById(R.id.text_layout2);
        meter_bg = (ImageView)mContainer.findViewById(R.id.meter_bg);
        meter_pointer1 = (ImageView)mContainer.findViewById(R.id.meter_pointer1);
        meter_pointer2 = (ImageView)mContainer.findViewById(R.id.meter_pointer2);
        meter1_text = (TextView)mContainer.findViewById(R.id.meter1_text);
        meter1_com = (TextView)mContainer.findViewById(R.id.meter1_com);
        meter1_text_2 = (TextView)mContainer.findViewById(R.id.meter1_text_2);
        meter1_com_2 = (TextView)mContainer.findViewById(R.id.meter1_com_2);
        meter1_text2 = (TextView)mContainer.findViewById(R.id.meter1_text2);
        meter1_text2_2 = (TextView)mContainer.findViewById(R.id.meter1_text2_2);
        meter1_text2_num = (TextView)mContainer.findViewById(R.id.meter1_text2_num);
        meter1_text2_num_2 = (TextView)mContainer.findViewById(R.id.meter1_text2_num_2);
        meter1_text3 = (TextView)mContainer.findViewById(R.id.meter1_text3);
        meter1_text3_2 = (TextView)mContainer.findViewById(R.id.meter1_text3_2);
        
       
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView(mContainer, lp);

       
    }
    public void setPointerNum(int n){
    	pointerNum = n;
    	if(pointerNum == 1){
    		meter_pointer2.setVisibility(View.GONE);
    		text_layout2.setVisibility(View.GONE);
    		meter1_text2_num.setVisibility(View.GONE);
    		meter1_text2.setTextAppearance(context, R.style.TextView_Meter_Text2);
    	}else{
    		meter_pointer2.setVisibility(View.VISIBLE);
    		text_layout2.setVisibility(View.VISIBLE);
    		meter1_text2_num.setVisibility(View.VISIBLE);
    		meter1_text2.setTextAppearance(context, R.style.TextView_Meter_Text2_2);
    	}
    }

    public void setOneData(MeterData data){//必须调用
    	oneData = data;
    	meter_bg.setImageResource(data.getMeterBg());
    	if(data.getPointerNum()>data.getPointerSum()){
    		data.setPointerNum(data.getPointerSum());
    	}
    	meter1_text.setText(Constants.float2String(data.getPointerNum()));
    	meter1_text2.setText(data.getTitle());
    	setState(meter1_text3,data.getState());
    }
    public void setTwoData(MeterData data){
    	twoData = data;
    	meter1_text_2.setText(Constants.float2String(data.getPointerNum()));
    	meter1_text2_2.setText(data.getTitle());
    	setState(meter1_text3_2,data.getState());
    }
    public void setCom(String com){
    	meter1_com.setText(com);
    	meter1_com_2.setText(com);
    }
    public void setState(TextView tv,int state) {
    	switch(state){
    	case -1:
    		tv.setText("");
    		break;
    	case 0:
    		tv.setText("偏低");
    		tv.setTextColor(context.getResources().getColor(R.color.mc_state1));
    		break;
    	case 1:
    		tv.setText("正常");
    		tv.setTextColor(context.getResources().getColor(R.color.mc_state0));
    		break;
    	case 2:
    		tv.setText("偏高");
    		tv.setTextColor(context.getResources().getColor(R.color.mc_state2));
    		break;
    	default:
    		tv.setText("异常");
    		tv.setTextColor(context.getResources().getColor(R.color.mc_state2));
			break;
    	}
    }
    public void setMeterBg(int rId){
    	
    }
    public void setAnimation(){
    	RotateAnimation rotateAnimation;
    	if(oneData != null){
    		rotateAnimation =new RotateAnimation(0f,Utils.countAngle(oneData.getPointerNum(), oneData.getPointerSum()),Animation.RELATIVE_TO_SELF, 
					0.5f,Animation.RELATIVE_TO_SELF,0.5f); 
			rotateAnimation.setDuration(Utils.countDuration(oneData.getPointerNum(), oneData.getPointerSum()));//设置动画持续时间
			rotateAnimation.setFillAfter(true);//动画执行完后是否停留在执行完的状态 
			meter_pointer1.setAnimation(rotateAnimation); 
			/** 开始动画 */ 
			rotateAnimation.startNow(); 
    	}
    	if(twoData != null){
    		rotateAnimation =new RotateAnimation(0f,Utils.countAngle(twoData.getPointerNum(), twoData.getPointerSum()),Animation.RELATIVE_TO_SELF, 
					0.5f,Animation.RELATIVE_TO_SELF,0.5f); 
			rotateAnimation.setDuration(Utils.countDuration(twoData.getPointerNum(), twoData.getPointerSum()));//设置动画持续时间
			rotateAnimation.setFillAfter(true);//动画执行完后是否停留在执行完的状态 
			meter_pointer2.setAnimation(rotateAnimation); 
			/** 开始动画 */ 
			rotateAnimation.startNow(); 
    	}
    }
    public MeterData getOneData(){
    	return oneData;
    }
    public MeterData getTwoData(){
    	return twoData;
    }
    public int getPointerNum(){
    	return pointerNum;
    }
    public View getMeterLayout(){
    	return meter_layout;
    }
    
    

}
