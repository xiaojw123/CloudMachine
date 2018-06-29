package com.cloudmachine.ui.home.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.activities.PermissionsActivity;
import com.cloudmachine.autolayout.widgets.RadiusButtonView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.PermissionsChecker;
import com.cloudmachine.utils.ToastUtils;
import com.cloudmachine.utils.widgets.ClearEditTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContactActivity extends BaseAutoLayoutActivity implements View.OnClickListener {
    private static final int REQ_READ_CONTACT = 0x18;
    private static final int SELECT_CONTACT = 0x19;
    private static final int SELECT_RELATION = 0x20;

    @BindView(R.id.contact_emergency_item)
    FrameLayout emergencyItem;
    @BindView(R.id.contact_relation_item)
    FrameLayout relationItem;
    @BindView(R.id.contact_mobile_tv)
    TextView mobileTv;
    @BindView(R.id.contact_name_tv)
    TextView nameTv;
    @BindView(R.id.contact_relation_tv)
    TextView relationTv;
    @BindView(R.id.contact_submit_btn)
    RadiusButtonView submitBtn;



    PermissionsChecker mChecker;
    int selectPos = -1;
    String relationText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        ButterKnife.bind(this);
        mChecker = new PermissionsChecker(mContext);
        submitBtn.setButtonClickEnable(false);
        submitBtn.setButtonClickListener(this);
    }

    @Override
    public void initPresenter() {

    }

    @OnClick({R.id.contact_emergency_item, R.id.contact_relation_item})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.radius_button_text:
                ToastUtils.showToast(mContext,"提交信息");
                break;

            case R.id.contact_emergency_item:
                if (mChecker.lacksPermissions(Manifest.permission.READ_CONTACTS)) {
                    PermissionsActivity.startActivityForResult(this, REQ_READ_CONTACT,
                            Manifest.permission.READ_CONTACTS);
                } else {
                    Constants.toActivityForR(this, AddressBookActivity.class, null, SELECT_CONTACT);
                }
                break;
            case R.id.contact_relation_item:
                String name = nameTv.getText().toString();
                if (!TextUtils.isEmpty(name)) {
                    Bundle data = new Bundle();
                    data.putInt(Constants.RELATION_POSITION, selectPos);
                    Constants.toActivityForR(this, RelationActivity.class, data, SELECT_RELATION);
                }else{
                    ToastUtils.showToast(mContext,"紧急联系人不能为空!");
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_READ_CONTACT:
                if (resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
                    ToastUtils.showToast(mContext, "需要开启联系人读写权限!");
                } else {
                    Constants.toActivityForR(this, AddressBookActivity.class, null);
                }
                break;
            case SELECT_CONTACT:
                if (data != null) {
                    String name = data.getStringExtra(Constants.NAME);
                    String mobile = data.getStringExtra(Constants.MOBILE);
                    nameTv.setText(name);
                    mobileTv.setText(mobile);
                }

                break;
            case SELECT_RELATION:
                if (data != null) {
                    relationText = data.getStringExtra(Constants.RELATION);
                    selectPos = data.getIntExtra(Constants.RELATION_POSITION, -1);
                    relationTv.setText(relationText);
                    submitBtn.setButtonClickEnable(true);
                }
                break;

        }


    }
}
