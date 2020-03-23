package com.charon.opengles30studydemo.circle;

import android.opengl.GLES30;

import com.charon.opengles30studydemo.MyApplication;
import com.charon.opengles30studydemo.R;
import com.charon.opengles30studydemo.base.BaseGLSurfaceViewRenderer;
import com.charon.opengles30studydemo.util.BufferUtil;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class CircleRender extends BaseGLSurfaceViewRenderer {
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
    private static final int POSITION_COMPONENT_COUNT = 3;
    private float circlePosition[];
    /**
     * 颜色占用的向量个数
     */
    private static final int COLOR_COMPONENT_COUNT = 4;
    private float color[];

    public CircleRender() {
        createPositions(1, 60);
        vertexBuffer = BufferUtil.getFloatBuffer(circlePosition);
        colorBuffer = BufferUtil.getFloatBuffer(color);
    }

    private void createPositions(int radius, int n){
        ArrayList<Float> data=new ArrayList<>();
        data.add(0.0f);             //设置圆心坐标
        data.add(0.0f);
        data.add(0.0f);
        float angDegSpan=360f/n;
        for(float i=0;i<360+angDegSpan;i+=angDegSpan){
            data.add((float) (radius*Math.sin(i*Math.PI/180f)));
            data.add((float)(radius*Math.cos(i*Math.PI/180f)));
            data.add(0.0f);
        }
        float[] f=new float[data.size()];
        for (int i=0;i<f.length;i++){
            f[i]=data.get(i);
        }

        circlePosition = f;

        //处理各个顶点的颜色
        color = new float[f.length*4/3];
        ArrayList<Float> tempC = new ArrayList<>();
        ArrayList<Float> totalC = new ArrayList<>();
        tempC.add(1.0f);
        tempC.add(0.0f);
        tempC.add(0.0f);
        tempC.add(1.0f);
        for (int i=0;i<f.length/3;i++){
            totalC.addAll(tempC);
        }

        for (int i=0; i<totalC.size();i++){
            color[i]=totalC.get(i);
        }
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
        glDrawArrays(GLES30.GL_TRIANGLE_FAN, 0, circlePosition.length / POSITION_COMPONENT_COUNT);
        glDisableVertexAttribArray(aPositionLocation);
        glDisableVertexAttribArray(aColorLocation);
    }
}















