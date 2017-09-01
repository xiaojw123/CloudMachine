package com.cloudmachine.wxapi;


import android.content.Intent;
import android.os.Bundle;

import com.cloudmachine.activities.RepairPayDetailsActivity;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.ui.homepage.activity.QuestionCommunityActivity;
import com.cloudmachine.utils.Constants;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;



public class WXPayEntryActivity extends BaseAutoLayoutActivity implements IWXAPIEventHandler{
	
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.pay_result);
        
    	api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.handleIntent(getIntent(), this);
    }

	@Override
	public void initPresenter() {

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		//Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			if (resp.errCode == 0) {
				Constants.ToastAction("支付成功");
				mRxManager.post(RepairPayDetailsActivity.FINISH_PAY_DETAIL,null);
				mRxManager.post(QuestionCommunityActivity.GO_TO_MY_ORDER,null);
                finish();
//				Bundle b = new Bundle();
//				b.putString("paymentResult","支付成功");


//				Constants.toActivity(WXPayEntryActivity.this, PaymentResultsActivity.class,b,true);
			} else {
				Constants.ToastAction("支付失败");
				mRxManager.post(RepairPayDetailsActivity.FINISH_PAY_DETAIL,null);
                finish();
//				Bundle b = new Bundle();
//				b.putString("paymentResult","支付失败");
//				Constants.toActivity(WXPayEntryActivity.this, PaymentResultsActivity.class, b, true);
			}
		}

		/*if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.app_tip);
			builder.setMessage(getString(R.string.pay_result_callback_msg, String.valueOf(resp.errCode)));
			builder.show();
		}*/
	}
}