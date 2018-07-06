package com.cloudmachine.adapter;

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

import com.cloudmachine.autolayout.utils.AutoUtils;
import com.cloudmachine.R;
import com.cloudmachine.bean.DeviceItem;
import com.cloudmachine.bean.McDeviceInfo;
import com.cloudmachine.bean.McDeviceLocation;
import com.cloudmachine.utils.Constants;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class OwnDeviceAdapter extends BaseAdapter{
	
	private List<McDeviceInfo> dataResult;
	private Context context;
	private LayoutInflater layoutInflater;
	private Handler handler;

	// 图片第一次加载的监听器
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	final private DisplayImageOptions options = Constants.displayListImageOptions;
	final private ImageLoader imageLoader = ImageLoader.getInstance();

	public OwnDeviceAdapter(Context context, Handler myHandler,
			List<McDeviceInfo> dataResult) {
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
			viewHolder.name_text = (TextView) convertView
					.findViewById(R.id.name_text);
			viewHolder.mac_address = (TextView) convertView
					.findViewById(R.id.mac_address);
			viewHolder.selImg= (ImageView) convertView.findViewById(R.id.md_sel_img);
			convertView.setTag(viewHolder);
			AutoUtils.autoSize(convertView);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		McDeviceInfo info=dataResult.get(position);
		if (info.isSelected()){
			viewHolder.selImg.setVisibility(View.VISIBLE);
		}else{
			viewHolder.selImg.setVisibility(View.GONE);
		}
		viewHolder.name_text.setText(info.getName());
		// viewHolder.line_bottom.setVisibility(View.VISIBLE);
		// viewHolder.line_bottom2.setVisibility(View.GONE);
		// convertView.setOnClickListener(new myClickListener(position));
		McDeviceLocation loc=info.getLocation();
		if (null != loc) {										//先判断location是否为空，在判断position是否为空
			String address = loc
					.getPosition();
			if (address != null) {
				viewHolder.mac_address.setText(address);
			}
		}
		ListView listView = (ListView) parent;
		listView.setOnScrollListener(new PauseOnScrollListener(imageLoader,
				false, true));
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
		TextView name_text;
		TextView mac_address;
		ImageView selImg;
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
