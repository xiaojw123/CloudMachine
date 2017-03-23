package com.cloudmachine.activities;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudmachine.R;
import com.cloudmachine.autolayout.widgets.RadiusButtonView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.net.task.ForgetPwdAsync;
import com.cloudmachine.net.task.GetMobileCodeAsync;
import com.cloudmachine.net.task.RegisterNewAsync;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MemeberKeeper;
import com.cloudmachine.utils.UMengKey;
import com.cloudmachine.utils.Utils;
import com.cloudmachine.utils.widgets.ClearEditTextView;
import com.cloudmachine.utils.widgets.Dialog.LoadingDialog;
import com.umeng.analytics.MobclickAgent;

import java.util.Timer;
import java.util.TimerTask;

public class FindPasswordActivity extends BaseAutoLayoutActivity implements OnClickListener ,Callback{

	private Context mContext;
	private Handler mHandler;
	private LoadingDialog progressDialog;


	private TextView title_text,agreement_text;
	private ClearEditTextView phone_string;
	private ClearEditTextView validate_code;
	private ClearEditTextView pwd_string;
	private ClearEditTextView pwd_new_string;
	private ClearEditTextView pwd_new2_string;
	private RadiusButtonView find_btn;

	private TextView validate_text;
	private static final int VALIDATENUM = 60; 
	private int validate_num;
	private String message = "正在加载，请稍后";
	
	
	private Timer myTimer;
	
	//  1表示忘记密码 2 表示修改密码  3表示注册
	private int type;
	
