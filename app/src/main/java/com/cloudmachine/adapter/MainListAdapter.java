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

import com.cloudm.autolayout.utils.AutoUtils;
import com.cloudmachine.R;
import com.cloudmachine.struc.McDeviceInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MainListAdapter extends BaseAdapter {
	private List<McDeviceInfo> dataResult;
	private Context context;
	private LayoutInflater layoutInflater;
	private Handler handler;

	// 图片第一次加载的监听器
//	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
//	final private ImageLoader imageLoader = ImageLoader.getInstance();
//
//	private DisplayImageOptions options= new DisplayImageOptions.Builder()
//	.showImageOnLoading(R.drawable.list_mc_default) // 设置图片在下载期间显示的图片
//	.showImageForEmptyUri(R.drawable.list_mc_default)// 设置图片Uri为空或是错误的时候显示的图片
//	.showImageOnFail(R.drawable.list_mc_default) // 设置图片加载/解码过程中错误时候显示的图片
//	.cacheInMemory(true)// 设置下载的图片是否缓存在内存中
//	.cacheOnDisc(true)// 设置下载的图片是否缓存在SD卡中
//	.imageScaleType(ImageScaleType.IN_SAMPLE_INT)// 设置图片以如何的编码方式显示
//	.considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型
//	.displayer(new FlexibleRoundedBitmapDisplayer(5,FlexibleRoundedBitmapDisplayer.CORNER_ALL))
//	.build();// 构建完成
	 private ImageLoader imageLoader = ImageLoader.getInstance();
	 private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	private DisplayImageOptions options = new DisplayImageOptions.Builder()
    .showStubImage(R.drawable.list_mc_default) // 在ImageView加载过程中显示图片
    .showImageForEmptyUri(R.drawable.list_mc_default) // image连接地址为空时
    .showImageOnFail(R.drawable.list_mc_default) // image加载失败
    .cacheInMemory(true) // 加载图片时会在内存中加载缓存
    .cacheOnDisc(true) // 加载图片时会在磁盘中加载缓存
    .displayer(new RoundedBitmapDisplayer(20)) // 设置用户加载图片task(这里是圆角图片显示)
    .build();
	
	public MainListAdapter(Context context, Handler myHandler,
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
			viewHolder.line_layout = convertView
					.findViewById(R.id.line_layout);
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
			viewHolder.arrow_r_text.setText("工作中");					//设置工作状态
		}
		if(position == getCount()-1){
			viewHolder.line_layout.setVisibility(View.GONE);
		}else{
			viewHolder.line_layout.setVisibility(View.VISIBLE);
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
			}else{
				viewHolder.mac_address.setText(context.getResources().getString(R.string.main_list_no_location));
			}
		}else{
			viewHolder.mac_address.setText(context.getResources().getString(R.string.main_list_no_location));
		}
		ListView listView = (ListView) parent;
//		listView.setOnScrollListener(new PauseOnScrollListener(imageLoader,
//				false, true));
//		imageLoader.displayImage(dataResult.get(position).getImage(),
//				viewHolder.iv_icon, options, animateFirstListener);
		imageLoader.displayImage(dataResult.get(position).getImage(), viewHolder.iv_icon,
                options, animateFirstListener);
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
		View line_layout;
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
//	public static class AnimateFirstDisplayListener extends
//			SimpleImageLoadingListener {
//
//		static final List<String> displayedImages = Collections
//				.synchronizedList(new LinkedList<String>());
//
//		@Override
//		public void onLoadingComplete(String imageUri, View view,
//				Bitmap loadedImage) {
//			if (loadedImage != null) {
//				ImageView imageView = (ImageView) view;
//				// 是否第一次显示
//				boolean firstDisplay = !displayedImages.contains(imageUri);
//				if (firstDisplay) {
//					// 图片淡入效果
//					FadeInBitmapDisplayer.animate(imageView, 200);
//					displayedImages.add(imageUri);
//				}
//			}
//		}
//	}
	
	 /** 图片加载监听事件 **/
    private static class AnimateFirstDisplayListener extends
            SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections
                .synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view,
                Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500); // 设置image隐藏动画500ms
                    displayedImages.add(imageUri); // 将图片uri添加到集合中
                }
            }
        }
    }

}
