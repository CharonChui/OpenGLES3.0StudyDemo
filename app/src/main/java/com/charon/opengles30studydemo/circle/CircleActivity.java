package com.charon.opengles30studydemo.circle;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.charon.opengles30studydemo.R;
import com.charon.opengles30studydemo.triangle.TriangleRender;

public class CircleActivity extends Activity {
    private GLSurfaceView mGlSurfaceView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle);
        mGlSurfaceView = findViewById(R.id.mGLSurfaceView);
        // OpenGL ES 3.0版本
        mGlSurfaceView.setEGLContextClientVersion(3);
        mGlSurfaceView.setRenderer(new CircleRender());
        mGlSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
}
