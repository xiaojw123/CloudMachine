package com.cloudmachine.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.adapter.holder.BaseHolder;
import com.cloudmachine.autolayout.utils.AutoUtils;
import com.cloudmachine.bean.RepairHistoryInfo;
import com.cloudmachine.chart.utils.AppLog;
import com.cloudmachine.helper.OrderStatus;
import com.cloudmachine.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RepairListAdapter extends BaseRecyclerAdapter<RepairHistoryInfo> {


    public RepairListAdapter(Context context, List<RepairHistoryInfo> items) {
        super(context, items);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_repair_history, parent, false);
        return new RepairListHolder(itemView);
    }


    class RepairListHolder extends BaseHolder<RepairHistoryInfo> {


        @BindView(R.id.tv_completed)
        TextView textComplete;
        @BindView(R.id.completed)
        RelativeLayout completed;
        @BindView(R.id.device_name)
        TextView deviceName;
        @BindView(R.id.logo_flag_img)
        ImageView logoFlagIcon;
        @BindView(R.id.repair_desc)
        TextView repairPlace;
        @BindView(R.id.repair_history_time_tv)
        TextView timeTv;
        @BindView(R.id.repair_history_status_tv)
        TextView status;

        public RepairListHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void initViewHolder(RepairHistoryInfo item) {
            deviceName.setText(item
                    .getBrandName()
                    + " "
                    + item.getModelName());

            repairPlace.setText(item.getServiceDesc());
            timeTv.setText(item.getCreateTime());
            // TODO: 2018/9/26 logoflag
//            String logoFlag =item.getLogo_flag();
//            AppLog.print("logoFlag___pos:" + position + ", flag:" + logoFlag);
//            if ("0".equals(logoFlag)) {
//                if (logoFlagIcon.getVisibility() == View.VISIBLE) {
//                    logoFlagIcon.setVisibility(View.INVISIBLE);
//                }
//            }
//            if ("1".equals(logoFlag)) {
//                if (logoFlagIcon.getVisibility() != View.VISIBLE) {
//                    logoFlagIcon.setVisibility(View.VISIBLE);
//                }
//            }

            String nStatusString = "";
//            if (item.isAlliance()) {//加盟站工单
                nStatusString = item.getOrderStatusValue();
                switch (item.getOrderStatus()) {
                    case 3://待付款
                        nStatusString = OrderStatus.PAY;
                        break;
                    case 4:
                    case 5:
                    case 6:
                        if (item.isEvaluated()) {
                            nStatusString = OrderStatus.COMPLETED;
                        } else {
                            nStatusString = OrderStatus.EVALUATION;
                        }
                        break;
                }
//            } else {
//
//                //非加盟站
//                String nstatus = historyInfos.get(position).getNstatus();
//                if (historyInfos.get(position).getFlag().equals("0")) {
//                    if (nstatus.equals("10220003")) {
//                        nStatusString = OrderStatus.CANCEL;
//                    } else {
//                        nStatusString = OrderStatus.WAIT;
//                    }
//
//                }
//                if (historyInfos.get(position).getFlag().equals("1")) {
//                    switch (nstatus) {
//                        case "0":
//                            nStatusString = OrderStatus.WAIT;
//                            break;
//                        case "8":
//                            switch (historyInfos.get(position).getNloanamount_TYPE()) {
//                                case 0://正常状态(已付款,直接判断是否评价)
//                                    if (historyInfos.get(position).getIs_EVALUATE().equals("N")) {
//                                        //未评价
//                                        nStatusString = OrderStatus.EVALUATION;
//                                    } else if (historyInfos.get(position).getIs_EVALUATE().equals("Y")) {
//                                        nStatusString = OrderStatus.COMPLETED;
//                                    }
//                                    break;
//                                case -1://未知状态
//                                    try {
//                                        if (Double.parseDouble(historyInfos.get(position).getNloanamount()) > 0.0) {
//                                            nStatusString = OrderStatus.PAY;
//                                        } else {
//                                            if (historyInfos.get(position).getIs_EVALUATE().equals("N")) {
//                                                nStatusString = OrderStatus.EVALUATION;
//                                            } else if (historyInfos.get(position).getIs_EVALUATE().equals("Y")) {
//                                                nStatusString = OrderStatus.COMPLETED;
//                                            }
//                                        }
//                                    } catch (NumberFormatException e) {
//                                        e.printStackTrace();
//                                        Constants.ToastAction("服务器传参数错误");
//                                    }
//                                    break;
//                                case 1:
//                                    if (Double.parseDouble(historyInfos.get(position).getNloanamount()) > 0.0) {
//                                        nStatusString = OrderStatus.PAY;
//                                    } else {
//                                        if (historyInfos.get(position).getIs_EVALUATE().equals("N")) {
//                                            nStatusString = OrderStatus.EVALUATION;
//                                        } else if (historyInfos.get(position).getIs_EVALUATE().equals("Y")) {
//                                            nStatusString = OrderStatus.COMPLETED;
//                                        }
//                                    }
//                                    break;
//                            }
//
//                            break;
//                        case "9":
//                            nStatusString = "已完工";
//                            if (historyInfos.get(position).getIs_EVALUATE().equals("N")) {
//                                nStatusString = "待评价";
//                            }
//
//                            break;
//                        case "10":
//                            nStatusString = "已丢单";
//                            break;
//                        default:
//                            nStatusString = "进行中";
//
//                            break;
//                    }
//
//
//                }
//            }
            status.setText(nStatusString);
            itemView.setTag(R.id.order_status, nStatusString);
//            itemView.setTag(R.id.order_item, item);
        }
    }




}
