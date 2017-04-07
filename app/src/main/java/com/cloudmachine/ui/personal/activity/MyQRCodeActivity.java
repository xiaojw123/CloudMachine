package com.cloudmachine.ui.personal.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import com.cloudmachine.R;
import com.cloudmachine.autolayout.widgets.TitleView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.utils.ToastUtils;

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

    @BindView(R.id.title_layout)
    TitleView mTitleLayout;
    @BindView(R.id.iv_qrcode)
    ImageView mIvQrcode;

    private String text = "测试生成的二维码";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myqrcode);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {

        initTitleLayout();
        createQrcode(text);
    }

    private void createQrcode(String text) {

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
                        ToastUtils.error(e.toString(),true);
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
