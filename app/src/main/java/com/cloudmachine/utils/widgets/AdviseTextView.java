package com.cloudmachine.utils.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudmachine.R;


public class AdviseTextView extends LinearLayout {
    private Context context;
	private LinearLayout mContainer;
    private TextView a_title,a_content;

    public AdviseTextView(Context context) {
        super(context);
        initView(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public AdviseTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        this.context = context;
    	// 初始情况，设置下拉刷新view高度为0
    	LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_advise_text, this);
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//        mContainer = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.view_advise_text, null);
//        addView(mContainer, lp);

        a_title = (TextView) findViewById(R.id.a_title);
        a_content = (TextView) findViewById(R.id.a_content);
    }
    public void setTitle(String title){
    	a_title.setText(title);
    }
    public void setContent(String content){
    	a_content.setText(content);
    }

   

}
