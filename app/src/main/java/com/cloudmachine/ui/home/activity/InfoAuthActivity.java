package com.cloudmachine.ui.home.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.model.Text;
import com.cloudmachine.R;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.bean.AuthBean;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.ui.homepage.activity.QuestionCommunityActivity;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.DensityUtil;
import com.cloudmachine.utils.ToastUtils;
import com.cloudmachine.widget.CommonTitleView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InfoAuthActivity extends BaseAutoLayoutActivity implements View.OnClickListener {


    @BindView(R.id.info_auth_name_tv)
    TextView infoAuthNameTv;
    @BindView(R.id.info_auth_idcard_tv)
    TextView infoAuthIdcardTv;
    @BindView(R.id.info_auth_idcard_status)
    TextView idcardStatus;
    @BindView(R.id.info_auth_idcard_container)
    FrameLayout infoAuthIdcardContainer;
    @BindView(R.id.info_auth_operator_status)
    TextView operatorStatus;
    @BindView(R.id.info_auth_operator_container)
    FrameLayout infoAuthOperatorContainer;
    @BindView(R.id.info_auth_bank_status)
    TextView infoAuthBankStatus;
    @BindView(R.id.info_auth_bank_cotainer)
    FrameLayout infoAuthBankCotainer;
    @BindView(R.id.engineer_contract_status)
    TextView engineerContractStatus;
    @BindView(R.id.info_auth_engineer_contract)
    FrameLayout infoAuthEngineerContract;
    @BindView(R.id.personal_income_status)
    TextView personalIncomeStatus;
    @BindView(R.id.info_auth_personal_income)
    FrameLayout infoAuthPersonalIncome;
    @BindView(R.id.device_owner_status)
    TextView deviceOwnerStatus;
    @BindView(R.id.info_auth_device_owner)
    FrameLayout infoAuthDeviceOwner;
    @BindView(R.id.bankaccount_model_container)
    LinearLayout modelContainer;
    @BindView(R.id.bankaccount_container)
    FrameLayout bankAccountContainer;
    @BindView(R.id.info_auth_relation_container)
    FrameLayout relationContainer;
    @BindView(R.id.info_auth_relation_status)
    TextView relationStatus;
    String uniqueNo;
    @BindView(R.id.info_auth_ctv)
    CommonTitleView titleView;
    boolean isAuthAll;
    String realName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_auth);
        ButterKnife.bind(this);
        infoAuthIdcardContainer.setEnabled(false);
        relationContainer.setEnabled(false);
        infoAuthOperatorContainer.setEnabled(false);
        infoAuthBankCotainer.setEnabled(false);
        infoAuthEngineerContract.setEnabled(false);
        infoAuthPersonalIncome.setEnabled(false);
        infoAuthDeviceOwner.setEnabled(false);
        titleView.setLeftClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    private void initView() {
        mRxManager.add(Api.getDefault(HostType.HOST_CLOUDM).getAuthInfo(UserHelper.getMemberId(this)).compose(RxHelper.<AuthBean>handleResult()).subscribe(new RxSubscriber<AuthBean>(mContext) {
            @Override
            protected void _onNext(AuthBean authBean) {
                uniqueNo = authBean.getUserUniqueNo();
                realName = authBean.getRealName();
                infoAuthNameTv.setText(realName);
                infoAuthIdcardTv.setText(authBean.getIdCardNo());
                int status0 = authBean.getIdentityCheckStatus();
                int status1 = authBean.getRelationAuthStatus();
                int status2 = authBean.getOperatorAuthorizedStatus();
                int status3 = authBean.getCardFourElementAuthStatus();
                int status4 = authBean.getLicenceCheckStatus();
                int status5 = authBean.getIncomeCheckStatus();
                int status6 = authBean.getMachineCheckStatus();
                setAuthStatus(status0, idcardStatus);
                setAuthStatus1(status1, relationStatus);//0未完善1已完善2已过期
                setAuthStatus1(status2, operatorStatus);//0未授权1已授权
                setAuthStatus1(status3, infoAuthBankStatus);//0未验证1已验证
                setAuthStatus(status4, engineerContractStatus);
                setAuthStatus(status5, personalIncomeStatus);
                setAuthStatus(status6, deviceOwnerStatus);
                isAuthAll = (status0 == 2 && status2 == 2 && status3 == 2 && status4 == 2 && status5 == 2 && status6 == 2 && status1 == 2);
                if (isAuthAll) {
                    modelContainer.setVisibility(View.VISIBLE);
                }
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(mContext, message);

            }
        }));
    }


    public void setAuthStatus(int status, TextView tv) {
        ViewGroup container = (ViewGroup) tv.getParent();
        String statusText = "待审核";
        switch (status) {
            case 0://待审
            case 3://失败
                if (status == 0) {
                    if (tv == idcardStatus) {
                        statusText = "去拍摄";
                    } else {
                        statusText = "去上传";
                    }
                } else {
                    statusText = "审核不通过";
                }
                setDefaultStausView(tv, container);
                break;
            case 1://审核中
                statusText = "审核中";
                container.setEnabled(false);
                tv.setTextColor(getResources().getColor(R.color.c_ff8901));
                tv.setCompoundDrawables(null, null, null, null);
                tv.setCompoundDrawablePadding(0);
                break;
            case 2://完成
                if (tv == personalIncomeStatus) {
                    statusText = "已验证";
                } else {
                    statusText = "已上传";
                }
                setCompleteStatusView(tv, container);
                break;
        }
        tv.setText(statusText);
    }

    public void setAuthStatus1(int status, TextView tv) {
        ViewGroup container = (ViewGroup) tv.getParent();
        String statusText = "未完善";
        switch (status) {
            case 0:
            case 2:
                if (status == 0) {
                    if (tv == operatorStatus) {
                        statusText = "去授权";
                    } else if (tv == infoAuthBankStatus) {
                        statusText = "未验证";
                    } else {
                        statusText = "未完善";
                    }
                } else {
                    statusText = "已过期";
                }
                setDefaultStausView(tv, container);
                break;
            case 1:
                if (tv == operatorStatus) {
                    statusText = "已授权";
                } else if (tv == infoAuthBankStatus) {
                    statusText = "已验证";
                } else {
                    statusText = "已完善";
                }
                setCompleteStatusView(tv, container);
                break;
        }
        tv.setText(statusText);

    }

    private void setCompleteStatusView(TextView tv, ViewGroup container) {
        container.setEnabled(false);
        tv.setTextColor(getResources().getColor(R.color.c_64baa4));
        Drawable leftDrawable = getResources().getDrawable(R.drawable.ic_item_status_left);
        leftDrawable.setBounds(0, 0, leftDrawable.getIntrinsicWidth(), leftDrawable.getIntrinsicHeight());
        tv.setCompoundDrawablePadding(DensityUtil.dip2px(this, 2));
        tv.setCompoundDrawables(leftDrawable, null, null, null);
        int left = DensityUtil.dip2px(this, 4);
        int top = DensityUtil.dip2px(this, 2);
        tv.setPadding(left, top, left, top);
        tv.setBackground(getResources().getDrawable(R.drawable.bg_info_auth_status));
    }

    private void setDefaultStausView(TextView tv, ViewGroup container) {
        container.setEnabled(true);
        tv.setTextColor(getResources().getColor(R.color.c_ff8901));
        Drawable rightDrawable = getResources().getDrawable(R.drawable.arrow_right);
        rightDrawable.setBounds(0, 0, rightDrawable.getIntrinsicWidth(), rightDrawable.getIntrinsicHeight());
        tv.setCompoundDrawables(null, null, rightDrawable, null);
        tv.setCompoundDrawablePadding((int) getResources().getDimension(R.dimen.pad1));
    }


    @Override
    public void initPresenter() {

    }

    @OnClick({R.id.info_auth_relation_container, R.id.bankaccount_container, R.id.info_auth_idcard_container, R.id.info_auth_operator_container, R.id.info_auth_bank_cotainer, R.id.info_auth_engineer_contract, R.id.info_auth_personal_income, R.id.info_auth_device_owner})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.info_auth_relation_container:
                Constants.toActivity(this, ContactActivity.class, null);
                break;
            case R.id.info_auth_idcard_container:
                Bundle idData = new Bundle();
                idData.putString(Constants.UNIQUEID, uniqueNo);
                Constants.toActivity(this, IdCardHandActivity.class, idData);
                break;
            case R.id.info_auth_operator_container:
                Constants.toActivity(this, OperateActivity.class, null);
                break;
            case R.id.info_auth_bank_cotainer:
                Bundle bvData = new Bundle();
                bvData.putString(Constants.REAL_NAME, realName);
                Constants.toActivity(this, BankVerifyctivity.class, bvData);
                break;
            case R.id.info_auth_engineer_contract:
                Bundle egData = new Bundle();
                egData.putString(Constants.PAGET_TYPE, IncomeProofActivity.ENGINEER_CONTRACT);
                egData.putString(Constants.UNIQUEID, uniqueNo);
                Constants.toActivity(this, IncomeProofActivity.class, egData);
                break;
            case R.id.info_auth_personal_income:
                Bundle piData = new Bundle();
                piData.putString(Constants.UNIQUEID, uniqueNo);
                piData.putString(Constants.PAGET_TYPE, IncomeProofActivity.INCOME_PROOF);
                Constants.toActivity(this, IncomeProofActivity.class, piData);
                break;
            case R.id.info_auth_device_owner:
                Bundle deData = new Bundle();
                deData.putString(Constants.UNIQUEID, uniqueNo);
                Constants.toActivity(this, MachineOwnershipActivity.class, deData);
                break;
            case R.id.bankaccount_container:
                ToastUtils.showToast(mContext, "尚未开放");
                break;
            case R.id.common_titleview_back_img:
                if (isAuthAll) {
                    setResult(QuestionCommunityActivity.UPDATE_TIKCET);
                }
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (isAuthAll) {
            setResult(QuestionCommunityActivity.UPDATE_TIKCET);
        }
        super.onBackPressed();
    }
}
