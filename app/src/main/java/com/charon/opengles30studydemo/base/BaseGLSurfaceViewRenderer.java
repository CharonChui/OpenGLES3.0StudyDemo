package com.charon.opengles30studydemo.base;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import androidx.annotation.NonNull;
import androidx.annotation.RawRes;

import com.charon.opengles30studydemo.util.ProjectionMatrixUtil;
import com.charon.opengles30studydemo.util.ResReadUtils;
import com.charon.opengles30studydemo.util.ShaderUtils;

public abstract class BaseGLSurfaceViewRenderer implements GLSurfaceView.Renderer {
    protected int mProgram;

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
}
