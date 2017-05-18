package com.cloudmachine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudm.autolayout.utils.AutoUtils;
import com.cloudmachine.R;
import com.cloudmachine.struc.RepairHistoryInfo;
import com.cloudmachine.utils.Constants;
import com.github.mikephil.charting.utils.AppLog;

import java.util.ArrayList;

public class RepairListAdapter extends BaseAdapter {

    private Context mActivity;
    private LayoutInflater layoutInflater;
    private boolean haveShowOnce = false;
    private int unFinishedNum = -1;
    private ArrayList<RepairHistoryInfo> historyInfos;

    public RepairListAdapter(Context mActivity,
                             ArrayList<RepairHistoryInfo> repairList) {
        this.mActivity = mActivity;
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
            holder.logoFlagIcon = (ImageView) convertView.findViewById(R.id.logo_flag_img);
            holder.ivIcon = (ImageView) convertView//左侧小圆点
                    .findViewById(R.id.iv_icon);
            holder.deviceName = (TextView) convertView
                    .findViewById(R.id.device_name);//品牌+型号
            holder.repairPlace = (TextView) convertView
                    .findViewById(R.id.repair_place);//描述
            holder.repairDate = (TextView) convertView//维修进度时间
                    .findViewById(R.id.repair_date);
            holder.arrowRight = (ImageView) convertView//向右的箭头
                    .findViewById(R.id.arrow_right);
            holder.status = (TextView) convertView//维修状态
                    .findViewById(R.id.status);
            holder.bgBottom = (View) convertView//底部空白栏
                    .findViewById(R.id.bg_bottom);
            holder.textComplete = (TextView) convertView//
                    .findViewById(R.id.tv_completed);//头部已完成标题栏文字
            holder.completed = (RelativeLayout) convertView.findViewById(R.id.completed);//头部已完成标题栏
            holder.price = (TextView) convertView.findViewById(R.id.price);

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

        if (position >= unFinishedNum) {
            holder.bgBottom.setVisibility(View.GONE);
        } else {
            holder.bgBottom.setVisibility(View.VISIBLE);
        }

        if (null != historyInfos.get(position)) {
            holder.deviceName.setText(historyInfos.get(position)
                    .getVbrandname()
                    + " "
                    + historyInfos.get(position).getVmaterialname());

            holder.repairPlace.setText(historyInfos
                    .get(position).getVdiscription());

            holder.repairDate.setText(historyInfos.get(position)
                    .getDopportunity());
            RepairHistoryInfo historyInfo = historyInfos.get(position);
            int type = historyInfo.getNloanamount_TYPE();
            String price = type==1 ? historyInfo.getNloanamount():historyInfo.getPrice();
            if (null != price &&
                    !price.startsWith("0")) {
                holder.price.setVisibility(View.VISIBLE);
                holder.price.setText(price + "元");
            } else {
                holder.price.setVisibility(View.INVISIBLE);
            }
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
            String nStatusString = "";
            if (historyInfos.get(position).getFlag().equals("0")) {
                if (historyInfos.get(position).getNstatus()
                        .equals("10220001")) {
                    nStatusString = "已报修";
                } else if (historyInfos.get(position).getNstatus()
                        .equals("102200012")) {
                    nStatusString = "已转工单";
                } else if (historyInfos.get(position).getNstatus()
                        .equals("10220003")) {
                    nStatusString = "已取消";
                } else if (historyInfos.get(position).getNstatus()
                        .equals("10220004")) {
                    nStatusString = "已跟踪";
                }
            }
            if (historyInfos.get(position).getFlag().equals("1")) {
                if (historyInfos.get(position).getNstatus().equals("0")) {
                    nStatusString = "已下单";
                } else if (historyInfos.get(position).getNstatus()
                        .equals("1")) {
                    nStatusString = "已派工";
                } else if (historyInfos.get(position).getNstatus()
                        .equals("2")) {
                    nStatusString = "已技师确认";
                } else if (historyInfos.get(position).getNstatus()
                        .equals("3")) {
                    nStatusString = "已预约";
                } else if (historyInfos.get(position).getNstatus()
                        .equals("4")) {
                    nStatusString = "已出发";
                } else if (historyInfos.get(position).getNstatus()
                        .equals("5")) {
                    nStatusString = "已到达";
                } else if (historyInfos.get(position).getNstatus()
                        .equals("6")) {
                    nStatusString = "已客户确认";
                } else if (historyInfos.get(position).getNstatus()
                        .equals("7")) {
                    nStatusString = "已结算";
                    if (historyInfos.get(position).getIs_EVALUATE().equals("N")) {
                        nStatusString = "待评价";
                    }
                } else if (historyInfos.get(position).getNstatus()
                        .equals("8")) {
                    /*nStatusString = "已完工";
                    if (historyInfos.get(position).getIs_EVALUATE().equals("N")) {
                        nStatusString = "待评价";
                    }
                    if (historyInfos.get(position).getIs_EVALUATE().equals("N")
                            && historyInfos.get(position).getNloanamount_TYPE() == 1
                            && Double.parseDouble(historyInfos.get(position).getNloanamount()) > 0.0) {
                        nStatusString = "待支付";
                    }*/
                    try {
                        double nloanamount = Double.parseDouble(historyInfos.get(position).getNloanamount());
                    } catch (Exception e) {
                        e.printStackTrace();
                        //Constants.ToastAction("服务器传参错误");
                    }

                    switch (historyInfos.get(position).getNloanamount_TYPE()) {
                        case 0://正常状态(已付款,直接判断是否评价)
                            if (historyInfos.get(position).getIs_EVALUATE().equals("N")) {
                                //未评价
                                nStatusString = "待评价";
                            } else if (historyInfos.get(position).getIs_EVALUATE().equals("Y")) {
                                nStatusString = "已完工";
                            }
                            break;
                        case -1://未知状态
                            try {
                                if (Double.parseDouble(historyInfos.get(position).getNloanamount()) > 0.0) {
                                    nStatusString = "待付款";
                                } else {
                                    if (historyInfos.get(position).getIs_EVALUATE().equals("N")) {
                                        nStatusString = "待评价";
                                    } else if (historyInfos.get(position).getIs_EVALUATE().equals("Y")) {
                                        nStatusString = "已完工";
                                    }
                                }
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                                Constants.ToastAction("服务器传参数错误");
                            }
                            break;
                        case 1:
                            if (Double.parseDouble(historyInfos.get(position).getNloanamount()) > 0.0) {
                                nStatusString = "待支付";
                            } else {
                                if (historyInfos.get(position).getIs_EVALUATE().equals("N")) {
                                    nStatusString = "待评价";
                                } else if (historyInfos.get(position).getIs_EVALUATE().equals("Y")) {
                                    nStatusString = "已完工";
                                }
                            }
                            break;
                    }


                } else if (historyInfos.get(position).getNstatus()
                        .equals("9")) {
                    nStatusString = "已返程";
                    if (historyInfos.get(position).getIs_EVALUATE().equals("N")) {
                        nStatusString = "待评价";
                    }
                } else if (historyInfos.get(position).getNstatus()
                        .equals("10")) {
                    nStatusString = "已丢单";
                }
            }

            holder.status.setText(nStatusString);
        }

        return convertView;
    }

    static class ViewHolder {
        ImageView logoFlagIcon;
        ImageView ivIcon;
        TextView deviceName;
        TextView repairPlace;
        TextView repairDate;
        TextView price;
        ImageView arrowRight;
        TextView status;
        View bgBottom;
        RelativeLayout completed;
        TextView textComplete;
    }

}
