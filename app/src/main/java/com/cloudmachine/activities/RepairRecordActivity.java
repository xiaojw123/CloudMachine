package com.cloudmachine.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;

import com.cloudmachine.R;
import com.cloudmachine.adapter.RepairListAdapter;
import com.cloudmachine.autolayout.widgets.RadiusButtonView;
import com.cloudmachine.autolayout.widgets.TitleView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.net.task.GetRepairListByIdAsync;
import com.cloudmachine.struc.FinishBean;
import com.cloudmachine.struc.McDeviceInfo;
import com.cloudmachine.struc.RepairHistoryInfo;
import com.cloudmachine.struc.RepairListInfo;
import com.cloudmachine.struc.UnfinishedBean;
import com.cloudmachine.ui.repair.activity.NewRepairActivity;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.widgets.XListView;
import com.cloudmachine.utils.widgets.XListView.IXListViewListener;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

/**
 * 
 * @author shixionglu 主页面的维修记录
 */
public class RepairRecordActivity extends BaseAutoLayoutActivity implements
		Callback, IXListViewListener, OnItemClickListener {

	private long deviceId;
	private int deviceType;
	private Context context;
	private Handler handler;

	private View noRepairRecord, have_repair_records;
	private RadiusButtonView btnRepairNow;
	private XListView xlvComplete;
	// private View completedHeaderView;
	private View uncompleteView;
	// private XListView xlvUnComplete;

	private RepairListAdapter repairListAdapter;
	private RelativeLayout rlRepairNow;
	private RadiusButtonView btnBottomRepair;
	private View viewParent;
	private TitleView title_layout;
	private RepairListInfo repairListInfo;
	private ArrayList<FinishBean> finishBeans = new ArrayList<>(); // 已完成集合
	private ArrayList<UnfinishedBean> unfinishedBeans = new ArrayList<>();// 未完成集合
	private ArrayList<RepairHistoryInfo> repairList = new ArrayList<>();// 合并两个集合
	private McDeviceInfo  mcDeviceInfo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_repair_record);
		context = this;
		handler = new Handler(this);
		getIntentData();
		initView();
