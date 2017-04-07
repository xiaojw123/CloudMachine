package com.cloudmachine.activities;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.cloudmachine.R;
import com.cloudmachine.adapter.GetCouponAdapter;
import com.cloudmachine.net.task.GetCouponAsync;
import com.cloudmachine.recycleadapter.ValidCouponAdapter;
import com.cloudmachine.recycleadapter.decoration.GridSpacingItemDecoration;
import com.cloudmachine.struc.CouponInfo;
import com.cloudmachine.utils.Constants;

import java.util.ArrayList;

/**
 * 项目名称：CloudMachine
 * 类描述：可用优惠券fragment
 * 创建人：shixionglu
 * 创建时间：2017/2/22 上午10:31
 * 修改人：shixionglu
 * 修改时间：2017/2/22 上午10:31
 * 修改备注：
 */

public class AvailableCouponsFragment extends Fragment implements Handler.Callback{

    private  View    viewParent;
    private Context mContext;
    private  Handler mHandler;
    private ListView lvAvavilableCoupon;
    private ArrayList<CouponInfo> dataList;
    private GetCouponAdapter getCouponAdapter;
    private RecyclerView mRecyclerView;
    private ValidCouponAdapter mValidCouponAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);

        if (null != viewParent) {
            ViewGroup parent = (ViewGroup) viewParent.getParent();
            if (null != parent) {
                parent.removeView(viewParent);
            }
        } else {
            viewParent = inflater.inflate(R.layout.fragment_available_coupons, null);
            mContext = getActivity();
            mHandler = new Handler(this);
            initView();

        }

        return viewParent;
    }

    private void initView() {
        dataList = new ArrayList<>();
        ViewCouponActivity viewCouponActivity = (ViewCouponActivity) getActivity();
        new GetCouponAsync(mHandler, "0", null, viewCouponActivity).execute();
        mRecyclerView = (RecyclerView) viewParent.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, getActivity().getResources().getDimensionPixelSize(R.dimen.padding_middle), true));
        mRecyclerView.setHasFixedSize(true);
        mValidCouponAdapter = new ValidCouponAdapter(dataList, getActivity());
        mRecyclerView.setAdapter(mValidCouponAdapter);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case Constants.HANDLER_GETCOUPONS_SUCCESS:
                dataList.clear();
                ArrayList<CouponInfo> data = (ArrayList<CouponInfo>) msg.obj;
                if (null != data) {
                    dataList.addAll(data);
                    mValidCouponAdapter.notifyDataSetChanged();
                }
                break;
            case Constants.HANDLER_GETCOUPONS_FAILD:
                Constants.ToastAction((String) msg.obj);
                break;
        }
        return false;
    }

}
