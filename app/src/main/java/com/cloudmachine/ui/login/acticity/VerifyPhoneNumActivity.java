package com.cloudmachine.ui.login.acticity;

import android.os.Bundle;

import com.cloudmachine.R;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.ui.login.contract.VerifyPhoneNumContract;
import com.cloudmachine.ui.login.model.VerifyPhoneNumModel;
import com.cloudmachine.ui.login.presenter.VerifyPhoneNumPresenter;

/**
 * 项目名称：CloudMachine
 * 类描述：验证手机号页面
 * 创建人：shixionglu
 * 创建时间：2017/3/27 下午2:39
 * 修改人：shixionglu
 * 修改时间：2017/3/27 下午2:39
 * 修改备注：
 */

public class VerifyPhoneNumActivity extends BaseAutoLayoutActivity<VerifyPhoneNumPresenter, VerifyPhoneNumModel>
        implements VerifyPhoneNumContract.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phonenum);
    }

    @Override
    public void initPresenter() {

    }
}
