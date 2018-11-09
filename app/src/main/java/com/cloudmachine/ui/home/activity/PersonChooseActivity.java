package com.cloudmachine.ui.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.adapter.PersonItemAdapter;
import com.cloudmachine.adapter.decoration.LineItemDecoration;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.base.Operator;
import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.ToastUtils;
import com.cloudmachine.widget.CommonTitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PersonChooseActivity extends BaseAutoLayoutActivity implements View.OnClickListener {
    public static final int SEL_OPERATOR_COMPLETE = 0x14;

    @BindView(R.id.person_choose_ctv)
    CommonTitleView mTitleView;
    @BindView(R.id.person_choose_rlv)
    RecyclerView mRecyclerView;
    PersonItemAdapter mAdapter;
    @BindView(R.id.person_choose_empty)
    TextView mEmptyTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_choose);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mTitleView.setRightClickListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new LineItemDecoration(this));
        mRxManager.add(Api.getDefault(HostType.HOST_LARK).getMchineOperators().compose(RxHelper.<List<Operator>>handleResult()).subscribe(new RxSubscriber<List<Operator>>(mContext) {
            @Override
            protected void _onNext(List<Operator> operators) {
                if (operators != null && operators.size() > 0) {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mEmptyTv.setVisibility(View.GONE);
                    mAdapter = new PersonItemAdapter(mContext, operators);
                    List<Operator> operatorItems = getIntent().getParcelableArrayListExtra(Constants.OPERATOR_LIST);
                    mAdapter.setSelOperators(operatorItems);
                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    mRecyclerView.setVisibility(View.GONE);
                    mEmptyTv.setVisibility(View.VISIBLE);
                }
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(mContext, message);

            }
        }));
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void onClick(View v) {
        Intent data = new Intent();
        if (mAdapter != null) {
            List<Operator> items = mAdapter.getSelOperators();
            data.putParcelableArrayListExtra(Constants.OPERATOR_LIST, (ArrayList<Operator>) items);
        }
        setResult(SEL_OPERATOR_COMPLETE, data);
        finish();
    }


    public void updateRightTitleText(int len) {
        if (len > 0) {
            mTitleView.setRightTextColor(getResources().getColor(R.color.cor8));
            mTitleView.setRightTextEnable(true);
            mTitleView.setRightText(getResources().getString(R.string.finish) + "(" + len + ")");
        } else {
            mTitleView.setRightTextColor(getResources().getColor(R.color.cor12));
            mTitleView.setRightTextEnable(false);
            mTitleView.setRightText(getResources().getString(R.string.finish));
        }
    }

}
