package com.cloudmachine.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudm.autolayout.utils.AutoUtils;
import com.cloudmachine.R;
import com.cloudmachine.struc.FaultWarnListInfo;

public class CheckReportAdapter extends BaseAdapter{
	
	private ArrayList<FaultWarnListInfo> mList;
	private Context mContext;
	private LayoutInflater layoutInflater;

	public CheckReportAdapter(Context mContext, ArrayList<FaultWarnListInfo> mList) {
		// TODO Auto-generated constructor stub
		this.mList = mList;
		this.mContext = mContext;
		this.layoutInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
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
		if(convertView == null){
			convertView = layoutInflater.inflate(R.layout.check_report_item_view, null);
			holder = new ViewHolder();
			holder.tvCheckItem = (TextView) convertView.findViewById(R.id.tv_check_item);
			holder.tvRange = (TextView) convertView.findViewById(R.id.tv_range);
			holder.tvResult = (TextView) convertView.findViewById(R.id.tv_result);
			holder.ivResult = (ImageView) convertView.findViewById(R.id.iv_result);
			convertView.setTag(holder);
			AutoUtils.autoSize(convertView);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		if(null != mList.get(position)){
			FaultWarnListInfo reListBean = mList.get(position);
			holder.tvCheckItem.setText(reListBean.getItem());
			holder.tvRange.setText(reListBean.getReferRange());
			holder.tvResult.setText(reListBean.getTestResult());
			if (! reListBean.getBexcess()) {
				holder.ivResult.setVisibility(View.VISIBLE);
			}else {
				holder.ivResult.setVisibility(View.INVISIBLE);
			}
		}
		return convertView;
	}
	
	
	
	static class ViewHolder{
		
		TextView tvCheckItem;
		TextView tvRange;
		TextView tvResult;
		ImageView ivResult;
		
	}
}
