package com.cloudmachine.ui.home.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;

import com.cloudmachine.R;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.base.baserx.RxSchedulers;
import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.bean.AuthInfoDetail;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.ToastUtils;
import com.cloudmachine.utils.widgets.ClearEditTextView;
import com.cloudmachine.widget.CommonTitleView;


public class AddressActivity extends BaseAutoLayoutActivity implements View.OnClickListener, ClearEditTextView.OnTextChangeListener {
    ClearEditTextView addressEdt;
    CommonTitleView titleView;
    String uniqueId;
    String mLastAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        uniqueId = getIntent().getStringExtra(Constants.UNIQUEID);
        titleView = (CommonTitleView) findViewById(R.id.address_title_ctv);
        addressEdt = (ClearEditTextView) findViewById(R.id.address_edt);
        addressEdt.setOnTextChangeListener(this);
        titleView.setRightClickListener(this);
        getPersonalInfo(uniqueId, InfoAuthActivity.BNS_TYPE_ADDRESS);
    }

    @Override
    protected void returnInfoDetail(AuthInfoDetail infoDetail) {
        mLastAddress = infoDetail.getResideAddress();
        if (!TextUtils.isEmpty(mLastAddress)) {
            addressEdt.setText(mLastAddress);
            addressEdt.setSelection(mLastAddress.length());
        }
    }


    @Override
    public void initPresenter() {

    }

    @Override
    public void onClick(View v) {
        String address = addressEdt.getText().toString();
        if (TextUtils.equals(address,mLastAddress)){
            ToastUtils.showToast(mContext,getResources().getString(R.string.cannot_submit));
            return;
        }
        if (isInfoUpdate) {
            updatePersonalInfo(InfoAuthActivity.BNS_TYPE_ADDRESS, uniqueId, address, null, null, null);
        } else {
            mRxManager.add(Api.getDefault(HostType.HOST_LARK).submitHomeAddress(uniqueId, address).compose(RxSchedulers.<BaseRespose<String>>io_main()).subscribe(new RxSubscriber<BaseRespose<String>>(mContext) {
                @Override
                protected void _onNext(BaseRespose<String> respose) {
                    if (respose.success()) {
                        ToastUtils.showToast(mContext, "提交成功");
                        finish();
                    } else {
                        _onError(respose.getMessage());
                    }
                }

                @Override
                protected void _onError(String message) {
                    ToastUtils.showToast(mContext, message);
                }
            }));
        }
    }

    @Override
    public void textChanged(Editable s) {
        if (s != null && s.length() > 0) {
            titleView.setRightText("提交");
        } else {
            titleView.setRightText("");
        }
    }
}
