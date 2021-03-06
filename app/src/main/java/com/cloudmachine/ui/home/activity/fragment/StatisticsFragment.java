package com.cloudmachine.ui.home.activity.fragment;

import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
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
import com.cloudmachine.base.BaseFragment;
import com.cloudmachine.bean.StatisticsInfo;
import com.cloudmachine.helper.MobEvent;
import com.cloudmachine.net.task.StatisticsAsync;
import com.cloudmachine.utils.Constants;
import com.umeng.analytics.MobclickAgent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2016/12/6 下午1:51
 * 修改人：shixionglu
 * 修改时间：2016/12/6 下午1:51
 * 修改备注：
 */

public class StatisticsFragment extends BaseFragment implements Handler.Callback, View.OnClickListener {

    private Handler mHandler;
    private long mDeviceId;
    private ImageView mIvCalendar;
    private ArrayList<String> calendarList = new ArrayList<>();
    private PopupWindow mPopupWindow;
    private TextView mSelectMonth;
    private StringBuilder sb = new StringBuilder();
    private TextView mTvWorkDaysValue;
    private TextView mTvTotalHoursValue;
    private TextView mTvAveHoursValue;
    private TextView mTvWorkRateValue;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy年MM月");
    private String mReplace;
    private TextView mEmptyTv;
    private LinearLayout daysItemCotainer;
//    private RadiusButtonView mViewReportBtn;

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onEvent(getActivity(), MobEvent.TIME_MACHINE_WORKTIME_STATISTICS);
    }


    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case Constants.HANDLER_GETDATASTATISTICS_SUCCESS:
                StatisticsInfo statisticsInfo = (StatisticsInfo) msg.obj;
                if (statisticsInfo == null) {
                    mEmptyTv.setVisibility(View.VISIBLE);
                    daysItemCotainer.setVisibility(View.GONE);
                    break;
                }
                String workDay = statisticsInfo.getWorkDay();
                String toalWorkTime = statisticsInfo.getTotalWorkTime();
                String avgWorkTime = statisticsInfo.getAvgWorkTime();
                String workRate = statisticsInfo.getWorkRate();
                if (TextUtils.isEmpty(workDay) && TextUtils.isEmpty(toalWorkTime) && TextUtils.isEmpty(avgWorkTime) & TextUtils.isEmpty(workRate)) {
                    mEmptyTv.setVisibility(View.VISIBLE);
                    daysItemCotainer.setVisibility(View.GONE);
                    break;
                } else {
                    mEmptyTv.setVisibility(View.GONE);
                    daysItemCotainer.setVisibility(View.VISIBLE);
                }
                mTvWorkDaysValue.setText(workDay);
                mTvTotalHoursValue.setText(toalWorkTime);
                mTvAveHoursValue.setText(avgWorkTime);
                mTvWorkRateValue.setText(workRate);
//                if (!TextUtils.isEmpty(statisticsInfo.getDeviceName()) && statisticsInfo.getRanking() != 0 && !TextUtils.isEmpty(statisticsInfo.getLeading())) {
//                    if (!isAdded()) {
//                        return false;
//                    }
//                    String strLine2 = statisticsInfo.getDeviceName() + "  累计工时数在云机械设备中排名" + statisticsInfo.getRanking() + ", 请继续保持哦。";
//                    SpannableString spanLine2 = new SpannableString(strLine2);
//                    spanLine2.setSpan(new TextAppearanceSpan(getActivity(), R.style.device_name), 0, statisticsInfo.getDeviceName().length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    spanLine2.setSpan(new TextAppearanceSpan(getActivity(), R.style.ranking), statisticsInfo.getDeviceName().length() + 16, statisticsInfo.getDeviceName().length() + 16 + String.valueOf(statisticsInfo.getRanking()).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    //Constants.MyLog(spanLine2.length()+"1111111111");
//                    if (TextUtils.isEmpty(mCurrentString)) {
//                        mCurrentString = mReplace;
//                    }
//                    String strLine3 = mCurrentString.substring(4) + "月份开工率已领先" + statisticsInfo.getLeading() + "%的云机械设备。";
//
//                    SpannableString spanLine3 = new SpannableString(strLine3);
//                    spanLine3.setSpan(new TextAppearanceSpan(getActivity(), R.style.ranking), mCurrentString.substring(4).length() + 8, mCurrentString.substring(4).length() + 8 + statisticsInfo.getLeading().length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                } else {
//                    Constants.MyToast("当月没有数据");
//                }
                break;
            case Constants.HANDLER_GETDATASTATISTICS_FAILD:
                mEmptyTv.setVisibility(View.VISIBLE);
                daysItemCotainer.setVisibility(View.GONE);
                break;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.radius_button_text:
