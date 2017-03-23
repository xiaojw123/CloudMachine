package com.cloudmachine.adapter;



import java.util.Iterator;

import com.cloudmachine.R;
import com.cloudmachine.net.task.ImageUploadAsync;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.photo.util.Bimp;
import com.cloudmachine.utils.photo.util.ImageItem;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class PhotoGridAdpter extends BaseAdapter{
	private LayoutInflater inflater;
	private int selectedPosition = -1;
	private boolean shape;
	private Context context;

	public boolean isShape() {
		return shape;
	}

	public void setShape(boolean shape) {
		this.shape = shape;
	}

	public PhotoGridAdpter(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	public void update() {
		uploadImage();
		loading();
	}
	
	private void uploadImage(){
		Iterator<ImageItem> iterator = Bimp.tempSelectBitmap.iterator();  
		while(iterator.hasNext()) { 
			if(!iterator.next().isSelected){
				 iterator.remove();  
			}
		}
		int tempSize = Bimp.tempSelectBitmap.size();
		for(int i=0; i<tempSize; i++){
			if(Bimp.tempSelectBitmap.get(i).getImageUrl() == null){
				String imString = Bimp.tempSelectBitmap.get(i).getImagePath();
				if(imString !=null){
					new ImageUploadAsync(handler, imString).execute();
				}
			}
		}
	}

	public int getCount() {
		if(Bimp.tempSelectBitmap.size() == 9){
			return 9;
		}
		return (Bimp.tempSelectBitmap.size() /*+ 1*/);
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

		if (position ==Bimp.tempSelectBitmap.size()) {
			holder.image.setImageBitmap(BitmapFactory.decodeResource(
					context.getResources(), R.drawable.icon_addpic_nm));
			if (position == 9) {
				holder.image.setVisibility(View.GONE);
			}
		} else {
			if(null != Bimp.tempSelectBitmap.get(position).getImagePath()){
				ImageLoader.getInstance().displayImage("file://"+Bimp.tempSelectBitmap.get(position).getImagePath(), holder.image,
						Constants.displayDeviceImageOptions, null);
			}else if(null != Bimp.tempSelectBitmap.get(position).getThumbnailPath()){
				ImageLoader.getInstance().displayImage("file://"+Bimp.tempSelectBitmap.get(position).getThumbnailPath(), holder.image,
						Constants.displayDeviceImageOptions, null);
			}else{
				ImageLoader.getInstance().displayImage(Bimp.tempSelectBitmap.get(position).getImageUrl(), holder.image,
						Constants.displayDeviceImageOptions, null);
			}
			
			/*Bitmap bm = Bimp.tempSelectBitmap.get(position).getBitmap();
			if(null != bm){
				holder.image.setImageBitmap(bm);
			}else{
				ImageLoader.getInstance().displayImage(Bimp.tempSelectBitmap.get(position).getImageUrl(), holder.image,
						Constants.displayDeviceImageOptions, null);
			}*/
			
			
		}

		return convertView;
	}

	public class ViewHolder {
		public ImageView image;
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				PhotoGridAdpter.this.notifyDataSetChanged();
				break;
			case ImageUploadAsync.ImageUpload_Success:
				String rObjStr = (String)msg.obj;
				String[] rStr = rObjStr.split(Constants.S_FG);
				if(null != rStr&& rStr.length == 2){
					String fpath = rStr[0];
					String url = rStr[1];
					int tempSize = Bimp.tempSelectBitmap.size();
					for(int i=0; i<tempSize; i++){
						if(fpath.equals(Bimp.tempSelectBitmap.get(i).getImagePath())){
							Bimp.tempSelectBitmap.get(i).setImageUrl(url);
						}
					}
				}
				break;
			case ImageUploadAsync.ImageUpload_Fail:
				break;
			}
			super.handleMessage(msg);
		}
	};

	public void loading() {
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					if (Bimp.max == Bimp.tempSelectBitmap.size()) {
						Message message = new Message();
						message.what = 1;
						handler.sendMessage(message);
						break;
					} else {
						Bimp.max += 1;
						Message message = new Message();
						message.what = 1;
						handler.sendMessage(message);
						break;
					}
				}
			}
		}).start();
	}
}

