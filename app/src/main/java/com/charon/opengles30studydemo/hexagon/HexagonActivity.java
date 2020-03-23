package com.charon.opengles30studydemo.hexagon;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.charon.opengles30studydemo.R;
import com.charon.opengles30studydemo.triangle.TriangleRender;

public class HexagonActivity extends Activity {
    private GLSurfaceView mGlSurfaceView;
    private HexagonRender mHexagonRender;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hexagon);
        mGlSurfaceView = findViewById(R.id.mGLSurfaceView);
        // OpenGL ES 3.0版本
        mGlSurfaceView.setEGLContextClientVersion(3);
        mHexagonRender = new HexagonRender();
        mGlSurfaceView.setRenderer(mHexagonRender);
        mGlSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
}
