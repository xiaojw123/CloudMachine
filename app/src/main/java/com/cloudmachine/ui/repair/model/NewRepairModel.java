package com.cloudmachine.ui.repair.model;

import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.ui.repair.contract.NewRepairContract;

import java.io.File;

import okhttp3.MediaType;
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
    public Observable<String> upLoadPhoto(String filename) {

        File file = new File(filename);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
       /* MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("file", file.getName(), requestBody);
        MultipartBody body=builder.build();*/

        return Api.getDefault(HostType.CLOUDM_HOST).upLoadPhoto(requestBody)
                .compose(RxHelper.<String>handleResult());
    }
}
