package com.cloudmachine.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.base.BaseActivity;
import com.cloudmachine.struc.ScanningOcdInfo;
import com.cloudmachine.struc.ScanningOcdInfoArray;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.ResV;
import com.cloudmachine.utils.mpchart.MyMarkerView;
import com.cloudmachine.utils.mpchart.MyYAxisValueFormatter;
import com.cloudmachine.utils.widgets.TitleView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.YAxis.YAxisLabelPosition;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;

public class OilWearActivity extends BaseActivity implements OnClickListener,Callback,
OnChartValueSelectedListener{

	private final static int sumDays = 30;
	private final static int[] typeColor = {0xff07c43f,0xffe9c915,0xff97b3e9,0xffe17a4d	};
	private View hours_use_layout,hours_cost_layout,day_use_layout,day_cost_layout;
	private int focus;
	private ScanningOcdInfoArray ocdArray;
	private ScanningOcdInfo[] ocdList;
	private TextView hours_use_text,hours_cost_text,day_use_text,day_cost_text;
	private int lastItem = 0;
	private String[] xD ;
	private double[] yD ;
	private TitleView title_layout;
	private BarChart barChart;
	private Typeface mTf;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mc_oilwear);
		getIntentData();
		initView();
		initData();
		changeFocus(0);
	}

	private void getIntentData(){
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
        	try{
        		ocdArray = (ScanningOcdInfoArray) bundle.getSerializable(Constants.P_OCDSERIALIZABLE);
        		ocdList = ocdArray.getOcdList();
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
		case R.id.hours_use_layout:
			changeFocus(0);
			break;
		case R.id.hours_cost_layout:
			changeFocus(1);
			break;
		case R.id.day_use_layout:
			changeFocus(2);
			break;
		case R.id.day_cost_layout:
			changeFocus(3);
			break;
		}
	}

	private void initView(){
		initTitleLayout();
		
		hours_use_layout = findViewById(R.id.hours_use_layout);
		hours_cost_layout = findViewById(R.id.hours_cost_layout);
		day_use_layout = findViewById(R.id.day_use_layout);
		day_cost_layout = findViewById(R.id.day_cost_layout);
		hours_use_layout.setOnClickListener(this);
		hours_cost_layout.setOnClickListener(this);
		day_use_layout.setOnClickListener(this);
		day_cost_layout.setOnClickListener(this);
		
		hours_use_text = (TextView)findViewById(R.id.hours_use_text);
		hours_cost_text = (TextView)findViewById(R.id.hours_cost_text);
		day_use_text = (TextView)findViewById(R.id.day_use_text);
		day_cost_text = (TextView)findViewById(R.id.day_cost_text);
		
		barChart = (BarChart) findViewById(R.id.chart1);
	}
	
	private void initTitleLayout(){
			
			title_layout = (TitleView)findViewById(R.id.title_layout);
			title_layout.setTitle("油耗");
			
			title_layout.setLeftImage(-1, new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					 finish(); 
				}
			});
		}
	private void initData(){
		if(null == ocdList)
			return;
		hours_use_text.setText(Constants.float2String(ocdList[lastItem].getHourOilConsume())+Constants.S_SCANNING_CONSUME);
		hours_cost_text.setText(Constants.float2String(ocdList[lastItem].getHourOilPay()));
		day_use_text.setText(Constants.float2String(ocdList[lastItem].getDayOilConsume())+Constants.S_SCANNING_CONSUME);
		day_cost_text.setText(Constants.float2String(ocdList[lastItem].getDayOilPay()));
//		int dan = Constants.getDateDays("2014-08-25","2014-08-24");
//		Constants.MyLog(dan+"");
	}
	
	private void changeFocus(int n){
		focus = n;
		addXY();
		if(null == xD || null == yD){
			return;
		}
		showBarChart();
		switch(n){
		case 0:
			hours_use_layout.setBackgroundResource(R.color.transparent);
			hours_cost_layout.setBackgroundResource(R.drawable.bg_btn_oilwear_tab);
			day_use_layout.setBackgroundResource(R.drawable.bg_btn_oilwear_tab);
			day_cost_layout.setBackgroundResource(R.drawable.bg_btn_oilwear_tab);
			break;
		case 1:
			hours_use_layout.setBackgroundResource(R.drawable.bg_btn_oilwear_tab);
			hours_cost_layout.setBackgroundResource(R.color.transparent);
			day_use_layout.setBackgroundResource(R.drawable.bg_btn_oilwear_tab);
			day_cost_layout.setBackgroundResource(R.drawable.bg_btn_oilwear_tab);
			break;
		case 2:
			hours_use_layout.setBackgroundResource(R.drawable.bg_btn_oilwear_tab);
			hours_cost_layout.setBackgroundResource(R.drawable.bg_btn_oilwear_tab);
			day_use_layout.setBackgroundResource(R.color.transparent);
			day_cost_layout.setBackgroundResource(R.drawable.bg_btn_oilwear_tab);
			break;
		case 3:
			hours_use_layout.setBackgroundResource(R.drawable.bg_btn_oilwear_tab);
			hours_cost_layout.setBackgroundResource(R.drawable.bg_btn_oilwear_tab);
			day_use_layout.setBackgroundResource(R.drawable.bg_btn_oilwear_tab);
			day_cost_layout.setBackgroundResource(R.color.transparent);
			break;
		}
	}
	
	private void addXY(){
		if(null == ocdList || ocdList.length<1 )
			return;
		int len = ocdList.length;
		xD = new String[len];
		yD = null;
		len = len>=sumDays?sumDays:len;
		for(int i=0; i<len; i++){
			if(null == ocdList[i])
				continue;
			xD[i] = ocdList[i].getCollectionDate();
//			int dayNum = sumDays-Constants.getDateDays(ocdList[lastItem].getCollectionDate(), ocdList[i].getCollectionDate());
//			xD = Constants.arrayCopyDouble(xD, dayNum);
			switch(focus){
			case 0:
				yD = Constants.arrayCopyDouble(yD, ocdList[i].getHourOilConsume());
				break;
			case 1:
				yD = Constants.arrayCopyDouble(yD, ocdList[i].getHourOilPay());
				break;
			case 2:
				yD = Constants.arrayCopyDouble(yD, ocdList[i].getDayOilConsume());
				break;
			case 3:
				yD = Constants.arrayCopyDouble(yD, ocdList[i].getDayOilPay());
				break;
			}
			
			
		}
	}
	private void showBarChart(){
		barChart.clear();
		mTf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
		setData(focus);
		
		MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view,barChart.getXAxis().getValues());
		barChart.setMarkerView(mv);
		// 如果没有数据的时候，会显示这个，类似ListView的EmptyView
        barChart.setNoDataTextDescription(ResV.getString(R.string.mpchart_not_date));
        
		barChart.setOnChartValueSelectedListener(this);

		barChart.setDrawBarShadow(false);
		barChart.setDrawValueAboveBar(true);

		barChart.setDescription("");

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
		barChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
		barChart.setPinchZoom(false);

        // draw shadows for each bar that show the maximum value
        // mChart.setDrawBarShadow(true);

        // mChart.setDrawXLabels(false);

		barChart.setDrawGridBackground(false);
        // mChart.setDrawYLabels(false);

        

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxisPosition.BOTTOM);
        xAxis.setTypeface(mTf);
        xAxis.setDrawGridLines(true);
        xAxis.setSpaceBetweenLabels(2);
        
        YAxisValueFormatter custom;
        if(focus == 0 || focus == 2){
        	custom = new MyYAxisValueFormatter("L");
        }else{
        	custom = new MyYAxisValueFormatter("元");
        }
        

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setTypeface(mTf);
        leftAxis.setLabelCount(8, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setTypeface(mTf);
        rightAxis.setLabelCount(8, false);
        rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(15f);
//        rightAxis.setInverted(true);
        rightAxis.setEnabled(false);

        Legend l = barChart.getLegend();
        l.setPosition(LegendPosition.BELOW_CHART_LEFT);
        l.setForm(LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
//         l.setExtra(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
//         "def", "ghj", "ikl", "mno" });
//         l.setCustom(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
//         "def", "ghj", "ikl", "mno" });

        barChart.getLegend().setEnabled(false); 
      //设置动画
        barChart.setVisibleXRangeMaximum(7);  //一个界面显示多少个点，其他点可以通过滑动看到  
        barChart.setVisibleXRangeMinimum(7);  //一个界面最少显示多少个点，放大后最多 放大到 剩多少 个点  
        barChart.moveViewToX(2);
        barChart.animateXY(0,3000);
	}

	 private void setData(int type) {
//		 focus
		 if(null == xD || null == yD)
			 return;
		 int count = xD.length<=yD.length?xD.length:yD.length;
	        ArrayList<String> xVals = new ArrayList<String>();
	        
	        for (int i = 0; i < count; i++) {
	            xVals.add(xD[i]);
	        }

	        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

	        for (int i = 0; i < count; i++) {
	            yVals1.add(new BarEntry((float)yD[i], i));
	        }

	        BarDataSet set1 = new BarDataSet(yVals1, "");
	        set1.setBarSpacePercent(35f);
	        set1.setColor(typeColor[type]);

	        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
	        dataSets.add(set1);

	        BarData data = new BarData(xVals, dataSets);
	        data.setValueTextSize(10f);
	        data.setValueTypeface(mTf);

	        barChart.setData(data);
//	        barChart.
	    }
	@Override
	public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNothingSelected() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {

	}
}
