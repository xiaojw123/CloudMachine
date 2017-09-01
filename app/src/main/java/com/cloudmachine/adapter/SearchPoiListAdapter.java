package com.cloudmachine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.autolayout.utils.AutoUtils;
import com.cloudmachine.bean.ResidentAddressInfo;
import com.cloudmachine.utils.Constants;

import java.util.List;


public class SearchPoiListAdapter extends BaseAdapter {
	private List<ResidentAddressInfo> dataResult;
	private LayoutInflater layoutInflater;
	public SearchPoiListAdapter(List<ResidentAddressInfo> dataResult,Context context) {
		this.dataResult = dataResult;
		this.layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return dataResult.size();
	}

	@Override
	public ResidentAddressInfo getItem(int position) {
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
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.list_item_search_poi,
					null);
			viewHolder = new ViewHolder();
			viewHolder.text1_view = (TextView) convertView
					.findViewById(R.id.text1_view);
			viewHolder.text2_view = (TextView) convertView
					.findViewById(R.id.text2_view);
			convertView.setTag(viewHolder);
			AutoUtils.autoSize(convertView);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.text1_view.setText(Constants.toViewString(dataResult.get(position).getTitle()));
		viewHolder.text2_view.setText(Constants.toViewString(dataResult.get(position).getPosition()));
		
		return convertView;
	}

	static class ViewHolder {
		TextView text1_view;
		TextView text2_view;
	}

}
