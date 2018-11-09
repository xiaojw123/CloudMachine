package com.cloudmachine.ui.home.contract;

import com.cloudmachine.base.BaseModel;
import com.cloudmachine.base.BasePresenter;
import com.cloudmachine.base.BaseView;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.bean.VideoBean;

import java.util.List;

import rx.Observable;

/**
 * Created by xiaojw on 2018/3/9.
 */

public interface WorkVideoContract {


    interface Model extends BaseModel {
        Observable<BaseRespose<String>> videoUpload(String deviceId, String id);

        Observable<VideoBean> getVideoList(String deviceId,String startTime,String endTime);

    }

    interface View extends BaseView {
        void returnVideoUploadSuccess();
        void returnVideoUploadError(String message);
        void returnGetVideoList(String liveUrl, List<VideoBean.VideoListBean> videoList);
        void returnGetVideoListError(String message);
    }

    abstract class Presenter extends BasePresenter<WorkVideoContract.View,WorkVideoContract.Model> {
        public abstract void videoUpload(String deviceId, String id);

        public abstract void getVideoList(String deviceId,String startTime,String endTime);

    }


}
