package com.cloudmachine.activities;

import android.animation.Animator;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cloudmachine.MyApplication;
import com.cloudmachine.R;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.bean.DailyWorkInfo;
import com.cloudmachine.bean.ScanningWTInfo;
import com.cloudmachine.chart.charts.BarChart;
import com.cloudmachine.chart.charts.HorizontalBarChart;
import com.cloudmachine.chart.components.XAxis;
import com.cloudmachine.chart.components.YAxis;
import com.cloudmachine.chart.data.BarData;
import com.cloudmachine.chart.data.BarDataSet;
import com.cloudmachine.chart.data.BarEntry;
import com.cloudmachine.chart.data.Entry;
import com.cloudmachine.chart.highlight.Highlight;
import com.cloudmachine.chart.listener.OnChartValueSelectedListener;
import com.cloudmachine.net.task.DailyWorkDitailsListAsync;
import com.cloudmachine.net.task.GetWorkTimeListAsync;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.ResV;
import com.cloudmachine.utils.UMListUtil;
import com.cloudmachine.utils.mpchart.DailyWorkYAxisValueFormatter;
import com.cloudmachine.widget.CommonTitleView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 项目名称：CloudMachine
 * 类描述：工作时长图表类页面
 * 创建人：shixionglu
 * 创建时间：2016/12/6 下午1:48
 * 修改人：shixionglu
 * 修改时间：2016/12/6 下午1:48
 * 修改备注：
 */

