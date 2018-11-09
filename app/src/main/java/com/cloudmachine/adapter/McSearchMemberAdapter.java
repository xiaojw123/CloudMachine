package com.cloudmachine.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.activities.SearchActivity;
import com.cloudmachine.bean.SearchMemberItem;
import com.cloudmachine.listener.OnClickEffectiveListener;
import com.cloudmachine.bean.MemberInfo;
import com.cloudmachine.utils.widgets.RadiusButtonView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.List;

public class McSearchMemberAdapter extends BaseAdapter {
    private List<SearchMemberItem> dataResult;
    private Context context;
    private Handler handler;
    private LayoutInflater layoutInflater;
    private DisplayImageOptions displayImageOptions;
    private ImageLoadingListener commImageLoadingLis;
    private long deviceId;
    private int searchListType;

    public McSearchMemberAdapter(List<SearchMemberItem> dataResult, int searchListType, Context context, Handler mHandler, long deviceId) {
        this.context = context;
        this.handler = mHandler;
        this.deviceId = deviceId;
        this.dataResult = dataResult;
        this.searchListType = searchListType;
        this.layoutInflater = LayoutInflater.from(context);
        displayImageOptions = new DisplayImageOptions.Builder()
                .showImageOnFail(R.drawable.ic_default_head)
                .showImageForEmptyUri(R.drawable.ic_default_head)
                .showImageOnFail(R.drawable.ic_default_head).cacheInMemory(true)
                .cacheOnDisc(true).displayer(new RoundedBitmapDisplayer(5))
                .build();
    }

    @Override
    public int getCount() {
        return dataResult.size();
    }

    @Override
    public SearchMemberItem getItem(int position) {
        return dataResult.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * inner_classifyleft_listview classifyleft_img classifyleft_text
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(
                    R.layout.mc_search_listview, null);
            viewHolder = new ViewHolder();
            viewHolder.info_img = (ImageView) convertView
                    .findViewById(R.id.info_img);
            viewHolder.arrow = (ImageView) convertView.findViewById(R.id.arrow);
            viewHolder.title = (TextView) convertView
                    .findViewById(R.id.title);
            viewHolder.summary = (TextView) convertView
                    .findViewById(R.id.summary);
            viewHolder.arrow_add = (RadiusButtonView) convertView.findViewById(R.id.arrow_add);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (searchListType == 1) {
            viewHolder.arrow_add.setText("添加");
        }
        final SearchMemberItem dInfo = dataResult.get(position);
        String imgString = dInfo.getMiddlelogo();
        viewHolder.title.setText(dInfo.getNickName());
        viewHolder.summary.setText(dInfo.getMobile());
        ImageLoader.getInstance().displayImage(imgString, viewHolder.info_img,
                displayImageOptions, commImageLoadingLis);
        viewHolder.arrow.setVisibility(View.GONE);
        viewHolder.arrow_add.setVisibility(View.VISIBLE);
//				if(searchListType == 1){
//					viewHolder.arrow_add.setText("添加");
//				}else{
//					viewHolder.arrow_add.setText("移交");
//				}
        viewHolder.arrow_add.setOnClickListener(new llClick(position));
        return convertView;
    }

    class llClick extends OnClickEffectiveListener {
        private int position;

        llClick(int position) {
            this.position = position;
        }

        @Override
        public void onClickEffective(View v) {
            // TODO Auto-generated method stub
                /*MemberInfo memberInfo = dataResult.get(position);
                Intent intent_h = new Intent(context, PermissionActivity.class);
				intent_h.putExtra(Constants.P_DEVICEID, deviceId);
				intent_h.putExtra(Constants.P_PERMISSIONTYPE, 1);
				intent_h.putExtra(Constants.P_MERMBERINFO, memberInfo);
				context.startActivity(intent_h);*/
				/*intent_h.putExtra(Constants.P_MERMBERID, memberInfo.getMemberId());
				intent_h.putExtra(Constants.P_MERMBERNAME, memberInfo.getName());
				intent_h.putExtra(Constants.P_MERMBERROLE, memberInfo.getRole());
				intent_h.putExtra(Constants.P_MERMBERPERMISSION, memberInfo.getPermissName());*/
            SearchMemberItem memberInfo = dataResult.get(position);
            if (searchListType == 1) {
                ((SearchActivity) context).addMember(memberInfo);
            } else {
                ((SearchActivity) context).showAlertView(memberInfo);
            }
        }

    }

    static class ViewHolder {
        ImageView info_img;
        ImageView arrow;
        TextView title;
        TextView summary;
        RadiusButtonView arrow_add;
    }


}
