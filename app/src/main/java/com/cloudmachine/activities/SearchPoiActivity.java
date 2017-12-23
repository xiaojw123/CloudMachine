package com.cloudmachine.activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.cloudmachine.R;
import com.cloudmachine.adapter.SearchPoiListAdapter;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.bean.ResidentAddressInfo;
import com.cloudmachine.chart.utils.AppLog;
import com.cloudmachine.helper.DataSupportManager;
import com.cloudmachine.helper.MobEvent;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.UMengKey;
import com.umeng.analytics.MobclickAgent;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class SearchPoiActivity extends BaseAutoLayoutActivity implements OnClickListener, Callback, TextWatcher
        , OnItemClickListener, OnPoiSearchListener {

    private Context mContext;
    private Handler mHandler;
    private View city_layout;
    private TextView right_text, city_text;
    private EditText site_edit;
    private ListView search_listview;
    private SearchPoiListAdapter adapter;
    private List<ResidentAddressInfo> dataResult = new ArrayList<ResidentAddressInfo>();
    private PoiResult poiResult; // poi返回的结果
    private int currentPage = 0;// 当前页面，从0开始计数
    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;// POI搜索
    private View map_choose_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_poi);
        MobclickAgent.onEvent(this, MobEvent.TIME_REPAIR_CREATE_LOCATION);
        mContext = this;
        mHandler = new Handler(this);
        getIntentData();
        initView();
    }

    @Override
    public void initPresenter() {

    }

    private void getIntentData() {

    }

    private void initView() {
        map_choose_layout = findViewById(R.id.map_choose_layout);
        map_choose_layout.setOnClickListener(this);
        city_layout = findViewById(R.id.city_layout);
        city_text = (TextView) findViewById(R.id.city_text);
        right_text = (TextView) findViewById(R.id.right_text);
        site_edit = (EditText) findViewById(R.id.site_edit);
        city_layout.setOnClickListener(this);
        right_text.setOnClickListener(this);
        site_edit.addTextChangedListener(this);
//        site_edit.setOnEditorActionListener(actionListener);
        site_edit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(site_edit.getWindowToken(), 0);
                    }
                }
                return false;
            }

        });
        List<ResidentAddressInfo> items = DataSupportManager.findAll(ResidentAddressInfo.class);
        if (items != null && items.size() > 0) {
            dataResult = items;
        }
        adapter = new SearchPoiListAdapter(dataResult, mContext);
        search_listview = (ListView) findViewById(R.id.search_listview);
        search_listview.setOnItemClickListener(this);
        search_listview.setAdapter(adapter);
    }


    private TextView.OnEditorActionListener actionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(site_edit.getWindowToken(), 0);
            }
            return false;
        }
    };


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.city_layout:
                Bundle bundle = new Bundle();
                bundle.putString(Constants.P_CITYNAME, city_text.getText().toString());
                Constants.toActivityForR(this, CityActivity.class, bundle, Constants.REQUEST_SELECTCITY);
                break;
            case R.id.map_choose_layout:
//			Constants.toActivityForR(this, MapChooseActivity.class, null,Constants.REQUEST_MAPCHOOSE);
                break;
            case R.id.right_text:
//			Intent intent=new Intent();  
//			intent.putExtra(Constants.P_SEARCHINFO, null);  
//			setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // TODO Auto-generated method stub
        //搜索


        if (s.toString().length() > 0) {
            doSearchQuery(s.toString());
        } else {
            dataResult.clear();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        ResidentAddressInfo info = dataResult.get(position);
        List<ResidentAddressInfo> infoList = DataSupportManager.findAll(ResidentAddressInfo.class);
        if (infoList == null) {
            infoList = new ArrayList<>();
        }
        boolean isFlag = true;
        for (ResidentAddressInfo i : infoList) {
            String pos = i.getPosition();
            if (pos != null && pos.equals(info.getPosition())) {
//                DataSupport.deleteAll(ResidentAddressInfo.class, "position=?", pos);
//                infoList = DataSupport.findAll(ResidentAddressInfo.class);
//                infoList.remove(i);
                isFlag = false;
            }
        }
        if (isFlag) {
            infoList.add(info);
        }
//        DataSupport.deleteAll(ResidentAddressInfo.class);
        DataSupport.saveAll(infoList);
        Intent intent = new Intent();
        intent.putExtra(Constants.P_SEARCHINFO, info);
        setResult(RESULT_OK, intent);
        finish();

    }


    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        // TODO Auto-generated method stub
        AppLog.print("onPoiSearched_____" + result + ",   code__" + rCode);
        if (rCode == 1000) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    poiResult = result;
                    // 取得搜索到的poiitems有多少页
                    List<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    List<SuggestionCity> suggestionCities = poiResult
                            .getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息


                    if (poiItems != null && poiItems.size() > 0) {
                        int len = poiItems.size();
                        dataResult.clear();
                        for (int i = 0; i < len; i++) {
                            ResidentAddressInfo info = new ResidentAddressInfo();
                            PoiItem item = poiItems.get(i);
                            AppLog.print("onPoiSearched____item title_" + item.getTitle());
                            info.setTitle(item.getTitle());
                            info.setPosition(!TextUtils.isEmpty(item.getSnippet()) ? item.getSnippet() : item.getTitle());
                            info.setLng(item.getLatLonPoint().getLongitude());
                            info.setLat(item.getLatLonPoint().getLatitude());
                            info.setProvince(item.getProvinceName());
                            info.setCity(item.getCityName());
                            info.setDistrict(item.getDirection());
                            dataResult.add(info);
                        }
                        adapter.notifyDataSetChanged();
                    } else if (suggestionCities != null
                            && suggestionCities.size() > 0) {
                        showSuggestCity(suggestionCities);
                    } else {
//						ToastUtil.show(PoiKeywordSearchActivity.this,
//								R.string.no_result);
                    }
                }
            } else {
//				ToastUtil.show(PoiKeywordSearchActivity.this,
//						R.string.no_result);
            }
        } else if (rCode == 27) {
//			ToastUtil.show(PoiKeywordSearchActivity.this,
//					R.string.error_network);
        } else if (rCode == 32) {
//			ToastUtil.show(PoiKeywordSearchActivity.this, R.string.error_key);
        } else {
//			ToastUtil.show(PoiKeywordSearchActivity.this, getString(R.string.error_other) + rCode);
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }


    private void showSuggestCity(List<SuggestionCity> cities) {

    }

    protected void doSearchQuery(String keyWord) {
        AppLog.print("doSearchQuery___" + keyWord);
        currentPage = 0;
        query = new PoiSearch.Query(keyWord, "", city_text.getText().toString());// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(20);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页

        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        switch (requestCode) {
            case Constants.REQUEST_SELECTCITY:
                if (null != data && null != data.getExtras()) {
                    city_text.setText(data.getExtras().getString(Constants.P_CITYNAME));
                }
                break;
            case Constants.REQUEST_MAPCHOOSE:
                if (null != data && null != data.getExtras()) {
                    Intent intent = new Intent();
                    intent.putExtra(Constants.P_SEARCHINFO, (ResidentAddressInfo) data.getExtras().getSerializable(Constants.P_SEARCHINFO));
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onPause() {
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(site_edit.getWindowToken(), 0);
        MobclickAgent.onPageEnd(UMengKey.time_repair_create_location);
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
