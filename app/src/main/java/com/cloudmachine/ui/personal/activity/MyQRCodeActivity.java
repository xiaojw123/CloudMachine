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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.cloudmachine.R;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.chart.utils.AppLog;
import com.cloudmachine.helper.LocationManager;
import com.cloudmachine.helper.MobEvent;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

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
        MobclickAgent.onEvent(this, MobEvent.RQCODE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                    Toast.makeText(this, "无法生成二维码, 您已禁止该权限，需要重新开启。", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }

    private void initLocation() {

        locationClient=LocationManager.getInstence().getLocationClient(this.getApplicationContext());
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




    private void createQrcode() {
        String memberId = String.valueOf(UserHelper.getMemberId(this));
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


    @Override
    public void initPresenter() {

    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }


}
