package com.cloudmachine.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.autolayout.widgets.DynamicWave;
import com.cloudmachine.base.BaseActivity;
import com.cloudmachine.net.task.GetOilLevelListAsync;
import com.cloudmachine.struc.ScanningOilLevelInfo;
import com.cloudmachine.struc.ScanningOilLevelInfoArray;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.ResV;
import com.cloudmachine.utils.mpchart.DrawTimerTask;
import com.cloudmachine.utils.mpchart.MyMarkerView;
import com.cloudmachine.utils.widgets.RadiusButtonView;
import com.cloudmachine.utils.widgets.TitleView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Timer;


public class OilAmountActivity extends BaseActivity implements OnClickListener,Callback,
OnChartValueSelectedListener{

	private static final int ANIMATETIME = 2000;
	private Context mContext;
	private Handler mHandler;
	private int oilLave;
	private long deviceId;
	private String deviceName;
	private ScanningOilLevelInfo[] oilLeve;
	private ScanningOilLevelInfo lastLevel;
	private TitleView title_layout;
	private LineChart mChart;
	private Typeface mTf;
	private int[] VORDIPLOM_COLORS = new int[2];
	
	private DynamicWave arcView;
	private TextView oil_proportion,last_date_oil_text,oil_proportion_last;
	private Timer myTimer;
	private RelativeLayout add_chart_layout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mc_oilamount);
		this.mContext = this;
		mHandler = new Handler(this);
		getIntentData();
		initTitleLayout();
		initView();
		new GetOilLevelListAsync(deviceId,mContext,mHandler).execute();
	}
	
	private void getIntentData(){
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
        	try{
        		deviceId = bundle.getLong(Constants.P_DEVICEID);
        		deviceName = bundle.getString(Constants.P_DEVICENAME);
        		oilLave = bundle.getInt(Constants.P_OILLAVE);
        		
//        		oilLeve = ((ScanningOilLevelInfoArray)bundle.get(Constants.P_OILLEVE)).getOilLevelList();
//        		lastLevel = (ScanningOilLevelInfo)bundle.get(Constants.P_LASTLEVEL);
        	/*	int len = oilLeve.length;
        		String tt ="\n";
        		for(int i=0; i<len; i++){
        			tt+=oilLeve[i].getLevel()+"\n";
        		}
        		Constants.MyLog(tt);*/
        	}catch (Exception e){
        		
        	}
        }
	}
	
	private void initView(){
		
		add_chart_layout = (RelativeLayout)findViewById(R.id.add_chart_layout);
		
		oil_proportion = (TextView)findViewById(R.id.oil_proportion);
		if(oilLave == -1){
			oil_proportion.setText(ResV.getString(R.string.amount_text2));
		}else{
			if(oilLave<0)
				oilLave = 0;
			if(oilLave>100)
				oilLave = 100;
			oil_proportion.setText(oilLave+"%");
		}
		if(oilLave<0)
			oilLave = 0;
		if(oilLave>100)
			oilLave = 100;
		arcView = (DynamicWave)findViewById(R.id.arcView);
		arcView.setLave(oilLave);
		
		
		
		last_date_oil_text = (TextView)findViewById(R.id.last_date_oil_text);
		oil_proportion_last = (TextView)findViewById(R.id.oil_proportion_last);
		
		
		
		
	}
	private void setLastData(){
		if(null != lastLevel){
			last_date_oil_text.setText(lastLevel.getTime()+" 最后油位百分比");
			oil_proportion_last.setText(lastLevel.getLevel()+"%");
		}else{
			if(null != oilLeve && oilLeve.length>0){
				last_date_oil_text.setText(oilLeve[0].getTime()+" 最后油位百分比");
				oil_proportion_last.setText(oilLeve[0].getLevel()+"%");
			}else{
				last_date_oil_text.setText("机器未启动");
				oil_proportion_last.setText(ResV.getString(R.string.amount_text2));
			}
		}
	}
	private void initTitleLayout(){
		
		title_layout = (TitleView)findViewById(R.id.title_layout);
		title_layout.setTitle("油位");
		
		title_layout.setLeftImage(-1, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 finish(); 
			}
		});
	}
	private void initChart(){
		VORDIPLOM_COLORS[0] = getResources().getColor(R.color.oil_amount_proportion_last_text);
		VORDIPLOM_COLORS[1] = getResources().getColor(R.color.oil_amount_proportion_text);
		
		mChart = (LineChart) findViewById(R.id.linechart);
        mChart.setOnChartValueSelectedListener(this);
        mChart.setDrawGridBackground(false);
//        mChart.setViewPortOffsets(0, 20, 0, 0); //靠边
        // no description text
        mChart.setNoDataTextDescription("");
        mChart.setNoDataText("");
        mChart.setDescription("");
//        mChart.setDescriptionColor(Color.rgb(227, 135, 0)); 
//        mChart.setDescriptionPosition(40f,60f);  

        // enable value highlighting
        mChart.setHighlightEnabled(true);

        // enable touch gestures
        mChart.setTouchEnabled(false);

        // enable scaling and dragging
        mChart.setDragEnabled(false);
        mChart.setScaleEnabled(false);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(false);
        mTf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        // add data
        setData();
        // set an alternative background color
        // mChart.setBackgroundColor(Color.GRAY);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view,mChart.getXAxis().getValues());

        // set the marker to the chart
        mChart.setMarkerView(mv);

        // enable/disable highlight indicators (the lines that indicate the
        // highlighted Entry)
//        mChart.setHighlightEnabled(false);
        
        XAxis xl = mChart.getXAxis();
        xl.setAvoidFirstLastClipping(true);
        xl.setPosition(XAxisPosition.BOTTOM/*XAxisPosition.BOTTOM_INSIDE*/);
        xl.setDrawGridLines(false);
        xl.setAxisLineColor(getResources().getColor(R.color.main_bg));
//        xl.setLabelsToSkip(3); //定制X轴Label间隔。
        xl.setAvoidFirstLastClipping(true); //定制X轴起点和终点Label不能超出屏幕。
        xl.setTextColor(getResources().getColor(R.color.personFont));
//        YAxis leftAxis = mChart.getAxisLeft();
//        leftAxis.setInverted(true);
        
        YAxis y = mChart.getAxisLeft();
        y.setTypeface(mTf);
        y.setLabelCount(2, false);//第一个参数是Y轴坐标的个数，第二个参数是 是否不均匀分布，true是不均匀分布  
        y.setStartAtZero(true); //设置Y轴坐标是否从0开始  
        y.setAxisMaxValue(100);    //设置Y轴坐标最大为多少  
//        y.setTextColor(Color.WHITE);
        y.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART /*INSIDE_CHART*/);
        y.setDrawGridLines(true);
        y.setGridColor(getResources().getColor(R.color.main_bg));
        y.setDrawAxisLine(false);
