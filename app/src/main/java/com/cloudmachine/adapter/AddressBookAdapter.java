package com.cloudmachine.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.adapter.holder.BaseHolder;
import com.cloudmachine.bean.AddressBookItem;
import com.cloudmachine.chart.utils.AppLog;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xiaojw on 2018/6/27.
 */

public class AddressBookAdapter extends BaseRecyclerAdapter<AddressBookItem> implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {

    public AddressBookAdapter(Context context, List<AddressBookItem> items) {
        super(context, items);
        setHasStableIds(true);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_address, parent, false);
        return new AddressBookHolder(view);
    }

    @Override
    public long getHeaderId(int position) {
        AppLog.print("getHeaderID___pos___"+position);
        if (mItems.size() > position) {
            AddressBookItem item = mItems.get(position);
            String letter = item.getFirstLetter();
            AppLog.print("headerpos__"+position+"___leter__"+letter+"_id__"+(int)letter.charAt(0));
            return (int)letter.charAt(0);
        }
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_address_header, parent, false);
        return new AHeaderHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        AppLog.print("onBindHeaderViewHolder___pos__" + position);
        if (position < 0) {
            return;
        }
        if (mItems.size() > position) {
            TextView wordTv = (TextView) holder.itemView.findViewById(R.id.item_header_word);
            AddressBookItem item = mItems.get(position);
            wordTv.setText(item.getFirstLetter());
        }
    }

    public class AHeaderHolder extends RecyclerView.ViewHolder {


        public AHeaderHolder(View itemView) {
            super(itemView);
        }

    }

    public class AddressBookHolder extends BaseHolder<AddressBookItem> {
        @BindView(R.id.address_name_tv)
        TextView nameTv;
        @BindView(R.id.address_mobile_tv)
        TextView mobileTv;

        public AddressBookHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void initViewHolder(AddressBookItem item) {
            nameTv.setText(item.getName());
            List<String> mobiles = item.getMobile();
            String mobileStr = "";
            if (mobiles != null && mobiles.size() > 0) {

                for (String mobile : mobiles) {
                    if (mobileStr.length()>0){
                        mobileStr += "<font color=\"#e5e5e5\"> | </font>";
                    }
                    mobileStr+=mobile;
                }
            }
            mobileTv.setText(Html.fromHtml(mobileStr));
        }
    }


}
