package com.cloudmachine.ui.home.presenter;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.base.baserx.RetryWithDelay;
import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.base.bean.PageBean;
import com.cloudmachine.bean.ArticleInfo;
import com.cloudmachine.bean.DeviceItem;
import com.cloudmachine.bean.McDeviceInfo;
import com.cloudmachine.bean.McDeviceLocation;
import com.cloudmachine.bean.TelBean;
import com.cloudmachine.chart.utils.AppLog;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.ui.home.activity.HomeActivity;
import com.cloudmachine.ui.home.contract.DeviceContract;
import com.cloudmachine.utils.CommonUtils;
import com.cloudmachine.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaojw on 2017/7/19.
 */

public class DevicePresenter extends DeviceContract.Prensenter {
    private int mPageNum, mToalPages;//ToalPages暂时限定为最多6页1200条数据，数据过多会出现OOM
    private List<DeviceItem> mToalItems;


    @Override
    public void getDeivceItems(final String memberId, int page) {
        mPageNum = page;
        mRxManage.add(mModel.getDeviceItems(memberId, page).subscribe(new RxSubscriber<BaseRespose<List<DeviceItem>>>(mContext) {
            @Override
            protected void _onNext(BaseRespose<List<DeviceItem>> br) {
                if (br != null) {
                    if (br.success()) {
                        PageBean bean = br.getPage();
                        List<DeviceItem> resutItems = br.result;
                        if (resutItems != null && resutItems.size() > 0) {
                            List<DeviceItem> dropItems = new ArrayList<>();
                            for (DeviceItem info : resutItems) {
                                if (info != null) {
                                    if (info.getLat() == 0 || info.getLng() == 0) {
                                        dropItems.add(info);
                                    }
                                }
                            }
                            if (dropItems.size() > 0) {
                                resutItems.removeAll(dropItems);
                            }
                        }
                        if (bean != null) {
                            if (mPageNum == 1) {
                                mToalItems = new ArrayList<>();
                                mToalPages = bean.totalPages;
                                if (mToalPages > 14) {
                                    mToalPages = 14;
                                }
                            }
                            if (resutItems != null && resutItems.size() > 0) {
                                mToalItems.addAll(resutItems);
                            }
                            if (mPageNum < mToalPages) {
                                mPageNum++;
                                getDeivceItems(memberId, mPageNum);
                            } else {
                                mView.updateDeviceItems(mToalItems, mPageNum);
                                if (!TextUtils.isEmpty(memberId)) {
                                    ((HomeActivity) mContext).requestPromotion(Long.parseLong(memberId));
                                }
                            }
                        }
                    }
                }
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(mContext, message);
                mView.updateDeviceItemError();
                if (!TextUtils.isEmpty(memberId)) {
                    ((HomeActivity) mContext).requestPromotion(Long.parseLong(memberId));
                }
            }
        }));

    }
    //                .map(new Func1<BaseRespose<List<DeviceItem>>, BaseRespose<List<DeviceItem>>>() {
//            @Override
//            public BaseRespose<List<DeviceItem>> call(BaseRespose<List<DeviceItem>> listBaseRespose) {

//                return resutItems;
//            }
//        });

//    @Override
//    public void getDevices(final long memberId, int type) {
//        mRxManage.add(mModel.getDevices(memberId, type).retryWhen(new RetryWithDelay(2, 1000)).subscribe(new RxSubscriber<List<McDeviceInfo>>(mContext, false) {
//            @Override
//            protected void _onNext(List<McDeviceInfo> mcDeviceInfos) {
//                mView.updateDevices(mcDeviceInfos);
//                ((HomeActivity) mContext).requestPromotion(memberId);
//            }
//
//            @Override
//            protected void _onError(String message) {
//                ((HomeActivity) mContext).requestPromotion(memberId);
//            }
//        }));
//
//    }
//
//    @Override
//    public void getDevices(int type) {
//        mRxManage.add(mModel.getDevices(type).retryWhen(new RetryWithDelay(2, 1000)).subscribe(new RxSubscriber<List<McDeviceInfo>>(mContext, false) {
//            @Override
//            protected void _onNext(List<McDeviceInfo> mcDeviceInfos) {
//                AppLog.print("getDevices onext__" + mcDeviceInfos);
//                if (mcDeviceInfos != null && mcDeviceInfos.size() > 0) {
//                    mView.updateDevices(mcDeviceInfos);
//                }
//            }
//
//            @Override
//            protected void _onError(String message) {
//                AppLog.print("getDevices error_message__" + message);
//
//            }
//        }));
//    }


