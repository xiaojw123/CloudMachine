package com.cloudmachine.utils.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudmachine.R;


/**
 * @author Maxwin
 * @file XHeaderView.java
 * @create Apr 18, 2012 5:22:27 PM
 * @description XListView's header
 */
public class ThreeColorView extends LinearLayout {
    private Context context;
	private LinearLayout mContainer;
    private TextView t_title,t_state,text1,text2,text3;

    public ThreeColorView(Context context) {
        super(context);
        initView(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public ThreeColorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        this.context = context;
    	// 初始情况，设置下拉刷新view高度为0
//    	LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        inflater.inflate(R.layout.three_color, this);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mContainer = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.three_color, null);
        addView(mContainer, lp);

        t_title = (TextView) findViewById(R.id.t_title);
        t_state = (TextView) findViewById(R.id.t_state);
        text1 = (TextView) findViewById(R.id.text1);
        text2 = (TextView) findViewById(R.id.text2);
        text3 = (TextView) findViewById(R.id.text3);
    }
    public void setText(String t1,String t2,String t3){
    	text1.setText(t1);
    	text2.setText(t2);
    	text3.setText(t3);
    }
    public void setTitle(String title){
    	t_title.setText(title+":");
    }

    public void setState(int state) {
    	setThreeColor(state);
    	switch(state){
    	case 0:
    		t_state.setText("很好");
    		t_state.setTextColor(context.getResources().getColor(R.color.mc_state0));
    		break;
    	case 1:
    		t_state.setText("正常");
    		t_state.setTextColor(context.getResources().getColor(R.color.mc_state0));
    		break;
    	case 2:
    		t_state.setText("偏高");
    		t_state.setTextColor(context.getResources().getColor(R.color.mc_state2));
    		break;
    	default:
    		t_state.setText("");
    		t_state.setTextColor(context.getResources().getColor(R.color.mc_state2));
			break;
    	}
        

    }
    
    private void setThreeColor(int state){
    	switch (state) {
        case 0:
        	text1.setBackgroundResource(R.drawable.radius_button_left_green);
        	text2.setBackgroundResource(R.color.mc_grey);
        	text3.setBackgroundResource(R.drawable.radius_button_right_grey);
            break;
        case 1:
        	text1.setBackgroundResource(R.drawable.radius_button_left_grey);
        	text2.setBackgroundResource(R.color.mc_green);
        	text3.setBackgroundResource(R.drawable.radius_button_right_grey);
            break;
        case 2:
        	text1.setBackgroundResource(R.drawable.radius_button_left_grey);
        	text2.setBackgroundResource(R.color.mc_grey);
        	text3.setBackgroundResource(R.drawable.radius_button_right_threecolor2);
            break;
        default:
        	text1.setBackgroundResource(R.drawable.radius_button_left_grey);
        	text2.setBackgroundResource(R.color.mc_grey);
        	text3.setBackgroundResource(R.drawable.radius_button_right_grey);
        	break;
    }
    }

}
