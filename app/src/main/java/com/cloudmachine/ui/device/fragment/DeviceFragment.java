package com.cloudmachine.ui.device.fragment;

import com.cloudmachine.R;
import com.cloudmachine.base.BaseFragment;
import com.cloudmachine.ui.device.contract.DeviceContract;
import com.cloudmachine.ui.device.model.DeviceModel;
import com.cloudmachine.ui.device.presenter.DevicePresenter;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/3/17 下午10:29
 * 修改人：shixionglu
 * 修改时间：2017/3/17 下午10:29
 * 修改备注：
 */

public class DeviceFragment extends BaseFragment<DevicePresenter, DeviceModel> implements DeviceContract.View {

    @Override
    protected void initView() {

    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_device;
    }
}
