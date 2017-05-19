package com.cloudmachine.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.adapter.McSearchAdapter;
import com.cloudmachine.adapter.McSearchMemberAdapter;
import com.cloudmachine.autolayout.widgets.CustomDialog;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.net.task.DevicesListAsync;
import com.cloudmachine.net.task.GivePermissionNewAsync;
import com.cloudmachine.net.task.SearchMemberAsync;
import com.cloudmachine.struc.McDeviceInfo;
import com.cloudmachine.struc.MemberInfo;
import com.cloudmachine.struc.News;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.UIHelper;
import com.cloudmachine.utils.UMListUtil;
import com.cloudmachine.utils.UMengKey;
import com.cloudmachine.utils.widgets.EmptyLayout;
import com.cloudmachine.utils.widgets.XListView;
import com.cloudmachine.utils.widgets.XListView.IXListViewListener;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseAutoLayoutActivity implements OnClickListener, IXListViewListener,
	Callback,OnItemClickListener{

	private Context mContext;
	private Handler mHandler;
	private EditText searchText;
	private ImageView icon_clear;
	private boolean isLoading;
	private InputMethodManager imm;
	private XListView listview;
	private List<News> displaylist = new ArrayList<News>();
	private McSearchAdapter mAdapter;
	private McSearchMemberAdapter mMemberAdapter;
	private List<McDeviceInfo> deviceList = new ArrayList<McDeviceInfo>();
	private List<MemberInfo> memberList = new ArrayList<MemberInfo>();
	private int pageNo=1;
	private EmptyLayout mEmptyLayout;
	private int searchListType=-1; //0:搜索机器 1:搜索成员  2:搜索成员移交机主
	private TextView button_cancel;
	private long deviceId;
	private ImageView mBtnBack;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		mContext = this;
		mHandler = new Handler(this);
		getIntentData();
//		searchListType = getIntent().getExtras().getInt(Constants.P_SEARCHLISTTYPE);
		initView();
	}

	@Override
	public void initPresenter() {

	}

	private void getIntentData(){
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
        	try{
        		searchListType =bundle.getInt(Constants.P_SEARCHLISTTYPE); 
        		deviceId =bundle.getLong(Constants.P_DEVICEID); 
        	}catch(Exception e){
        		Constants.MyLog(e.getMessage());
        	}
        	
        }
        
    }
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.btn_back:
			this.finish();
			MobclickAgent.onEvent(mContext,UMengKey.time_search_cancel);
			break;
			case R.id.button_cancel:
				clickSearch();
				break;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		if(Constants.isDeleteDeviceMember){
			Constants.isDeleteDeviceMember = false;
			clickSearch();
		}
		//MobclickAgent.onPageStart(UMengKey.time_search);
		super.onResume();
	}

	@Override
	protected void onPause() {
		//MobclickAgent.onPageEnd(UMengKey.time_search);
		super.onPause();
	}

	private void initView(){
		mBtnBack = (ImageView) findViewById(R.id.btn_back);
		mBtnBack.setOnClickListener(this);
		button_cancel = (TextView) findViewById(R.id.button_cancel);
		button_cancel.setOnClickListener(this);
		searchText = (EditText) findViewById(R.id.et_search_keyword);
//		searchText.requestFocus();
//		InputMethodManager im = ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE));
//		im.showSoftInput(searchText, InputMethodManager.SHOW_FORCED);
		icon_clear = (ImageView)findViewById(R.id.icon_clear);
		listview = (XListView) findViewById(
				R.id.noti_listview);
		listview.setVisibility(View.GONE);
		imm = (InputMethodManager)this
			    .getSystemService(Context.INPUT_METHOD_SERVICE);
//		imm.hideSoftInputFromWindow(searchText.getWindowToken(), 0);

		

		if(searchListType == 0){
			mAdapter = new McSearchAdapter(deviceList, this);
			listview.setAdapter(mAdapter);
			listview.setOnItemClickListener(this);
		}if(searchListType == 1 || searchListType == 2){
			searchText.setRawInputType(InputType.TYPE_CLASS_NUMBER);
			searchText.setHint(getResources().getString(R.string.search_member_hint_message));
			mMemberAdapter = new McSearchMemberAdapter(memberList,searchListType, this,mHandler,deviceId);
			listview.setAdapter(mMemberAdapter);
		}
		
		
		
//		listview.setShowFooterView(false);
		listview.setPullLoadEnable(false);
		listview.setPullRefreshEnable(false);
		listview.setXListViewListener(this);
		mEmptyLayout = new EmptyLayout(this, listview);
//		mEmptyLayout.setErrorButtonClickListener(mErrorClickListener);
		
		searchText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				icon_clear.setVisibility(View.VISIBLE);

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				icon_clear.setVisibility(View.GONE);

			}

			@Override
			public void afterTextChanged(Editable s) {
				icon_clear.setVisibility(View.VISIBLE);

			}
		});
		icon_clear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				searchText.setText("");
				icon_clear.setVisibility(View.GONE);
			}

		});

		searchText.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER & !isLoading) {
					clickSearch();
					MobclickAgent.onEvent(mContext,UMengKey.count_search_keyboard);
				}
				return false;
			}
		});
	}
	
	
	private Handler blogHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
			case Constants.HANDLER_BLOGSUPPORTLIST_SUCCESS:
				break;
			case Constants.HANDLER_BLOGSUPPORTLIST_FAIL:
				Constants.ToastAction((String)msg.obj);
				break;
			}
		}
		
	};
	private MemberInfo memberInfo;

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		switch(msg.what){
		case Constants.HANDLER_GETDEVICELIST_SUCCESS:
			isLoading = false;
			if(deviceList!=null)
				deviceList.clear();
			deviceList.addAll((List<McDeviceInfo>)msg.obj);
			if(deviceList.size()==0){
				mEmptyLayout.showEmpty();
			}
			listview.setPullLoadEnable(false);
			mAdapter.notifyDataSetChanged();
			break;
		case Constants.HANDLER_GETDEVICELIST_FAIL:
			isLoading = false;
			if(deviceList!=null)
				deviceList.clear();
			mEmptyLayout.setErrorMessage("未找到您要的设备，请重新搜索！");
			mEmptyLayout.showError();
			break;
		case Constants.HANDLER_SEARCHMEMBER_SUCCESS:
			isLoading = false;
			if(null != memberList){
				memberList.clear();
			}
			memberList.add((MemberInfo)msg.obj);
			if(memberList.size()==0){
				mEmptyLayout.showEmpty();
			}
//			listview.setPullLoadEnable(true);
//			listview.setPullLoadEnable(false);
			mMemberAdapter.notifyDataSetChanged();
			break;
		case Constants.HANDLER_SEARCHMEMBER_FAIL:
			isLoading = false;
			if(memberList!=null)
				memberList.clear();
//			mEmptyLayout.setErrorMessage(getResources().getString(R.string.search_member_error_message));
			mEmptyLayout.setEmptyMessage(getResources().getString(R.string.search_member_error_message));
			Constants.ToastAction("未找到该账户");
//			mEmptyLayout.showError();
			break;
		case Constants.HANDLER_SEARCHBLOG_SUCCESS:
			break;
		case Constants.HANDLER_SEARCHBLOG_FAIL:
			isLoading = false;
			listview.stopRefresh();
			listview.stopLoadMore();
//			mEmptyLayout.showError();
			UIHelper.ToastMessage(mContext, (String)msg.obj);
			break;
		case Constants.HANDLER_ADDMEMBER:
			memberInfo = (MemberInfo)msg.obj;
			if (searchListType == 1) {
				new GivePermissionNewAsync(memberInfo.getMemberId(),deviceId,searchListType,mContext,mHandler).execute();
			}else {
				CustomDialog.Builder builder = new CustomDialog.Builder(SearchActivity.this);
				String name = memberInfo.getName();
				builder.setLeftButtonColor(getResources().getColor(R.color.black));
				builder.setRightButtonColor(getResources().getColor(R.color.dialog_changeowner_color));
				builder.setMessage("确定将机器移交给"+"'"+name+"'"+",移交之后，您将无权再查看此机器", "");
				builder.setTitle("提示");
				builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						return;
					}
				});
				builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						new GivePermissionNewAsync(memberInfo.getMemberId(),deviceId,searchListType,mContext,mHandler).execute();
						dialog.dismiss();
					}
				});
				builder.create().show();
			}
			break;
		case Constants.HANDLER_ADDMEMBER_SUCCESS:
			UMListUtil.getUMListUtil().sendStruEvent("AddMemberSuccess",mContext);
			Constants.MyToast((String)msg.obj);
			finish();
			break;
		case Constants.HANDLER_ADDMEMBER_FAIL:
			Constants.MyToast((String)msg.obj);
			break;
			
		
		
		}
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		McDeviceInfo info = mAdapter.getItem(position-1);
		long dId = info.getId();
		
		Intent intent = new Intent(mContext,DeviceMcActivity.class);
//		intent.putExtra(Constants.P_DEVICEID, dId);
//		intent.putExtra(Constants.P_DEVICENAME, info.getName());
//		intent.putExtra(Constants.P_DEVICEMAC, info.getMacAddress());
 		intent.putExtra("deviceId", dId);
		intent.putExtra("deviceName", info.getName());
		intent.putExtra("deviceMac", info.getMacAddress());
		intent.putExtra("deviceWorkState",info.getWorkStatus());
		startActivity(intent);
		
		
	}

	private void clickSearch(){
		listview.setVisibility(View.VISIBLE);
		isLoading = true;
//		displaylist.clear();
		imm.hideSoftInputFromWindow(searchText.getWindowToken(), 0);
		String sText = searchText.getText().toString().trim();
		if(searchListType == 0){
			new DevicesListAsync(Constants.MC_DevicesList_AllType,sText,mContext,mHandler).execute();
		}
		if(searchListType == 1 || searchListType == 2){
			sText = sText.replace("-", "").trim();
			new SearchMemberAsync(mContext,mHandler).execute(sText);
		}
		
	}
	
}
