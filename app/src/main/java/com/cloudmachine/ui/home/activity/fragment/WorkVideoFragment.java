package com.cloudmachine.ui.home.activity.fragment;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.adapter.BaseRecyclerAdapter;
import com.cloudmachine.adapter.VideoListAdapter;
import com.cloudmachine.base.BaseFragment;
import com.cloudmachine.base.baserx.RxSchedulers;
import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.bean.VideoBean;
import com.cloudmachine.chart.utils.AppLog;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.ui.home.activity.FullScreenActivity;
import com.cloudmachine.ui.home.contract.WorkVideoContract;
import com.cloudmachine.ui.home.model.WorkVideoModel;
import com.cloudmachine.ui.home.presenter.WorkVideoPresenter;
import com.cloudmachine.utils.CommonUtils;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.ToastUtils;
import com.cloudmachine.utils.widgets.wheelview.OnWheelScrollListener;
import com.cloudmachine.utils.widgets.wheelview.WheelView;
import com.cloudmachine.utils.widgets.wheelview.adapter.NumericWheelAdapter;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLOnBufferingUpdateListener;
import com.pili.pldroid.player.PLOnCompletionListener;
import com.pili.pldroid.player.PLOnErrorListener;
import com.pili.pldroid.player.widget.PLVideoView;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
/**
 * Created by xiaojw on 2018/3/9.
 */

public class WorkVideoFragment extends BaseFragment<WorkVideoPresenter, WorkVideoModel> implements WorkVideoContract.View, XRecyclerView.LoadingListener, BaseRecyclerAdapter.OnItemClickListener, View.OnClickListener {
    private static final String PROMPT_NO_ONLINE = "当前设备离线，无法观看视频";
    private static final String PROMPT_NO_VIDEO = "抱歉，当前暂无工作视频";
    @BindView(R.id.work_live_videoview)
    PLVideoView workLiveVideoview;
    @BindView(R.id.work_live_status)
    TextView workLiveStatus;
    @BindView(R.id.work_video_tab_today)
    TextView workVideoTabToday;
    @BindView(R.id.work_video_tab_yesterday)
    TextView workVideoTabYesterday;
    @BindView(R.id.work_video_tab_period)
    TextView workVideoTabPeriod;
    @BindView(R.id.work_video_xrlv)
    XRecyclerView workVideoXrlv;
    @BindView(R.id.work_video_tab_indicator)
    View indicatorView;
    @BindView(R.id.work_video_list_empty)
    TextView emptyListTv;
    @BindView(R.id.work_video_all_empty)
    TextView emptyAllTv;
    @BindView(R.id.work_video_live_cotainer)
    RelativeLayout liveContainer;
    @BindView(R.id.work_video_list_cotainer)
    RelativeLayout videoCotaniner;
    @BindView(R.id.video_wheelview_layout)
    LinearLayout mWvLayout;
    @BindView(R.id.video_wheelview_cancel)
    TextView mWvCancelTv;
    @BindView(R.id.video_wheelview_determine)
    TextView mWvDeterMineTv;
    @BindView(R.id.video_wheelview_title)
    TextView mWvTitleTv;
    @BindView(R.id.video_hour_wheelview)
    WheelView mWvHour;
    @BindView(R.id.video_minute_wheelview)
    WheelView mWvMinute;
    @BindView(R.id.video_second_wheelview)
    WheelView mWvSecond;
    @BindView(R.id.work_video_replay)
    Button rePlayBtn;
    ImageView workLivePlay;
    ImageView fullScreenImg;
    ImageView workLivePause;

    VideoListAdapter mVideoListAdapter;
    String deviceId;
    String playUrl;
    String videoId;
    String liveUrl;
    String startTime, endTime;
    String name;
    int timeType = 0;
    long startTimeL, endTimeL;
    String time1 = null;
    String time2 = null;
    boolean isPause;
    int resultCode;
    boolean isOnLine;
    boolean isFirst = true;
    String sn;
    int selectedId = -1;
    boolean isVolumeAble=true;

