package com.cloudmachine.adapter;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.net.task.MessageUpdateStatusAsync;
import com.cloudmachine.struc.MessageBO;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MessageAdapter extends BaseAdapter{
	
	private Context mContext;
	private List<MessageBO> data;
	private Handler myHandler;
	private boolean isLoading;
	
	public MessageAdapter (Context context,List<MessageBO> data,Handler handler){
		mContext = context;
		this.data = data;
		myHandler = handler;
	}
	
	
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public MessageBO getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if(convertView==null){
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.inner_message_list1,null);
			viewHolder.imageView = (ImageView)convertView.findViewById(R.id.message_body);//照片
			viewHolder.time = (TextView)convertView.findViewById(R.id.message_time);//系统通知时间
			viewHolder.llSelect = (LinearLayout) convertView.findViewById(R.id.ll_select);
			viewHolder.dividerBottom = convertView.findViewById(R.id.divider2);
			viewHolder.llDetail = (LinearLayout) convertView.findViewById(R.id.ll_detail);
			viewHolder.messageOval = convertView.findViewById(R.id.message_oval);
			viewHolder.invite_name = (TextView) convertView.findViewById(R.id.invite_name);
			viewHolder.info_inv = (TextView) convertView.findViewById(R.id.info_inv);
			viewHolder.llrefuse = (LinearLayout) convertView.findViewById(R.id.ll_resfuse);
			viewHolder.llaccept = (LinearLayout) convertView.findViewById(R.id.ll_accept);
			viewHolder.tv2 = (TextView) convertView.findViewById(R.id.device_name_message);
			viewHolder.tv3 = (TextView) convertView.findViewById(R.id.tv3);
			viewHolder.dividerTop = convertView.findViewById(R.id.divider1);
			viewHolder.arrowRight = (ImageView) convertView.findViewById(R.id.arrow_right);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		MessageBO item = data.get(position);
		if(item.getMessageType() == Constants.MESSAGETYPE[3] 
				|| item.getMessageType() == Constants.MESSAGETYPE[4]
						||item.getMessageType() == Constants.MESSAGETYPE[5]){//系统消息
			viewHolder.arrowRight.setVisibility(View.VISIBLE);
			viewHolder.tv2.setVisibility(View.GONE);
			viewHolder.tv3.setVisibility(View.GONE);
			viewHolder.llSelect.setVisibility(View.GONE);
			viewHolder.dividerBottom.setVisibility(View.GONE);
			viewHolder.dividerTop.setVisibility(View.VISIBLE);
			viewHolder.llDetail.setOnClickListener(new ActionClickListener(3, position, item.getId(), item.getInviterId()));
			
			switch(item.getStatus()){
			case 1:
				viewHolder.messageOval.setVisibility(View.VISIBLE);
				break;
			case 4:
				viewHolder.messageOval.setVisibility(View.INVISIBLE);
				break;
			}
			
			if(item.getMessageType() == Constants.MESSAGETYPE[5]){
				viewHolder.invite_name.setText(item.getTitle());
				viewHolder.time.setText(item.getInviteTime());
				viewHolder.info_inv.setVisibility(View.INVISIBLE);
			}else{
				viewHolder.invite_name.setSingleLine(true);
				viewHolder.invite_name.setText(item.getTitle());
				viewHolder.time.setText(item.getInviteTime());
				viewHolder.info_inv.setText(item.getContent());
			}
			viewHolder.imageView.setImageResource(R.drawable.system1);
		}else{
			viewHolder.arrowRight.setVisibility(View.INVISIBLE);
			viewHolder.tv2.setVisibility(View.VISIBLE);
			viewHolder.tv3.setVisibility(View.VISIBLE);
			viewHolder.llSelect.setVisibility(View.VISIBLE);
			viewHolder.dividerBottom.setVisibility(View.VISIBLE);
			
			switch(item.getStatus()){
			case 1:
				viewHolder.messageOval.setVisibility(View.VISIBLE);       
				viewHolder.llSelect.setVisibility(View.VISIBLE);
				viewHolder.dividerBottom.setVisibility(View.VISIBLE);
				if (item.getMessageType() == 1) {
					viewHolder.info_inv.setText("邀请你共享 ");
					viewHolder.tv2.setText(item.getDeviceName());
					viewHolder.tv3.setText(" 该设备的数据");
				}else if(item.getMessageType() == 2){
					viewHolder.info_inv.setText("技师移交 ");
					viewHolder.tv2.setText(item.getDeviceName());
					viewHolder.tv3.setText(" 给您，请接受");
				}
				break;
				
			case 2:
				
				viewHolder.messageOval.setVisibility(View.INVISIBLE);
				viewHolder.llSelect.setVisibility(View.GONE);
				viewHolder.dividerTop.setVisibility(View.GONE);
				if (item.getMessageType() == 1) {
					viewHolder.info_inv.setText("邀请你共享 ");
					viewHolder.tv2.setText(item.getDeviceName());
					viewHolder.tv3.setText(" 该设备的数据，已拒绝");
				}else if(item.getMessageType() == 2){
					viewHolder.info_inv.setText("技师移交 ");
					viewHolder.tv2.setText(item.getDeviceName());
					viewHolder.tv3.setText(" 给您，已拒绝");
				}
				break;
			case 3:
				viewHolder.dividerTop.setVisibility(View.GONE);
				viewHolder.messageOval.setVisibility(View.INVISIBLE);
				viewHolder.llSelect.setVisibility(View.GONE);
				
				if (item.getMessageType() == 1) {
					viewHolder.info_inv.setText("邀请你共享 ");
					viewHolder.tv2.setText(item.getDeviceName());
					viewHolder.tv3.setText(" 该设备的数据，已接受");
				}else if(item.getMessageType() == 2){
					viewHolder.info_inv.setText("技师移交 ");
					viewHolder.tv2.setText(item.getDeviceName());
					viewHolder.tv3.setText(" 给您，已接受");
				}
				break;
			}
			
			viewHolder.invite_name.setText(item.getInviterNickname());
			viewHolder.time.setText(item.getInviteTime());
			ImageLoader.getInstance().displayImage(item.getImgpath(), viewHolder.imageView,Utils.displayImageOptions);
		}
		
		
		
		viewHolder.llaccept.setOnClickListener(new ActionClickListener(1, position, item.getId(), item.getInviterId()));//点击事件
		viewHolder.llrefuse.setOnClickListener(new ActionClickListener(2, position, item.getId(), item.getInviterId()));
		
		
		
		return convertView;
	}
	
	class ActionClickListener implements OnClickListener{
		private int position;
		private int type ;
		private long itemId;
		private long inviteId;
		ActionClickListener(int type,int position,long itemId, long inviteId){
			this.type = type;
			this.position = position;
			this.itemId = itemId;
			this.inviteId = inviteId;
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(type == 3){
				Message msg = Message.obtain();
				msg.what = Constants.HANDLER_GOTO_MESSAGECONTENT;
				msg.arg1 = position;
				myHandler.sendMessage(msg);
			}
			if(null != data){
				if(data.get(position).getMessageType() != Constants.MESSAGETYPE[5])
					new MessageUpdateStatusAsync(type,itemId,inviteId,position,mContext,myHandler).execute();
			}
		}
		
	}
    private class ViewHolder{
    	private ImageView imageView;
    	
    	private TextView time;//时间复用
    	private LinearLayout llSelect; //下半部分，接受或者拒绝布局
    	private View dividerBottom;    //底部分割线
    	private LinearLayout llDetail; //上半部分，消息详情布局
    	private View messageOval;  //小圆点，标识是否阅读过消息
    	private TextView invite_name;//邀请人昵称，消息页面中标识消息状态
    	private TextView info_inv;	//昵称下面的文字，第一段
    	private LinearLayout llaccept;
    	private LinearLayout llrefuse;
    	private TextView tv2; //昵称下面的文字，第二段
    	private TextView tv3; //昵称下面的文字，第三段
    	private View dividerTop;
    	private ImageView arrowRight;
    }

}