public class WorkTimeActivity extends BaseAutoLayoutActivity implements View.OnClickListener, Callback, SeekBar.OnSeekBarChangeListener,
        OnChartValueSelectedListener {

    //private Context mContext;
    // private Handler mHandler;
    //private long deviceId;

    private final static int sumDays = 30;
    private Handler mHandler;
    private long deviceId;
    protected HorizontalBarChart dailyChart;
    protected BarChart mChart;
    private Typeface tf;
    private int lastItem = 0;
    private double[] xD;
    private double[] yD;
    private CommonTitleView title_layout;
    //	private ScanningWTInfo[] wtInfo;
    private List<DailyWorkInfo> dailyWorkList;
    private int page = 0;
    private static final int SHOWNUM = 7;
    private static final int SUMPAGE = 4;
    private int pageNum;
    private int showNum;
    private static final int[] VORDIPLOM_COLORS = {
            Color.rgb(255, 247, 140), Color.rgb(192, 255, 140)
    };
    private static final int[] VORDIPLOM_COLORS0 = {
            MyApplication.mContext.getResources().getColor(R.color.chart_not), Color.TRANSPARENT
    };
    private static final int[] VORDIPLOM_COLORS1 = {//工作中
            Color.TRANSPARENT, MyApplication.mContext.getResources().getColor(R.color.chart_yes)
    };
    private static final int[] VORDIPLOM_COLORS2 = {
            Color.TRANSPARENT, MyApplication.mContext.getResources().getColor(R.color.chart_not)
    };

    private ScanningWTInfo[][] showWTInof;
    private ImageView left_image, right_image;
    private View daily_layout;
    private TextView daily_title;
    private TextView wtEmptTv;
    private LinearLayout wtFormatLlt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_work_time);
        UMListUtil.getUMListUtil().sendStruEvent("WorkTimeActivity", this);
        mHandler = new Handler(this);
        initView();

    }

    private void initView() {
        deviceId = getIntent().getLongExtra(Constants.P_DEVICEID, -1);
        wtEmptTv = (TextView) findViewById(R.id.wt_empt_tv);
        wtFormatLlt = (LinearLayout) findViewById(R.id.wt_form_llt);
        daily_layout = findViewById(R.id.daily_layout);
        daily_title = (TextView) findViewById(R.id.daily_title);
        daily_layout.setOnClickListener(this);

        initTitleLayout();

        left_image = (ImageView) findViewById(R.id.left_image);
        right_image = (ImageView) findViewById(R.id.right_image);
        left_image.setOnClickListener(this);
        right_image.setOnClickListener(this);


        mChart = (BarChart) findViewById(R.id.chart1);
        mChart.setLayerType(View.LAYER_TYPE_NONE, null);
        mChart.setNoDataText("");
//		Constants.ToastAction("uuu:"+mChart.isHardwareAccelerated());
//		View.setLayerType(View.LAYER_TYPE_HARDWARE, null);
//		mChart.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//        mChart.setOnChartValueSelectedListener(this);
        // mChart.setHighlightEnabled(false);

        mChart.setDrawBarShadow(false);

        mChart.setDrawValueAboveBar(true);

        mChart.setDescription("");
        mChart.setOnChartValueSelectedListener(this);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
//        mChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);
        mChart.setScaleEnabled(false);

        // draw shadows for each bar that show the maximum value
        // mChart.setDrawBarShadow(true);

        // mChart.setDrawXLabels(false);

        mChart.setDrawGridBackground(false);


        // mChart.setDrawYLabels(false);

        tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        XAxis xl = mChart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setTypeface(tf);
        xl.setDrawAxisLine(true);
        xl.setAxisLineColor(getResources().getColor(R.color.public_line));
        xl.setDrawGridLines(false);
        xl.setGridColor(getResources().getColor(R.color.public_line));
        xl.setGridLineWidth(0.3f);
        xl.enableGridDashedLine(5, 5, 0);
        xl.setEnabled(true);
        xl.setLabelsToSkip(0);
        xl.setTextColor(getResources().getColor(R.color.public_black_9));


        YAxis yl = mChart.getAxisLeft();
        yl.setTypeface(tf);
        yl.setDrawAxisLine(false);
        yl.setDrawGridLines(true);
        yl.setGridColor(getResources().getColor(R.color.public_line));
        yl.setGridLineWidth(0.3f);
        yl.setDrawLabels(true);
        yl.setAxisMaxValue(24);
        yl.setAxisMinValue(0);    //设置Y轴坐标最小为多少
        yl.setLabelCount(8, false);
        yl.setTextColor(getResources().getColor(R.color.public_black_9));
//        yl.enableGridDashedLine(30, 5, 0);
//        yl.setEnabled(false);
//        yl.setInverted(true);

        YAxis yr = mChart.getAxisRight();
        yr.setTypeface(tf);
        yr.setDrawAxisLine(true);
        yr.setDrawGridLines(false);
        yr.enableGridDashedLine(30, 5, 0);
        yr.setEnabled(false);
        mChart.getLegend().setEnabled(false);


//        yr.setInverted(true);
        // setting data
        // mChart.setDrawLegend(false);
        new GetWorkTimeListAsync(deviceId, mContext, mHandler).execute();

    }


    private void initPage(/*ScanningWTInfoArray wtInfoArray*/ArrayList<ScanningWTInfo> wtInfo) {
//		ScanningWTInfo[] wtInfo = wtInfoArray.getWtList();
        int len = wtInfo.size();
        int nnpage = len / SHOWNUM;
        int nnpp = len % SHOWNUM;
        if (nnpage == 0 && nnpp > 0) {
            pageNum = 1;
            showNum = nnpp;
        } else if (nnpage > 0 && nnpp == 0) {
            pageNum = nnpage;
            showNum = SHOWNUM;
            if (pageNum > SUMPAGE) {
                pageNum = SUMPAGE;
                showNum = SHOWNUM;
            }
        } else if (nnpage > 0 && nnpp > 0) {
            pageNum = nnpage + 1;
            showNum = nnpp;
            if (pageNum > SUMPAGE) {
                pageNum = SUMPAGE;
                showNum = SHOWNUM;
            }
        }
        showWTInof = new ScanningWTInfo[pageNum][7];

        int nPage = 0;
        for (int i = 0; i < len; i++) {
            if (i == (nPage + 1) * SHOWNUM) {
                nPage++;
            }
            if (nPage >= SUMPAGE) {
                return;
            }
            if (i < (nPage + 1) * SHOWNUM) {
                showWTInof[nPage][i - nPage * SHOWNUM] = wtInfo.get(len - i - 1)/*wtInfo[len-i-1]*/;
            }
        }

    }

    private void getDailyWork(int potion) {
//		if(null != wtInfo && wtInfo.length>0 && potion<wtInfo.length)
        new DailyWorkDitailsListAsync(deviceId, showWTInof[page][potion].getCollectionDate(), mContext, mHandler).execute();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        UMListUtil.getUMListUtil().removeList("WorkTimeFragment");
        super.onDestroy();
    }


    @Override
    public void initPresenter() {

    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        //MobclickAgent.onPageStart(UMengKey.time_machine_worktime);
        super.onResume();
    }

    @Override
    public void onPause() {
        //MobclickAgent.onPageEnd(UMengKey.time_machine_worktime);
        super.onPause();
    }

    @Override
    public boolean handleMessage(Message msg) {
        // TODO Auto-generated method stub
        switch (msg.what) {
            case Constants.HANDLER_DAILYWORK_SUCCESS:
                dailyWorkList = (List<DailyWorkInfo>) msg.obj;
                if (null != dailyWorkList) {
                    setDailyData();
                } else {
                    Constants.MyToast((String) msg.obj);
                }
                break;
            case Constants.HANDLER_DAILYWORK_FAIL:
                dailyWorkList = null;
                Constants.MyToast((String) msg.obj);
//			setDailyData();
                break;
            case Constants.HANDLE_GETWORKTIMELIST_SUCCESS:
                ArrayList<ScanningWTInfo> wtInfo = (ArrayList<ScanningWTInfo>) msg.obj;
                if (wtInfo == null || wtInfo.size() <= 0) {
                    wtEmptTv.setVisibility(View.VISIBLE);
                    wtFormatLlt.setVisibility(View.GONE);
                    break;
                }
                boolean istTime = false;
                for (ScanningWTInfo info : wtInfo) {
                    float workTime = info.getDayWorkHour();
                    if (workTime != 0) {
                        istTime = true;
                    }
                }
                if (!istTime) {
                    wtEmptTv.setVisibility(View.VISIBLE);
                    wtFormatLlt.setVisibility(View.GONE);
                    break;
                }
                wtEmptTv.setVisibility(View.GONE);
                wtFormatLlt.setVisibility(View.VISIBLE);
                int len = wtInfo.size();
                int s = len % SHOWNUM;
                if (s != 0) {
                    int addLen = 7 - s;
                    for (int i = 0; i < addLen; i++) {
                        ScanningWTInfo info = new ScanningWTInfo();
                        info.setDayWorkHour(0);
                        info.setCollectionDate("");
                        wtInfo.add(0, info);
                    }
                }
            /*if(null != wtInfo && wtInfo.size()%7!=0){
                int len = 7-wtInfo.size();
				for(int i=0; i<len; i++){
					ScanningWTInfo info = new ScanningWTInfo();
					info.setDayWorkHour(0);
					info.setCollectionDate(Constants.getNowTimeAll());
					wtInfo.add(info);
				}
			}*/
                initPage(wtInfo);
                changePage(0);
                setData();
                initDailyChart();
                break;
            case Constants.HANDLE_GETWORKTIMELIST_FAILD:
                Constants.MyToast((String) msg.obj);
                if (null != mChart)
                    mChart.setNoDataText(getResources().getString(R.string.work_time_daily_nodata));
                break;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.left_image:
                page++;
                if (page >= pageNum - 1)
                    page = pageNum - 1;
                changePage(page);
                setData();
                break;
            case R.id.right_image:
                page--;
                if (page < 0)
                    page = 0;
                changePage(page);
                setData();
                break;
            case R.id.daily_layout:
                daily_layout.setVisibility(View.GONE);
                break;
        }
    }


    private void setData() {

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<String> xVals = new ArrayList<String>();
        if (null == showWTInof) {
            wtEmptTv.setVisibility(View.VISIBLE);
            return;
        }
        if (null == showWTInof[page]) {
            wtEmptTv.setVisibility(View.VISIBLE);
            return;
        }

        int len = showWTInof[page].length;
        if (page == pageNum - 1) {
            len = showNum;
        }
        boolean isData = false;
        for (int i = 0; i < len; i++) {
            float workTime = showWTInof[page][len - i - 1].getDayWorkHour();
            if (workTime != 0) {
                isData = true;
            }
            xVals.add(
                    Constants.changeDateFormat2(showWTInof[page][len - i - 1].getCollectionDate(),
                            "yyyy-MM-dd", "MM-dd", new int[]{0}, new String[]{"今日"}));

            yVals1.add(new BarEntry(workTime, i));
        }
        if (!isData) {
            wtEmptTv.setVisibility(View.VISIBLE);
            mChart.setVisibility(View.GONE);
            return;
        }
        mChart.setVisibility(View.VISIBLE);
        wtEmptTv.setVisibility(View.GONE);
//	        xVals.add("D");
//	        BarEntry barr = new BarEntry(1,1);
//	        barr.setData(data)
        BarDataSet set1 = new BarDataSet(yVals1, "单位：小时");
        if (page == 0) {
            int[] nColor = new int[len];
            for (int i = 0; i < len; i++) {
                if (i == len - 1) {
                    nColor[i] = getResources().getColor(R.color.cor4);
                } else {
                    nColor[i] = getResources().getColor(R.color.cor6);
                }
            }
            set1.setColors(nColor);
        } else {
            set1.setColor(getResources().getColor(R.color.cor6));
        }
        set1.setBarSpacePercent(30f);
        set1.setValueFormatter(new DailyWorkYAxisValueFormatter(""));
        set1.setRound(5, 5);
        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(xVals, dataSets);
        data.setValueTypeface(tf);
        data.setValueTextSize(12f);
//        data.setValueTextColor(getResources().getColor(R.color.cor5));
//	        mChart.setVisibleXRangeMaximum(7);  //一个界面显示多少个点，其他点可以通过滑动看到
        mChart.setData(data);
//        mChart.getAnimator().setAnimListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                isChartSelected = true;
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animation) {
//
//            }
//        });
        mChart.animateY(1500);
    }

    private boolean isChartSelected;

    private void initDailyChart() {
        dailyChart = (HorizontalBarChart) findViewById(R.id.dailyChart);
        dailyChart.setLayerType(View.LAYER_TYPE_NONE, null);
        dailyChart.setNoDataText("");
//			Constants.ToastAction("uuu:"+dailyChart.isHardwareAccelerated());
//			View.setLayerType(View.LAYER_TYPE_HARDWARE, null);
//			dailyChart.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//	        dailyChart.setOnChartValueSelectedListener(this);
        // dailyChart.setHighlightEnabled(false);

        dailyChart.setDrawBarShadow(false);

        dailyChart.setDrawValueAboveBar(true);

        dailyChart.setDescription("");

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
//	        dailyChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        dailyChart.setPinchZoom(false);
        dailyChart.setScaleEnabled(false);

        // draw shadows for each bar that show the maximum value
        // dailyChart.setDrawBarShadow(true);

        // dailyChart.setDrawXLabels(false);

        dailyChart.setDrawGridBackground(false);


        // dailyChart.setDrawYLabels(false);

        tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        XAxis xl = dailyChart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.TOP_INSIDE);
        xl.setDrawLabels(false);
        xl.setGridColor(getResources().getColor(R.color.public_line));
        xl.setAxisLineColor(getResources().getColor(R.color.public_line));

	        /*
            xl.setPosition(XAxisPosition.BOTTOM);
	        xl.setTypeface(tf);
	        xl.setDrawAxisLine(true);
	        xl.setDrawGridLines(false);
//	        xl.setGridLineWidth(0.3f);
//	        xl.enableGridDashedLine(5, 5, 0);
	        xl.setEnabled(false);
	        */


        YAxis yl = dailyChart.getAxisLeft();
        yl.setDrawLabels(false);
        yl.setAxisMaxValue(24);
        yl.setAxisMinValue(0);    //设置Y轴坐标最小为多少
        yl.setLabelCount(12, false);
        yl.setSpaceTop(0f);
