package com.cloudmachine.activities;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.cloudmachine.R;
import com.cloudmachine.base.BaseActivity;
import com.cloudmachine.net.task.GetMobileCodeAsync;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.widgets.ClearEditTextView;
import com.cloudmachine.utils.widgets.TitleView;
import com.cloudmachine.utils.widgets.Dialog.MyDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


public class InputPhoneNum extends BaseActivity implements
                           OnClickListener,TextWatcher,Callback{

	private Context mContext;
	private Handler mHandler;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private ClearEditTextView phone_text;
	private CheckBox checkBox;
	private Button next_btn;
//	private ImageView backImg;
	private TitleView title_layout_reg;
	private static String moblieNum;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newregister);
		this.mContext = this;
		this.mHandler = new Handler(this);
		imageLoader.init(ImageLoaderConfiguration.createDefault(this));
		initView();
		

	}
	
	private String registerCheck() {
		if (!Constants.isMobileNO(phone_text.getText().toString())) {
			return getResources().getString(R.string.register_phone_not_correct);
		}
		if (!checkBox.isChecked()) {
			return "请同意用户协议";
		}
		return null;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==R.id.next_btn)
		{
			String result = registerCheck();
			
			if (result != null) {
				Toast.makeText(InputPhoneNum.this, result + "",
						Toast.LENGTH_LONG).show();
				return;
			}
			moblieNum = phone_text.getText().toString();
			
			MyDialog dialog = new MyDialog(mContext, R.style.MyDialog,  
			        new MyDialog.LeaveMyDialogListener() {    
			         @Override   
			         public void onClick(View view) {   
			            switch(view.getId()){   
			              case R.id.negative_button:
			            	  new GetMobileCodeAsync(moblieNum,"1",mContext,mHandler).execute();
			                        break; 
			              default:
			              break;
			                     }   
			                 }   
			            });   
			dialog.show();
			dialog.setText("我们将发送验证码到这个号码："+moblieNum);
			dialog.setNegativeText("好");
			
		}
	
		
	}
	
	void initView(){
		
		title_layout_reg = (TitleView)findViewById(R.id.title_layout_reg);
		
		title_layout_reg.setTitle("注册");
		title_layout_reg.setLeftImage(R.drawable.back_item_selector, new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		phone_text = (ClearEditTextView)findViewById(R.id.phone_text);
		checkBox =(CheckBox)findViewById(R.id.check_box);
		next_btn = (Button)findViewById(R.id.next_btn);
		next_btn.setOnClickListener(this);
		phone_text.addTextChangedListener(this);  
		next_btn.setEnabled(false);
		
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		
		if (!TextUtils.isEmpty(phone_text.getText().toString())) {
			next_btn.setEnabled(true);
		}
		else {
			next_btn.setEnabled(false);
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		switch(msg.what){
		case Constants.HANDLER_GETCODE_SUCCESS:
			Bundle b = new Bundle();
			b.putString("phone", moblieNum);
			Constants.toActivity(this, InputCode.class, b,true);
			break;
		case Constants.HANDLER_GETCODE_FAIL:
			Constants.MyToast((String)msg.obj);
			break;
		}
		return false;
	}

}
