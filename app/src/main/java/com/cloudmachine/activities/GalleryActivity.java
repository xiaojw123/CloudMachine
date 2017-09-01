package com.cloudmachine.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.utils.photo.util.Bimp;
import com.cloudmachine.utils.photo.util.ImageItem;
import com.cloudmachine.utils.photo.util.PublicWay;
import com.cloudmachine.utils.photo.util.Res;
import com.cloudmachine.utils.photo.zoom.PhotoView;
import com.cloudmachine.utils.photo.zoom.ViewPagerFixed;
import com.cloudmachine.utils.widgets.BadgeView;
import com.cloudmachine.utils.widgets.TitleView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 这个是用于进行图片浏览时的界面
 *
 * @author king
 * @QQ:595163260
 * @version 2014年10月18日  下午11:47:53
 */
public class GalleryActivity extends Activity {
	private Intent intent;
	// 发送按钮
	private Button send_bt;
	//顶部显示预览图片位置的textview
	private TextView positionTextView;
	//获取前一个activity传过来的position
	private int position;
	//当前的位置
	private int location = 0;
	
	private ArrayList<View> listViews = null;
	private ViewPagerFixed pager;
	private MyPageAdapter adapter;

	public List<Bitmap> bmp = new ArrayList<Bitmap>();
	public List<String> drr = new ArrayList<String>();
	public List<String> del = new ArrayList<String>();
	
	private Context mContext;

	RelativeLayout photo_relativeLayout;
	
	private TitleView title_layout;
	private BadgeView badge;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(Res.getLayoutID("plugin_camera_gallery"));// 切屏到主界面
		PublicWay.activityList.add(this);
		mContext = this;
		initTitleLayout();
		send_bt = (Button) findViewById(Res.getWidgetID("send_button"));
		send_bt.setOnClickListener(new GallerySendListener());
		badge = new BadgeView(this, send_bt);
		changeNum();
		intent = getIntent();
		Bundle bundle = intent.getExtras();
		position = Integer.parseInt(intent.getStringExtra("position"));
		int id = intent.getIntExtra("ID", 0);
//		isShowOkBt();
		// 为发送按钮设置文字
		pager = (ViewPagerFixed) findViewById(Res.getWidgetID("gallery01"));
		pager.setOnPageChangeListener(pageChangeListener);
//		if(position == 1){
			for (int i = 0; i < Bimp.tempSelectBitmap.size(); i++) {
				Bimp.tempSelectBitmap.get(i).setSelected(true);
				if(null != Bimp.tempSelectBitmap.get(i).getImagePath()){
					initListViews(ImageLoader.getInstance().loadImageSync("file://"+Bimp.tempSelectBitmap.get(i).getImagePath()));
				}else if(null != Bimp.tempSelectBitmap.get(i).getThumbnailPath()){
					initListViews(ImageLoader.getInstance().loadImageSync("file://"+Bimp.tempSelectBitmap.get(i).getThumbnailPath()));
				}else{
					initListViews(ImageLoader.getInstance().loadImageSync(Bimp.tempSelectBitmap.get(i).imageUrl));
				}
				/*if(null != Bimp.tempSelectBitmap.get(i).getBitmap() ){
					initListViews( Bimp.tempSelectBitmap.get(i).getBitmap() );
				}*/
				
				
			}
//		}
		/*else if(position == 2){
			if(null != AlbumActivity.dataList){
				for(int i =(id-5<0?0:id-5);i<id-5;i--){
					initListViews(  AlbumActivity.dataList.get(i).getBitmap() );
				}
				for (int i = id; i <  (id+5<AlbumActivity.dataList.size()?id+5:AlbumActivity.dataList.size()); i++) {
					initListViews(  AlbumActivity.dataList.get(i).getBitmap() );
				}
				
			}
		}*/
		
		
		adapter = new MyPageAdapter(listViews);
		pager.setAdapter(adapter);
		pager.setPageMargin((int)getResources().getDimensionPixelOffset(Res.getDimenID("ui_10_dip")));
		
		pager.setCurrentItem(id);
		
