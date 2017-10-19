package com.cloudmachine.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.ui.home.model.CouponItem;
import com.cloudmachine.utils.DensityUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/4/5 下午5:16
 * 修改人：shixionglu
 * 修改时间：2017/4/5 下午5:16
 * 修改备注：
 */

public class InvalidCouponAdapter extends RecyclerView.Adapter<InvalidCouponAdapter.InvalidCouponHolder> {

    private List<CouponItem> dataList;
    private LayoutInflater inflater;
    private Context mContext;


    public InvalidCouponAdapter(List<CouponItem> dataList, Context context) {
        this.dataList = dataList;
        mContext = context;
        inflater = LayoutInflater.from(mContext);
    }

    public void updateItems(List<CouponItem> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }


    @Override
    public InvalidCouponHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = inflater.inflate(R.layout.list_item_coupon_invalid, parent, false);
        return new InvalidCouponHolder(itemView);
    }

    @Override
    public void onBindViewHolder(InvalidCouponHolder holder, int position) {
        if (dataList != null) {
            int len = dataList.size();
            if (len > 0) {
                CouponItem bean = dataList.get(position);
                if (bean != null) {
                    holder.itemCouponInValidPrice.setText(String.valueOf(bean.getAmount()));
                    holder.itemCouponInValidTitle.setText(bean.getCouponName());
                    holder.itemCouponInVaildLimitInfo.setText(bean.getRemark());
                    holder.itemCouponInValidityPeriod.setText("有效期：" + bean.getStartTime() + "至" + bean.getEndTime());
                   int status=bean.getCStatus();
                    switch (status){
                        case 1:
                            holder.itemStatusImg.setBackgroundResource(R.drawable.ic_invaild_used);
                            break;
                        case 0:
                        case 2:
                            holder.itemStatusImg.setBackgroundResource(R.drawable.ic_invalid_dated);
                            break;

                    }
                }
                if (position == len - 1) {
                    ((RecyclerView.LayoutParams) holder.itemView.getLayoutParams()).bottomMargin = DensityUtil.dip2px(mContext, 26);
                }
            }
        }
//        String title=dataList.get(position).getTitle();
//        if (TextUtils.isEmpty(title)){
//            title=dataList.get(position).getUserType();
//        }
//        holder.mTvTitle.setText(title);
//        holder.mTvAmount.setText(String.valueOf(dataList.get(position).getAmount()));
//        String limitInfo = dataList.get(position).getLimitInfo();
//        String remark = dataList.get(position).getRemark();
//        if (!TextUtils.isEmpty(remark)) {
//            limitInfo = remark;
//        }
//        holder.mTvLimitInfo.setText(limitInfo);
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

    public static class InvalidCouponHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_coupon_invalid)
        TextView itemCouponInValid;
        @BindView(R.id.item_coupon_invalid_price)
        TextView itemCouponInValidPrice;
        @BindView(R.id.item_coupon_invalid_title)
        TextView itemCouponInValidTitle;
        @BindView(R.id.item_coupon_invaild_limitInfo)
        TextView itemCouponInVaildLimitInfo;
        @BindView(R.id.item_coupon_invalidity_period)
        TextView itemCouponInValidityPeriod;
        @BindView(R.id.item_coupon_invalid_status_img)
        ImageView itemStatusImg;

        public InvalidCouponHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
//            mTvTitle= (TextView) itemView.findViewById(R.id.vaild_title_tv);
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
