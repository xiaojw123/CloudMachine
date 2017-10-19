package com.cloudmachine.activities;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.cloudmachine.R;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.helper.MobEvent;
import com.cloudmachine.net.task.FeedBackAsync;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.widgets.Dialog.LoadingDialog;
import com.cloudmachine.widget.CommonTitleView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.umeng.analytics.MobclickAgent;

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
	private EditText suggestion_ed;
	private CommonTitleView title_layout;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.suggestionback);
		MobclickAgent.onEvent(SuggestBackActivity.this, MobEvent.SETTING_FEEDBACK);
		mContext = this;
		mHandler = new Handler(this);
		imageLoader.init(ImageLoaderConfiguration.createDefault(this));
		initView();
	}

	@Override
	public void initPresenter() {

	}

	private void initTitleLayout(){
		
		title_layout = (CommonTitleView)findViewById(R.id.title_layout);
		title_layout.setRightText("提交", new OnClickListener() {
			@Override
			public void onClick(View v) {
					show();
					new FeedBackAsync(suggestion_ed.getText().toString()
							,mContext,mHandler).execute();

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
		suggestion_ed = (EditText)findViewById(R.id.suggestion_ed);
//		backImg.setOnClickListener(this);
		suggestion_ed.setFocusable(true);
		suggestion_ed.requestFocus();
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
