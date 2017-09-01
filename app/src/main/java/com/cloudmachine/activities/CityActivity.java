package com.cloudmachine.activities;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationClientOption.AMapLocationProtocol;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.LocationSource;
import com.cloudmachine.R;
import com.cloudmachine.chart.utils.AppLog;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.DensityUtil;
import com.cloudmachine.utils.locatecity.City;
import com.cloudmachine.utils.locatecity.DBHelper;
import com.cloudmachine.utils.locatecity.DatabaseHelper;
import com.cloudmachine.utils.locatecity.MyLetterListView;
import com.cloudmachine.utils.locatecity.MyLetterListView.OnTouchingLetterChangedListener;
import com.cloudmachine.utils.locatecity.PingYinUtil;
import com.cloudmachine.utils.widgets.ClearEditTextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;


public class CityActivity extends Activity implements OnScrollListener, TextWatcher, OnClickListener {
    private BaseAdapter adapter;
    private ListView personList;
    private TextView overlay; // 对话框首字母textview
    private MyLetterListView letterListView; // A-Z listview
    private HashMap<String, Integer> alphaIndexer;// 存放存在的汉语拼音首字母和与之对应的列表位置
    private String[] sections;// 存放存在的汉语拼音首字母
    private Handler handler;
    private OverlayThread overlayThread; // 显示首字母对话框
    private ArrayList<City> allCity_lists; // 所有城市列表
    private ArrayList<City> city_lists;// 城市列表
    private ArrayList<City> city_hot;
    private ArrayList<City> city_result;
    private ArrayList<String> city_history;
    private TextView tv_noresult;

    private LocationSource.OnLocationChangedListener mListener;

    private String currentCity; // 用于保存定位到的城市
    private int locateProcess = 1; // 记录当前定位的状态 正在定位-定位成功-定位失败
    private boolean isNeedFresh;

    private DatabaseHelper helper;

    private AMapLocationClient locationClient = null;

