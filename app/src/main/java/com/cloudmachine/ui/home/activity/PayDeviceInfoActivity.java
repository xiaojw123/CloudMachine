package com.cloudmachine.ui.home.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.adapter.PhotoListAdapter;
import com.cloudmachine.adapter.decoration.SpaceItemDecoration;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.bean.WorkDetailBean;
import com.cloudmachine.helper.MobEvent;
import com.cloudmachine.utils.Constants;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

public class PayDeviceInfoActivity extends BaseAutoLayoutActivity {
    private TextView tvBrand;
    private TextView tvModel;
    private TextView emptTv;
    private LinearLayout detailLlt;
    private TextView desTv;
    private TextView noTv;
    private TextView locTv;
    private RecyclerView infoImgRlv;
    ArrayList<String> logoList;
    private TextView imgTitleTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_device_info);
        MobclickAgent.onEvent(this, MobEvent.REPAIR_DEVICE_INFO);
        WorkDetailBean workDetail = (WorkDetailBean) getIntent().getSerializableExtra(Constants.WORK_DETAIL);
        String flag=getIntent().getStringExtra(Constants.FLAG);
        logoList = getIntent().getStringArrayListExtra(Constants.LOGO_LIST);
        imgTitleTv = (TextView) findViewById(R.id.device_info_title);
        emptTv = (TextView) findViewById(R.id.pay_di_empty_tv);
        noTv = (TextView) findViewById(R.id.pay_di_dno);
        locTv = (TextView) findViewById(R.id.pay_di_loc);
        desTv = (TextView) findViewById(R.id.pay_di_des);
        detailLlt = (LinearLayout) findViewById(R.id.pay_di_detail_llt);
        tvBrand = (TextView) findViewById(R.id.tv_device_brand);
        tvModel = (TextView) findViewById(R.id.tv_device_model);
        infoImgRlv = (RecyclerView) findViewById(R.id.device_info_rlv);
        //刷新耗件详情列表
        if (workDetail == null) {
            emptTv.setVisibility(View.VISIBLE);
            detailLlt.setVisibility(View.GONE);
            return;
        }
        if (logoList != null && logoList.size() > 0) {
            imgTitleTv.setVisibility(View.VISIBLE);
            infoImgRlv.setVisibility(View.VISIBLE);
            infoImgRlv.setLayoutManager(new GridLayoutManager(this, 3));
            infoImgRlv.addItemDecoration(new SpaceItemDecoration(this, 5));
            PhotoListAdapter adapter= new PhotoListAdapter();
            adapter.updateItems(logoList);
            infoImgRlv.setAdapter(adapter);
        }
        //设置机器品牌
        tvBrand.setText(workDetail.getVbrandname());
        //设置机器型号
        tvModel.setText(workDetail.getVmaterialname());
//        rdDevicenameTv.setText(detailBean.getVbrandname());
//        rdDevicenoTv.setText(detailBean.getVmaterialname());
//        rdDescriptionTv.setText(detailBean.getVdiscription());

        noTv.setText(workDetail.getVmachinenum());
        locTv.setText(workDetail.getVworkaddress());
        String description;
        if ("0".equals(flag)){
            description=workDetail.getVdiscription();
        }else{
            description=workDetail.getCusdemanddesc();
        }
        desTv.setText(description);

    }

    @Override
    public void initPresenter() {

    }



}
