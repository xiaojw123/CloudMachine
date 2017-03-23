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
import com.cloudmachine.base.BaseActivity;
import com.cloudmachine.net.task.FaultDitailsAsync;
import com.cloudmachine.struc.FaultWarnDitailsInfo;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.mpchart.DrawTimerTask;
import com.cloudmachine.utils.mpchart.MyMarkerView;
import com.cloudmachine.utils.widgets.MeterView;
import com.cloudmachine.utils.widgets.RadiusButtonView;
import com.cloudmachine.utils.widgets.TitleView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
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

public class FaultDitailsListActivity extends BaseActivity implements OnClickListener,Callback,
OnChartValueSelectedListener{

	private static final int OUTMAX = 20;
	private Context mContext;
	private Handler mHandler;
	private TitleView title_layout;
	private FaultWarnDitailsInfo faultWarnDitailsInfo;
	private long deiverId;
	private long collectionDate;
	private int id;
	private TextView unit_textView;
	private String titleStr = "";
	private MeterView meterView1;
	private LineChart mChart;
//	private int lineColor = 0xff07c43f;
	private Typeface mTf;
	private RelativeLayout add_chart_layout;
	private Timer myTimer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fault_ditails_list);
		mContext = this;
		mHandler = new Handler(this);
		getIntentData();
		initView();
		initChart();
		new FaultDitailsAsync(collectionDate,deiverId,id,mContext,mHandler).execute();
	}

	private void getIntentData(){
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
        	try{
        		deiverId = bundle.getLong(Constants.P_DEVICEID);
        		collectionDate = bundle.getLong(Constants.P_DEVICEINFO_collectionDate);
        		id = bundle.getInt(Constants.P_ID);
        		titleStr = bundle.getString(Constants.P_DEVICENAME);
        	}catch(Exception e){
        		Constants.MyLog(e.getMessage());
        	}
        	
        }
    }

	@Override
	protected void onResume() {
		//MobclickAgent.onPageStart(UMengKey.time_machine_report_warning);
		super.onResume();
	}

	@Override
	protected void onPause() {
		//MobclickAgent.onPageEnd(UMengKey.time_machine_report_warning);
		super.onPause();
	}

	private void initView(){
		initTitleLayout();
		add_chart_layout = (RelativeLayout)findViewById(R.id.add_chart_layout);
		unit_textView = (TextView)findViewById(R.id.unit_textView);
	}
	private void initTitleLayout(){
		title_layout = (TitleView)findViewById(R.id.title_layout);
		
		title_layout.setTitle(titleStr);
		title_layout.setLeftImage(-1,  new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 finish(); 
			}
		});
	}
	private void initChart(){
		mChart = (LineChart) findViewById(R.id.linechart);
        mChart.setOnChartValueSelectedListener(this);
        mChart.setDrawGridBackground(false);
//        mChart.setViewPortOffsets(0, 20, 0, 0); //靠边
        // no description text
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

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);
	 
        
        
        mChart.getLegend().setEnabled(false);  //隐藏色素线条
        

        // dont forget to refresh the drawing
        
	}
	 private void setData() {
		 if(null == faultWarnDitailsInfo)
			 return;
		 LimitLine upLine = new LimitLine(faultWarnDitailsInfo.getUp(), ""+faultWarnDitailsInfo.getUp());
//		 upLine.setLineWidth(4f);
//		 upLine.setTypeface(mTf);
		 upLine.setTextColor(getResources().getColor(R.color.chart_Limit_line));
		 upLine.setLineColor(getResources().getColor(R.color.chart_Limit_line));
		 
		 LimitLine downLine = new LimitLine(faultWarnDitailsInfo.getDown(), ""+faultWarnDitailsInfo.getDown());
//		 downLine.setLineWidth(4f);
//		 downLine.setTypeface(mTf);
		 downLine.setTextColor(getResources().getColor(R.color.chart_Limit_line));
		 downLine.setLineColor(getResources().getColor(R.color.chart_Limit_line));
		 
	        XAxis xl = mChart.getXAxis();
	        xl.setAvoidFirstLastClipping(true);
	        xl.setPosition(XAxisPosition.BOTTOM/*XAxisPosition.BOTTOM_INSIDE*/);
	        xl.setDrawGridLines(false);
//	        xl.setLabelsToSkip(3); //定制X轴Label间隔。
	        xl.setAvoidFirstLastClipping(true); //定制X轴起点和终点Label不能超出屏幕。
//	        YAxis leftAxis = mChart.getAxisLeft();
//	        leftAxis.setInverted(true);
	        xl.setTextColor(getResources().getColor(R.color.personFont));
	        xl.setAxisLineColor(getResources().getColor(R.color.personFont));
	        
	        YAxis y = mChart.getAxisLeft();
	        y.setTypeface(mTf);
	        y.setLabelCount(6, false);//第一个参数是Y轴坐标的个数，第二个参数是 是否不均匀分布，true是不均匀分布  
	        y.setStartAtZero(false); //设置Y轴坐标是否从0开始  
//	        y.setAxisMaxValue(100);    //设置Y轴坐标最大为多少  
//	        y.setTextColor(Color.WHITE);
	        y.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART /*INSIDE_CHART*/);
	        y.setDrawGridLines(true);
	        y.setDrawAxisLine(false);
	        y.setGridColor(getResources().getColor(R.color.personFont));
