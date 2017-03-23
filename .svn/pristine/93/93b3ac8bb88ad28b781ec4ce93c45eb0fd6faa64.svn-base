package com.cloudmachine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.struc.SetUpTimeInfo;

import java.util.ArrayList;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/2/19 下午9:03
 * 修改人：shixionglu
 * 修改时间：2017/2/19 下午9:03
 * 修改备注：
 */

public class SetupTimeAdapter extends BaseAdapter {

    private LayoutInflater            layoutInflater;
    private  Context                  context;
    private  ArrayList<SetUpTimeInfo> dataList;

    public SetupTimeAdapter( Context context, ArrayList<SetUpTimeInfo> dataList) {
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
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item_deliverymethod, null);
            viewHolder = new ViewHolder();
            viewHolder.tvSetupTime = (TextView) convertView.findViewById(R.id.tv_delivery_method);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvSetupTime.setText(dataList.get(position).getName());
        return convertView;
    }

    static class ViewHolder{

        TextView tvSetupTime;
    }
}
