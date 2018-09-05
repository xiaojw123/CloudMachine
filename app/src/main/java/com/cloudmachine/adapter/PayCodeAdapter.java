package com.cloudmachine.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.model.Text;
import com.cloudmachine.R;
import com.cloudmachine.adapter.holder.BaseHolder;
import com.cloudmachine.bean.PayCodeItem;
import com.cloudmachine.utils.CommonUtils;

import java.util.List;

/**
 * Created by xiaojw on 2018/8/13.
 */

public class PayCodeAdapter extends BaseRecyclerAdapter<PayCodeItem> {

    private boolean isHistory;

    public PayCodeAdapter(Context context, List<PayCodeItem> items) {
        super(context, items);
    }

    public void setHistoryQuery(boolean isHistory) {
        this.isHistory = isHistory;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (isHistory) {
            return new PayHistoryHolder(LayoutInflater.from(mContext).inflate(R.layout.item_pay_history, parent, false));
        } else {
            return new PayCodeHolder(LayoutInflater.from(mContext).inflate(R.layout.item_pay_code, parent, false));
        }
    }

    private class PayCodeHolder extends BaseHolder<PayCodeItem> {


        TextView orderNoTv;

        TextView attrTv;
        TextView timeTv;
        ImageView configImg;


        private PayCodeHolder(View itemView) {
            super(itemView);
            orderNoTv = (TextView) itemView.findViewById(R.id.paycode_order_no);
            attrTv = (TextView) itemView.findViewById(R.id.paycode_order_attr);
            timeTv = (TextView) itemView.findViewById(R.id.paycode_time_tv);
            configImg = (ImageView) itemView.findViewById(R.id.paycode_config_img);
        }

        @Override
        public void initViewHolder(PayCodeItem item) {
            initData(orderNoTv, attrTv, configImg, timeTv, item);
        }
    }

    private class PayHistoryHolder extends BaseHolder<PayCodeItem> {

        TextView orderNoTv;
        TextView attrTv;
        TextView timeTv;
        ImageView configImg;
        ImageView statusImg;


        private PayHistoryHolder(View itemView) {
            super(itemView);
            orderNoTv = (TextView) itemView.findViewById(R.id.history_order_no);
            attrTv = (TextView) itemView.findViewById(R.id.history_order_attr);
            timeTv = (TextView) itemView.findViewById(R.id.history_time_tv);
            configImg = (ImageView) itemView.findViewById(R.id.history_config_img);
            statusImg = (ImageView) itemView.findViewById(R.id.history_status_img);
        }

        @Override
        public void initViewHolder(PayCodeItem item) {
            initData(orderNoTv, attrTv, configImg, timeTv, item);
            int boxStatus = item.getBoxStatus();
            switch (boxStatus) {
                case 2:
                    itemView.setAlpha(1.0f);
                    statusImg.setImageResource(R.drawable.ic_pay_vaild);
                    break;
                case 3:
                    itemView.setAlpha(0.45f);
                    statusImg.setImageResource(R.drawable.ic_pay_invaild);
                    break;
            }


        }
    }

    private void initData(TextView orderNo, TextView attr, ImageView config, TextView time, PayCodeItem item) {
        orderNo.setText(item.getBoxCode());
        int buyType = item.getBuyType();
        String buyTypeStr = null;
        int leftDrawableId = 0;
        switch (buyType) {
            case 1:
                buyTypeStr = "云盒体验";
                leftDrawableId = R.drawable.ic_box_experience;
                break;
            case 2:
                buyTypeStr = "企业采购";
                leftDrawableId = R.drawable.ic_company_procurement;
                break;
            case 4:
                buyTypeStr = "在线购买";
                leftDrawableId = R.drawable.ic_online_pay;
                break;
        }
        attr.setText(buyTypeStr);
        if (leftDrawableId != 0) {
            Drawable leftDrawable = mContext.getResources().getDrawable(leftDrawableId);
            leftDrawable.setBounds(0, 0, leftDrawable.getIntrinsicWidth(), leftDrawable.getIntrinsicHeight());
            attr.setCompoundDrawables(leftDrawable, null, null, null);
        } else {
            attr.setCompoundDrawables(null, null, null, null);
        }
        int boxType = item.getBoxType();
        if (boxType == 1) {
            config.setActivated(false);
        } else if (boxType == 2) {
            config.setActivated(true);
        }
        time.setText(CommonUtils.getDateStampF(item.getGmtCreate()));
    }
}
