package com.cloudmachine.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.struc.McDeviceInfo;
import com.cloudmachine.utils.Constants;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class DeviceListAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<McDeviceInfo> mList;
	private LayoutInflater layoutInflater;
	// 图片第一次加载的监听器
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	final private DisplayImageOptions options = Constants.displayListImageOptions;
	final private ImageLoader imageLoader = ImageLoader.getInstance();

	public DeviceListAdapter(Context mContext, ArrayList<McDeviceInfo> mList) {
		this.mContext = mContext;
		this.mList = mList;
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
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = layoutInflater.inflate(
					R.layout.device_select_item_view, null);
			holder = new ViewHolder();
			holder.iv_show_device_photo = (ImageView) convertView
					.findViewById(R.id.iv_device_photo);
			holder.iv_checked = (ImageView) convertView
					.findViewById(R.id.iv_checked);
			holder.tv_device_name = (TextView) convertView
					.findViewById(R.id.tv_device_name);        
			holder.tv_devic_addrss = (TextView) convertView
					.findViewById(R.id.tv_device_address);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.iv_checked.setVisibility(View.GONE); // 对号隐藏
		if (null != mList.get(position).getName()) {
			holder.tv_device_name.setText(mList.get(position).getName());
		}
		if (null != mList.get(position).getLocation()) {
			if (null != mList.get(position).getLocation().getPosition()) {
				holder.tv_devic_addrss.setText(mList.get(position)
						.getLocation().getPosition());
			}
		}
		ListView listView = (ListView) parent;
		listView.setOnScrollListener(new PauseOnScrollListener(imageLoader,
				false, true));
		imageLoader.displayImage(mList.get(position).getImage(),
				holder.iv_show_device_photo, options, animateFirstListener);
		return convertView;
	}

	static class ViewHolder {

		ImageView iv_show_device_photo;
		TextView tv_device_name;
		TextView tv_devic_addrss;
		ImageView iv_checked;
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
