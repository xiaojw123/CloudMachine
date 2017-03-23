package com.cloudmachine.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.adapter.PermissionAdapter;
import com.cloudmachine.base.BaseActivity;
import com.cloudmachine.cache.LocationSerializable;
import com.cloudmachine.net.task.AddMemberAsync;
import com.cloudmachine.net.task.DevicesDeleteMemberAsync;
import com.cloudmachine.net.task.updateMemberPermissionAsync;
import com.cloudmachine.struc.EditListInfo;
import com.cloudmachine.struc.MemberInfo;
import com.cloudmachine.struc.Permission;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.widgets.AddDeviceItemView;
import com.cloudmachine.utils.widgets.Dialog.MyDialog;
import com.cloudmachine.utils.widgets.RadiusButtonView;
import com.cloudmachine.utils.widgets.TitleView;

import java.util.ArrayList;
import java.util.List;

public class PermissionActivity extends BaseActivity implements Callback, OnClickListener{

	private Context mContext;
	private Handler mHandler;
	private long deviceId;
	private int deviceType;
	private int memberId;
	private String memberName,memberRole,memberRoleRemark,memberPermission;
	private RadiusButtonView delRButton;
	private ListView permission_listview;
	private PermissionAdapter pAdapter;
	private List<Permission> dataResult = new ArrayList<Permission>();
	private List<Permission> permissionList;
	private TextView quarters_text;
	private int roleIdS;  
	private String[] permissionId;
	private boolean isChange = false;
	private int permissionType ;//0:查看权限 1:新增权限
	private boolean isSave = false;
	private View nodes_layout;
	private TextView name;
	private AddDeviceItemView member_role_remark;
	private MemberInfo memberInfo;
	private boolean isChangeMaster = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_device_member_permission);
		mContext = this;
		mHandler = new Handler(this);
		getIntentData();
		initView();
		initPermissionData();
	}

	private void getIntentData(){
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
        	try{
        		permissionType = bundle.getInt(Constants.P_PERMISSIONTYPE);
        		deviceId =bundle.getLong(Constants.P_DEVICEID); 
        		deviceType =bundle.getInt(Constants.P_DEVICETYPE); 
        		memberInfo = (MemberInfo)bundle.getSerializable(Constants.P_MERMBERINFO); 
        		if(null != memberInfo){
        			memberId =memberInfo.getMemberId(); 
            		memberName =memberInfo.getName(); 
            		memberRole =memberInfo.getRole();
            		memberPermission =memberInfo.getPermissName(); 
            		memberRoleRemark =memberInfo.getRoleRemark(); 
            		roleIdS = memberInfo.getRoleIdS();
        		}else{
        			memberId =bundle.getInt(Constants.P_MERMBERID); 
            		memberName =bundle.getString(Constants.P_MERMBERNAME,""); 
            		memberRole =bundle.getString(Constants.P_MERMBERROLE,""); 
            		memberRoleRemark =bundle.getString(Constants.P_MERMBERROLEREMARK,""); 
            		memberPermission =bundle.getString(Constants.P_MERMBERPERMISSION,""); 
        		}
        		if(null !=memberPermission)
        			permissionId = memberPermission.split(",");
        	}catch(Exception e){
        		Constants.MyLog(e.getMessage());
        	}
        	
        }
    }

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	private void initView(){
		initTitleLayout();
		name = (TextView)findViewById(R.id.name);
		member_role_remark = (AddDeviceItemView)findViewById(R.id.member_role_remark);
		if(null != memberRoleRemark){
			member_role_remark.setContent(memberRoleRemark);
		}
		member_role_remark.setOnClickListener(this);
		quarters_text = (TextView)findViewById(R.id.quarters_text);
		quarters_text.setText(memberRole);
		quarters_text.setOnClickListener(this);
		pAdapter = new PermissionAdapter(mContext, dataResult, mHandler,Constants.isNoEditInMcMember(deviceId, deviceType));
		permission_listview =  (ListView)findViewById(R.id.permission_listview);
		permission_listview.setAdapter(pAdapter);
		delRButton = (RadiusButtonView)findViewById(R.id.delRButton);
		delRButton.setText("删除成员");
		delRButton.setColor(getResources().getColor(R.color.mc_rad_but_red_stroke),
				getResources().getColor(R.color.mc_rad_but_red_nm),
				getResources().getColor(R.color.mc_rad_but_dw_0));
		delRButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showPickDialog();
			}
		});
		delRButton.inToButton();
