package com.cloudmachine.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.cloudmachine.R;
import com.cloudmachine.adapter.DeliveryMethodAdapter;
import com.cloudmachine.adapter.GetCouponAdapter;
import com.cloudmachine.adapter.SetupTimeAdapter;
import com.cloudmachine.alipay.PayResult;
import com.cloudmachine.app.MyApplication;
import com.cloudmachine.autolayout.widgets.TitleView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.net.task.GetDeliveryMethodAsync;
import com.cloudmachine.net.task.GetSetupTimeAsync;
import com.cloudmachine.net.task.PayPriceAsync;
import com.cloudmachine.net.task.YunBoxPayAsync;
import com.cloudmachine.struc.AliPayBean;
import com.cloudmachine.struc.CouponInfo;
import com.cloudmachine.struc.DeliveryMethodInfo;
import com.cloudmachine.struc.EditListInfo;
import com.cloudmachine.struc.MachineTypeInfo;
import com.cloudmachine.struc.PayPriceInfo;
import com.cloudmachine.struc.ResidentAddressInfo;
import com.cloudmachine.struc.SetUpTimeInfo;
import com.cloudmachine.struc.WeiXinEntityBean;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.widgets.ClearEditTextView;
import com.rey.material.app.BottomSheetDialog;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.ArrayList;
import java.util.Map;


/**
 * 项目名称：CloudMachine
 * 类描述：购买云盒子确认信息页面
 * 创建人：shixionglu
 * 创建时间：2017/2/7 上午10:16
 * 修改人：shixionglu
 * 修改时间：2017/2/7 上午10:16
 * 修改备注：
 */

public class ConfirmInformationActivity extends BaseAutoLayoutActivity implements View.OnClickListener, Handler.Callback {

