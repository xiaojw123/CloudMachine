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
import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.bean.TypeItem;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.utils.CommonUtils;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.PermissionsChecker;
import com.cloudmachine.utils.ToastUtils;
import com.cloudmachine.utils.widgets.ClearEditTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    TypeItem typeItem;


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
                saveContact();
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
                } else {
                    ToastUtils.showToast(mContext, "紧急联系人不能为空!");
                }
                break;
        }
    }

    private void saveContact() {
        String contactName = nameTv.getText().toString();
        String contactMobile=mobileTv.getText().toString();
        JSONArray contactJarray=new JSONArray();
        JSONObject contactJobj=new JSONObject();
        try {
            contactJobj.put("contactName",contactName);
            contactJobj.put("contactMobile",contactMobile);
            contactJobj.put("isEmergency",1);
            contactJobj.put("relationType",typeItem.getCode());
            contactJarray.put(contactJobj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mRxManager.add(Api.getDefault(HostType.HOST_CLOUDM_YJX).saveContacts(UserHelper.getMemberId(this), contactJarray.toString()).compose(RxHelper.<String>handleResult()).subscribe(new RxSubscriber<String>(mContext) {
            @Override
            protected void _onNext(String s) {
                ToastUtils.showToast(mContext,"提交成功");
                finish();
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(mContext,message);

            }
        }));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_READ_CONTACT:
                if (resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
                    CommonUtils.showPermissionDialog(mContext,Constants.PermissionType.ADDRESS_BOOK);
                } else {
                    Constants.toActivityForR(this, AddressBookActivity.class, null, SELECT_CONTACT);
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
                    typeItem = data.getParcelableExtra(Constants.TYPE_ITEM);
                    selectPos = data.getIntExtra(Constants.RELATION_POSITION, -1);
                    relationTv.setText(typeItem.getValue());
                    submitBtn.setButtonClickEnable(true);
                }
                break;

        }


    }
}
