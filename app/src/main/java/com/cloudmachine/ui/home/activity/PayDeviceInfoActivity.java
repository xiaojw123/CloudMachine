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
import com.cloudmachine.bean.RepairDetail;
import com.cloudmachine.bean.WorkDetailBean;
import com.cloudmachine.helper.MobEvent;
import com.cloudmachine.utils.Constants;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

public class PayDeviceInfoActivity extends BaseAutoLayoutActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_device_info);
        RepairDetail detail =getIntent().getParcelableExtra(Constants.REPAIR_DETAIL);
        TextView imgTitleTv = (TextView) findViewById(R.id.device_info_title);
        TextView emptTv = (TextView) findViewById(R.id.pay_di_empty_tv);
        TextView noTv = (TextView) findViewById(R.id.pay_di_dno);
        TextView locTv = (TextView) findViewById(R.id.pay_di_loc);
        TextView desTv = (TextView) findViewById(R.id.pay_di_des);
        LinearLayout  detailLlt = (LinearLayout) findViewById(R.id.pay_di_detail_llt);
        TextView  tvBrand = (TextView) findViewById(R.id.tv_device_brand);
        TextView  tvModel = (TextView) findViewById(R.id.tv_device_model);
        RecyclerView infoImgRlv = (RecyclerView) findViewById(R.id.device_info_rlv);
        //刷新耗件详情列表
        if (detail == null) {
            emptTv.setVisibility(View.VISIBLE);
            detailLlt.setVisibility(View.GONE);
            return;
        }
        List<String> logoList=detail.getAttachmentUrls();
        if (logoList != null && logoList.size() > 0) {
            imgTitleTv.setVisibility(View.VISIBLE);
            infoImgRlv.setVisibility(View.VISIBLE);
            infoImgRlv.setLayoutManager(new GridLayoutManager(this, 3));
            infoImgRlv.addItemDecoration(new SpaceItemDecoration(this, 5));
            PhotoListAdapter adapter = new PhotoListAdapter();
            adapter.updateItems(logoList);
            infoImgRlv.setAdapter(adapter);
        }
        //设置机器品牌
        tvBrand.setText(detail.getBrandName());
        //设置机器型号
        tvModel.setText(detail.getModelName());
        noTv.setText(String.valueOf(detail.getModelId()));
        locTv.setText(detail.getAddressDetail());
        desTv.setText(detail.getServiceDesc());

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onEvent(this, MobEvent.REPAIR_DEVICE_INFO);
    }

    @Override
    public void initPresenter() {

    }


}
