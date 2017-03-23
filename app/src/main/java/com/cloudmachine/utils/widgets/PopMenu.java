package com.cloudmachine.utils.widgets;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.cloudmachine.R;
import com.cloudmachine.struc.PopmenuDataInfo;
import com.cloudmachine.utils.Constants;


public class PopMenu {
	private ArrayList<PopmenuDataInfo> itemList;
	private Context context;
	private PopupWindow popupWindow ;
	private ListView listView;
	//private OnItemClickListener listener;
	
	private PopAdapter popAdapter;
	private Handler handler;
	public PopMenu(Context context,Handler handler) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.handler = handler;
		itemList = new ArrayList<PopmenuDataInfo>(5);
		View view = LayoutInflater.from(context).inflate(R.layout.popmenu, null);
        
		popAdapter = new PopAdapter();
        //设置 listview
        listView = (ListView)view.findViewById(R.id.popmenu_listview);
        listView.setAdapter(popAdapter);
        popupWindow = new PopupWindow(view, 100, LayoutParams.WRAP_CONTENT);
        popupWindow = new PopupWindow(view, 
        		context.getResources().getDimensionPixelSize(R.dimen.popmenu_width), 
        		LayoutParams.WRAP_CONTENT);
        
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景（很神奇的）
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
	}

	//设置菜单项点击监听器
	public void setOnItemClickListener(OnItemClickListener listener) {
		//this.listener = listener;
		listView.setOnItemClickListener(listener);
	}

	//批量添加菜单项
	public void addItems(PopmenuDataInfo[] items) {
		for (PopmenuDataInfo s : items)
			itemList.add(s);
		
	}

	//单个添加菜单项
	public void addItem(PopmenuDataInfo item) {
		itemList.add(item);
	}

	public ArrayList<PopmenuDataInfo> getItemList(){
		return itemList;
	}
	//下拉式 弹出 pop菜单 parent 右下角
	public void showAsDropDown(View parent) {
		
		popupWindow.showAsDropDown(parent, context.getResources().getDimensionPixelSize(R.dimen.popmenu_marginLeft), 
				//保证尺寸是根据屏幕像素密度来的
				context.getResources().getDimensionPixelSize(R.dimen.popmenu_yoff));
		
		// 使其聚集
        popupWindow.setFocusable(true);
        // 设置允许在外点击消失
        popupWindow.setOutsideTouchable(true);
        //刷新状态
        popupWindow.update();
       
	}
	
	//隐藏菜单
	public void dismiss() {
		popupWindow.dismiss();
	}

	// 适配器
	private final class PopAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return itemList==null?0:itemList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return itemList==null?null:itemList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(R.layout.pomenu_item, null);
				holder = new ViewHolder();
				holder.groupItem = (TextView) convertView.findViewById(R.id.textView);
				holder.myToggleButton = (ToggleButton) convertView.findViewById(R.id.myToggleButton);
				
				convertView.setTag(holder);

				
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.groupItem.setText(itemList.get(position).getName());
			holder.myToggleButton.setChecked(itemList.get(position).getIsOpen());
//			holder.myToggleButton.setOnCheckedChangeListener(new myCheckedChangeListener(position));
			holder.myToggleButton.setOnClickListener(new myClickListener(position));
			return convertView;
		}

		private final class ViewHolder {
			TextView groupItem;
			ToggleButton myToggleButton;
		}
		class myClickListener implements OnClickListener{
			int position;
			myClickListener (int position){
				this.position = position;
			}
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				itemList.get(position).setIsOpen(!itemList.get(position).getIsOpen());
				Message msg = Message.obtain();
				msg.what = Constants.HANDLER_CHANGE_MAP;
				msg.arg1 = position;
				handler.sendMessage(msg);
			}
			
		}
		/*class  myCheckedChangeListener implements OnCheckedChangeListener{
			private int position;
			myCheckedChangeListener(int position){
				this.position = position;
			}
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				
				
				if(itemList.get(position).getIsOpen()!=isChecked){
					itemList.get(position).setIsOpen(isChecked);
					Message msg = Message.obtain();
					msg.what = Main5FM.HANDLER_CHANGE_MAP;
					msg.arg1 = position;
					handler.sendMessage(msg);
				}
				
				
			}
			
		}*/
	}
}
