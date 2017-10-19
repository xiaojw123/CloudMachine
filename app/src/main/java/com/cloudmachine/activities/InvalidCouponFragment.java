package com.cloudmachine.activities;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cloudmachine.R;
import com.cloudmachine.adapter.InvalidCouponAdapter;
import com.cloudmachine.base.BaseFragment;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.ui.home.contract.MyCouponContract;
import com.cloudmachine.ui.home.model.CouponItem;
import com.cloudmachine.ui.home.model.MyCouponModel;
import com.cloudmachine.ui.home.presenter.MyCouponPresenter;

import java.util.List;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/2/22 上午10:44
 * 修改人：shixionglu
 * 修改时间：2017/2/22 上午10:44
 * 修改备注：
 */

public class InvalidCouponFragment extends BaseFragment<MyCouponPresenter, MyCouponModel> implements MyCouponContract.View {

    private RecyclerView mRecyclerView;
    private InvalidCouponAdapter mInvalidCouponAdapter;
    private View empTv;


    @Override
    protected void initView() {
        empTv = viewParent.findViewById(R.id.empt_tv);
        mRecyclerView = (RecyclerView) viewParent.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        mInvalidCouponAdapter = new InvalidCouponAdapter(null, getActivity());
        mRecyclerView.setAdapter(mInvalidCouponAdapter);
        mPresenter.getInavaildCouponList(UserHelper.getMemberId(getActivity()));
    }


    @Override
    protected void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_invali_coupon;
    }

    @Override
    public void updateAvaildCouponListView(int sumNum,int sumAmount, List<CouponItem> couponBOList) {

    }

    @Override
    public void updateInavaildCouponListView(int sumNum,List<CouponItem> couponBOList) {
        ((ViewCouponActivityNew)getActivity()).setInvaildNum(sumNum);
        if (null != couponBOList && couponBOList.size() > 0) {
            empTv.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mInvalidCouponAdapter.updateItems(couponBOList);
        } else {
            empTv.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void updateMyCouponDetailListView(int sumAmount,List<CouponItem> couponBOList) {

    }
}
