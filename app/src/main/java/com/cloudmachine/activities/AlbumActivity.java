package com.cloudmachine.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.cloudmachine.R;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.photo.adapter.AlbumGridViewAdapter;
import com.cloudmachine.utils.photo.util.AlbumHelper;
import com.cloudmachine.utils.photo.util.Bimp;
import com.cloudmachine.utils.photo.util.ImageBucket;
import com.cloudmachine.utils.photo.util.ImageItem;
import com.cloudmachine.utils.photo.util.PublicWay;
import com.cloudmachine.utils.photo.util.Res;
import com.cloudmachine.utils.widgets.BadgeView;
import com.cloudmachine.utils.widgets.TitleView;

import java.util.ArrayList;
import java.util.List;


/**
 * 这个是进入相册显示所有图片的界面
 * 
 * @author king
 * @QQ:595163260
 * @version 2014年10月18日  下午11:47:15
 */
public class AlbumActivity extends AppCompatActivity {
	//显示手机里的所有图片的列表控件
	private GridView gridView;
	//当手机里没有图片时，提示用户没有图片的控件
	private TextView tv;
	//gridView的adapter
	private AlbumGridViewAdapter gridImageAdapter;
	//完成按钮
	private Button okButton;
	private Intent intent;
	// 预览按钮
	private Button preview;
	private Context mContext;
	private ArrayList<ImageItem> dataList;
	private AlbumHelper helper;
	public static List<ImageBucket> contentList;
	public static Bitmap bitmap;
	
	private TitleView title_layout;
	private BadgeView badge;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(Res.getLayoutID("plugin_camera_album"));
		PublicWay.activityList.add(this);
		mContext = this;
		//注册一个广播，这个广播主要是用于在GalleryActivity进行预览时，防止当所有图片都删除完后，再回到该页面时被取消选中的图片仍处于选中状态
		IntentFilter filter = new IntentFilter("data.broadcast.action");  
		registerReceiver(broadcastReceiver, filter);  
        bitmap = BitmapFactory.decodeResource(getResources(),Res.getDrawableID("plugin_camera_no_pictures"));
        init();
		initListener();
		//这个函数主要用来控制预览和完成按钮的状态
		isShowOkBt();
	}
	
	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {  
		  
        @Override  
        public void onReceive(Context context, Intent intent) {  
        	//mContext.unregisterReceiver(this);
            // TODO Auto-generated method stub  
        	gridImageAdapter.notifyDataSetChanged();
        }  
    };  

	// 预览按钮的监听
	private class PreviewListener implements OnClickListener {
		public void onClick(View v) {
			if (Bimp.tempSelectBitmap.size() > 0) {
				Constants.gotoImageBrower(AlbumActivity.this, "1", 0);
			}
		}

	}

	// 完成按钮的监听
	private class AlbumSendListener implements OnClickListener {
		public void onClick(View v) {
//			overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
//			intent.setClass(mContext, AddDeviceActivity.class);
//			startActivity(intent);
			finish();
			
		}

	}

	// 返回按钮监听
	private class BackListener implements OnClickListener {
		public void onClick(View v) {
			intent.setClass(AlbumActivity.this, ImageFile.class);
			startActivity(intent);
		}
	}

	// 取消按钮的监听
	private class CancelListener implements OnClickListener {
		public void onClick(View v) {
			Bimp.tempSelectBitmap.clear();
//			intent.setClass(mContext, AddDeviceActivity.class);
//			startActivity(intent);
			finish();
			
		}
	}

	

	// 初始化，给一些对象赋值
	private void init() {
		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());
		
		contentList = helper.getImagesBucketList(false);
		dataList = new ArrayList<ImageItem>();
		for(int i = 0; i<contentList.size(); i++){
			dataList.addAll( contentList.get(i).imageList );
		}
		initTitleLayout();
		
		preview = (Button) findViewById(Res.getWidgetID("preview"));
		preview.setOnClickListener(new PreviewListener());
		intent = getIntent();
		Bundle bundle = intent.getExtras();
		gridView = (GridView) findViewById(Res.getWidgetID("myGrid"));
		gridImageAdapter = new AlbumGridViewAdapter(this,dataList,
				Bimp.tempSelectBitmap);
		gridView.setAdapter(gridImageAdapter);
		tv = (TextView) findViewById(Res.getWidgetID("myText"));
		gridView.setEmptyView(tv);
		okButton = (Button) findViewById(Res.getWidgetID("ok_button"));
		badge = new BadgeView(this, okButton);
		changeNum();
