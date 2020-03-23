package com.charon.opengles30studydemo.isosceletriangle;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.charon.opengles30studydemo.R;
import com.charon.opengles30studydemo.view.GLTextureView;

public class IsoTriangleActivity extends Activity {
    private GLTextureView mGLTextureView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iso_triangle);
        mGLTextureView = findViewById(R.id.mGLTextureView);
        IsoTriangleRender render = new IsoTriangleRender();
        mGLTextureView.setEGLContextClientVersion(3);
        mGLTextureView.setRenderer(render);
        mGLTextureView.setRenderMode(GLTextureView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLTextureView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLTextureView.onResume();
    }
}
