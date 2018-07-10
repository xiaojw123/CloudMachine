package com.cloudmachine.ui.home.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.activities.PermissionsActivity;
import com.cloudmachine.autolayout.widgets.CustomDialog;
import com.cloudmachine.autolayout.widgets.RadiusButtonView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.bean.LoanAuthInfo;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.ApiConstants;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.utils.CommonUtils;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.PermissionsChecker;
import com.cloudmachine.utils.ToastUtils;
import com.cloudmachine.utils.URLs;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.tongdun.android.liveness.LivenessDetectActivity;
import retrofit2.http.Field;

/**
 * 身份认证管理
 */
public class InfoManagerActivity extends BaseAutoLayoutActivity implements View.OnClickListener {
    public static final String KEY_COMPLETED = "key_completed";
    @BindView(R.id.mamager_personalinfo_rl)
    RelativeLayout personalInfoRl;
    @BindView(R.id.mamager_face_rl)
    RelativeLayout faceInfoRl;
    @BindView(R.id.mamager_bank_rl)
    RelativeLayout bankInfoRl;
    @BindView(R.id.mamager_contact_rl)
    RelativeLayout contactInfoRl;
    @BindView(R.id.mamager_operator_rl)
    RelativeLayout operatorInfoRl;//运营商信息
    @BindView(R.id.manager_ticket_btn)
    RadiusButtonView ticketBtn;
    @BindView(R.id.person_status_tv)
    TextView personStatusTv;
    @BindView(R.id.bank_status_tv)
    TextView bankStausTv;
    @BindView(R.id.face_status_tv)
    TextView faceStatusTv;
    @BindView(R.id.contact_status_tv)
    TextView contactStatusTv;
    @BindView(R.id.operator_status_tv)
    TextView operatortatusTv;
    @BindView(R.id.ticket_question_tv)
    TextView questionTv;
    boolean isIdCardAuth;//身份证验证
    PermissionsChecker mChecker;
    long memberId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_manager);
        ButterKnife.bind(this);
        initView();

    }

    private void initView() {
        memberId = UserHelper.getMemberId(this);
        mChecker = new PermissionsChecker(this);
        ticketBtn.setButtonClickEnable(false);
        ticketBtn.setButtonClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateAuthStatus();
    }

    private void updateAuthStatus() {
        mRxManager.add(Api.getDefault(HostType.HOST_CLOUDM_YJX).getAuthStatus(memberId).compose(RxHelper.<LoanAuthInfo>handleResult()).subscribe(new RxSubscriber<LoanAuthInfo>(mContext) {
            @Override
            protected void _onNext(LoanAuthInfo info) {
                if (info != null) {
                    isIdCardAuth = info.isIdCardAuth();
                    String text1 = isIdCardAuth ? getString(R.string.completed) : getString(R.string.no_complete);
                    String text2 = info.isBankCardAuth() ? getString(R.string.verified) : getString(R.string.no_verify);
                    String text3 = info.isFigureAuth() ? getString(R.string.verified) : getString(R.string.no_verify);
                    String text4 = info.isRelationAuth() ? getString(R.string.completed) : getString(R.string.no_complete);
                    String text5 = info.isOpeatoryAuth() ? getString(R.string.authorizationed) : getString(R.string.no_authorization);
                    personStatusTv.setActivated(isIdCardAuth);
                    personStatusTv.setText(text1);
                    bankStausTv.setActivated(info.isBankCardAuth());
                    bankStausTv.setText(text2);
                    faceStatusTv.setActivated(info.isFigureAuth());
                    faceStatusTv.setText(text3);
                    contactStatusTv.setActivated(info.isRelationAuth());
                    contactStatusTv.setText(text4);
                    if (info.isFigureAuth()) {
                        faceInfoRl.setEnabled(false);
                        faceStatusTv.setCompoundDrawables(null, null, null, null);
                    } else {
                        faceInfoRl.setEnabled(true);
                    }
                    if (info.isRelationAuth()) {
                        contactInfoRl.setEnabled(false);
                        contactStatusTv.setCompoundDrawables(null, null, null, null);
                    } else {
                        contactInfoRl.setEnabled(true);
                    }
                    operatortatusTv.setActivated(info.isOpeatoryAuth());
                    operatortatusTv.setText(text5);
                    if (info.isOpeatoryAuth()) {
                        operatorInfoRl.setEnabled(false);
                        operatortatusTv.setCompoundDrawables(null, null, null, null);
                    } else {
                        operatorInfoRl.setEnabled(true);
                    }
                    if (isIdCardAuth && info.isBankCardAuth() && info.isFigureAuth() && info.isRelationAuth() && info.isOpeatoryAuth()) {
                        ticketBtn.setButtonClickEnable(true);
                    } else {
                        ticketBtn.setButtonClickEnable(false);
                    }

                }
            }

            @Override
            protected void _onError(String message) {
            }
        }));
    }

    @Override
    public void initPresenter() {

    }

    @OnClick({R.id.ticket_question_tv, R.id.mamager_personalinfo_rl, R.id.mamager_face_rl, R.id.mamager_bank_rl, R.id.mamager_contact_rl, R.id.mamager_operator_rl})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ticket_question_tv:
            case R.id.radius_button_text://开通小票，满足以上五个条件，进行下一步
                Bundle ticketData = new Bundle();
                ticketData.putString(Constants.PAGET_TYPE, Constants.IPageType.PAGE_INFO_MANAGER);
                Constants.toActivity(this, TicketActivity.class, ticketData);
                break;
            case R.id.mamager_personalinfo_rl://个人信息
                Constants.toActivity(this, AuthPersonalInfoActivity.class, null);
                break;
            case R.id.mamager_bank_rl://银行卡信息
                if (isIdCardAuth) {
                    Constants.toActivity(this, BankVerifyctivity.class, null);
                } else {
                    showDialog("验证");
                }
                break;
            case R.id.mamager_face_rl://刷脸信息
                if (isIdCardAuth) {
                    if (mChecker.lacksPermissions(Constants.PERMISSIONS_CAMER_SD)) {
                        PermissionsActivity.startActivityForResult(this, HomeActivity.PEM_REQCODE_CAMERA, Constants.PERMISSIONS_CAMER_SD);
                    } else {
                        gotoLiveness();
                    }
                } else {
                    showDialog("验证");
                }

                break;
            case R.id.mamager_contact_rl://联系人信息
                if (isIdCardAuth) {
                    Constants.toActivity(this, ContactActivity.class, null);
                } else {
                    showDialog("完善");
                }
                break;
            case R.id.mamager_operator_rl://运营商信息
                if (isIdCardAuth) {//
                    Constants.toActivity(this, OperateActivity.class, null);
                } else {
                    showDialog("授权");
                }
                break;
        }
    }

    private void gotoLiveness() {
        Bundle faceData = new Bundle();
        faceData.putString(LivenessDetectActivity.URL_CONTRASTFACE, URLs.FACE_URL);
        faceData.putLong(LivenessDetectActivity.MEMBER_ID, memberId);
        Constants.toActivity(this, LivenessDetectActivity.class, faceData);
    }

    public void showDialog(String action) {
        CustomDialog.Builder builder = new CustomDialog.Builder(mContext);
        builder.setMessage(Html.fromHtml("请先完善<font color=\"#ff8901\">\"个人信息\"</font>,再" + action));
        builder.setNeutralButton("去完善", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Constants.toActivity(InfoManagerActivity.this, AuthPersonalInfoActivity.class, null);
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == HomeActivity.PEM_REQCODE_CAMERA) {
            if (resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
                CommonUtils.showCameraSDPermissionDialog(this);
            } else {
                gotoLiveness();
            }
        }


    }
}
