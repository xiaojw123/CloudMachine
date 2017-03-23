package com.cloudmachine.activities;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.cloudmachine.R;
import com.cloudmachine.adapter.GetCouponAdapter;
import com.cloudmachine.net.task.GetCouponAsync;
import com.cloudmachine.struc.CouponInfo;
import com.cloudmachine.utils.Constants;

import java.util.ArrayList;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/2/22 上午10:44
 * 修改人：shixionglu
 * 修改时间：2017/2/22 上午10:44
 * 修改备注：
 */

public class InvalidCouponFragment extends Fragment implements Handler.Callback{

    private View                  viewParent;
    private Context               mContext;
    private Handler               mHandler;
    private ListView              lvInvaliCoupon;
    private ArrayList<CouponInfo> dataList;
    private GetCouponAdapter      getCouponAdapter;
    private ListView lvInvaliCoupon1;

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
            viewParent = inflater.inflate(R.layout.fragment_invali_coupon, null);
            mContext = getActivity();
            mHandler = new Handler(this);
            initView();

        }
        //MobclickAgent.onEvent(getActivity(),UMengKey.time_machine_list);

        return viewParent;
    }

    private void initView() {
        ViewCouponActivity viewCouponActivity = (ViewCouponActivity) getActivity();
        lvInvaliCoupon = (ListView) viewParent.findViewById(R.id.lv_invali_coupon);
        new GetCouponAsync(mHandler, "2", null, viewCouponActivity).execute();
        dataList = new ArrayList<>();
        getCouponAdapter = new GetCouponAdapter(mContext, dataList,2);
        lvInvaliCoupon.setAdapter(getCouponAdapter);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case Constants.HANDLER_GETCOUPONS_SUCCESS:
                ArrayList<CouponInfo> data = (ArrayList<CouponInfo>) msg.obj;
                if (null != data) {
                    dataList.addAll(data);
                    getCouponAdapter.notifyDataSetChanged();
                }
                break;
            case Constants.HANDLER_GETCOUPONS_FAILD:
                Constants.ToastAction((String) msg.obj);
                break;
        }
        return false;
    }
}