//		okButton.setText(Res.getString("finish")+"(" + Bimp.tempSelectBitmap.size()
//				+ "/"+PublicWay.num+")");
	}

	private void initTitleLayout() {

		title_layout = (TitleView) findViewById(R.id.title_layout);
		title_layout.setTitle("照片");
		title_layout.setLeftImage(-1, new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	private void initListener() {
		gridImageAdapter
		.setOnImageItemClickListener(new AlbumGridViewAdapter.OnImageItemClickListener() {

			@Override
			public void onItemClick(
					int position, boolean isChecked) {
//				Constants.gotoImageBrower(AlbumActivity.this, "2", position);
				/*int size = dataList.size();
				String[] urls = new String[size];
				for(int i=0; i<size; i++){
					urls[i] = "file://"+(dataList.get(i).imagePath!=null?dataList.get(i).imagePath:dataList.get(i).thumbnailPath);
				}*/
				
				Constants.gotoImageBrower(AlbumActivity.this, position, dataList, true);
			}
		});
		
		gridImageAdapter
				.setOnChooseClickListener(new AlbumGridViewAdapter.OnChooseClickListener() {

					@Override
					public void onItemClick(final ToggleButton toggleButton,
							int position, boolean isChecked,Button chooseBt) {
						if (Bimp.tempSelectBitmap.size() >= PublicWay.num) {
							toggleButton.setChecked(false);
//							chooseBt.setVisibility(View.GONE);
							if (!removeOneData(dataList.get(position))) {
								Toast.makeText(AlbumActivity.this, Res.getString("only_choose_num"),
										Toast.LENGTH_SHORT).show();
							}
							return;
						}
						dataList.get(position).setSelected(isChecked);
						if (isChecked) {
//							ImageItem temp = new ImageItem();
							Bimp.tempSelectBitmap.add(dataList.get(position));
							changeNum();
						} else {
							Bimp.tempSelectBitmap.remove(dataList.get(position));
							changeNum();
						}
						isShowOkBt();
					}
				});

		okButton.setOnClickListener(new AlbumSendListener());

	}

	private boolean removeOneData(ImageItem imageItem) {
			if (Bimp.tempSelectBitmap.contains(imageItem)) {
				Bimp.tempSelectBitmap.remove(imageItem);
//				okButton.setText(Res.getString("finish")+"(" +Bimp.tempSelectBitmap.size() + "/"+PublicWay.num+")");
				changeNum();
				return true;
			}
		return false;
	}
	
	public void isShowOkBt() {
		if (Bimp.tempSelectBitmap.size() > 0) {
//			okButton.setText(Res.getString("finish")+"(" + Bimp.tempSelectBitmap.size() + "/"+PublicWay.num+")");
			changeNum();
			preview.setPressed(true);
			okButton.setPressed(true);
			preview.setClickable(true);
			okButton.setClickable(true);
			okButton.setTextColor(getResources().getColor(R.color.green));
			preview.setTextColor(getResources().getColor(R.color.black));
		} else {
//			okButton.setText(Res.getString("finish")+"(" + Bimp.tempSelectBitmap.size() + "/"+PublicWay.num+")");
			changeNum();
			preview.setPressed(false);
			preview.setClickable(false);
			okButton.setPressed(false);
			okButton.setClickable(false);
			okButton.setTextColor(getResources().getColor(R.color.common_text_hint));//Color.parseColor("#E1E0DE")
			preview.setTextColor(getResources().getColor(R.color.common_text_hint));
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			intent.setClass(AlbumActivity.this, ImageFile.class);
//			startActivity(intent);
			finish();
		}
		return false;

	}
@Override
protected void onRestart() {
	isShowOkBt();
	super.onRestart();
}
private void changeNum(){
	if(Bimp.tempSelectBitmap.size()>0){
		badge.setBadgeBackgroundColor(getResources().getColor(R.color.green));
		badge.setBadgePosition(BadgeView.POSITION_CENTER_LEFT);
//    	badge4.setBadgeMargin(15, 10);
		badge.setText(Bimp.tempSelectBitmap.size()+""); 
		badge.setTextSize(getResources().getDimension(R.dimen.badgeview_text_size));
		badge.show();
	}else{
		badge.hide();
	}
	
}

@Override
protected void onDestroy() {
	// TODO Auto-generated method stub
	super.onDestroy();
	if(null != dataList){
		dataList.clear();
		dataList = null;
	}
	if(null != contentList){
		contentList.clear();
		contentList = null;
	}
	if(null != helper){
		helper.ReleaseAlbum();
		helper = null;
	}
	unregisterReceiver(broadcastReceiver);
}



}
