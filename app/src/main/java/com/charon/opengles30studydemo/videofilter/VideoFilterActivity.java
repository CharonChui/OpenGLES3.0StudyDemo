package com.charon.opengles30studydemo.videofilter;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;

import androidx.annotation.Nullable;

import com.charon.opengles30studydemo.R;

public class VideoFilterActivity extends Activity {
    private GLSurfaceView mGLSurfaceView;
    private VideoFilterRender mVideoFilterRender;
    private Surface mSurface;
    private MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_filter);
        mGLSurfaceView = findViewById(R.id.mGLSurfaceView);
        mGLSurfaceView.setEGLContextClientVersion(3);
        mVideoFilterRender = new VideoFilterRender(mGLSurfaceView);
        mVideoFilterRender.setIVideoTextureRenderListener(new VideoFilterRender.IVideoTextureRenderListener() {
            @Override
            public void onCreate(SurfaceTexture surfaceTexture) {
                mSurface = new Surface(surfaceTexture);
                startPlay();
            }
        });
        mGLSurfaceView.setRenderer(mVideoFilterRender);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSurface != null && mSurface.isValid()) {
            startPlay();
        }
        if (mGLSurfaceView != null) {
            mGLSurfaceView.onResume();
        }
    }

    private void startPlay() {
        if (mMediaPlayer != null && mSurface != null && mSurface.isValid()) {
            mMediaPlayer.setSurface(mSurface);
            mMediaPlayer.start();
            return;
        }
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer = MediaPlayer.create(this, R.raw.beauty);
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    if (mp != null) {
                        mp.start();
                    }
                }
            });
            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    return false;
                }
            });
            mMediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                @Override
                public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                    Log.e("@@@", "onVideoSizeChanged width : " + width + "...height..." + height);
                    mVideoFilterRender.setVideoSize(width, height);
                }
            });
            // 将surface设置给mediaplayer
            mMediaPlayer.setSurface(mSurface);
            mSurface.release();
            mMediaPlayer.setScreenOnWhilePlaying(true);
            mMediaPlayer.setLooping(true);
            mMediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGLSurfaceView != null) {
            mGLSurfaceView.onPause();
        }
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}
