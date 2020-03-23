package com.charon.opengles30studydemo.multitexture;

import android.opengl.GLES20;
import android.opengl.GLES30;

import com.charon.opengles30studydemo.MyApplication;
import com.charon.opengles30studydemo.R;
import com.charon.opengles30studydemo.base.BaseGLSurfaceViewRenderer;
import com.charon.opengles30studydemo.util.BufferUtil;
import com.charon.opengles30studydemo.util.TextureUtil;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MultiTextureRender2 extends BaseGLSurfaceViewRenderer {
    private final FloatBuffer vertextBuffer;
    private final FloatBuffer textureBuffer;
    private final ShortBuffer indiceBuffer;
//    private final FloatBuffer textureBuffer2;

    private int textureId;
    private int textureId2;
    private int aPositionLocation;
    private int aTextureLocation;
    //    private int aTextureLocation2;
    private int uSamplerTextureLocation;
    private int uSamplerTextureLocation2;

    /**
     * 坐标占用的向量个数
     */
    private static final int POSITION_COMPONENT_COUNT = 2;
    // 逆时针顺序排列
    private static final float[] POINT_DATA = {
            -0.5f, 0.5f,
            -0.5f, -0.5f,
            0.5f, -0.5f,
            0.5f, 0.5f
    };
    private final short[] INDICES_DATA = {0, 1, 2, 0, 2, 3};

    /**
     * 颜色占用的向量个数
     */
    private static final int TEXTURE_COMPONENT_COUNT = 2;
    // 纹理坐标(s, t)，t坐标方向和顶点y坐标反着
    private static final float[] TEXTURE_DATA = {
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f
    };

//    private static final float[] TEXTURE_DATA2 = {
//            0.0f, 1.0f,
//            0.0f, 0.0f,
//            1.0f, 0.0f,
//            1.0f, 1.0f
//    };


    public MultiTextureRender2() {
        vertextBuffer = BufferUtil.getFloatBuffer(POINT_DATA);
        textureBuffer = BufferUtil.getFloatBuffer(TEXTURE_DATA);
        indiceBuffer = BufferUtil.getShortBuffer(INDICES_DATA);
//        textureBuffer2 = BufferUtil.getFloatBuffer(TEXTURE_DATA2);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        handleProgram(MyApplication.getInstance(), R.raw.multi2_texture_vertex_shader, R.raw.multi2_texture_fragment_shader);
        aPositionLocation = glGetAttribLocation("vPosition");
        aTextureLocation = glGetAttribLocation("aTextureCoord");
//        aTextureLocation2 = glGetAttribLocation("aTextureCoord2");
        uSamplerTextureLocation = glGetUniformLocation("uTextureUnit");
        uSamplerTextureLocation2 = glGetUniformLocation("uTextureUnit2");

        textureId = TextureUtil.loadTexture(MyApplication.getInstance(), R.drawable.img2).getTextureId();
        textureId2 = TextureUtil.loadTexture(MyApplication.getInstance(), R.drawable.img3).getTextureId();

        // 开启纹理透明混合，这样才能绘制透明图片
        GLES30.glEnable(GL10.GL_BLEND);
        GLES30.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
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
        glEnableVertexAttribArray(aTextureLocation);
        glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GLES30.GL_FLOAT, false, 0, vertextBuffer);
        glVertexAttribPointer(aTextureLocation, TEXTURE_COMPONENT_COUNT, GLES30.GL_FLOAT, false, 0, textureBuffer);

//        glVertexAttribPointer(aTextureLocation2, TEXTURE_COMPONENT_COUNT, GLES30.GL_FLOAT, false, 0, textureBuffer2);
        /****************/
        // 中间这一部分代码的意思: 采样器统一变量将加载一个指定纹理绑定的纹理单元的数值，
        // 例如，用数值0指定采样器表示从单元GL_TEXTURE0读取，指定数值1表示从GL_TEXTURE1读取，以此类推。
        // 激活纹理单元后需要把它和纹理Id绑定，然后再通过GLES30.glUniform1i()方法传递给着色器中。


        // 下面这两句表示纹理如何绑定到纹理单元。设置当前活动的纹理单元为纹理单元0，并将纹理ID绑定到当前活动的纹理单元上
        glActiveTexture(GLES30.GL_TEXTURE0);
        glBindTexture(GLES30.GL_TEXTURE_2D, textureId);
        // 将采样器绑定到纹理单元，0就表示GLES30.GL_TEXTURE0
        glUniform1i(uSamplerTextureLocation, 0);

        glActiveTexture(GLES30.GL_TEXTURE1);
        glBindTexture(GLES30.GL_TEXTURE_2D, textureId2);
        // 将采样器绑定到纹理单元，0就表示GLES30.GL_TEXTURE0
        glUniform1i(uSamplerTextureLocation2, 1);
        /****************/

//        glEnableVertexAttribArray(aTextureLocation2);
        GLES30.glDrawElements(GLES30.GL_TRIANGLES, 6, GLES30.GL_UNSIGNED_SHORT, indiceBuffer);
        glDisableVertexAttribArray(aPositionLocation);
        glDisableVertexAttribArray(aTextureLocation);
//        glDisableVertexAttribArray(aTextureLocation2);
    }
}
