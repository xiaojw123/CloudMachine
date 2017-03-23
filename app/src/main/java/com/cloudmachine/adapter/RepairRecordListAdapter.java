package com.cloudmachine.adapter;

import java.util.List;

import com.cloudmachine.R;
import com.cloudmachine.net.task.GetRootNodesAsync;
import com.cloudmachine.struc.FaultWarnListInfo;
import com.cloudmachine.struc.RepairRecordInfo;


import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class RepairRecordListAdapter extends BaseAdapter {
	private List<RepairRecordInfo> dataResult;
	private Context context;
	private LayoutInflater layoutInflater;
	private Handler handler;
	public RepairRecordListAdapter(List<RepairRecordInfo> dataResult, Context context,
			Handler myHandler) {
		this.context = context;
		this.dataResult = dataResult;
		this.layoutInflater = LayoutInflater.from(context);
		handler = myHandler;
	}

	@Override
	public int getCount() {
		return dataResult.size();
	}

	@Override
	public RepairRecordInfo getItem(int position) {
		return dataResult.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * inner_classifyleft_listview classifyleft_img classifyleft_text
	 * 
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.repair_record_list_item,
					null);
			viewHolder = new ViewHolder();
			viewHolder.rDesc = (TextView) convertView
					.findViewById(R.id.rDesc);
			viewHolder.rTime = (TextView) convertView
					.findViewById(R.id.rTime);
			viewHolder.rConsume = (TextView) convertView
					.findViewById(R.id.rConsume);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.rDesc.setText(dataResult.get(position).getServiceDesc());
		viewHolder.rTime.setText(dataResult.get(position).getDrepairFinishTime());
		viewHolder.rConsume.setText(String.valueOf(dataResult.get(position).getConsume()));
		
		return convertView;
	}

	static class ViewHolder {
		TextView rDesc;
		TextView rTime;
		TextView rConsume;
	}

}
