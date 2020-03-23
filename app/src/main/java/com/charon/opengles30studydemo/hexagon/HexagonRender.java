package com.charon.opengles30studydemo.hexagon;

import android.opengl.GLES20;
import android.opengl.GLES30;

import com.charon.opengles30studydemo.MyApplication;
import com.charon.opengles30studydemo.R;
import com.charon.opengles30studydemo.base.BaseGLSurfaceViewRenderer;
import com.charon.opengles30studydemo.util.BufferUtil;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class HexagonRender extends BaseGLSurfaceViewRenderer {
    //顶点位置缓存
    private final FloatBuffer vertexBuffer;
    //顶点颜色缓存
    private final FloatBuffer colorBuffer;
    // 顶点索引缓存
    private final ShortBuffer indexBuffer;
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
            0.5f, 0.5f,
            -0.5f, 0.5f,
            0f, -1.0f,
            0f, 1.0f
    };

    /**
     * 数组绘制的索引:当前是绘制三角形，所以是3个元素构成一个绘制顺序
     */
    private static final short[] INDEX_DATA = {
            0, 1, 2,
            0, 2, 3,
            0, 4, 1,
            3, 2, 5
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

    public HexagonRender() {
        vertexBuffer = BufferUtil.getFloatBuffer(POINT_DATA);
        colorBuffer = BufferUtil.getFloatBuffer(COLOR_DATA);
        indexBuffer = BufferUtil.getShortBuffer(INDEX_DATA);
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
        // 绘制相对复杂的图形时，若顶点有较多重复时，对比数据占用空间而言，glDrawElements会比glDrawArrays小很多，也会更高效
        // 因为在有重复顶点的情况下，glDrawArrays方式需要的3个顶点位置是用Float型的，占3*4的Byte值；
        // 而glDrawElements需要3个Short型的，占3*2Byte值
        // 1. 图形绘制方式； 2. 绘制的顶点数； 3. 索引的数据格式； 4. 索引的数据Buffer
        glDrawElements(GLES30.GL_TRIANGLES, INDEX_DATA.length,
                GLES30.GL_UNSIGNED_SHORT, indexBuffer);
        glDisableVertexAttribArray(aPositionLocation);
        glDisableVertexAttribArray(aColorLocation);
    }
}















