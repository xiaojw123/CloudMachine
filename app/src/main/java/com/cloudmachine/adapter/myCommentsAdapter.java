package com.cloudmachine.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.struc.News;
import com.cloudmachine.utils.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

@SuppressLint("NewApi")
public class myCommentsAdapter extends BaseAdapter {

		private List<News> dataResult;
		private List<String> imageSrc;
		private Context context;
		private LayoutInflater layoutInflater;
		private DisplayImageOptions displayImageOptions;
		private ImageLoadingListener commImageLoadingLis;
		private String[] strArray = null;   
		private ImageView main_suppor_image;
		
		
		
		public myCommentsAdapter(List<News> dataResult, Context context) {
			this.context = context;
			this.dataResult = dataResult;
			this.layoutInflater = LayoutInflater.from(context);
			displayImageOptions = new DisplayImageOptions.Builder()
				.showImageOnFail(R.drawable.default_img)
				.showImageForEmptyUri(R.drawable.default_img)
				.showImageOnFail(R.drawable.default_img).cacheInMemory(true)
				.cacheOnDisc(true).displayer(new RoundedBitmapDisplayer(5))
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
						R.layout.my_comments, null);
				viewHolder = new ViewHolder();
				viewHolder.info_name = (TextView) convertView.findViewById(R.id.info_name);//用户名
				viewHolder.info_img =  (ImageView) convertView.findViewById(R.id.info_img);//用户头像
				viewHolder.content_img =  (ImageView) convertView.findViewById(R.id.content_img);//用户头像
				viewHolder.info_time = (TextView) convertView.findViewById(R.id.info_time);//用户名
				viewHolder.name_comment = (TextView) convertView.findViewById(R.id.name_comment);//用户名
				viewHolder.bl_content = (TextView) convertView.findViewById(R.id.bl_content);//用户名
				viewHolder.back_content = (TextView) convertView.findViewById(R.id.back_content);//用户名
				       
				
				
         
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			final News p = dataResult.get(position);
			String imgString = p.getLogo();
			viewHolder.info_name.setText(p.getMemberName());
			//viewHolder.info_content.setText(p.getTitle()+"");
			viewHolder.info_time.setText(p.getTime());
			ImageLoader.getInstance().displayImage(imgString, viewHolder.info_img,
					displayImageOptions, commImageLoadingLis);
			
		     viewHolder.back_content.setText(p.getContent());
             viewHolder.bl_content.setText(p.getSummary());
             viewHolder.name_comment.setText(null!=p.getReplyMemberName()?p.getReplyMemberName():p.getAuthor()+"");
     		ImageLoader.getInstance().displayImage(p.getImage(), viewHolder.content_img,
					Utils.displayImageOptions, commImageLoadingLis);
			return convertView;
		}

		
		
		   



		
		static class ViewHolder {
			ImageView info_img,content_img;
			TextView info_name,info_time,name_comment,bl_content,back_content;
		}

	}
