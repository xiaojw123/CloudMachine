package com.cloudmachine.ui.home.activity;

import android.content.Context;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudmachine.BuildConfig;
import com.cloudmachine.R;
import com.cloudmachine.adapter.BaseRecyclerAdapter;
import com.cloudmachine.adapter.decoration.LineItemDecoration;
import com.cloudmachine.adapter.holder.BaseHolder;
import com.cloudmachine.net.api.ApiConstants;

import java.util.ArrayList;
import java.util.List;

public class HostConfigActivity extends AppCompatActivity implements BaseRecyclerAdapter.OnItemClickListener {

    int selPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_config);
        selPos=getIntent().getIntExtra(BuildConfig.BUILD_TYPE,0);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.config_rlv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new LineItemDecoration(this));
        List<String> hostList = new ArrayList<>();
        hostList.add("线上环境");
        hostList.add("预发布(218)");
        hostList.add("测试(109)");
        hostList.add("联调(179)");
        HostAdapter adapter = new HostAdapter(this, hostList);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        ApiConstants.saveHostConfig(this, position);
        finish();
    }

    private class HostAdapter extends BaseRecyclerAdapter<String> {


        private HostAdapter(Context context, List<String> items) {
            super(context, items);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_host, parent, false);
            return new HostHolder(itemView);
        }
    }

    private class HostHolder extends BaseHolder<String> {
        TextView contentTv;
        ImageView statusImg;

        private HostHolder(View itemView) {
            super(itemView);
            contentTv = (TextView) itemView.findViewById(R.id.item_host_content);
            statusImg = (ImageView) itemView.findViewById(R.id.item_host_status);
        }

        @Override
        public void initViewHolder(String item) {

        }

        @Override
        public void initViewHolder(String item,int position) {
            contentTv.setText(item);
            if (position==selPos){
                statusImg.setVisibility(View.VISIBLE);
            }else{
                statusImg.setVisibility(View.GONE);
            }
        }
    }
}
