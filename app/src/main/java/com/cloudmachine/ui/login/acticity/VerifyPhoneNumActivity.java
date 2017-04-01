package com.cloudmachine.ui.login.acticity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.autolayout.widgets.RadiusButtonView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.ui.login.contract.VerifyPhoneNumContract;
import com.cloudmachine.ui.login.model.VerifyPhoneNumModel;
import com.cloudmachine.ui.login.presenter.VerifyPhoneNumPresenter;
import com.cloudmachine.utils.widgets.ClearEditTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

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


    @BindView(R.id.ll_back)
    LinearLayout      mLlBack;//返回按钮
    @BindView(R.id.phone_string)//手机号码
    ClearEditTextView mPhoneString;
    @BindView(R.id.validate_text)//
    TextView          mValidateText;
    @BindView(R.id.validate_layout)
    RelativeLayout    mValidateLayout;
    @BindView(R.id.validate_code)
    ClearEditTextView mValidateCode;
    @BindView(R.id.code_layout)
    RelativeLayout    mCodeLayout;
    @BindView(R.id.invitation_code)
    ClearEditTextView mInvitationCode;
    @BindView(R.id.pwd_string)
    ClearEditTextView mPwdString;
    @BindView(R.id.find_btn)
    RadiusButtonView  mFindBtn;
    @BindView(R.id.agreement_text)
    TextView          mAgreementText;
    @BindView(R.id.agreement_layout)
    LinearLayout      mAgreementLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phonenum);
        ButterKnife.bind(this);

    }

    @Override
    public void initPresenter() {

    }
}