    @Override
    protected void initView() {
        remedyBind();
        initParams();
        initTab();
        initWheelView();
        initRecyclerView();
        initVideoView();
    }

    private void remedyBind() {
        workLivePlay= (ImageView) viewParent.findViewById(R.id.work_live_play);
        workLivePause= (ImageView) viewParent.findViewById(R.id.work_live_pause);
        fullScreenImg= (ImageView) viewParent.findViewById(R.id.work_video_fullscreen);
        workLivePlay.setOnClickListener(this);
        workLivePause.setOnClickListener(this);
        fullScreenImg.setOnClickListener(this);
    }

    private void setMuteVolume() {
        if (isVolumeAble) {
            isVolumeAble=false;
            AudioManager am = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
            am.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
        }
    }


    private void initParams() {
        deviceId = getActivity().getIntent().getStringExtra(Constants.DEVICE_ID);
        sn = getActivity().getIntent().getStringExtra(Constants.SN_ID);
        isOnLine = getActivity().getIntent().getBooleanExtra(Constants.IS_ONLINE, false);
    }

    private void initWheelView() {
        NumericWheelAdapter hAdpater = new NumericWheelAdapter(getActivity(), 0, 23, "%02d");
        String hour_unit = "时";
        hAdpater.setLabel(hour_unit);
        mWvHour.setViewAdapter(hAdpater);
        mWvHour.setCyclic(true);
        mWvHour.setVisibleItems(7);
        mWvHour.addScrollingListener(scrollListener);
        NumericWheelAdapter mAdapter = new NumericWheelAdapter(getActivity(), 0, 59, "%02d");
        String minute_unit = "分";
        mAdapter.setLabel(minute_unit);
        mWvMinute.setViewAdapter(mAdapter);
        mWvMinute.setCyclic(true);
        mWvMinute.setVisibleItems(7);
        mWvMinute.addScrollingListener(scrollListener);
        NumericWheelAdapter sAdapter = new NumericWheelAdapter(getActivity(), 0, 59, "%02d");
        String second_unit = "秒";
        sAdapter.setLabel(second_unit);
        mWvSecond.setViewAdapter(sAdapter);
        mWvSecond.setCyclic(true);
        mWvSecond.setVisibleItems(7);
        mWvSecond.addScrollingListener(scrollListener);
    }

    private void initTab() {
        workVideoTabToday.setSelected(true);
        workVideoTabYesterday.setSelected(false);
//        initDateTime(0);
    }

