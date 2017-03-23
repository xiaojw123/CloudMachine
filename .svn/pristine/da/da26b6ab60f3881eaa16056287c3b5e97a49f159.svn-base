package com.cloudmachine.adapter;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.struc.EditListInfo;

public class EditListAdapter extends BaseAdapter {
	private List<EditListInfo> dataResult;
	private Context context;
	private LayoutInflater layoutInflater;
	private Handler handler;
	public EditListAdapter(Context context,List<EditListInfo> dataResult,
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
	public EditListInfo getItem(int position) {
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
			convertView = layoutInflater.inflate(R.layout.edit_list_item,
					null);
			viewHolder = new ViewHolder();
			viewHolder.image_view = (ImageView) convertView
					.findViewById(R.id.image_view);
			viewHolder.text_view = (TextView) convertView
					.findViewById(R.id.text_view);
			viewHolder.text_view2 = (TextView) convertView
					.findViewById(R.id.text_view2);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.text_view.setText(dataResult.get(position).getName());
		if(!TextUtils.isEmpty(dataResult.get(position).getStr2())){
			viewHolder.text_view2.setVisibility(View.VISIBLE);
			viewHolder.text_view2.setText(dataResult.get(position).getStr2());
		}else{
			viewHolder.text_view2.setVisibility(View.GONE);
		}
		
		if(dataResult.get(position).getIsClick()){
			viewHolder.image_view.setVisibility(View.VISIBLE);
		}else{
			viewHolder.image_view.setVisibility(View.GONE);
		}
//		convertView.setOnClickListener(new myClickListener(position));
		
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
		ImageView image_view;
		TextView text_view;
		TextView text_view2;
	}

}
