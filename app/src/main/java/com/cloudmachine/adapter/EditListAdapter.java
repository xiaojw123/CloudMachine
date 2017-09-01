package com.cloudmachine.adapter;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.bean.EditListInfo;

import java.util.List;

public class EditListAdapter extends BaseAdapter {
    private List<EditListInfo> dataResult;
    private LayoutInflater layoutInflater;
    private String itemName;

    public EditListAdapter(Context context, List<EditListInfo> dataResult,
                           Handler myHandler) {
        this.dataResult = dataResult;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    @Override
    public int getCount() {
        return dataResult.size();
    }

    @Override
    public EditListInfo getItem(int position) {
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
            convertView = layoutInflater.inflate(R.layout.edit_list_item,
                    null);
            viewHolder = new ViewHolder();
            viewHolder.image_view = (ImageView) convertView
                    .findViewById(R.id.image_view);
            viewHolder.text_view = (TextView) convertView
                    .findViewById(R.id.text_view);
            viewHolder.text_view2 = (TextView) convertView
                    .findViewById(R.id.text_view2);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (dataResult != null && dataResult.size() > 0) {
            EditListInfo info = dataResult.get(position);
            String name = info.getName();
            if (!TextUtils.isEmpty(itemName) && itemName.equals(name)) {
                info.setIsClick(true);
            } else {
                info.setIsClick(false);
            }
            viewHolder.text_view.setText(name);
            if (!TextUtils.isEmpty(info.getStr2())) {
                viewHolder.text_view2.setVisibility(View.VISIBLE);
                viewHolder.text_view2.setText(info.getStr2());
            } else {
                viewHolder.text_view2.setVisibility(View.GONE);
            }

            if (info.getIsClick()) {
                viewHolder.image_view.setVisibility(View.VISIBLE);
            } else {
                viewHolder.image_view.setVisibility(View.GONE);
            }


        }

        return convertView;
    }


    static class ViewHolder {
        ImageView image_view;
        TextView text_view;
        TextView text_view2;
    }

}