    private void initVideoView() {
        AVOptions options = new AVOptions();
        options.setInteger(AVOptions.KEY_LIVE_STREAMING, 1);
        options.setInteger(AVOptions.KEY_FAST_OPEN, 1);
        options.setInteger(AVOptions.KEY_OPEN_RETRY_TIMES, 5);
        workLiveVideoview.setAVOptions(options);
        workLiveVideoview.setLooping(false);
        ProgressBar pb = new ProgressBar(getActivity());
        workLiveVideoview.setBufferingIndicator(pb);
        workLiveVideoview.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_FIT_PARENT);
//        workLiveVideoview.setScreenOnWhilePlaying(true);
        workLiveVideoview.setKeepScreenOn(true);
        workLiveVideoview.setOnErrorListener(mErrorListener);
//        workLiveVideoview.setOnInfoListener(mOnInfoListener);
        workLiveVideoview.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
        workLiveVideoview.setOnCompletionListener(mOnCompletionListener);
    }

    private void initRecyclerView() {
        workVideoXrlv.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        workVideoXrlv.setLayoutManager(new LinearLayoutManager(getActivity()));
        workVideoXrlv.setPullRefreshEnabled(true);
        workVideoXrlv.setLoadingMoreEnabled(false);
        workVideoXrlv.setLoadingListener(this);
    }


    public void initDateTime(int offset) {
        initDateTime(offset, "00:00:00", "23:59:59");
    }

    public void initDateTime(int offset, String time1, String time2) {
        String todayDate = CommonUtils.getPastDate(offset);
        startTime = todayDate + " " + time1;
        endTime = todayDate + " " + time2;
    }


    public void updateVideoInfo(final boolean isResume) {
        if (isResume) {
            if (isFirst) {
                isFirst = false;
                if (isOnLine) {
                    resumeVideo();
                } else {
                    setEmptVideo(PROMPT_NO_ONLINE);
                }
            } else {
                resumeVideo();
            }

        } else {

            flush();
        }


    }

    public void flush() {
        if (workVideoXrlv.getVisibility() == View.VISIBLE) {
            workVideoXrlv.refresh();
        } else {
            onRefresh();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        AppLog.print("onResume__");
        updateVideoInfo(true);
    }

    public void setEmptVideo(String promptMsg) {
        emptyAllTv.setText(promptMsg);
        emptyAllTv.setVisibility(View.VISIBLE);
        liveContainer.setVisibility(View.GONE);
        videoCotaniner.setVisibility(View.GONE);

    }

    public void resumeVideo() {
        mPresenter.getVideoList(deviceId, startTime, endTime);
        if (resultCode == FullScreenActivity.MEIA_STATUS_REPLAY) {
            playComptete();
        } else if (resultCode == FullScreenActivity.MEIA_STATUS_PAUSE) {
            pausePlay();
        } else {
            if (workLiveVideoview != null && !workLiveVideoview.isPlaying()) {
                workLiveVideoview.start();
            }
        }
    }


    @Override
    protected void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_work_video;
    }


    @Override
    public void returnVideoUploadSuccess() {
        workLiveVideoview.setVideoPath(playUrl);
        startPlay();
    }

    @Override
    public void returnVideoUploadError(String message) {
//        if (rePlayBtn.getVisibility() != View.VISIBLE) {
//            workLivePlay.setVisibility(View.VISIBLE);
//            workLivePause.setVisibility(View.GONE);
//            fullScreenImg.setVisibility(View.GONE);
//        }
        ToastUtils.showToast(getActivity(), message);
    }

    @Override
    public void returnGetVideoList(String liveUrl, List<VideoBean.VideoListBean> videoList) {
        if (TextUtils.isEmpty(liveUrl) && (videoList == null || videoList.size() <= 0)) {
            setEmptVideo(PROMPT_NO_VIDEO);
            return;
        }
        liveContainer.setVisibility(View.VISIBLE);
        videoCotaniner.setVisibility(View.VISIBLE);
        this.liveUrl = liveUrl;
        playUrl = liveUrl;
        workVideoXrlv.refreshComplete();
        if (videoList != null && videoList.size() > 0) {
            if (selectedId != -1) {
                for (VideoBean.VideoListBean item : videoList) {
                    if (item.getId() == selectedId) {
                        item.setSelected(true);
                    } else {
                        item.setSelected(false);
                    }
                }
            }
//            Collections.reverse(videoList);
            emptyListTv.setVisibility(View.GONE);
            workVideoXrlv.setVisibility(View.VISIBLE);
            if (mVideoListAdapter == null) {
                mVideoListAdapter = new VideoListAdapter(getActivity(), videoList);
                mVideoListAdapter.setOnItemClickListener(this);
                workVideoXrlv.setAdapter(mVideoListAdapter);
            } else {
                mVideoListAdapter.updateItems(videoList);
            }
        } else {
            emptyListTv.setVisibility(View.VISIBLE);
            workVideoXrlv.setVisibility(View.GONE);
        }
    }

    @Override
    public void returnGetVideoListError(String message) {
        if (!TextUtils.isEmpty(message)) {
            setEmptVideo(message + "，无法观看视频");
        } else {
            setEmptVideo(PROMPT_NO_VIDEO);
        }
    }

    @OnClick({R.id.work_live_status, R.id.work_video_replay, R.id.video_wheelview_cancel, R.id.video_wheelview_determine, R.id.work_video_tab_period, R.id.work_video_tab_today, R.id.work_video_tab_yesterday})
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.work_video_replay:
                mPresenter.videoUpload(deviceId, videoId);
                break;
            case R.id.work_live_status://回到直播
                setVideoSelectedItem(null);
                playUrl = liveUrl;
                videoId = null;
                name = null;
                mPresenter.videoUpload(deviceId, videoId);
                break;
            case R.id.work_video_tab_period:
                showWheel();
                timeType = 0;
                mWvTitleTv.setText(getResources().getString(R.string.historical_track_wheelview_time_start));
                break;
            case R.id.video_wheelview_cancel:
                hidenWheel();
                break;
            case R.id.video_wheelview_determine:
                if (timeType == 0) {
                    timeType = 1;
//                    startTimeL=mWvHour.getCurrentItem()
                    time1 = mWvHour.getCurrentItem() + ":" + mWvMinute.getCurrentItem() + ":" + mWvSecond.getCurrentItem();
                    startTimeL = Constants.getDatetolong(time1,
                            "HH:mm:ss");
                    mWvTitleTv.setText(getResources().getString(R.string.historical_track_wheelview_time_end));
                    AppLog.print("time1:" + time1);
                } else {
                    hidenWheel();
                    time2 = mWvHour.getCurrentItem() + ":" + mWvMinute.getCurrentItem() + ":" + mWvSecond.getCurrentItem();
                    endTimeL = Constants.getDatetolong(time2,
                            "HH:mm:ss");
                    AppLog.print("time2：" + time2);
                    if (startTimeL > endTimeL) {
                        time1 = null;
                        time2 = null;
                        ToastUtils.showToast(getActivity(), getResources().getString(R.string.start_end_time_error));
                        return;
                    } else {
                        if (workVideoTabToday.isSelected()) {
                            initDateTime(0, time1, time2);
                        } else if (workVideoTabYesterday.isSelected()) {
                            initDateTime(1, time1, time2);
                        }
                        AppLog.print("time1___" + time1 + ", time2__" + time2);
                        mPresenter.getVideoList(deviceId, startTime, endTime);
                    }
                }
                break;
            case R.id.work_live_pause:
                pausePlay();
                break;
            case R.id.work_video_fullscreen:
                resultCode = 0;
                Bundle bundle = new Bundle();
                bundle.putString(FullScreenActivity.PLAY_URL, playUrl);
                bundle.putString(FullScreenActivity.NAME, name);
                bundle.putString(Constants.DEVICE_ID, deviceId);
                bundle.putString(Constants.VIDEO_ID, videoId);
                Intent intent = new Intent(getActivity(), FullScreenActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
                break;
            case R.id.work_live_play:
//                Drawable resDrawable=workLivePuase.getDrawable();
                if (isPause) {
                    isPause = false;
                    startPlay();
                } else {
                    mPresenter.videoUpload(deviceId, videoId);
                }
                break;
            case R.id.work_video_tab_today:
            case R.id.work_video_tab_yesterday:
                if (view.isSelected()) {
                    return;
                }
                setSelectTab(id);
                view.setSelected(true);
                if (id == R.id.work_video_tab_today) {
                    workVideoTabYesterday.setSelected(false);
                    if (!TextUtils.isEmpty(time1) && !TextUtils.isEmpty(time2)) {
                        initDateTime(0, time1, time2);
                    } else {
                        initDateTime(0);
                    }
                } else {
                    workVideoTabToday.setSelected(false);
                    if (!TextUtils.isEmpty(time1) && !TextUtils.isEmpty(time2)) {
                        initDateTime(1, time1, time2);
                    } else {
                        initDateTime(1);
                    }
                }
                mPresenter.getVideoList(deviceId, startTime, endTime);
                break;
        }

    }

    private void setVideoSelectedItem(VideoBean.VideoListBean item) {
        selectedId = -1;
        if (mVideoListAdapter != null) {
            List<VideoBean.VideoListBean> videoItems = mVideoListAdapter.getItems();
            if (videoItems != null && videoItems.size() > 0) {
                for (VideoBean.VideoListBean bean : videoItems) {
                    if (item == bean) {
                        selectedId = bean.getId();
                        bean.setSelected(true);
                    } else {
                        bean.setSelected(false);
                    }
                }
                mVideoListAdapter.notifyDataSetChanged();
            }
        }
    }

    public void pausePlay() {
        isPause = true;
        workLivePlay.setVisibility(View.VISIBLE);
        workLivePause.setVisibility(View.GONE);
        fullScreenImg.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(videoId)) {
            workLiveStatus.setPadding(0, 0, 0, 0);
            workLiveStatus.setText("回到直播");
            workLiveStatus.setEnabled(true);
            workLiveStatus.setBackground(getResources().getDrawable(R.drawable.ic_work_vieo_live_3));
        } else {
            workLiveStatus.setPadding((int) getResources().getDimension(R.dimen.dimen_size_8), 0, 0, 0);
            workLiveStatus.setText("直播");
            workLiveStatus.setEnabled(false);
            workLiveStatus.setBackground(getResources().getDrawable(R.drawable.ic_work_vieo_live_1));
        }
        if (workLiveVideoview.isPlaying()) {
            workLiveVideoview.pause();
        }
    }

    public void startPlay() {
        workLiveVideoview.start();
        rePlayBtn.setVisibility(View.GONE);
        workLivePlay.setVisibility(View.GONE);
        workLivePause.setVisibility(View.VISIBLE);
        fullScreenImg.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(videoId)) {
            workLiveStatus.setPadding(0, 0, 0, 0);
            workLiveStatus.setText("回到直播");
            workLiveStatus.setEnabled(true);
            workLiveStatus.setBackground(getResources().getDrawable(R.drawable.ic_work_vieo_live_3));
        } else {
            workLiveStatus.setPadding((int) getResources().getDimension(R.dimen.dimen_size_8), 0, 0, 0);
            workLiveStatus.setText("直播中");
            workLiveStatus.setEnabled(false);
            workLiveStatus.setBackground(getResources().getDrawable(R.drawable.ic_work_vieo_live_2));
        }
        setMuteVolume();
    }

    private void playComptete() {
        if (workLiveVideoview != null) {
            workLiveVideoview.stopPlayback();
        }
        workLivePlay.setVisibility(View.VISIBLE);
        workLivePause.setVisibility(View.GONE);
        fullScreenImg.setVisibility(View.GONE);
        String statusText = workLiveStatus.getText().toString();
        if ("回到直播".equals(statusText)) {
            rePlayBtn.setText(getResources().getString(R.string.replay));
        } else {
            rePlayBtn.setText(getResources().getString(R.string.resumeplay));
        }
        rePlayBtn.setVisibility(View.VISIBLE);

    }


    private void showWheel() {
        workVideoTabPeriod.setEnabled(false);
        mWvLayout.setVisibility(View.VISIBLE);
    }

    private void hidenWheel() {
        workVideoTabPeriod.setEnabled(true);
        mWvLayout.setVisibility(View.GONE);
    }


    OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
        @Override
        public void onScrollingStarted(WheelView wheel) {

        }

        @Override
        public void onScrollingFinished(WheelView wheel) {
            if (workVideoTabToday.isSelected()) {
                Calendar now = Calendar.getInstance();
                Date date = now.getTime();
                int hour = date.getHours();
                int minute = date.getMinutes();
                int second = date.getSeconds();
                AppLog.print("hour_" + hour + "__min__" + minute + ", second__" + second);
                if ((mWvHour.getCurrentItem()) > hour) {
                    mWvHour.setCurrentItem(hour);
                } else if (mWvHour.getCurrentItem() == hour) {
                    if (mWvMinute.getCurrentItem() > minute) {
                        mWvMinute.setCurrentItem(minute);
                    } else if (mWvMinute.getCurrentItem() == minute) {
                        if (mWvSecond.getCurrentItem() > second) {
                            mWvSecond.setCurrentItem(second);
                        }
                    }
                }
            }
        }
    };


    private void setSelectTab(int id) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) indicatorView.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_LEFT, id);
        params.addRule(RelativeLayout.ALIGN_RIGHT, id);
        indicatorView.setLayoutParams(params);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (workLiveVideoview != null) {
            workLiveVideoview.pause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (workLiveVideoview != null) {
            workLiveVideoview.stopPlayback();
        }
    }

    public void flushData() {
        mRxManager.add(Api.getDefault(HostType.HOST_LARK).getImei(sn).compose(RxSchedulers.<JsonObject>io_main()).subscribe(new RxSubscriber<JsonObject>(getActivity()) {
            @Override
            protected void _onNext(JsonObject respJobj) {
                JsonObject resultJobj = respJobj.getAsJsonObject("result");
                JsonElement videoJel = resultJobj.get("haveVideo");
                if (videoJel != null) {
                    boolean hasVideo = videoJel.getAsBoolean();
                    if (hasVideo) {
                        updateVideoInfo(false);

                    } else {
                        setEmptVideo(PROMPT_NO_VIDEO);
                    }
                }
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(getActivity(), message);
            }
        }));

    }

    @Override
    public void onRefresh() {
        mPresenter.getVideoList(deviceId, startTime, endTime);
    }

    @Override
    public void onLoadMore() {


    }

    @Override
    public void onItemClick(View view, int position) {
        VideoBean.VideoListBean item = (VideoBean.VideoListBean) view.getTag();
        if (item != null) {
            AppLog.print("onItemClick___pos__" + position);
            name = item.getName();
            videoId = String.valueOf(item.getId());
            playUrl = item.getLiveUrl();
            setVideoSelectedItem(item);
            mPresenter.videoUpload(deviceId, String.valueOf(item.getId()));
        }


    }


    private PLOnCompletionListener mOnCompletionListener = new PLOnCompletionListener() {
        @Override
        public void onCompletion() {
            AppLog.print("onCompletion___");
            playComptete();
        }
    };

    private PLOnBufferingUpdateListener mOnBufferingUpdateListener = new PLOnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(int precent) {
            AppLog.print("onBufferingUpdate: " + precent);
        }
    };

    private PLOnErrorListener mErrorListener = new PLOnErrorListener() {
        @Override
        public boolean onError(int i) {
            playComptete();
            return false;
        }
    };


