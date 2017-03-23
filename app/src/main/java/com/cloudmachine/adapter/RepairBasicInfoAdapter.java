package com.cloudmachine.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.struc.RepairBasicInfo;
import com.cloudmachine.struc.ScheduleBean;

public class RepairBasicInfoAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater layoutInflater;
	private RepairBasicInfo repairBasicInfo;
	private ArrayList<ScheduleBean> scheduleBean;
	public static final int TYPE_1 = 0;
	public static final int TYPE_2 = 1;

	public RepairBasicInfoAdapter(Context mContext,
			RepairBasicInfo repairBasicInfo) {
		this.mContext = mContext;
		layoutInflater = LayoutInflater.from(mContext);
		this.repairBasicInfo = repairBasicInfo;
		scheduleBean = this.repairBasicInfo.getSchedule();

	}

	public void setData(RepairBasicInfo basicInfo) {
		this.repairBasicInfo = basicInfo;
		this.scheduleBean = repairBasicInfo.getSchedule();
	}

	public RepairBasicInfo getData() {
		return this.repairBasicInfo;
	}

	@Override
	public int getCount() {
		// if (null != repairBasicInfo.getSchedule() && getBasicInfoSize() != 0
		// ) {
		// return progressListBeans.size()+getBasicInfoSize();
		// }
		// return 0;
		if (!TextUtils.isEmpty(repairBasicInfo.getAddress())) {
			return scheduleBean.size() + 7;
		} else if(null != scheduleBean){
			if (scheduleBean.size() == 0) {
				return 0;
			} else {
				return scheduleBean.size() + 6;
			}
		}else{
			return 0;
		}
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getItemViewType(int position) {
		if (position <= scheduleBean.size() - 1) {
			return TYPE_1;
		} else {
			return TYPE_2;
		}
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Type1Holder type1Holder = null;
		Type2Holder type2Holder = null;
		switch (getItemViewType(position)) {
		case TYPE_1:
			if (convertView == null) {
				type1Holder = new Type1Holder();
				convertView = layoutInflater.inflate(
						R.layout.list_item_schedual, null);
				type1Holder.rl_header = (RelativeLayout) convertView
						.findViewById(R.id.rl_header);
				type1Holder.line_above = (View) convertView
						.findViewById(R.id.line_above);
				type1Holder.line_below = (View) convertView
						.findViewById(R.id.line_below);
				type1Holder.iv_cilcle_select = (ImageView) convertView
						.findViewById(R.id.iv_cilcle_select);
				type1Holder.iv_circle_unselect = (ImageView) convertView
						.findViewById(R.id.iv_circle_unselect);
				type1Holder.tv_above = (TextView) convertView
						.findViewById(R.id.tv_above);
				type1Holder.tv_bottom = (TextView) convertView
						.findViewById(R.id.tv_bottom);
				type1Holder.line_bottomView = convertView
						.findViewById(R.id.line_bottom);

				if (position == 0) {
					type1Holder.rl_header.setVisibility(View.VISIBLE);
					type1Holder.line_above.setVisibility(View.GONE);
					type1Holder.line_below.setVisibility(View.VISIBLE);
					type1Holder.line_above.setBackgroundColor(mContext
							.getResources().getColor(R.color.current_text));
					type1Holder.line_below.setBackgroundColor(mContext
							.getResources().getColor(R.color.current_text));
					type1Holder.tv_above.setTextColor(mContext.getResources()
							.getColor(R.color.current_text));
					type1Holder.tv_bottom.setTextColor(mContext.getResources()
							.getColor(R.color.current_text));
					type1Holder.iv_cilcle_select.setVisibility(View.VISIBLE);
					type1Holder.iv_circle_unselect
							.setVisibility(View.INVISIBLE);
				} else {
					type1Holder.rl_header.setVisibility(View.GONE);
					type1Holder.line_above.setVisibility(View.VISIBLE);
					type1Holder.line_above.setBackgroundColor(mContext
							.getResources().getColor(R.color.history_text));
					type1Holder.line_below.setBackgroundColor(mContext
							.getResources().getColor(R.color.history_text));
					type1Holder.tv_above.setTextColor(mContext.getResources()
							.getColor(R.color.history_text));
					type1Holder.tv_bottom.setTextColor(mContext.getResources()
							.getColor(R.color.history_text));
					type1Holder.iv_cilcle_select.setVisibility(View.INVISIBLE);
					type1Holder.iv_circle_unselect.setVisibility(View.VISIBLE);
				}
				if (position == scheduleBean.size() - 1) {
					type1Holder.line_bottomView.setVisibility(View.VISIBLE);
					// type1Holder.line_bottomView.setBackgroundColor(mContext.getResources().getColor(R.color.line_bottom_color));
				} else {
					type1Holder.line_bottomView.setVisibility(View.GONE);
				}
				convertView.setTag(type1Holder);

			} else {
				type1Holder = (Type1Holder) convertView.getTag();
			}
			type1Holder.tv_above.setText(scheduleBean.get(position).getDesc());
			type1Holder.tv_bottom.setText(scheduleBean.get(position)
					.getCreateTime());
			break;
		case TYPE_2:
			if (convertView == null) {
				type2Holder = new Type2Holder();
				convertView = layoutInflater.inflate(
						R.layout.list_item_repair_basic_info, null);
				type2Holder.tv_desc = (TextView) convertView
						.findViewById(R.id.tv_desc);
				type2Holder.tv_tvtitle = (TextView) convertView
						.findViewById(R.id.tv_title);
				type2Holder.v_bottom = convertView.findViewById(R.id.v_bottom);
				convertView.setTag(type2Holder);
			} else {
				type2Holder = (Type2Holder) convertView.getTag();
			}
			if (position == scheduleBean.size()) {
				if (null != repairBasicInfo.getContact()) {
					type2Holder.tv_tvtitle.setText("联系人");
					type2Holder.tv_desc
							.setText(repairBasicInfo.getContact());
				}
			}
			if (position == scheduleBean.size() + 1) {
				if (null != repairBasicInfo.getMobile()) {
					type2Holder.tv_tvtitle.setText("联系方式");
					type2Holder.tv_desc.setText(repairBasicInfo.getMobile());
				}
			}
			if (position == scheduleBean.size() + 2) {
				if (null != repairBasicInfo.getType()) {
					type2Holder.tv_tvtitle.setText("产品");
					type2Holder.tv_desc.setText(repairBasicInfo.getType());
				}
			}
			if (position == scheduleBean.size() + 3) {
				if (null != repairBasicInfo.getBrand()) {
					type2Holder.tv_tvtitle.setText("品牌");
					type2Holder.tv_desc.setText(repairBasicInfo.getBrand());
				}
			}
			if (position == scheduleBean.size() + 4) {
				if (null != repairBasicInfo.getModel()) {
					type2Holder.tv_tvtitle.setText("机型");
					type2Holder.tv_desc.setText(repairBasicInfo.getModel());
					type2Holder.v_bottom.setVisibility(View.VISIBLE);
				}
			} else {
				type2Holder.v_bottom.setVisibility(View.GONE);
			}
			if (position == scheduleBean.size() + 5) {
				if (!TextUtils.isEmpty(repairBasicInfo.getAddress())) {
					type2Holder.tv_tvtitle.setText("位置");
					type2Holder.tv_desc.setText(repairBasicInfo.getAddress());
					
				}else {
					type2Holder.tv_tvtitle.setText("故障描述");
					type2Holder.tv_desc.setText(repairBasicInfo.getDesc());
				}
			}
			if (!TextUtils.isEmpty(repairBasicInfo.getAddress()) && position == scheduleBean.size() + 6) {
				if (null != repairBasicInfo.getDesc()) {
					type2Holder.tv_tvtitle.setText("故障描述");
					type2Holder.tv_desc.setText(repairBasicInfo.getDesc());
				}
			}

			break;

		default:
			break;
		}
		return convertView;
	}

	static class Type1Holder {
		RelativeLayout rl_header;
		View line_above;
		View line_below;
		ImageView iv_circle_unselect;
		ImageView iv_cilcle_select;
		TextView tv_above;
		TextView tv_bottom;
		View line_bottomView;
	}

	static class Type2Holder {
		TextView tv_tvtitle;
		TextView tv_desc;
		View v_bottom;
	}

	// public int getBasicInfoSize() {
	// int count = 0;
	// if (null != recordBasicInfo.getDeviceName()) {
	// count++;
	// }
	// if (null != recordBasicInfo.getType()) {
	// count++;
	// }
	// if (null != recordBasicInfo.getBrand()) {
	// count++;
	// }
	// if (null != recordBasicInfo.getModel()) {
	// count++;
	// }
	// if (null != recordBasicInfo.getRepairPlace()) {
	// count++;
	// }
	// if (recordBasicInfo.getMobile() != 0) {
	// count++;
	// }
	// if (null != recordBasicInfo.getDescription()) {
	// count++;
	// }
	// return count;
	// }

}

// ViewHolder holder = null;
// if (convertView == null) {
// convertView = layoutInflater.inflate(
// R.layout.list_item_repair_basic_info, null);
// holder = new ViewHolder();
// holder.bgTop = convertView.findViewById(R.id.bg_top);
// holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
// holder.tvDesc = (TextView) convertView.findViewById(R.id.tv_desc);
// holder.ivDesc = (ImageView) convertView.findViewById(R.id.iv_desc);
// if(position == 0 || position == 5){
// holder.bgTop.setVisibility(View.VISIBLE);
// }else {
// holder.bgTop.setVisibility(View.GONE);
// }
// if(position == 5 || position == 6){
// holder.ivDesc.setVisibility(View.GONE);
// }else {
// holder.ivDesc.setVisibility(View.VISIBLE);
// }
// convertView.setTag(holder);
// } else {
// holder = (ViewHolder) convertView.getTag();
// }
// holder.tvTitle.setText(str_repair_desc[position]);
// holder.tvDesc.setText(repairDesc.get(position));
// return convertView;

//
// static class ViewHolder {
// View bgTop;
// TextView tvTitle;
// TextView tvDesc;
// ImageView ivDesc;
// }