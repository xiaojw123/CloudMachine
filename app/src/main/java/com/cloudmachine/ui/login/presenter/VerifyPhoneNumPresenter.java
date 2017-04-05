package com.cloudmachine.ui.login.presenter;

import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.recyclerbean.CheckNumBean;
import com.cloudmachine.struc.Member;
import com.cloudmachine.ui.login.contract.VerifyPhoneNumContract;
import com.cloudmachine.utils.ToastUtils;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/3/27 下午2:49
 * 修改人：shixionglu
 * 修改时间：2017/3/27 下午2:49
 * 修改备注：
 */

public class VerifyPhoneNumPresenter extends VerifyPhoneNumContract.Presenter {


    @Override
    public void wxBindMobile(long mobile, long type) {
        mRxManage.add(mModel.wxBindMobile(mobile, type)
                .compose(RxHelper.handleBooleanResult())
                .subscribe(new RxSubscriber<String>(mContext,false) {
                    @Override
                    protected void _onNext(String s) {
                        mView.returnWXBindMobile(s);
                    }

                    @Override
                    protected void _onError(String message) {
                        ToastUtils.error(message,true);
                    }
                })
        );
    }

    @Override
    public void checkNum(long mobile) {

        mRxManage.add(mModel.checkNum(mobile)
                .subscribe(new RxSubscriber<CheckNumBean>(mContext,false) {
                    @Override
                    protected void _onNext(CheckNumBean checkNumBean) {
                        mView.returnCheckNum(checkNumBean);
                    }

                    @Override
                    protected void _onError(String message) {
                        ToastUtils.error(message,true);
                    }
                })
        );
    }

    @Override
    public void bindWx(String unionId, String openId, String account, String code,
                       String inviteCode, String pwd, String nickname,
                       String headLogo, Integer type) {
        mRxManage.add(mModel.bindWx(unionId,openId,account,code,inviteCode,pwd,nickname,headLogo
        ,type).subscribe(new RxSubscriber<Member>(mContext,false) {
            @Override
            protected void _onNext(Member member) {
                mView.returnBindWx(member);
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.error(message,true);
            }
        }));
    }


}
