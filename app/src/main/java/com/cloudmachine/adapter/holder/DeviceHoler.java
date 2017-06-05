package com.cloudmachine.adapter.holder;

import android.view.View;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.struc.McDeviceInfo;
import com.cloudmachine.struc.McDeviceLocation;

import butterknife.BindView;

public class DeviceHoler extends BaseHolder<McDeviceInfo>{
    @BindView(R.id.item_device_name)
    TextView itemDeviceName;
    @BindView(R.id.item_device_fuelcus)
    TextView itemDeviceFuelcus;
    @BindView(R.id.item_device_worktime)
    TextView itemDeviceWorktime;
    @BindView(R.id.itme_deives_status)
    TextView itmeDeivesStatus;
    @BindView(R.id.item_device_loc)
    TextView itemDeviceLoc;

    public DeviceHoler(View itemView) {
        super(itemView);
    }

    @Override
    public void initViewHolder(McDeviceInfo item) {
        itemDeviceName.setText(item.getName());
        McDeviceLocation location = item.getLocation();
        if (location != null) {
            itemDeviceLoc.setText(location.getPosition());
        }

    }

}