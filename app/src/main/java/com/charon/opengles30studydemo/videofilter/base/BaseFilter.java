package com.charon.opengles30studydemo.videofilter.base;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLES30;

import androidx.annotation.NonNull;
import androidx.annotation.RawRes;

import com.charon.opengles30studydemo.MyApplication;
import com.charon.opengles30studydemo.R;
import com.charon.opengles30studydemo.util.ProjectionMatrixUtil;
import com.charon.opengles30studydemo.util.ResReadUtils;
import com.charon.opengles30studydemo.util.ShaderUtils;

import java.nio.FloatBuffer;
import java.util.LinkedList;

public class BaseFilter {
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int TEXTURE_COMPONENT_COUNT = 2;

    protected int mProgram;
    @RawRes
    private int mVertexShaderResId;
    @RawRes
    private int mFragmentShaderResId;

    private boolean mInited;

    private int vertexPosition;
    private int texturePosition;
    private int samplerTexturePosition;

    //渲染线程
    private LinkedList<Runnable> mRunOnDraw = new LinkedList();

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
            mInited = true;
            onInited();
        }
    }

    public void onInit() {
        handleProgram(MyApplication.getInstance(), mVertexShaderResId, mFragmentShaderResId);
        vertexPosition = glGetAttribLocation("vPosition");
        texturePosition = glGetAttribLocation("vCoordPosition");
        samplerTexturePosition = glGetUniformLocation("uSamplerTexture");
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

    public void onDraw(final int textureId, final FloatBuffer mVertextBuffer,
                       final FloatBuffer mTextureBuffer) {
        runPendingOnDrawTasks();
        if (!mInited) {
            return;
        }

        glVertexAttribPointer(vertexPosition, POSITION_COMPONENT_COUNT, GLES30.GL_FLOAT, false, 0, mVertextBuffer);
        glVertexAttribPointer(texturePosition, TEXTURE_COMPONENT_COUNT, GLES30.GL_FLOAT, false, 0, mTextureBuffer);
        GLES30.glEnableVertexAttribArray(vertexPosition);
        GLES30.glEnableVertexAttribArray(texturePosition);
        GLES30.glUniform1i(samplerTexturePosition, 0);
//        if (textureId != GL.NO_TEXTURE) {
//            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
//            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
//            GLES20.glUniform1i(glUniformTexture, 0);
//        }

        onDrawArraysPre();
        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_STRIP, 0, 4);
        GLES30.glDisableVertexAttribArray(vertexPosition);
        GLES30.glDisableVertexAttribArray(texturePosition);
    }

    protected void runPendingOnDrawTasks() {
        while (!mRunOnDraw.isEmpty()) {
            mRunOnDraw.removeFirst().run();
        }
    }


    /**
     * 设置着色器中对象float值
     */
    protected void setFloat(final int location, final float floatValue) {
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                GLES30.glUniform1f(location, floatValue);
            }
        });
    }

    /**
     * 设置着色器中对象组值float值
     */
    protected void setFloatVec2(final int location, final float[] arrayValue) {
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                GLES30.glUniform2fv(location, 1, FloatBuffer.wrap(arrayValue));
            }
        });
    }

    /**
     * 设置着色中数组值
     */
    protected void setFloatVec3(final int location, final float[] arrayValue) {
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                GLES20.glUniform3fv(location, 1, FloatBuffer.wrap(arrayValue));
            }
        });
    }

    /**
     * 设置着色器中对象组值float值
     */
    protected void setFloatVec4(final int location, final float[] arrayValue) {
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                GLES20.glUniform4fv(location, 1, FloatBuffer.wrap(arrayValue));
            }
        });
    }

    /**
     * 设置着色器中3维矩阵的值
     */
    protected void setUniformMatrix3f(final int location, final float[] matrix) {
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                GLES20.glUniformMatrix3fv(location, 1, false, matrix, 0);
            }
        });
    }

    /**
     * 设置着色器中4维矩阵的值
     */
    protected void setUniformMatrix4f(final int location, final float[] matrix) {
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                GLES20.glUniformMatrix4fv(location, 1, false, matrix, 0);
            }
        });
    }


    protected void runOnDraw(Runnable runnable) {
        synchronized (mRunOnDraw) {
            mRunOnDraw.addLast(runnable);
        }
    }

    protected void onDrawArraysPre() {

    }


    protected void glViewport(int x, int y, int width, int height) {
        GLES30.glViewport(x, y, width, height);
    }

    protected void glClearColor(float red, float green, float blue, float alpha) {
        GLES30.glClearColor(red, green, blue, alpha);
    }

    protected void glClear(int mask) {
        GLES30.glClear(mask);
    }

    protected static void glEnableVertexAttribArray(int index) {
        GLES30.glEnableVertexAttribArray(index);
    }

    protected void glDisableVertexAttribArray(int index) {
        GLES30.glDisableVertexAttribArray(index);
    }

    protected int glGetAttribLocation(String name) {
        return GLES30.glGetAttribLocation(mProgram, name);
    }

    protected int glGetUniformLocation(String name) {
        return GLES30.glGetUniformLocation(mProgram, name);
    }

    protected void glUniformMatrix4fv(int location, int count, boolean transpose, float[] value, int offset) {
        GLES30.glUniformMatrix4fv(location, count, transpose, value, offset);
    }

    protected void glDrawArrays(int mode, int first, int count) {
        GLES30.glDrawArrays(mode, first, count);
    }

    protected void glDrawElements(int mode, int count, int type, java.nio.Buffer indices) {
        GLES30.glDrawElements(mode, count, type, indices);
    }

    protected void orthoM(String name, int width, int height) {
        ProjectionMatrixUtil.orthoM(mProgram, width, height, name);
    }

    protected void glVertexAttribPointer(
            int indx,
            int size,
            int type,
            boolean normalized,
            int stride,
            java.nio.Buffer ptr) {
        GLES30.glVertexAttribPointer(indx, size, type, normalized, stride, ptr);
    }

    protected void glActiveTexture(int texture) {
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
    }

    protected void glBindTexture(int target, int texture) {
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texture);
    }

    protected void glUniform1i(int location, int x) {
        GLES20.glUniform1i(location, x);
    }

    public int getProgram() {
        return mProgram;
    }
}