    private RelativeLayout        mRlBrand;
    private RelativeLayout        mRlModel;
    private RelativeLayout        mRlContactName;
    private RelativeLayout        mRlPhoneNumber;
    private RelativeLayout        mRlLocation;
    private RelativeLayout        mRlStreetAddress;
    private RelativeLayout        mRlWeiXinPay;
    private RelativeLayout        mRlAliPay;
    private RelativeLayout        mRlCoupon;
    private RelativeLayout        mRlDistributionMode;
    private RelativeLayout        mRlInstallationTime;
    private Button                mBtnToPay;
    private CheckBox              mCbAliPay;
    private CheckBox              mCbWeiXinPay;
    private Context               mContext;
    private Handler               mHandler;
    private String                pk_prod_def;//机器类型nc号码
    private TextView              deviceBrand;
    private String                pk_brand;
    private TextView              deviceModel;
    private TitleView             titleLayout;
    private ClearEditTextView     etConfirmNickName;
    private String                nickname;
    private ClearEditTextView     etConfirmPhoneNumber;
    private String                mobile;
    private TextView              installLocation;
    private TextView              installStreetAddress;
    private ListView              lvCoupon;
    private ArrayList<CouponInfo> dataList;
    private GetCouponAdapter      infosAdapter;
    private int couponCount = 1;
    private int                   couponHeight;
    private RelativeLayout        root;
    private Button                btnCancel;
    private BottomSheetDialog     couponDialog;
    private int                   height;
    private RelativeLayout        rlDeliveryMethod;
    private Button                btnComplete;
    private ListView              lvDeliveryMethod;
    private DeliveryMethodAdapter deliveryMethodAdapter;
    private int deliveryMethodCount = 1;
    private int               deliveryHeight;
    private BottomSheetDialog deliveryMethodDialog;
    private int               height1;
    private RelativeLayout    rlSetuptime;
    private Button            btnSetupTimeComplete;
    private ListView          lvSetupTime;
    private int setupTimeCount = 1;
    private SetupTimeAdapter              setupTimeAdapter;
    private int                           setupTimeHeight;
    private BottomSheetDialog             setupTimeDialog;
    private TextView                      tvBrand;
    private TextView                      tvModel;
    private TextView                      tvCoupon;
    private TextView                      tvDeliveryMethod;
    private TextView                      tvSetupTime;
    private RelativeLayout                rlType;
    private TextView                      tvType;
    private long                          typeId;
    private long                          brandId;
    private long                          modelId;
    private EditListInfo                  eInfoType;
    private EditListInfo                  eInfoBrand;
    private EditListInfo                  eInfoModel;
    private ResidentAddressInfo           addressInfo;
    private ArrayList<DeliveryMethodInfo> deliveryMethodData;
    private ArrayList<SetUpTimeInfo>      setupTimeData;
    private long                          deliveryMethodId;
    private long                 setupTimeId;
    private ArrayList<CouponInfo> couponData;
    private String               province;
    private String               position;
    private String               city;
    private String               district;
    private String               title;
    private String               custname;
    private String               custtel;
    private String               payType;
    private String               sign;
    private static final int SDK_PAY_FLAG = 1;


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                       // Toast.makeText(ConfirmInformationActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        Bundle b = new Bundle();
                        b.putString("paymentResult", "支付成功");
                        Constants.toActivity(ConfirmInformationActivity.this,PaymentResultsActivity.class,b,true);
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        //Toast.makeText(ConfirmInformationActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                        Bundle b = new Bundle();
                        b.putString("paymentResult","支付失败");
                        Constants.toActivity(ConfirmInformationActivity.this,PaymentResultsActivity.class,b,false);
                    }
                    break;
                }
                default:
                    break;
            }
        };
    };
    private String partnerId;
    private String prepayId;
    private String nonceStr;
    private String timestamp;
    private String sign1;
    private String amount;
    private TextView tvPayPrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirminformation);
        mContext = this;
        mHandler = new Handler(this);
        couponData = new ArrayList<>();
        deliveryMethodData = new ArrayList<>();
        setupTimeData = new ArrayList<>();
        initView();
        initSetupTimeView();
    }

    @Override
    public void initPresenter() {

    }


    private void initView() {
        initTitleView();
        tvPayPrice = (TextView) findViewById(R.id.tv_pay_price);
        installLocation = (TextView) findViewById(R.id.install_location);
        installStreetAddress = (TextView) findViewById(R.id.install_street_address);
        //联系人姓名
        etConfirmNickName = (ClearEditTextView) findViewById(R.id.et_confirm_nickname);
        nickname = MyApplication.getInstance().getTempMember().getNickname();
        if (!TextUtils.isEmpty(nickname)) {
            etConfirmNickName.setPadding(0, 0, dip2px(40), 0);
            etConfirmNickName.setText(nickname);
        }
        //手机号码
        etConfirmPhoneNumber = (ClearEditTextView) findViewById(R.id.et_confirm_phone_number);
        mobile = MyApplication.getInstance().getTempMember().getMobile();
        if (!TextUtils.isEmpty(mobile)) {
            etConfirmPhoneNumber.setText(mobile);
        }

        deviceBrand = (TextView) findViewById(R.id.device_brand);
        deviceModel = (TextView) findViewById(R.id.device_model);
        rlType = (RelativeLayout) findViewById(R.id.rl_type);
        rlType.setOnClickListener(this);
        tvType = (TextView) findViewById(R.id.select0);
        mRlBrand = (RelativeLayout) findViewById(R.id.rl_brand);
        mRlBrand.setOnClickListener(this);
        mRlModel = (RelativeLayout) findViewById(R.id.rl_model);
        mRlModel.setOnClickListener(this);
        mRlContactName = (RelativeLayout) findViewById(R.id.rl_contact_name);
        mRlContactName.setOnClickListener(this);
        mRlPhoneNumber = (RelativeLayout) findViewById(R.id.rl_phone_number);
        mRlPhoneNumber.setOnClickListener(this);
        mRlLocation = (RelativeLayout) findViewById(R.id.rl_location);
        mRlLocation.setOnClickListener(this);
        mRlStreetAddress = (RelativeLayout) findViewById(R.id.rl_street_adress);
        mRlStreetAddress.setOnClickListener(this);
        mRlWeiXinPay = (RelativeLayout) findViewById(R.id.rl_weixin_pay);
        mRlWeiXinPay.setOnClickListener(this);
        mRlAliPay = (RelativeLayout) findViewById(R.id.rl_alipay);
        mRlAliPay.setOnClickListener(this);
        mRlCoupon = (RelativeLayout) findViewById(R.id.rl_coupon);
        mRlCoupon.setOnClickListener(this);
        mRlDistributionMode = (RelativeLayout) findViewById(R.id.rl_distribution_mode);
        mRlDistributionMode.setOnClickListener(this);
        mRlInstallationTime = (RelativeLayout) findViewById(R.id.rl_installation_time);
        mRlInstallationTime.setOnClickListener(this);
        mBtnToPay = (Button) findViewById(R.id.btn_topay);
        mBtnToPay.setOnClickListener(this);
        mCbAliPay = (CheckBox) findViewById(R.id.cb_alipay);
        mCbWeiXinPay = (CheckBox) findViewById(R.id.cb_weixin_pay);
        tvBrand = (TextView) findViewById(R.id.select1);
        tvModel = (TextView) findViewById(R.id.select2);
        tvCoupon = (TextView) findViewById(R.id.select3);
        tvCoupon.setText("无可用");
        tvDeliveryMethod = (TextView) findViewById(R.id.select4);
        tvSetupTime = (TextView) findViewById(R.id.select5);
        //new GetMyCouponAsync(mHandler, mContext, 1).execute();
      //  new GetCouponAsync(mHandler, "0", null, mContext).execute();
        new GetDeliveryMethodAsync(mHandler, mContext).execute();
        new GetSetupTimeAsync(mHandler, mContext).execute();
        new PayPriceAsync(mContext,mHandler,null,"301","-1").execute();
    }

    private void initTitleView() {

        titleLayout = (TitleView) findViewById(R.id.title_layout);
        titleLayout.setLeftImage(-1, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        titleLayout.setTitle("确认信息");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_type:
                gotoEditActivity(Constants.E_DEVICE_LIST,
                        Constants.E_ITEMS_category, "", "", "", "机型");
                break;
            case R.id.rl_brand://品牌
                if (!TextUtils.isEmpty(pk_prod_def)) {
                    gotoEditActivity(Constants.E_DEVICE_LIST, Constants.E_ITEMS_brand,
                            pk_prod_def, "", "", "品牌");
                } else {
                    Constants.ToastAction("请先选择机器类型");
                }
                break;
            case R.id.rl_model://型号
                if (!TextUtils.isEmpty(pk_prod_def) && !TextUtils.isEmpty(pk_brand)) {
                    gotoEditActivity(Constants.E_DEVICE_LIST, Constants.E_ITEMS_model,
                            pk_prod_def, pk_brand, "", "型号");
                } else {
                    Constants.ToastAction("请先选择品牌");
                }

                break;
            case R.id.rl_contact_name://联系人姓名
                break;
            case R.id.rl_phone_number://电话号码
                break;
            case R.id.rl_location://所在地区
                Intent intent = new Intent(this, SearchPoiActivity.class);
                startActivityForResult(intent, Constants.REQUEST_ToSearchActivity);
                break;
            case R.id.rl_street_adress://街道地址
                break;
            case R.id.rl_weixin_pay://微信支付
                mCbWeiXinPay.setChecked(!mCbWeiXinPay.isChecked());
                mCbAliPay.setChecked(!mCbWeiXinPay.isChecked());
                break;
            case R.id.rl_alipay://支付宝支付
                mCbAliPay.setChecked(!mCbAliPay.isChecked());
                mCbWeiXinPay.setChecked(!mCbAliPay.isChecked());
                break;
            case R.id.rl_coupon://优惠券
               // showCounponDialog();
               // Constants.MyLog(String.valueOf(MemeberKeeper.getOauth(mContext).getId()) + "得到的memberId");
                break;
            case R.id.rl_distribution_mode://配送方式
                showDistributionModeDialog();
                break;
            case R.id.rl_installation_time://安装时间
                showSetupTimeDialog();
                break;
            case R.id.btn_topay://去付款

                if (null == eInfoType) {
                    Constants.ToastAction("机器类型不能为空");
                    return;
                }
                if (null == eInfoBrand) {
                    Constants.ToastAction("机器品牌不能为空");
                    return;
                }
                if (null == eInfoModel) {
                    Constants.ToastAction("机器型号不能为空");
                    return;
                }
                if (TextUtils.isEmpty(etConfirmNickName.getText())) {
                    Constants.ToastAction("联系人姓名不能为空");
                    return;
                }
                if (TextUtils.isEmpty(etConfirmPhoneNumber.getText())) {
                    Constants.ToastAction("手机号码不能为空");
                    return;
                }
                if (null == addressInfo) {
                    Constants.ToastAction("请选择所在地区和街道地址");
                    return;
                }
                if (!mCbAliPay.isChecked() && !mCbWeiXinPay.isChecked()) {
                    Constants.ToastAction("请选择一种支付方式");
                    return;
                }
                if (null == deliveryMethodData) {
                    Constants.ToastAction("配送方式不能为空");
                    return;
                }
                if (null == setupTimeData) {
                    Constants.ToastAction("安装时间不能为空");
                    return;
                }
                if (TextUtils.isEmpty(position)) {
                    Constants.ToastAction("配送地址不能为空");
                    return;
                }
                if (TextUtils.isEmpty(province)) {
                    Constants.ToastAction("所选省份不能为空");
                    return;
                }

                if (TextUtils.isEmpty(etConfirmNickName.getText().toString())) {
                    Constants.ToastAction("联系人姓名不能为空");
                    return;
                }
                if (TextUtils.isEmpty(etConfirmPhoneNumber.getText().toString())) {
                    Constants.ToastAction("手机号码不能为空");
                    return;
                }
                if (mCbWeiXinPay.isChecked()) {
                    payType = "101";
                } else if (mCbAliPay.isChecked()) {
                    payType = "102";
                }
                custname = etConfirmNickName.getText().toString();
                custtel = etConfirmPhoneNumber.getText().toString();
                //购买云盒子支付签名
                new YunBoxPayAsync(mHandler, mContext, typeId, brandId, modelId, position, custname
                        , custtel, payType, (int) deliveryMethodId, (int) setupTimeId, province, "00000").execute();
                break;
            case R.id.btn_complete:

                break;
            default:
                break;
        }
    }

    //初始化安装时间
    private void initSetupTimeView() {

        rlSetuptime = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.dialog_setuptime, null);
        btnSetupTimeComplete = (Button) rlSetuptime.findViewById(R.id.btn_complete);
        btnSetupTimeComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupTimeDialog.dismiss();
            }
        });
        lvSetupTime = (ListView) rlSetuptime.findViewById(R.id.lv_setuptime);

        setupTimeAdapter = new SetupTimeAdapter(mContext, setupTimeData);
        lvSetupTime.setAdapter(setupTimeAdapter);
        setupTimeAdapter.notifyDataSetChanged();
        lvSetupTime.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setupTimeId = setupTimeData.get(position).getId();
                setupTimeDialog.dismiss();
                Constants.MyLog("配送时间id" + setupTimeId);
                tvSetupTime.setText(setupTimeData.get(position).getName());
            }
        });
    }

    //初始化配送方式
    private void initDistributeView() {

        rlDeliveryMethod = (RelativeLayout) LayoutInflater.from(ConfirmInformationActivity.this).inflate(R.layout.dialog_deliverymethod, null);
        btnComplete = (Button) rlDeliveryMethod.findViewById(R.id.btn_complete);
        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deliveryMethodDialog.dismiss();
            }
        });
        lvDeliveryMethod = (ListView) rlDeliveryMethod.findViewById(R.id.lv_deliverymethod);

        deliveryMethodAdapter = new DeliveryMethodAdapter(mContext, deliveryMethodData);
        lvDeliveryMethod.setAdapter(deliveryMethodAdapter);
        deliveryMethodAdapter.notifyDataSetChanged();
        lvDeliveryMethod.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                deliveryMethodId = deliveryMethodData.get(position).getId();
                deliveryMethodDialog.dismiss();
                Constants.MyLog("配送方式" + deliveryMethodId);
                tvDeliveryMethod.setText(deliveryMethodData.get(position).getName());
            }
        });
    }


    //初始化优惠券弹出框页面布局
    private void initCouponView() {

        root = (RelativeLayout) LayoutInflater.from(ConfirmInformationActivity.this).
                inflate(R.layout.dialog_counpon, null);
        btnCancel = (Button) root.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                couponDialog.dismiss();
            }
        });
        lvCoupon = (ListView) root.findViewById(R.id.lv_coupon);
        infosAdapter = new GetCouponAdapter(mContext, couponData, 0);
        lvCoupon.setAdapter(infosAdapter);
        infosAdapter.notifyDataSetChanged();
        lvCoupon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String couponId = String.valueOf(couponData.get(position).getCouponId());
                couponDialog.dismiss();
                Constants.MyLog("优惠券id" + couponId);
            }
        });

    }


    //选择配送方式弹窗
    private void showDistributionModeDialog() {
        int measureHeight = GetViewHeight(R.layout.dialog_deliverymethod);
        int itemDeliveryHeight = GetViewHeight(R.layout.list_item_deliverymethod);
        if (deliveryMethodCount <= 0) {
            return;
        }
        int totalHeight = measureHeight + deliveryMethodCount * itemDeliveryHeight + dip2px(60);
        height = getScreenHeight();
        if (totalHeight > (height / 3) * 2) {
            deliveryHeight = (height / 3) * 2;
        } else {
            deliveryHeight = totalHeight;
        }

        initDistributeView();

        deliveryMethodDialog = new BottomSheetDialog(ConfirmInformationActivity.this);
        deliveryMethodDialog.contentView(rlDeliveryMethod)
                .heightParam(deliveryHeight)
                .inDuration(500)
                .outDuration(500)
                .inInterpolator(new BounceInterpolator())
                .outInterpolator(new AnticipateInterpolator())
                .show();
    }

    private void showSetupTimeDialog() {
        int measureHeight = GetViewHeight(R.layout.dialog_setuptime);
        int itemDeliveryHeight = GetViewHeight(R.layout.list_item_deliverymethod);
        int totalHeight = measureHeight + setupTimeCount * itemDeliveryHeight + dip2px(70);
        height = getScreenHeight();
        if (totalHeight > (height / 3) * 2) {
            setupTimeHeight = (height / 3) * 2;
        } else {
            setupTimeHeight = totalHeight;
        }
        initSetupTimeView();
        setupTimeDialog = new BottomSheetDialog(ConfirmInformationActivity.this);
        setupTimeDialog.contentView(rlSetuptime)
                .heightParam(setupTimeHeight)
                .inDuration(500)
                .outDuration(500)
                .inInterpolator(new BounceInterpolator())
                .outInterpolator(new AnticipateInterpolator())
                .show();
    }

    //弹出优惠券弹窗
    private void showCounponDialog() {
        //查看优惠券弹窗除listview其余部分的高度
        int measureHeight = GetViewHeight(R.layout.dialog_counpon);
        //listview单个条目的高度
        int itemCouponHeight = GetViewHeight(R.layout.list_item_coupon);
        //Constants.MyLog("条目数量"+couponCount);
        if (couponCount <= 0) {
            return;
        }
        //总体高度
        int totalHeight = measureHeight + couponCount * itemCouponHeight + dip2px(60);
        //屏幕高度
        height = getScreenHeight();
        if (totalHeight > (height / 3) * 2) {
            couponHeight = (height / 3) * 2;
        } else {
            couponHeight = totalHeight;
        }
        initCouponView();
        couponDialog = new BottomSheetDialog(ConfirmInformationActivity.this);
        couponDialog.contentView(root)
                .heightParam(couponHeight)
                .inDuration(500)
                .outDuration(500)
                .inInterpolator(new BounceInterpolator())
                .outInterpolator(new AnticipateInterpolator())
                .show();
    }

    // v1用来传nc类型id，v2用来传nc品牌id，v4用来传类型id
    private void gotoEditActivity(int editType, int itemType, String v1,
                                  String v2, String v3, String titleName) {

        Bundle bundle = new Bundle();
        bundle.putString(Constants.P_TITLETEXT, titleName);
        bundle.putInt(Constants.P_EDITTYPE, editType);
        bundle.putInt(Constants.P_ITEMTYPE, itemType);
        bundle.putString(Constants.P_EDIT_LIST_VALUE1, v1);
        bundle.putString(Constants.P_EDIT_LIST_VALUE2, v2);
        Constants.toActivityForR(this, EditLayoutActivity.class, bundle, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Constants.REQUEST_ToSearchActivity) {
            if (null != data && null != data.getExtras()) {
                addressInfo = (ResidentAddressInfo) data.getExtras().getSerializable(Constants.P_SEARCHINFO);
                if (null != addressInfo) {
                    province = addressInfo.getProvince();
                    position = addressInfo.getPosition();
                    city = addressInfo.getCity();
                    district = addressInfo.getDistrict();
                    title = addressInfo.getTitle();
                    installLocation.setText(null != province ? province : "" + " " + null != city ? city : "");
                    installStreetAddress.setText(null != position ? position : "" + " " + null != title ? title : "");
                }
            }
            return;
        }


        switch (resultCode) {
            case RESULT_OK:
                Bundle bundle = data.getExtras();
                int editType = bundle.getInt(Constants.P_EDITTYPE);
                int itemType = bundle.getInt(Constants.P_ITEMTYPE);
                switch (itemType) {
                    case Constants.E_ITEMS_category:
                        eInfoType = (EditListInfo) bundle
                                .getSerializable(Constants.P_EDITRESULTITEM);
                        if (null != eInfoType) {
                            tvType.setText(eInfoType.getName());
                            pk_prod_def = eInfoType.getPK_PROD_DEF();
                            typeId = eInfoType.getId();
                        }
                        break;
                    case Constants.E_ITEMS_brand:
                        eInfoBrand = (EditListInfo) bundle
                                .getSerializable(Constants.P_EDITRESULTITEM);
                        if (null != eInfoBrand) {
                            tvBrand.setText(eInfoBrand.getName());
                            pk_brand = eInfoBrand.getPK_BRAND();
                            brandId = eInfoBrand.getId();
                        }

                        break;
                    case Constants.E_ITEMS_model:
                        eInfoModel = (EditListInfo) bundle
                                .getSerializable(Constants.P_EDITRESULTITEM);
                        if (null != eInfoModel) {
                            tvModel.setText(eInfoModel.getName());
                            modelId = eInfoModel.getId();
                        }
                        break;
                }
                break;
            default:
                break;
        }

    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case Constants.HANDLER_GETMACHINETYPES_SUCCESS:
                ArrayList<MachineTypeInfo> mTypeInfo = (ArrayList<MachineTypeInfo>) msg.obj;
                if (null != mTypeInfo && mTypeInfo.size() > 0) {
                    MachineTypeInfo mInfo = mTypeInfo.get(0);
                    if (null != mInfo) {
                        String deviceTypeName = mInfo.getName();
                        pk_prod_def = mInfo.getPk_PROD_DEF();
                    }
                }
                break;
            case Constants.HANDLER_GETMACHINETYPES_FAIL:
                Constants.ToastAction((String) msg.obj);
                break;
            case Constants.HANDLER_GETCOUPONS_SUCCESS:
                ArrayList<CouponInfo> data = (ArrayList<CouponInfo>) msg.obj;
                if (null != data) {
                    couponData.addAll(data);
                    couponCount = couponData.size();
                }
                if (null != data && data.size() > 0) {
                    tvCoupon.setText("请选择");
                } else {
                    tvCoupon.setText("无可用");
                }
                break;
            case Constants.HANDLER_GETCOUPONS_FAILD:
                Constants.ToastAction((String) msg.obj);
                break;
            case Constants.HANDLER_GETDELIVERYMETHOD_SUCCESS:
                ArrayList<DeliveryMethodInfo> data1 = (ArrayList<DeliveryMethodInfo>) msg.obj;
                deliveryMethodData.clear();
                deliveryMethodData.addAll(data1);
                deliveryMethodCount = deliveryMethodData.size();
                Constants.MyLog(data1.toString());
                if (deliveryMethodCount > 0) {
                    tvDeliveryMethod.setText(deliveryMethodData.get(0).getName());
                    deliveryMethodId = deliveryMethodData.get(0).getId();
                }
                break;
            case Constants.HANDLER_GETDELIVERYMETHOD_FAILD:
                Constants.ToastAction((String) msg.obj);
                break;
            case Constants.HANDLER_GETSETUPTIME_SUCCESS:
                ArrayList<SetUpTimeInfo> data3 = (ArrayList<SetUpTimeInfo>) msg.obj;
                setupTimeData.addAll(data3);
                setupTimeCount = setupTimeData.size();
                if (setupTimeCount > 0) {
                    tvSetupTime.setText(setupTimeData.get(0).getName());
                    setupTimeId = setupTimeData.get(0).getId();
                }
                break;
            case Constants.HANDLER_GETSETUPTIME_FAILD:
                Constants.ToastAction((String) msg.obj);
                break;
            case Constants.HANDLER_GETYUNBOXPAY_SUCCESS:
                Constants.MyLog("拿到签名成功");
                if (payType.equals("101")) {
                    WeiXinEntityBean entityBean = (WeiXinEntityBean) msg.obj;
                    partnerId = entityBean.getPartnerid();
                    prepayId = entityBean.getPrepayid();
                    nonceStr = entityBean.getNoncestr();
                    timestamp = entityBean.getTimestamp();
                    sign = entityBean.getSign();
                    Constants.MyLog(entityBean.toString()+"微信签名");
                    payWeiXin();
                } else if (payType.equals("102")) {
                    AliPayBean aliPayBean = (AliPayBean) msg.obj;
                    Constants.MyLog("解析到的支付宝信息"+aliPayBean);
                    String signInfo = aliPayBean.getSign();
                    payV2(signInfo);
                }
                Constants.MyLog(sign);
                break;
            case Constants.HANDLER_GETYUNBOXPAY_FAILD:
                Constants.MyLog("拿到签名失败");
                Constants.MyToast((String) msg.obj);
                break;
            case Constants.HANDLER_GETPAYPRICE_SUCCESS:
                PayPriceInfo payPriceInfo = (PayPriceInfo) msg.obj;
                amount = payPriceInfo.getAmount();
                if (TextUtils.isEmpty(amount)) {
                    tvPayPrice.setText("合计: ¥"+amount);
                }
                break;
            case Constants.HANDLER_GETPAYPRICE_FAILD:
                Constants.ToastAction((String) msg.obj);
                break;
            case Constants.HANDLER_CHECKPAY_SUCCESS:
                Constants.MyLog("获取支付结果成功");
                break;
            case Constants.HANDLER_CHECKPAY_FAIDL:
                break;
        }
        return false;
    }

    //微信支付
    private void payWeiXin() {

        IWXAPI api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.registerApp(Constants.APP_ID);
        PayReq payRequest = new PayReq();
        payRequest.appId = Constants.APP_ID;
        payRequest.partnerId = partnerId;
        payRequest.prepayId = prepayId;
        payRequest.packageValue = "Sign=WXPay";
        payRequest.nonceStr = nonceStr;
        payRequest.timeStamp = timestamp;
        payRequest.sign = sign;
        api.sendReq(payRequest);
    }


    public int GetViewHeight(int resource) {

        View view = LayoutInflater.from(mContext).inflate(resource, null);
        view.setLayoutParams(new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT));
        int parameterW = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int parameterH = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(parameterW, parameterH);
        int measuredHeight = view.getMeasuredHeight();
        int measuredWidth = view.getMeasuredWidth();
        return measuredHeight;
    }

    public int getScreenHeight() {
        WindowManager wm = this.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        return height;
    }

    public int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    public void payV2(final String orderInfo) {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(ConfirmInformationActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();

    }

    /**
     * get the sdk version. 获取SDK版本号
     *
     */
    public void getSDKVersion() {
        PayTask payTask = new PayTask(this);
        String version = payTask.getVersion();
        Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
    }
}