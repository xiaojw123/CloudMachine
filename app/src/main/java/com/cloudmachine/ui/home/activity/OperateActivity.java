package com.cloudmachine.ui.home.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.model.Text;
import com.cloudmachine.R;
import com.cloudmachine.autolayout.widgets.RadiusButtonView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.bean.Member;
import com.cloudmachine.bean.UserInfo;
import com.cloudmachine.chart.utils.AppLog;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.ui.home.contract.OperateContact;
import com.cloudmachine.ui.home.model.OperateModel;
import com.cloudmachine.ui.home.presenter.OperatePresenter;
import com.cloudmachine.utils.MemeberKeeper;
import com.cloudmachine.utils.ToastUtils;
import com.cloudmachine.utils.widgets.ClearEditTextView;
import com.cloudmachine.widget.CustomBindDialog;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 运营商授权
 */

public class OperateActivity extends BaseAutoLayoutActivity<OperatePresenter, OperateModel> implements View.OnClickListener, OperateContact.View, ClearEditTextView.OnTextChangeListener {
    @BindView(R.id.opeator_foraget_tv)
    TextView foragetTv;
    @BindView(R.id.operator_submit_btn)
    RadiusButtonView submitBtn;
    @BindView(R.id.operator_mobile_tv)
    TextView mobieTv;
    @BindView(R.id.opeartor_switch_img)
    ImageView switchImg;
    @BindView(R.id.operator_psw_edt)
    ClearEditTextView pswEdt;

    long memberId;
    Timer mTimer;
    int timeOut = 60;//定时时长60s
    CustomBindDialog identyfDialog;
    String mTaskId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operate);
        ButterKnife.bind(this);
        Member member = MemeberKeeper.getOauth(mContext);
        if (member != null) {
            memberId = member.getId();
            mobieTv.setText(member.getMobile());
        }
        submitBtn.setButtonClickEnable(false);
        submitBtn.setButtonClickListener(this);
        pswEdt.setOnTextChangeListener(this);
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);

    }

    @OnClick({R.id.opeator_foraget_tv, R.id.opeartor_switch_img})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.opeator_foraget_tv:
                break;
            case R.id.opeartor_switch_img:
                onDrawableRightClickListener(view);
                break;
            case R.id.radius_button_text:
                String psw = pswEdt.getText().toString();
                mPresenter.authOperator(memberId, psw);
                break;

        }


    }

    public void onDrawableRightClickListener(View view) {

        if (view.isSelected()) {
            view.setSelected(false);
            pswEdt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else {
            view.setSelected(true);
            pswEdt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }
        String contentStr = pswEdt.getText().toString();
        pswEdt.setSelection(contentStr.length());
    }

    @Override
    public void returnVerfiyCode() {
        final CustomBindDialog.Builder builder = new CustomBindDialog.Builder(this);
        builder.setCodeGetListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                v.setEnabled(false);
                mPresenter.getVerifyCode(memberId, mTaskId);
                startTimer(v);
            }
        });
        builder.setNegativeButtonClickListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButtonClickListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String code = builder.getInputCode();
                if (!TextUtils.isEmpty(code)) {
                    if (identyfDialog != null && identyfDialog.isShowing()) {
                        identyfDialog.dismiss();
                    }
                    mPresenter.checkVerifyCode(memberId, mTaskId, code);
                } else {
                    ToastUtils.showToast(mContext, "验证码不能为空");
                }
            }
        });
        builder.setPhoneNum(mobieTv.getText().toString());
        identyfDialog = builder.create();
        identyfDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                cancelTimer();
            }
        });
        identyfDialog.show();
        TextView codeTv = builder.getCodeTv();
        if (codeTv != null) {
            codeTv.setEnabled(false);
            startTimer(codeTv);
        }


    }

    private void cancelTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }


    private void startTimer(final View v) {
        timeOut = 60;
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        timeOut--;
                        String text;
                        if (timeOut <= 0) {
                            if (mTimer != null) {
                                mTimer.cancel();
                                mTimer = null;
                            }
                            text = "重新获取";
                            v.setEnabled(true);
                        } else {
                            text = timeOut + "S";
                        }
                        ((TextView) v).setText(text);
                    }
                });
            }
        }, 0, 1000);
    }


    @Override
    public void checkVertifyCodeSuccess(String message) {
        ToastUtils.showToast(mContext, message);
        finish();
    }

    @Override
    public void returnAuthOperator(String taskId, boolean isAuthed) {
        if (isAuthed) {
            ToastUtils.showToast(mContext, "授权成功");
            finish();
        } else {
            mTaskId = taskId;
            returnVerfiyCode();
        }

    }

    @Override
    public void textChanged(Editable s) {
        String mobileStr = mobieTv.getText().toString();
        if (s.length() > 0&&mobileStr.length()>0) {
            submitBtn.setButtonClickEnable(true);
        }else{
            submitBtn.setButtonClickEnable(false);
        }

    }
}
