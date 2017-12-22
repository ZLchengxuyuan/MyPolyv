package com.lifeng.mypolyv.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.easefun.polyvsdk.video.PolyvVideoView;
import com.easefun.polyvsdk.video.listener.IPolyvOnGestureClickListener;
import com.easefun.polyvsdk.video.listener.IPolyvOnGestureLeftDownListener;
import com.easefun.polyvsdk.video.listener.IPolyvOnGestureLeftUpListener;
import com.easefun.polyvsdk.video.listener.IPolyvOnGestureRightDownListener;
import com.easefun.polyvsdk.video.listener.IPolyvOnGestureRightUpListener;
import com.easefun.polyvsdk.video.listener.IPolyvOnGestureSwipeLeftListener;
import com.easefun.polyvsdk.video.listener.IPolyvOnGestureSwipeRightListener;
import com.easefun.polyvsdk.video.listener.IPolyvOnPreparedListener2;
import com.lifeng.mypolyv.R;
import com.lifeng.mypolyv.player.PolyvPlayerMediaController;
import com.lifeng.mypolyv.utils.PolyvScreenUtils;

public class PolyvPlayerActivity extends FragmentActivity {
    private static final String TAG = PolyvPlayerActivity.class.getSimpleName();
    private ImageView iv_vlms_cover;
    /**
     * 播放器的parentView
     */
    private RelativeLayout viewLayout = null;
    /**
     * 播放主视频播放器
     */
    private PolyvVideoView pvideoView = null;
    /**
     * 视频控制栏
     */
    private PolyvPlayerMediaController mediaController = null;


    private int fastForwardPos = 0;
    private boolean isPlay = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null)
            savedInstanceState.putParcelable("android:support:fragments", null);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_polyv_player);
        findIdAndNew();
        initView();
        int playModeCode = getIntent().getIntExtra("playMode", PolyvPlayerMediaController.PlayMode.portrait.getCode());
        PolyvPlayerMediaController.PlayMode playMode = PolyvPlayerMediaController.PlayMode.getPlayMode(playModeCode);
        if (playMode == null)
            playMode = PolyvPlayerMediaController.PlayMode.portrait;
        String vid = "7ac375c1ed34d573a17dc80127835261_7";
