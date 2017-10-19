package com.cloudmachine.activities;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.adapter.ValidCouponAdapter;
import com.cloudmachine.base.BaseFragment;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.ui.home.contract.MyCouponContract;
import com.cloudmachine.ui.home.model.CouponItem;
import com.cloudmachine.ui.home.model.MyCouponModel;
import com.cloudmachine.ui.home.presenter.MyCouponPresenter;

import java.util.List;

/**
 * 项目名称：CloudMachine
 * 类描述：可用优惠券fragment
 * 创建人：shixionglu
 * 创建时间：2017/2/22 上午10:31
 * 修改人：shixionglu
 * 修改时间：2017/2/22 上午10:31
 * 修改备注：
 */

public class AvailableCouponsFragment extends BaseFragment<MyCouponPresenter, MyCouponModel> implements MyCouponContract.View {

    private RecyclerView mRecyclerView;
    private ValidCouponAdapter mValidCouponAdapter;
    private View emptTv;
    private TextView toalAmountTv;


    @Override
    protected void initView() {
        toalAmountTv = (TextView) viewParent.findViewById(R.id.available_toalamount_tv);
        emptTv = viewParent.findViewById(R.id.empt_tv);
        mRecyclerView = (RecyclerView) viewParent.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        mValidCouponAdapter = new ValidCouponAdapter(null, getActivity());
        mRecyclerView.setAdapter(mValidCouponAdapter);
        mPresenter.getAvaildCouponList(UserHelper.getMemberId(getActivity()));
    }

    @Override
    protected void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_available_coupons;
    }


    @Override
    public void updateAvaildCouponListView(int sumNum,int sumAmount, List<CouponItem> couponBOList) {
        ((ViewCouponActivityNew)getActivity()).setAvaidNum(sumNum);
        if (null != couponBOList && couponBOList.size() > 0) {
            ((ViewGroup)toalAmountTv.getParent()).setVisibility(View.VISIBLE);
            toalAmountTv.setText("总金额：" + sumAmount + "元");
            mRecyclerView.setVisibility(View.VISIBLE);
            emptTv.setVisibility(View.GONE);
            mValidCouponAdapter.updateItems(couponBOList);
        } else {
            ((ViewGroup)toalAmountTv.getParent()).setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);
            emptTv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void updateInavaildCouponListView(int sumNum,List<CouponItem> couponBOList) {

    }

    @Override
    public void updateMyCouponDetailListView(int sumAmount, List<CouponItem> couponBOList) {

    }
}