//    private PLOnInfoListener mOnInfoListener = new PLOnInfoListener() {
//        @Override
//        public void onInfo(int what, int extra) {
//            AppLog.print("OnInfo, what = " + what + ", extra = " + extra);
//            switch (what) {
//                case PLOnInfoListener.MEDIA_INFO_BUFFERING_START:
//                    break;
//                case PLOnInfoListener.MEDIA_INFO_BUFFERING_END:
//                    break;
//                case PLOnInfoListener.MEDIA_INFO_VIDEO_RENDERING_START:
//                    break;
//                case PLOnInfoListener.MEDIA_INFO_AUDIO_RENDERING_START:
//                    break;
//                case PLOnInfoListener.MEDIA_INFO_VIDEO_FRAME_RENDERING:
//                    break;
//                case PLOnInfoListener.MEDIA_INFO_AUDIO_FRAME_RENDERING:
//                    break;
//                case PLOnInfoListener.MEDIA_INFO_VIDEO_GOP_TIME:
//                    break;
//                case PLOnInfoListener.MEDIA_INFO_SWITCHING_SW_DECODE:
//                    break;
//                case PLOnInfoListener.MEDIA_INFO_METADATA:
//                    break;
//                case PLOnInfoListener.MEDIA_INFO_VIDEO_BITRATE:
//                case PLOnInfoListener.MEDIA_INFO_VIDEO_FPS:
//                    break;
//                case PLOnInfoListener.MEDIA_INFO_CONNECTED:
//                    break;
//                case PLOnInfoListener.MEDIA_INFO_VIDEO_ROTATION_CHANGED:
//                default:
//                    break;
//            }
//        }
//    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        AppLog.print("onActivityResult__" + resultCode);
        this.resultCode = resultCode;
    }

}
