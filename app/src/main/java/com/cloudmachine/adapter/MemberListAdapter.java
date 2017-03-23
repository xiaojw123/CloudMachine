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
import com.cloudmachine.struc.MemberInfo;
import com.cloudmachine.utils.widgets.RadiusButtonView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class MemberListAdapter extends BaseAdapter {

		private List<MemberInfo> dataResult;
		private Context context;
		private LayoutInflater layoutInflater;
		private DisplayImageOptions displayImageOptions;
		private ImageLoadingListener commImageLoadingLis;

		public MemberListAdapter(List<MemberInfo> dataResult, Context context) {
			this.context = context;
			this.dataResult = dataResult;
			this.layoutInflater = LayoutInflater.from(context);
			displayImageOptions = new DisplayImageOptions.Builder()
				.showImageOnFail(R.drawable.mc_default_icon)
				.showImageForEmptyUri(R.drawable.mc_default_icon)
				.showImageOnFail(R.drawable.mc_default_icon).cacheInMemory(true)
				.cacheOnDisc(true).displayer(new RoundedBitmapDisplayer(5))
				.build();
		}

		@Override
		public int getCount() {
			return dataResult.size();
		}

		@Override
		public MemberInfo getItem(int position) {
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
						R.layout.mc_search_listview, null);
				viewHolder = new ViewHolder();
				viewHolder.info_img =  (ImageView) convertView
						.findViewById(R.id.info_img);
				viewHolder.arrow  = (ImageView)convertView.findViewById(R.id.arrow);
				viewHolder.title = (TextView) convertView
						.findViewById(R.id.title);
				viewHolder.summary = (TextView) convertView
						.findViewById(R.id.summary);
				viewHolder.arrow_add = (RadiusButtonView)convertView.findViewById(R.id.arrow_add);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			final MemberInfo dInfo = dataResult.get(position);
			String imgString = dInfo.getMiddlelogo();
			viewHolder.title.setText(dInfo.getName()!=null?dInfo.getName():"");
			if(position == 0){
				viewHolder.title.setTextColor(context.getResources().getColor(R.color.public_blue_bg));
			}else{
				viewHolder.title.setTextColor(context.getResources().getColor(R.color.public_black));
			}
			viewHolder.summary.setText(dInfo.getRoleRemark()!=null?dInfo.getRoleRemark():dInfo.getRole());
			ImageLoader.getInstance().displayImage(imgString, viewHolder.info_img,
					displayImageOptions, commImageLoadingLis);
			return convertView;
		}

		static class ViewHolder {
			ImageView info_img;
			ImageView arrow;
			TextView title;
			TextView summary;
			RadiusButtonView arrow_add;
		}
		


	}
