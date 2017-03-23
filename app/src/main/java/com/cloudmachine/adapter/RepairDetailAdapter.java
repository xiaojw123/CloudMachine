package com.cloudmachine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.struc.WorkcollarListBean;

import java.util.ArrayList;

/**
 * 项目名称：CloudMachine
 * 类描述：报修详情页面listview
 * 创建人：shixionglu
 * 创建时间：2017/2/23 下午10:53
 * 修改人：shixionglu
 * 修改时间：2017/2/23 下午10:53
 * 修改备注：
 */

public class RepairDetailAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<WorkcollarListBean> dataList;
    private LayoutInflater layoutInflater;

    public RepairDetailAdapter(Context context, ArrayList<WorkcollarListBean> dataList) {
        this.context = context;
        this.dataList = dataList;
        this.layoutInflater = LayoutInflater.from(context);
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
            convertView = layoutInflater.inflate(R.layout.list_item_repair_detail, null);
            holder = new ViewHolder();
            holder.tv_part_name = (TextView) convertView.findViewById(R.id.tv_part_name);
            holder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
            holder.line = convertView.findViewById(R.id.line);
            holder.iv_three = (ImageView) convertView.findViewById(R.id.iv_three);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (dataList.get(position).getNispack().equals("0")) {
            holder.line.setVisibility(View.INVISIBLE);
            holder.iv_three.setVisibility(View.INVISIBLE);
        } else if (dataList.get(position).getNispack().equals("1")) {
            holder.line.setVisibility(View.VISIBLE);
            holder.iv_three.setVisibility(View.VISIBLE);
        }

        holder.tv_part_name.setText(new StringBuffer().append(dataList.get(position).getPart_NAME()).append("x").append(dataList.get(position).getNpackpartnum()));
        holder.tv_price.setText(dataList.get(position).getUnit_PRICE());
        return convertView;
    }

    static class ViewHolder {

        TextView tv_part_name;
        TextView tv_price;
        View line;
        ImageView iv_three;
    }

}
