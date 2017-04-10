package com.cloudmachine.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;

import com.cloudmachine.R;
import com.cloudmachine.adapter.MemberSlideAdapter;
import com.cloudmachine.autolayout.widgets.ListViewCompat;
import com.cloudmachine.autolayout.widgets.RadiusButtonView;
import com.cloudmachine.autolayout.widgets.SlideView;
import com.cloudmachine.autolayout.widgets.TitleView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.net.task.DevicesMemberListAsync;
import com.cloudmachine.struc.MemberInfo;
import com.cloudmachine.struc.MemberInfoSlide;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.UMengKey;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

public class DeviceMcMemberActivity extends BaseAutoLayoutActivity implements Callback, OnClickListener{

	private Context mContext;
	private Handler mHandler;
	private long deviceId;
	private int deviceType;
	private ListViewCompat member_listview;
	private MemberSlideAdapter pAdapter;
	private List<MemberInfoSlide> dataResult = new ArrayList<MemberInfoSlide>();
	private int quartersId;
	private RadiusButtonView add_member;
	private View empty_layout;
	private SlideView mLastSlideViewWithStatusOn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_device_mc_member);
		mContext = this;
		mHandler = new Handler(this);
		getIntentData();
		initView();
		getMemberList();
	}

	@Override
	public void initPresenter() {

	}

	private void getIntentData(){
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
        	try{
        		deviceId =bundle.getLong(Constants.P_DEVICEID); 
        		deviceType =bundle.getInt(Constants.P_DEVICETYPE); 
        	}catch(Exception e){
        		Constants.MyLog(e.getMessage());
        	}
        	
        }
    }

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	private void initView(){
		initTitleLayout();
		empty_layout  = findViewById(R.id.empty_layout);
		pAdapter = new MemberSlideAdapter(dataResult,deviceId, deviceType, mContext,mHandler);
		member_listview =  (ListViewCompat)findViewById(R.id.member_listview);
		member_listview.setAdapter(pAdapter);
		
		add_member = (RadiusButtonView)findViewById(R.id.add_member);
		if(!Constants.isNoEditInMcMember(deviceId, deviceType)){
			add_member.setVisibility(View.VISIBLE);
			add_member.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					MobclickAgent.onEvent(mContext, UMengKey.count_memeber_add);
					gotoSearch();
				}
			});
		}else{
			add_member.setVisibility(View.GONE);
		}
		
	}
	
	private void initTitleLayout(){
		TitleView title_layout;
		title_layout = (TitleView)findViewById(R.id.title_layout);
		title_layout.setTitle(getResources().getString(R.string.mc_member_title));
		title_layout.setLeftOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DeviceMcMemberActivity.this.finish();
			}
		});
		
		if(!Constants.isNoEditInMcMember(deviceId, deviceType)){
			title_layout.setRightText(-1, getResources().getString(R.string.mc_member_add),  new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					gotoSearch();
				}
			});
			title_layout.setRightTextEdit(true);
		}
		
		
	}

	private void gotoSearch(){
		Bundle bundle = new Bundle();
		bundle.putInt(Constants.P_SEARCHLISTTYPE, 1);
		bundle.putLong(Constants.P_DEVICEID, deviceId);
		Constants.toActivity(this, SearchActivity.class, bundle);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		}
		
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		switch(msg.what){
		case Constants.HANDLER_GETDEVICEMEMBER_SUCCESS:
			List<MemberInfo> tM = (List<MemberInfo>)msg.obj;
			if(null !=tM){
				dataResult.clear();
				for(int i=0; i<tM.size(); i++){
					MemberInfoSlide minfo = new MemberInfoSlide();
					minfo.setMemberId(tM.get(i).getMemberId());
					minfo.setName(tM.get(i).getName());
					minfo.setRoleRemark(tM.get(i).getRoleRemark());
					minfo.setRole(tM.get(i).getRole());
					minfo.setMiddlelogo(tM.get(i).getMiddlelogo());
					dataResult.add(minfo);
				}
//				dataResult.addAll(tM);
				if(null != dataResult && dataResult.size()>0){
					empty_layout.setVisibility(View.GONE);
				}
				pAdapter.notifyDataSetChanged();
				tM = null;
			}
			
			break;
		case Constants.HANDLER_GETDEVICEMEMBER_FAIL:
			
			break;
		case Constants.HANDLER_DELETEMEMBER_SUCCESS:
			Constants.MyToast((String)msg.obj);
			getMemberList();
			break;
		case Constants.HANDLER_DELETEMBER_FAIL:
			Constants.MyToast((String)msg.obj);
			break;
		
		}
		return false;
	}
/*
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
		MemberInfo memberInfo = dataResult.get(position);
		Intent intent_h = new Intent(this, PermissionActivity.class);
		overridePendingTransition(R.anim.slide_right_in,
				R.anim.slide_left_out);
		intent_h.putExtra(Constants.P_DEVICEID, deviceId);
		intent_h.putExtra(Constants.P_DEVICETYPE, deviceType);
		intent_h.putExtra(Constants.P_PERMISSIONTYPE, 0);
		intent_h.putExtra(Constants.P_MERMBERINFO, memberInfo);
		
		intent_h.putExtra(Constants.P_MERMBERID, memberInfo.getMemberId());
		intent_h.putExtra(Constants.P_MERMBERNAME, memberInfo.getName());
		intent_h.putExtra(Constants.P_MERMBERROLE, memberInfo.getRole());
		intent_h.putExtra(Constants.P_MERMBERROLEREMARK, memberInfo.getRoleRemark());
		intent_h.putExtra(Constants.P_MERMBERPERMISSION, memberInfo.getPermissName());
		startActivityForResult(intent_h,0);
	}*/
	private void getMemberList(){
//		if(deviceId != Constants.MC_Simulation_DeviceId){
//			new DevicesMemberListAsync(deviceId,mContext,mHandler).execute();
//		}
		new DevicesMemberListAsync(deviceId,mContext,mHandler).execute();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (resultCode)
		{
		case RESULT_OK:
				Bundle bundle=data.getExtras();  
				if(bundle.getBoolean(Constants.P_ISDELETEMERMBER))
					getMemberList();
			break;
		}
	}
	


	
}