//        y.setAxisLineColor(Color.WHITE);
        y.setLabelCount(5, false);
        y.setTextColor(getResources().getColor(R.color.personFont));
        
        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);

        

        // // restrain the maximum scale-out factor
        // mChart.setScaleMinima(3f, 3f);
        //
        // // center the view to a specific position inside the chart
        // mChart.centerViewPort(10, 50);

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        // modify the legend ...
        // l.setPosition(LegendPosition.LEFT_OF_CHART);
        l.setForm(LegendForm.LINE);
        
        mChart.getLegend().setEnabled(false);  //隐藏色素线条
        
//        mChart.animateXY(2000, 2000);
        mChart.animateX(ANIMATETIME);

        // dont forget to refresh the drawing
        mChart.invalidate();
	}
	
	
	 private void setData() {
//		 focus
		 if(null == oilLeve )
			 return;
		 if(oilLeve.length<=0 && lastLevel == null)
			 return;
		 int count = oilLeve.length;
	        ArrayList<String> xVals = new ArrayList<String>();
	        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
	        int lastId = 0;
	        if(null != lastLevel){
	        	xVals.add("");
	        	yVals1.add(new BarEntry((float)lastLevel.getLevel(), 0));
	        	lastId = 1;
	        }
	        
	        for (int i = 0; i < count; i++) {
	            xVals.add(oilLeve[i].getTime());
	        }


	        for (int i = 0; i < count; i++) {
	            yVals1.add(new BarEntry((float)oilLeve[i].getLevel(), i+lastId));
	        }

	        LineDataSet set1 = new LineDataSet(yVals1, "");
//	        set1.setLineSpacePercent(35f);
	        set1.setColor(getResources().getColor(R.color.oil_amount_proportion_text));
	        set1.setDrawCircles(false); //不画圆点
	        //单独的点
	        ArrayList<Entry> yVals2 = new ArrayList<Entry>();
	        if(null != lastLevel){
	        	yVals2.add(new BarEntry(lastLevel.getLevel(), 0));
	        }else if(oilLeve.length>0){
	        	yVals2.add(new BarEntry((float)oilLeve[0].getLevel(), 0));
	        }
	        
	        if(count>1)
	        	yVals2.add(new BarEntry((float)oilLeve[count-1].getLevel(), count-1+lastId));
	        LineDataSet set2 = new LineDataSet(yVals2, "");
	        set2.setCircleColors(VORDIPLOM_COLORS);
	        set2.setColor(getResources().getColor(R.color.transparent));
	        set2.setDrawCircles(true); 
//	        yVals2.get(0).getData()
	        
	        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
	        dataSets.add(set1);
	        dataSets.add(set2);
	        LineData data = new LineData(xVals, dataSets);
	        data.setValueTextSize(10f);
	        data.setValueTypeface(mTf);
	        data.setDrawValues(false);
	        mChart.setData(data);
//	        barChart.
	        if(null != myTimer){
	        	myTimer.cancel();
	        	myTimer = null;
	        }
	        myTimer = new Timer(true);
			myTimer.schedule(new DrawTimerTask(mChart,
					yVals2.get(0),new Highlight(0,1),
					mHandler,Constants.HANDLER_CHART_MARKER_TIME),200); 
	    }
	
	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		switch(msg.what){
		case Constants.HANDLER_CHART_MARKER_TIME:
			float[] f = (float[])msg.obj;
			if(null != f){
				RadiusButtonView rbView = new RadiusButtonView(this);
				rbView.setText(String.valueOf((int)f[2]));
				rbView.setRoundRadius(15f);
				rbView.setTextSize(getResources().getDimension(R.dimen.chart_marker_text_size));
				rbView.setTextColor(getResources().getColor(R.color.white));
				rbView.setColor(VORDIPLOM_COLORS[0],
						VORDIPLOM_COLORS[0], 
						VORDIPLOM_COLORS[0]);
				rbView.inToButton();
				RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
						(int)getResources().getDimension(R.dimen.chart_marker_width),
						(int)getResources().getDimension(R.dimen.chart_marker_height)
						);
				if(f[2]>90){
					layoutParams.leftMargin=(int)f[0] + 5;
			        layoutParams.topMargin=(int)f[1] 
			        		+5;
				}else{
					layoutParams.leftMargin=(int)f[0] + 5;
			        layoutParams.topMargin=(int)f[1] 
			        		- (int)getResources().getDimension(R.dimen.chart_marker_height)
			        		-5;
				}
				
		        add_chart_layout.addView(rbView,layoutParams);
			}
			
			break;
		case Constants.HANDLE_GETOILLIST_SUCCESS:
			ScanningOilLevelInfoArray infoArray = (ScanningOilLevelInfoArray)msg.obj;
			oilLeve = infoArray.getOilLevel();
			lastLevel = infoArray.getLastLevel();
			initChart();
			setLastData();
			break;
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
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
	
	/*class DrawTimerTask extends TimerTask{
		
		Entry e;
		Highlight highlight;
		public DrawTimerTask(Entry e, Highlight highlight){
			this.e = e;
			this.highlight = highlight;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			 Message message = new Message();      
		      message.what = HANDLER_TIMER;   
		      float f[] = new float[3];
		      float p[] = mChart.getMarkerPosition(e, highlight);
		      f[0] = p[0];
		      f[1] = p[1];
		      f[2] = e.getVal();
		      message.obj = f;
		      mHandler.sendMessage(message); 
		}
		
	}*/

	@Override
	protected void onResume() {
		//MobclickAgent.onPageStart(UMengKey.time_machine_waterlevel);
		super.onResume();
	}

	@Override
	protected void onPause() {
		//MobclickAgent.onPageEnd(UMengKey.time_machine_waterlevel);
		super.onPause();
	}
}
