package com.lifeng.mypolyv.player;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.easefun.polyvsdk.video.PolyvVideoView;
import com.easefun.polyvsdk.video.auxiliary.PolyvAuxiliaryVideoView;
import com.lifeng.mypolyv.R;

/**
 * Created by Administrator on 2017/12/25.
 */

public class PolyvPlayerVideoViewController extends FrameLayout {
    /**
     * 播放主视频播放器
     */
    private PolyvVideoView videoView = null;
    /**
     * 视频广告，视频片头加载缓冲视图
     */
    public ProgressBar auxiliaryLoadingProgress = null;
    /**
     * 用于播放广告片头的播放器
     */
    public PolyvAuxiliaryVideoView auxiliaryVideoView = null;
    /**
     * 视频加载缓冲视图
     */
    public ProgressBar loadingProgress = null;
    /**
     * 手势出现的进度界面
     */
    public PolyvPlayerProgressView progressView = null;
    /**
     * 手势出现的亮度界面
     */
    public PolyvPlayerLightView lightView = null;
    /**
     * 手势出现的音量界面
     */
    public PolyvPlayerVolumeView volumeView = null;
    private View view;

    public PolyvPlayerVideoViewController(@NonNull Context context) {
        super(context);
        this.view = LayoutInflater.from(context).inflate(R.layout.polyv_controller_video_view, this);
        findIdAndNew();
    }

    public PolyvPlayerVideoViewController(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.view = LayoutInflater.from(context).inflate(R.layout.polyv_controller_video_view, this);
        findIdAndNew();
    }

    public PolyvPlayerVideoViewController(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.view = LayoutInflater.from(context).inflate(R.layout.polyv_controller_video_view, this);
        findIdAndNew();
    }

    private void findIdAndNew() {
        videoView = (PolyvVideoView) findViewById(R.id.polyv_video_view);
        lightView = (PolyvPlayerLightView) findViewById(R.id.polyv_player_light_view);
        volumeView = (PolyvPlayerVolumeView) findViewById(R.id.polyv_player_volume_view);
        progressView = (PolyvPlayerProgressView) findViewById(R.id.polyv_player_progress_view);
        loadingProgress = (ProgressBar) findViewById(R.id.loading_progress);
        auxiliaryVideoView = (PolyvAuxiliaryVideoView) findViewById(R.id.polyv_auxiliary_video_view);
        auxiliaryLoadingProgress = (ProgressBar) findViewById(R.id.auxiliary_loading_progress);

    }

    public void clearGestureInfo() {
        videoView.clearGestureInfo();
        progressView.hide();
        volumeView.hide();
        lightView.hide();
    }

    /**
     * 播放模式
     *
     * @author TanQu
     */
    public enum PlayMode {
        /**
         * 横屏
         */
        landScape(3),
        /**
         * 竖屏
         */
        portrait(4);

        private final int code;

        private PlayMode(int code) {
            this.code = code;
        }

        /**
         * 取得类型对应的code
         *
         * @return
         */
        public int getCode() {
            return code;
        }

        public static PlayMode getPlayMode(int code) {
            switch (code) {
                case 3:
                    return landScape;
                case 4:
                    return portrait;
            }

            return null;
        }
    }
}
