package com.cloudmachine.ui.home.presenter;

import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.bean.VideoBean;
import com.cloudmachine.ui.home.contract.WorkVideoContract;

/**
 * Created by xiaojw on 2018/3/9.
 */

public class WorkVideoPresenter extends WorkVideoContract.Presenter {


    @Override
    public void videoUpload(long memberId, String deviceId, final String id) {
        mRxManage.add(mModel.videoUpload(memberId, deviceId, id).subscribe(new RxSubscriber<BaseRespose<String>>(mContext) {
            @Override
            protected void _onNext(BaseRespose<String> respose) {
                if (respose.success()) {
                    mView.returnVideoUploadSuccess();
                } else {
                    mView.returnVideoUploadError(respose.message);
                }
            }

            @Override
            protected void _onError(String message) {
                mView.returnVideoUploadError(message);

            }
        }));

    }

    @Override
    public void getVideoList(long memberId, String deviceId,String startTime,String endTime) {
        mRxManage.add(mModel.getVideoList(memberId, deviceId, startTime, endTime).subscribe(new RxSubscriber<VideoBean>(mContext) {
            @Override
            protected void _onNext(VideoBean videoBean) {
                if (videoBean != null) {
                    mView.returnGetVideoList(videoBean.getLiveUrl(), videoBean.getVideoList());
                } else {
                    mView.returnGetVideoListError(null);
                }
            }

            @Override
            protected void _onError(String message) {
                mView.returnGetVideoListError(message);
            }
        }));


    }
}
