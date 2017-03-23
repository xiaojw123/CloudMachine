package com.cloudmachine.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.struc.News;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class NewsAdapter extends BaseAdapter {

		private List<News> dataResult;
		private Context context;
		private LayoutInflater layoutInflater;
		private DisplayImageOptions displayImageOptions;
		private ImageLoadingListener commImageLoadingLis;

		public NewsAdapter(List<News> dataResult, Context context) {
			this.context = context;
			this.dataResult = dataResult;
			this.layoutInflater = LayoutInflater.from(context);
			displayImageOptions = new DisplayImageOptions.Builder()
				.showImageOnFail(R.drawable.ic_launcher)
				.showImageForEmptyUri(R.drawable.img_failure)
				.showImageOnFail(R.drawable.img_failure).cacheInMemory(true)
				.cacheOnDisc(true).displayer(new RoundedBitmapDisplayer(20))
				.build();
		}

		@Override
		public int getCount() {
			return dataResult.size();
		}

		@Override
		public News getItem(int position) {
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
				convertView = layoutInflater.inflate(
						R.layout.inner_infolist_listview, null);
				viewHolder = new ViewHolder();
				viewHolder.info_name = (TextView) convertView
						.findViewById(R.id.info_name);
				viewHolder.info_content = (TextView) convertView
						.findViewById(R.id.info_content);
				viewHolder.info_img =  (ImageView) convertView
						.findViewById(R.id.info_img);
				viewHolder.info_time = (TextView)convertView.findViewById(R.id.info_time);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			News p = dataResult.get(position);
			String imgString = p.getImage();
			viewHolder.info_name.setText(p.getTitle()+"");
			viewHolder.info_content.setText(p.getSummary()+"");
			viewHolder.info_time.setText(p.getTime());
			ImageLoader.getInstance().displayImage(imgString, viewHolder.info_img,
					displayImageOptions, commImageLoadingLis);
			return convertView;
		}

		static class ViewHolder {
			ImageView info_img;
			TextView info_name;
			TextView info_content;
			TextView info_time;
		}

	}
