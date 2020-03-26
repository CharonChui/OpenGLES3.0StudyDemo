package com.charon.opengles30studydemo.videofilter.base;

import android.content.Context;
import android.opengl.GLES30;

import androidx.annotation.NonNull;
import androidx.annotation.RawRes;

import com.charon.opengles30studydemo.MyApplication;
import com.charon.opengles30studydemo.R;
import com.charon.opengles30studydemo.util.ResReadUtils;
import com.charon.opengles30studydemo.util.ShaderUtils;

import java.nio.FloatBuffer;

public class BaseFilter {
    protected int mProgram;
    @RawRes
    private int mVertexShaderResId;
    @RawRes
    private int mFragmentShaderResId;

    private boolean mInited;

    public BaseFilter() {
        this(R.raw.video_no_filter_vertex_shader, R.raw.video_no_filter_fragment_shader);
    }

    public BaseFilter(@RawRes int vertexShaderResId, @RawRes int fragmentShaderResId) {
        mVertexShaderResId = vertexShaderResId;
        mFragmentShaderResId = fragmentShaderResId;
    }

    public void init() {
        if (!mInited) {
            onInit();
            onInited();
        }
    }

    public void onInit() {
        handleProgram(MyApplication.getInstance(), mVertexShaderResId, mFragmentShaderResId);
    }

    /**
     * readResource -> compileShader -> linkProgram -> useProgram
     *
     * @param context
     * @param vertexShader
     * @param fragmentShader
     */
    protected void handleProgram(@NonNull Context context, @RawRes int vertexShader, @RawRes int fragmentShader) {
        String vertexShaderStr = ResReadUtils.readResource(context, vertexShader);
        int vertexShaderId = ShaderUtils.compileVertexShader(vertexShaderStr);
        //编译片段着色程序
        String fragmentShaderStr = ResReadUtils.readResource(context, fragmentShader);
        int fragmentShaderId = ShaderUtils.compileFragmentShader(fragmentShaderStr);
        //连接程序
        mProgram = ShaderUtils.linkProgram(vertexShaderId, fragmentShaderId);
        //在OpenGLES环境中使用程序
        GLES30.glUseProgram(mProgram);
    }

    public void onInited() {

    }

    public final void destroy() {
        mInited = false;
        GLES30.glDeleteProgram(mProgram);
        onDestroy();
    }

    public void onDestroy() {

    }

    public void onDraw(final int textureId, final FloatBuffer cubeBuffer,
                       final FloatBuffer textureBuffer) {

    }









}
