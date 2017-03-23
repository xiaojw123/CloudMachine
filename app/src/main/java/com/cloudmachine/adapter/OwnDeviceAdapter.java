package com.cloudmachine.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cloudm.autolayout.utils.AutoUtils;
import com.cloudmachine.R;
import com.cloudmachine.struc.McDeviceInfo;
import com.cloudmachine.utils.Constants;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class OwnDeviceAdapter extends BaseAdapter{
	
	private ArrayList<McDeviceInfo> dataResult;
	private Context context;
	private LayoutInflater layoutInflater;
	private Handler handler;

	// 图片第一次加载的监听器
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	final private DisplayImageOptions options = Constants.displayListImageOptions;
	final private ImageLoader imageLoader = ImageLoader.getInstance();

	public OwnDeviceAdapter(Context context, Handler myHandler,
			ArrayList<McDeviceInfo> dataResult) {
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
	public McDeviceInfo getItem(int position) {

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
			convertView = layoutInflater.inflate(R.layout.item_main_list, null);
			viewHolder = new ViewHolder();
			viewHolder.line_bottom = (ImageView) convertView
					.findViewById(R.id.line_bottom);
			viewHolder.name_text = (TextView) convertView
					.findViewById(R.id.name_text);
			viewHolder.arrow_r_text = (TextView) convertView
					.findViewById(R.id.arrow_r_text);
			viewHolder.iv_icon = (ImageView) convertView
					.findViewById(R.id.icon);
			viewHolder.mac_address = (TextView) convertView
					.findViewById(R.id.mac_address);
			convertView.setTag(viewHolder);
			AutoUtils.autoSize(convertView);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.name_text.setText(dataResult.get(position).getName());
		if (dataResult.get(position).getWorkStatus() == 0) {
			viewHolder.arrow_r_text.setText("");
		} else {
			viewHolder.arrow_r_text.setText("工作中");
		}
		// viewHolder.line_bottom.setVisibility(View.VISIBLE);
		// viewHolder.line_bottom2.setVisibility(View.GONE);
		// convertView.setOnClickListener(new myClickListener(position));
		viewHolder.mac_address.setText("");
		if (null != dataResult.get(position).getLocation()) {										//先判断location是否为空，在判断position是否为空
			String address = dataResult.get(position).getLocation()
					.getPosition();
			if (address != null) {
				viewHolder.mac_address.setText(dataResult.get(position)
						.getLocation().getPosition());
			}
		}
		ListView listView = (ListView) parent;
		listView.setOnScrollListener(new PauseOnScrollListener(imageLoader,
				false, true));
		imageLoader.displayImage(dataResult.get(position).getImage(),
				viewHolder.iv_icon, options, animateFirstListener);
		return convertView;
	}

	/*
	 * class myClickListener implements OnClickListener{ int position;
	 * myClickListener (int position){ this.position = position; }
	 * 
	 * @Override public void onClick(View v) { // TODO Auto-generated method
	 * stub
	 * dataResult.get(position).setIsHave(!dataResult.get(position).getIsHave
	 * ()); Utils.MyLog("ppp:"+position); Message msg = Message.obtain();
	 * msg.what = Constants.HANDLER_CHANGE_MAP; msg.arg1 = position;
	 * handler.sendMessage(msg); }
	 * 
	 * }
	 */

	static class ViewHolder {
		ImageView line_bottom;
		ImageView iv_icon;
		TextView name_text;
		TextView arrow_r_text;
		TextView mac_address;
	}

	/**
	 * 图片加载第一次显示监听器
	 * 
	 * @author Administrator
	 * 
	 */
	private static class AnimateFirstDisplayListener extends
			SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				// 是否第一次显示
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					// 图片淡入效果
					FadeInBitmapDisplayer.animate(imageView, 200);
					displayedImages.add(imageUri);
				}
			}
		}
	}
}
