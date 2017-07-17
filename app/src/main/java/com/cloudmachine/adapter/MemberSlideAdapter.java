package com.cloudmachine.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudm.autolayout.utils.AutoUtils;
import com.cloudmachine.R;
import com.cloudmachine.app.MyApplication;
import com.cloudmachine.autolayout.widgets.CustomDialog;
import com.cloudmachine.autolayout.widgets.SlideView;
import com.cloudmachine.autolayout.widgets.SlideView.OnSlideListener;
import com.cloudmachine.listener.OnClickEffectiveListener;
import com.cloudmachine.net.task.DevicesDeleteMemberAsync;
import com.cloudmachine.struc.MemberInfoSlide;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.UMengKey;
import com.cloudmachine.utils.widgets.RadiusButtonView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.umeng.analytics.MobclickAgent;

import java.util.List;


public class MemberSlideAdapter extends BaseAdapter implements OnSlideListener{
	private List<MemberInfoSlide> dataResult ;
	private Context context;
	private Handler handler;
	private long deviceId;
	private int deviceType;
	private LayoutInflater layoutInflater;
	private DisplayImageOptions displayImageOptions;
	private ImageLoadingListener commImageLoadingLis;
	private int searchListType;
	private SlideView mLastSlideViewWithStatusOn;

	public MemberSlideAdapter(List<MemberInfoSlide> dataResult,long deviceId,int deviceType,Context context, Handler mHandler) {
        super();
        this.dataResult = dataResult;
        this.context = context;
        this.handler = mHandler;
        this.deviceId = deviceId;
        this.deviceType = deviceType;		
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
    public MemberInfoSlide getItem(int position) {
        return dataResult.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        SlideView slideView = (SlideView) convertView;
        
        if (slideView == null) {
            View itemView = layoutInflater.inflate(R.layout.mc_search_listview, null);
            AutoUtils.auto(itemView);
            slideView = new SlideView(context);
            slideView.setContentView(itemView);
            holder = new ViewHolder(slideView);
            slideView.setOnSlideListener(this);
            slideView.setTag(holder);
            AutoUtils.auto(slideView);
        } else {
            holder = (ViewHolder) slideView.getTag();
        }
        
        //回退的效果
        MemberInfoSlide dInfo = dataResult.get(position);
		slideView.setTag(R.id.id_RemarkInfo,dInfo);
        if(!Constants.isNoEditInMcMember(deviceId, deviceType)){
        	if(position !=0){
            	dInfo.setSlideView(slideView);
                dInfo.getSlideView().shrink();
            }
        }
//        dInfo.setSlideView(slideView);
//        dInfo.getSlideView().shrink();
        
		String imgString = dInfo.getMiddlelogo();
//		holder.title.setText(dInfo.getName()!=null?dInfo.getName():"");
		String titleName;
		String remarkName=dInfo.getRoleRemark();
		if (!TextUtils.isEmpty(remarkName)){
			titleName=remarkName;
		}else{
			titleName=dInfo.getName();
		}
		holder.title.setText(titleName);
		if(null != MyApplication.getInstance().getTempMember()){
			if(dInfo.getMemberId() == MyApplication.getInstance().getTempMember().getId()){
				holder.title.setText("我");
			}
		}else{
			if(dInfo.getMemberId() == 0){
				holder.title.setText("我");
			}
		}
		
		if(position == 0){
			holder.title.setTextColor(context.getResources().getColor(R.color.public_blue_bg));
		}else{
			holder.title.setTextColor(context.getResources().getColor(R.color.public_black));
		}
//		holder.summary.setText(dInfo.getRoleRemark()!=null?dInfo.getRoleRemark():dInfo.getRole());
		holder.summary.setText(dInfo.getRole());
		ImageLoader.getInstance().displayImage(imgString, holder.info_img,
				displayImageOptions, commImageLoadingLis);
		holder.deleteHolder.setOnClickListener(new llClick(position));
        /*holder.icon.setImageResource(item.iconRes);
        holder.title.setText(item.title);
        holder.msg.setText(item.msg);
        holder.time.setText(item.time);
        holder.deleteHolder.setOnClickListener(ActivityListDelte.this);*/

        return slideView;
    }

    @Override
    public void onSlide(View view, int status) {
        if (mLastSlideViewWithStatusOn != null && mLastSlideViewWithStatusOn != view) {
            mLastSlideViewWithStatusOn.shrink();
        }

        if (status == SLIDE_STATUS_ON) {
            mLastSlideViewWithStatusOn = (SlideView) view;
        }
    }
    class llClick extends OnClickEffectiveListener{
		private int position;
		llClick(int position){
			this.position = position;
		}
		
		@Override
		public void onClickEffective(View v) {
			MobclickAgent.onEvent(context, UMengKey.count_memeber_delete);
			// TODO Auto-generated method stub
			if (! TextUtils.isEmpty(dataResult.get(position).getName())) {
				CustomDialog.Builder builder = new CustomDialog.Builder(context);
				String name = dataResult.get(position).getName();
				builder.setMessage("确定删除"+"'"+name+"'"+",删除之后该成员将无法查看此机器的数据", "");
				builder.setTitle("提示");
				builder.setLeftButtonColor(context.getResources().getColor(R.color.black));
				builder.setRightButtonColor(context.getResources().getColor(R.color.black));
				builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						return;
					}
				});
				builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						new DevicesDeleteMemberAsync(context,handler).execute(String.valueOf(dataResult.get(position).getMemberId()),
								String.valueOf(deviceId));
						dialog.dismiss();
					}
				});
				builder.create().show();
			}
			if (TextUtils.isEmpty(dataResult.get(position).getName())) {
				CustomDialog.Builder builder = new CustomDialog.Builder(context);
				builder.setLeftButtonColor(context.getResources().getColor(R.color.black));
				builder.setRightButtonColor(context.getResources().getColor(R.color.black));
				builder.setMessage("确定删除?"+",删除之后该成员将无法查看此机器的数据", "");
				builder.setTitle("提示");
				builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						return;
					}
				});
				builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						new DevicesDeleteMemberAsync(context,handler).execute(String.valueOf(dataResult.get(position).getMemberId()),
								String.valueOf(deviceId));
						dialog.dismiss();
					}
				});
				builder.create().show();
			}
//			new DevicesDeleteMemberAsync(context,handler).execute(String.valueOf(dataResult.get(position).getMemberId()),
//					String.valueOf(deviceId));
		}
		
	}

    
    static class ViewHolder {
		ImageView info_img;
		ImageView arrow;
		TextView title;
		TextView summary;
		RadiusButtonView arrow_add;
		ViewGroup deleteHolder;
		ViewHolder(View view) {
			info_img =  (ImageView) view
					.findViewById(R.id.info_img);
			arrow  = (ImageView)view.findViewById(R.id.arrow);
			title = (TextView) view
					.findViewById(R.id.title);
			summary = (TextView) view
					.findViewById(R.id.summary);
			arrow_add = (RadiusButtonView)view.findViewById(R.id.arrow_add);
			deleteHolder = (ViewGroup)view.findViewById(R.id.holder);
		}
	}
}
