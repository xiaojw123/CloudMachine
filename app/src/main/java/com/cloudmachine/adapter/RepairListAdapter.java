package com.cloudmachine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudmachine.autolayout.utils.AutoUtils;
import com.cloudmachine.R;
import com.cloudmachine.helper.OrderStatus;
import com.cloudmachine.bean.RepairHistoryInfo;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.chart.utils.AppLog;

import java.util.ArrayList;

public class RepairListAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private boolean haveShowOnce = false;
    private int unFinishedNum = -1;
    private ArrayList<RepairHistoryInfo> historyInfos;

    public RepairListAdapter(Context mActivity,
                             ArrayList<RepairHistoryInfo> repairList) {
        this.layoutInflater = LayoutInflater.from(mActivity);
        this.historyInfos = repairList;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return historyInfos.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    public void setUnFinishedNum(int size) {
        this.unFinishedNum = size;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(
                    R.layout.list_item_repair_history, null);
            holder.timeTv = (TextView) convertView.findViewById(R.id.repair_history_time_tv);
            holder.logoFlagIcon = (ImageView) convertView.findViewById(R.id.logo_flag_img);
            holder.deviceName = (TextView) convertView
                    .findViewById(R.id.device_name);//品牌+型号
            holder.repairPlace = (TextView) convertView
                    .findViewById(R.id.repair_desc);//描述
            holder.status = (TextView) convertView//维修状态
                    .findViewById(R.id.repair_history_status_tv);
            holder.textComplete = (TextView) convertView//
                    .findViewById(R.id.tv_completed);//头部已完成标题栏文字
            holder.completed = (RelativeLayout) convertView.findViewById(R.id.completed);//头部已完成标题栏
            convertView.setTag(holder);
            AutoUtils.auto(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == unFinishedNum && historyInfos.size() != 0) {
            holder.completed.setVisibility(View.VISIBLE);
        } else {
            holder.completed.setVisibility(View.GONE);
        }
        if (historyInfos.size() == 0) {
            holder.completed.setVisibility(View.GONE);
        }


        if (null != historyInfos.get(position)) {
            holder.deviceName.setText(historyInfos.get(position)
                    .getVbrandname()
                    + " "
                    + historyInfos.get(position).getVmaterialname());

            holder.repairPlace.setText(historyInfos
                    .get(position).getVdiscription());
            holder.timeTv.setText(historyInfos.get(position).getDopportunity());
            String logoFlag = historyInfos.get(position).getLogo_flag();
            AppLog.print("logoFlag___pos:" + position + ", flag:" + logoFlag);
            if ("0".equals(logoFlag)) {
                if (holder.logoFlagIcon.getVisibility() == View.VISIBLE) {
                    holder.logoFlagIcon.setVisibility(View.INVISIBLE);
                }
            }
            if ("1".equals(logoFlag)) {
                if (holder.logoFlagIcon.getVisibility() != View.VISIBLE) {
                    holder.logoFlagIcon.setVisibility(View.VISIBLE);
                }
            }
            RepairHistoryInfo info = historyInfos.get(position);

            String nStatusString = "";
            if (info.isAlliance()) {//加盟站工单
                nStatusString = info.getOrderStatusValue();
                switch (info.getNstatus()) {
                    case "3"://待付款
                        nStatusString = OrderStatus.PAY;
                        break;
                    case "4":
                    case "5":
                    case "6":
                        if ("N".equals(info.getIs_EVALUATE())) {
                            nStatusString = OrderStatus.EVALUATION;
                        } else {
                            nStatusString = OrderStatus.COMPLETED;
                        }
                        break;
                }
            } else {//非加盟站
                String nstatus = historyInfos.get(position).getNstatus();
                if (historyInfos.get(position).getFlag().equals("0")) {
                    if (nstatus.equals("10220003")) {
                        nStatusString = OrderStatus.CANCEL;
                    } else {
                        nStatusString = OrderStatus.WAIT;
                    }

                }
                if (historyInfos.get(position).getFlag().equals("1")) {
                    switch (nstatus) {
                        case "0":
                            nStatusString = OrderStatus.WAIT;
                            break;
                        case "8":
                            switch (historyInfos.get(position).getNloanamount_TYPE()) {
                                case 0://正常状态(已付款,直接判断是否评价)
                                    if (historyInfos.get(position).getIs_EVALUATE().equals("N")) {
                                        //未评价
                                        nStatusString = OrderStatus.EVALUATION;
                                    } else if (historyInfos.get(position).getIs_EVALUATE().equals("Y")) {
                                        nStatusString = OrderStatus.COMPLETED;
                                    }
                                    break;
                                case -1://未知状态
                                    try {
                                        if (Double.parseDouble(historyInfos.get(position).getNloanamount()) > 0.0) {
                                            nStatusString = OrderStatus.PAY;
                                        } else {
                                            if (historyInfos.get(position).getIs_EVALUATE().equals("N")) {
                                                nStatusString = OrderStatus.EVALUATION;
                                            } else if (historyInfos.get(position).getIs_EVALUATE().equals("Y")) {
                                                nStatusString = OrderStatus.COMPLETED;
                                            }
                                        }
                                    } catch (NumberFormatException e) {
                                        e.printStackTrace();
                                        Constants.ToastAction("服务器传参数错误");
                                    }
                                    break;
                                case 1:
                                    if (Double.parseDouble(historyInfos.get(position).getNloanamount()) > 0.0) {
                                        nStatusString = OrderStatus.PAY;
                                    } else {
                                        if (historyInfos.get(position).getIs_EVALUATE().equals("N")) {
                                            nStatusString = OrderStatus.EVALUATION;
                                        } else if (historyInfos.get(position).getIs_EVALUATE().equals("Y")) {
                                            nStatusString = OrderStatus.COMPLETED;
                                        }
                                    }
                                    break;
                            }

                            break;
                        case "9":
                            nStatusString = "已完工";
                            if (historyInfos.get(position).getIs_EVALUATE().equals("N")) {
                                nStatusString = "待评价";
                            }

                            break;
                        case "10":
                            nStatusString = "已丢单";
                            break;
                        default:
                            nStatusString = "进行中";

                            break;
                    }


                }
            }
            holder.status.setText(nStatusString);
            convertView.setTag(R.id.order_status, nStatusString);
            convertView.setTag(R.id.order_item, historyInfos.get(position));
        }

        return convertView;
    }

    static class ViewHolder {
        TextView timeTv;
        ImageView logoFlagIcon;
        TextView deviceName;
        TextView repairPlace;
        TextView status;
        RelativeLayout completed;
        TextView textComplete;
    }

}
