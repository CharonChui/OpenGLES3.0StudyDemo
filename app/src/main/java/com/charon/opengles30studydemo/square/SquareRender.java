package com.charon.opengles30studydemo.square;

import android.opengl.GLES30;

import com.charon.opengles30studydemo.MyApplication;
import com.charon.opengles30studydemo.R;
import com.charon.opengles30studydemo.base.BaseGLSurfaceViewRenderer;
import com.charon.opengles30studydemo.util.BufferUtil;

import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class SquareRender extends BaseGLSurfaceViewRenderer {
    //顶点位置缓存
    private final FloatBuffer vertexBuffer;
    //顶点颜色缓存
    private final FloatBuffer colorBuffer;
    //位置
    private int aPositionLocation;
    //颜色
    private int aColorLocation;

    /**
     * 坐标占用的向量个数
     */
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final float[] POINT_DATA = {
            -0.5f, -0.5f,
            0.5f, -0.5f,
            -0.5f, 0.5f,
            0.5f, 0.5f,
    };

    /**
     * 颜色占用的向量个数
     */
    private static final int COLOR_COMPONENT_COUNT = 4;
    private static final float[] COLOR_DATA = {
            // 一个顶点有3个向量数据：r、g、b、a
            1f, 0.5f, 0.5f, 0f,
            1f, 0f, 1f, 0f,
            0f, 1f, 1f, 0f,
            1f, 1f, 0f, 0f
    };

    public SquareRender() {
        vertexBuffer = BufferUtil.getFloatBuffer(POINT_DATA);
        colorBuffer = BufferUtil.getFloatBuffer(COLOR_DATA);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        handleProgram(MyApplication.getInstance(), R.raw.iso_triangle_vertex_shader, R.raw.iso_triangle_fragment_shader);
        aPositionLocation = glGetAttribLocation("vPosition");
        aColorLocation = glGetAttribLocation("aColor");
        glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GLES30.GL_FLOAT, false, 0, vertexBuffer);
        glVertexAttribPointer(aColorLocation, COLOR_COMPONENT_COUNT, GLES30.GL_FLOAT, false, 0, colorBuffer);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);
        orthoM("u_Matrix", width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GLES30.GL_COLOR_BUFFER_BIT);
        glEnableVertexAttribArray(aPositionLocation);
        glEnableVertexAttribArray(aColorLocation);
        // 正方形、四个点(POINT_DATA.length / POSITION_COMPONENT_COUNT)
        glDrawArrays(GLES30.GL_TRIANGLE_STRIP, 0, POINT_DATA.length / POSITION_COMPONENT_COUNT);
        glDisableVertexAttribArray(aPositionLocation);
        glDisableVertexAttribArray(aColorLocation);
    }
}















