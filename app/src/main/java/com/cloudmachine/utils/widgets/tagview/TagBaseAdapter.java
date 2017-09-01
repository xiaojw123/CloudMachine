package com.cloudmachine.utils.widgets.tagview;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.cloudmachine.autolayout.utils.AutoUtils;
import com.cloudmachine.R;
import com.cloudmachine.bean.DisadvantageBean;

import java.util.List;

/**
 * 
 */
public class TagBaseAdapter extends BaseAdapter {

    private Context mContext;
    //private List<String> mList;
    private List<DisadvantageBean> tagBeans;
    
    
//    public TagBaseAdapter(Context context, List<String> list) {
//        mContext = context;
//        mList = list;
//    }

    public TagBaseAdapter(Context mContext,
			List<DisadvantageBean> tagBeans) {
    	this.mContext = mContext;
    	this.tagBeans = tagBeans;
    }

	

	@Override
    public int getCount() {
        return tagBeans.size();
    }

    @Override
    public DisadvantageBean getItem(int position) {
        return tagBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.tagview, null);
            holder = new ViewHolder();
            holder.tagBtn = (Button) convertView.findViewById(R.id.tag_btn);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        DisadvantageBean disadvantageBean = getItem(position);
        String text = disadvantageBean.getCode_NAME();
        holder.tagBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX,AutoUtils.getPercentHeightSizeBigger((int)mContext.getResources().getDimension(R.dimen.public_textsize5)));
        if (disadvantageBean.isChecked()) {
			holder.tagBtn.setText(text);
			holder.tagBtn.setBackground(mContext.getResources().getDrawable(R.drawable.tag_checked));
			holder.tagBtn.setTextColor(mContext.getResources().getColor(R.color.cor2));
		}else {
			holder.tagBtn.setText(text);
			holder.tagBtn.setBackground(mContext.getResources().getDrawable(R.drawable.tag_unchecked));
			holder.tagBtn.setTextColor(mContext.getResources().getColor(R.color.cor9));

		}

        return convertView;
    }

    static class ViewHolder {
        Button tagBtn;
    }
}


//final String text = getItem(position);
//holder.tagBtn.setText(text);
//holder.tagBtn.setTextColor(mContext.getResources().getColor(R.color.blue));
////holder.tagBtn.setBackgroundResource(mContext.getResources().getColor(R.color.black));
//holder.tagBtn.setBackgroundColor(mContext.getResources().getColor(R.color.black));
