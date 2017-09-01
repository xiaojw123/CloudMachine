package com.cloudmachine.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cloudmachine.R;
import com.cloudmachine.adapter.holder.DeviceHoler;
import com.cloudmachine.bean.McDeviceInfo;

import java.util.List;

/**
 * Created by xiaojw on 2017/5/24.
 */

public class DeviceListAdpater extends BaseRecyclerAdapter<McDeviceInfo> {


    public DeviceListAdpater(Context context, List<McDeviceInfo> items) {
        super(context, items);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_device, parent, false);
        return new DeviceHoler(view);
    }





}
