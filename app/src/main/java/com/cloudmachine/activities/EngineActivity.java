package com.cloudmachine.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;

import com.cloudmachine.R;
import com.cloudmachine.base.BaseActivity;
import com.cloudmachine.struc.MeterData;
import com.cloudmachine.struc.ScanningEdInfo;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.Utils;
import com.cloudmachine.utils.widgets.AdviseTextView;
import com.cloudmachine.utils.widgets.MeterView;
import com.cloudmachine.utils.widgets.TemperatureView;
import com.cloudmachine.utils.widgets.ThreeColorView;
import com.cloudmachine.utils.widgets.TitleView;

public class EngineActivity extends BaseActivity implements OnClickListener,Callback{

	private int deviceId;
	private ThreeColorView three_layout1,three_layout2;
	private TemperatureView temperatureView1;
	private MeterView meterView;
	private AdviseTextView adviseText;
	private ScanningEdInfo edInfo;
	private float airTemper;//进风口温度
	private float oilPress;//机油压力
	private int workstatus;//工作状态
	private float waterTemper;//冷却水温度
	private float shockDegree;//振动量
	private String suggestions;//建议
	private int waterTemperStatus;
	private int oilPressStatus;
	private TitleView title_layout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mc_engine);
		getIntentData();
		initView();
	}

	private void getIntentData(){
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
        	try{
        		edInfo = (ScanningEdInfo) bundle.getSerializable(Constants.P_EDSERIALIZABLE);
        		if(edInfo != null){
        			airTemper = edInfo.getAirTemper();
        			oilPress = edInfo.getOilPress();
        			workstatus = edInfo.getWorkstatus();
        			waterTemper = edInfo.getWaterTemper();
        			shockDegree = edInfo.getShockDegree();
            		suggestions = edInfo.getSuggestions();
            		waterTemperStatus = edInfo.getWaterTemperStatus();
            		oilPressStatus = edInfo.getOilPressStatus();
        		}
        	}catch(Exception e){
        		Constants.MyLog(e.getMessage());
        	}
        }
    }
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		}
	}

	private void initView(){
		initTitleLayout();
		three_layout1 = (ThreeColorView)findViewById(R.id.three_layout1);
		three_layout1.setText("0-12", "13-71", "72-180");
		three_layout1.setTitle("怠速状态(mm/s)");
		
		three_layout2 = (ThreeColorView)findViewById(R.id.three_layout2);
		three_layout2.setText("0-12", "13-71", "72-180");
		three_layout2.setTitle("工作状态(mm/s)");
		
		
		if(workstatus == 0){
			three_layout1.setState(initShockDegree(shockDegree));
			three_layout2.setState(-1);
		}else{//1
			three_layout1.setState(-1);
			three_layout2.setState(initShockDegree(shockDegree));
		}
		
		meterView = (MeterView)findViewById(R.id.meterView);
		initMeterView();
		
		temperatureView1 = (TemperatureView)findViewById(R.id.temperature_layout1);
		temperatureView1.setTitle("冷却水温度");
		temperatureView1.setState(waterTemperStatus);
		temperatureView1.setTemperature((int)waterTemper,100);
		
		adviseText  = (AdviseTextView)findViewById(R.id.adviseText);
		adviseText.setContent(suggestions);
	}
	private void initTitleLayout(){
		
		title_layout = (TitleView)findViewById(R.id.title_layout);
		title_layout.setTitle("发动机");
		
		title_layout.setLeftImage(-1, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 finish(); 
			}
		});
	}
	
	private void initMeterView(){
		meterView.setPointerNum(1);
		MeterData data = new MeterData();
		data.setPointerNum(oilPress);
		data.setPointerSum(6);
		data.setTitle("机油压力");
		data.setMeterBg(R.drawable.mc_engine_meter_icon);
		data.setState(oilPressStatus);
		meterView.setOneData(data);
		Utils.initMeterImageHW(meterView.getMeterLayout());
		meterView.setAnimation();
	}
	
	private int initShockDegree(float sd){
		int rid;
		if(sd<=12){
			rid = 0;
		}else if(sd>12&&sd<=71){
			rid = 1;
		}else{
			rid = 2;
		}
		return rid;
	}
	
}
