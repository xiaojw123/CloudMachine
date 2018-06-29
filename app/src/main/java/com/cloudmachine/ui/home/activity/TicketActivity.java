package com.cloudmachine.ui.home.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.autolayout.widgets.CustomDialog;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.utils.CommonUtils;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TicketActivity extends BaseAutoLayoutActivity {

    @BindView(R.id.ticket_open_btn)
    Button openBtn;
    @BindView(R.id.tick_protocol_tv)
    TextView protocalTv;
    @BindView(R.id.ticket_select_rbtn)
    RadioButton selectRbtn;


    boolean isAuth;//身份认证都标识

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);
        ButterKnife.bind(this);
    }

    @Override
    public void initPresenter() {

    }

    @OnClick({R.id.ticket_open_btn, R.id.tick_protocol_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ticket_open_btn://开通小票，isAuth需赋值

                if (selectRbtn.isChecked()) {
                    if (isAuth) {
                        CustomDialog.Builder builder = new CustomDialog.Builder(mContext);
                        builder.setAlertIcon(R.drawable.ic_dialog_success);
                        builder.setMessage("恭喜，您已成功开通小票");
                        builder.setNeutralButton("去看看", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.create().show();
                    } else {
                        CustomDialog.Builder builder = new CustomDialog.Builder(mContext);
                        builder.setMessage("开通，请先完成身份认证");
                        builder.setNeutralButton("去认证", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Constants.toActivity(TicketActivity.this, InfoManagerActivity.class, null);
                            }
                        });
                        builder.create().show();
                    }
                }else{
                    ToastUtils.showToast(mContext,"需同意相关协议后才能去开通小票");
                }
                break;
            case R.id.tick_protocol_tv://跳转协议H5页面

                break;


        }


    }
}
