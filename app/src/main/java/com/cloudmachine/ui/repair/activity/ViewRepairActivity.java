package com.cloudmachine.ui.repair.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.cloudmachine.R;
import com.cloudmachine.autolayout.widgets.TitleView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.listener.RecyclerItemClickListener;
import com.cloudmachine.recycleadapter.ValidCouponAdapter;
import com.cloudmachine.recycleadapter.decoration.GridSpacingItemDecoration;
import com.cloudmachine.struc.CouponInfo;

import java.math.BigDecimal;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/4/6 上午10:23
 * 修改人：shixionglu
 * 修改时间：2017/4/6 上午10:23
 * 修改备注：
 */

public class ViewRepairActivity extends BaseAutoLayoutActivity {

    @BindView(R.id.title_layout)
    TitleView    mTitleLayout;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private ArrayList<CouponInfo> mCouponData;


    private Context mContext;
    private ValidCouponAdapter mValidCouponAdapter;
    private long mCouponId = -1;
    private BigDecimal mAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewrepair);
        ButterKnife.bind(this);
        mContext = this;
        mCouponData = new ArrayList<>();
        getIntentData();
        initView();
    }

    private void getIntentData() {

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            mCouponData = (ArrayList<CouponInfo>) bundle.getSerializable("couponData");
        }
    }

    private void initView() {

        initTitleLayout();
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, getResources().getDimensionPixelSize(R.dimen.padding_middle), true));
        mRecyclerView.setHasFixedSize(true);
        mValidCouponAdapter = new ValidCouponAdapter(mCouponData, ViewRepairActivity.this);
        mRecyclerView.setAdapter(mValidCouponAdapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(ViewRepairActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mCouponId = mCouponData.get(position).getId();
                mAmount = mCouponData.get(position).getAmount();
                Intent intent = new Intent();
                intent.putExtra("couponId", mCouponData);
                intent.putExtra("amount", String.valueOf(mAmount));
                ViewRepairActivity.this.setResult(RESULT_OK,intent);
                finish();
            }
        }));

    }

    private void initTitleLayout() {

        mTitleLayout.setTitle("查看优惠券");
        mTitleLayout.setLeftImage(-1, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void initPresenter() {

    }
}
