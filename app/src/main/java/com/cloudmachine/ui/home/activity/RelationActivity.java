package com.cloudmachine.ui.home.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.adapter.BaseRecyclerAdapter;
import com.cloudmachine.adapter.decoration.LineItemDecoration;
import com.cloudmachine.adapter.decoration.SpaceItemDecoration;
import com.cloudmachine.adapter.holder.BaseHolder;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.ui.home.contract.ExtrContract;
import com.cloudmachine.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class RelationActivity extends BaseAutoLayoutActivity implements BaseRecyclerAdapter.OnItemClickListener {

    RecyclerView mRecyclerView;
    int selectPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relation);
        selectPos=getIntent().getIntExtra(Constants.RELATION_POSITION,-1);
        mRecyclerView = (RecyclerView) findViewById(R.id.relation_rlv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new LineItemDecoration(this));
        List<String> items = new ArrayList<>();
        items.add("家人");
        items.add("朋友");
        items.add("同事");
//        items.add("父子(父女)");
//        items.add("母子(母女)");
//        items.add("配偶");
//        items.add("朋友");
        RelationAdapter adapter = new RelationAdapter(mContext, items);
        adapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(adapter);

    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void onItemClick(View view, int position) {
        Object obj = view.getTag();
        if (obj!=null){
            Intent intent=new Intent();
            intent.putExtra(Constants.RELATION,(String) obj);
            intent.putExtra(Constants.RELATION_POSITION,position);
            setResult(RESULT_OK,intent);
            finish();
        }
    }

    private class RelationAdapter extends BaseRecyclerAdapter<String> {


        public RelationAdapter(Context context, List<String> items) {
            super(context, items);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_relation, parent, false);
            return new RelationHolder(view);
        }


    }

    private class RelationHolder extends BaseHolder<String> {

        TextView nameTv;
        ImageView statusImg;

        public RelationHolder(View itemView) {
            super(itemView);
            nameTv = (TextView) itemView.findViewById(R.id.item_relation_name);
            statusImg = (ImageView) itemView.findViewById(R.id.item_relation_status);
        }

        @Override
        public void initViewHolder(String item) {

        }

        @Override
        public void initViewHolder(String item, int position) {
            nameTv.setText(item);
            if (selectPos>=0&&selectPos==position){
                statusImg.setVisibility(View.VISIBLE);
            }else{
                statusImg.setVisibility(View.GONE);
            }
        }
    }


}
