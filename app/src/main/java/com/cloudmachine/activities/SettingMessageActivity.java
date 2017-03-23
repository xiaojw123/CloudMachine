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
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.cloudmachine.R;
import com.cloudmachine.app.MyApplication;
import com.cloudmachine.base.BaseActivity;
import com.cloudmachine.net.task.UpdateMemberInfoAsync;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MemeberKeeper;
import com.cloudmachine.utils.widgets.TitleView;
import com.cloudmachine.utils.widgets.UISwitchButton;
import com.cloudmachine.utils.widgets.Dialog.MyDialog;

public class SettingMessageActivity extends BaseActivity implements OnClickListener, Callback{

	private Context mContext;
	private Handler mHandler;
	private TitleView title_layout_set;
	private UISwitchButton switchButton;
	private int reKgStatus ;//0开启  1关闭
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_message);
		 
		mContext = this;
		mHandler = new Handler(this);
		initView();

	}

	private void initView() {
		//set_clear=(RelativeLayout) findViewById(R.id.set_clear);
		title_layout_set = (TitleView)findViewById(R.id.title_layout_set);
		title_layout_set.setTitle("消息提醒");
		title_layout_set.setLeftImage(R.drawable.back_item_selector, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
//		backImg.setOnClickListener(this);
		switchButton = (UISwitchButton) findViewById(R.id.switch_button);
		if(MyApplication.getInstance().getTempMember().getReKgStatus() == 0){
			switchButton.setChecked(true);
		}else{
			switchButton.setChecked(false);
		}
		switchButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
//					Toast.makeText(TestActivity.this, "开启", Toast.LENGTH_SHORT)
//							.show();
					setReKG(0);
				} else {
//					Toast.makeText(TestActivity.this, "关闭", Toast.LENGTH_SHORT)
//							.show();
					String msg = "关闭消息通知后，无法及时接收新工程，确认关闭？";
					showPickDialog(msg,"","");
				}
			}
		});
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		
		default:
			break;
		}
		
	}
	
	

	@Override
	protected void onResume() {
		if(MemeberKeeper.getOauth(SettingMessageActivity.this)==null){
			Intent intent = new Intent(SettingMessageActivity.this,LoginActivity.class);
			intent.putExtra("flag", 2);
			startActivity(intent);
			finish();
		}
		super.onResume();
	}
	
	private void showPickDialog(CharSequence msg,String negativeText,String positiveText) {
		final MyDialog dialog = new MyDialog(mContext, R.style.MyDialog,  
		        new MyDialog.LeaveMyDialogListener() {    
		         @Override   
		         public void onClick(View view) {   
		            switch(view.getId()){   
		              case R.id.positive_button:
		            	  switchButton.setChecked(true);
		            	  break;
		              case R.id.negative_button:
		            	  setReKG(1);
		            	  break;
		                 }   
		         }
		            });   
		dialog.show();
		dialog.setText(msg);
		if(!TextUtils.isEmpty(negativeText)){
			dialog.setNegativeText(negativeText);
		}
		if(!TextUtils.isEmpty(positiveText)){
			dialog.setPositiveText(positiveText);
		}
		
		
	}
	
private void setReKG(int value){//0开启  1关闭
	reKgStatus = value;
	new UpdateMemberInfoAsync("reKgStatus",reKgStatus+"",mContext,mHandler).execute();
}

@Override
public boolean handleMessage(Message msg) {
	// TODO Auto-generated method stub
	switch(msg.what){
	case Constants.HANDLER_UPDATEMEMBERINFO_SUCCESS:
		MyApplication.getInstance().getTempMember().setReKgStatus(reKgStatus);
		break;
	case Constants.HANDLER_UPDATEMEMBERINFO_FAIL:
		
		break;
	}
	return false;
}
	
}
