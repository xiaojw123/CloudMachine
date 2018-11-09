package com.cloudmachine.ui.home.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.autolayout.widgets.RadiusButtonView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.base.baserx.RxSchedulers;
import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.bean.ResonItem;
import com.cloudmachine.chart.utils.AppLog;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.utils.CommonUtils;
import com.cloudmachine.utils.DensityUtil;
import com.cloudmachine.utils.ToastUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RebundDepositActivity extends BaseAutoLayoutActivity {
    @BindView(R.id.rebund_submit_btn)
    RadiusButtonView rebundSubmitBtn;
    @BindView(R.id.rebund_system_reason)
    LinearLayout rebundSysReasonLayout;
    @BindView(R.id.rebund_other_reason)
    EditText otherReasonEdt;
    @BindView(R.id.rebund_reason_tv)
    TextView chooseReasonTv;
    int reasonId = -1;//-1其他原因
    String idStr;
    String needAmount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rebund_deposit);
        ButterKnife.bind(this);
        initParams();
        getReasonsRxTask();
    }

    private void initParams() {
        idStr = getIntent().getStringExtra("id");
        needAmount = getIntent().getStringExtra("needAmount");
    }


    private void updateResonListView(List<ResonItem> itemList) {
        if (itemList != null && itemList.size() > 0) {
            chooseReasonTv.setVisibility(View.VISIBLE);
            otherReasonEdt.setHint(getResources().getString(R.string.input_other_reason));
            for (ResonItem item : itemList) {
                if (item != null) {
                    FrameLayout itemContainer = new FrameLayout(this);
                    itemContainer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(this, 60)));
                    itemContainer.setPadding(DensityUtil.dip2px(this, 14), 0, DensityUtil.dip2px(this, 14), 0);
                    TextView tv = new TextView(this);
                    FrameLayout.LayoutParams tvParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    tvParams.gravity = Gravity.CENTER_VERTICAL;
                    tv.setLayoutParams(tvParams);
                    tv.setGravity(Gravity.CENTER_VERTICAL);
                    tv.setTextColor(getResources().getColor(R.color.cor8));
                    tv.setText(item.getDescription());
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.siz3));
                    FrameLayout.LayoutParams cbParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    cbParams.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
                    CheckBox cb = new CheckBox(this);
                    cb.setButtonDrawable(getResources().getDrawable(R.drawable.pay_checkbox_selector));
                    cb.setLayoutParams(cbParams);
                    cb.setClickable(false);
                    itemContainer.setTag(item.getId());
                    itemContainer.addView(tv);
                    itemContainer.addView(cb);
                    itemContainer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setItemChecked(v);

                        }
                    });
                    rebundSysReasonLayout.addView(itemContainer);
                }
            }

        }

    }

    private void setItemChecked(View v) {
        CheckBox cb = ((CheckBox) (((ViewGroup) v).getChildAt(1)));
        if (cb.isChecked()) {
            return;
        }
        cb.setChecked(true);
        Object obj = v.getTag();
        if (obj instanceof Integer) {
            reasonId = (int) obj;
        }
        for (int i = 0; i < rebundSysReasonLayout.getChildCount(); i++) {
            ViewGroup itemViewGroup = (ViewGroup) rebundSysReasonLayout.getChildAt(i);
            CheckBox itemCb = (CheckBox) itemViewGroup.getChildAt(1);
            if (v != itemViewGroup) {
                itemCb.setChecked(false);
            }
        }
    }


    @Override
    public void initPresenter() {

    }

    @OnClick({R.id.rebund_submit_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.radius_button_text:
                rebundRxTask();
                break;
        }
    }

    private void rebundRxTask() {
        Map<String, String> paramsMap = new HashMap<>();
        String text = otherReasonEdt.getText().toString();
        if (reasonId == -1 && TextUtils.isEmpty(text)) {
            ToastUtils.showToast(this, "请选择退款原因");
            return;
        }
        if (!TextUtils.isEmpty(text)) {
            paramsMap.put("otherReason", text);
        }
        if (reasonId != -1) {
            paramsMap.put("reasionId", String.valueOf(reasonId));
        }
        paramsMap.put("id", idStr);
        mRxManager.add(Api.getDefault(HostType.HOST_LARK).rebundDesosit(paramsMap).compose(RxSchedulers.<BaseRespose>io_main()).subscribe(new RxSubscriber<BaseRespose>(mContext) {
            @Override
            protected void _onNext(BaseRespose baseRespose) {
                AppLog.print("baseRespose__" + baseRespose);
                if (baseRespose.success()) {
                    CommonUtils.showSuccessDialog(mContext, "退押金申请提交成功", needAmount, "亲，我们已经收到您的退还押金申请，24小时内我们的客服将与您取得联系，请耐心等待");
                } else {
                    ToastUtils.showToast(RebundDepositActivity.this, baseRespose.getMessage());
                }
            }

            @Override
            protected void _onError(String message) {

            }
        }));


    }

    private void getReasonsRxTask() {
        mRxManager.add(Api.getDefault(HostType.HOST_LARK).getRefundReasonItems().compose(RxSchedulers.<BaseRespose<List<ResonItem>>>io_main()).subscribe(new RxSubscriber<BaseRespose<List<ResonItem>>>(this) {
            @Override
            protected void _onNext(BaseRespose<List<ResonItem>> listBaseRespose) {
                updateResonListView(listBaseRespose.getResult());

            }

            @Override
            protected void _onError(String message) {

            }
        }));
    }
}
