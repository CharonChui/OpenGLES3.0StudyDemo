package com.charon.opengles30studydemo.triangle;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.charon.opengles30studydemo.R;

public class TriangleActivity extends Activity {
    private GLSurfaceView mGlSurfaceView;
    private TriangleRender mTriangleRender;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_triangle);
        mGlSurfaceView = findViewById(R.id.mGLSurfaceView);
        // OpenGL ES 3.0版本
        mGlSurfaceView.setEGLContextClientVersion(3);
        mTriangleRender = new TriangleRender();
        mGlSurfaceView.setRenderer(mTriangleRender);
        mGlSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGlSurfaceView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGlSurfaceView.onResume();
    }
}
