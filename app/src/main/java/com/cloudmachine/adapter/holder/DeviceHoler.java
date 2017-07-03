package com.cloudmachine.adapter.holder;

import android.view.View;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.struc.McDeviceInfo;
import com.cloudmachine.struc.McDeviceLocation;
import com.cloudmachine.utils.mpchart.ValueFormatUtil;

import butterknife.BindView;

public class DeviceHoler extends BaseHolder<McDeviceInfo> {
    @BindView(R.id.item_device_name)
    TextView itemDeviceName;
    @BindView(R.id.item_device_oil)
    TextView itemDeviceOil;
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
        if (item == null) {
            return;
        }
        itemDeviceName.setText(item.getName());
        McDeviceLocation location = item.getLocation();
        if (location != null) {
            itemDeviceLoc.setText(location.getPosition());
        }
        if (item.getWorkStatus() == 1) {
            itmeDeivesStatus.setText("工作中");
        } else {
            itmeDeivesStatus.setText("");
        }
        itemDeviceOil.setText(item.getOilLave() + "%");
        itemDeviceWorktime.setText(ValueFormatUtil.getWorkTime(item.getWorkTime(), "0时"));
    }

}