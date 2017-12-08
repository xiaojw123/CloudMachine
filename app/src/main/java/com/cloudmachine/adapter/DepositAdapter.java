package com.cloudmachine.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.adapter.holder.BaseHolder;
import com.cloudmachine.bean.DepositItem;
import com.cloudmachine.ui.home.activity.RebundDepositActivity;
import com.cloudmachine.utils.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xiaojw on 2017/12/1.
 */

public class DepositAdapter extends BaseRecyclerAdapter {


    public DepositAdapter(Context context, List items) {
        super(context, items);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DepositHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item_deposit_layout, parent, false));
    }

    class DepositHolder extends BaseHolder<DepositItem> implements View.OnClickListener {

        @BindView(R.id.item_deposit_devicename)
        TextView itemDepositDevicename;
        @BindView(R.id.item_deposit_sn)
        TextView itemDepositSn;
        @BindView(R.id.item_deposit_fix_date)
        TextView itemDepositFixDate;
        @BindView(R.id.item_deposit_amount)
        TextView itemDepositAmount;
        @BindView(R.id.item_deposit_refund)
        TextView itemDepositRefund;
        @BindView(R.id.item_deposit_status)
        TextView itemDepositStatus;

        private DepositHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemDepositRefund.setOnClickListener(this);
        }

        @Override
        public void initViewHolder(DepositItem item) {
            itemDepositRefund.setTag(item);
            itemDepositDevicename.setText("设备名称:" + item.getDeviceName());
            itemDepositSn.setText("SN:" + item.getSnId());
            itemDepositFixDate.setText("安装日期：" + item.getGmtCreate());
            itemDepositAmount.setText("¥ " + item.getNeedAmount());
            switch (item.getStatus()) {
                case 1:
                    itemDepositRefund.setVisibility(View.VISIBLE);
                    itemDepositStatus.setVisibility(View.GONE);
                    itemDepositAmount.setTextColor(mContext.getResources().getColor(R.color.cor8));
                    break;
                case 2:
                    itemDepositStatus.setVisibility(View.VISIBLE);
                    itemDepositStatus.setText("退款中");
                    itemDepositAmount.setTextColor(mContext.getResources().getColor(R.color.cor8));
                    itemDepositStatus.setTextColor(mContext.getResources().getColor(R.color.cor2));
                    itemDepositRefund.setVisibility(View.GONE);
                    break;
                case 3:
                    itemDepositStatus.setVisibility(View.VISIBLE);
                    itemDepositStatus.setText("退款成功");
                    itemDepositAmount.setTextColor(mContext.getResources().getColor(R.color.cor10));
                    itemDepositStatus.setTextColor(mContext.getResources().getColor(R.color.cor10));
                    itemDepositRefund.setVisibility(View.GONE);
                    break;
            }
        }

        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            Object obj = v.getTag();
            if (obj instanceof DepositItem) {
                DepositItem  item= (DepositItem) obj;
            bundle.putString("id",String.valueOf(item.getId()));
            bundle.putString("needAmount",item.getNeedAmount());
            }
            Constants.toActivity((Activity) v.getContext(), RebundDepositActivity.class, bundle);
        }
    }

}