//	        yl.setGridLineWidth(0.3f);
//	        yl.setDrawLabels(false);
//	        yl.enableGridDashedLine(30, 5, 0);
        yl.setEnabled(false);
//	        yl.setInverted(true);

        YAxis yr = dailyChart.getAxisRight();
        yr.setTypeface(tf);
        yr.setDrawAxisLine(true);
        yr.setDrawGridLines(true);
        yr.setDrawLabels(true);
        yr.setAxisMaxValue(24);
        yr.setAxisMinValue(0);    //设置Y轴坐标最小为多少
        yr.setLabelCount(12, false);
        yr.setSpaceTop(0f);
        yr.setGridColor(getResources().getColor(R.color.public_line));
        yr.setAxisLineColor(getResources().getColor(R.color.public_line));
        yr.setTextColor(getResources().getColor(R.color.public_black_9));
//	        yr.enableGridDashedLine(30, 5, 0);
//	        yr.setInverted(true);
        // setting data

//	        Legend l = dailyChart.getLegend();
//	        l.setPosition(LegendPosition.BELOW_CHART_LEFT);
//	        l.setFormSize(8f);
//	        l.setXEntrySpace(4f);
        dailyChart.setDrawValueAboveBar(true);//柱状图上面的数值是否在柱子上面
        dailyChart.setVisibleXRangeMaximum(1);  //一个界面显示多少个点，其他点可以通过滑动看到
        dailyChart.getLegend().setEnabled(false);

    }

    private void setDailyData() {
        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("");
//		 ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
//		 ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();
        float[] allTime = getFenfloat();
        if (null != allTime) {
            float[][] drawTime = getDrawTime(allTime);
            for (int i = 0; i < allTime.length; i++) {
                ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();
                yVals.add(new BarEntry(drawTime[i], 0));
                BarDataSet set = new BarDataSet(yVals, "");
                if (i == 0) {
                    set.setColors(VORDIPLOM_COLORS0);
                    set.setDrawValues(false);
                } else if (i % 2 > 0) {
                    set.setColors(VORDIPLOM_COLORS1);
                    set.setDrawValues(true);
                } else {
                    set.setColors(VORDIPLOM_COLORS2);
                    set.setDrawValues(false);
                }
                set.setValueFormatter(new DailyWorkYAxisValueFormatter(""));
                set.setRound(10, 10);
                set.setHighlightEnabled(false);
                set.setValuesColorsSameRect(true);
                dataSets.add(set);
            }
        }
        /* if(null != dailyWorkList &&  dailyWorkList.size()>0 ){
             xVals.add(dailyWorkList.get(0).getDate());
			 int len = dailyWorkList.size();
			 int size = len + len + 1;
			 float[] f = new float[size];
			 float[] noWork = new float[len+1];
			 for(int i=0; i<len; i++){
				 if(i == 0){
					 noWork[i] = getTime("00:00:00",dailyWorkList.get(i).getStartTime());
					 if(i == len -1){
						 noWork[i+1] = getTime(dailyWorkList.get(i).getEndTime(),"23:59:59");
					 }
				 }else if(i == len-1){
					 noWork[i] = getTime(dailyWorkList.get(i-1).getEndTime(),dailyWorkList.get(i).getStartTime());
					 noWork[i+1] = getTime(dailyWorkList.get(i).getEndTime(),"23:59:59");
				 }else{
					 noWork[i] = getTime(dailyWorkList.get(i-1).getEndTime(),dailyWorkList.get(i).getStartTime());
				 }

				 f[i*2] = noWork[i];
				 f[i*2+1] =dailyWorkList.get(i).getWorkTime();
			 }
			 f[f.length-1] = noWork[noWork.length-1];
			 yVals1.add(new BarEntry(f, 0));
		 }*/


		 /*BarDataSet set1 = new BarDataSet(yVals1, "");
         set1.setColors(VORDIPLOM_COLORS2);
		 set1.setDrawValues(true);
		 set1.setRound(5, 5);
		 set1.setHighlightEnabled(false);
		 set1.setValuesColorsSameRect(true);*/

		 /*yVals2.add(new BarEntry(new float[]{1,2}, 0));
         yVals2.add(new BarEntry(new float[]{3,4}, 1));
		 yVals2.add(new BarEntry(new float[]{7,10}, 2));
		 BarDataSet set2 = new BarDataSet(yVals2, "");
		 set2.setColors(VORDIPLOM_COLORS2);
		 set2.setDrawValues(true);
		 set2.setRound(5, 5);
		 set2.setHighlightEnabled(false);
		 set2.setValuesColorsSameRect(true);*/


//	        dataSets.add(set2);

        BarData data = new BarData(xVals, dataSets);
        data.setValueTextSize(10f);
        data.setValueTypeface(tf);
//	        data.setDrawValues(true);

        dailyChart.setData(data);
        dailyChart.getAnimator().setAnimListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                daily_layout.setVisibility(View.VISIBLE);
                if (mShowAction == null) {
                    mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                            -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                    mShowAction.setDuration(500);
                }
                dailyChart.startAnimation(mShowAction);
                dailyChart.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        dailyChart.invalidate();
        dailyChart.animateY(1);
    }

    TranslateAnimation mShowAction;

    private float[] getFenfloat() {
        if (null != dailyWorkList && dailyWorkList.size() > 0) {
            int len = dailyWorkList.size();
            int size = len + len + 1;
            float[] f = new float[size];
            float[] noWork = new float[len + 1];
            for (int i = 0; i < len; i++) {
                if (i == 0) {
                    noWork[i] = getTime("00:00:00", dailyWorkList.get(i).getStartTime());
                    if (i == len - 1) {
                        noWork[i + 1] = getTime(dailyWorkList.get(i).getEndTime(), "23:59:59");
                    }
                } else if (i == len - 1) {
                    noWork[i] = getTime(dailyWorkList.get(i - 1).getEndTime(), dailyWorkList.get(i).getStartTime());
                    noWork[i + 1] = getTime(dailyWorkList.get(i).getEndTime(), "23:59:59");
                } else {
                    noWork[i] = getTime(dailyWorkList.get(i - 1).getEndTime(), dailyWorkList.get(i).getStartTime());
                }

                f[i * 2] = noWork[i];
                f[i * 2 + 1] = dailyWorkList.get(i).getWorkTime();
            }
            f[f.length - 1] = noWork[noWork.length - 1];
            return f;
        }
        return null;
    }

    private float[][] getDrawTime(float[] allTime) {
        int len = allTime.length;
        float[][] drawTime = new float[len][2];
        float[] temp = new float[len];
        for (int i = 0; i < len; i++) {
            if (i == 0) {
                temp[i] = drawTime[i][0] = allTime[i];

            } else {
                drawTime[i][0] = temp[i - 1];
                drawTime[i][1] = allTime[i];
                temp[i] = temp[i - 1] + allTime[i];
            }
        }
        return drawTime;
    }

    private void initDailyChart2() {
        dailyChart = (HorizontalBarChart) findViewById(R.id.dailyChart);
        dailyChart.setDescription("");

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        dailyChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        dailyChart.setPinchZoom(false);

        dailyChart.setDrawGridBackground(false);
        dailyChart.setDrawBarShadow(false);

        dailyChart.setDrawValueAboveBar(false);

        // change the position of the y-labels
        YAxis yLabels = dailyChart.getAxisRight();
//			yLabels.setValueFormatter(new MyYAxisValueFormatter());
        yLabels.setAxisMaxValue(6);
        yLabels.setAxisMinValue(0);    //设置Y轴坐标最小为多少
        yLabels.setLabelCount(6, false);

        YAxis yl = dailyChart.getAxisLeft();
        yl.setDrawLabels(false);
        yl.setAxisMaxValue(6);
        yl.setAxisMinValue(0);    //设置Y轴坐标最小为多少
        yl.setLabelCount(6, false);

        XAxis xLabels = dailyChart.getXAxis();
        xLabels.setPosition(XAxis.XAxisPosition.TOP);
        xLabels.setDrawLabels(false);

        // mChart.setDrawXLabels(false);
        // mChart.setDrawYLabels(false);

        // setting data
        setDailyData2();
        dailyChart.setDrawValueAboveBar(false);//柱状图上面的数值是否在柱子上面
        dailyChart.getLegend().setEnabled(false);
    }

    private void setDailyData2() {
        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add(mMonths[0 % mMonths.length]);

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();


        yVals1.add(new BarEntry(new float[]{1, 2, 3}, 0));

        BarDataSet set1 = new BarDataSet(yVals1, "");
        set1.setColors(getColors());

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(xVals, dataSets);
//			data.setValueFormatter(new MyValueFormatter());

        dailyChart.setData(data);
        dailyChart.invalidate();
    }

    private void initTitleLayout() {

        title_layout = (CommonTitleView) findViewById(R.id.title_layout);
        title_layout.setLeftOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (daily_layout.getVisibility() != View.GONE) {
                    daily_layout.setVisibility(View.GONE);
                } else {
                    finish();
                }
            }
        });
        title_layout.setRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.toActivity(WorkTimeActivity.this, StatisticsActivity.class, getIntent().getExtras());
            }
        });
    }



	/*private void addXY(){
        int len = wtInfo.length;
		xD = null;
		yD = null;
		len = len>=sumDays?sumDays:len;
		for(int i=0; i<len; i++){
			int dayNum = sumDays-Constants.getDateDays(wtInfo[lastItem].getCollectionDate(), wtInfo[i].getCollectionDate());
			xD = Constants.arrayCopyDouble(xD, dayNum);
			int t0 = (int)wtInfo[i].getDayWorkHour();
			yD = Constants.arrayCopyDouble(yD, (double)t0);
		}
	}*/
    /*private void addXY(){
        int len = wtInfo.length;
		xD = null;
		yD = null;
		len = len>=sumDays?sumDays:len;
		for(int i=0; i<len; i++){
			int dayNum = sumDays-Constants.getDateDays(wtInfo[lastItem].getCollectionDate(), wtInfo[i].getCollectionDate());
			xD = Constants.arrayCopyDouble(xD, dayNum);
			int t0 = (int)wtInfo[i].getDayWorkHour();
			yD = Constants.arrayCopyDouble(yD, (double)t0);
		}
	}*/


    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        // TODO Auto-generated method stub
