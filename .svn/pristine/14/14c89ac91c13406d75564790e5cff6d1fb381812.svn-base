package com.cloudmachine.utils.widgets;

import com.cloudmachine.R;
import com.google.gson.TypeAdapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * @author shixionglu
 * 检测报告使用的view
 */
public class DetectRecordItemView extends LinearLayout{
	
	private Context mContext;
	private TextView detectItem;
	private TextView referenceRange;
	private TextView tvResult;
	private ImageView ivResult;
	private String sDetectItem;
	private String sRefrenceRange;
	private String sTvResult;
	private boolean bAbnormal;

	public DetectRecordItemView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public DetectRecordItemView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public DetectRecordItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.detectItem_view);
		sDetectItem = typedArray.getString(R.styleable.detectItem_view_detectItem);
		sRefrenceRange = typedArray.getString(R.styleable.detectItem_view_referenceRange);
		sTvResult = typedArray.getString(R.styleable.detectItem_view_tvResult);
		bAbnormal = typedArray.getBoolean(R.styleable.detectItem_view_abnormal, true);
		
		initView(context);
		initDatas();
		isAbnormal(bAbnormal);
		typedArray.recycle();
		
	}
	//检测数据是否异常
	private void isAbnormal(boolean bAbnormal) {
		if(bAbnormal){
			ivResult.setVisibility(View.VISIBLE);
		}else {
			ivResult.setVisibility(View.GONE);
		}
	}

	//初始化数据
	private void initDatas() {
		if(TextUtils.isEmpty(sDetectItem)){
			detectItem.setText(sDetectItem);
		}
		if(TextUtils.isEmpty(sRefrenceRange)){
			referenceRange.setText(sRefrenceRange);
		}
		if(TextUtils.isEmpty(sTvResult)){
			tvResult.setText(sTvResult);
		}
	}

	private void initView(Context context) {
		// TODO Auto-generated method stub
		this.mContext = context;
		LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflate.inflate(R.layout.detect_report_item_view,this);
		detectItem = (TextView) findViewById(R.id.tv_detect_item);
		referenceRange = (TextView) findViewById(R.id.tv_reference_range);
		tvResult = (TextView) findViewById(R.id.tv_result);
		ivResult = (ImageView) findViewById(R.id.iv_result);
		
	}
	
}
