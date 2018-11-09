package com.cloudmachine.ui.home.activity;

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
import com.cloudmachine.adapter.holder.BaseHolder;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.bean.EmunBean;
import com.cloudmachine.bean.TypeItem;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.utils.Constants;

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
        updateView();
    }

    public void updateView(){
        mRxManager.add(Api.getDefault(HostType.HOST_LARK).getEnum("relationShip").compose(RxHelper.<List<EmunBean>>handleResult()).subscribe(new RxSubscriber<List<EmunBean>>(mContext) {
            @Override
            protected void _onNext(List<EmunBean> items) {
                RelationAdapter adapter = new RelationAdapter(mContext, items);
                adapter.setOnItemClickListener(RelationActivity.this);
                mRecyclerView.setAdapter(adapter);

            }

            @Override
            protected void _onError(String message) {

            }
        }));


    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void onItemClick(View view, int position) {
        Object obj = view.getTag();
        if (obj!=null){
            Intent intent=new Intent();
            intent.putExtra(Constants.TYPE_ITEM,(TypeItem) obj);
            intent.putExtra(Constants.RELATION_POSITION,position);
            setResult(RESULT_OK,intent);
            finish();
        }
    }

    private class RelationAdapter extends BaseRecyclerAdapter<EmunBean> {


        public RelationAdapter(Context context, List<EmunBean> items) {
            super(context, items);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_relation, parent, false);
            return new RelationHolder(view);
        }


    }

    private class RelationHolder extends BaseHolder<EmunBean> {

        TextView nameTv;
        ImageView statusImg;

        public RelationHolder(View itemView) {
            super(itemView);
            nameTv = (TextView) itemView.findViewById(R.id.item_relation_name);
            statusImg = (ImageView) itemView.findViewById(R.id.item_relation_status);
        }

        @Override
        public void initViewHolder(EmunBean item) {

        }

        @Override
        public void initViewHolder(EmunBean item, int position) {
            nameTv.setText(item.getValueName());
            if (selectPos>=0&&selectPos==position){
                statusImg.setVisibility(View.VISIBLE);
            }else{
                statusImg.setVisibility(View.GONE);
            }
        }
    }


}