//	        y.setAxisLineColor(Color.WHITE);
	        y.addLimitLine(upLine);
	        y.addLimitLine(downLine);
	        y.setTextColor(getResources().getColor(R.color.personFont));
	        
	        
	        
		 
		 

		 
		 int count = faultWarnDitailsInfo.getPList().length;
	        ArrayList<String> xVals = new ArrayList<String>();
	        
	        for (int i = 0; i < count; i++) {
	            xVals.add(faultWarnDitailsInfo.getPList()[i].getTime());
	        }

	        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

	        for (int i = 0; i < count; i++) {
	            yVals1.add(new BarEntry(faultWarnDitailsInfo.getPList()[i].getData(), i));
	        }
	        LineDataSet set1 = new LineDataSet(yVals1, "");
//	        set1.setLineSpacePercent(35f);
	        set1.setColor(getResources().getColor(R.color.chart_yes));
	        set1.setDrawCircles(false); //不画圆点
	        //单独的点
	        ArrayList<Entry> yVals2 = new ArrayList<Entry>();
	        yVals2.add(new BarEntry((float)faultWarnDitailsInfo.getPoint().getData(), count/2));
//	        yVals2.add(new BarEntry((float)oilLeve[oilLeve.length-1].getLevel(), oilLeve.length-1));
	        LineDataSet set2 = new LineDataSet(yVals2, "");
	        set2.setCircleColor(getResources().getColor(R.color.chart_fault_fill));
	        set2.setColor(getResources().getColor(R.color.transparent));
	        set2.setDrawCircles(true); 
	        set2.setDrawCircleHole(false);//是否空心
	        
	        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
	        dataSets.add(set1);
	        dataSets.add(set2);

	        LineData data = new LineData(xVals, dataSets);
	        data.setValueTextSize(10f);
	        data.setValueTypeface(mTf);
	        data.setDrawValues(false);
	        
	        
	        
	        mChart.setData(data);
//	        mChart.animateX(2000);
	        mChart.invalidate();
	        float max = mChart.getYChartMax();
	        float min = mChart.getYChartMin();
	        y.setAxisMinValue(-20);
	        if(max<faultWarnDitailsInfo.getUp()){
	        	y.setAxisMaxValue(faultWarnDitailsInfo.getUp()+OUTMAX);
	        }else{
	        	y.setAxisMaxValue(max+OUTMAX);
	        }
	        if(min < faultWarnDitailsInfo.getDown()){
	        	y.setAxisMinValue(min);
	        }else{
	        	y.setAxisMinValue(faultWarnDitailsInfo.getDown());
	        }
	        mChart.setData(data);
	        mChart.invalidate();
	        
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
		case Constants.HANDLER_FAULTDITAILS_SUCCESS:
			faultWarnDitailsInfo = (FaultWarnDitailsInfo)msg.obj;
			Constants.MyLog(""+faultWarnDitailsInfo.getUp()+" "+faultWarnDitailsInfo.getDown());
			unit_textView.setText(getResources().getString(R.string.fault_info_t_2)+faultWarnDitailsInfo.getUnit());
			setData();
			break;
		case Constants.HANDLER_FAULTDITAILS_FAIL:
			mChart.setDescription(getResources().getString(R.string.chart_nodata));
			break;
		case Constants.HANDLER_CHART_MARKER_TIME:
	        
			float[] f = (float[])msg.obj;
			if(null != f){
				RadiusButtonView rbView = new RadiusButtonView(this);
				rbView.setText(String.valueOf((int)f[2]));
				rbView.setRoundRadius(15f);
				rbView.setTextSize(getResources().getDimension(R.dimen.chart_marker_text_size));
				rbView.setTextColor(getResources().getColor(R.color.white));
				rbView.setColor(getResources().getColor(R.color.chart_fault_fill),
						getResources().getColor(R.color.chart_fault_fill), 
						getResources().getColor(R.color.chart_fault_fill));
				rbView.inToButton();
				RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
						(int)getResources().getDimension(R.dimen.chart_marker_width),
						(int)getResources().getDimension(R.dimen.chart_marker_height)
						);
				layoutParams.leftMargin=(int)f[0] + 5;
		        layoutParams.topMargin=(int)f[1] 
		        		- (int)getResources().getDimension(R.dimen.chart_marker_height)
		        		-5;
		        add_chart_layout.addView(rbView,layoutParams);
			}
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

}
