package com.cloudmachine.activities;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.base.BaseActivity;
import com.cloudmachine.utils.MemeberKeeper;
import com.cloudmachine.utils.widgets.TitleView;

import java.io.File;

import cn.jpush.android.api.JPushInterface;

public class SettingActivity extends BaseActivity implements OnClickListener {

	private final String LOG_TAG = "SetingActivity";
	private RelativeLayout set_clear;
	private RelativeLayout set_suggest;
	private Button set_light;
	private Button exitLogin;
	private RelativeLayout set_qrcode;
	private RelativeLayout button2;
	private RelativeLayout set_about;
	private RelativeLayout set_message;
	private LinearLayout exitlayout;
	private SeekBar seekBar;
	private TextView text2;
	private ImageView backImg; 
	private PopupWindow mpopupWindow;
	private TitleView title_layout_set;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set);
		 
		initView();

	}

	private void initView() {
		//set_clear=(RelativeLayout) findViewById(R.id.set_clear);
		set_suggest =(RelativeLayout) findViewById(R.id.set_suggest);
		set_light = (Button)findViewById(R.id.set_light);
		set_qrcode = (RelativeLayout)findViewById(R.id.set_qrcode);
		button2 = (RelativeLayout)findViewById(R.id.my_pwd);
		exitlayout = (LinearLayout)findViewById(R.id.exitlayout);
		set_about = (RelativeLayout)findViewById(R.id.about_cloud);
		set_message = (RelativeLayout)findViewById(R.id.set_message);
//		backImg = (ImageView)findViewById(R.id.backImg);
		exitLogin = (Button)findViewById(R.id.exitLogin);
		title_layout_set = (TitleView)findViewById(R.id.title_layout_set);
		title_layout_set.setTitle("设置");
		title_layout_set.setLeftImage(R.drawable.back_item_selector, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		exitlayout.setOnClickListener(this);
//		exitLogin.setOnClickListener(this);
		button2.setOnClickListener(this);
		set_light.setOnClickListener(this);
		set_suggest.setOnClickListener(this);
		//set_clear.setOnClickListener(this);
		set_qrcode.setOnClickListener(this);
		set_about.setOnClickListener(this);
		set_message.setOnClickListener(this);
//		backImg.setOnClickListener(this);
	}
	private void  popWindow(){
    }
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.set_clear:
//			clearCache();
//			Toast.makeText(this, "已清除缓存！", Toast.LENGTH_LONG).show();
//			break;
		case R.id.set_suggest:
//		Toast.makeText(this, "你喝酒了·！", Toast.LENGTH_LONG).show();
		startActivity(new Intent(SettingActivity.this,SuggestBackActivity.class));
		break;
		case R.id.set_light:
			popWindow();
			break;
		case R.id.set_qrcode:
			startActivity(new Intent(SettingActivity.this,QrCodeActivity.class));
			break;
		case R.id.my_pwd:
			Intent intent = new Intent(SettingActivity.this,FindPasswordActivity.class);
			intent.putExtra("type", 2);
			startActivity(intent);
			break;
		case R.id.about_cloud:
			startActivity(new Intent(SettingActivity.this,AboutCloudActivity.class));
			break;
		case R.id.exitlayout:
			showPopMenu();
			break;
		case R.id.bt_cancel:
			mpopupWindow.dismiss();
			break;
		case R.id.bt_exitLogin:
			JPushInterface.setAliasAndTags(getApplicationContext(), "", null, null);
			MemeberKeeper.clearOauth(SettingActivity.this);
			SettingActivity.this.finish();
			break;
		case R.id.set_message:
			startActivity(new Intent(SettingActivity.this,SettingMessageActivity.class));
			break;
		default:
			break;
		}
		
	}
	
	private void showPopMenu() {
		View view = View.inflate(getApplicationContext(), R.layout.popup_menu, null);
		//RelativeLayout rl_weixin = (RelativeLayout) view.findViewById(R.id.rl_weixin);
		//RelativeLayout rl_weibo = (RelativeLayout) view.findViewById(R.id.rl_weibo);
		//RelativeLayout rl_duanxin = (RelativeLayout) view.findViewById(R.id.rl_duanxin);
		Button bt_cancle = (Button) view.findViewById(R.id.bt_cancel);
        Button bt_exitLogin = (Button)view.findViewById(R.id.bt_exitLogin);
		//rl_weixin.setOnClickListener(this);
		//rl_weibo.setOnClickListener(this);
		//rl_duanxin.setOnClickListener(this);
		bt_cancle.setOnClickListener(this);
		bt_exitLogin.setOnClickListener(this);
	
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				mpopupWindow.dismiss();
			}
		});
		
		view.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in));
		LinearLayout ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
		ll_popup.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.push_bottom_in));
		
		if(mpopupWindow==null){
			mpopupWindow = new PopupWindow(this);
			mpopupWindow.setWidth(LayoutParams.MATCH_PARENT);
			mpopupWindow.setHeight(LayoutParams.MATCH_PARENT);
			mpopupWindow.setBackgroundDrawable(new BitmapDrawable());

			mpopupWindow.setFocusable(true);
			mpopupWindow.setOutsideTouchable(true);
			mpopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		}
		
		mpopupWindow.setContentView(view);
		mpopupWindow.showAtLocation(view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
		mpopupWindow.update();
	}
	
	private boolean clearCache(){
	       boolean flag = false;
	       String path = Environment.getExternalStorageDirectory()+"/cloudmachine/images";
	       File file = new File(path);
	       if (!file.exists()) {
	         return flag;
	       }
	       if (!file.isDirectory()) {
	         return flag;
	       }
	       String[] tempList = file.list();
	       File temp = null;
	       for (int i = 0; i < tempList.length; i++) {
	          if (path.endsWith(File.separator)) {
	             temp = new File(path + tempList[i]);
	          } else {
	              temp = new File(path + File.separator + tempList[i]);
	          }
	          if (temp.isFile()) {
	             temp.delete();
	          }
	       }
	       return flag;
	}

	@Override
	protected void onResume() {
		if(MemeberKeeper.getOauth(SettingActivity.this)==null){
			Intent intent = new Intent(SettingActivity.this,LoginActivity.class);
			intent.putExtra("flag", 2);
			startActivity(intent);
			finish();
		}
		super.onResume();
	}
	
	

}
