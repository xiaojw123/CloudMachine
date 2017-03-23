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
import com.cloudmachine.struc.ScanningHsdInfo;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.Utils;
import com.cloudmachine.utils.widgets.AdviseTextView;
import com.cloudmachine.utils.widgets.MeterView;
import com.cloudmachine.utils.widgets.TemperatureView;
import com.cloudmachine.utils.widgets.TitleView;

public class HydraulicActivity extends BaseActivity implements OnClickListener,Callback{

	private int deviceId;
	private TemperatureView temperatureView1,temperatureView2;
	private MeterView meterView1,meterView2,meterView3;
	private AdviseTextView adviseText;
	private ScanningHsdInfo hsdInfo;
	private float pumpPress1;//泵压
	private float pumpPress2;//泵压
	private float pumpOilTemper;//主泵泄油口温度
	private float oilTemper;//回油温度
	private float leaderPress;//先导压力
	private float backOilPress;//回油压力
	private String suggestions;//建议
	private int pumpPress1Status;
	private int pumpPress2Status;
	private int pumpOilTemperStatus;
	private int oilTemperStatus;
	private int backOilPressStatus;
	private int leaderPressStatus;
	private TitleView title_layout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mc_hydraulic);
		getIntentData();
		initView();
	}

	private void getIntentData(){
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
        	try{
        		hsdInfo = (ScanningHsdInfo) bundle.getSerializable(Constants.P_HSDSERIALIZABLE);
        		if(hsdInfo != null){
        			pumpPress1 = hsdInfo.getPumpPress1();
            		pumpPress2 = hsdInfo.getPumpPress2();
            		pumpOilTemper = hsdInfo.getPumpOilTemper();
            		oilTemper = hsdInfo.getOilTemper();
            		leaderPress = hsdInfo.getLeaderPress();
            		backOilPress = hsdInfo.getBackOilPress();
            		suggestions = hsdInfo.getSuggestions();
            		pumpPress1Status = hsdInfo.getPumpPress1Status();
            		pumpPress2Status = hsdInfo.getPumpPress2Status();
            		pumpOilTemperStatus = hsdInfo.getPumpOilTemperStatus();
            		oilTemperStatus = hsdInfo.getOilTemperStatus();
            		backOilPressStatus = hsdInfo.getBackOilPressStatus();
            		leaderPressStatus = hsdInfo.getLeaderPressStatus();
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
		/*int len = rotateAnimation.length;
		for(int i=0; i<len; i++){
			if(null != rotateAnimation[i]){
				rotateAnimation[i].cancel();
			}
		}*/
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
		
		temperatureView1 = (TemperatureView)findViewById(R.id.temperature_layout1);
		temperatureView1.setTitle("回油温度");
		temperatureView1.setState(oilTemperStatus);
		temperatureView1.setTemperature((int)oilTemper,100);
		
		temperatureView2 = (TemperatureView)findViewById(R.id.temperature_layout2);
		temperatureView2.setTitle("主泵泄油口温度");
		temperatureView2.setState(pumpOilTemperStatus);
		temperatureView2.setTemperature((int)pumpOilTemper,100);
		
		meterView1 = (MeterView)findViewById(R.id.meterView1);
		meterView2 = (MeterView)findViewById(R.id.meterView2);
		meterView3 = (MeterView)findViewById(R.id.meterView3);
		initMeterView();
		
		adviseText  = (AdviseTextView)findViewById(R.id.adviseText);
		adviseText.setContent(suggestions);
	}
	private void initTitleLayout(){
		
		title_layout = (TitleView)findViewById(R.id.title_layout);
		title_layout.setTitle("液压系统");
		
		title_layout.setLeftImage(-1, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 finish(); 
			}
		});
	}
	private void initMeterView(){
		meterView1.setPointerNum(1);
		MeterData data = new MeterData();
		data.setPointerNum(leaderPress);
		data.setPointerSum(200);
		data.setTitle("先导压力");
		data.setMeterBg(R.drawable.mc_engine_meter_icon1);
		data.setState(leaderPressStatus);
		meterView1.setOneData(data);
		Utils.initMeterImageHW(meterView1.getMeterLayout());
		meterView1.setAnimation();
		
		meterView2.setPointerNum(2);
		data = new MeterData();
		data.setPointerNum(pumpPress1);
		data.setPointerSum(600);
		data.setTitle("主泵压力");
		data.setMeterBg(R.drawable.mc_engine_meter_icon2);
		data.setState(pumpPress1Status);
		meterView2.setOneData(data);
		
		data = new MeterData();
		data.setPointerNum(pumpPress2);
		data.setPointerSum(600);
		data.setTitle("主泵压力");
		data.setMeterBg(R.drawable.mc_engine_meter_icon2);
		data.setState(pumpPress2Status);
		meterView2.setTwoData(data);
		Utils.initMeterImageHW(meterView2.getMeterLayout());
		meterView2.setAnimation();
		
		meterView3.setPointerNum(1);
		data = new MeterData();
		data.setPointerNum(backOilPress);
		data.setPointerSum(60);
		data.setTitle("回油压力");
		data.setMeterBg(R.drawable.mc_engine_meter_icon3);
		data.setState(backOilPressStatus);
		meterView3.setOneData(data);
		Utils.initMeterImageHW(meterView3.getMeterLayout());
		meterView3.setAnimation();
	}
	
}
