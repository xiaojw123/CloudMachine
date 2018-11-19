package com.cloudmachine.ui.home.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;

import com.cloudmachine.R;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.InputMoney;
import com.cloudmachine.utils.ToastUtils;
import com.cloudmachine.utils.widgets.ClearEditTextView;
import com.cloudmachine.widget.CommonTitleView;

/**
 * Created by xiaojw on 2018/11/15.
 */

public class AnnualIncomeActivity extends BaseAutoLayoutActivity implements ClearEditTextView.OnTextChangeListener, View.OnClickListener {
    public static final String ANNUAL_INCOME = "annual_income";
    private ClearEditTextView mIncomeEdt;
    private CommonTitleView mTitleView;
    private String mAnnualIncome;
    private String uniqueNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annualincome);
        mTitleView = (CommonTitleView) findViewById(R.id.annual_income_title);
        mTitleView.setRightClickListener(this);
        mIncomeEdt = (ClearEditTextView) findViewById(R.id.annual_income_edt);
        mIncomeEdt.setOnTextChangeListener(this);
        mIncomeEdt.setFilters(new InputFilter[]{new InputMoney(mIncomeEdt)});
        mIncomeEdt.setFocusable(true);
        mIncomeEdt.setFocusableInTouchMode(true);
        mAnnualIncome = getIntent().getStringExtra(ANNUAL_INCOME);
        uniqueNo = getIntent().getStringExtra(Constants.UNIQUEID);
        if (!TextUtils.isEmpty(mAnnualIncome)) {
            mIncomeEdt.setText(mAnnualIncome);
            mIncomeEdt.setSelection(mAnnualIncome.length());
        }
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void textChanged(Editable s) {
        if (s != null && s.length() > 0) {
            mTitleView.setRightText("提交");
        } else {
            mTitleView.setRightText("");
        }
    }

    @Override
    public void onClick(View v) {
        String annualIncome = mIncomeEdt.getText().toString();
        if (TextUtils.isEmpty(annualIncome)) {
            ToastUtils.showToast(mContext, "年收入不能为空");
            return;
        }
        if (TextUtils.equals(annualIncome, mAnnualIncome)) {
            ToastUtils.showToast(mContext, getResources().getString(R.string.cannot_submit));
            return;
        }
        updatePersonalInfo(InfoAuthActivity.BNS_TYPE_ANNUAL_INCOME, uniqueNo, null, annualIncome, null, null);
    }
}
