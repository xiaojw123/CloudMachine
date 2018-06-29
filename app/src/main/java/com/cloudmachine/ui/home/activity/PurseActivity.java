package com.cloudmachine.ui.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.autonavi.rtbt.IFrameForRTBT;
import com.cloudmachine.R;
import com.cloudmachine.activities.ViewCouponActivityNew;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.bean.Member;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.net.api.ApiConstants;
import com.cloudmachine.ui.home.contract.PurseContract;
import com.cloudmachine.ui.home.model.PurseModel;
import com.cloudmachine.ui.home.presenter.PursePresenter;
import com.cloudmachine.ui.homepage.activity.QuestionCommunityActivity;
import com.cloudmachine.utils.CommonUtils;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MemeberKeeper;
import com.cloudmachine.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 钱包 1余额、押金 2.短信验证 3.支付宝/微信绑定
 */

public class PurseActivity extends BaseAutoLayoutActivity<PursePresenter, PurseModel> implements PurseContract.View {
    public static final String KEY_WALLETAMOUNT = "key_walletamout";
    public static final String KEY_DEPOSITAMOUNT = "key_depositamount";
    public static final int TYPE_ALIPAY = 11;
    public static final int TYPE_WX = 10;
    @BindView(R.id.purse_back_img)
    ImageView purseBackImg;
    @BindView(R.id.purse_balance_tv)
    TextView purseBalanceTv;
    @BindView(R.id.purse_coupon_num_tv)
    TextView purseCouponNumTv;
    @BindView(R.id.purse_coupon_fl)
    FrameLayout purseCouponFl;
    @BindView(R.id.purse_extraction_cash_btn)
    Button purseExtractionCashBtn;
    @BindView(R.id.purse_instruction_img)
    ImageView instructionImg;
    @BindView(R.id.purse_salary_fl)
    FrameLayout salaryFl;
    @BindView(R.id.purse_detail_fl)
    FrameLayout detailFl;
    @BindView(R.id.purse_money_fl)
    FrameLayout moneyFl;
    @BindView(R.id.purse_salary_auth)
    TextView salaryAuthTv;
    Member mMember;
    String walletAmountStr;
    double mDespoitAmout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purse);
        ButterKnife.bind(this);
        mMember = MemeberKeeper.getOauth(mContext);
        if (mMember.isAuth()){
            salaryAuthTv.setText(getResources().getString(R.string.has_auth));
        }
    }




    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getWalletAmount(mMember.getId());
        mPresenter.getAvaildCouponList(mMember.getId());

    }

    @Override
    public void updateWalletAmountView(double walletAmount, double depositAmount) {
        mDespoitAmout = depositAmount;
        walletAmountStr = CommonUtils.formartPrice(String.valueOf(walletAmount));
        purseBalanceTv.setText(walletAmountStr);
        if (walletAmount > 0) {
            purseExtractionCashBtn.setEnabled(true);
        } else {
            purseExtractionCashBtn.setEnabled(false);
        }
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @OnClick({R.id.purse_money_fl,R.id.purse_detail_fl, R.id.purse_salary_fl, R.id.purse_instruction_img, R.id.purse_back_img, R.id.purse_coupon_fl, R.id.purse_extraction_cash_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.purse_money_fl:
                ToastUtils.showToast(this,"尚未开放，敬请期待");

                
                break;
            case R.id.purse_salary_fl:
                if (mMember.isAuth()){
                    boolean isOwner=UserHelper.isOwner(mContext,mMember.getId());
                    if (isOwner){
                        Bundle sb = new Bundle();
                        sb.putString(KEY_WALLETAMOUNT, walletAmountStr);
                        Constants.toActivity(this,SalaryActivity.class,sb);
                    }else{
                        Bundle b=new Bundle();
                        b.putBoolean(IncomeSpendActivity.IS_MEMBER,true);
                        Constants.toActivity(this,IncomeSpendActivity.class,b);
                    }
                }else{
                    Constants.toActivityForR(this, AuthNameActivity.class, null);
                }
                break;


            case R.id.purse_instruction_img:
                Bundle bundle = new Bundle();
                bundle.putString(QuestionCommunityActivity.H5_URL, ApiConstants.AppWalletHelper);
                Constants.toActivity(this, QuestionCommunityActivity.class, bundle);
                break;

            case R.id.purse_detail_fl:
                if (mDespoitAmout > 0) {
                    Constants.toActivity(this, DepositActivity.class, null);
                } else {
                    ToastUtils.showToast(mContext, "没有明细!");
                }
                break;

            case R.id.purse_back_img://返回
                finish();
                break;
            case R.id.purse_coupon_fl://卡券
                Constants.toActivity(this, ViewCouponActivityNew.class, null, false);
                break;
            case R.id.purse_extraction_cash_btn://提现
                Bundle eb = new Bundle();
                eb.putString(KEY_WALLETAMOUNT, walletAmountStr);
                eb.putSerializable(Constants.MC_MEMBER,mMember);
                Constants.toActivity(this, ExtractionCashActivity.class, eb, false);
                break;
        }

    }











    @Override
    public void updateAvaildCouponSumNum(int sumNum) {
        purseCouponNumTv.setText(sumNum + "张");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==AuthNameActivity.AUTH_SUCCESS){
            salaryAuthTv.setText(getResources().getString(R.string.has_auth));
            mMember.setIsAuth(1);
            MemeberKeeper.saveOAuth(mMember,mContext);
        }

    }
}
