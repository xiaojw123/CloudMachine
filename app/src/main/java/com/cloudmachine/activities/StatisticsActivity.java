package com.cloudmachine.activities;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.autolayout.utils.AutoUtils;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.bean.StatisticsInfo;
import com.cloudmachine.net.api.ApiConstants;
import com.cloudmachine.net.task.StatisticsAsync;
import com.cloudmachine.ui.homepage.activity.QuestionCommunityActivity;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.UMListUtil;
import com.cloudmachine.utils.UMengKey;
import com.cloudmachine.widget.CommonTitleView;
import com.umeng.analytics.MobclickAgent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2016/12/6 下午1:51
 * 修改人：shixionglu
 * 修改时间：2016/12/6 下午1:51
 * 修改备注：
 */

public class StatisticsActivity extends BaseAutoLayoutActivity implements Handler.Callback, View.OnClickListener {

    private Handler mHandler;
    private long mDeviceId;
    private CommonTitleView title_layout;
    private ImageView mIvCalendar;
    private ArrayList<String> calendarList;
    private ListView lvCalendar;
    private View mView;
    private PopupWindow mPopupWindow;
    private TextView mSelectMonth;
    private StringBuilder sb = new StringBuilder();
    private TextView mTvWorkDaysValue;
    private TextView mTvTotalHoursValue;
    private TextView mTvAveHoursValue;
    private TextView mTvWorkRateValue;
    private String mCurrentString;
    private SimpleDateFormat mDateFormat;
    private Date mDate;
    private String mReplace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_statistics);
        mHandler = new Handler(this);
        initView();
        UMListUtil.getUMListUtil().sendStruEvent("StatisticsActivity", this);
    }


    public void getPreMonth(Calendar calendar, ArrayList<String> list) {
        for (int i = 1; i <= 5; i++) {
            calendar.setTime(mDate); // 设置为当前时间
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - i);
            calendarList.add(mDateFormat.format(calendar.getTime()));
        }

    }

    private void initView() {
        mDeviceId = getIntent().getLongExtra(Constants.P_DEVICEID,-1);
        calendarList = new ArrayList<>();//初始化集合
        //当月
        mDateFormat = new SimpleDateFormat("yyyy年MM月");
        mDate = new Date();
        calendarList.add(mDateFormat.format(mDate));
        mReplace = mDateFormat.format(mDate).replace("年", "").replace("月", "");
        new StatisticsAsync(mContext, mHandler, mDeviceId, mReplace).execute();
        //前几月
        Calendar calendar = Calendar.getInstance();
        getPreMonth(calendar, calendarList);

        initTitleLayout();
        mIvCalendar = (ImageView) findViewById(R.id.iv_calendar);
        mIvCalendar.setOnClickListener(this);
        mSelectMonth = (TextView) findViewById(R.id.f11);//对应日历的月份(默认展示最近一个月)
        mSelectMonth.setText(splitString2(mDateFormat.format(mDate)));
        mTvWorkDaysValue = (TextView)findViewById(R.id.tv_work_days_value);//工作天数
        mTvTotalHoursValue = (TextView)findViewById(R.id.tv_total_hours_value);
        mTvAveHoursValue = (TextView) findViewById(R.id.tv_ave_hours_value);
        mTvWorkRateValue = (TextView)findViewById(R.id.tv_work_rate_value);
    }

    private void initTitleLayout() {

        title_layout = (CommonTitleView)findViewById(R.id.title_layout);
        title_layout.setRightImg(R.drawable.coupon_des_normal, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(mContext, UMengKey.count_work_time_statistics_info);
                Intent statisticsIntent = new Intent(mContext, QuestionCommunityActivity.class);
                statisticsIntent.putExtra(QuestionCommunityActivity.H5_URL, ApiConstants.AppWorkTimeStatistics);
                startActivity(statisticsIntent);
            }
        });

    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case Constants.HANDLER_GETDATASTATISTICS_SUCCESS:
                StatisticsInfo statisticsInfo = (StatisticsInfo) msg.obj;
                mTvWorkDaysValue.setText(statisticsInfo.getWorkDay());
                mTvTotalHoursValue.setText(statisticsInfo.getTotalWorkTime());
                mTvAveHoursValue.setText(statisticsInfo.getAvgWorkTime());
                mTvWorkRateValue.setText(statisticsInfo.getWorkRate());
                if (!TextUtils.isEmpty(statisticsInfo.getDeviceName()) && statisticsInfo.getRanking() != 0 && !TextUtils.isEmpty(statisticsInfo.getLeading())) {
                    String strLine2 = statisticsInfo.getDeviceName() + "  累计工时数在云机械设备中排名" + statisticsInfo.getRanking() + ", 请继续保持哦。";
                    SpannableString spanLine2 = new SpannableString(strLine2);
                    spanLine2.setSpan(new TextAppearanceSpan(mContext, R.style.device_name), 0, statisticsInfo.getDeviceName().length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spanLine2.setSpan(new TextAppearanceSpan(mContext, R.style.ranking), statisticsInfo.getDeviceName().length() + 16, statisticsInfo.getDeviceName().length() + 16 + String.valueOf(statisticsInfo.getRanking()).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    //Constants.MyLog(spanLine2.length()+"1111111111");
                    if (TextUtils.isEmpty(mCurrentString)) {
                        mCurrentString = mReplace;
                    }
                    String strLine3 = mCurrentString.substring(4) + "月份开工率已领先" + statisticsInfo.getLeading() + "%的云机械设备。";

                    SpannableString spanLine3 = new SpannableString(strLine3);
                    spanLine3.setSpan(new TextAppearanceSpan(mContext, R.style.ranking), mCurrentString.substring(4).length() + 8, mCurrentString.substring(4).length() + 8 + statisticsInfo.getLeading().length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else {
                    Constants.MyToast("当月没有数据");
                }
                break;
            case Constants.HANDLER_GETDATASTATISTICS_FAILD:

                break;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_calendar:
                mView = getLayoutInflater().inflate(R.layout.check_calendar_view, null);
                lvCalendar = (ListView) mView.findViewById(R.id.lv_calendar);
                lvCalendar.setAdapter(new CheckCalendarAdapter());

                mPopupWindow = new PopupWindow(mView, dpToPx(140), LinearLayout.LayoutParams.WRAP_CONTENT);
                // mPopupWindow = new PopupWindow(mView, getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels);
                showPopupWindow(mIvCalendar);//展示popupwindow

                lvCalendar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        TextView tvDate = (TextView) view.findViewById(R.id.tv_date);
                        splitString(tvDate);//切分好字符串并发送网络请求
                        closePopupWindow();
                    }
                });
                break;
            default:
                break;
        }
    }

    private void splitString(TextView tv) {
        String str1 = tv.getText().toString();
        String str2 = getString(str1, "年");
        mCurrentString = getString(str2, "月");


        String part1 = mCurrentString.substring(0, 4);//生成日历左侧的时间格式
        String part2 = mCurrentString.substring(4);
        sb.setLength(0);
        sb.append(part2).append("/").append(part1);
        mSelectMonth.setText(sb.toString());
        new StatisticsAsync(mContext, mHandler, mDeviceId, mCurrentString).execute();
        //new StatisticsAsync(mContext, mHandler, 833, mCurrentString).execute();
    }

    public String splitString2(String currentString) {

        String str1 = getString(currentString, "年");
        String str2 = getString(str1, "月");
        String part1 = str2.substring(0, 4);
        String part2 = str2.substring(4);
        sb.setLength(0);
        sb.append(part2).append("/").append(part1);
        return sb.toString();
    }


    private void showPopupWindow(View v) {

        //修改系统页面的透明度
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.7f;
        getWindow().setAttributes(params);

        //mView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        //  int popupWidth = mView.getMeasuredWidth();    //  获取测量后的宽度
        //  int popupHeight = mView.getMeasuredHeight();  //获取测量后的高度
        //ﬁ  int[] location = new int[2];
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setOutsideTouchable(true);   //设置外部点击关闭ppw窗口
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = getWindow().getAttributes();
                params.alpha = 1f;
                getWindow().setAttributes(params);
            }
        });

        //v.getLocationOnScreen(location);
        //mPopupWindow.showAtLocation(v, Gravity.NO_GRAVITY,location[0]+v.getWidth(),location[1]);
        mPopupWindow.showAsDropDown(v, -dpToPx(134), 0);
    }


    /**
     * popupwindow弹出框的listview适配器
     */
    class CheckCalendarAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return 6;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item_calendar, null);
                holder = new ViewHolder();
                holder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
                convertView.setTag(holder);
                AutoUtils.autoSize(convertView);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tvDate.setText(calendarList.get(position));
            holder.tvDate.setTextSize(AutoUtils.getPercentWidthSizeBigger(48));
            return convertView;
        }

        class ViewHolder {
            TextView tvDate;
        }

    }


    public String getString(String s, String s1)//s是需要删除某个子串的字符串s1是需要删除的子串  
    {
        int postion = s.indexOf(s1);
        int length = s1.length();
        int Length = s.length();
        String newString = s.substring(0, postion) + s.substring(postion + length, Length);
        return newString;//返回已经删除好的字符串  
    }

    private void closePopupWindow() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
            mPopupWindow = null;
            WindowManager.LayoutParams params =getWindow().getAttributes();
            params.alpha = 1f;
            getWindow().setAttributes(params);
        }
    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        UMListUtil.getUMListUtil().removeList("StatisticsFragment");
    }
}
