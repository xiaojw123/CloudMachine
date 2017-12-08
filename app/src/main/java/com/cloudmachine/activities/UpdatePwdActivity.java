package com.cloudmachine.activities;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.cloudmachine.R;
import com.cloudmachine.autolayout.widgets.RadiusButtonView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.helper.MobEvent;
import com.cloudmachine.net.task.UpdatePwdAsync;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.Utils;
import com.cloudmachine.utils.widgets.ClearEditTextView;
import com.umeng.analytics.MobclickAgent;

public class UpdatePwdActivity extends BaseAutoLayoutActivity implements Callback{

	private Context mContext;
	private Handler mHandler;
	private RadiusButtonView btn_bottom;
	private ClearEditTextView old_pwd;
	private ClearEditTextView new_pwd;
	private ClearEditTextView confirm_pwd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_pwd);
		mContext = this;
		mHandler = new Handler(this);
		initView();

	}

	@Override
	public void initPresenter() {

	}

	@Override
	protected void onResume() {
		//MobclickAgent.onPageStart(UMengKey.time_changepassword);
		super.onResume();
		MobclickAgent.onEvent(this, MobEvent.TIME_CHANGEPASSWORD);
	}

	@Override
	protected void onPause() {
		//MobclickAgent.onPageEnd(UMengKey.time_changepassword);
		super.onPause();
	}

	private void initView(){
		btn_bottom = (RadiusButtonView)findViewById(R.id.btn_bottom);
		old_pwd = (ClearEditTextView)findViewById(R.id.old_pwd);
		new_pwd = (ClearEditTextView)findViewById(R.id.new_pwd);
		confirm_pwd = (ClearEditTextView)findViewById(R.id.confirm_pwd);
		btn_bottom.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String old_str = old_pwd.getText().toString().trim();
				String new_str = new_pwd.getText().toString().trim();
				String confirm_str = confirm_pwd.getText().toString().trim();
				if(TextUtils.isEmpty(old_str)){
					Constants.ToastAction("旧密码不能为空！");
				}else if(TextUtils.isEmpty(new_str)){
					Constants.ToastAction("新密码不能为空！");
				}else if(TextUtils.isEmpty(confirm_str)){
					Constants.ToastAction("确认密码不能为空！");
				}else if(new_str.length()<6){
					Constants.ToastAction("新密码长度必须大于6位");
				}else if(!new_str.equals(confirm_str)){
					Constants.ToastAction("两次密码输入不一样");
				}else{
					new UpdatePwdAsync(Utils.getPwdStr(old_str),
							Utils.getPwdStr(new_str),
							Utils.getPwdStr(confirm_str),mContext,mHandler).execute();
				}
				
			}
		});
	}


	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		switch(msg.what){
		case Constants.HANDLER_UPDATEPWD_SUCCESS:
			Constants.MyToast((String)msg.obj);
			setResult(RESULT_OK);
			finish();
			break;
		case Constants.HANDLER_UPDATEPWD_FAIL:
			Constants.MyToast((String)msg.obj);
			break;
		}
		return false;
	}
}