//        int bitrate = getIntent().getIntExtra("bitrate", PolyvBitRate.ziDong.getNum());
//        boolean startNow = getIntent().getBooleanExtra("startNow", false);
//        boolean isMustFromLocal = getIntent().getBooleanExtra("isMustFromLocal", false);

        switch (playMode) {
            case landScape:
                mediaController.changeToLandscape();
                break;
            case portrait:
                mediaController.changeToPortrait();
                break;
        }

        play(vid);
    }


    private void findIdAndNew() {
        viewLayout = (RelativeLayout) findViewById(R.id.view_layout);
        pvideoView = (PolyvVideoView) findViewById(R.id.polyv_video_view);
        mediaController = (PolyvPlayerMediaController) findViewById(R.id.polyv_player_media_controller);

        mediaController.initConfig(viewLayout);
        pvideoView.setMediaController(mediaController);
        pvideoView.setAuxiliaryVideoView(mediaController.auxiliaryVideoView);
        pvideoView.setPlayerBufferingIndicator(mediaController.loadingProgress);
    }

    private void initView() {
        pvideoView.setOpenAd(true);
        pvideoView.setOpenTeaser(true);
        pvideoView.setOpenQuestion(true);
        pvideoView.setOpenSRT(true);
        pvideoView.setOpenPreload(true, 2);
        pvideoView.setAutoContinue(true);
        pvideoView.setNeedGestureDetector(true);

        pvideoView.setOnPreparedListener(new IPolyvOnPreparedListener2() {
            @Override
            public void onPrepared() {
                mediaController.preparedView();
            }
        });
        pvideoView.setOnGestureLeftUpListener(new IPolyvOnGestureLeftUpListener() {

            @Override
            public void callback(boolean start, boolean end) {
                Log.d(TAG, String.format("LeftUp %b %b brightness %d", start, end, pvideoView.getBrightness(PolyvPlayerActivity.this)));
                int brightness = pvideoView.getBrightness(PolyvPlayerActivity.this) + 5;
                if (brightness > 100) {
                    brightness = 100;
                }

                pvideoView.setBrightness(PolyvPlayerActivity.this, brightness);
                mediaController.setViewLightValue(brightness, end);
            }
        });

        pvideoView.setOnGestureLeftDownListener(new IPolyvOnGestureLeftDownListener() {

            @Override
            public void callback(boolean start, boolean end) {
                Log.d(TAG, String.format("LeftDown %b %b brightness %d", start, end, pvideoView.getBrightness(PolyvPlayerActivity.this)));
                int brightness = pvideoView.getBrightness(PolyvPlayerActivity.this) - 5;
                if (brightness < 0) {
                    brightness = 0;
                }

                pvideoView.setBrightness(PolyvPlayerActivity.this, brightness);
                mediaController.setViewLightValue(brightness, end);
            }
        });

        pvideoView.setOnGestureRightUpListener(new IPolyvOnGestureRightUpListener() {

            @Override
            public void callback(boolean start, boolean end) {
                Log.d(TAG, String.format("RightUp %b %b volume %d", start, end, pvideoView.getVolume()));
                // 加减单位最小为10，否则无效果
                int volume = pvideoView.getVolume() + 10;
                if (volume > 100) {
                    volume = 100;
                }

                pvideoView.setVolume(volume);
                mediaController.setViewVolumeValue(volume, end);
            }
        });

        pvideoView.setOnGestureRightDownListener(new IPolyvOnGestureRightDownListener() {

            @Override
            public void callback(boolean start, boolean end) {
                Log.d(TAG, String.format("RightDown %b %b volume %d", start, end, pvideoView.getVolume()));
                // 加减单位最小为10，否则无效果
                int volume = pvideoView.getVolume() - 10;
                if (volume < 0) {
                    volume = 0;
                }

                pvideoView.setVolume(volume);
                mediaController.setViewVolumeValue(volume, end);
            }
        });

        pvideoView.setOnGestureSwipeLeftListener(new IPolyvOnGestureSwipeLeftListener() {

            @Override
            public void callback(boolean start, boolean end) {
                // 左滑事件
                Log.d(TAG, String.format("SwipeLeft %b %b", start, end));
                if (fastForwardPos == 0) {
                    fastForwardPos = pvideoView.getCurrentPosition();
                }

                if (end) {
                    if (fastForwardPos < 0)
                        fastForwardPos = 0;
                    pvideoView.seekTo(fastForwardPos);
                    if (pvideoView.isCompletedState()) {
                        pvideoView.start();
                    }
                    fastForwardPos = 0;
                } else {
                    fastForwardPos -= 10000;
                    if (fastForwardPos <= 0)
                        fastForwardPos = -1;
                }
                mediaController.progressView.setViewProgressValue(fastForwardPos, pvideoView.getDuration(), end, false);
            }
        });

        pvideoView.setOnGestureSwipeRightListener(new IPolyvOnGestureSwipeRightListener() {

            @Override
            public void callback(boolean start, boolean end) {
                // 右滑事件
                Log.d(TAG, String.format("SwipeRight %b %b", start, end));
                if (fastForwardPos == 0) {
                    fastForwardPos = pvideoView.getCurrentPosition();
                }

                if (end) {
                    if (fastForwardPos > pvideoView.getDuration())
                        fastForwardPos = pvideoView.getDuration();
                    pvideoView.seekTo(fastForwardPos);
                    if (pvideoView.isCompletedState()) {
                        pvideoView.start();
                    }
                    fastForwardPos = 0;
                } else {
                    fastForwardPos += 10000;
                    if (fastForwardPos > pvideoView.getDuration())
                        fastForwardPos = pvideoView.getDuration();
                }
                mediaController.progressView.setViewProgressValue(fastForwardPos, pvideoView.getDuration(), end, true);
            }
        });

        pvideoView.setOnGestureClickListener(new IPolyvOnGestureClickListener() {
            @Override
            public void callback(boolean start, boolean end) {
                if (pvideoView.isInPlaybackState() && mediaController != null)
                    if (mediaController.isShowing())
                        mediaController.hide();
                    else
                        mediaController.show();
            }
        });
    }

    /**
     * 播放视频
     *
     * @param vid               视频id
     * @param //bitrate         码率（清晰度）
     * @param //startNow        是否现在开始播放视频
     * @param //isMustFromLocal 是否必须从本地（本地缓存的视频）播放
     */
    public void play(final String vid) {
        if (TextUtils.isEmpty(vid)) return;
        if (iv_vlms_cover != null && iv_vlms_cover.getVisibility() == View.VISIBLE)
            iv_vlms_cover.setVisibility(View.GONE);
        pvideoView.release();
        mediaController.hide();
        mediaController.loadingProgress.setVisibility(View.GONE);
        mediaController.auxiliaryVideoView.hide();
        //调用setVid方法视频会自动播放
        pvideoView.setVid(vid);

    }


    @Override
    protected void onResume() {
        super.onResume();
        //回来后继续播放
        if (isPlay) {
            pvideoView.onActivityResume();
        }
        mediaController.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaController.clearGestureInfo();
        mediaController.pause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //弹出去暂停
        isPlay = pvideoView.onActivityStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pvideoView.destroy();
        mediaController.disable();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (PolyvScreenUtils.isLandscape(this) && mediaController != null) {
                mediaController.changeToPortrait();
                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }
}