//                Bundle vB = new Bundle();
//                vB.putString(QuestionCommunityActivity.H5_URL, ApiConstants.AppWorkReport);
//                Constants.toActivity(getActivity(), QuestionCommunityActivity.class, vB);
//                break;
            case R.id.iv_calendar:
                // mPopupWindow = new PopupWindow(mView, getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels);
                showPopupWindow();//展示popupwindow
                break;
            default:
                break;
        }
    }

    private void splitString(TextView tv) {
        String str1 = tv.getText().toString();
        String str2 = getString(str1, "年");
        String mCurrentString = getString(str2, "月");
        String part1 = mCurrentString.substring(0, 4);//生成日历左侧的时间格式
        String part2 = mCurrentString.substring(4);
        sb.setLength(0);
        sb.append(part2).append("/").append(part1);
        mSelectMonth.setText(sb.toString());
        new StatisticsAsync(mHandler, mDeviceId, mCurrentString).execute();
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


    private void showPopupWindow() {
        if (mPopupWindow == null) {
            View mView = getActivity().getLayoutInflater().inflate(R.layout.check_calendar_view, null);
            ListView lvCalendar = (ListView) mView.findViewById(R.id.lv_calendar);
            lvCalendar.setAdapter(new CheckCalendarAdapter());
            lvCalendar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView tvDate = (TextView) view.findViewById(R.id.tv_date);
                    splitString(tvDate);//切分好字符串并发送网络请求
                    closePopupWindow();
                }
            });

            mPopupWindow = new PopupWindow(mView, dpToPx(140), LinearLayout.LayoutParams.WRAP_CONTENT);

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
                    WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
                    params.alpha = 1f;
                    getActivity().getWindow().setAttributes(params);
                }
            });

        }
        if (!mPopupWindow.isShowing()) {
            //修改系统页面的透明度
            WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
            params.alpha = 0.7f;
            getActivity().getWindow().setAttributes(params);
            mPopupWindow.showAsDropDown(mIvCalendar, -dpToPx(134), 0);
        }
    }


    /**
     * popupwindow弹出框的listview适配器
     */
  private   class CheckCalendarAdapter extends BaseAdapter {
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
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_calendar, null);
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
        return s.substring(0, postion) + s.substring(postion + length, Length);//返回已经删除好的字符串
    }

    private void closePopupWindow() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
            mPopupWindow = null;
            WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
            params.alpha = 1f;
            getActivity().getWindow().setAttributes(params);
        }
    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    @Override
    protected void initView() {
        mHandler = new Handler(this);
        mDeviceId = getActivity().getIntent().getLongExtra(Constants.P_DEVICEID, -1);
        mEmptyTv = (TextView) viewParent.findViewById(R.id.work_days_empty_tv);
        daysItemCotainer = (LinearLayout) viewParent.findViewById(R.id.work_days_container);
        mIvCalendar = (ImageView) viewParent.findViewById(R.id.iv_calendar);
        mIvCalendar.setOnClickListener(this);
        mSelectMonth = (TextView) viewParent.findViewById(R.id.f11);//对应日历的月份(默认展示最近一个月)
        mTvWorkDaysValue = (TextView) viewParent.findViewById(R.id.tv_work_days_value);//工作天数
        mTvTotalHoursValue = (TextView) viewParent.findViewById(R.id.tv_total_hours_value);
        mTvAveHoursValue = (TextView) viewParent.findViewById(R.id.tv_ave_hours_value);
        mTvWorkRateValue = (TextView) viewParent.findViewById(R.id.tv_work_rate_value);
//       mViewReportBtn = (RadiusButtonView) viewParent.findViewById(R.id.view_report_btn);
//        mViewReportBtn.setOnClickListener(this);
//        if (!TextUtils.isEmpty(ApiConstants.AppWorkReport)) {
//            mViewReportBtn.setVisibility(View.VISIBLE);
//            String deviceName = getActivity().getIntent().getStringExtra(Constants.P_DEVICENAME);
//            mViewReportBtn.setText("查看"+deviceName+"月度工作报表");
//        }
        initCalendarData();
        new StatisticsAsync(mHandler, mDeviceId, mReplace).execute();

    }

    private void initCalendarData() {
        Calendar c = Calendar.getInstance();
        for (int i = 0; i <= 5; i++) {
            String dateStr = mDateFormat.format(c.getTime());
            calendarList.add(dateStr);
            if (i == 0) {
                mSelectMonth.setText(splitString2(dateStr));
                mReplace = dateStr.replace("年", "").replace("月", "");
            }
            if (i < 5) {
                c.add(Calendar.MONTH, -1);
            }
        }
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_statistics;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
