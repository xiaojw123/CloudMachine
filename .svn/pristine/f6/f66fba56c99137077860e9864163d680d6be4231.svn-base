package com.cloudmachine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.struc.InfosInfo;

import java.util.ArrayList;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/2/15 上午9:34
 * 修改人：shixionglu
 * 修改时间：2017/2/15 上午9:34
 * 修改备注：
 */

public class GetInfosAdapter extends BaseAdapter {

    private LayoutInflater       layoutInflater;
    private Context              context;
    private ArrayList<InfosInfo> dataList;

    public GetInfosAdapter(Context context, ArrayList<InfosInfo> dataList) {

        this.context = context;
        this.dataList = dataList;
        layoutInflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item_coupon, null);
            holder = new ViewHolder();
            holder.tvPrice = (TextView) convertView.findViewById(R.id.price);
            holder.tvUseType = (TextView) convertView.findViewById(R.id.tv_userType);
            holder.tvlimitInfo = (TextView) convertView.findViewById(R.id.limitInfo);
            holder.validiteTime = (TextView) convertView.findViewById(R.id.tv_valadite_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvPrice.setText(String.valueOf(dataList.get(position).getAmount()));
        holder.tvUseType.setText(String.valueOf(dataList.get(position).getName()));
        holder.tvlimitInfo.setText(String.valueOf("满"+dataList.get(position).getLimitInfo())+"使用");
        holder.validiteTime.setText(dataList.get(position).getExpiryLife());
        return convertView;
    }

    static class ViewHolder {

        TextView tvPrice;
        TextView tvUseType;
        TextView tvlimitInfo;
        TextView validiteTime;
    }

}
