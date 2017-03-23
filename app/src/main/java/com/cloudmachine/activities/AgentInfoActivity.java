package com.cloudmachine.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.adapter.RepairRecordListAdapter;
import com.cloudmachine.base.BaseActivity;
import com.cloudmachine.net.task.AgentInfoAsync;
import com.cloudmachine.net.task.RepairRecordListAsync;
import com.cloudmachine.struc.AgentInfo;
import com.cloudmachine.struc.RepairRecordInfo;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.widgets.TitleView;

import java.util.ArrayList;
import java.util.List;

public class AgentInfoActivity extends BaseActivity implements Callback,OnClickListener{

	private Handler mHandler;
	private Context mContext; 
	private long deviceId;
	private TitleView title_layout;
	private TextView agent_name,agent_user_name;
	private TextView rrTitle;
	private String telephone ;
	private ImageView call_phone;
	private ListView agent_listview;
	private RepairRecordListAdapter rrAdapter;
	private List<RepairRecordInfo> dataResult = new ArrayList<RepairRecordInfo>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agentinfo);
		mContext = this;
		mHandler = new Handler(this);
		getIntentData();
		
		call_phone =  (ImageView)findViewById(R.id.call_phone);
		call_phone.setOnClickListener(this);
		
		rrTitle =  (TextView)findViewById(R.id.rrTitle);
		rrTitle.setVisibility(View.GONE);
		
		agent_name =  (TextView)findViewById(R.id.agent_name);
		agent_user_name =  (TextView)findViewById(R.id.agent_user_name);
		
		rrAdapter = new RepairRecordListAdapter(dataResult,mContext,mHandler);
		agent_listview =  (ListView)findViewById(R.id.agent_listview);
		agent_listview.setAdapter(rrAdapter);
		
		title_layout = (TitleView)findViewById(R.id.title_layout);
		title_layout.setTitle("服务商");
		
		title_layout.setLeftImage(-1, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 finish(); 
			}
		});
		
		new AgentInfoAsync(deviceId,mContext,mHandler).execute();
		new RepairRecordListAsync(deviceId, mContext, mHandler).execute();
	}

	
	private void getIntentData(){
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
        	try{
        		deviceId = bundle.getLong(Constants.P_DEVICEID);
        	
        	}catch(Exception e){
        		Constants.MyLog(e.getMessage());
        	}
        }
    }


	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		switch(msg.what){
		case Constants.HANDLER_GETAGENTS_SUCCESS:
			AgentInfo aginfo = (AgentInfo)msg.obj;
			
			agent_name.setText(Constants.getNotNullString(aginfo.getName()));
			agent_user_name.setText(Constants.getNotNullString(aginfo.getUserName()));
			telephone = aginfo.getTelephone();
			
			break;
		case Constants.HANDLER_GETAGENTS_FAIL:
			Constants.ToastAction((String)msg.obj);
			/*String strMessage2 = "";
//			strMessage2 = "名称："+"\r\n手机号码："+"\r\n座机："
//			+"\r\n邮箱："+"\r\n传真：";
			AgentInfo info = new AgentInfo();
			info.setName("浙江聚励云机械科技有限公司");
			info.setTelephone("13854387438");
			info.setLandline("0517-86741356");
			info.setMail("support@cloudm.com");
			info.setFax("0517-86741356");
			strMessage2 = "名称："+info.getName()+"\r\n电话："+info.getTelephone()+"\r\n座机："
					+info.getLandline()+"\r\n邮箱："+info.getMail()+"\r\n传真："+info.getFax();
			message.setText(strMessage2);*/
			break;
		case Constants.HANDLER_REPAIRRECORD_SUCCESS:
			
			dataResult.clear();
			dataResult.addAll((List<RepairRecordInfo>)msg.obj);
			if(dataResult.size()>0){
				rrTitle.setVisibility(View.VISIBLE);
			}
			rrAdapter.notifyDataSetChanged();
			break;
		case Constants.HANDLER_REPAIRRECORD_FAIL:
			
			break;
			
		}
		return false;
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.call_phone:
			if(!TextUtils.isEmpty(telephone)){
				Constants.TEL(this, telephone);
			}else{
				Constants.ToastAction(getResources().getString(R.string.agent_no_phone));
			}
			break;
		}
	}
	
}
