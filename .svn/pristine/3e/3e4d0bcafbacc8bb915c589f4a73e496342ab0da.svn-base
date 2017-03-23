package com.cloudmachine.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudm.autolayout.utils.AutoUtils;
import com.cloudmachine.R;
import com.cloudmachine.main.MainActivity;
import com.cloudmachine.struc.FinishBean;
import com.cloudmachine.struc.UnfinishedBean;
import com.cloudmachine.utils.Constants;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class RepairCompleteAdapter extends BaseAdapter {

	private MainActivity mActivity;
	private LayoutInflater layoutInflater;
	private boolean haveShowOnce = false;
	// 图片第一次加载的监听器
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	final private DisplayImageOptions options = Constants.displayListImageOptions;
	final private ImageLoader imageLoader = ImageLoader.getInstance();
	private ListView listView;
	private String[] str_status;
	private ArrayList<FinishBean> finishBeans;
	private ArrayList<UnfinishedBean> unfinishedBeans;
	public static final int TYPE_FINISHED = 1;
	public static final int TYPE_UNFINISHED = 2;
	public static final int TYPE_TITLE = 3;

	public RepairCompleteAdapter(MainActivity mActivity,
			ArrayList<FinishBean> finishBeans,
			ArrayList<UnfinishedBean> unfinishedBeans) {
		this.mActivity = mActivity;
		this.layoutInflater = LayoutInflater.from(mActivity);
		this.finishBeans = finishBeans;
		this.unfinishedBeans = unfinishedBeans;
		str_status = mActivity.getResources()
				.getStringArray(R.array.str_status);
	}

	@Override
	public int getCount() {
		// if (finishBeans.size() != 0 || unfinishedBeans.size() != 0) {
		// return finishBeans.size() + unfinishedBeans.size() + 1;
		// }
		return (finishBeans.isEmpty() ? 0 : finishBeans.size())
				+ (unfinishedBeans.isEmpty() ? 0 : unfinishedBeans.size()) + 1;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public int getViewTypeCount() {
		return 3;
	}

	@Override
	public int getItemViewType(int position) {
		// if (position < unfinishedBeans.size()) {
		// return TYPE_UNFINISHED;
		// } else if (position == unfinishedBeans.size()) {
		// return TYPE_TITLE;
		// } else {
		// return TYPE_FINISHED;
		// }
		if (!unfinishedBeans.isEmpty() && position < unfinishedBeans.size()) {
			return TYPE_UNFINISHED;
		}
		if (!unfinishedBeans.isEmpty() && position == unfinishedBeans.size()
				&& finishBeans.size() != 0) {
			return TYPE_TITLE;
		}
		if (!finishBeans.isEmpty() && unfinishedBeans.size() != 0
				&& position > unfinishedBeans.size()) {
			return TYPE_FINISHED;
		}

		if (unfinishedBeans.isEmpty() && !finishBeans.isEmpty()
				&& position == 0) {
			return TYPE_TITLE;
		}
		if (!finishBeans.isEmpty() && unfinishedBeans.isEmpty() && position > 0) {
			return TYPE_FINISHED;
		}
		return -1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Constants.MyLog("getView");
		FinishHolder finishHolder = null;
		UnFinishHolder unFinishHolder = null;
		TitleHolder titleHolder = null;
		switch (getItemViewType(position)) {
		case TYPE_UNFINISHED:
			if (convertView == null) {
				unFinishHolder = new UnFinishHolder();
				convertView = layoutInflater.inflate(
						R.layout.list_item_repair_history, null);
				unFinishHolder.ivIcon = (ImageView) convertView
						.findViewById(R.id.iv_icon);
				unFinishHolder.deviceName = (TextView) convertView
						.findViewById(R.id.device_name);
				unFinishHolder.repairPlace = (TextView) convertView
						.findViewById(R.id.repair_place);
				unFinishHolder.repairDate = (TextView) convertView
						.findViewById(R.id.repair_date);
				unFinishHolder.arrowRight = (ImageView) convertView
						.findViewById(R.id.arrow_right);
				unFinishHolder.status = (TextView) convertView
						.findViewById(R.id.status);
				unFinishHolder.bgBottom = (View) convertView
						.findViewById(R.id.bg_bottom);
				unFinishHolder.bgBottom.setVisibility(View.VISIBLE);
				
				convertView.setTag(unFinishHolder);
				AutoUtils.autoSize(convertView);
			} else {
				unFinishHolder = (UnFinishHolder) convertView.getTag();
			}
			if (null != unfinishedBeans.get(position)) {
				unFinishHolder.deviceName.setText(unfinishedBeans.get(position)
						.getVbrandname()
						+ " "
						+ unfinishedBeans.get(position).getVmaterialname());
				unFinishHolder.repairPlace.setText(unfinishedBeans
						.get(position).getVdiscription());
				unFinishHolder.repairDate.setText(unfinishedBeans.get(position)
						.getDopportunity());
				String nStatusString = "";
				if (unfinishedBeans.get(position).getFlag().equals(0)) {
					if (unfinishedBeans.get(position).getNstatus()
							.equals(10220001)) {
						nStatusString = "已提交";
					} else if (unfinishedBeans.get(position).getNstatus()
							.equals(102200012)) {
						nStatusString = "已转工单";
					} else if (unfinishedBeans.get(position).getNstatus()
							.equals(10220003)) {
						nStatusString = "已取消";
					} else if (unfinishedBeans.get(position).getNstatus()
							.equals(10220004)) {
						nStatusString = "已跟踪";
					}
				}
				if (unfinishedBeans.get(position).getFlag().equals(1)) {
					if (unfinishedBeans.get(position).getNstatus().equals(0)) {
						nStatusString = "已下单";
					} else if (unfinishedBeans.get(position).getNstatus()
							.equals(1)) {
						nStatusString = "已派工";
					} else if (unfinishedBeans.get(position).getNstatus()
							.equals(2)) {
						nStatusString = "已技师确认";
					} else if (unfinishedBeans.get(position).getNstatus()
							.equals(3)) {
						nStatusString = "已预约";
					} else if (unfinishedBeans.get(position).getNstatus()
							.equals(4)) {
						nStatusString = "已出发";
					} else if (unfinishedBeans.get(position).getNstatus()
							.equals(5)) {
						nStatusString = "已到达";
					} else if (unfinishedBeans.get(position).getNstatus()
							.equals(6)) {
						nStatusString = "已客户确认";
					} else if (unfinishedBeans.get(position).getNstatus()
							.equals(7)) {
						nStatusString = "已结算";
					} else if (unfinishedBeans.get(position).getNstatus()
							.equals(8)) {
						nStatusString = "已完工关单";
					} else if (unfinishedBeans.get(position).getNstatus()
							.equals(9)) {
						nStatusString = "已返程";
					}
				}
				unFinishHolder.status.setText(nStatusString);
			}
			break;
		case TYPE_TITLE:
			if (convertView == null) {
				titleHolder = new TitleHolder();
				convertView = layoutInflater.inflate(
						R.layout.text_repair_completed, null);
				titleHolder.textComplete = (TextView) convertView
						.findViewById(R.id.tv_completed);
				convertView.setTag(titleHolder);
				AutoUtils.autoSize(convertView);
			} else {
				titleHolder = (TitleHolder) convertView.getTag();
			}
			titleHolder.textComplete.setText("已完成");
			break;
		case TYPE_FINISHED:
			if (convertView == null) {
				finishHolder = new FinishHolder();
				convertView = layoutInflater.inflate(
						R.layout.list_item_repair_history, null);
				finishHolder.ivIcon = (ImageView) convertView
						.findViewById(R.id.iv_icon);
				finishHolder.deviceName = (TextView) convertView
						.findViewById(R.id.device_name);
				finishHolder.repairPlace = (TextView) convertView
						.findViewById(R.id.repair_place);
				finishHolder.repairDate = (TextView) convertView
						.findViewById(R.id.repair_date);
				finishHolder.arrowRight = (ImageView) convertView
						.findViewById(R.id.arrow_right);
				finishHolder.status = (TextView) convertView
						.findViewById(R.id.status);
				finishHolder.bgBottom = (View) convertView
						.findViewById(R.id.bg_bottom);
				finishHolder.bgBottom.setVisibility(View.VISIBLE);
				convertView.setTag(finishHolder);
				AutoUtils.autoSize(convertView);
			} else {
				finishHolder = (FinishHolder) convertView.getTag();
			}
			if (null != finishBeans.get(position)) {
				finishHolder.deviceName.setText(unfinishedBeans.get(position)
						.getVbrandname()
						+ " "
						+ unfinishedBeans.get(position).getVmaterialname());
				finishHolder.repairPlace.setText(unfinishedBeans.get(position)
						.getVdiscription());
				finishHolder.repairDate.setText(unfinishedBeans.get(position)
						.getDopportunity());
				String nStatusString = "";
				if (finishBeans.get(position).getFlag().equals(0)) {
					if (finishBeans.get(position).getNstatus().equals(10220001)) {
						nStatusString = "已提交";
					} else if (finishBeans.get(position).getNstatus()
							.equals(102200012)) {
						nStatusString = "已转工单";
					} else if (finishBeans.get(position).getNstatus()
							.equals(10220003)) {
						nStatusString = "已取消";
					} else if (finishBeans.get(position).getNstatus()
							.equals(10220004)) {
						nStatusString = "已跟踪";
					}
				}
				if (finishBeans.get(position).getFlag().equals(1)) {
					if (finishBeans.get(position).getNstatus().equals(0)) {
						nStatusString = "已下单";
					} else if (finishBeans.get(position).getNstatus().equals(1)) {
						nStatusString = "已派工";
					} else if (finishBeans.get(position).getNstatus().equals(2)) {
						nStatusString = "已技师确认";
					} else if (finishBeans.get(position).getNstatus().equals(3)) {
						nStatusString = "已预约";
					} else if (finishBeans.get(position).getNstatus().equals(4)) {
						nStatusString = "已出发";
					} else if (finishBeans.get(position).getNstatus().equals(5)) {
						nStatusString = "已到达";
					} else if (finishBeans.get(position).getNstatus().equals(6)) {
						nStatusString = "已客户确认";
					} else if (finishBeans.get(position).getNstatus().equals(7)) {
						nStatusString = "已结算";
					} else if (finishBeans.get(position).getNstatus().equals(8)) {
						nStatusString = "已完工关单";
					} else if (finishBeans.get(position).getNstatus().equals(9)) {
						nStatusString = "已返程";
					}
				}
				finishHolder.status.setText(nStatusString);
			}
			break;
		default:
			break;
		}
		return convertView;
	}

	static class FinishHolder {
		ImageView ivIcon;
		TextView deviceName;
		TextView repairPlace;
		TextView repairDate;
		ImageView arrowRight;
		TextView status;
		View bgBottom;

	}

	static class UnFinishHolder {
		ImageView ivIcon;
		TextView deviceName;
		TextView repairPlace;
		TextView repairDate;
		ImageView arrowRight;
		TextView status;
		View bgBottom;
	}

	static class TitleHolder {
		RelativeLayout completed;
		TextView textComplete;
	}

	private static class AnimateFirstDisplayListener extends
			SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				// 是否第一次显示
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					// 图片淡入效果
					FadeInBitmapDisplayer.animate(imageView, 200);
					displayedImages.add(imageUri);
				}
			}
		}
	}

	// public void clearList(){
	// mList.clear();
	// }

	// public void changeStates(){
	// haveShowOnce = ! haveShowOnce;
	// }

}

