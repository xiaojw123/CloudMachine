package com.cloudmachine.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.cloudmachine.R;
import com.cloudmachine.adapter.OwnDeviceAdapter;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.bean.McDeviceInfo;
import com.cloudmachine.helper.MobEvent;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.utils.Constants;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

public class CheckMachineActivity extends BaseAutoLayoutActivity implements Callback, OnItemClickListener {

    private ListView lvOwnDevice;
    private Context mContext;
    private Handler mHandler;
    private OwnDeviceAdapter ownDeviceAdapter;
    List<McDeviceInfo> infos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_machine);
        mContext = this;
        mHandler = new Handler(this);
        initView();



//		http://api.test.cloudm.com/cloudm3/yjx/device/getDeviceByKey?osPlatform=Android&osVersion=3.1&memberId=142&type=1
    }

    @Override
    public void initPresenter() {
    }

    @Override
    protected void onResume() {
        //MobclickAgent.onPageStart(UMengKey.time_repair_create_device);
        super.onResume();
        MobclickAgent.onEvent(this, MobEvent.TIME_REPAIR_CREATE_DEVICE);
    }

    @Override
    protected void onPause() {
        //MobclickAgent.onPageEnd(UMengKey.time_repair_create_device);
        super.onPause();
    }

    private void initView() {
        lvOwnDevice = (ListView) findViewById(R.id.listView);
        lvOwnDevice.setOnItemClickListener(this);
       infos = UserHelper.getMyDevices();
        if (infos != null && infos.size() > 0) {
            ownDeviceAdapter = new OwnDeviceAdapter(mContext, mHandler, infos);
            lvOwnDevice.setAdapter(ownDeviceAdapter);
        }
    }


    @Override
    public boolean handleMessage(Message arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        setSelectionItem(position);
        Intent intent = new Intent();
        intent.putExtra("selInfo", selInfo);
        setResult(Constants.CLICK_POSITION, intent);
        finish();
    }

    private void setSelectionItem(int position) {
        for (int i=0;i<infos.size();i++){
           McDeviceInfo info=infos.get(i);
            if (i==position){
                selInfo=info;
                info.setSelected(true);
            }else{
                info.setSelected(false);
            }
        }
    }
    McDeviceInfo selInfo;

}
