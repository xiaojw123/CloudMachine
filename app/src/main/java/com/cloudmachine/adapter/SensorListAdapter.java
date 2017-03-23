package com.cloudmachine.adapter;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudm.autolayout.utils.AutoUtils;
import com.cloudmachine.R;
import com.cloudmachine.struc.FaultWarnListInfo;


public class SensorListAdapter extends BaseAdapter {
	private List<FaultWarnListInfo> dataResult;
	private Context context;
	private LayoutInflater layoutInflater;
	private Handler handler;
	private boolean isNoEdit;
	public SensorListAdapter(Context context,List<FaultWarnListInfo> dataResult,
			Handler myHandler,boolean isNoEdite) {
		this.context = context;
		this.dataResult = dataResult;
		this.layoutInflater = LayoutInflater.from(context);
		handler = myHandler;
		this.isNoEdit = isNoEdite;
	}

	@Override
	public int getCount() {
		return dataResult.size();
	}

	@Override
	public FaultWarnListInfo getItem(int position) {
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
			convertView = layoutInflater.inflate(R.layout.sensor_list_item,
					null);
			viewHolder = new ViewHolder();
			viewHolder.fault_name = (TextView) convertView
					.findViewById(R.id.fault_name);
			viewHolder.fault_range = (TextView) convertView
					.findViewById(R.id.fault_range);
			viewHolder.fault_date = (TextView) convertView
					.findViewById(R.id.fault_date);
			viewHolder.line = (TextView) convertView
					.findViewById(R.id.line);
			viewHolder.state_image = (ImageView) convertView
					.findViewById(R.id.state_image);
			convertView.setTag(viewHolder);
			AutoUtils.autoSize(convertView);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.fault_name.setText(dataResult.get(position).getItem());
		viewHolder.fault_range.setText(dataResult.get(position).getReferRange());
		viewHolder.fault_date.setText(dataResult.get(position).getTestResult());
//		if(position == getCount()-1){
//			LinearLayout.LayoutParams lpBtn = new LinearLayout.LayoutParams(
//					LayoutParams.MATCH_PARENT-Utils.dip2px(15), LayoutParams.WRAP_CONTENT);
//			lpBtn.setMargins(Utils.dip2px(15), 0, 0, 0);
//			viewHolder.line.setVisibility(View.GONE);
//		}else{
			viewHolder.line.setVisibility(View.VISIBLE);
//		}
		if(dataResult.get(position).getBexcess()){
			viewHolder.fault_date.setTextColor(context.getResources().getColor(R.color.black));
			viewHolder.state_image.setVisibility(View.GONE);
		}else{
			viewHolder.fault_date.setTextColor(context.getResources().getColor(R.color.chart_Limit_line));
			viewHolder.state_image.setVisibility(View.VISIBLE);
		}
		
		return convertView;
	}

	/*class myClickListener implements OnClickListener{
		int position;
		myClickListener (int position){
			this.position = position;
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			dataResult.get(position).setIsHave(!dataResult.get(position).getIsHave());
			Utils.MyLog("ppp:"+position);
			Message msg = Message.obtain();
			msg.what = Constants.HANDLER_CHANGE_MAP;
			msg.arg1 = position;
			handler.sendMessage(msg);
		}
		
	}*/

	static class ViewHolder {
		TextView line;
		TextView fault_name;
		TextView fault_range;
		TextView fault_date;
		ImageView state_image;
	}

}
