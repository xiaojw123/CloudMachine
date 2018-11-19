package com.cloudmachine.ui.home.presenter;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.media.Image;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.autolayout.utils.L;
import com.cloudmachine.base.baserx.RetryWithDelay;
import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.base.bean.PageBean;
import com.cloudmachine.bean.LarkDeviceDetail;
import com.cloudmachine.bean.LarkDeviceInfo;
import com.cloudmachine.bean.LarkLocBean;
import com.cloudmachine.bean.McDeviceInfo;
import com.cloudmachine.bean.RedPacket;
import com.cloudmachine.bean.TelBean;
import com.cloudmachine.chart.utils.AppLog;
import com.cloudmachine.ui.home.activity.HomeActivity;
import com.cloudmachine.ui.home.contract.DeviceContract;
import com.cloudmachine.utils.CommonUtils;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

/**
 * Created by xiaojw on 2017/7/19.
 */

public class DevicePresenter extends DeviceContract.Prensenter {
    private List<LarkDeviceInfo> mAllDeviceList = new ArrayList<>();

    private ObjectAnimator mRoateAnim;

    @Override
    public void getRedPacketConfig() {
        cancelRedPacketAnim();
        mRxManage.add(mModel.getReadPacketConfig().subscribe(new RxSubscriber<RedPacket>(mContext) {
            @Override
            protected void _onNext(RedPacket redPacket) {
                if (redPacket != null) {
                    if (redPacket.getIsOpenActivity() == 1) {
                        boolean isNoOpen = false;
                        List<RedPacket.RecordVos> recordVosList = redPacket.getRedPacketRecordVOS();
                        if (recordVosList != null && recordVosList.size() > 0) {
                            for (RedPacket.RecordVos vos : recordVosList) {
                                if (vos.getRedPacketStatus() == 0) {
                                    isNoOpen = true;
                                }
                            }
                        }
                        mView.retrunPacketConfig(redPacket.getImageUrl(), redPacket.getJumpUrl(), isNoOpen);
                        return;
                    }
                }
                mView.hidenPacket();
            }

            @Override
            protected void _onError(String message) {
                mView.hidenPacket();
            }
        }));

    }

    @Override
    public void getAllDeviceList(int page, String deviceName) {
        mRxManage.add(mModel.getAllDeviceList(page, deviceName).subscribe(new RxSubscriber<BaseRespose<List<LarkDeviceDetail>>>(mContext) {
            @Override
            protected void _onNext(BaseRespose<List<LarkDeviceDetail>> br) {
                if (br.isSuccess()) {
                    PageBean page = br.getPage();
                    if (page != null) {
                        mView.returnAllDeviceList(br.getResult(), page.first, page.last, page.totalElements);
                    } else {
                        _onError(null);
                    }
                } else {
                    _onError(br.getMessage());
                }
            }

            @Override
            protected void _onError(String message) {
                if (TextUtils.isEmpty(message)) {
                    message = "未知异常";
                }
                ToastUtils.showToast(mContext, message);
                mView.retrunAllDeviceError();

            }
        }));
    }

    @Override
    public void updatePagerItem(long deviceId, final View itemView) {
        mRxManage.add(mModel.getDeviceNowData(deviceId).subscribe(new RxSubscriber<LarkDeviceDetail>(mContext) {
            @Override
            protected void _onNext(LarkDeviceDetail detail) {
                if (detail != null) {
                    TextView locTv = (TextView) itemView.findViewById(R.id.device_info_loc);
                    TextView statusTv = (TextView) itemView.findViewById(R.id.device_info_status);
                    TextView oilTv = (TextView) itemView.findViewById(R.id.device_info_oil);
                    TextView lenTv = (TextView) itemView.findViewById(R.id.device_info_timelen);
//                    final ImageView oilModelImg = (ImageView) itemView.findViewById(R.id.device_info_oil_model);
//                    if (detail.isSimpleBox()) {
//                        oilTv.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                oilModelImg.setVisibility(View.VISIBLE);
//                            }
//                        });
//                    }
                    switch (detail.getWorkStatus()) {
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
                    oilTv.setText(CommonUtils.formatOilValue(detail.getOilLave()));
                    lenTv.setText(CommonUtils.formatTimeLen(detail.getWorkTime()));
                    LarkLocBean loc = detail.getLocation();
                    if (loc != null) {
                        locTv.setText(loc.getPosition());
                    }
                }
            }

            @Override
            protected void _onError(String message) {

            }
        }));


    }

    @Override
    public void getDeviceMapList(final int pageN) {
        mRxManage.add(mModel.getDeviceMapList(pageN).subscribe(new RxSubscriber<BaseRespose<List<LarkDeviceInfo>>>(mContext) {
            @Override
            protected void _onNext(BaseRespose<List<LarkDeviceInfo>> br) {
                if (br != null) {
                    if (br.isSuccess()) {
                        PageBean page = br.getPage();
                        List<LarkDeviceInfo> infoList = br.getResult();
                        List<LarkDeviceInfo> dropItems = new ArrayList<LarkDeviceInfo>();
                        for (LarkDeviceInfo info : infoList) {
                            if (info.getLat() == 0 || info.getLng() == 0) {
                                dropItems.add(info);
                            }
                        }
                        if (dropItems.size() > 0) {
                            infoList.removeAll(dropItems);
                        }
                        if (pageN == 1) {
                            mAllDeviceList.clear();
                        }
                        mAllDeviceList.addAll(infoList);
                        if (page.last) {
                            mView.updateLarkDevices(mAllDeviceList);
                            ((HomeActivity) mContext).obtainSystemAd(HomeActivity.AD_WINDOW);
                        } else {
                            getDeviceMapList(pageN + 1);
                        }
                    }
                }
            }

            @Override
            protected void _onError(String message) {
                ((HomeActivity) mContext).obtainSystemAd(HomeActivity.AD_WINDOW);
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

    public void registerRxEvent() {
        mRxManage.on(Constants.UPDATE_DEVICE_LIST, new Action1<String>() {
            @Override
            public void call(String o) {
                getDeviceMapList(1);
            }
        });
    }

    public void startRedPacketAnim(final ImageView target) {
        if (mRoateAnim == null) {
            mRoateAnim = ObjectAnimator.ofFloat(target, "rotation", -30f, 30f);
            mRoateAnim.setStartDelay(1000);
            mRoateAnim.setDuration(200);
            mRoateAnim.setRepeatCount(3);
            mRoateAnim.setRepeatMode(ValueAnimator.REVERSE);
        }
        mRoateAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                target.setRotation(0);
                animation.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                target.setRotation(0);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mRoateAnim.start();
    }

    public void cancelRedPacketAnim() {
        if (mRoateAnim != null) {
            mRoateAnim.removeAllListeners();
            mRoateAnim.cancel();
            Object object = mRoateAnim.getTarget();
            if (object != null && object instanceof ImageView) {
                ((ImageView) object).setRotation(0);
            }
        }
    }


}
