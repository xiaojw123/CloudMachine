package com.cloudmachine.ui.home.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.bean.LarkMemberInfo;
import com.cloudmachine.bean.Member;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.net.api.ApiConstants;
import com.cloudmachine.ui.home.contract.PurseContract;
import com.cloudmachine.ui.home.model.PurseModel;
import com.cloudmachine.ui.home.presenter.PursePresenter;
import com.cloudmachine.utils.CommonUtils;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MemeberKeeper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 钱包 1余额、押金 2.短信验证 3.支付宝/微信绑定
 */

public class PurseActivity extends BaseAutoLayoutActivity<PursePresenter, PurseModel> implements PurseContract.View {
    public static final String KEY_WALLETAMOUNT = "key_walletamout";
    @BindView(R.id.purse_back_img)
    ImageView purseBackImg;
    @BindView(R.id.purse_balance_tv)
    TextView purseBalanceTv;
    @BindView(R.id.purse_extraction_cash_btn)
    Button purseExtractionCashBtn;
    @BindView(R.id.purse_instruction_img)
    ImageView instructionImg;
    @BindView(R.id.purse_salary_fl)
    FrameLayout salaryFl;
    @BindView(R.id.purse_detail_fl)
    FrameLayout detailFl;
    @BindView(R.id.purse_salary_auth)
    TextView salaryAuthTv;
    @BindView(R.id.purse_ticket_fl)
    FrameLayout ticketFl;
    @BindView(R.id.ticket_line)
    View ticketLine;
    @BindView(R.id.purse_paycode_fl)
    FrameLayout paycodeFl;
    Member mMember;
    String walletAmountStr;
    String mJumpUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purse);
        ButterKnife.bind(this);
        mMember = MemeberKeeper.getOauth(mContext);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getMemberInfo();
        mPresenter.getWalletAmount();

    }

    @Override
    public void updateWalletAmountView(double walletAmount, String jumpUrl) {
        mJumpUrl = jumpUrl;
        walletAmountStr = CommonUtils.formartPrice(String.valueOf(walletAmount));
        purseBalanceTv.setText(walletAmountStr);
        if (walletAmount > 0) {
            purseExtractionCashBtn.setEnabled(true);
        } else {
            purseExtractionCashBtn.setEnabled(false);
        }
        if (!TextUtils.isEmpty(mJumpUrl)) {
            ticketFl.setVisibility(View.VISIBLE);
            ticketLine.setVisibility(View.VISIBLE);
        } else {
            ticketFl.setVisibility(View.GONE);
            ticketLine.setVisibility(View.GONE);
        }
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @OnClick({R.id.purse_paycode_fl, R.id.purse_ticket_fl, R.id.purse_detail_fl, R.id.purse_salary_fl, R.id.purse_instruction_img, R.id.purse_back_img, R.id.purse_extraction_cash_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.purse_paycode_fl:
                Constants.toActivity(this, PayCodeActivity.class, null);
                break;
            case R.id.purse_ticket_fl:
                Bundle qcBundle = new Bundle();
                qcBundle.putString(QuestionCommunityActivity.H5_URL, mJumpUrl);
                Constants.toActivity(this, QuestionCommunityActivity.class, qcBundle);
                break;

            case R.id.purse_salary_fl:
                if (mMember.isAuth()) {
                    boolean isOwner = UserHelper.isOwner(mContext, mMember.getId());
                    if (isOwner) {
                        Bundle sb = new Bundle();
                        sb.putString(KEY_WALLETAMOUNT, walletAmountStr);
                        Constants.toActivity(this, SalaryActivity.class, sb);
                    } else {
                        Bundle b = new Bundle();
                        b.putBoolean(IncomeSpendActivity.IS_MEMBER, true);
                        Constants.toActivity(this, IncomeSpendActivity.class, b);
                    }
                } else {
                    Constants.toActivity(this, AuthPersonalInfoActivity.class, null);
                }
                break;


            case R.id.purse_instruction_img:
                Bundle bundle = new Bundle();
                bundle.putString(QuestionCommunityActivity.H5_URL, ApiConstants.AppWalletHelper);
                Constants.toActivity(this, QuestionCommunityActivity.class, bundle);
                break;

            case R.id.purse_detail_fl:
                Constants.toActivity(this, DepositActivity.class, null);
                break;

            case R.id.purse_back_img://返回
                finish();
                break;
            case R.id.purse_extraction_cash_btn://提现
                if (mMember.isAuth()) {
                    Bundle eb = new Bundle();
                    eb.putString(KEY_WALLETAMOUNT, walletAmountStr);
                    eb.putSerializable(Constants.MC_MEMBER, mMember);
                    Constants.toActivity(this, ExtractionCashActivity.class, eb, false);
                } else {
                    Constants.toActivity(this, AuthPersonalInfoActivity.class, null);
                }
                break;
        }

    }


    @Override
    public void updateMemberInfo(LarkMemberInfo info) {
        mMember = CommonUtils.convertMember(info);
        if (mMember.isAuth()) {
            salaryAuthTv.setText(getResources().getString(R.string.has_auth));
        }
        MemeberKeeper.saveOAuth(mMember, mContext);
    }
}
