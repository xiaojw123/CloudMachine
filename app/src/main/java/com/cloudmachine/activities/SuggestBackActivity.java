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

import com.cloudmachine.R;
import com.cloudmachine.autolayout.widgets.TitleView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.net.task.FeedBackAsync;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.widgets.ClearEditTextView;
import com.cloudmachine.utils.widgets.Dialog.LoadingDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * 意见反馈
 */
public class SuggestBackActivity extends BaseAutoLayoutActivity implements
OnClickListener, Callback{

	private Context mContext;
	private Handler mHandler;
	private final String LOG_TAG = "SuggwetbackActivity";
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	
	private LoadingDialog progressDialog;
	
//	private ImageView backImg;
	private ClearEditTextView suggestion_ed;
	private TitleView title_layout;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.suggestionback);
		 
		mContext = this;
		mHandler = new Handler(this);
		imageLoader.init(ImageLoaderConfiguration.createDefault(this));
		initView();
	}

	@Override
	public void initPresenter() {

	}

	private void initTitleLayout(){
		
		title_layout = (TitleView)findViewById(R.id.title_layout);
		title_layout.setTitle("意见反馈");
		
		title_layout.setLeftOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 finish(); 
			}
		});
		title_layout.setRightTextEdit(false);
		title_layout.setRightText(-1, "提交", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(title_layout.getRightTextEdit()){
					show();
					new FeedBackAsync(suggestion_ed.getText().toString()
							,mContext,mHandler).execute();
				}
				
				
			}
		});
		
	}

	@Override
	protected void onResume() {
		//MobclickAgent.onPageStart(UMengKey.time_setting_feedback);
		super.onResume();
	}

	@Override
	protected void onPause() {
		//MobclickAgent.onPageEnd(UMengKey.time_setting_feedback);
		super.onPause();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		default:
			break;
		}
		
	}

	
	void initView(){
		initTitleLayout();
//		backImg = (ImageView) findViewById(R.id.backImg);
		suggestion_ed = (ClearEditTextView)findViewById(R.id.suggestion_ed);
//		backImg.setOnClickListener(this);
		
		suggestion_ed.setFocusable(true);
		suggestion_ed.requestFocus();
		
		suggestion_ed.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (!TextUtils.isEmpty(suggestion_ed.getText().toString())) {
					title_layout.setRightTextEdit(true);
				}
				else {
					title_layout.setRightTextEdit(false);
				}
			}
		});
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

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		switch(msg.what){
		case Constants.HANDLER_FEEDBACK_SUCCESS:
			disMiss();
			Constants.MyToast((String)msg.obj);
			this.finish();
			break;
		case Constants.HANDLER_FEEDBACK_FAIL:
			disMiss();
			Constants.MyToast((String)msg.obj);
			break;
		}
		return false;
	}

}