//		if(permissionType == 1){
		if(Constants.isNoEditInMcMember(deviceId, deviceType) || permissionType == 1){
			delRButton.setVisibility(View.GONE);
		}else{
			delRButton.setVisibility(View.VISIBLE);
		}
		if(Constants.isNoEditInMcMember(deviceId, deviceType)){
			delRButton.setVisibility(View.GONE);
			member_role_remark.setVisibility(View.GONE);
		}
	}
	private void initPermissionData(){
		permissionList = (List<Permission>)LocationSerializable.getSerializable2File(LocationSerializable.PermissionsList);
		if(null != permissionList){
			dataResult.clear();
			int len = permissionList.size();
			for(int i=0; i<len; i++){
				Permission p = permissionList.get(i);
				p.setIsHave(isHaveP(String.valueOf(p.getId())));
				dataResult.add(p);
			}
		}else{
			Constants.getPermissionsList(mContext, mHandler);
		}
//		pAdapter.notifyDataSetChanged();
//		Permission p1 = new Permission();
//		p1.setValue("查看油耗数据");
//		p1.setIsHave(isHaveP(String.valueOf(21)));
//		p1.setId(21);
//		Permission p2 = new Permission();
//		p2.setValue("查看发动机数据");
//		p2.setIsHave(isHaveP(String.valueOf(20)));
//		p2.setId(20);
//		Permission p3 = new Permission();
//		p3.setValue("查看液压系统数据");
//		p3.setIsHave(isHaveP(String.valueOf(99)));
//		p3.setId(22);
//		dataResult.add(p1);
//		dataResult.add(p2);
//		dataResult.add(p3);
		pAdapter.notifyDataSetChanged();
	}
	
	private void initTitleLayout(){
		TitleView title_layout;
		title_layout = (TitleView)findViewById(R.id.title_layout);
		title_layout.setTitle(memberName);
		title_layout.setLeftImage(-1, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				resultActivity();
				cancel();
			}
		});
		if(!Constants.isNoEditInMcMember(deviceId, deviceType)){
			title_layout.setRightText(-1, null, new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(roleIdS>0){
						String p ="";
						int len = dataResult.size();
						for(int i=0; i<len; i++){
							if(dataResult.get(i).getIsHave()){
								p = p+dataResult.get(i).getId()+",";
							}
						}
						if(null !=p && p.length()>1){
							p = p.substring(0, p.length()-1);
						}
						if(permissionType == 1){
							new AddMemberAsync(mContext,mHandler).execute(
									String.valueOf(memberId),p,String.valueOf(roleIdS),
									String.valueOf(deviceId),member_role_remark.getContent(),"1");
						}else{
							new updateMemberPermissionAsync(mContext,mHandler).execute(
									String.valueOf(memberId),p,String.valueOf(roleIdS),
									String.valueOf(deviceId),member_role_remark.getContent());
						}
						
					}else{
						Constants.MyToast("岗位不能为空！");
					}
				}
			});
			title_layout.setRightTextEdit(true);
		}
		
		if(permissionType == 1){
			title_layout.setRightTextEdit(true);
		}
		
	}

	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_UP) {
//			resultActivity();
			cancel();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.quarters_text:
			if(!Constants.isNoEditInMcMember(deviceId, deviceType))
				changeQuarters((TextView)v);
			break;
		case R.id.member_role_remark:
			gotoEditActivity(member_role_remark,Constants.E_DEVICE_TEXT,Constants.E_ITEMS_role_remark);
			break;
		}
		
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		switch(msg.what){
		case Constants.HANDLER_DELETEMEMBER_SUCCESS:
			isChange = true;
			resultActivity();
			break;
		case Constants.HANDLER_DELETEMBER_FAIL:
			Constants.MyToast("删除成员失败，请重试！");
			break;
		case Constants.HANDLER_ADDMEMBER_SUCCESS:
			isChange = true;
			isSave = true;
			Constants.MyToast((String)msg.obj);
			cancel();
			break;
		case Constants.HANDLER_ADDMEMBER_FAIL:
			cleanRoleIdS();
			Constants.ToastAction((String)msg.obj);
			break;
		case Constants.HANDLER_GETPERMISSIONS_SUCCESS:
			initPermissionData();
			
			break;
		}
		return false;
	}
	
	public void changeQuarters(final TextView v) {
		Bundle bundle=new Bundle();
		bundle.putString(Constants.P_TITLETEXT, name.getText().toString());
		bundle.putInt(Constants.P_EDITRESULTINT, -1);
		bundle.putInt(Constants.P_ITEMTYPE, Constants.E_ITEMS_role_list);
		bundle.putInt(Constants.P_EDITTYPE, Constants.E_DEVICE_LIST);
		bundle.putInt(Constants.P_EDIT_LIST_VALUE1, (int)deviceId);
		bundle.putInt(Constants.P_EDIT_LIST_VALUE2, memberId);
		Constants.toActivityForR(PermissionActivity.this, EditLayoutActivity.class, bundle);
		
		/*new AlertDialog.Builder(this)
				.setTitle("岗位名称")
				.setSingleChoiceItems(itemQuarters, quartersId, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						isChange = true;
						quartersId = which;
						v.setText(itemQuarters[quartersId]);
						dialog.dismiss();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();*/

	}

	private boolean isHaveP(String p){
		if(null != permissionId){
			int len = permissionId.length;
			for(int i=0; i<len; i++){
				if(permissionId[i].equals(p)){
					return true;
				}
			}
		}
		return false;
	}
	private void showPickDialog() {
		View cView = getLayoutInflater().inflate(
				R.layout.layout_dialog, null);
		TextView text = (TextView)cView.findViewById(R.id.dialog_text);
		text.setText("确定要删除该成员？");
		new AlertDialog.Builder(mContext)
				.setNegativeButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						new DevicesDeleteMemberAsync(mContext,mHandler).execute(String.valueOf(memberId),
								String.valueOf(deviceId));
					}
				})
				.setPositiveButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.dismiss();
				
					}
				})
				.setView(cView)
				.show();
	}
	
	private void resultActivity(){
		Intent intent=new Intent();  
        intent.putExtra(Constants.P_ISDELETEMERMBER, isChange);  
        setResult(RESULT_OK, intent);  
        finish();
	}
	private void cancel(){
		if(!Constants.isNoEditInMcMember(deviceId, deviceType)){
			if(!isSave){
				if(permissionType == 1){
					showPickDialog1();
				}else{
					showPickDialog2();
				}
			}else{
				if(permissionType == 1){
					finish();
				}else{
					resultActivity();
				}
			}
		}else{
			finish();
		}
		
	}
	
	private void showPickDialog1() {
		MyDialog dialog = new MyDialog(mContext, R.style.MyDialog,  
		        new MyDialog.LeaveMyDialogListener() {    
		         @Override   
		         public void onClick(View view) {   
		            switch(view.getId()){   
		              case R.id.negative_button:
		            	  finish();
		                        break; 
		              default:
		              break;
		                     }   
		                 }   
		            });   
		dialog.show();
		dialog.setText("确定要放弃添加该成员？");
		dialog.setNegativeText("确定");
	}
	private void showPickDialog2() {
		MyDialog dialog = new MyDialog(mContext, R.style.MyDialog,  
		        new MyDialog.LeaveMyDialogListener() {    
		         @Override   
		         public void onClick(View view) {   
		            switch(view.getId()){   
		              case R.id.negative_button:
		            	  finish();
		                        break; 
		              default:
		              break;
		                     }   
		                 }   
		            });   
		dialog.show();
		dialog.setText("确定要放弃对该成员权限的修改？");
		dialog.setNegativeText("确定");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (resultCode)
		{
		case RESULT_OK:
			Bundle bundle=data.getExtras(); 
			int editType = bundle.getInt(Constants.P_EDITTYPE);
			int itemType = bundle.getInt(Constants.P_ITEMTYPE);
			switch(editType){
			case Constants.E_DEVICE_TEXT:
				String name = bundle.getString(Constants.P_EDITRESULTSTRING);
				if(itemType == Constants.E_ITEMS_role_remark){
					member_role_remark.setContent(name!=null?name:"");
				}
				break;
			case Constants.E_DEVICE_DATA:
				String timeStr = bundle.getString(Constants.P_EDITRESULTSTRING);
				break;
			case Constants.E_DEVICE_LIST:
				EditListInfo eInfo = (EditListInfo)bundle.getSerializable(Constants.P_EDITRESULTITEM);
				if(null != eInfo){
					memberRole = eInfo.getName();
					quarters_text.setText(memberRole);
					roleIdS = (int) eInfo.getId();
					memberPermission = eInfo.getStr1();
					if(null !=memberPermission)
						permissionId = memberPermission.split(",");
					int len = dataResult.size();
					for(int i=0; i<len; i++){
						Permission p = dataResult.get(i);
						p.setIsHave(isHaveP(String.valueOf(p.getId())));
					}
					pAdapter.notifyDataSetChanged();
					
					if(roleIdS == 1){
						showChangeMasterDialog();
					}
				}
				break;
				default:
					break;
			}
			
			break;
		}
	}
	private void gotoEditActivity(AddDeviceItemView view,int editType,int itemType){
		Bundle bundle=new Bundle();
		bundle.putString(Constants.P_TITLETEXT, view.getTitle());
		bundle.putString(Constants.P_EDITRESULTSTRING, view.getContent());
		bundle.putInt(Constants.P_EDITTYPE, editType);
		bundle.putInt(Constants.P_ITEMTYPE, itemType);
		Constants.toActivityForR(this, EditLayoutActivity.class, bundle);
	}
	
	private void showChangeMasterDialog() {
		new AlertDialog.Builder(mContext)
				.setTitle("确定将机器转移他人，且放弃所有权限！")
				.setNegativeButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						changeMaster();
					}
				})
				.setPositiveButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.dismiss();
						
						cleanRoleIdS();
					}
				}).show();
	}
	
	private void changeMaster(){
		isChangeMaster = true;
		String p ="";
		int len = dataResult.size();
		for(int i=0; i<len; i++){
			if(dataResult.get(i).getIsHave()){
				p = p+dataResult.get(i).getId()+",";
			}
		}
		if(null !=p && p.length()>1){
			p = p.substring(0, p.length()-1);
			
		}
		
		new AddMemberAsync(mContext,mHandler).execute(
				String.valueOf(memberId),p,String.valueOf(roleIdS),
				String.valueOf(deviceId),member_role_remark.getContent(),"2");
	}
	private void cleanRoleIdS(){
		isChangeMaster = false;
		quarters_text.setText("");
		roleIdS = -1;
	}
}
