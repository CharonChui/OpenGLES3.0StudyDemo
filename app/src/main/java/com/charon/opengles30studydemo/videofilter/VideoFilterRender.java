package com.charon.opengles30studydemo.videofilter;

import android.graphics.SurfaceTexture;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.charon.opengles30studydemo.MyApplication;
import com.charon.opengles30studydemo.R;
import com.charon.opengles30studydemo.base.BaseGLSurfaceViewRenderer;
import com.charon.opengles30studydemo.util.BufferUtil;
import com.charon.opengles30studydemo.util.TextureUtil;
import com.charon.opengles30studydemo.videofilter.base.BaseFilter;

import java.nio.FloatBuffer;
import java.util.LinkedList;
import java.util.Queue;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class VideoFilterRender extends BaseGLSurfaceViewRenderer {
    private int mTextureId;
    private SurfaceTexture mSurfaceTexture;
    private GLSurfaceView mGLSurfaceView;
    private boolean mUpdateSurfaceTexture;
    private FloatBuffer mVertextBuffer;
    private FloatBuffer mTextureBuffer;
    private BaseFilter mFilter = new BaseFilter();
    /**
     * 视频的宽高
     */
    private int mVideoWidth;
    private int mVideoHeight;
    /**
     * 需改更改渲染的大小
     */
    private boolean mNeedUpdateSize;
    /**
     * Surface的宽高
     */
    private int mSurfaceWidth;
    private int mSurfaceHeight;

    // 逆时针顺序排列
    private static final float[] POINT_DATA = {
            -1f, -1f,
            1f, -1f,
            -1f, 1f,
            1f, 1f,
    };
    // 纹理坐标(s, t)，t坐标方向和顶点y坐标反着
    private static final float[] TEXTURE_DATA = {
            0.0f, 1.0f,
            1.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f
    };

    public VideoFilterRender(GLSurfaceView surfaceView) {
        mGLSurfaceView = surfaceView;
        mVertextBuffer = BufferUtil.getFloatBuffer(POINT_DATA);
        mTextureBuffer = BufferUtil.getFloatBuffer(TEXTURE_DATA);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(0.0f, 1.0f, 0.0f, 1.0f);
        mFilter.init();
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
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        mSurfaceWidth = width;
        mSurfaceHeight = height;
        adjustVideoSize();
        Log.e("@@@", "onSurfaceChanged width: " + width + "...height.." + height);
        glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);
        adjustVideoSize();
        synchronized (this) {
            if (mUpdateSurfaceTexture) {
                mSurfaceTexture.updateTexImage();
                mUpdateSurfaceTexture = false;
            }
        }
        runAll(mRunOnDraw);
        mFilter.onDraw(mTextureId, mVertextBuffer, mTextureBuffer);
    }

    public void setVideoSize(int width, int height) {
        if (mVideoWidth == width && mVideoHeight == height) {
            return;
        }
        // videoWidth 272
        // videoHeight 480
        mVideoWidth = width;
        mVideoHeight = height;
        mNeedUpdateSize = true;
    }

    private void adjustVideoSize() {
        if (mVideoWidth == 0 || mVideoHeight == 0 || mSurfaceHeight == 0 || mSurfaceWidth == 0) {
            return;
        }
        if (!mNeedUpdateSize) {
            return;
        }
        mNeedUpdateSize = false;
        float widthRation = (float) mSurfaceWidth / mVideoWidth;
        float heightRation = (float) mSurfaceHeight / mVideoHeight;
        float ration = Math.max(widthRation, heightRation);
        // 把视频宽高最小的一个扩大到Surface的大小
        int targetVideoWidth = Math.round(mVideoWidth * ration);
        int targetVideoHeight = Math.round(mVideoHeight * ration);
        // 扩大之后的宽高除以目前surface的宽高，来算错各自要xy的比例，这俩里面有一个肯定是1

        float rationX = (float) targetVideoWidth / mSurfaceWidth;
        float rationY = (float) targetVideoHeight / mSurfaceHeight;

        float[] targetPositionData = new float[]{
                POINT_DATA[0] / rationY, POINT_DATA[1] / rationX,
                POINT_DATA[2] / rationY, POINT_DATA[3] / rationX,
                POINT_DATA[4] / rationY, POINT_DATA[5] / rationX,
                POINT_DATA[6] / rationY, POINT_DATA[7] / rationX,

        };
        // 换算缩放后的顶点坐标。后面在onDraw()方法中会有这个值设置给顶点着色器
        mVertextBuffer.clear();
        mVertextBuffer.put(targetPositionData);
        mVertextBuffer.position(0);
    }

    public void setFilter(final BaseFilter filter) {
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                mFilter.destroy();

                mFilter = filter;
                mFilter.init();
                GLES30.glUseProgram(mFilter.getProgram());
            }
        });

    }

    //绘制线程集合
    private Queue<Runnable> mRunOnDraw = new LinkedList();
    /**
     * 添加到线程
     */
    private void runOnDraw(Runnable runnable) {
        synchronized(mRunOnDraw) {
            mRunOnDraw.add(runnable);
        }
    }

    /**
     * 运行所以线程
     */
    private void runAll(Queue<Runnable> queue) {
        synchronized(queue) {
            while (!queue.isEmpty()) {
                queue.poll().run();
            }
        }
    }

    private IVideoTextureRenderListener mTextureRenderListener;

    public void setIVideoTextureRenderListener(IVideoTextureRenderListener render) {
        mTextureRenderListener = render;
    }

    public interface IVideoTextureRenderListener {
        void onCreate(SurfaceTexture surfaceTexture);
    }
}
