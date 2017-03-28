package com.cloudmachine.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudmachine.R;
import com.cloudmachine.base.BaseActivity;
import com.cloudmachine.net.task.CheckCodeAsync;
import com.cloudmachine.net.task.RegisterAsync;
import com.cloudmachine.ui.login.acticity.LoginActivity;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.ResV;
import com.cloudmachine.utils.widgets.AppMsg;
import com.cloudmachine.utils.widgets.ClearEditTextView;
import com.cloudmachine.utils.widgets.Dialog.LoadingDialog;
import com.cloudmachine.utils.widgets.RadiusButtonView;
import com.cloudmachine.utils.widgets.TitleView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.Timer;
import java.util.TimerTask;

public class InputCode extends BaseActivity implements
OnClickListener, Callback {

	private final String LOG_TAG = "InputCodeActivity";
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private static final int VALIDATENUM = 60;
	private Context mContext;
	private Handler mHandler;
	private LoadingDialog progressDialog;
	private TextView phone;
	private Button next_btn;
	private RadiusButtonView save_btn;
	private String phoneNum;
	private LinearLayout codelayout;
	private LinearLayout confirminfo;
//	private TextView title;
	private String finCode;
	private ClearEditTextView code_text;
	private ClearEditTextView nickname_ed;
	private ClearEditTextView password_ed;
	private ClearEditTextView confirmpassword_ed;
//	private ImageView backImg;
	private TitleView title_layout_code;
	private int validate_num;
	private TextView seconds_text;
	private Timer myTimer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inputcode);
		 
		mContext = this;
		mHandler = new Handler(this);
		imageLoader.init(ImageLoaderConfiguration.createDefault(this));
		phoneNum = getIntent().getStringExtra("phone");
		initView();
		if(null != myTimer){
			myTimer.cancel();
			myTimer = null;
		}
		myTimer = new Timer(true);
		myTimer.schedule(new ListenerTimerTask(),100,1000); 

	}
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		if(v.getId()==R.id.next_btn)
		{
			//
			
			
			if (code_text.getText().toString().length()!=4) {
				code_text.setShakeAnimation();
				AppMsg appMsg = AppMsg.makeText(this, "请正确输入验证码", AppMsg.STYLE_CO);
				appMsg.setLayoutGravity(Gravity.TOP);
				appMsg.show();
				return ;
			}
			
			
			show();
			new CheckCodeAsync(phoneNum,code_text.getText().toString(),
					mContext,mHandler).execute();
//			codelayout.setVisibility(View.GONE);
//			confirminfo.setVisibility(View.VISIBLE);
//			title.setText("设置密码");
		}

		
		
		if (v.getId()==R.id.save_btn) {
			String result = saveCheck();
			
			if (result!=null) {
				Toast.makeText(InputCode.this, result + "",
						Toast.LENGTH_LONG).show();
				return;
			}
			show();
			new RegisterAsync(phoneNum+"",
					password_ed.getText().toString(),
					nickname_ed.getText().toString(),mContext,mHandler).execute();
		}
	}
	
	
	void initView(){
		
		phone = (TextView)findViewById(R.id.phone);
		next_btn = (Button)findViewById(R.id.next_btn);
		save_btn = (RadiusButtonView)findViewById(R.id.save_btn);
		codelayout = (LinearLayout)findViewById(R.id.codelayout);
		confirminfo = (LinearLayout)findViewById(R.id.confirminfo);
		code_text = (ClearEditTextView)findViewById(R.id.code_text);
		nickname_ed = (ClearEditTextView)findViewById(R.id.nickname_ed);
		password_ed = (ClearEditTextView)findViewById(R.id.password_ed);
		confirmpassword_ed = (ClearEditTextView)findViewById(R.id.confirmpassword_ed);
//		title = (TextView)findViewById(R.id.title);
		
		title_layout_code = (TitleView)findViewById(R.id.title_layout_code);
		title_layout_code.setTitle("填写验证码");
		title_layout_code.setLeftImage(R.drawable.back_item_selector, new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
//		backImg = (ImageView)findViewById(R.id.backImg);
		phone.setText(phoneNum);
//		backImg.setOnClickListener(this);
		next_btn.setOnClickListener(this);
		save_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				String result = saveCheck();
				
				if (result!=null) {
					Toast.makeText(InputCode.this, result + "",
							Toast.LENGTH_LONG).show();
					return;
				}
				show();
				new RegisterAsync(phoneNum+"",
						password_ed.getText().toString(),
						nickname_ed.getText().toString(),mContext,mHandler).execute();
			}
		});		
		seconds_text = (TextView)findViewById(R.id.seconds_text);
	}
	
	
	private String saveCheck() {
		String nick = nickname_ed.getText().toString().trim();
		String pwd = password_ed.getText().toString().trim();
		String pwd2 = confirmpassword_ed.getText().toString().trim();
		if(TextUtils.isEmpty(nick)){
			return ResV.getString(R.string.register_nick_null);
		}
		if(TextUtils.isEmpty(pwd)||TextUtils.isEmpty(pwd2)){
			return ResV.getString(R.string.register_password_null);
		}
		if (pwd.length() < 6 ) {
			return ResV.getString(R.string.register_password_lenght);
		}
		if (!pwd.equals(pwd2)) {
			return ResV.getString(R.string.register_password_not_same);
		}
		return null;
	}
	
	
	
	
	
	private void show() {
		disMiss();
		if (progressDialog == null) {
			progressDialog = LoadingDialog.createDialog(this);
			progressDialog.setMessage("正在加载，请稍后");
			progressDialog.show();
		}
	}

	private void disMiss() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
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
			validate_num ++;
			if((VALIDATENUM - validate_num)<0){
				myTimer.cancel();
				myTimer = null;
				validate_num = 0;
				seconds_text.setText("如果未收到验证码，请确认手机号码是否正确重新获取！");
			}else{
				seconds_text.setText("获取验证码("+(VALIDATENUM - validate_num)+")");
			}
			break;
		case Constants.HANDLER_REGISTER_SUCCESS:
			disMiss();
			Toast.makeText(InputCode.this, "注册成功",
					Toast.LENGTH_SHORT).show();
			InputCode.this.finish();
			
			Intent it = new Intent(InputCode.this,LoginActivity.class);
			 it.putExtra("flag", 1);
			InputCode.this.startActivity(it);
			break;
		case Constants.HANDLER_REGISTER_FAIL:
			disMiss();
			Toast.makeText(InputCode.this,
					(String)msg.obj, Toast.LENGTH_SHORT)
					.show();
			break;
		case Constants.HANDLER_CHECKCODE_SUCCESS:
			disMiss();
			Toast.makeText(InputCode.this, "验证码正确",
					Toast.LENGTH_SHORT).show();
			//InputCode.this.finish();
			codelayout.setVisibility(View.GONE);
			confirminfo.setVisibility(View.VISIBLE);
			finCode = code_text.getText().toString();
//			title.setText("设置密码");
			title_layout_code.setTitle("设置密码");
			break;
		case Constants.HANDLER_CHECKCODE_FAIL:
			disMiss();
			Toast.makeText(InputCode.this,
					(String)msg.obj, Toast.LENGTH_SHORT)
					.show();
			break;
		}
		return false;
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
