package com.cloudmachine.ui.home.presenter;

import android.text.TextUtils;

import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.bean.Member;
import com.cloudmachine.ui.home.contract.PurseContract;
import com.cloudmachine.ui.home.model.CouponBean;
import com.cloudmachine.utils.ToastUtils;

import java.util.Map;

/**
 * Created by xiaojw on 2017/12/6.
 */

public class PursePresenter extends PurseContract.Presenter {
    @Override
    public void getVerfyCode(final int type, String mobile) {
        mRxManage.add(mModel.getVerfyCode(mobile).subscribe(new RxSubscriber<BaseRespose>(mContext) {
            @Override
            protected void _onNext(BaseRespose baseRespose) {
                mView.returnToastError(baseRespose.getMessage());
                if (type != -1) {
                    mView.returnVerfyCode(type);
                }
            }

            @Override
            protected void _onError(String message) {

            }
        }));

    }

    @Override
    public void identifyCode(final int type, String mobile, String code) {
        mRxManage.add(mModel.identifyCode(mobile, code).subscribe(new RxSubscriber<BaseRespose>(mContext) {
            @Override
            protected void _onNext(BaseRespose baseRespose) {
                if (baseRespose.success()) {
                    mView.returnIdentifyCode(type);
                } else {
                    mView.returnToastError(baseRespose.getMessage());
                }
            }

            @Override
            protected void _onError(String message) {

            }
        }));
    }

    @Override
    public void unBind(long memberId, final int type) {
        mRxManage.add(mModel.unBind(memberId, type).subscribe(new RxSubscriber<BaseRespose>(mContext) {
            @Override
            protected void _onNext(BaseRespose baseRespose) {
                mView.returnUnBind(type, baseRespose.success());
            }

            @Override
            protected void _onError(String message) {

            }
        }));
    }

    @Override
    public void bindWxUser(Map<String, String> pm, final String wxNickName, final String openId) {
        mRxManage.add(mModel.bindWxUser(pm).subscribe(new RxSubscriber<BaseRespose>(mContext) {
            @Override
            protected void _onNext(BaseRespose baseRespose) {
                if (baseRespose.success()) {
                    String accountText;
                    if (!TextUtils.isEmpty(wxNickName)) {
                        accountText = wxNickName;
                    } else {
                        accountText = openId;
                    }
                    mView.updateBindWxUserView(accountText);
                }else{
                    ToastUtils.showToast(mContext,baseRespose.getMessage());
                }
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(mContext,message);

            }
        }));

    }

    @Override
    public void getAvaildCouponList(long memberid) {
        mRxManage.add(mModel.getAvaildCouponList(memberid).subscribe(new RxSubscriber<CouponBean>(mContext) {
            @Override
            protected void _onNext(CouponBean couponBean) {
                if (couponBean != null) {
                    mView.updateAvaildCouponSumNum(couponBean.getSumNum());
                }
            }

            @Override
            protected void _onError(String message) {

            }
        }));
    }

    @Override
    public void getMemberInfo(final long memberId) {

        mRxManage.add(mModel.getMemberInfo(memberId).subscribe(new RxSubscriber<Member>(mContext) {
            @Override
            protected void _onNext(Member member) {
                mView.returnMemberInfo(member);
            }

            @Override
            protected void _onError(String message) {
                mView.returnMemberInfo(null);

            }
        }));

    }


}
