package com.cloudmachine.ui.home.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.autolayout.widgets.CustomDialog;
import com.cloudmachine.autolayout.widgets.RadiusButtonView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.bean.OilSynBean;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.ui.home.contract.OilSyncContract;
import com.cloudmachine.ui.home.model.OilSyncModel;
import com.cloudmachine.ui.home.presenter.OilSyncPresenter;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.ToastUtils;
import com.cloudmachine.widget.CommonTitleView;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OilSyncActivity extends BaseAutoLayoutActivity<OilSyncPresenter, OilSyncModel> implements View.OnClickListener, OilSyncContract.View {

    @BindView(R.id.oil_sync_tv1)
    TextView syncTv1;
    @BindView(R.id.oil_sync_cb1)
    CheckBox syncCb1;
    @BindView(R.id.oil_sync_tv2)
    TextView syncTv2;
    @BindView(R.id.oil_sync_cb2)
    CheckBox syncCb2;
    @BindView(R.id.oil_sync_tv3)
    TextView syncTv3;
    @BindView(R.id.oil_sync_cb3)
    CheckBox syncCb3;
    @BindView(R.id.oil_sync_tv4)
    TextView syncTv4;
    @BindView(R.id.oil_sync_cb4)
    CheckBox syncCb4;
    @BindView(R.id.oil_sync_tv5)
    TextView syncTv5;
    @BindView(R.id.oil_sync_cb5)
    CheckBox syncCb5;
    @BindView(R.id.oil_sync_submit_btn)
    RadiusButtonView submitBtn;
    @BindView(R.id.oil_sync_ctv)
    CommonTitleView syncCtv;
    long deviceId;
    CustomDialog.Builder syncDialogBuilder;
    CustomDialog syncDialog;
    int oilPos = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oil_sync);
        ButterKnife.bind(this);
        deviceId = getIntent().getLongExtra(Constants.P_DEVICEID, -1);
        submitBtn.setOnClickListener(this);
        syncCtv.setRightClickListener(titleRightClickLi);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getOilSyncList(deviceId);
    }

    View.OnClickListener titleRightClickLi = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showResetDialog();
        }
    };

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @OnClick({R.id.oil_sync_cb1, R.id.oil_sync_cb2, R.id.oil_sync_cb3, R.id.oil_sync_cb4, R.id.oil_sync_cb5})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.oil_sync_cb1:
                oilPos = 0;
                syncCb2.setChecked(false);
                syncCb3.setChecked(false);
                syncCb4.setChecked(false);
                syncCb5.setChecked(false);
                break;
            case R.id.oil_sync_cb2:
                oilPos = 1;
                syncCb1.setChecked(false);
                syncCb3.setChecked(false);
                syncCb4.setChecked(false);
                syncCb5.setChecked(false);
                break;
            case R.id.oil_sync_cb3:
                oilPos = 2;
                syncCb1.setChecked(false);
                syncCb2.setChecked(false);
                syncCb4.setChecked(false);
                syncCb5.setChecked(false);
                break;
            case R.id.oil_sync_cb4:
                oilPos = 3;
                syncCb1.setChecked(false);
                syncCb2.setChecked(false);
                syncCb3.setChecked(false);
                syncCb5.setChecked(false);
                break;
            case R.id.oil_sync_cb5:
                oilPos = 4;
                syncCb1.setChecked(false);
                syncCb2.setChecked(false);
                syncCb3.setChecked(false);
                syncCb4.setChecked(false);
                break;

            case R.id.radius_button_text:
                if (!syncCb1.isChecked() && !syncCb2.isChecked() && !syncCb3.isChecked() && !syncCb4.isChecked() && !syncCb5.isChecked()) {
                    ToastUtils.showToast(this, "未选中任何选项，不能提交");
                    break;
                }
                if (UserHelper.isLogin(this)) {
                    mPresenter.syncOilLevel(deviceId, oilPos);
                } else {
                    ToastUtils.showToast(this, "您还未登录，登录之后才能提交哦");

                }
                break;


        }


    }


    @Override
    public void updateOilSynItemView(int oilPos, Spanned textSpanned) {
        switch (oilPos) {
            case 0:
                syncTv1.setText(textSpanned);
                break;
            case 1:
                syncTv2.setText(textSpanned);
                break;
            case 2:
                syncTv3.setText(textSpanned);
                break;
            case 3:
                syncTv4.setText(textSpanned);
                break;
            case 4:
                syncTv5.setText(textSpanned);
                break;
        }
    }


    private void showResetDialog() {
        CustomDialog.Builder builder = new CustomDialog.Builder(mContext);
        builder.setMessage("油位采集标准恢复出厂设置");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                mPresenter.restOilLevel(deviceId);
            }
        });
        builder.create().show();
    }


    @Override
    public void showLoadingDailog() {
        syncDialogBuilder = new CustomDialog.Builder(this);
        syncDialogBuilder.setAlertIcon(R.drawable.ic_sync_loading);
        syncDialogBuilder.setMessage("正在校准，请稍候");
        syncDialogBuilder.setRotateAmimEnable(true);
        syncDialog = syncDialogBuilder.create();
        syncDialog.setCancelable(false);
        syncDialog.show();

    }

    @Override
    public void showSyncSuccessDailog(String message) {
        if (syncDialog != null && syncDialog.isShowing()) {
            syncDialogBuilder.updateSyncSucess(message, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    syncDialog.dismiss();
                }
            });
        }

    }

    @Override
    public void retrunSyncFailed(String message) {
        if (syncDialog != null && syncDialog.isShowing()) {
            syncDialog.dismiss();
        }
        ToastUtils.showToast(this, message);

    }

    @Override
    protected void onDestroy() {
        if (syncDialog != null && syncDialog.isShowing()) {
            syncDialog.dismiss();
            syncDialog = null;
        }
        super.onDestroy();

    }
}
