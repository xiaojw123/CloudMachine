package com.cloudmachine.ui.home.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.ToastUtils;
import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLOnErrorListener;
import com.pili.pldroid.player.widget.PLVideoView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;

public class FullScreenActivity extends AppCompatActivity implements Handler.Callback, PLOnErrorListener {
    private static final int HOLD_ON_TIME=5000;
    public static final String PLAY_URL = "play_url";
    public static final String NAME = "name";
    public static final int MSG_DELAY_CTRL = 0x11;
    public static final int MEIA_STATUS_PLAY=0x000;
    public static final int MEIA_STATUS_PAUSE=0x111;
    public static final int MEIA_STATUS_REPLAY=0x112;
    @BindView(R.id.fullscreen_pvv)
    PLVideoView mVieoView;
    @BindView(R.id.fullscreen_status_img)
    ImageView mStatusImg;
    @BindView(R.id.fullscreen_back_img)
    ImageView mBackImg;
    @BindView(R.id.fullscreen_name)
    TextView mNameTv;
    @BindView(R.id.fullscreen_replay)
    Button mReplayBtn;
    Handler mHandler;
    long memberId;
    String deviceId;
    String videoId;
    String playUrl;
    int status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);
        ButterKnife.bind(this);
        mHandler = new Handler(this);
        playUrl = getIntent().getStringExtra(PLAY_URL);
        String name = getIntent().getStringExtra(NAME);
        memberId = getIntent().getLongExtra(Constants.MEMBER_ID, 0);
        deviceId = getIntent().getStringExtra(Constants.DEVICE_ID);
        videoId = getIntent().getStringExtra(Constants.VIDEO_ID);
        mNameTv.setText(name);
        AVOptions options = new AVOptions();
        options.setInteger(AVOptions.KEY_LIVE_STREAMING, 1);
        options.setInteger(AVOptions.KEY_FAST_OPEN, 1);
        options.setInteger(AVOptions.KEY_OPEN_RETRY_TIMES, 5);
        mVieoView.setAVOptions(options);
        mVieoView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_FIT_PARENT);
        mVieoView.setOnErrorListener(this);
        mVieoView.setScreenOnWhilePlaying(true);
        mVieoView.setKeepScreenOn(true);
        mVieoView.setVideoPath(playUrl);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (mHandler.hasMessages(MSG_DELAY_CTRL)) {
                mHandler.removeMessages(MSG_DELAY_CTRL);
            } else {
                showVideoCtrl(View.VISIBLE);
            }
            mHandler.sendEmptyMessageDelayed(MSG_DELAY_CTRL, HOLD_ON_TIME);
        }
        return super.dispatchTouchEvent(ev);
    }

    public void showVideoCtrl(int visibility) {
        mBackImg.setVisibility(visibility);
        mNameTv.setVisibility(visibility);
        if (mReplayBtn.getVisibility() != View.VISIBLE) {
            mStatusImg.setVisibility(visibility);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        play();
        mHandler.sendEmptyMessageDelayed(MSG_DELAY_CTRL, HOLD_ON_TIME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVieoView.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVieoView.stopPlayback();
    }

    @OnClick({R.id.fullscreen_back_img, R.id.fullscreen_status_img, R.id.fullscreen_replay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fullscreen_replay:
                Api.getDefault(HostType.HOST_CLOUDM_YJX).videoUpload(memberId, deviceId, videoId).compose(RxHelper.<String>handleBaseResult()).subscribe(new Subscriber<BaseRespose<String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(BaseRespose<String> respose) {
                        if (respose != null) {
                            if (respose.success()) {
                                mReplayBtn.setVisibility(View.GONE);
                                mVieoView.setVideoPath(playUrl);
                                play();
                            } else {
                                ToastUtils.showToast(FullScreenActivity.this, respose.getMessage());
                            }

                        }
                    }
                });

                break;
            case R.id.fullscreen_back_img:
                setResult(status);
                finish();
                break;
            case R.id.fullscreen_status_img:
                if (mVieoView.isPlaying()) {
                    pause();
                    mStatusImg.setImageResource(R.drawable.ic_work_vide_pause);
                } else {
                    play();
                    mStatusImg.setImageResource(R.drawable.ic_work_vide_play);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        setResult(status);
        super.onBackPressed();
    }

    private void pause() {
        status=MEIA_STATUS_PAUSE;
        mVieoView.pause();
    }

    private void play() {
        status=MEIA_STATUS_PLAY;
        mVieoView.start();
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == MSG_DELAY_CTRL) {
            showVideoCtrl(View.GONE);
        }
        return false;
    }

    @Override
    public boolean onError(int i) {
        playComplete();
        return false;
    }

    private void playComplete() {
        status=MEIA_STATUS_REPLAY;
        mVieoView.stopPlayback();
        if (!TextUtils.isEmpty(videoId)) {
            mReplayBtn.setText(getResources().getString(R.string.replay));
        } else {
            mReplayBtn.setText(getResources().getString(R.string.resumeplay));
        }
        mReplayBtn.setVisibility(View.VISIBLE);
        mStatusImg.setVisibility(View.GONE);
    }


}
