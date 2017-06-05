package com.cloudmachine.ui.home.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cloudmachine.R;
import com.cloudmachine.adapter.TrackHistoryAdapter;
import com.cloudmachine.adapter.decoration.SpaceItemDecoration;
import com.cloudmachine.autolayout.widgets.RadiusButtonView;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.DensityUtil;

public class TrackHistoryActivity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView mRecyclerView;
    RadiusButtonView mQueryBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_history);
        initView();
    }

    private void initView() {
        mRecyclerView=(RecyclerView) findViewById(R.id.track_history_item_rlv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new TrackHistoryAdapter(this));
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(this, DensityUtil.dip2px(this,7),true));
        mQueryBtn=(RadiusButtonView) findViewById(R.id.track_history_query_btn);
        mQueryBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        setResult(Constants.RESULT_QUERY_BY_TIME);
        finish();
    }
}
