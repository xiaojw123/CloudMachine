package com.cloudmachine.ui.home.presenter;

import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;

import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.bean.OilSynBean;
import com.cloudmachine.ui.home.contract.OilSyncContract;
import com.cloudmachine.utils.ToastUtils;

import java.util.List;

/**
 * Created by xiaojw on 2018/5/9.
 */

public class OilSyncPresenter extends OilSyncContract.Presenter {
    private static final String UN_SYN_STATUS = "0";
    private static final String SYN_STATUS = "1";
    private static final String INVALID_STATUS = "2";

    @Override
    public void getOilSyncList(long deviceId) {
        mRxManage.add(mModel.getOilSyncList(deviceId).subscribe(new RxSubscriber<List<OilSynBean>>(mContext) {
            @Override
            protected void _onNext(List<OilSynBean> oilSynBeen) {
                if (oilSynBeen != null && oilSynBeen.size() > 0) {
                    for (OilSynBean bean : oilSynBeen) {
                        if (bean != null) {
                            Spanned textSp = null;
                            String synStatus = bean.getSynStatus();
                            if (!TextUtils.isEmpty(synStatus) && !UN_SYN_STATUS.equals(synStatus)) {
                                String synText;
                                if (SYN_STATUS.equals(synStatus)) {
                                    synText = bean.getSynTimeStr() + " <font color=\"#3cbca3\">" + bean.getSynStatusValue() + "</font>";
                                } else if (INVALID_STATUS.equals(synStatus)) {
                                    synText = bean.getSynTimeStr() + " <font color=\"#f36a6a\">" + bean.getSynStatusValue() + "</font>";
                                } else {
                                    synText = bean.getSynTimeStr() + " " + bean.getSynStatusValue();
                                }
                                if (!TextUtils.isEmpty(synText)) {
                                    textSp = Html.fromHtml(synText);
                                }
                            }
                            mView.updateOilSynItemView(bean.getOilPosition(), textSp);
                        }

                    }


                }

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(mContext, message);

            }
        }));


    }

    @Override
    public void syncOilLevel(final long deviceId, int oilPos, long memberId) {
        mView.showLoadingDailog();
        mRxManage.add(mModel.syncOilLevel(deviceId, oilPos, memberId).subscribe(new RxSubscriber<BaseRespose<String>>(mContext) {
            @Override
            protected void _onNext(BaseRespose<String> respose) {
                if (respose.success()) {
                    mView.showSyncSuccessDailog(respose.getMessage());
                    getOilSyncList(deviceId);
                } else {
                    mView.retrunSyncFailed(respose.getMessage());
                }
            }

            @Override
            protected void _onError(String message) {
                mView.retrunSyncFailed(message);
            }
        }));


    }

    @Override
    public void restOilLevel(final long deviceId) {
        mRxManage.add(mModel.restOilLevel(deviceId).subscribe(new RxSubscriber<BaseRespose<String>>(mContext) {
            @Override
            protected void _onNext(BaseRespose<String> respose) {
                if (respose.success()) {
                    ToastUtils.showToast(mContext, "恢复成功");
                    getOilSyncList(deviceId);
                } else {
                    ToastUtils.showToast(mContext, respose.getMessage()+", 恢复失败");
                }
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(mContext,  message+", 恢复失败");
            }
        }));

    }
}
