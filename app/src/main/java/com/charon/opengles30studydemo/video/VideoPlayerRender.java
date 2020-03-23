package com.charon.opengles30studydemo.video;

import android.graphics.SurfaceTexture;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import com.charon.opengles30studydemo.MyApplication;
import com.charon.opengles30studydemo.R;
import com.charon.opengles30studydemo.base.BaseGLSurfaceViewRenderer;
import com.charon.opengles30studydemo.util.BufferUtil;
import com.charon.opengles30studydemo.util.TextureUtil;

import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class VideoPlayerRender extends BaseGLSurfaceViewRenderer {
    private int mTextureId;
    private SurfaceTexture mSurfaceTexture;
    private GLSurfaceView mGLSurfaceView;
    private boolean mUpdateSurfaceTexture;
    private FloatBuffer mVertextBuffer;
    private FloatBuffer mTextureBuffer;
    private int vertexPosition;
    private int texturePosition;
    private int samplerTexturePosition;

    /**
     * 坐标占用的向量个数
     */
    private static final int POSITION_COMPONENT_COUNT = 2;
    // 逆时针顺序排列
    private static final float[] POINT_DATA = {
            -1f, -1f,
            1f, -1f,
            -1f, 1f,
            1f, 1f,
    };
    /**
     * 颜色占用的向量个数
     */
    private static final int TEXTURE_COMPONENT_COUNT = 2;
    // 纹理坐标(s, t)，t坐标方向和顶点y坐标反着
    private static final float[] TEXTURE_DATA = {
            0.0f, 1.0f,
            1.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f
    };

    public VideoPlayerRender(GLSurfaceView surfaceView) {
        mGLSurfaceView = surfaceView;
        mVertextBuffer = BufferUtil.getFloatBuffer(POINT_DATA);
        mTextureBuffer = BufferUtil.getFloatBuffer(TEXTURE_DATA);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        handleProgram(MyApplication.getInstance(), R.raw.video_vertex_shader, R.raw.video_fragment_shader);
        vertexPosition = glGetAttribLocation("vPosition");
        texturePosition = glGetAttribLocation("vCoordPosition");
        samplerTexturePosition = glGetUniformLocation("uSamplerTexture");
        mTextureId = TextureUtil.createOESTextureId();
        mSurfaceTexture = new SurfaceTexture(mTextureId);
        mSurfaceTexture.setDefaultBufferSize(mGLSurfaceView.getWidth(), mGLSurfaceView.getHeight());
        mSurfaceTexture.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
            @Override
            public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                mUpdateSurfaceTexture = true;
                if (mGLSurfaceView != null) {
                    mGLSurfaceView.requestRender();
                }
            }
        });
        if (mTextureRenderListener != null) {
            mTextureRenderListener.onCreate(mSurfaceTexture);
        }

        glVertexAttribPointer(vertexPosition, POSITION_COMPONENT_COUNT, GLES30.GL_FLOAT, false, 0, mVertextBuffer);
        glVertexAttribPointer(texturePosition, TEXTURE_COMPONENT_COUNT, GLES30.GL_FLOAT, false, 0, mTextureBuffer);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);
        synchronized (this) {
            if (mUpdateSurfaceTexture) {
                mSurfaceTexture.updateTexImage();
                mUpdateSurfaceTexture = false;
            }
        }
        GLES30.glEnableVertexAttribArray(vertexPosition);
        GLES30.glEnableVertexAttribArray(texturePosition);
        GLES30.glUniform1i(samplerTexturePosition, 0);
        // 绘制
        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_STRIP, 0, 4);
        GLES30.glFlush();
        GLES30.glDisableVertexAttribArray(vertexPosition);
        GLES30.glDisableVertexAttribArray(texturePosition);

    }

    private IVideoTextureRenderListener mTextureRenderListener;

    public void setIVideoTextureRenderListener(IVideoTextureRenderListener render) {
        mTextureRenderListener = render;
    }

    public interface IVideoTextureRenderListener {
        void onCreate(SurfaceTexture surfaceTexture);
    }
}