		initTitleRightImage();
	}
	
	private void initTitleLayout() {

		title_layout = (TitleView) findViewById(R.id.title_layout);
		title_layout.setTitle("照片");
		title_layout.setLeftImage(-1, new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(0,
						R.anim.zoomout);
			}
		});
		title_layout.setRightImage(R.drawable.plugin_camera_choosed, new ChangeStateListener());
		
	}

	private void initTitleRightImage(){
		int imageSize = Bimp.tempSelectBitmap.size();
		if(imageSize>0){
			if(Bimp.tempSelectBitmap.get(location).isSelected){
				title_layout.setRightImageResource(R.drawable.plugin_camera_choosed);
			}else{
				title_layout.setRightImageResource(R.drawable.plugin_camera_unchoosed);
			}
		}
	}
	private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

		public void onPageSelected(int arg0) {
			location = arg0;
			initTitleRightImage();
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		public void onPageScrollStateChanged(int arg0) {

		}
	};
	
	private void initListViews(Bitmap bm) {
		if (listViews == null)
			listViews = new ArrayList<View>();
		PhotoView img = new PhotoView(this);
		img.setBackgroundColor(0xff000000);
		img.setImageBitmap(bm);
		img.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		listViews.add(img);
	}
	
	// 返回按钮添加的监听器
	private class BackListener implements OnClickListener {

		public void onClick(View v) {
//			intent.setClass(GalleryActivity.this, ImageFile.class);
//			startActivity(intent);
			finish();
			overridePendingTransition(0,
					R.anim.zoomout);
		}
	}
	
	// 删除按钮添加的监听器
	private class DelListener implements OnClickListener {

		public void onClick(View v) {
			if (listViews.size() == 1) {
				Bimp.tempSelectBitmap.clear();
				Bimp.max = 0;
				send_bt.setText(Res.getString("finish")+"(" + Bimp.tempSelectBitmap.size() + "/"+PublicWay.num+")");
				Intent intent = new Intent("data.broadcast.action");  
                sendBroadcast(intent);  
				finish();
			} else {
				Bimp.tempSelectBitmap.remove(location);
				Bimp.max--;
				pager.removeAllViews();
				listViews.remove(location);
				adapter.setListViews(listViews);
				send_bt.setText(Res.getString("finish")+"(" + Bimp.tempSelectBitmap.size() + "/"+PublicWay.num+")");
				adapter.notifyDataSetChanged();
			}
		}
	}

	// 改变选择状态
	private class ChangeStateListener implements OnClickListener {
		public void onClick(View v) {
			if(Bimp.tempSelectBitmap.get(location).isSelected()){
				Bimp.tempSelectBitmap.get(location).setSelected(false);
			}else{
				Bimp.tempSelectBitmap.get(location).setSelected(true);
			}
			changeNum();
//			send_bt.setText(Res.getString("finish")+"(" + PublicWay.getTempSelectNum() + "/"+PublicWay.num+")");
			initTitleRightImage();
		}
		
	}
	// 完成按钮的监听
	private class GallerySendListener implements OnClickListener {
		public void onClick(View v) {
			finish();
			overridePendingTransition(0,
					R.anim.zoomout);
		}

	}

	/*public void isShowOkBt() {
		if (Bimp.tempSelectBitmap.size() > 0) {
			changeNum();
//			send_bt.setText(Res.getString("finish")+"(" + Bimp.tempSelectBitmap.size() + "/"+PublicWay.num+")");
			send_bt.setPressed(true);
			send_bt.setClickable(true);
			send_bt.setTextColor(getResources().getColor(R.color.green));
		} else {
			send_bt.setPressed(false);
			send_bt.setClickable(false);
			send_bt.setTextColor(getResources().getColor(R.color.common_text_hint));
		}
	}*/

	/**
	 * 监听返回按钮
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			/*if(position==1){
				this.finish();
				intent.setClass(GalleryActivity.this, AlbumActivity.class);
				startActivity(intent);
			}else if(position==2){
				this.finish();
				intent.setClass(GalleryActivity.this, ShowAllPhoto.class);
				startActivity(intent);
			}*/
			this.finish();
			overridePendingTransition(0,
					R.anim.zoomout);
		}
		return true;
	}
	
	
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Iterator<ImageItem> iter = Bimp.tempSelectBitmap.iterator();  
		while(iter.hasNext()){  
			if(!iter.next().isSelected()){
				iter.remove(); 
			}
		}
		Intent intent = new Intent("data.broadcast.action");  
        sendBroadcast(intent);  
	}

	class MyPageAdapter extends PagerAdapter {

		private ArrayList<View> listViews;

		private int size;
		public MyPageAdapter(ArrayList<View> listViews) {
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

		public void setListViews(ArrayList<View> listViews) {
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

		public int getCount() {
			return size;
		}

		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPagerFixed) arg0).removeView(listViews.get(arg1 % size));
		}

		public void finishUpdate(View arg0) {
		}

		public Object instantiateItem(View arg0, int arg1) {
			try {
				((ViewPagerFixed) arg0).addView(listViews.get(arg1 % size), 0);

			} catch (Exception e) {
			}
			return listViews.get(arg1 % size);
		}

		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

	}
	
	private void changeNum(){
		if(Bimp.tempSelectBitmap.size()>0){
			badge.setBadgeBackgroundColor(getResources().getColor(R.color.green));
			badge.setBadgePosition(BadgeView.POSITION_CENTER_LEFT);
//	    	badge4.setBadgeMargin(15, 10);
			badge.setText(PublicWay.getTempSelectNum()+""); 
			badge.setTextSize(getResources().getDimension(R.dimen.badgeview_text_size));
			badge.show();
			send_bt.setPressed(true);
			send_bt.setClickable(true);
			send_bt.setTextColor(getResources().getColor(R.color.green));
		}else{
			badge.hide();
			send_bt.setPressed(false);
			send_bt.setClickable(false);
			send_bt.setTextColor(getResources().getColor(R.color.common_text_hint));
		}
		
	}
	
}
