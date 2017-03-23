package com.cloudmachine.utils.widgets;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.struc.ScreenInfo;
import com.cloudmachine.utils.Utils;


public class TemperatureView extends RelativeLayout {
	private Context context;
    private RelativeLayout mContainer;
    private TextView t_title,t_state,t_color,t_text;

    public TemperatureView(Context context) {
        super(context);
        initView(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public TemperatureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
    	this.context = context;
        // 初始情况，设置下拉刷新view高度为0
    	LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_temperature, this);
//    	RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//        mContainer = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.view_temperature, null);
//        addView(mContainer, lp);

        t_title = (TextView) findViewById(R.id.t_title);
        t_state = (TextView) findViewById(R.id.t_state);
        t_color = (TextView) findViewById(R.id.t_color);
        t_text = (TextView) findViewById(R.id.t_text);
    }
    public void setTitle(String title){
    	t_title.setText(title+":");
    }
    public void setState(int state){
    	switch(state){
    	case 0:
    		t_state.setText("偏低");
    		t_state.setTextColor(context.getResources().getColor(R.color.mc_state1));
    		t_color.setBackgroundResource(R.drawable.radius_button_left_tp1);
    		break;
    	case 1:
    		t_state.setText("正常");
    		t_state.setTextColor(context.getResources().getColor(R.color.mc_state0));
    		t_color.setBackgroundResource(R.drawable.radius_button_left_tp0);
    		break;
    	case 2:
    		t_state.setText("偏高");
    		t_state.setTextColor(context.getResources().getColor(R.color.mc_state2));
    		t_color.setBackgroundResource(R.drawable.radius_button_left_tp2);
    		break;
    	default:
    		t_state.setText("异常");
    		t_state.setTextColor(context.getResources().getColor(R.color.mc_state2));
    		t_color.setBackgroundResource(R.drawable.radius_button_left_tp2);
			break;
    	}
    }
    public void setTemperature(int n, int sum){
    	t_text.setText(n+"℃");
//    	int px1 = Utils.dip2px(290);
    	int px = ScreenInfo.screen_width-Utils.dip2px(30);
    	if(n>sum)
    		n = sum;
    	float w = (float)n/(float)sum*px;
    	t_color.setLayoutParams(new LayoutParams((int)w, LayoutParams.FILL_PARENT));
    }


}
