package com.cloudmachine.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.cloudmachine.R;

public class GridAdapter extends BaseAdapter {
	private LayoutInflater inflater; // 视图容器
	private int selectedPosition = -1;// 选中的位置
	private boolean shape;
	private ArrayList<Bitmap> mBitmaps; // 传过来的Imges参数
	private int maxSize;
	
	public boolean isShape() {
		return shape;
	}
	
	public void setShape(boolean shape) {
		this.shape = shape;
	}

//	public void update() {
//		loading();
//	}
	public void update(){
		notifyDataSetChanged();
	}
	
	
	public GridAdapter(Context context, ArrayList<Bitmap> lImags,int size) {
		this.inflater = LayoutInflater.from(context);
		this.mBitmaps = lImags;
		this.maxSize = size;
	}
	
	public GridAdapter(Context context){
		this.inflater = LayoutInflater.from(context);
	}

	public int getCount() {
		if(mBitmaps==null){
			return 1;
		}
		int size = mBitmaps.size();
		return size<maxSize?(size + 1):size;
	}

	public Object getItem(int arg0) {

		return null;
	}

	public long getItemId(int arg0) {

		return 0;
	}

	public void setSelectedPosition(int position) {
		selectedPosition = position;
	}

	public int getSelectedPosition() {
		return selectedPosition;
	}

	/**
	 * ListView Item设置
	 */
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {

			convertView = inflater.inflate(R.layout.item_published_grida,
					parent, false);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView
					.findViewById(R.id.item_grida_image);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if((position==mBitmaps.size()&&mBitmaps.size()<maxSize)||position == maxSize){
			holder.image.setImageBitmap(BitmapFactory.decodeResource(
					convertView.getResources(),
					R.drawable.nothing));
		}
		else {
			holder.image.setImageBitmap(mBitmaps.get(position));
		}
		

		return convertView;
	}

	public class ViewHolder {
		public ImageView image;
	}

}


