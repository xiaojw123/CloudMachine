package com.cloudmachine.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.autolayout.widgets.CustomDialog;
import com.cloudmachine.autolayout.widgets.DynamicWave;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.base.baserx.RxSchedulers;
import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.bean.ScanningOilLevelInfo;
import com.cloudmachine.bean.ScanningOilLevelInfoArray;
import com.cloudmachine.chart.charts.LineChart;
import com.cloudmachine.chart.components.Legend;
import com.cloudmachine.chart.components.Legend.LegendForm;
import com.cloudmachine.chart.components.XAxis;
import com.cloudmachine.chart.components.XAxis.XAxisPosition;
import com.cloudmachine.chart.components.YAxis;
import com.cloudmachine.chart.data.BarEntry;
import com.cloudmachine.chart.data.Entry;
import com.cloudmachine.chart.data.LineData;
import com.cloudmachine.chart.data.LineDataSet;
import com.cloudmachine.chart.highlight.Highlight;
import com.cloudmachine.chart.listener.OnChartValueSelectedListener;
import com.cloudmachine.chart.utils.AppLog;
import com.cloudmachine.helper.MobEvent;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.ApiConstants;
import com.cloudmachine.net.api.ApiService;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.ui.home.activity.OilSyncActivity;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.DensityUtil;
import com.cloudmachine.utils.ResV;
import com.cloudmachine.utils.ToastUtils;
import com.cloudmachine.utils.mpchart.DrawTimerTask;
import com.cloudmachine.utils.mpchart.MyMarkerView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;


