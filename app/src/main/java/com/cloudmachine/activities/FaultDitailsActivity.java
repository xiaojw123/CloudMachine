package com.cloudmachine.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.adapter.SensorListAdapter;
import com.cloudmachine.autolayout.widgets.TitleView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.struc.AgentInfo;
import com.cloudmachine.struc.FaultWarnInfo;
import com.cloudmachine.struc.FaultWarnListInfo;
import com.cloudmachine.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class FaultDitailsActivity extends BaseAutoLayoutActivity implements OnClickListener,Callback
,OnItemClickListener{

	private Context mContext;
	private Handler mHandler;
	private TitleView title_layout;
	private ListView listView;
	private SensorListAdapter sAdapter;
	private List<FaultWarnListInfo> dataResult = new ArrayList<FaultWarnListInfo>();
	private long deviceId;  
	private LinearLayout  footerViewLayout;
	private View list_title_layout;
	private long collectionDate;
	private FaultWarnInfo faultWarnInfo;
	private FaultWarnListInfo faultWarnListInfo;
	private TextView fault_time;
	private String telephone ;
	private String deviceName = "";
	private int mWorkState;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fault_ditails);
		mContext = this;
		mHandler = new Handler(this);
		getIntentData();
		initView();
//		new AgentInfoAsync(deviceId,mContext,mHandler).execute();
//		new FaultInfoAsync(collectionDate,deviceId,mContext,mHandler).execute();
	}

	@Override
	public void initPresenter() {

	}

	private void getIntentData(){
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
        	try{
        		deviceId = bundle.getLong(Constants.P_DEVICEID);
				mWorkState = bundle.getInt(Constants.P_WORKSTATES);
				deviceName = bundle.getString(Constants.P_DEVICENAME);
        		faultWarnInfo = (FaultWarnInfo)bundle.getSerializable(Constants.P_DEVICEINFO_faultWarnInfo);
        		if(null != faultWarnInfo)
        			collectionDate = faultWarnInfo.getCheckDate();
        	}catch(Exception e){
        		Constants.MyLog(e.getMessage());
        	}
        	
        }
    }

	@Override
	protected void onResume() {
		//MobclickAgent.onPageStart(UMengKey.time_machine_report);
		super.onResume();
	}


	@Override
	protected void onPause() {
		//MobclickAgent.onPageEnd(UMengKey.time_machine_report);
		super.onPause();
	}

	private void initView(){
		initTitleLayout();
		list_title_layout = findViewById(R.id.list_title_layout);
		TextView name = (TextView)list_title_layout.findViewById(R.id.fault_name);
		name.setTextColor(getResources().getColor(R.color.public_blue_bg));
//		name.setTextSize(getResources().getDimension(R.dimen.timesize));
		name.setText("检验项目");
		TextView range = (TextView)list_title_layout.findViewById(R.id.fault_range);
		range.setTextColor(getResources().getColor(R.color.public_blue_bg));
//		range.setTextSize(getResources().getDimension(R.dimen.timesize));
		range.setText("参考范围");
		TextView date = (TextView)list_title_layout.findViewById(R.id.fault_date);
		date.setTextColor(getResources().getColor(R.color.public_blue_bg));
//		date.setTextSize(getResources().getDimension(R.dimen.timesize));
		date.setText("结果");
		View bView =list_title_layout.findViewById(R.id.right_layout);
		bView.setBackgroundColor(Color.parseColor("#f7fcfb"));
		
		footerViewLayout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.sensor_list_footer, null);
		fault_time = (TextView)footerViewLayout.findViewById(R.id.censor_action_text);
		fault_time.setText(getResources().getString(R.string.mc_collect_time) + faultWarnInfo.getPastTime());
		listView = (ListView)findViewById(R.id.sensor_listview);
//		listView.setParentScrollView(pScroll);
		sAdapter = new SensorListAdapter(mContext, dataResult, mHandler,true);
		
//		footerView = LayoutInflater.from(this).inflate(R.layout.view_sensor_add,null);
//		footerAdd = (ImageView)(footerView.findViewById(R.id.buttonAdd));
//		footerAdd.setOnClickListener(this);
		listView.setOnItemClickListener(this);
		listView.addFooterView(footerViewLayout,null,false);
		listView.setAdapter(sAdapter);
		
		
		if(null != faultWarnInfo){
			dataResult.clear();
			dataResult.addAll(faultWarnInfo.getReList());
			sAdapter.notifyDataSetChanged();
		}else{
			finish();
		}
	}
	private void initTitleLayout(){
		title_layout = (TitleView)findViewById(R.id.title_layout);
		title_layout.setTitle(deviceName);
		title_layout.setLeftOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 finish(); 
			}
		});
	}
	
	
	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		switch(msg.what){
		case Constants.HANDLER_FAULTINFO_SUCCESS:
			faultWarnInfo = (FaultWarnInfo)msg.obj;
			if(null != faultWarnInfo){
//				fault_desc.setText(null!=faultWarnInfo.getFaultDesc()?faultWarnInfo.getFaultDesc():"");
				dataResult.clear();
				dataResult.addAll(faultWarnInfo.getReList());
				sAdapter.notifyDataSetChanged();
			}else{
				Constants.ToastAction((String)msg.obj);
				finish();
			}
			
			break;
		case Constants.HANDLER_FAULTINFO_FAIL:
			Constants.ToastAction((String)msg.obj);
			finish();
			break;
		case Constants.HANDLER_GETAGENTS_SUCCESS:
			telephone = ((AgentInfo)msg.obj).getTelephone();
			break;
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if(!dataResult.get(position).getBexcess()){
			Bundle b = new Bundle();
			b.putLong(Constants.P_DEVICEID, deviceId);
			b.putLong(Constants.P_DEVICEINFO_collectionDate, collectionDate);
			b.putInt(Constants.P_ID, dataResult.get(position).getId());
			b.putString(Constants.P_DEVICENAME, dataResult.get(position).getItem());
			Constants.toActivity(this, FaultDitailsListActivity.class, b, false);
		}
	}

}
