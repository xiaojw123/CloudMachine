package com.cloudmachine.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.autolayout.widgets.TitleView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.net.task.GetWorkDetailAsync;
import com.cloudmachine.struc.BOInfo;
import com.cloudmachine.struc.BOInfo.WorkDetailBean;
import com.cloudmachine.struc.CWInfo;
import com.cloudmachine.struc.ScheduleBean;
import com.cloudmachine.utils.Constants;

import java.util.ArrayList;

/**
 * @author shixionglu 单个报修记录基本信息展示页
 */
public class RepairBasicInfomationActivity extends BaseAutoLayoutActivity
        implements Callback {

    private Handler   mHandler;
    private Context   mContext;
    private TitleView title_layout;
    private String    orderNum, flag;
    private TextView repair_basic_text1, repair_basic_text2, repair_basic_text3, repair_basic_text4,
            repair_basic_text5, repair_basic_text6, repair_basic_text7, repair_basic_text8;
    private View line5, layout5;
    private LinearLayout add_list_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_basic_infomation);
        mHandler = new Handler(this);
        mContext = this;
        getIntentData();
        initView();
    }

    @Override
    public void initPresenter() {

    }

    private void getIntentData() {
        // TODO Auto-generated method stub
        Intent intent = this.getIntent();
        orderNum = intent.getStringExtra("orderNum");
        flag = intent.getStringExtra("flag");
        new GetWorkDetailAsync(mHandler, mContext, orderNum, flag).execute();
    }

    private void initView() {
        initTitleLayout();
        repair_basic_text1 = (TextView) findViewById(R.id.repair_basic_text1);
        repair_basic_text2 = (TextView) findViewById(R.id.repair_basic_text2);
        repair_basic_text3 = (TextView) findViewById(R.id.repair_basic_text3);
        repair_basic_text4 = (TextView) findViewById(R.id.repair_basic_text4);
        repair_basic_text5 = (TextView) findViewById(R.id.repair_basic_text5);
        repair_basic_text6 = (TextView) findViewById(R.id.repair_basic_text6);
        repair_basic_text7 = (TextView) findViewById(R.id.repair_basic_text7);
        repair_basic_text8 = (TextView) findViewById(R.id.repair_basic_text8);
        line5 = findViewById(R.id.line5);
        layout5 = findViewById(R.id.layout5);
        add_list_layout = (LinearLayout) findViewById(R.id.add_list_layout);

    }

    private void initTitleLayout() {

        title_layout = (TitleView) findViewById(R.id.title_layout);
        title_layout.setTitle(getResources().getString(R.string.detail));
        title_layout.setLeftOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
    }

    private void setItemText(String str1, String str2, String str3, String str4,
                             String str5, String str6, String str7, String str8) {
        repair_basic_text1.setText(Constants.toViewString(str1));
        repair_basic_text2.setText(Constants.toViewString(str2));
        repair_basic_text3.setText(Constants.toViewString(str3));
        repair_basic_text4.setText(Constants.toViewString(str4));
        repair_basic_text5.setText(Constants.toViewString(str5));
        repair_basic_text6.setText(Constants.toViewString(str6));
        repair_basic_text7.setText(Constants.toViewString(str7));
        repair_basic_text8.setText(Constants.toViewString(str8));
        if (null == str5) {
            line5.setVisibility(View.GONE);
            layout5.setVisibility(View.GONE);
        }
    }


    //在代码中动态添加
    public void addListItem(ArrayList<ScheduleBean> data) {
        //	ArrayList<ScheduleBean> data =new ArrayList<ScheduleBean> ();
        //	int len = data2.size();
        //	for(int i=0; i<len; i++){
        //		data.add(data2.get(len-1-i));
        //	}
        add_list_layout.removeAllViews();

        for (int i = 0; i < data.size(); i++) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.repair_info_add_layout,
                    null);


            TextView text_line1 = (TextView) layout.findViewById(R.id.text_line1);
            TextView text_line2 = (TextView) layout.findViewById(R.id.text_line2);
            ImageView left_iamge = (ImageView) layout.findViewById(R.id.left_iamge);
            TextView text1 = (TextView) layout.findViewById(R.id.text1);
            TextView text2 = (TextView) layout.findViewById(R.id.text2);
            if (data.size() == 1) {
                text_line1.setVisibility(View.INVISIBLE);
                text_line2.setVisibility(View.INVISIBLE);
                left_iamge.setBackgroundResource(R.drawable.schedual_circle_select);
            } else {
                if (i == 0) {
                    text_line1.setVisibility(View.INVISIBLE);
                    text_line2.setVisibility(View.VISIBLE);
                    left_iamge.setBackgroundResource(R.drawable.schedual_circle_select);
                } else {
                    text_line1.setVisibility(View.VISIBLE);
                    text_line2.setVisibility(View.VISIBLE);
                    left_iamge.setBackgroundResource(R.drawable.schedual_circle_unselect);
                }
            }
            text1.setText(Constants.toViewString(data.get(i).getDesc()));
            text2.setText(Constants.toViewString(data.get(i).getCreateTime()));
            if (i == 0) {
                text1.setTextColor(getResources().getColor(R.color.public_blue_bg));
                text2.setTextColor(getResources().getColor(R.color.public_blue_bg));
            } else {
                text1.setTextColor(getResources().getColor(R.color.public_black_9));
                text2.setTextColor(getResources().getColor(R.color.public_black_9));
            }


            add_list_layout.addView(layout);
        }

    }

    @Override
    public boolean handleMessage(Message msg) {
        // TODO Auto-generated method stub
        switch (msg.what) {
            case Constants.HANDLER_GET_BODETAIL_SUCCESS:

                BOInfo boInfo = (BOInfo) msg.obj;
                ArrayList<ScheduleBean> boScheduleBeans = boInfo.getSchedule();
                addListItem(boScheduleBeans);
                WorkDetailBean workDetail = boInfo.getWorkDetail();
                setItemText(workDetail.getVprodname(), workDetail.getVbrandname(), workDetail.getVmaterialname(),
                        workDetail.getVmachinenum(), null, workDetail.getVmacopname(), workDetail.getVmacoptel(),
                        workDetail.getVdiscription());
                break;
            case Constants.HANDLER_GET_CWDETAIL_SUCCESS:
                CWInfo cwInfo = (CWInfo) msg.obj;
                ArrayList<ScheduleBean> schedule = cwInfo.getSchedule();
                addListItem(schedule);
                com.cloudmachine.struc.WorkDetailBean workDetail2 = cwInfo.getWorkDetail();

                setItemText(workDetail2.getVprodname(), workDetail2.getVbrandname(), workDetail2.getVmaterialname(),
                        workDetail2.getVmachinenum(), null, workDetail2.getVfieldcontactor(),
                        workDetail2.getVcontactphone(), workDetail2.getCusdemanddesc());


                break;
            case Constants.HANDLER_GET_WORKDETAIL_FAILD:
                Constants.MyToast((String) msg.obj);
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    protected void onResume() {
        //	MobclickAgent.onPageStart(UMengKey.time_repair_detail);
        super.onResume();
    }

    @Override
    protected void onPause() {
        //MobclickAgent.onPageEnd(UMengKey.time_repair_detail);
        super.onPause();
    }
}
