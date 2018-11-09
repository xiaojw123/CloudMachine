package com.cloudmachine.ui.repair.model;

import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.bean.LarkDeviceDetail;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.ui.repair.contract.NewRepairContract;
import com.google.gson.JsonObject;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/3/22 下午3:12
 * 修改人：shixionglu
 * 修改时间：2017/3/22 下午3:12
 * 修改备注：
 */

public class NewRepairModel implements NewRepairContract.Model {


    @Override
    public Observable<JsonObject> getWarnMessage(String tel) {
        return Api.getDefault(HostType.HOST_LARK).getWarnMessage(tel).compose(RxHelper.<JsonObject>handleResult());
    }

    @Override
    public Observable<LarkDeviceDetail> getLocactionInfo(long deviceId) {
        return Api.getDefault(HostType.HOST_LARK).getLarkDeviceNowData(deviceId).compose(RxHelper.<LarkDeviceDetail>handleResult());
    }

}
