package com.cloudmachine.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
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
import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.bean.EditListInfo;
import com.cloudmachine.bean.EmunBean;
import com.cloudmachine.bean.MachineBrandInfo;
import com.cloudmachine.bean.MachineModelInfo;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
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
    //    private String value1, value2, value3;
    CommonTitleView title_layout;

    String deviceID, deviceName;
    String name;
    String itemName;
    String titleName;
    String typeId;
    String brandId;
    String modelId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
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
            brandId = bundle.getString(Constants.P_BRANDID);
            typeId = bundle.getString(Constants.P_TYPEID);
            modelId = bundle.getString(Constants.P_MODELID);
            titleName = bundle.getString(Constants.P_TITLENAME);
            itemName = bundle.getString(Constants.P_EDIT_LIST_ITEM_NAME);
            deviceID = bundle.getString(Constants.P_DEVICEID);
            deviceName = bundle.getString(Constants.P_DEVICENAME);
            editType = bundle.getInt(Constants.P_EDITTYPE);
            itemType = bundle.getInt(Constants.P_ITEMTYPE);
            titleText = bundle.getString(Constants.P_TITLETEXT);
            cText = bundle.getString(Constants.P_EDITRESULTSTRING);

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
//                    case Constants.E_ITEMS_role_list:
//
//                        new GetRootNodesAsync(mContext, mHandler).execute(
//                                String.valueOf(value1), String.valueOf(value2));
//                        break;
                    case Constants.E_ITEMS_category:
                        updateMachineTypeList();
                        break;
                    case Constants.E_ITEMS_brand:
                        updateMachineBrandList();
//                        new MachineBrandListAsync(mHandler).execute(memberId, typeId);
                        break;
                    case Constants.E_ITEMS_model:
                        updateMachineModelList();
//                        new MachineModelListAsync(mContext, mHandler).execute(
//                                memberId, typeId, brandId);
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
                        UpdateDeviceInfoTask task = new UpdateDeviceInfoTask(mHandler, deviceID, name);
                        task.execute();
                    } else {
                        ToastUtils.showToast(EditLayoutActivity.this, "请输入2~12位汉字、字母、数字组合");
                    }
                }
            });
        }
    }

    public void updateMachineBrandList() {
        mRxManager.add(Api.getDefault(HostType.HOST_LARK).getBrandList(typeId, null).compose(RxHelper.<List<MachineBrandInfo>>handleResult())
                .subscribe(new RxSubscriber<List<MachineBrandInfo>>(mContext) {
                    @Override
                    protected void _onNext(List<MachineBrandInfo> items) {
                        if (items != null && items.size() > 0) {
                            int len = items.size();
                            dataResult.clear();
                            int selectionId2 = -1;
                            for (int i = 0; i < len; i++) {
                                MachineBrandInfo item = items.get(i);
                                EditListInfo data = new EditListInfo();
                                data.setId(item.getId());
                                data.setName(item.getBrandName());
                                if (TextUtils.equals(item.getId(), brandId)) {
                                    data.setIsClick(true);
                                    selectionId2 = i;
                                }
                                dataResult.add(data);
                            }
                            if (selectionId2 != -1)
                                edit_listview.setSelection(selectionId2);
                            eAdapter.notifyDataSetChanged();
                        } else {
                            _onError(null);
                        }
                    }

                    @Override
                    protected void _onError(String message) {
                        ToastUtils.showToast(mContext, "没有数据哦");
                    }
                }));
    }


    public void updateMachineTypeList() {
        mRxManager.add(Api.getDefault(HostType.HOST_LARK).getEnum(Constants.MACHINE_TYPE).compose(RxHelper.<List<EmunBean>>handleResult())
                .subscribe(new RxSubscriber<List<EmunBean>>(mContext) {
                    @Override
                    protected void _onNext(List<EmunBean> emumList) {
                        if (emumList != null && emumList.size() > 0) {
                            dataResult.clear();
                            int selectionId = -1;
                            int len = emumList.size();
                            for (int i = 0; i < len; i++) {
                                EmunBean item = emumList.get(i);
                                EditListInfo data = new EditListInfo();
                                data.setId(String.valueOf(item.getKeyType()));
                                data.setName(item.getValueName());
//                                    data.setPK_PROD_DEF(mInfo.getPk_PROD_DEF());
                                if (TextUtils.equals(String.valueOf(item.getKeyType()), typeId)) {
                                    data.setIsClick(true);
                                    selectionId = i;
                                }
                                dataResult.add(data);

                            }
                            if (selectionId != -1)
                                edit_listview.setSelection(selectionId);
                            eAdapter.notifyDataSetChanged();
                        } else {
                            _onError(null);
                        }

                    }

                    @Override
                    protected void _onError(String message) {
                        ToastUtils.showToast(mContext, "没有数据哦");
                    }
                }));


    }


    public void updateMachineModelList() {
        mRxManager.add(Api.getDefault(HostType.HOST_LARK).getModelList(typeId, brandId, null).compose(RxHelper.<List<MachineModelInfo>>handleResult())
                .subscribe(new RxSubscriber<List<MachineModelInfo>>(mContext) {
                    @Override
                    protected void _onNext(List<MachineModelInfo> items) {
                        if (items != null && items.size() > 0) {
                            int len = items.size();
                            dataResult.clear();
                            int selectionId3 = -1;
                            for (int i = 0; i < len; i++) {
                                MachineModelInfo item = items.get(i);
                                EditListInfo data = new EditListInfo();
                                data.setId(item.getId());
                                data.setName(item.getModelName());
                                if (TextUtils.equals(item.getId(), modelId)) {
                                    data.setIsClick(true);
                                    selectionId3 = i;
                                }
                                dataResult.add(data);
                            }
                            if (selectionId3 != -1)
                                edit_listview.setSelection(selectionId3);
                            eAdapter.notifyDataSetChanged();
                        } else {
                            _onError(null);
                        }


                    }

                    @Override
                    protected void _onError(String message) {
                        ToastUtils.showToast(mContext, "没有数据哦");
                    }
                }));

    }

//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        // TODO Auto-generated method stub
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
////			resultActivity();
//            cancel();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

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
                ToastUtils.showToast(this, "保存成功！");
                Intent intent = new Intent();
                intent.putExtra(Constants.P_DEVICENAME, name);
                setResult(AddDeviceActivity.UPATE_INFO, intent);
                finish();
                break;
            case Constants.HANDLER_UPDATE_INFO_FAILD:
                ToastUtils.showToast(this, "保存失败！");
                break;
            case Constants.HANDLER_GETMACHINEBRAND_FAIL:
            case Constants.HANDLER_GETMACHINEMODEL_FAIL:
                message = (String) msg.obj;
                Constants.MyToast(null != message ? message : getResources().getString(R.string.empty_message1));
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
        if (parent.getId() == R.id.edit_listview) {
            int len = dataResult.size();
            for (int i = 0; i < len; i++) {
                EditListInfo info = dataResult.get(i);

                if (i == position) {
                    info.setIsClick(true);
                } else {
                    info.setIsClick(false);
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
