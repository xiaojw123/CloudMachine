package com.cloudmachine.activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.base.SupportBaseAutoActivity;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.photo.util.Bimp;
import com.cloudmachine.utils.photo.util.ImageItem;
import com.cloudmachine.utils.photo.util.PublicWay;
import com.cloudmachine.utils.widgets.BadgeView;
import com.cloudmachine.widget.CommonTitleView;
import com.cloudmachine.widget.HackyViewPager;
import com.cloudmachine.widget.ImageDetailFragment;

import java.util.ArrayList;

public class ImagePagerActivity extends SupportBaseAutoActivity implements Callback,OnClickListener{
	private static final String STATE_POSITION = "STATE_POSITION";
	public static final String EXTRA_IMAGE_INDEX = "image_index";
	public static final String EXTRA_IMAGE_ITEM = "image_item";
	public static final String EXTRA_IMAGE_TYPE = "image_type";

	private Handler mHandler;
	private Context mContext;
	private ImagePagerAdapter mAdapter;
	private HackyViewPager mPager;
//	private String[] urls;
	private ArrayList<ImageItem> dataList;
	private int pagerPosition;
	private int animType; 
	private CommonTitleView title_layout;
	private boolean isDelete = false;
	private boolean isCanDelete = false;
	private boolean isType = false;
	private Button send_button;
	private BadgeView badge;
	private View bottom_layout;
	private View delet_image_layout;
	private TextView delet_ok,delet_cancel;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_detail_pager);
		mContext = this;
		mHandler = new Handler(this);
		getIntentData();
		initTitleLayout();
		initView(savedInstanceState);
		changeNum();
	}

	@Override
	public void initPresenter() {

	}

	private void getIntentData(){
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
        	try{
        		pagerPosition = bundle.getInt(EXTRA_IMAGE_INDEX, 0);
        		animType = bundle.getInt(EXTRA_IMAGE_TYPE, 0);
        		isCanDelete = bundle.getBoolean(Constants.P_IMAGEBROWERDELETE, false);
        		dataList = (ArrayList<ImageItem>)bundle.getSerializable(EXTRA_IMAGE_ITEM);
        		
        		/*int size = dataList.size();
        		urls = new String[size];
				for(int i=0; i<size; i++){
					if(null !=dataList.get(i).imagePath){
						urls[i] = "file://"+dataList.get(i).imagePath;
					}else if(null !=dataList.get(i).thumbnailPath){
						urls[i] = "file://"+dataList.get(i).thumbnailPath;
					}else if(null != dataList.get(i).imageUrl){
						urls[i] =dataList.get(i).imageUrl;
					}else{
						urls[i] ="";
					}
//					urls[i] = "file://"+(dataList.get(i).imagePath!=null?dataList.get(i).imagePath:dataList.get(i).thumbnailPath);
				}*/
        	}catch(Exception e){
        	}
        	
        }
	}
   private void initView(Bundle savedInstanceState){
	   
	   delet_image_layout = findViewById(R.id.delet_image_layout);
	   delet_image_layout.setOnClickListener(this);
	   delet_ok = (TextView)findViewById(R.id.delet_ok);
	   delet_cancel = (TextView)findViewById(R.id.delet_cancel);
	   delet_ok.setOnClickListener(this);
	   delet_cancel.setOnClickListener(this);
	   bottom_layout = findViewById(R.id.bottom_layout);
	   if(animType == 2 || animType == 1){
		   bottom_layout.setVisibility(View.GONE);
	   }
	   mPager = (HackyViewPager) findViewById(R.id.pager);
		mAdapter = new ImagePagerAdapter(
				getSupportFragmentManager(), dataList);
		
		mPager.setAdapter(mAdapter);
		// 更新下标
				mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

					@Override
					public void onPageScrollStateChanged(int arg0) {
					}

					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) {
					}

					@Override
					public void onPageSelected(int arg0) {
						pagerPosition = arg0;
						updataTitleText(arg0);
						initTitleRightImage();
					}

				});
				if (savedInstanceState != null) {
					pagerPosition = savedInstanceState.getInt(STATE_POSITION);
				}

				mPager.setCurrentItem(pagerPosition);
				
				send_button = (Button)findViewById(R.id.send_button);
				send_button.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						mHandler.sendEmptyMessage(Constants.HANDLER_FINISH_IMAGEPAGERACTIVITY);
					}
				});
				badge = new BadgeView(this, send_button);
   }
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_POSITION, mPager.getCurrentItem());
	}

	private class ImagePagerAdapter extends FragmentStatePagerAdapter {

		public ArrayList<ImageItem> fileList;

		public ImagePagerAdapter(FragmentManager fm, ArrayList<ImageItem> fileList) {
			super(fm);
			this.fileList = fileList;
		}

		public void setData(ArrayList<ImageItem> fileList){
			this.fileList = fileList;
		}
		@Override
		public int getCount() {
			return fileList == null ? 0 : fileList.size();
		}


		public Fragment getItem(int position) {
			ImageItem item = fileList.get(position);
			String url = "";
			if(null !=item.imagePath){
				url = "file://"+item.imagePath;
			}else if(null !=item.thumbnailPath){
				url = "file://"+item.thumbnailPath;
			}else if(null != item.imageUrl){
				url =item.imageUrl;
			}else{
				url ="";
			}
			return ImageDetailFragment.newInstance(url,mHandler);
		}

	}
	
	private void initTitleLayout() {

		title_layout = (CommonTitleView) findViewById(R.id.title_layout);
		if(animType == 2){
			title_layout.setTitleName("");
		}else if(animType == 1){
			title_layout.setRightImg(R.drawable.bg_btn_delet,
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							delet_image_layout.setVisibility(View.VISIBLE);
						}
					});
			updataTitleText(0);
		}else{
			if(isCanDelete){
				title_layout.setRightImg(R.drawable.plugin_camera_choosed,
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
//								showPickDialog1();
								if(dataList.get(pagerPosition).isSelected()){
									dataList.get(pagerPosition).setSelected(false);
									Bimp.tempSelectBitmap.remove(dataList.get(pagerPosition));
								}else{
									dataList.get(pagerPosition).setSelected(true);
									Bimp.tempSelectBitmap.add(dataList.get(pagerPosition));
								}
								initTitleRightImage();
								Intent intent = new Intent("data.broadcast.action");  
				                sendBroadcast(intent);  
				                changeNum();
							}
						});
			}
			
			updataTitleText(0);
		}
	}
	private void updataTitleText(int index){
		String text = "";
		if(null != dataList && dataList.size()>0)
			text = getString(R.string.viewpager_indicator,
				index + 1, dataList.size());
		title_layout.setTitleName(text);
	}
	
	private void initTitleRightImage(){
		if(animType != 0)
			return;
		
		int imageSize = dataList.size();
		if(imageSize>pagerPosition){
			if(dataList.get(pagerPosition).isSelected){
				title_layout.setRightImg(R.drawable.plugin_camera_choosed,null);
			}else{
				title_layout.setRightImg(R.drawable.plugin_camera_unchoosed,null);
			}
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		switch(msg.what){
		case Constants.HANDLER_FINISH_IMAGEPAGERACTIVITY:
			/*if(isCanDelete && isDelete){
				Intent intent=new Intent();  
				intent.putExtra(Constants.P_IMAGEBROWERURLS, urls);
				intent.putExtra(Constants.P_IMAGEBROWERDELETE, isDelete);
				setResult(RESULT_OK, intent); 
			}*/
			finish();
			if(animType == 2){
				overridePendingTransition(0,
						R.anim.zoomout_2);
			}else{
				overridePendingTransition(0,
						R.anim.zoomout);
			}
			
			break;
		}
		return false;
	}
	
	/*private void showPickDialog1() {
		View cView = getLayoutInflater().inflate(
				R.layout.layout_dialog, null);
		TextView text = (TextView)cView.findViewById(R.id.dialog_text);
		text.setText("确定要删除这张照片吗");
		new AlertDialog.Builder(mContext)
		.setNegativeButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				
				isDelete = true;
				urls = Constants.arrayDeleteSring(urls, pagerPosition);
				if(urls.length>0){
					mAdapter = new ImagePagerAdapter(
							getSupportFragmentManager(), urls);
					mPager.setAdapter(mAdapter);
					mAdapter.notifyDataSetChanged();
					if(pagerPosition>=urls.length){
						pagerPosition = urls.length-1;
					}
					mPager.setCurrentItem(pagerPosition);
					updataTitleText(pagerPosition);
				}else{
					mHandler.sendEmptyMessage(Constants.HANDLER_FINISH_IMAGEPAGERACTIVITY);
				}
				
			}
		})
		.setPositiveButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
				
			}
		})
		.setView(cView)
		.show();
	}*/
	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			mHandler.sendEmptyMessage(Constants.HANDLER_FINISH_IMAGEPAGERACTIVITY);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void changeNum(){
		if(Bimp.tempSelectBitmap.size()>0){
			badge.setBadgeBackgroundColor(getResources().getColor(R.color.green));
			badge.setBadgePosition(BadgeView.POSITION_CENTER_LEFT);
//	    	badge4.setBadgeMargin(15, 10);
			badge.setText(PublicWay.getTempSelectNum()+""); 
			badge.setTextSize(getResources().getDimension(R.dimen.badgeview_text_size));
			badge.show();
			send_button.setPressed(true);
			send_button.setClickable(true);
			send_button.setTextColor(getResources().getColor(R.color.green));
		}else{
			badge.hide();
			send_button.setPressed(false);
			send_button.setClickable(false);
			send_button.setTextColor(getResources().getColor(R.color.common_text_hint));
		}
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		/*Iterator<ImageItem> iter = Bimp.tempSelectBitmap.iterator();  
		while(iter.hasNext()){  
			if(!iter.next().isSelected()){
				iter.remove(); 
			}
		}
		Intent intent = new Intent("data.broadcast.action");  
        sendBroadcast(intent); */
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.delet_ok:
			
			if(null != Bimp.tempSelectBitmap && Bimp.tempSelectBitmap.size()>0){
				Bimp.tempSelectBitmap.remove(pagerPosition);
				dataList.remove(pagerPosition);
				if(Bimp.tempSelectBitmap.size() == 0){
					mHandler.sendEmptyMessage(Constants.HANDLER_FINISH_IMAGEPAGERACTIVITY);
					return;
				}
				mAdapter = null;
				mAdapter = new ImagePagerAdapter(
						getSupportFragmentManager(), dataList);
				mPager.setAdapter(mAdapter);
//				mAdapter.setData(dataList);
				if(Bimp.tempSelectBitmap.size()>pagerPosition){
					mPager.setCurrentItem(pagerPosition);
				}else{
					mPager.setCurrentItem(--pagerPosition);
				}
//				mPager.setCurrentItem(0);
				mAdapter.notifyDataSetChanged();
//				initTitleRightImage();
//				Intent intent = new Intent("data.broadcast.action");  
//	            sendBroadcast(intent);  
//	            changeNum();
	            delet_image_layout.setVisibility(View.GONE);
	            updataTitleText(pagerPosition);
			}else{
				mHandler.sendEmptyMessage(Constants.HANDLER_FINISH_IMAGEPAGERACTIVITY);
			}
			
			
			break;
		case R.id.delet_cancel:
		case R.id.delet_image_layout:
			delet_image_layout.setVisibility(View.GONE);
			
			break;
		}
	}
	
	
}