//		new AllRepairHistoryAsync(context, handler).execute();
		
	}

	@Override
	public void initPresenter() {

	}

	private void initView() {

		initTitleLayout();
		noRepairRecord = findViewById(R.id.no_repair_records); // 无维修记录布局
		have_repair_records = findViewById(R.id.have_repair_records); // 有维修记录布局
		btnRepairNow = (RadiusButtonView) 
				findViewById(R.id.btn_repair_now); // 立刻报修
		btnRepairNow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gotoNewRepair();
			}
		});

		xlvComplete = (XListView) findViewById(R.id.xlv_complete); // 已完成的listview
		repairListAdapter = new RepairListAdapter(context, repairList);
		xlvComplete.setPullRefreshEnable(false);
		xlvComplete.setPullLoadEnable(false);
		xlvComplete.setXListViewListener(this);
		xlvComplete.setAdapter(repairListAdapter);
		xlvComplete.setOnItemClickListener(this);
		rlRepairNow = (RelativeLayout) findViewById(R.id.rl_repair_now);
		btnBottomRepair = (RadiusButtonView) findViewById(R.id.btn_bottom_repair);
		btnBottomRepair.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				gotoNewRepair();
			}
		});

		if(Constants.isNoEditInMcMember(deviceId, deviceType)){
			btnRepairNow.setVisibility(View.GONE);
			rlRepairNow.setVisibility(View.GONE);
			
		}
		
		showData();
	}
	
	private void gotoNewRepair(){
		Bundle bundle = new Bundle();
		bundle.putSerializable(Constants.P_DEVICEINFO_MY, mcDeviceInfo);
		Constants.toActivity(RepairRecordActivity.this,
				NewRepairActivity.class, bundle);
	}

	private void showData() {
		if ((null != repairList && repairList.size() > 0)) {
			have_repair_records.setVisibility(View.VISIBLE);
			noRepairRecord.setVisibility(View.GONE);
		} else {
			have_repair_records.setVisibility(View.GONE);
			noRepairRecord.setVisibility(View.VISIBLE);
		}

	}

	private void initTitleLayout() {

		title_layout = (TitleView) findViewById(R.id.title_layout);
		title_layout.setTitle(getResources().getString(
				R.string.main3_title_text)); 
		title_layout.setLeftOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});

	}

	private void getIntentData(){
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
        	try{
        		mcDeviceInfo = (McDeviceInfo)bundle.getSerializable(Constants.MC_DEVICEINFO);
				deviceType=mcDeviceInfo.getType();
				deviceId=mcDeviceInfo.getId();
//        		deviceType =bundle.getInt(Constants.P_DEVICETYPE);
        	}catch(Exception e){
        		Constants.MyLog(e.getMessage());
        	}
        	
        }
    }

	@Override
	public boolean handleMessage(Message msg) {

		switch (msg.what) {
		case Constants.HANDLER_GET_REPAIRHISTORY_SUCCESS:
			xlvComplete.stopRefresh();
			xlvComplete.stopLoadMore();
			finishBeans.clear();
			unfinishedBeans.clear();
			repairList.clear();
			if (null != msg.obj) {
				noRepairRecord.setVisibility(View.GONE);
				xlvComplete.setVisibility(View.VISIBLE);
				repairListInfo = (RepairListInfo) msg.obj;
				ArrayList<FinishBean> finish = repairListInfo.getFinish();
				ArrayList<UnfinishedBean> unfinished = repairListInfo
						.getUnfinished();
				if (null != finish) {
					finishBeans.addAll(finish);
				}
				if (null != unfinished) {
					unfinishedBeans.addAll(unfinished);
				}
				int count = finishBeans.size() + unfinishedBeans.size();
				int finishCount = 0;
				for (int i = 0; i < count; i++) {
					RepairHistoryInfo historyInfo = new RepairHistoryInfo();
					if (i < unfinishedBeans.size()) {
						historyInfo.setLogo_flag(unfinishedBeans.get(i).getLogo_flag());
						historyInfo.setFlag(unfinishedBeans.get(i).getFlag());
						historyInfo.setPrice(unfinishedBeans.get(i).getPrice());
						historyInfo.setDopportunity(unfinishedBeans.get(i)
								.getDopportunity());
						historyInfo.setVmachinenum(unfinishedBeans.get(i)
								.getVmachinenum());
						historyInfo.setVbrandname(unfinishedBeans.get(i)
								.getVbrandname());
						historyInfo.setVmaterialname(unfinishedBeans.get(i)
								.getVmaterialname());
						historyInfo.setVmacopname(unfinishedBeans.get(i)
								.getVmacopname());
						historyInfo.setVdiscription(unfinishedBeans.get(i)
								.getVdiscription());
						historyInfo.setIs_EVALUATE(unfinishedBeans.get(i)
								.getIs_EVALUATE());
						historyInfo.setVprodname(unfinishedBeans.get(i)
								.getVprodname());
						historyInfo.setVmacoptel(unfinishedBeans.get(i)
								.getVmacoptel());
						historyInfo.setNstatus(unfinishedBeans.get(i)
								.getNstatus());
						historyInfo.setOrderNum(unfinishedBeans.get(i)
								.getOrderNum());
					} else {
						historyInfo.setLogo_flag(finishBeans.get(finishCount).getLogo_flag());
						historyInfo.setFlag(finishBeans.get(finishCount)
								.getFlag());
						historyInfo.setPrice(finishBeans.get(finishCount)
								.getPrice());
						historyInfo.setDopportunity(finishBeans
								.get(finishCount).getDopportunity());
						historyInfo.setVmachinenum(finishBeans.get(finishCount)
								.getVmachinenum());
						historyInfo.setVbrandname(finishBeans.get(finishCount)
								.getVbrandname());
						historyInfo.setVmaterialname(finishBeans.get(
								finishCount).getVmaterialname());
						historyInfo.setVmacopname(finishBeans.get(finishCount)
								.getVmacopname());
						historyInfo.setVdiscription(finishBeans
								.get(finishCount).getVdiscription());
						historyInfo.setIs_EVALUATE(finishBeans.get(finishCount)
								.getIs_EVALUATE());
						historyInfo.setVprodname(finishBeans.get(finishCount)
								.getVprodname());
						historyInfo.setVmacoptel(finishBeans.get(finishCount)
								.getVmacoptel());
						historyInfo.setNstatus(finishBeans.get(finishCount)
								.getNstatus());
						historyInfo.setOrderNum(finishBeans.get(finishCount)
								.getOrderNum());
						finishCount++;
					}
					repairList.add(historyInfo);
				}
				showData();
				repairListAdapter.setUnFinishedNum(unfinishedBeans.size());
				repairListAdapter.notifyDataSetChanged();
			} else {
				xlvComplete.setVisibility(View.GONE);
				noRepairRecord.setVisibility(View.VISIBLE);
				btnBottomRepair.setVisibility(View.GONE);
			}
			break;
		case Constants.HANDLER_GET_REPAIRHISTORY_FAILD:
			showData();
			break;
		default:
			break;
		}
		return false;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// Constants.MyLog("获取焦点");
		new GetRepairListByIdAsync(context, handler, deviceId).execute();
		MobclickAgent.onPageStart(this.getClass().getName());
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		int temp = position-1;
		if(null != repairList && repairList.size() > 0			//跳转单个报修详情页面
				) {
			
			if (temp < unfinishedBeans.size()) {
				String orderNum = unfinishedBeans.get(temp).getOrderNum();
				Intent intent = new Intent(RepairRecordActivity.this,RepairBasicInfomationActivity.class);
				intent.putExtra("orderNum", orderNum);
				intent.putExtra("flag", unfinishedBeans.get(temp).getFlag());
				startActivity(intent);
			}else {
				int p = temp - unfinishedBeans.size();
				if(finishBeans.get(p).getIs_EVALUATE().equals("N")){
					Intent intent = new Intent(RepairRecordActivity.this,EvaluationActivity.class);
					intent.putExtra("orderNum", finishBeans.get(p).getOrderNum());
					intent.putExtra("tel", finishBeans.get(p).getVmacoptel());
					startActivity(intent);
				}
			}
			
		}
	}
	@Override
	public void onRefresh() {
		new GetRepairListByIdAsync(context, handler, deviceId);

	}

	@Override
	public void onLoadMore() {

	}

//	@Override
//	public void onClick(View v) {
//		// TODO Auto-generated method stub
//		switch (v.getId()) {
//		case R.id.btn_bottom_repair:
//			Constants.toActivity(RepairRecordActivity.this, NewRepairActivity.class, null);
//			break;
//
//		default:
//			break;
//		}
//	}
}