	private View left_layout,validate_layout,
	pwd_layout,pwd_new_layout,agreement_layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forget_change_pssword);
		this.mContext = this;
		mHandler = new Handler(this);
		type = getIntent().getIntExtra("type", 1);
		
		initView();

	}

	@Override
	public void initPresenter() {

	}

	private void initView() {
		agreement_layout = findViewById(R.id.agreement_layout);
		left_layout = findViewById(R.id.left_layout);
		left_layout.setOnClickListener(this);
		title_text = (TextView)findViewById(R.id.title_text);
		validate_text = (TextView)findViewById(R.id.validate_text);
		agreement_text = (TextView)findViewById(R.id.agreement_text);
		agreement_text.setOnClickListener(this);
		
//		backImg  = (ImageView)findViewById(R.id.backImg);
		phone_string = (ClearEditTextView) findViewById(R.id.phone_string);
		validate_code = (ClearEditTextView) findViewById(R.id.validate_code);
		validate_layout = findViewById(R.id.validate_layout);
		validate_layout.setOnClickListener(this);
		pwd_layout = findViewById(R.id.pwd_layout);
		pwd_new_layout = findViewById(R.id.pwd_new_layout);
		
		pwd_string = (ClearEditTextView) findViewById(R.id.pwd_string);
		pwd_new_string = (ClearEditTextView) findViewById(R.id.pwd_new_string);
		pwd_new2_string = (ClearEditTextView)findViewById(R.id.pwd_new2_string);
		
		switch(type){
		case 1:
			title_text.setText("忘记密码");
			pwd_layout.setVisibility(View.GONE);
			pwd_new_layout.setVisibility(View.VISIBLE);
			agreement_layout.setVisibility(View.GONE);
			
			break;
		case 2:
			title_text.setText("修改密码");
			pwd_layout.setVisibility(View.GONE);
			pwd_new_layout.setVisibility(View.VISIBLE);
			agreement_layout.setVisibility(View.GONE);
			break;
		case 3:
			MobclickAgent.onPageStart(UMengKey.time_register);
			title_text.setText("新用户注册");
			pwd_layout.setVisibility(View.VISIBLE);
			pwd_new_layout.setVisibility(View.GONE);
			agreement_layout.setVisibility(View.VISIBLE);
			break;
			default:
				title_text.setText("新用户注册");
				pwd_layout.setVisibility(View.VISIBLE);
				pwd_new_layout.setVisibility(View.GONE);
				agreement_layout.setVisibility(View.VISIBLE);
				break;
		}
		
		find_btn = (RadiusButtonView) findViewById(R.id.find_btn);
		find_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				savePwd();
			}
		});
		
		if(MemeberKeeper.getOauth(FindPasswordActivity.this)!=null){
			phone_string.setText(MemeberKeeper.getOauth(FindPasswordActivity.this).getMobile());
		}
	}

	
	
	
	private void show() {
		if (progressDialog == null) {
			progressDialog = LoadingDialog.createDialog(this);
		}
		progressDialog.setMessage(message);
		progressDialog.show();
	}

	private void disMiss() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_layout:
			finish();
			break;
		case R.id.validate_layout:
			if(null == myTimer){
				String phoneString =phone_string.getText().toString();
				if(phoneString!=null&&phoneString.length()==11){
					if(validate_num == 0){
						show();
						new GetMobileCodeAsync(phoneString,"2",mContext,mHandler).execute();
					}
				}
				else {
					Toast.makeText(FindPasswordActivity.this, "请输入手机号", Toast.LENGTH_SHORT).show();
					
				}
			}
			
			break;
		case R.id.agreement_text:
			Bundle bundle = new Bundle();
			bundle.putString(Constants.P_WebView_Url, "http://www.cloudm.com/agreement");
			bundle.putString(Constants.P_WebView_Title, agreement_text.getText().toString());
			Constants.toActivity(this, WebviewActivity.class, bundle);
			break;
		default:
			break;
		}
	}
	private void savePwd(){
		String code = validate_code.getText().toString().trim();
		String phone = phone_string.getText().toString().trim();
		String pwdStr = pwd_string.getText().toString().trim();
		String newpwd1 = pwd_new_string.getText().toString().trim();
		String newpwd2 = pwd_new2_string.getText().toString().trim();
		if(type == 3){
			if(TextUtils.isEmpty(phone)){
				Constants.ToastAction("请输入手机号码");
			}else if(TextUtils.isEmpty(code)){
				Constants.ToastAction("请输入验证码");
			}else if(TextUtils.isEmpty(pwdStr)){
				Constants.ToastAction("请输入密码");
			}else if(pwdStr.length()<6){
				Constants.ToastAction("新密码长度必须大于6位");
			}else{
				new RegisterNewAsync(phone, Utils.getPwdStr(pwdStr), code, mContext, mHandler).execute();
			}
		}else{
			if(TextUtils.isEmpty(phone)){
				Constants.ToastAction("请输入手机号码");
			}else if(TextUtils.isEmpty(code)){
				Constants.ToastAction("请输入验证码");
			}else if(TextUtils.isEmpty(newpwd1)){
				Constants.ToastAction("请输入新密码");
			}else if(newpwd1.length()<6){
				Constants.ToastAction("新密码长度必须大于6位");
			}else if(TextUtils.isEmpty(newpwd2)){
				Constants.ToastAction("请再次输入新密码");
			}else if(!newpwd1.equals(newpwd2)){
				Constants.ToastAction("两次密码输入不一样");
			}else{
//				if(type == 1){
					new ForgetPwdAsync(phone,
							Utils.getPwdStr(newpwd1),
							code,mContext,mHandler).execute();
//				}else if(type == 2){
					/*new UpdatePwdAsync(Utils.getPwdStr(old_password.getText().toString()),
							Utils.getPwdStr(new_passwrod.getText().toString()),
							Utils.getPwdStr(confirm_passwrod.getText().toString()),mContext,mHandler).execute();*/
//				}
			}
		}
		
	}
	class ListenerTimerTask extends TimerTask{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Message message = new Message();      
		      message.what = Constants.HANDLER_TIMER;   
		      mHandler.sendMessage(message); 
		}
		
	}
	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		switch(msg.what){
		case Constants.HANDLER_TIMER:
			Constants.MyLog("HANDLER_TIMER:"+validate_num);
			validate_num ++;
			if((VALIDATENUM - validate_num)<0){
				myTimer.cancel();
				myTimer = null;
				validate_num = 0;
				validate_text.setText("获取验证码");
			}else{
				validate_text.setText("获取验证码("+(VALIDATENUM - validate_num)+")");
			}
			break;
		case Constants.HANDLER_GETCODE_SUCCESS:
			disMiss();
			Toast.makeText(FindPasswordActivity.this, "验证码已发送请注意查收", Toast.LENGTH_SHORT).show();
			validate_text.setText(""+(VALIDATENUM - validate_num));
			if(null != myTimer){
				myTimer.cancel();
				myTimer = null;
			}
			myTimer = new Timer(true);
			myTimer.schedule(new ListenerTimerTask(),100,1000); 
			break;
		case Constants.HANDLER_GETCODE_FAIL:
			disMiss();
			Toast.makeText(FindPasswordActivity.this, (String)msg.obj, Toast.LENGTH_SHORT).show();
			break;
		case Constants.HANDLER_FORGETPWD_SUCCESS:
		case Constants.HANDLER_UPDATEPWD_SUCCESS:
			disMiss();
			Toast.makeText(FindPasswordActivity.this, "修改密码成功", Toast.LENGTH_SHORT).show();
//			MemeberKeeper.clearOauth(this);
			finish();
			break;
		case Constants.HANDLER_FORGETPWD_FAIL:
		case Constants.HANDLER_UPDATEPWD_FAIL:
		case Constants.HANDLER_REGISTER_FAIL:
			disMiss();
			Toast.makeText(this, (String)msg.obj, Toast.LENGTH_SHORT).show();
			pwd_new2_string.setText("");
			pwd_new_string.setText("");
			pwd_string.setText("");
			validate_code.setText("");
			break;
		case Constants.HANDLER_REGISTER_SUCCESS:
			disMiss();
			Toast.makeText(FindPasswordActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
			finish();
			break;
		}
		return false;
	}


	@Override
	protected void onPause() {
		MobclickAgent.onPageEnd(UMengKey.time_register);
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(null != myTimer){
			myTimer.cancel();
			myTimer = null;
		}
	}
	
	

}
