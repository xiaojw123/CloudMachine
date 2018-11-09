package com.cloudmachine.adapter.holder;

import android.view.View;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.bean.LarkDeviceDetail;
import com.cloudmachine.bean.LarkLocBean;
import com.cloudmachine.bean.McDeviceInfo;
import com.cloudmachine.bean.McDeviceLocation;
import com.cloudmachine.utils.CommonUtils;

import butterknife.BindView;

public class DeviceHoler extends BaseHolder<LarkDeviceDetail> {
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
    @BindView(R.id.item_device_hc)
    TextView itemDeviceHc;

    public DeviceHoler(View itemView) {
        super(itemView);
    }

    @Override
    public void initViewHolder(LarkDeviceDetail item) {
        if (item == null) {
            return;
        }
        itemDeviceName.setText(item.getDeviceName());
        if (CommonUtils.isHConfig(item.getSnId())){
            itemDeviceHc.setVisibility(View.VISIBLE);
        }else{
            itemDeviceHc.setVisibility(View.GONE);
        }
        LarkLocBean location = item.getLocation();
        if (location != null) {
            itemDeviceLoc.setText(location.getPosition());
        }

        switch (item.getWorkStatus()){
            case 1:
                itmeDeivesStatus.setText("工作中");
                itmeDeivesStatus.setVisibility(View.VISIBLE);
                break;
            case 2:
                itmeDeivesStatus.setText("在线");
                itmeDeivesStatus.setVisibility(View.VISIBLE);
                break;
            default:
                itmeDeivesStatus.setVisibility(View.GONE);
                break;
        }
        itemDeviceOil.setText(CommonUtils.formatOilValue(item.getOilLave()));
        itemDeviceWorktime.setText(CommonUtils.formatTimeLen(item.getWorkTime()));
    }

}