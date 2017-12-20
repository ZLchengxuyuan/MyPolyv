package com.lifeng.mypolyv.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.widget.RelativeLayout;

import com.lifeng.mypolyv.R;
import com.lifeng.mypolyv.player.PolyvPlayerMediaController;
import com.lifeng.mypolyv.utils.PolyvScreenUtils;


public class PolyvPlayerActivity extends FragmentActivity {
    private static final String TAG = PolyvPlayerActivity.class.getSimpleName();


    /**
     * 播放器的parentView
     */
    private RelativeLayout viewLayout = null;
    /**
     * 视频控制栏
     */
    private PolyvPlayerMediaController mediaController = null;

//    /**
//     * 播放主视频播放器
//     */
//    private PolyvVideoView pvideoView = null;

    private boolean isPlay = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null)
            savedInstanceState.putParcelable("android:support:fragments", null);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_polyv_player);
        findIdAndNew();
        mediaController.initVideoView();

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

        mediaController.play(vid);

    }


    private void findIdAndNew() {
        viewLayout = (RelativeLayout) findViewById(R.id.view_layout);
        // pvideoView = (PolyvVideoView) findViewById(R.id.polyv_video_view);
        mediaController = (PolyvPlayerMediaController) findViewById(R.id.polyv_player_media_controller);
        mediaController.initConfig(viewLayout);
        mediaController.pvideoView.setMediaController(mediaController);
        mediaController.pvideoView.setAuxiliaryVideoView(mediaController.auxiliaryVideoView);
        mediaController.pvideoView.setPlayerBufferingIndicator(mediaController.loadingProgress);
    }


    @Override
    protected void onResume() {
        super.onResume();
        //回来后继续播放
        if (isPlay) {
            mediaController.pvideoView.onActivityResume();
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
        isPlay = mediaController.pvideoView.onActivityStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaController.pvideoView.destroy();
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
