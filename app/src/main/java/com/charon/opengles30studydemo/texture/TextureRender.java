package com.charon.opengles30studydemo.texture;

import android.opengl.GLES20;
import android.opengl.GLES30;

import com.charon.opengles30studydemo.MyApplication;
import com.charon.opengles30studydemo.R;
import com.charon.opengles30studydemo.base.BaseGLSurfaceViewRenderer;
import com.charon.opengles30studydemo.util.BufferUtil;
import com.charon.opengles30studydemo.util.TextureUtil;

import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class TextureRender extends BaseGLSurfaceViewRenderer {
    private final FloatBuffer vertextBuffer;
    private final FloatBuffer textureBuffer;

    private int textureId;
    private int aPositionLocation;
    private int aTextureLocation;
    private int uSamplerTextureLocation;

    /**
     * 坐标占用的向量个数
     */
    private static final int POSITION_COMPONENT_COUNT = 2;
    // 逆时针顺序排列
    private static final float[] POINT_DATA = {
            -0.5f, -0.5f,
            -0.5f, 0.5f,
            0.5f, 0.5f,
            0.5f, -0.5f,
    };
    /**
     * 颜色占用的向量个数
     */
    private static final int TEXTURE_COMPONENT_COUNT = 2;
    // 纹理坐标(s, t)，t坐标方向和顶点y坐标反着
    private static final float[] TEXTURE_DATA = {
            0.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f,
            1.0f, 1.0f
    };

    public TextureRender() {
        vertextBuffer = BufferUtil.getFloatBuffer(POINT_DATA);
        textureBuffer = BufferUtil.getFloatBuffer(TEXTURE_DATA);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        handleProgram(MyApplication.getInstance(), R.raw.texture_vertex_shader, R.raw.texture_fragment_shader);
        aPositionLocation = glGetAttribLocation("vPosition");
        aTextureLocation = glGetAttribLocation("aTextureCoord");
        uSamplerTextureLocation = glGetUniformLocation("uTextureUnit");
        textureId = TextureUtil.loadTexture(MyApplication.getInstance(), R.drawable.img).getTextureId();
        glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GLES30.GL_FLOAT, false, 0, vertextBuffer);
        glVertexAttribPointer(aTextureLocation, TEXTURE_COMPONENT_COUNT, GLES30.GL_FLOAT, false, 0, textureBuffer);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);
        orthoM("u_Matrix", width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GLES30.GL_COLOR_BUFFER_BIT);
        /****************/
        // 中间这一部分代码的意思: 采样器统一变量将加载一个指定纹理绑定的纹理单元的数值，
        // 例如，用数值0指定采样器表示从单元GL_TEXTURE0读取，指定数值1表示从GL_TEXTURE1读取，以此类推。
        // 激活纹理单元后需要把它和纹理Id绑定，然后再通过GLES30.glUniform1i()方法传递给着色器中。


        // 下面这两句表示纹理如何绑定到纹理单元。设置当前活动的纹理单元为纹理单元0，并将纹理ID绑定到当前活动的纹理单元上
        glActiveTexture(GLES30.GL_TEXTURE0);
        glBindTexture(GLES30.GL_TEXTURE_2D, textureId);
        // 将采样器绑定到纹理单元，0就表示GLES30.GL_TEXTURE0
        glUniform1i(uSamplerTextureLocation, 0);
        /****************/
        glEnableVertexAttribArray(aPositionLocation);
        glEnableVertexAttribArray(aTextureLocation);
        glDrawArrays(GLES30.GL_TRIANGLE_FAN, 0, POINT_DATA.length / POSITION_COMPONENT_COUNT);
        glDisableVertexAttribArray(aPositionLocation);
        glDisableVertexAttribArray(aTextureLocation);
    }
}
