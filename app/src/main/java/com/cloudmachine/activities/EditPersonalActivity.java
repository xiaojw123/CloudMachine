package com.cloudmachine.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudmachine.MyApplication;
import com.cloudmachine.R;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.helper.MobEvent;
import com.cloudmachine.net.ATask;
import com.cloudmachine.net.HttpURLConnectionImp;
import com.cloudmachine.net.IHttp;
import com.cloudmachine.utils.LarkUrls;
import com.cloudmachine.utils.ToastUtils;
import com.cloudmachine.utils.widgets.ClearEditTextView;
import com.cloudmachine.utils.widgets.Dialog.LoadingDialog;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

public class EditPersonalActivity extends BaseAutoLayoutActivity implements
        OnClickListener, TextWatcher {
    public static final String NICK_NAME = "nickName";
    private LoadingDialog progressDialog;
    private ClearEditTextView info_ed;
    private String info;
    private TextView save_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_personal_info);
        info = getIntent().getStringExtra("info");
        initView();
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onEvent(this, MobEvent.TIME_PROFILE_EDIT);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.info_save:
                String edtText = info_ed.getText().toString().trim();
                if (!TextUtils.isEmpty(edtText)) {
                    int len = edtText.length();
                    if (len >= 2 && len <= 24) {
                        new saveInfoAsync(edtText).execute();
                    } else {
                        ToastUtils.showToast(this, "请输入2-24位汉字、字母、数字组合");
                    }
                } else {
                    ToastUtils.showToast(this, "请输入2-24位汉字、字母、数字组合");
                }
                break;
        }
    }

    void initView() {
        info_ed = (ClearEditTextView) findViewById(R.id.info_ed);
        save_tv = (TextView) findViewById(R.id.info_save);
        info_ed.addTextChangedListener(this);
        info_ed.setText(info);
        save_tv.setOnClickListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() > 0) {
            save_tv.setVisibility(View.VISIBLE);
        } else {
            save_tv.setVisibility(View.GONE);
        }

    }

    private class saveInfoAsync extends ATask {
        String nickName;

        private saveInfoAsync(String nickName) {
            this.nickName = nickName;
        }

        @Override
        protected void onPreExecute() {
            show();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            if (!MyApplication.getInstance().isOpenNetwork(
                    EditPersonalActivity.this)) {
                return null;
            }
            IHttp httpRequest = new HttpURLConnectionImp();
            String result = "";
            try {
                Map<String, String> paramsMap = new HashMap<>();
                paramsMap.put("nickName", nickName);
                result = httpRequest.post(LarkUrls.INFO_MODIFY_URL, paramsMap);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            disMiss();
            decodeJson(result);
            if (isSuccess) {
                Toast.makeText(EditPersonalActivity.this, "修改成功",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra(NICK_NAME, nickName);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(mContext,
                        message, Toast.LENGTH_SHORT)
                        .show();
            }

        }

    }

    private void show() {
        disMiss();
        if (progressDialog == null) {
            progressDialog = LoadingDialog.createDialog(this);
            progressDialog.setMessage("正在加载，请稍后");
            progressDialog.show();
        }
    }

    private void disMiss() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
