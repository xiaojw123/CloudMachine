package com.cloudmachine.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.bean.CouponInfo;

import java.util.ArrayList;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/4/6 上午9:00
 * 修改人：shixionglu
 * 修改时间：2017/4/6 上午9:00
 * 修改备注：
 */

public class ValidCouponAdapter extends RecyclerView.Adapter<ValidCouponAdapter.ValidCouponHolder> {


    private ArrayList<CouponInfo> dataList;
    private LayoutInflater        inflater;
    private Context               mContext;

    public ValidCouponAdapter(ArrayList<CouponInfo> dataList, Context context) {
        this.dataList = dataList;
        mContext = context;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public ValidCouponHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = inflater.inflate(R.layout.list_item_validcoupon, parent, false);
        return new ValidCouponHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ValidCouponHolder holder, int position) {
        holder.mTvAmount.setText(String.valueOf(dataList.get(position).getAmount()));
        holder.mTvUseType.setText(dataList.get(position).getUserType());
        holder.mTvLimitInfo.setText(dataList.get(position).getLimitInfo());
        holder.mTvValidDate.setText(new StringBuffer().append("有效期：")
                .append(dataList.get(position).getStartTime())
                .append("至")
                .append(dataList.get(position).getEndTime()));
        int cStatus = dataList.get(position).getcStatus();
        if (cStatus == 1) {
            holder.mIvUsed.setVisibility(View.VISIBLE);
            holder.mIvExpired.setVisibility(View.GONE);
        } else if (cStatus == 2) {
            holder.mIvUsed.setVisibility(View.GONE);
            holder.mIvExpired.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ValidCouponHolder extends RecyclerView.ViewHolder {

        private TextView  mTvAmount;
        private TextView  mTvUseType;
        private TextView  mTvLimitInfo;
        private TextView  mTvValidDate;
        private ImageView mIvUsed;
        private ImageView mIvExpired;

        public ValidCouponHolder(View itemView) {
            super(itemView);

            //优惠券金额
            mTvAmount = (TextView) itemView.findViewById(R.id.tv_amount);
            //优惠券所属分类
            mTvUseType = (TextView) itemView.findViewById(R.id.tv_use_type);
            //优惠券使用条件
            mTvLimitInfo = (TextView) itemView.findViewById(R.id.limitInfo);
            //有效期限
            mTvValidDate = (TextView) itemView.findViewById(R.id.tv_valid_data);
            //已使用图片
            mIvUsed = (ImageView) itemView.findViewById(R.id.iv_used);
            //已过期图片
            mIvExpired = (ImageView) itemView.findViewById(R.id.iv_expried);

        }
    }

}