public class OilAmountActivity extends BaseAutoLayoutActivity implements OnClickListener, Callback,
        OnChartValueSelectedListener {

    private static final int ANIMATETIME = 2000;
    private Context mContext;
    private Handler mHandler;
    private long deviceId;
    private String deviceName;
    private ScanningOilLevelInfo[] oilLeve;
    private ScanningOilLevelInfo lastLevel;
    private LineChart mChart, mWeekChat;
    private Typeface mTf;
    private int[] VORDIPLOM_COLORS = new int[2];

    //    private DynamicWave arcView;
    private TextView oil_proportion, last_date_oil_text, oil_proportion_last;
    private Timer myTimer;
    //    private TextView oilEmptyTv;
    private ViewGroup oilFormCotainer;
    private TextView todayTv, weekTv;
    private LinearLayout turnOffLayout;
    RelativeLayout todayRbView, weekRbView;
    String memberId;
    RelativeLayout liearChatCotanienr, weekChatCotainer;
    TextView oilEmptyTv;

    Button customBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mc_oilamount);
        this.mContext = this;
        mHandler = new Handler(this);
        if (UserHelper.isLogin(this)) {
            memberId = String.valueOf(UserHelper.getMemberId(this));
        }
        getIntentData();
        initView();
        getOilData();
    }

    @Override
    public void initPresenter() {

    }

    private void getIntentData() {
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            try {
                deviceId = bundle.getLong(Constants.P_DEVICEID);
                deviceName = bundle.getString(Constants.P_DEVICENAME);

//        		oilLeve = ((ScanningOilLevelInfoArray)bundle.get(Constants.P_OILLEVE)).getOilLevelList();
//        		lastLevel = (ScanningOilLevelInfo)bundle.get(Constants.P_LASTLEVEL);
            /*	int len = oilLeve.length;
                String tt ="\n";
        		for(int i=0; i<len; i++){
        			tt+=oilLeve[i].getLevel()+"\n";
        		}
        		Constants.MyLog(tt);*/
            } catch (Exception e) {

            }
        }
    }

    private void initView() {
        customBtn = (Button) findViewById(R.id.mc_custom_btn);
        liearChatCotanienr = (RelativeLayout) findViewById(R.id.linechart_cotainer);
        weekChatCotainer = (RelativeLayout) findViewById(R.id.weekchart_cotainer);
        turnOffLayout = (LinearLayout) findViewById(R.id.chart_bottom_layout);
        todayTv = (TextView) findViewById(R.id.mc_oil_today_tv);
        weekTv = (TextView) findViewById(R.id.mc_oil_week_tv);
        mChart = (LineChart) findViewById(R.id.linechart);
        mWeekChat = (LineChart) findViewById(R.id.weekchart);
        oilFormCotainer = (ViewGroup) findViewById(R.id.oil_form_container);
        oilEmptyTv = (TextView) findViewById(R.id.oil_empty_tv);
        oil_proportion = (TextView) findViewById(R.id.oil_proportion);
        todayTv.setOnClickListener(this);
        weekTv.setOnClickListener(this);
        customBtn.setOnClickListener(this);
        todayTv.setSelected(true);
        weekTv.setSelected(false);
        last_date_oil_text = (TextView) findViewById(R.id.last_date_oil_text);
        oil_proportion_last = (TextView) findViewById(R.id.oil_proportion_last);

    }

    private void setOilValue(int oilLave) {
        if (oilLave == -1) {
            oil_proportion.setText(ResV.getString(R.string.amount_text2));
        } else {
            if (oilLave < 0)
                oilLave = 0;
            if (oilLave > 100)
                oilLave = 100;
            oil_proportion.setText(oilLave + "%");
        }
//        if (oilLave < 0)
//            oilLave = 0;
//        if (oilLave > 100)
//            oilLave = 100;
//        arcView = (DynamicWave) findViewById(R.id.arcView);
//        arcView.setLave(oilLave);
    }

    private void setLastData() {
        if (null != lastLevel) {
            last_date_oil_text.setText(lastLevel.getTime() + " 关机油位");
            oil_proportion_last.setText(lastLevel.getLevel() + "%");
        } else {
            if (null != oilLeve && oilLeve.length > 0) {
                last_date_oil_text.setText(oilLeve[0].getTime() + " 关机油位");
                oil_proportion_last.setText(oilLeve[0].getLevel() + "%");
            } else {
                last_date_oil_text.setText("机器未启动");
                oil_proportion_last.setText(ResV.getString(R.string.amount_text2));
            }
        }
    }

    private void initChart(LineChart lineChart) {
        VORDIPLOM_COLORS[0] = getResources().getColor(R.color.oil_amount_proportion_last_text);
        VORDIPLOM_COLORS[1] = getResources().getColor(R.color.oil_amount_proportion_text);

        lineChart.setOnChartValueSelectedListener(this);
        lineChart.setDrawGridBackground(false);
//        mChart.setViewPortOffsets(0, 20, 0, 0); //靠边
        // no description text
        lineChart.setNoDataTextDescription("");
        lineChart.setNoDataText("");
        lineChart.setDescription("");
//        mChart.setDescriptionColor(Color.rgb(227, 135, 0)); 
//        mChart.setDescriptionPosition(40f,60f);  

        // enable value highlighting
        lineChart.setHighlightEnabled(true);

        // enable touch gestures
//        mChart.setTouchEnabled(false);

        // enable scaling and dragging
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(false);

        // if disabled, scaling can be done on x- and y-axis separately
        lineChart.setPinchZoom(false);
        mTf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        // add data
        setData(lineChart);
        // set an alternative background color
        // mChart.setBackgroundColor(Color.GRAY);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view, oilLeve, todayTv.isSelected());
        // set the marker to the chart
        lineChart.setMarkerView(mv);

        // enable/disable highlight indicators (the lines that indicate the
        // highlighted Entry)
//        mChart.setHighlightEnabled(false);

        XAxis xl = lineChart.getXAxis();
        xl.setAvoidFirstLastClipping(true);
        xl.setPosition(XAxisPosition.BOTTOM/*XAxisPosition.BOTTOM_INSIDE*/);
        xl.setDrawGridLines(false);
        xl.setAxisLineColor(getResources().getColor(R.color.main_bg));
//        xl.setLabelsToSkip(3); //定制X轴Label间隔。
        xl.setAvoidFirstLastClipping(true); //定制X轴起点和终点Label不能超出屏幕。
        xl.setTextColor(getResources().getColor(R.color.cor10));
//        YAxis leftAxis = mChart.getAxisLeft();
//        leftAxis.setInverted(true);

        YAxis y = lineChart.getAxisLeft();
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
        y.setAxisLineWidth(1f);
        y.setAxisLineColor(getResources().getColor(R.color.cor12));
        y.setLabelCount(5, false);
        y.setTextColor(getResources().getColor(R.color.cor10));
        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);


        // // restrain the maximum scale-out factor
        // mChart.setScaleMinima(3f, 3f);
        //
        // // center the view to a specific position inside the chart
        // mChart.centerViewPort(10, 50);

        // get the legend (only possible after setting data)
        Legend l = lineChart.getLegend();

        // modify the legend ...
        // l.setPosition(LegendPosition.LEFT_OF_CHART);
        l.setForm(LegendForm.LINE);

        lineChart.getLegend().setEnabled(false);  //隐藏色素线条

