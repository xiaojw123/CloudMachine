package com.cloudmachine.ui.home.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.autolayout.widgets.CustomDialog;
import com.cloudmachine.autolayout.widgets.RadiusButtonView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.base.Operator;
import com.cloudmachine.chart.utils.AppLog;
import com.cloudmachine.net.api.ApiConstants;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.DensityUtil;
import com.cloudmachine.utils.InputMoney;
import com.cloudmachine.utils.ToastUtils;
import com.cloudmachine.utils.widgets.ClearEditTextView;
import com.cloudmachine.widget.CommonTitleView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SalaryActivity extends BaseAutoLayoutActivity implements View.OnClickListener, ClearEditTextView.OnTextChangeListener {

    @BindView(R.id.item_collection_per)
    FrameLayout perFl;
    @BindView(R.id.salary_collection_container)
    LinearLayout collectionContainer;
    ArrayList<Operator> mOperatorItems;
    @BindView(R.id.salary_person_num)
    TextView numTv;
    @BindView(R.id.salary_toal_amount)
    TextView toalAmountTv;
    @BindView(R.id.salary_pool_rlt)
    RelativeLayout poolRlt;
    @BindView(R.id.salary_wxpay_flt)
    FrameLayout wxPayFl;
    @BindView(R.id.salary_alipay_flt)
    FrameLayout aliPayFl;
    @BindView(R.id.salary_purse_rlt)
    RelativeLayout purseFl;
    @BindView(R.id.salary_wxpay_cb)
    CheckBox wxPayCb;
    @BindView(R.id.salary_alipay_cb)
    CheckBox aliPayCb;
    @BindView(R.id.salary_purse_cb)
    CheckBox purseCb;
    @BindView(R.id.salary_pay_btn)
    RadiusButtonView payBtn;
    @BindView(R.id.salary_title_tv)
    CommonTitleView titleTv;
    @BindView(R.id.salary_balance_tv)
    TextView balanceTv;


    ArrayList<Operator> receiverList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salary);
        ButterKnife.bind(this);
        if (TextUtils.isEmpty(ApiConstants.AppWagesLoan)) {
            poolRlt.setVisibility(View.GONE);
        } else {
            poolRlt.setVisibility(View.VISIBLE);
        }
        titleTv.setRightClickListener(this);
        titleTv.setLeftClickListener(this);
        payBtn.setOnClickListener(this);
        payBtn.setButtonEnable(false);
        String walletAmount = getIntent().getStringExtra(PurseActivity.KEY_WALLETAMOUNT);
        if (!TextUtils.isEmpty(walletAmount)) {
            balanceTv.setText("余额:" + walletAmount);
        }
    }

    @Override
    public void initPresenter() {

    }


    @OnClick({R.id.item_collection_per, R.id.salary_pool_rlt, R.id.salary_wxpay_flt, R.id.salary_alipay_flt, R.id.salary_purse_rlt})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_titleview_back_img:
                if (mOperatorItems != null && mOperatorItems.size() > 0) {
                    CustomDialog.Builder builder = new CustomDialog.Builder(this);
                    builder.setMessage("确定放弃本次工资的发放吗?");
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    });
                    builder.create().show();
                } else {
                    finish();
                }
                break;

            case R.id.common_titleview_right_tv:
                Constants.toActivity(this, IncomeSpendActivity.class, null);
                break;

            case R.id.radius_button_text:
                if (payBtn.isButtonEanble()) {
                    String amountText = toalAmountTv.getText().toString();
                    double amount = Double.parseDouble(amountText);
                    if (amount > 0) {
                        Bundle data = new Bundle();
                        int payType = 0;
                        if (aliPayCb.isChecked()) {
                            payType = Constants.PAY_TYPE_ALIPAY;
                        } else if (wxPayCb.isChecked()) {
                            payType = Constants.PAY_TYPE_WX;
                        } else if (purseCb.isChecked()) {
                            payType = Constants.PAY_TYPE_PURSE;
                        }
                        data.putInt(Constants.P_PAYTYPE, payType);
                        data.putString(Constants.P_PAYAMOUNT, amountText);
                        data.putParcelableArrayList(Constants.P_RECEIVERLIST, receiverList);
                        Constants.toActivityForR(this, PayInfoActivity.class, data);
                    } else {
                        ToastUtils.showToast(mContext, "金额大于0才可支付！");
                    }
                }
                break;

            case R.id.item_collection_per:
                Constants.toActivityForR(this, PersonChooseActivity.class, null);
                break;
            case R.id.item_collection_del:
                final Operator item = (Operator) v.getTag(R.id.operator_item);
                final View itemView = (View) v.getTag();
                showDeleteDialog(item, itemView);
                break;
            case R.id.coll_add_tv:
                Bundle b1 = new Bundle();
                b1.putParcelableArrayList(Constants.OPERATOR_LIST, mOperatorItems);
                Constants.toActivityForR(this, PersonChooseActivity.class, b1);
                break;
            case R.id.salary_pool_rlt:
                Bundle b2 = new Bundle();
                b2.putString(QuestionCommunityActivity.H5_URL, ApiConstants.AppWagesLoan);
                Constants.toActivity(this, QuestionCommunityActivity.class, b2);
                break;
            case R.id.salary_wxpay_flt:
                if (wxPayCb.isChecked()) {
                    return;
                }
                wxPayCb.setChecked(true);
                aliPayCb.setChecked(false);
                purseCb.setChecked(false);
                payBtn.setTextColor(getResources().getColor(R.color.cor15));
                payBtn.setButtonEnable(true);
                break;
            case R.id.salary_alipay_flt:
                if (aliPayCb.isChecked()) {
                    return;
                }
                wxPayCb.setChecked(false);
                aliPayCb.setChecked(true);
                purseCb.setChecked(false);
                payBtn.setTextColor(getResources().getColor(R.color.cor15));
                payBtn.setButtonEnable(true);
                break;
            case R.id.salary_purse_rlt:
                if (purseCb.isChecked()) {
                    return;
                }
                wxPayCb.setChecked(false);
                aliPayCb.setChecked(false);
                purseCb.setChecked(true);
                payBtn.setTextColor(getResources().getColor(R.color.cor15));
                payBtn.setButtonEnable(true);
                break;

        }


    }

    private void showDeleteDialog(final Operator item, final View itemView) {
        CustomDialog.Builder builder = new CustomDialog.Builder(mContext);
        builder.setMessage(Html.fromHtml("确认删除收款人<font color=\"#ff8901\">\"" + item.getName() + "\"</font>"));
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(getResources().getColor(R.color.c_ff8901), "删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                int len;
                if (mOperatorItems != null) {
                    len = mOperatorItems.size();
                    if (len > 0) {
                        mOperatorItems.remove(item);
                        collectionContainer.removeView(itemView);
                        numTv.setText(String.valueOf(mOperatorItems.size()));
                        textChanged(null);
                    }
                }
            }
        });
        builder.create().show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == PersonChooseActivity.SEL_OPERATOR_COMPLETE) {
            mOperatorItems = data.getParcelableArrayListExtra(Constants.OPERATOR_LIST);
            updateCollItemView();
        } else if (resultCode == PayInfoActivity.SUCCESS_SALARY_PAY) {
            int len;
            if (mOperatorItems != null) {
                len = mOperatorItems.size();
                if (len > 0) {
                    mOperatorItems.clear();
                    receiverList.clear();
                    collectionContainer.removeAllViews();
                    View view = LayoutInflater.from(this).inflate(R.layout.item_salary_collection, null);
                    FrameLayout perLayout = (FrameLayout) view.findViewById(R.id.item_collection_per);
                    perLayout.setOnClickListener(this);
                    collectionContainer.addView(view);
                    numTv.setText(String.valueOf(mOperatorItems.size()));
                    toalAmountTv.setText(String.valueOf(0));
                }
            }

        }

    }

    @SuppressWarnings("ResourceType")
    private void updateCollItemView() {
        if (mOperatorItems != null) {
            int len = mOperatorItems.size();
            if (len > 0) {
                collectionContainer.removeAllViews();
                for (int i = 0; i < len; i++) {
                    Operator item = mOperatorItems.get(i);
                    if (item != null) {
                        View itemView = View.inflate(mContext, R.layout.item_salary_collection, null);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        if (i > 0) {
                            params.topMargin = DensityUtil.dip2px(mContext, 10);
                        }
                        TextView nameTv = (TextView) itemView.findViewById(R.id.item_collection_name);
                        ImageView delImg = (ImageView) itemView.findViewById(R.id.item_collection_del);
                        TextView recordTv = (TextView) itemView.findViewById(R.id.item_collection_record);
                        ImageView arrowImg = (ImageView) itemView.findViewById(R.id.item_collection_arrow);
                        ClearEditTextView collEdt = (ClearEditTextView) itemView.findViewById(R.id.item_collection_edt);
                        View blineView = itemView.findViewById(R.id.item_collection_bline);
                        collEdt.setFilters(new InputFilter[]{new InputMoney(collEdt)});
                        collEdt.setFocusable(true);
                        collEdt.setFocusableInTouchMode(true);
                        collEdt.setOnTextChangeListener(this);
                        collEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                            @Override
                            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                AppLog.print("onEditorAction editonId__" + actionId);
                                return false;
                            }
                        });
                        collEdt.setTag(item);
                        arrowImg.setVisibility(View.GONE);
                        delImg.setVisibility(View.VISIBLE);
                        delImg.setOnClickListener(this);
                        delImg.setTag(itemView);
                        delImg.setTag(R.id.operator_item, item);
                        String gmtCreate = item.getGmtCreate();
                        nameTv.setText(item.getName());
                        if (!TextUtils.isEmpty(gmtCreate)) {
                            recordTv.setText(Html.fromHtml("上次发放:<font color=\"#333333\">" + item.getSalaryAmount() + "</font>(" + gmtCreate + ")"));
                        }
                        if (i == len - 1) {
                            blineView.setVisibility(View.VISIBLE);
                        } else {
                            blineView.setVisibility(View.GONE);
                        }
                        collectionContainer.addView(itemView, params);
                    }
                }
                TextView collTv = new TextView(mContext);
                collTv.setBackgroundColor(Color.WHITE);
                collTv.setText("+添加收款人");
                collTv.setGravity(Gravity.CENTER);
                collTv.setTextColor(getResources().getColor(R.color.cor8));
                collTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                collTv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(mContext, 50)));
                collTv.setId(R.id.coll_add_tv);
                collTv.setOnClickListener(this);
                collectionContainer.addView(collTv);
                numTv.setText(String.valueOf(len));
            }
        }
    }

    @Override
    public void textChanged(Editable s) {
        double toalAmount = 0;
        receiverList.clear();
        for (int i = 0; i < collectionContainer.getChildCount() - 1; i++) {
            ViewGroup itemCotainer = (ViewGroup) collectionContainer.getChildAt(i);
            ClearEditTextView editText = (ClearEditTextView) itemCotainer.findViewById(R.id.item_collection_edt);
            if (editText != null) {
                String amountText = editText.getText().toString();
                if (!TextUtils.isEmpty(amountText)) {
                    Operator item = (Operator) editText.getTag();
                    item.setAmount(amountText);
                    receiverList.add(item);
                    double itemAmout = Double.parseDouble(amountText);
                    toalAmount += itemAmout;
                }
            }
        }
        toalAmountTv.setText(String.valueOf(toalAmount));

    }

}
