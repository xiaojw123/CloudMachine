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
import com.cloudmachine.listener.OnClickEffectiveListener;
import com.cloudmachine.struc.McDeviceInfo;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.widgets.RadiusButtonView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class McSearchAdapter extends BaseAdapter {

		private List<McDeviceInfo> dataResult;
		private Context context;
		private LayoutInflater layoutInflater;
		private DisplayImageOptions displayImageOptions;
		private ImageLoadingListener commImageLoadingLis;
		private int aDapterType;

		public McSearchAdapter(List<McDeviceInfo> dataResult, Context context) {
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
			final McDeviceInfo dInfo = dataResult.get(position);
			String imgString = dInfo.getImage();
			viewHolder.title.setText(dInfo.getName());
			viewHolder.summary.setText(dInfo.getLocation()!=null?dInfo.getLocation().getPosition():Constants.S_DEVICE_LOCATION_NO);
			ImageLoader.getInstance().displayImage(imgString, viewHolder.info_img,
					displayImageOptions, commImageLoadingLis);
			if(aDapterType == 0){
				viewHolder.arrow.setVisibility(View.VISIBLE);
				viewHolder.arrow_add.setVisibility(View.GONE);
			}else{
				viewHolder.arrow.setVisibility(View.GONE);
				viewHolder.arrow_add.setVisibility(View.VISIBLE);
			}
			viewHolder.arrow_add.setOnClickListener(new llClick(position));
			return convertView;
		}
		class llClick extends OnClickEffectiveListener{
			private int position;
			llClick(int position){
				this.position = position;
			}
			
			@Override
			public void onClickEffective(View v) {
				// TODO Auto-generated method stub
				
			}
			
		}

		static class ViewHolder {
			ImageView info_img;
			ImageView arrow;
			TextView title;
			TextView summary;
			RadiusButtonView arrow_add;
		}
		


	}
