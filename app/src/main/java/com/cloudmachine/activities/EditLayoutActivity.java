package com.cloudmachine.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import com.cloudmachine.R;
import com.cloudmachine.adapter.EditListAdapter;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.bean.EditListInfo;
import com.cloudmachine.bean.MachineBrandInfo;
import com.cloudmachine.bean.MachineModelInfo;
import com.cloudmachine.bean.MachineTypeInfo;
import com.cloudmachine.bean.RootNodesInfo;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.net.task.GetRootNodesAsync;
import com.cloudmachine.net.task.MachineBrandListAsync;
import com.cloudmachine.net.task.MachineModelListAsync;
import com.cloudmachine.net.task.MachineTypesListAsync;
import com.cloudmachine.net.task.UpdateDeviceInfoTask;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.ToastUtils;
import com.cloudmachine.utils.UMengKey;
import com.cloudmachine.widget.CommonTitleView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EditLayoutActivity extends BaseAutoLayoutActivity implements Callback, OnClickListener, OnItemClickListener {

    private Context mContext;
    private Handler mHandler;
    private String titleText;
    private int editType;
    private int itemType;
    private View edit_layout;
    private EditText text_edit;
    private String cText;

    private static final int DATE_DIALOG_ID = 1;
    private static final int SHOW_DATAPICK = 0;
    private int mYear;
    private int mMonth;
    private int mDay;
    private String timeStr;

    private View list_layout;
    private ListView edit_listview;
    private EditListAdapter eAdapter;
    private List<EditListInfo> dataResult = new ArrayList<EditListInfo>();
    private EditListInfo editListInfo;
    private String value1, value2, value3;
    CommonTitleView title_layout;

    String deviceID, deviceName;
    String name;
    String itemName;
    String titleName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        this.mContext = this;
        mHandler = new Handler(this);
        getIntentData();
        initView();
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void onResume() {
        //MobclickAgent.onPageStart(UMengKey.time_repair_create_attribute);
        super.onResume();
    }

    @Override
    protected void onPause() {
        //MobclickAgent.onPageEnd(UMengKey.time_repair_create_attribute);
        super.onPause();
    }

    private void getIntentData() {
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            try {
                titleName = bundle.getString(Constants.P_TITLENAME);
                itemName = bundle.getString(Constants.P_EDIT_LIST_ITEM_NAME);
                deviceID = bundle.getString(Constants.P_DEVICEID);
                deviceName = bundle.getString(Constants.P_DEVICENAME);
                editType = bundle.getInt(Constants.P_EDITTYPE);
                itemType = bundle.getInt(Constants.P_ITEMTYPE);
                titleText = bundle.getString(Constants.P_TITLETEXT);
                cText = bundle.getString(Constants.P_EDITRESULTSTRING);
                value1 = bundle.getString(Constants.P_EDIT_LIST_VALUE1);
                value2 = bundle.getString(Constants.P_EDIT_LIST_VALUE2);
                value3 = bundle.getString(Constants.P_EDIT_LIST_VALUE3);

            } catch (Exception e) {
            }

        }
    }

    private void initView() {
        title_layout = (CommonTitleView) findViewById(R.id.title_layout);

        edit_layout = findViewById(R.id.edit_layout);
        text_edit = (EditText) findViewById(R.id.text_edit);
        list_layout = findViewById(R.id.list_layout);
        edit_listview = (ListView) findViewById(R.id.edit_listview);
        eAdapter = new EditListAdapter(mContext, dataResult, mHandler);
        eAdapter.setItemName(itemName);
        edit_listview.setAdapter(eAdapter);
        edit_listview.setOnItemClickListener(this);
        switch (editType) {
            case Constants.E_DEVICE_TEXT:
                MobclickAgent.onPageStart(UMengKey.time_machine_info_edit);
                edit_layout.setVisibility(View.VISIBLE);
                list_layout.setVisibility(View.GONE);
                text_edit.setText(null != cText ? cText : "");
                switch (itemType) {
                    case Constants.E_ITEMS_buyPrice:
                        text_edit.setRawInputType(InputType.TYPE_CLASS_NUMBER);
                        break;
                }
                text_edit.setFocusable(true);
                text_edit.requestFocus();
                break;
            case Constants.E_DEVICE_DATA:
                edit_layout.setVisibility(View.GONE);
                list_layout.setVisibility(View.GONE);
                setDateTime();
                break;
            case Constants.E_DEVICE_LIST:
                edit_layout.setVisibility(View.GONE);
                list_layout.setVisibility(View.VISIBLE);
                //
                switch (itemType) {
                    case Constants.E_ITEMS_role_list:

                        new GetRootNodesAsync(mContext, mHandler).execute(
                                String.valueOf(value1), String.valueOf(value2));
                        break;
                    case Constants.E_ITEMS_category:
                        new MachineTypesListAsync(mContext, mHandler).execute();
                        break;
                    case Constants.E_ITEMS_brand:
                        new MachineBrandListAsync(mContext, mHandler).execute(value1);
                        break;
                    case Constants.E_ITEMS_model:
                        new MachineModelListAsync(mContext, mHandler).execute(
                                value1, value2);
                        break;
                }

                break;
        }
        if (!TextUtils.isEmpty(titleName)) {
            title_layout.setTitleName(titleName);
        }
        if (Constants.IPageType.PAGE_DEVICE_INFO.equals(getPageType())) {
            title_layout.setRightText("保存", new OnClickListener() {
                @Override
                public void onClick(View v) {
                    name = text_edit.getText().toString();
                    if (TextUtils.isEmpty(name)) {
                        ToastUtils.showToast(EditLayoutActivity.this, "名称不能为空");
                        return;
                    }
                    int len = name.length();
                    if (len >= 2 && len <= 12) {
                        UpdateDeviceInfoTask task = new UpdateDeviceInfoTask(mHandler, UserHelper.getMemberId(EditLayoutActivity.this), deviceID, name);
                        task.execute();
                    } else {
                        ToastUtils.showToast(EditLayoutActivity.this, "请输入2~12位汉字、字母、数字组合");
                    }
                }
            });
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//			resultActivity();
            cancel();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean handleMessage(Message msg) {
        // TODO Auto-generated method stub
        int len = 0;
        String message = "";
        switch (msg.what) {
            case Constants.HANDLER_UPDATE_INFO_SUCCESS:
                String result1 = (String) msg.obj;
                ToastUtils.showToast(this, result1);
                Intent intent = new Intent();
                intent.putExtra(Constants.P_DEVICENAME, name);
                setResult(AddDeviceActivity.UPATE_INFO, intent);
                finish();
                break;
            case Constants.HANDLER_UPDATE_INFO_FAILD:
                String result2 = (String) msg.obj;
                ToastUtils.showToast(this, result2);
                break;
            case Constants.HANDLER_GETROOTNODES_SUCCESS:

                List<RootNodesInfo> rNodes = (List<RootNodesInfo>) msg.obj;
                len = rNodes.size();
                dataResult.clear();
                for (int i = 0; i < len; i++) {
                    RootNodesInfo rInfo = rNodes.get(i);
                    EditListInfo data = new EditListInfo();
                    data.setId(rInfo.getId());
                    data.setName(rInfo.getName());
                    data.setStr2(rInfo.getRemark());
                    data.setStr1(rInfo.getPermissionId());
                    dataResult.add(data);
                }
                eAdapter.notifyDataSetChanged();
                break;
            case Constants.HANDLER_GETROOTNODES_FAIL:
                message = (String) msg.obj;
                Constants.MyToast(null != message ? message : getResources().getString(R.string.empty_message1));
                break;
            case Constants.HANDLER_GETMACHINETYPES_SUCCESS:
                List<MachineTypeInfo> mTypeInfo = (List<MachineTypeInfo>) msg.obj;
                len = mTypeInfo.size();
                dataResult.clear();
                int selectionId = -1;
                for (int i = 0; i < len; i++) {
                    MachineTypeInfo mInfo = mTypeInfo.get(i);
                    EditListInfo data = new EditListInfo();
                    data.setId(mInfo.getId());
                    data.setName(mInfo.getName());
                    data.setPK_PROD_DEF(mInfo.getPk_PROD_DEF());
                    if (mInfo.getPk_PROD_DEF() == value1) {
                        data.setIsClick(true);
                        selectionId = i;
                    }
                    dataResult.add(data);
                }
                if (selectionId != -1)
                    edit_listview.setSelection(selectionId);
                eAdapter.notifyDataSetChanged();
                break;
            case Constants.HANDLER_GETMACHINETYPES_FAIL:
            case Constants.HANDLER_GETMACHINEBRAND_FAIL:
            case Constants.HANDLER_GETMACHINEMODEL_FAIL:
                message = (String) msg.obj;
                Constants.MyToast(null != message ? message : getResources().getString(R.string.empty_message1));
                break;
            case Constants.HANDLER_GETMACHINEBRAND_SUCCESS:
                List<MachineBrandInfo> mBrandInfo = (List<MachineBrandInfo>) msg.obj;
                len = mBrandInfo.size();
                dataResult.clear();
                int selectionId2 = -1;
                for (int i = 0; i < len; i++) {
                    MachineBrandInfo mInfo = mBrandInfo.get(i);
                    EditListInfo data = new EditListInfo();
                    data.setId(mInfo.getId());
                    data.setName(mInfo.getName());
                    data.setPK_BRAND(mInfo.getPk_BRAND());
                    if (mInfo.getPk_BRAND() == value2) {
                        data.setIsClick(true);
                        selectionId2 = i;
                    }
                    dataResult.add(data);
                }
                if (selectionId2 != -1)
                    edit_listview.setSelection(selectionId2);
                eAdapter.notifyDataSetChanged();
                break;
            case Constants.HANDLER_GETMACHINEMODEL_SUCCESS:
                List<MachineModelInfo> mModelInfo = (List<MachineModelInfo>) msg.obj;
                len = mModelInfo.size();
                dataResult.clear();
                int selectionId3 = -1;
                for (int i = 0; i < len; i++) {
                    MachineModelInfo mInfo = mModelInfo.get(i);
                    EditListInfo data = new EditListInfo();
                    data.setId(mInfo.getId());
                    data.setName(mInfo.getModelName());
                    data.setPK_VHCL_MATERIAL(mInfo.getPk_VHCL_MATERIAL());
                    if (mInfo.getPk_VHCL_MATERIAL() == value3) {
                        data.setIsClick(true);
                        selectionId3 = i;
                    }
                    dataResult.add(data);
                }
                if (selectionId3 != -1)
                    edit_listview.setSelection(selectionId3);
                eAdapter.notifyDataSetChanged();
                break;
        }
        return false;
    }

    private void cancel() {
        Intent intent = new Intent();
        intent.putExtra(Constants.P_EDITTYPE, editType);
        intent.putExtra(Constants.P_ITEMTYPE, itemType);
        switch (editType) {
            case Constants.E_DEVICE_TEXT:
                String str = text_edit.getText().toString();
                switch (itemType) {
                    case Constants.E_ITEMS_buyPrice:
                        try {
                            float price = Float.valueOf(str);
                        } catch (Exception e) {
                            Constants.MyToast("输入格式错误！");
                            return;
                        }
                        break;
                }
                intent.putExtra(Constants.P_EDITRESULTSTRING, str);
                break;
            case Constants.E_DEVICE_DATA:
                intent.putExtra(Constants.P_EDITRESULTSTRING, timeStr);
                break;
            case Constants.E_DEVICE_LIST:
                intent.putExtra(Constants.P_EDITRESULTITEM, editListInfo);
                break;
        }
        setResult(RESULT_OK, intent);
        finish();
    }

    private void setDateTime() {
        if (null != cText && cText.split(Constants.S_TIME_FG).length >= 3) {
            String[] time = cText.split(Constants.S_TIME_FG);
            mYear = Integer.valueOf(time[0]);
            mMonth = Integer.valueOf(time[1]);
            mDay = Integer.valueOf(time[2]);
        } else {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
        }
        showDialog(DATE_DIALOG_ID);
    }

    /**
     * 日期控件的事件
     */
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            updateDisplay();
        }

    };

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
                        mDay);
        }
        return null;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        switch (id) {
            case DATE_DIALOG_ID:
                ((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
                break;
        }
    }

    /**
     * 更新日期
     */
    private void updateDisplay() {
        timeStr = new StringBuilder().append(mYear).append(Constants.S_TIME_FG).append(
                (mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1)).append(Constants.S_TIME_FG).append(
                (mDay < 10) ? "0" + mDay : mDay) + "";
        cancel();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        // TODO Auto-generated method stub
        if (parent.getId() == R.id.edit_listview) {
            int len = dataResult.size();
            for (int i = 0; i < len; i++) {
                if (i == position) {
                    dataResult.get(i).setIsClick(true);
                } else {
                    dataResult.get(i).setIsClick(false);
                }
            }
            eAdapter.notifyDataSetChanged();
            editListInfo = dataResult.get(position);
            cancel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MobclickAgent.onPageEnd(UMengKey.time_machine_info_edit);
    }
}