//        if (!isChartSelected) {
//            return;
//        }
        if (e == null)
            return;
        if (e.getVal() <= 0) {
            return;
        }

        dailyWorkList = null;
//		setDailyData();


        int len = showWTInof[page].length;
        if (page == pageNum - 1) {
            len = showNum;
        }
        int size = len - e.getXIndex() - 1;
        String date = showWTInof[page][size].getCollectionDate();
        daily_title.setText(date + ResV.getString(R.string.work_time_text1));
        new DailyWorkDitailsListAsync(deviceId, date, this, mHandler).execute();
        /*RectF bounds = mChart.getBarBounds((BarEntry) e);
        PointF position = mChart.getPosition(e, mChart.getData().getDataSetByIndex(dataSetIndex)
                .getAxisDependency());

        */
    }

    @Override
    public void onNothingSelected() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }

    protected String[] mMonths = new String[]{
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"
    };

    private int[] getColors() {

//			int stacksize = 2;
//
//			// have as many colors as stack-values per entry
//			int[] colors = new int[stacksize];
//
//			for (int i = 0; i < stacksize; i++) {
//				colors[i] = ColorTemplate.VORDIPLOM_COLORS[i];
//			}
//
//			return colors;
        return VORDIPLOM_COLORS;
    }

    private float getTime(String date1, String date2) {
        float time = 0;
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        try {
            Date d1 = df.parse(date1);
            Date d2 = df.parse(date2);
            long diff = d2.getTime() - d1.getTime();//这样得到的差值是微秒级别
            long days = diff / (1000 * 60 * 60 * 24);
            long fen = diff / (1000 * 60);
            time = ((float) fen) / 60;
//			 BigDecimal   b  =   new BigDecimal(time);
//			 time   =  b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        } catch (Exception e) {

        }

        return time;
    }

    private void changePage(int page) {
        this.page = page;
        if (pageNum <= 1) {
            left_image.setVisibility(View.GONE);
            right_image.setVisibility(View.GONE);
        } else if (page == 0) {
            left_image.setVisibility(View.VISIBLE);
            right_image.setVisibility(View.GONE);

        } else if (page == pageNum - 1) {
            left_image.setVisibility(View.GONE);
            right_image.setVisibility(View.VISIBLE);
        } else {
            left_image.setVisibility(View.VISIBLE);
            right_image.setVisibility(View.VISIBLE);
        }
    }
}
