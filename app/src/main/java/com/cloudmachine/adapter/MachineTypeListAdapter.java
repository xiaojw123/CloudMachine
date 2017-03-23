package com.cloudmachine.adapter;

import java.util.ArrayList;

import com.cloudm.autolayout.utils.AutoUtils;
import com.cloudmachine.R;
import com.cloudmachine.struc.MachineTypeInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MachineTypeListAdapter extends BaseAdapter{
	
	private Context mContext;
	private ArrayList<MachineTypeInfo> mList;
	private LayoutInflater layoutInflater;

	public MachineTypeListAdapter(Context mContext,
			ArrayList<MachineTypeInfo> mList) {
		this.mContext = mContext;
		this.mList = mList;
		this.layoutInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.list_item_machinetype, null);
			holder.tvText = (TextView) convertView.findViewById(R.id.tv_type);
			convertView.setTag(holder);
			AutoUtils.auto(convertView);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		return convertView;
	}
	
	
	static class ViewHolder{
		TextView tvText;
	}

}
