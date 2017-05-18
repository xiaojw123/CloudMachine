package com.cloudmachine.ui.personal.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.cloudmachine.R;
import com.cloudmachine.autolayout.widgets.TitleView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.ui.personal.fragment.PersonalFragment;
import com.cloudmachine.utils.ToastUtils;
import com.github.mikephil.charting.utils.AppLog;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/4/5 下午1:40
 * 修改人：shixionglu
 * 修改时间：2017/4/5 下午1:40
 * 修改备注：
 */
public class MyQRCodeActivity extends BaseAutoLayoutActivity {
    private static int PERIOD = 1000;
    private static int MSG_TIMER = 0x10;
    @BindView(R.id.title_layout)
    TitleView mTitleLayout;
    @BindView(R.id.iv_qrcode)
    ImageView mIvQrcode;
    @BindView(R.id.leftime_tv)
    TextView mLeftTimieTv;
    private AMapLocationClient locationClient = null;

    private String text = "测试生成的二维码";
    private static final int REQUEST_PERMISSION = 0x005;  //权限请求
    //需要申请的权限数组
    static final String[] PERMISSIONS = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};

    boolean flag = true;
    double lat, lng;
    int timeCount = 300;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            timeCount--;
            mLeftTimieTv.setText(getChTime(timeCount));
            if (timeCount <= 0) {
                timeCount = 300;
                createQrcode();
            }
            mHandler.sendEmptyMessageDelayed(MSG_TIMER, PERIOD);
        }
    };


    public String getChTime(int time) {
        int m = time / 60;
        int s = time % 60;
        if (m >= 1) {
            return "有效时间: " + m + "分" + s + "秒";
        } else {
            return "有效时间: " + s + "秒";
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler.hasMessages(MSG_TIMER)) {
            mHandler.removeMessages(MSG_TIMER);
            mHandler = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myqrcode);
        ButterKnife.bind(this);
        initView();
        //检查权限(6.0以上做权限判断)
        AppLog.print("权限检查");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            AppLog.print("没有授权");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION);
        } else {
            initLocation();
        }


//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        timeCount--;
//                        mLeftTimieTv.setText("有效时间: "+timeCount+"s");
//                    }
//                });
//
//            }
//        }, 0, 1000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //用户同意授权
                    initLocation();
                } else {
                    //用户拒绝授权
                    Toast.makeText(this, "您已禁止该权限，需要重新开启。", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void initLocation() {

        //初始化client
        locationClient = new AMapLocationClient(this.getApplicationContext());
        //设置定位参数
        locationClient.setLocationOption(getDefaultOption());
        // 设置定位监听
        locationClient.setLocationListener(locationListener);

        locationClient.startLocation();
    }

    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation loc) {
            if (null != loc) {
                //解析定位结果
//                AppLog.print("定位成功 经    度    : " + loc.getLongitude() + "\n" + "纬    度    : " + loc.getLatitude());
                if (flag) {
                    flag = false;
                    mHandler.sendEmptyMessageDelayed(MSG_TIMER, PERIOD);
                    lat = loc.getLatitude();
                    lng = loc.getLongitude();
                    createQrcode();


                }

            } else {
                AppLog.print("定位失败，loc is null");
            }
        }
    };

    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
//        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
//        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }


    private void initView() {

        initTitleLayout();

//        Location location= LocationUtils.getInstance(this).showLocation();
//        String address = "纬度："+location.getLatitude()+"经度："+location.getLongitude();
//        AppLog.print("address___"+address);

    }

    private void createQrcode() {
        String memberId = getIntent().getStringExtra(PersonalFragment.MEMBER_ID);
        long time = System.currentTimeMillis();
        StringBuffer sb = new StringBuffer();
        sb.append("lat=" + lat)
                .append("&lng=" + lng).append("&id=" + memberId + "&time=" + time);
        String text = sb.toString();
        AppLog.print("qrCode text:" + text);
        Observable.just(text)
                .map(new Func1<String, Bitmap>() {
                    @Override
                    public Bitmap call(String s) {
                        return QRCodeEncoder.syncEncodeQRCode(s,
                                BGAQRCodeUtil.dp2px(MyQRCodeActivity.this, 163),
                                Color.parseColor("#ff000000"));
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Bitmap>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.error(e.toString(), true);
                    }

                    @Override
                    public void onNext(Bitmap bitmap) {
                        mIvQrcode.setImageBitmap(bitmap);
                    }
                });


    }

    private void initTitleLayout() {

        mTitleLayout.setTitle("我的二维码");
        mTitleLayout.setLeftImage(-1, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void initPresenter() {

    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }


}