    @Override
    public void getArticleList() {
        mRxManage.add(mModel.getArticles().subscribe(new RxSubscriber<List<ArticleInfo>>(mContext) {
            @Override
            protected void _onNext(List<ArticleInfo> articleInfos) {
                mView.updateArticles(articleInfos);
            }

            @Override
            protected void _onError(String message) {

            }
        }));

    }

    @Override
    public void getServiceTel() {
        mRxManage.add(mModel.getServiceTel().subscribe(new RxSubscriber<List<TelBean>>(mContext) {
            @Override
            protected void _onNext(List<TelBean> telBeanList) {
                if (telBeanList != null && telBeanList.size() > 0) {
                    String boxTel = null;
                    String repairTel = null;
                    for (TelBean bean : telBeanList) {
                        String key = bean.getKey();
                        String value = bean.getValue();
                        if ("BoxServiceTel".equals(key)) {
                            boxTel = value;
                        } else if ("RepairTel".equals(key)) {
                            repairTel = value;
                        }
                    }
                    mView.retrunGetServiceTel(boxTel, repairTel);
                }

            }

            @Override
            protected void _onError(String message) {

            }
        }));


    }

    @Override
    public void getDeviceNowData(final String deviceId, final View view) {

        if (UserHelper.isLogin(mContext)) {
            mRxManage.add(mModel.getDeviceNowData(deviceId, UserHelper.getMemberId(mContext)).subscribe(new RxSubscriber<McDeviceInfo>(mContext) {
                @Override
                protected void _onNext(McDeviceInfo info) {
                    updatePagerItemView(info, view, deviceId);

                }

                @Override
                protected void _onError(String message) {

                }
            }));
        } else {

            mRxManage.add(mModel.getDeviceNowData(deviceId).subscribe(new RxSubscriber<McDeviceInfo>(mContext) {
                @Override
                protected void _onNext(McDeviceInfo info) {
                    updatePagerItemView(info, view, deviceId);
                }

                @Override
                protected void _onError(String message) {

                }
            }));
        }


    }

    @Override
    public void getAllDeviceList(long memberId, final int page, String key) {
        mRxManage.add(mModel.getAllDeviceList(memberId, page, key).subscribe(new RxSubscriber<BaseRespose<List<McDeviceInfo>>>(mContext) {
            @Override
            protected void _onNext(BaseRespose<List<McDeviceInfo>> br) {
                mView.updateAllDeviceList(br, page);

            }

            @Override
            protected void _onError(String message) {
                mView.updateAllDeviceError(message);

            }
        }));

    }

    private void updatePagerItemView(McDeviceInfo info, View view, String id) {
        if (id != null && id.equals(String.valueOf(info.getId()))) {
            TextView locTv = (TextView) view.findViewById(R.id.device_info_loc);
            TextView statusTv = (TextView) view.findViewById(R.id.device_info_status);
            TextView oilTv = (TextView) view.findViewById(R.id.device_info_oil);
            TextView lenTv = (TextView) view.findViewById(R.id.device_info_timelen);
            switch (info.getWorkStatus()) {
                case 1:
                    statusTv.setVisibility(View.VISIBLE);
                    statusTv.setText("工作中");
                    break;
                case 2:
                    statusTv.setVisibility(View.VISIBLE);
                    statusTv.setText("在线");
                    break;

                default:
                    statusTv.setVisibility(View.GONE);
                    break;

            }
            McDeviceLocation location = info.getLocation();
            oilTv.setText(CommonUtils.formatOilValue(info.getOilLave()));
            lenTv.setText(CommonUtils.formatTimeLen(info.getWorkTime()));
            if (location != null) {
                locTv.setText(location.getPosition());
            }
        }
    }


}
