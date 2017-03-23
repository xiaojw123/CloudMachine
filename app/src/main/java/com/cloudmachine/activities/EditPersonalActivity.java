package com.cloudmachine.activities;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.cloudmachine.R;
import com.cloudmachine.app.MyApplication;
import com.cloudmachine.autolayout.widgets.TitleView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.net.HttpURLConnectionImp;
import com.cloudmachine.net.IHttp;
import com.cloudmachine.struc.BaseBO;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MemeberKeeper;
import com.cloudmachine.utils.URLs;
import com.cloudmachine.utils.widgets.ClearEditTextView;
import com.cloudmachine.utils.widgets.Dialog.LoadingDialog;
import com.google.gson.Gson;

public class EditPersonalActivity extends BaseAutoLayoutActivity implements
OnClickListener {
	private LoadingDialog progressDialog;
	private ClearEditTextView info_ed;
	private String info;
	private String key;
	private TitleView title_layout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_personal_info);
		info = getIntent().getStringExtra("info");
		initView();
	}

	@Override
	public void initPresenter() {

	}

	@Override
	protected void onResume() {
		//MobclickAgent.onPageStart(UMengKey.time_profile_edit);
		super.onResume();
	}

	@Override
	protected void onPause() {
		//MobclickAgent.onPageEnd(UMengKey.time_profile_edit);
		super.onPause();
	}

	private void initTitleLayout() {
		title_layout = (TitleView) findViewById(R.id.title_layout);
		title_layout.setTitle("昵称");
		title_layout.setLeftOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				if(!info.equals(Constants.toViewString(info_ed.getText().toString()))){
					key="nickName";
					new saveInfoAsync().execute();
				}
				
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		default:
			break;
		}
	}
	
	void initView(){
		initTitleLayout();
		info_ed = (ClearEditTextView)findViewById(R.id.info_ed);
		info_ed.setText(info);
	}

	public class saveInfoAsync extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			show();
		}
		
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			if (!MyApplication.getInstance().isOpenNetwork(
					EditPersonalActivity.this)) {
				return null;
			}
			IHttp httpRequest = new HttpURLConnectionImp();
			List<NameValuePair> list = new ArrayList<NameValuePair>();		
		    
		    	list.add(new BasicNameValuePair("memberId", MemeberKeeper
						.getOauth(EditPersonalActivity.this).getId().toString()));
				list.add(new BasicNameValuePair("key", key));
				list.add(new BasicNameValuePair("value", info_ed.getText()
						.toString()));
					
       
			String result = "";
			try {
					result = httpRequest.post(URLs.EDITINFO_URL, list);
				
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}

			return result;
		}	
		@Override
		protected void onPostExecute(String result) {
			disMiss();
			if (result != null) {
				Gson gson = new Gson();
				BaseBO updateResult = gson.fromJson(result, BaseBO.class);
				if (updateResult.isOk()) {
					Toast.makeText(EditPersonalActivity.this, "修改成功",
							Toast.LENGTH_SHORT).show();
					EditPersonalActivity.this.finish();

				} else {
					Toast.makeText(EditPersonalActivity.this,
							updateResult.getMessage(), Toast.LENGTH_SHORT)
							.show();
				}
			} else {
				Toast.makeText(EditPersonalActivity.this, "修改失败",
						Toast.LENGTH_SHORT).show();

			}

		}
		
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
}
