package com.cloudmachine.ui.repair.contract;

import com.cloudmachine.base.BaseModel;
import com.cloudmachine.base.BasePresenter;
import com.cloudmachine.base.BaseView;
import com.cloudmachine.bean.NewRepairInfo;
import com.google.gson.JsonObject;

import rx.Observable;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/3/22 下午2:45
 * 修改人：shixionglu
 * 修改时间：2017/3/22 下午2:45
 * 修改备注：
 */

public interface NewRepairContract {

    interface Model extends BaseModel {
        //发送上传图片的网络请求
       Observable<String> upLoadPhoto(String filename);
        Observable<JsonObject> getWarnMessage(long memeberId,String tel);



    }


    interface View extends BaseView {
        //操作对应的图片链接
        void returnUploadPhoto(String url);
        void returnGetWarnMessage(NewRepairInfo info,String message);
    }

    abstract static class Presenter extends BasePresenter<View, Model> {
        //报修上传指定图片请求
        public abstract void upLoadPhotoRequest(String filename);

        public abstract void getWarnMessage(long memeberId,String tel,final NewRepairInfo info);
    }
}