// ViewHolder viewHolder = null;
// if (convertView == null) {
// convertView = layoutInflater.inflate(
// R.layout.list_item_repair_history, null);
// viewHolder = new ViewHolder();
// viewHolder.ivIcon = (ImageView) convertView
// .findViewById(R.id.iv_icon);
// viewHolder.deviceName = (TextView) convertView
// .findViewById(R.id.device_name);
// viewHolder.repairPlace = (TextView) convertView
// .findViewById(R.id.repair_place);
// viewHolder.repairDate = (TextView) convertView
// .findViewById(R.id.repair_date);
// viewHolder.arrowRight = (ImageView) convertView
// .findViewById(R.id.arrow_right);
// viewHolder.status = (TextView) convertView
// .findViewById(R.id.status);
// viewHolder.completed = (RelativeLayout) convertView
// .findViewById(R.id.completed);
// viewHolder.bgBottom = (View) convertView
// .findViewById(R.id.bg_bottom);
// convertView.setTag(viewHolder);
// } else {
// viewHolder = (ViewHolder) convertView.getTag();
// }
// if (null != mList) {
// int status = mList.get(position).getStatus(); // 判断列表条目的状态值
// switch (status) {
// case 6:
// case 7:
// viewHolder.bgBottom.setVisibility(View.GONE); // 已完成状态，底部背景栏隐藏
// listView = (ListView) parent;
// listView.setOnScrollListener(new PauseOnScrollListener(
// imageLoader, // 图片展示逻辑
// false, true));
// imageLoader.displayImage(mList.get(position).getImage(),
// viewHolder.ivIcon, options, animateFirstListener);
//
// viewHolder.deviceName.setText(mList.get(position)
// .getDeviceName()); // 设备名称
// viewHolder.repairPlace.setText(mList.get(position)
// .getRepairPlace()); // 别的手机可能有问题，需要加非空判断（维修地点）
// viewHolder.repairDate.setText(mList.get(position)
// .getRepairDate()); // 报修时间
// viewHolder.arrowRight.setVisibility(View.GONE); // 向右箭头，不展示数据
// viewHolder.repairPlace.setText(mList.get(position)
// .getRepairPlace()); // 维修地点
// viewHolder.status.setText(String.valueOf(mList.get(position)
// .getPrice()));
//
// if (!haveShowOnce) { // 是否已经展示过已完成标题
// haveShowOnce = true;
// viewHolder.completed.setVisibility(View.VISIBLE);
// } else {
// viewHolder.completed.setVisibility(View.GONE);
// }
// break;
// case 8:
// viewHolder.bgBottom.setVisibility(View.GONE); // 已完成状态，底部背景栏隐藏
// listView = (ListView) parent;
// listView.setOnScrollListener(new PauseOnScrollListener(
// imageLoader, // 图片展示逻辑
// false, true));
// imageLoader.displayImage(mList.get(position).getImage(),
// viewHolder.ivIcon, options, animateFirstListener);
//
// viewHolder.deviceName.setText(mList.get(position)
// .getDeviceName()); // 设备名称
// viewHolder.repairPlace.setText(mList.get(position)
// .getRepairPlace()); // 别的手机可能有问题，需要加非空判断（维修地点）
// viewHolder.repairDate.setText(mList.get(position)
// .getRepairDate()); // 报修时间
// viewHolder.arrowRight.setVisibility(View.GONE); // 向右箭头，不展示数据
// viewHolder.repairPlace.setText(mList.get(position)
// .getRepairPlace()); // 维修地点
// viewHolder.status.setText("已丢单");
// if (!haveShowOnce) { // 是否已经展示过已完成标题
// haveShowOnce = true;
// viewHolder.completed.setVisibility(View.VISIBLE);
// } else {
// viewHolder.completed.setVisibility(View.GONE);
// }
// break;
// case 1:
// case 2:
// case 3:
// case 4:
// case 5:
// viewHolder.completed.setVisibility(View.GONE);
// if (position == mList.size() - 1) {
// viewHolder.bgBottom.setVisibility(View.GONE);
// }
// viewHolder.bgBottom.setVisibility(View.VISIBLE);
//
// listView = (ListView) parent;
// listView.setOnScrollListener(new PauseOnScrollListener(
// imageLoader, // 图片展示逻辑
// false, true));
// imageLoader.displayImage(mList.get(position).getImage(),
// viewHolder.ivIcon, options, animateFirstListener);
//
// viewHolder.deviceName.setText(mList.get(position)
// .getDeviceName()); // 设备名称
// viewHolder.repairPlace.setText(mList.get(position)
// .getRepairPlace()); // 别的手机可能有问题，需要加非空判断（维修地点）
// viewHolder.repairDate.setText(mList.get(position)
// .getRepairDate()); // 报修时间
// viewHolder.arrowRight.setVisibility(View.GONE); // 向右箭头，不展示数据
// viewHolder.repairPlace.setText(mList.get(position)
// .getRepairPlace()); // 维修地点
// viewHolder.status.setText(str_status[status - 1]);// 维修状态
// break;
//
// default:
// break;
// }
// }
//

// ListView listView = (ListView) parent;
// listView.setOnScrollListener(new PauseOnScrollListener(imageLoader,
// false, true));
// imageLoader.displayImage(mList.get(position).getImage(),
// viewHolder.ivIcon, options, animateFirstListener);
// if (null != mList) {
// viewHolder.deviceName.setText(mList.get(position).getDeviceName());
// viewHolder.repairPlace
// .setText(mList.get(position).getRepairPlace()); // 别的手机可能有问题，需要加非空判断
// viewHolder.repairDate.setText(mList.get(position).getRepairDate());
// viewHolder.arrowRight.setVisibility(View.VISIBLE);
// viewHolder.repairPlace
// .setText(mList.get(position).getRepairPlace());
// // Constants.MyLog(mList.toString()+"2222222222222222222");
// }
