package com.cloudmachine.ui.home.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.model.Text;
import com.cloudmachine.R;
import com.cloudmachine.autolayout.widgets.CustomDialog;
import com.cloudmachine.autolayout.widgets.RadiusButtonView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.tongdun.android.liveness.LivenessDetectActivity;

/**
 * 身份认证管理
 */
public class InfoManagerActivity extends BaseAutoLayoutActivity implements View.OnClickListener {
    public static final String KEY_COMPLETED="key_completed";
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
    @BindView(R.id.face_status_tv)
    TextView faceStatusTv;
    @BindView(R.id.ticket_question_tv)
    TextView questionTv;
    boolean isPerCompleted;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_manager);
        ButterKnife.bind(this);
        initView();

    }

    private void initView() {
        ticketBtn.setButtonEnable(false);
        ticketBtn.setOnClickListener(this);
        if (isPerCompleted) {
            personStatusTv.setTextColor(getResources().getColor(R.color.cor20));
            personStatusTv.setText(getResources().getString(R.string.completed));
        } else {
            personStatusTv.setTextColor(getResources().getColor(R.color.c_ff8901));
            personStatusTv.setText(getResources().getString(R.string.no_complete));
        }
    }

    @Override
    public void initPresenter() {

    }

    @OnClick({R.id.ticket_question_tv,R.id.mamager_personalinfo_rl, R.id.mamager_face_rl, R.id.mamager_bank_rl, R.id.mamager_contact_rl, R.id.mamager_operator_rl})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ticket_question_tv:
                Constants.toActivity(this,TicketActivity.class,null);
                break;
            case R.id.mamager_personalinfo_rl://个人信息
                isPerCompleted=true;
                personStatusTv.setTextColor(getResources().getColor(R.color.cor20));
                personStatusTv.setText("已完善");
                Constants.toActivity(this, AuthPersonalInfoActivity.class, null);
                break;
            case R.id.mamager_bank_rl://银行卡信息
                if (isPerCompleted){
                    Constants.toActivity(this, BankVerifyctivity.class, null);
                }else{
                    showDialog("验证");
                }
                break;
            case R.id.mamager_face_rl://刷脸信息
                if (isPerCompleted){
                    Constants.toActivityForR(this, LivenessDetectActivity.class,null);
                }else{
                    showDialog("验证");
                }

                break;
            case R.id.mamager_contact_rl://联系人信息
                if (isPerCompleted){
                    Constants.toActivity(this,ContactActivity.class,null);
                }else{
                    showDialog("完善");
                }
                break;
            case R.id.mamager_operator_rl://运营商信息
                if(isPerCompleted){
                    Constants.toActivity(this,OperateActivity.class,null);
                }else{
                    showDialog("授权");
                }
                break;
            case R.id.radius_button_text://开通小票，满足以上五个条件，进行下一步
                break;
        }
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
        switch (resultCode){
            case LivenessDetectActivity.REST_FACE_IDENTIFY:
                faceStatusTv.setTextColor(getResources().getColor(R.color.cor20));
                faceStatusTv.setText(getResources().getString(R.string.completed));
                faceStatusTv.setCompoundDrawables(null,null,null,null);
                break;

        }




    }
}
