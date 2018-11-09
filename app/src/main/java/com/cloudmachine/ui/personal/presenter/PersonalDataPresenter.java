package com.cloudmachine.ui.personal.presenter;

import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.ui.personal.contract.PersonalDataContract;
import com.cloudmachine.utils.ToastUtils;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/4/7 下午4:36
 * 修改人：shixionglu
 * 修改时间：2017/4/7 下午4:36
 * 修改备注：
 */

public class PersonalDataPresenter extends PersonalDataContract.Presenter{

    @Override
    public void modifyMemberInfo(String nickName, String logo) {
        mRxManage.add(mModel.modifyMemberInfo(nickName,logo)
                .subscribe(new RxSubscriber<String>(mContext,false) {
                    @Override
                    protected void _onNext(String s) {
                        mView.returnModifyLogo();
                    }

                    @Override
                    protected void _onError(String message) {
                        ToastUtils.error(message,true);
                    }
                }));
    }
}
