package com.charon.opengles30studydemo.video;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Display;
import android.view.Surface;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.charon.opengles30studydemo.R;

public class VideoPlayerActivity extends Activity {
    private GLSurfaceView mGLSurfaceView;
    private Surface mSurface;
    private MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGLSurfaceView = new GLSurfaceView(this);
        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = (int) ((width) * (9 / 16f));
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(width, height);
        mGLSurfaceView.setLayoutParams(layoutParams);
        setContentView(mGLSurfaceView);
        mGLSurfaceView.setEGLContextClientVersion(3);
        VideoPlayerRender videoPlayerRender = new VideoPlayerRender(mGLSurfaceView);
        videoPlayerRender.setIVideoTextureRenderListener(new VideoPlayerRender.IVideoTextureRenderListener() {
            @Override
            public void onCreate(SurfaceTexture surfaceTexture) {
                mSurface = new Surface(surfaceTexture);
                startPlay();
            }
        });
        mGLSurfaceView.setRenderer(videoPlayerRender);
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
