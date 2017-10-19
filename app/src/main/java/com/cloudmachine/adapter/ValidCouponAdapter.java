package com.cloudmachine.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.activities.ConponSecListActivity;
import com.cloudmachine.activities.ViewCouponActivityNew;
import com.cloudmachine.ui.home.model.CouponItem;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.DensityUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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


    private List<CouponItem> dataList;
    private LayoutInflater inflater;
    private Context mContext;
    private boolean mIsUse;

    public ValidCouponAdapter(List<CouponItem> dataList, Context context) {
        this.dataList = dataList;
        mContext = context;
        inflater = LayoutInflater.from(mContext);
    }

    public void updateItems(List<CouponItem> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }


    public void setUseAble(boolean isUse) {
        mIsUse = isUse;
    }

    @Override
    public ValidCouponHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.list_item_coupon_valid, parent, false);
        return new ValidCouponHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ValidCouponHolder holder, int position) {
        if (dataList != null) {
            int len = dataList.size();
            if (len > 0) {

                final CouponItem bean = dataList.get(position);
                if (bean != null) {
                    holder.itemCouponValidPrice.setText(String.valueOf(bean.getAmount()));

                    if (mIsUse) {
                        holder.itemNumTv.setText("(使用" + bean.getUseNum() + "张)");
                    } else {
                        holder.itemNumTv.setText("(剩余" + bean.getCouponNum() + "张)");
                    }
                    holder.itemCouponVaildLimitInfo.setText(bean.getRemark());
                }

                if (mContext instanceof ConponSecListActivity) {
                    ((LinearLayout.LayoutParams) holder.itemCouponVaildLimitInfo.getLayoutParams()).topMargin = DensityUtil.dip2px(mContext, 10);
                    holder.itemCouponValidityPeriod.setVisibility(View.VISIBLE);
                    if (bean != null) {
                        holder.itemCouponValidTitle.setText(bean.getPackName());
                        String startTime = bean.getStartTime();
                        String endTime = bean.getEndTime();
                        if (startTime != null) {
                            startTime = startTime.trim();
                        }
                        if (endTime != null) {
                            endTime = endTime.trim();
                        }
                        holder.itemCouponValidityPeriod.setText("有效期: " + startTime + "至" + endTime);
                    }
                }else{
                    holder.itemCouponValidTitle.setText(bean.getCouponName());
                }
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mContext instanceof ViewCouponActivityNew) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(ConponSecListActivity.COUPON_AMOUNT, bean.getAmount());
                            bundle.putInt(ConponSecListActivity.COUPON_BASE_ID, bean.getCouponBaseId());
                            Constants.toActivity((Activity) mContext, ConponSecListActivity.class, bundle);
                        }
                    }
                });


            }
        }


//        String title=dataList.get(position).getTitle();
//        if (TextUtils.isEmpty(title)){
//            title=dataList.get(position).getUserType();
//        }
//        holder.mTvTitle.setText(title);
//        holder.mTvAmount.setText(String.valueOf(dataList.get(position).getAmount()));
//        String remark = dataList.get(position).getRemark();
//        String limtInfo = dataList.get(position).getLimitInfo();
//        if (!TextUtils.isEmpty(remark)) {
//            limtInfo = remark;
//        }
//        holder.mTvLimitInfo.setText(limtInfo);
//        holder.mTvValidDate.setText(new StringBuffer().append("有效期：")
//                .append(dataList.get(position).getStartTime())
//                .append("至")
//                .append(dataList.get(position).getEndTime()));
//        int cStatus = dataList.get(position).getcStatus();
//        if (cStatus == 1) {
//            holder.mIvUsed.setVisibility(View.VISIBLE);
//            holder.mIvExpired.setVisibility(View.GONE);
//        } else if (cStatus == 2) {
//            holder.mIvUsed.setVisibility(View.GONE);
//            holder.mIvExpired.setVisibility(View.VISIBLE);
//        }
    }


    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }

    public static class ValidCouponHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_coupon_valid)
        TextView itemCouponValid;
        @BindView(R.id.item_coupon_valid_price)
        TextView itemCouponValidPrice;
        @BindView(R.id.item_coupon_valid_title)
        TextView itemCouponValidTitle;
        @BindView(R.id.item_coupon_vaild_limitInfo)
        TextView itemCouponVaildLimitInfo;
        @BindView(R.id.item_coupon_validity_period)
        TextView itemCouponValidityPeriod;
        @BindView(R.id.item_coupon_num)
        TextView itemNumTv;


//        private TextView mTvAmount;
//        private TextView mTvLimitInfo;
//        private TextView mTvValidDate;
//        private ImageView mIvUsed;
//        private ImageView mIvExpired;
//        private TextView mTvTitle;

        public ValidCouponHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
//            mTvTitle = (TextView) itemView.findViewById(R.id.vaild_title_tv);
//            //优惠券金额
//            mTvAmount = (TextView) itemView.findViewById(R.id.tv_amount);
//            //优惠券使用条件
//            mTvLimitInfo = (TextView) itemView.findViewById(R.id.limitInfo);
//            //有效期限
//            mTvValidDate = (TextView) itemView.findViewById(R.id.tv_valid_data);
//            //已使用图片
//            mIvUsed = (ImageView) itemView.findViewById(R.id.iv_used);
//            //已过期图片
//            mIvExpired = (ImageView) itemView.findViewById(R.id.iv_expried);

        }
    }

}