    ClearEditTextView citySearchEdt;
    TextView cancelTv;
    SearchResultAdapter searchAdapter;
    private LayoutInflater inflater;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        inflater = LayoutInflater.from(this);
        allCity_lists = new ArrayList<>();
        city_hot = new ArrayList<>();
        city_result = new ArrayList<>();
        city_history = new ArrayList<>();
        helper = new DatabaseHelper(this);
        currentCity = getIntent().getStringExtra(Constants.P_CITYNAME);
        citySearchEdt = (ClearEditTextView) findViewById(R.id.city_search_edt);
        cancelTv = (TextView) findViewById(R.id.city_cancel_tv);
        cancelTv.setOnClickListener(this);
        citySearchEdt.addTextChangedListener(this);
        personList = (ListView) findViewById(R.id.list_view);
        letterListView = (MyLetterListView) findViewById(R.id.city_selected_mlv);
        letterListView
                .setOnTouchingLetterChangedListener(new LetterListViewListener());
        alphaIndexer = new HashMap<>();
        handler = new Handler();
        overlayThread = new OverlayThread();
        isNeedFresh = true;
//        personList.setOnItemClickListener(new OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                TextView itemNameTv = (TextView) view.findViewById(R.id.name);
//                if (itemNameTv != null) {
//                    Object tagObj = itemNameTv.getTag(R.id.city_list_item);
//                    if (tagObj != null && tagObj instanceof City) {
//                        selectCity(((City) tagObj).getName());
//                    }
//                }
//
//            }
//        });
        locateProcess = 1;
        initOverlay();
        cityInit();
        hotCityInit();
        hisCityInit();
        adapter = new ListAdapter(allCity_lists, city_hot);
        personList.setAdapter(adapter);
        initLocation();
    }


    public void InsertCity(String name) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from recentcity where name = '"
                + name + "'", null);
        if (cursor.getCount() > 0) { //
            db.delete("recentcity", "name = ?", new String[]{name});
        }
        db.execSQL("insert into recentcity(name, date) values('" + name + "', "
                + System.currentTimeMillis() + ")");
        db.close();
    }


    private void cityInit() {
        City city = new City("★", "2"); // 热门城市
        allCity_lists.add(city);
        city_lists = getCityList();
        allCity_lists.addAll(city_lists);
    }

    /**
     * 热门城市
     */
    public void hotCityInit() {
        City city = new City("北京", "2");
        city_hot.add(city);
        city = new City("广州", "2");
        city_hot.add(city);
        city = new City("成都", "2");
        city_hot.add(city);
        city = new City("深圳", "2");
        city_hot.add(city);
        city = new City("杭州", "2");
        city_hot.add(city);
        city = new City("武汉", "2");
        city_hot.add(city);
    }

    private void hisCityInit() {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "select * from recentcity order by date desc limit 0, 3", null);
        while (cursor.moveToNext()) {
            city_history.add(cursor.getString(1));
        }
        cursor.close();
        db.close();
    }

    @SuppressWarnings("unchecked")
    private ArrayList<City> getCityList() {
        DBHelper dbHelper = new DBHelper(this);
        ArrayList<City> list = new ArrayList<City>();
        try {
            dbHelper.createDataBase();
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery("select * from city", null);
            City city;
            while (cursor.moveToNext()) {
                city = new City(cursor.getString(1), cursor.getString(2));
                list.add(city);
            }
            cursor.close();
            db.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Collections.sort(list, comparator);
        return list;
    }

    @SuppressWarnings("unchecked")
    private void getResultCityList(String keyword) {
        city_result.clear();
        DBHelper dbHelper = new DBHelper(this);
        try {
            dbHelper.createDataBase();
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery(
                    "select * from city where name like \"%" + keyword
                            + "%\" or pinyin like \"%" + keyword + "%\"", null);
            City city;
            Log.e("info", "length = " + cursor.getCount());
            while (cursor.moveToNext()) {
                city = new City(cursor.getString(1), cursor.getString(2));
                city_result.add(city);
            }
            cursor.close();
            db.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Collections.sort(city_result, comparator);
    }

    /**
     * a-z排序
     */
    @SuppressWarnings("rawtypes")
    Comparator comparator = new Comparator<City>() {
        @Override
        public int compare(City lhs, City rhs) {
            String a = lhs.getPinyi().substring(0, 1);
            String b = rhs.getPinyi().substring(0, 1);
            int flag = a.compareTo(b);
            if (flag == 0) {
                return a.compareTo(b);
            } else {
                return flag;
            }
        }
    };


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String content = s.toString().trim();
        // XA 安 杭      char 1 2
        if (!TextUtils.isEmpty(content) && content.length() > 0) {
//            String c = PingYinUtil.getPingYin(content);
//            List<City> resultList = new ArrayList<>();
//            for (City city : allCity_lists) {
//                String pinyin = city.getPinyi();
//                if (!TextUtils.isEmpty(pinyin) && pinyin.contains(c)) {
//                    if (pinyin.equals(c)) {
//                        resultList.clear();
//                        resultList.add(city);
//                        break;
//                    }
//                    resultList.add(city);
//                }
//            }
            getResultCityList(content);
            if (searchAdapter == null) {
                searchAdapter = new SearchResultAdapter(city_result);
            } else {
                searchAdapter.updateItems(city_result);
            }
            personList.setAdapter(searchAdapter);
        } else {
            personList.setAdapter(adapter);
        }

//        allCity_lists

    }


    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.name:
                Object tagObj = v.getTag();
                if (tagObj != null && tagObj instanceof City) {
                    selectCity(((City) tagObj).getName());
                }
                break;
            case R.id.city_cancel_tv:
                finish();
                break;


        }
    }

    private class SearchResultAdapter extends BaseAdapter {
        List<City> mItems;

        public SearchResultAdapter(List<City> items) {
            mItems = items;
        }

        public void updateItems(List<City> items) {
            mItems = items;
        }

        @Override
        public int getCount() {
            return mItems != null ? mItems.size() : 0;
        }

        @Override
        public Object getItem(int position) {
            return mItems != null ? mItems.get(position) : null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.list_item_city_item, null);
            if (mItems != null && mItems.size() > 0) {
                TextView cityTv = (TextView) convertView.findViewById(R.id.name);
                City item = mItems.get(position);
                cityTv.setTag(item);
                cityTv.setOnClickListener(CityActivity.this);
                cityTv.setText(item.getName());
            }
            return convertView;
        }
    }

    private class ListAdapter extends BaseAdapter {
        private List<City> list;
        private List<City> hotList;
        final int VIEW_TYPE = 5;

        public ListAdapter(List<City> list,
                           List<City> hotList) {
            this.list = list;
            this.hotList = hotList;
            alphaIndexer = new HashMap<>();
            sections = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                // 当前汉语拼音首字母
                String currentStr = getAlpha(list.get(i).getPinyi());
                // 上一个汉语拼音首字母，如果不存在为" "
                String previewStr = (i - 1) >= 0 ? getAlpha(list.get(i - 1)
                        .getPinyi()) : " ";
                if (!previewStr.equals(currentStr)) {
                    String name = getAlpha(list.get(i).getPinyi());
                    alphaIndexer.put(name, i);
                    sections[i] = name;
                }
            }
        }

        @Override
        public int getViewTypeCount() {
            return VIEW_TYPE;
        }

        @Override
        public int getItemViewType(int position) {
            return position < 2 ? position : 2;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        ViewHolder holder;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final TextView city;
            int viewType = getItemViewType(position);
            if (viewType == 0) { // 定位
                convertView = inflater.inflate(R.layout.list_item_city, null);
                TextView locateHint = (TextView) convertView
                        .findViewById(R.id.locateHint);
                city = (TextView) convertView.findViewById(R.id.lng_city);
                city.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (locateProcess == 2) {
                            selectCity(city.getText().toString());
                        } else if (locateProcess == 3) {
                            locateProcess = 1;
                            personList.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            isNeedFresh = true;
                        }
                    }
                });
                ProgressBar pbLocate = (ProgressBar) convertView
                        .findViewById(R.id.pbLocate);
                if (locateProcess == 1) { // 正在定位
                    locateHint.setText("正在定位");
                    city.setVisibility(View.GONE);
                    pbLocate.setVisibility(View.VISIBLE);
                } else if (locateProcess == 2) { // 定位成功
                    locateHint.setText("当前定位城市:");
                    city.setVisibility(View.VISIBLE);
                    city.setText(currentCity);
//					mLocationClient.stop();
                    pbLocate.setVisibility(View.GONE);
                } else if (locateProcess == 3) {
                    locateHint.setText("未定位到城市,请选择");
                    city.setVisibility(View.VISIBLE);
                    city.setText("重新选择");
                    pbLocate.setVisibility(View.GONE);
                }
            } else if (viewType == 1) {
                convertView = inflater.inflate(R.layout.layout_recent_city, null);
                LinearLayout hotCity = (LinearLayout) convertView
                        .findViewById(R.id.recent_city);
                TextView hotHint = (TextView) convertView
                        .findViewById(R.id.recentHint);
                hotHint.setText("★热门城市");
                for (City item : hotList) {
                    TextView itemView = getItemView(item.getName());
                    itemView.setTag(item);
                    itemView.setOnClickListener(CityActivity.this);
//                      <View
//                    android:layout_height="1dp"
//                    style="@style/CommonLandscapeLine"
//                    android:layout_marginLeft="@dimen/pad1"
//                            />
                    hotCity.addView(itemView);
                    hotCity.addView(getLineView());
                }
            } else {
                AppLog.print("ListAdapter position____" + position);
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.list_item_city_item, null);
                    holder = new ViewHolder();
                    holder.alpha = (TextView) convertView
                            .findViewById(R.id.alpha);
                    holder.name = (TextView) convertView
                            .findViewById(R.id.name);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                if (position >= 2) {
                    City cityItem = list.get(position - 1);
                    holder.name.setTag(cityItem);
                    holder.name.setOnClickListener(CityActivity.this);
                    holder.name.setText(cityItem.getName());
                    String currentStr = getAlpha(list.get(position - 1).getPinyi());
                    int prePos = position - 2;
                    String previewStr = prePos >= 0 ? getAlpha(list
                            .get(prePos).getPinyi()) : " ";
                    if (!previewStr.equals(currentStr)) {
                        holder.alpha.setVisibility(View.VISIBLE);
                        holder.alpha.setText(currentStr);
                    } else {
                        holder.alpha.setVisibility(View.GONE);
                    }
                }
            }
            return convertView;
        }

        private class ViewHolder {
            TextView alpha; // 首字母标题
            TextView name; // 城市名字
        }
    }


    private boolean mReady;

    // 初始化汉语拼音首字母弹出提示框
    private void initOverlay() {
        mReady = true;
//        <TextView xmlns:android="http://schemas.android.com/apk/res/android"
//        android:layout_height="wrap_content"
//        android:background="#a0000000"
//        android:gravity="center"
//        android:text="fasfafa"
//        android:minWidth="65dp"
//        android:padding="10dip"
//        android:textColor="#ffffff"
//        android:textSize="55sp" />
        if (overlay == null) {
            overlay = new TextView(this);
            overlay.setGravity(Gravity.CENTER);
            overlay.setMinWidth(DensityUtil.dip2px(this, 65));
            int left = DensityUtil.dip2px(this, 10);
            overlay.setPadding(left, left, left, left);
            overlay.setTextSize(TypedValue.COMPLEX_UNIT_SP, 55);
            overlay.setBackgroundColor(getResources().getColor(R.color.lightransparent));
            overlay.setTextColor(getResources().getColor(R.color.white));
        }
        overlay.setVisibility(View.INVISIBLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);
        WindowManager windowManager = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.addView(overlay, lp);
    }

    private boolean isScroll = false;

    private class LetterListViewListener implements
            OnTouchingLetterChangedListener {

        @Override
        public void onTouchingLetterChanged(final String s) {
            isScroll = false;
            if (alphaIndexer.get(s) != null) {
                int position = alphaIndexer.get(s);
                personList.setSelection(position);
                overlay.setText(s);
                overlay.setVisibility(View.VISIBLE);
                handler.removeCallbacks(overlayThread);
                // 延迟一秒后执行，让overlay为不可见
                handler.postDelayed(overlayThread, 1000);
            }
        }
    }

    // 设置overlay不可见
    private class OverlayThread implements Runnable {
        @Override
        public void run() {
            overlay.setVisibility(View.GONE);
        }
    }

    // 获得汉语拼音首字母
    private String getAlpha(String str) {
        if (str == null) {
            return "#";
        }
        if (str.trim().length() == 0) {
            return "#";
        }
        char c = str.trim().substring(0, 1).charAt(0);
        // 正则表达式，判断首字母是否是英文字母
        Pattern pattern = Pattern.compile("^[A-Za-z]+$");
        if (pattern.matcher(c + "").matches()) {
            return (c + "").toUpperCase();
        } else if (str.equals("2")) {
            return "★";
        } else {
            return "#";
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_TOUCH_SCROLL
                || scrollState == SCROLL_STATE_FLING) {
            isScroll = true;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        if (!isScroll) {
            return;
        }

        if (mReady) {
            String text;
            String name = allCity_lists.get(firstVisibleItem).getName();
            String pinyin = allCity_lists.get(firstVisibleItem).getPinyi();
            if (firstVisibleItem < 4) {
                text = name;
            } else {
                text = PingYinUtil.converterToFirstSpell(pinyin)
                        .substring(0, 1).toUpperCase();
            }
            overlay.setText(text);
            overlay.setVisibility(View.VISIBLE);
            handler.removeCallbacks(overlayThread);
            // 延迟一秒后执行，让overlay为不可见
            handler.postDelayed(overlayThread, 1000);
        }
    }

    private void selectCity(String city) {
        Intent intent = new Intent();
        intent.putExtra(Constants.P_CITYNAME, city);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        destroyLocation();
        super.onDestroy();
    }

    private void initLocation() {
        //初始化client
        locationClient = new AMapLocationClient(this.getApplicationContext());
        //设置定位参数
        locationClient.setLocationOption(getDefaultOption());
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
        locationClient.startLocation();
    }

    private void stopLocation() {
        // 停止定位
        locationClient.stopLocation();
    }

    private void destroyLocation() {
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
//			locationClient.stopAssistantLocation();
            stopLocation();
            locationClient.onDestroy();
            locationClient = null;
        }
    }

    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是ture
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        return mOption;
    }

    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation loc) {
            if (null != loc) {
                //解析定位结果
                if (!isNeedFresh) {
                    return;
                }
                isNeedFresh = false;
                if (loc.getCity() == null) {
                    locateProcess = 3; // 定位失败
                    personList.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    return;
                }
                currentCity = loc.getCity().substring(0,
                        loc.getCity().length() - 1);
                locateProcess = 2; // 定位成功
                personList.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                stopLocation();


            } else {
//				tvReult.setText("定位失败，loc is null");
            }
        }
    };


    private View getLineView() {
        View lineView=new View(CityActivity.this);
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, DensityUtil.dip2px(CityActivity.this,0.5f));
        lineView.setBackgroundColor(getResources().getColor(R.color.cor12));
        params.leftMargin=DensityUtil.dip2px(this,10);
        lineView.setLayoutParams(params);
        return lineView;
    }



    public TextView getItemView(String title) {
        TextView convertView = new TextView(this);
        convertView.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.dimen_size_44)));
        convertView.setBackgroundColor(getResources().getColor(R.color.white));
        convertView.setPadding((int) getResources().getDimension(R.dimen.pad2), 0, 0, 0);
        convertView.setGravity(Gravity.CENTER_VERTICAL);
        convertView.setTextColor(getResources().getColor(R.color.cor8));
        convertView.setTextSize(TypedValue.COMPLEX_UNIT_PX, 48);
        convertView.setText(title);
        convertView.setId(R.id.name);
        return convertView;

    }


}