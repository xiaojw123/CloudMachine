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
import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
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
    String mPageType;
    long memberId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);
        ButterKnife.bind(this);
        mPageType=getIntent().getStringExtra(Constants.PAGET_TYPE);
        memberId= UserHelper.getMemberId(this);
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnClick({R.id.ticket_open_btn, R.id.tick_protocol_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ticket_open_btn://开通小票，isAuth需赋值

                if (selectRbtn.isChecked()) {
                    if (isAuth) {
                        openTicket();
                    } else {
                        CustomDialog.Builder builder = new CustomDialog.Builder(mContext);
                        builder.setMessage("开通前，请先完成身份认证");
                        builder.setNeutralButton("去认证", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (Constants.IPageType.PAGE_INFO_MANAGER.equals(mPageType)){
                                    finish();
                                }else{
                                    Constants.toActivity(TicketActivity.this, InfoManagerActivity.class, null);
                                }
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
    private void openTicket(){
        mRxManager.add(Api.getDefault(HostType.HOST_CLOUDM_YJX).openTicket(memberId).compose(RxHelper.<String>handleResult()).subscribe(new RxSubscriber<String>(this) {
            @Override
            protected void _onNext(String s) {
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
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(mContext,message);
            }
        }));

    }
}