//        mChart.animateXY(2000, 2000);
        lineChart.animateX(ANIMATETIME);

        // dont forget to refresh the drawing
        lineChart.invalidate();
    }


    private void setData(LineChart chart) {
//		 focus
        if (null == oilLeve)
            return;
        if (oilLeve.length <= 0 && lastLevel == null)
            return;
        int count = oilLeve.length;
        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        int lastId = 0;
        if (null != lastLevel) {
            xVals.add("");
            yVals1.add(new BarEntry((float) lastLevel.getLevel(), 0));
            lastId = 1;
        }

        for (int i = 0; i < count; i++) {
            if (todayTv.isSelected()) {
                xVals.add(oilLeve[i].getTime());
            } else {
                xVals.add(oilLeve[i].getDay());
            }
        }


        for (int i = 0; i < count; i++) {
            yVals1.add(new BarEntry((float) oilLeve[i].getLevel(), i + lastId));
        }

        LineDataSet set1 = new LineDataSet(yVals1, "");
//	        set1.setLineSpacePercent(35f);
        set1.setColor(getResources().getColor(R.color.oil_amount_proportion_text));
        set1.setDrawCircles(false); //不画圆点
        set1.setLineWidth(2);
        //单独的点
        ArrayList<Entry> yVals2 = new ArrayList<Entry>();
        if (null != lastLevel) {
            yVals2.add(new BarEntry(lastLevel.getLevel(), 0));
        } else if (oilLeve.length > 0) {
            yVals2.add(new BarEntry((float) oilLeve[0].getLevel(), 0));
        }

        if (count > 1)
            yVals2.add(new BarEntry((float) oilLeve[count - 1].getLevel(), count - 1 + lastId));
        LineDataSet set2 = new LineDataSet(yVals2, "");
        set2.setLineWidth(4);
        set2.setCircleSize(6);
        int[] circleColors = VORDIPLOM_COLORS.clone();
        if (weekTv.isSelected()) {
            circleColors[0] = circleColors[1];
        }
        set2.setCircleColors(circleColors);
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
        chart.setData(data);
        startTimer(chart, yVals2);
    }

    private void startTimer(LineChart chart, ArrayList<Entry> yVals2) {
        if (null != myTimer) {
            myTimer.cancel();
            myTimer = null;
        }
        myTimer = new Timer(true);
        myTimer.schedule(new DrawTimerTask(chart,
                yVals2.get(0), new Highlight(0, 1),
                mHandler, Constants.HANDLER_CHART_MARKER_TIME), 200);
    }

    @Override
    public boolean handleMessage(Message msg) {
        // TODO Auto-generated method stub
        switch (msg.what) {
            case Constants.HANDLER_CHART_MARKER_TIME:
                float[] f = (float[]) msg.obj;
                if (null != f) {
                    if (todayTv.isSelected()) {
                        if (todayRbView == null) {
                            todayRbView = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.custom_marker_view, null);
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, DensityUtil.dip2px(this, 27));
                            liearChatCotanienr.addView(todayRbView, params);
                        }
                        setRaduiView(f, todayRbView, true);
                    } else if (weekTv.isSelected()) {
                        if (weekRbView == null) {
                            weekRbView = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.custom_marker_view, null);
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, DensityUtil.dip2px(this, 27));
                            weekChatCotainer.addView(weekRbView, params);
                        }
                        setRaduiView(f, weekRbView, false);
                    }
                }
                break;
        }
        return false;
    }

    private void setRaduiView(float[] f, RelativeLayout rbView, boolean isActived) {
        TextView contentTv = (TextView) rbView.findViewById(R.id.tvContent);
        if (oilLeve != null && oilLeve.length > 0) {
            ScanningOilLevelInfo info = oilLeve[0];
            if (info != null) {
//                        rbView.setText(String.valueOf((int) f[2]) + "% " + xTime);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rbView.getLayoutParams();
                int offset = DensityUtil.dip2px(this, 20);
                int offset1 = DensityUtil.dip2px(this, 2);
                if (f[2] > 90) {
                    layoutParams.leftMargin = (int) f[0] + offset1;
                    //			        layoutParams.topMargin=(int)f[1]
//			        		+5;
                    layoutParams.topMargin = (int) f[1] - offset;
                } else {
                    layoutParams.leftMargin = (int) f[0] + offset1;
                    layoutParams.topMargin = (int) f[1]
                            - (int) getResources().getDimension(R.dimen.chart_marker_height)
                            - offset1 - offset;
                }
                if (layoutParams.topMargin < 0) {
                    layoutParams.topMargin += (offset * 2);
                }
                if (layoutParams.leftMargin < 0) {
                    layoutParams.leftMargin = 0;
                }
                if (rbView.getVisibility() != View.VISIBLE) {
                    rbView.setVisibility(View.VISIBLE);
                }
                rbView.setActivated(isActived);
                contentTv.setText(info.getLevel() + "% " + info.getTime());
            } else {
                rbView.setVisibility(View.GONE);
            }
        } else {
            rbView.setVisibility(View.GONE);
        }
    }

    public void updateOilChat(ScanningOilLevelInfoArray infoArray, LineChart lineChart) {
        if (infoArray != null) {
            oilLeve = infoArray.getOilLevel();
            lastLevel = infoArray.getLastLevel();
            if (lastLevel != null || (oilLeve != null && oilLeve.length > 0)) {
                oilEmptyTv.setVisibility(View.GONE);
                oilFormCotainer.setVisibility(View.VISIBLE);
                if (todayTv.isSelected()) {
                    if (lastLevel != null) {//array 空 or 不空
                        ScanningOilLevelInfo level0 = new ScanningOilLevelInfo();
                        level0.setDay(lastLevel.getDay());
                        level0.setLevel(lastLevel.getLevel());
                        String time0 = lastLevel.getTime();
                        if (time0.length() >= 5) {
                            time0 = time0.substring(time0.length() - 5, time0.length());
                        }
                        level0.setTime(time0);
                        if (oilLeve != null && oilLeve.length > 0) {
                            ScanningOilLevelInfo[] tmpOilLeve = new ScanningOilLevelInfo[oilLeve.length + 1];
                            tmpOilLeve[0] = level0;
                            System.arraycopy(oilLeve, 0, tmpOilLeve, 1, tmpOilLeve.length - 1);
                            oilLeve = tmpOilLeve;
                            lineChart.setTouchEnabled(true);
                        } else {
                            lineChart.setTouchEnabled(false);
                            oilLeve = new ScanningOilLevelInfo[1];
                            oilLeve[0] = level0;
                        }
                    } else {//array必不空
                        lineChart.setTouchEnabled(true);
                        lastLevel = oilLeve[0];
                    }
                    ScanningOilLevelInfo newOilLeve = oilLeve[oilLeve.length - 1];
                    if (newOilLeve != null) {
                        setOilValue(newOilLeve.getLevel());
                    }
                    setLastData();
                }
                initChart(lineChart);
            } else {
                if (todayTv.isSelected()) {
                    oilEmptyTv.setVisibility(View.VISIBLE);
                    oilFormCotainer.setVisibility(View.GONE);
                }
            }
        } else {
            if (todayTv.isSelected()) {
                oilEmptyTv.setVisibility(View.VISIBLE);
                oilFormCotainer.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.mc_custom_btn:
                gotoCustomOil();
                break;
            case R.id.mc_oil_today_tv:
                if (todayTv.isSelected()) {
                    return;
                }
                todayTv.setSelected(true);
                weekTv.setSelected(false);
                liearChatCotanienr.setVisibility(View.VISIBLE);
                weekChatCotainer.setVisibility(View.GONE);
                turnOffLayout.setVisibility(View.VISIBLE);
                if (mChart.getData() == null) {
                    getOilData();
                }
                break;
            case R.id.mc_oil_week_tv:
                if (weekTv.isSelected()) {
                    return;
                }
                todayTv.setSelected(false);
                weekTv.setSelected(true);
                liearChatCotanienr.setVisibility(View.GONE);
                weekChatCotainer.setVisibility(View.VISIBLE);
                turnOffLayout.setVisibility(View.INVISIBLE);
                if (mWeekChat.getData() == null) {
                    getOilData();
                }
                break;
        }

    }

    public void gotoCustomOil() {
        mRxManager.add(Api.getDefault(HostType.HOST_CLOUDM_YJX).queryWorkStatus(deviceId).compose(RxSchedulers.<BaseRespose<JsonObject>>io_main()).subscribe(new RxSubscriber<BaseRespose<JsonObject>>(mContext) {
            @Override
            protected void _onNext(BaseRespose<JsonObject> respose) {

                if (respose.success()) {

                    JsonObject resJobj = respose.getResult();
                    if (resJobj != null) {
                        JsonElement isWorkJE = resJobj.get("isWork");
                        boolean isWork = isWorkJE.getAsBoolean();
                        if (isWork) {
                            Bundle data = new Bundle();
                            data.putLong(Constants.P_DEVICEID, deviceId);
                            Constants.toActivity(OilAmountActivity.this, OilSyncActivity.class, data);
                        } else {
                            showPromptDialog(R.drawable.ic_alert_mc, "请启动机器再进行校准哦！");
                        }
                    }

                } else {
                    showPromptDialog(-1, respose.getMessage());
                }


            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(OilAmountActivity.this, message);

            }
        }));


    }

    public void showPromptDialog(int resId, String message) {
        CustomDialog.Builder builder = new CustomDialog.Builder(OilAmountActivity.this);
        if (resId != -1) {
            builder.setAlertIcon(resId);
        }
        builder.setMessage(message);
        builder.setNeutralButton("好", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();

    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h, MotionEvent event) {
        // TODO Auto-generated method stub
        AppLog.print("onValuseSelected__Entry_" + e);
        if (todayTv.isSelected()) {
            if (todayRbView != null && todayRbView.getVisibility() == View.VISIBLE) {
                todayRbView.setVisibility(View.GONE);
            }
        } else if (weekTv.isSelected()) {
            if (weekRbView != null && weekRbView.getVisibility() == View.VISIBLE) {
                weekRbView.setVisibility(View.GONE);
            }
        }

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
        MobclickAgent.onEvent(this, MobEvent.TIME_MACHINE_WATERLEVEL);
//        getOilData();
    }

    public void getOilData() {
        if (todayTv.isSelected()) {
            mRxManager.add(Api.getDefault(HostType.HOST_CLOUDM_YJX).getTodayOil(memberId, deviceId).compose(RxHelper.<ScanningOilLevelInfoArray>handleResult()).subscribe(new RxSubscriber<ScanningOilLevelInfoArray>(mContext) {
                @Override
                protected void _onNext(ScanningOilLevelInfoArray infoArray) {
                    updateOilChat(infoArray, mChart);
                }

                @Override
                protected void _onError(String message) {
                    ToastUtils.showToast(mContext, message);
                }
            }));
        } else if (weekTv.isSelected()) {
            mRxManager.add(Api.getDefault(HostType.HOST_CLOUDM_YJX).getWeeklyOil(memberId, deviceId).compose(RxSchedulers.<JsonObject>io_main()).subscribe(new RxSubscriber<JsonObject>(mContext) {
                @Override
                protected void _onNext(JsonObject responseJson) {
                    if (responseJson != null) {
                        JsonElement resutJe = responseJson.get("result");
                        if (resutJe != null) {
                            Gson gson = new Gson();
                            JsonArray resultJarray = resutJe.getAsJsonArray();
                            ScanningOilLevelInfoArray infoArray = null;
                            if (resultJarray != null && resultJarray.size() > 0) {
                                ScanningOilLevelInfo[] oilLevelInfoArray = gson.fromJson(resultJarray, new TypeToken<ScanningOilLevelInfo[]>() {
                                }.getType());
                                infoArray = new ScanningOilLevelInfoArray();
                                infoArray.setOilLevel(oilLevelInfoArray);
                            }
                            updateOilChat(infoArray, mWeekChat);
                        }

                    }

                }

                @Override
                protected void _onError(String message) {
                    ToastUtils.showToast(mContext, message);
                }
            }));
        }

    }


}
