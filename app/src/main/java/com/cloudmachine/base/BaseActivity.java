package com.cloudmachine.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cloudmachine.utils.AppManager;
import com.cloudmachine.utils.UMListUtil;
import com.umeng.analytics.MobclickAgent;

public class BaseActivity extends AppCompatActivity{



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		 UMListUtil.getUMListUtil().sendStruEvent(this.getClass().getSimpleName(),this);//发送结构化事件
		super.onCreate(savedInstanceState);
		AppManager.getAppManager().addActivity(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		UMListUtil.getUMListUtil().startCustomEvent(this);//页面时长统计开始
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		UMListUtil.getUMListUtil().endCustomEvent(this);//页面时长统计结束
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		UMListUtil.getUMListUtil().removeList(this.getClass().getSimpleName());//页面销毁,集合移除
		super.onDestroy();

	}

}
