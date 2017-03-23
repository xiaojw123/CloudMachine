package com.cloudmachine.adapter;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.cloudmachine.R;
import com.cloudmachine.struc.Permission;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.Utils;

public class PermissionAdapter extends BaseAdapter {
	private List<Permission> dataResult;
	private Context context;
	private LayoutInflater layoutInflater;
	private Handler handler;
	private boolean isNoEdit;
	public PermissionAdapter(Context context,List<Permission> dataResult,
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
	public Permission getItem(int position) {
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
			convertView = layoutInflater.inflate(R.layout.permission_list_item,
					null);
			viewHolder = new ViewHolder();
			viewHolder.myToggleButton = (ToggleButton) convertView
					.findViewById(R.id.myToggleButton);
			viewHolder.textView = (TextView) convertView
					.findViewById(R.id.textView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.textView.setText(dataResult.get(position).getValue());
		viewHolder.myToggleButton.setChecked(dataResult.get(position).getIsHave());
		if(!isNoEdit){
			viewHolder.myToggleButton.setClickable(true);
			viewHolder.myToggleButton.setOnClickListener(new myClickListener(position));
		}else{
			viewHolder.myToggleButton.setClickable(false);
		}
			
		
		return convertView;
	}

	class myClickListener implements OnClickListener{
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
		
	}

	static class ViewHolder {
		ToggleButton myToggleButton;
		TextView textView;
	}

}
