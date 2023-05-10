package com.charon.opengles30studydemo.isosceletriangle;

import android.opengl.GLES30;
import android.opengl.Matrix;

import com.charon.opengles30studydemo.MyApplication;
import com.charon.opengles30studydemo.R;
import com.charon.opengles30studydemo.util.ResReadUtils;
import com.charon.opengles30studydemo.util.ShaderUtils;
import com.charon.opengles30studydemo.view.GLTextureView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class IsoTriangleRender implements GLTextureView.Renderer {
    //一个Float占用4Byte
    private static final int BYTES_PER_FLOAT = 4;
    //三个顶点
    private static final int POSITION_COMPONENT_COUNT = 3;
    //顶点位置缓存
    private final FloatBuffer vertexBuffer;
    //顶点颜色缓存
    private final FloatBuffer colorBuffer;
    //渲染程序
    private int mProgram;
    private int uMatrixLocation;
    // 矩阵数组
    private final float[] mProjectionMatrix = new float[]{
            1, 0, 0, 0,
            0, 1, 0, 0,
            0, 0, 1, 0,
            0, 0, 0, 1,
    };
    //三个顶点的位置参数
    private float triangleCoords[] = {
            0.5f, 0.5f, 0.0f, // top
            -0.5f, -0.5f, 0.0f, // bottom left
            0.5f, -0.5f, 0.0f // bottom right
    };

    //三个顶点的颜色参数
    private float color[] = {
            1.0f, 0.0f, 0.0f, 1.0f,// top
            0.0f, 1.0f, 0.0f, 1.0f,// bottom left
            0.0f, 0.0f, 1.0f, 1.0f// bottom right
    };

    public IsoTriangleRender() {
        //顶点位置相关
        //分配本地内存空间,每个浮点型占4字节空间；将坐标数据转换为FloatBuffer，用以传入给OpenGL ES程序
        vertexBuffer = ByteBuffer.allocateDirect(triangleCoords.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexBuffer.put(triangleCoords);
        vertexBuffer.position(0);

        //顶点颜色相关
        colorBuffer = ByteBuffer.allocateDirect(color.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        colorBuffer.put(color);
        colorBuffer.position(0);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //将背景设置为白色
        GLES30.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        //编译顶点着色程序
        String vertexShaderStr = ResReadUtils.readResource(MyApplication.getInstance(), R.raw.iso_triangle_vertex_shader);
        int vertexShaderId = ShaderUtils.compileVertexShader(vertexShaderStr);
        //编译片段着色程序
        String fragmentShaderStr = ResReadUtils.readResource(MyApplication.getInstance(), R.raw.iso_triangle_fragment_shader);
        int fragmentShaderId = ShaderUtils.compileFragmentShader(fragmentShaderStr);
        //连接程序
        mProgram = ShaderUtils.linkProgram(vertexShaderId, fragmentShaderId);
        //在OpenGLES环境中使用程序
        GLES30.glUseProgram(mProgram);

        /**********新加的部分，获取变换矩阵以及其的位置、颜色等************/
        uMatrixLocation = GLES30.glGetUniformLocation(mProgram, "u_Matrix");
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES30.glViewport(0, 0, width, height);
        /**********新加的部分，将变换矩阵传入顶点渲染器************/
        //计算宽高比
        // 边长比(>=1)，非宽高比
        float aspectRatio = width > height ?
                (float) width / (float) height :
                (float) height / (float) width;

        // 1. 矩阵数组
        // 2. 结果矩阵起始的偏移量
        // 3. left：x的最小值
        // 4. right：x的最大值
        // 5. bottom：y的最小值
        // 6. top：y的最大值
        // 7. near：z的最小值
        // 8. far：z的最大值
        if (width > height) {
            // 横屏
            Matrix.orthoM(mProjectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f);
        } else {
            // 竖屏or正方形
            Matrix.orthoM(mProjectionMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f);
        }
        // 更新u_Matrix的值，即更新矩阵数组
        GLES30.glUniformMatrix4fv(uMatrixLocation, 1, false, mProjectionMatrix, 0);
    }

    @Override
    public boolean onDrawFrame(GL10 gl) {
        //把颜色缓冲区设置为我们预设的颜色
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);
        //绑定vertex坐标数据，告诉OpenGL可以在缓冲区vertexBuffer中获取vPosition的护具
        GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT, false, 3 * Float.BYTES, vertexBuffer);
        //启用顶点位置句柄
        GLES30.glEnableVertexAttribArray(0);
        //准备颜色数据
        GLES30.glVertexAttribPointer(1, 4, GLES30.GL_FLOAT, false, 4 * Float.BYTES, colorBuffer);
        //启用顶点颜色句柄
        GLES30.glEnableVertexAttribArray(1);
        //绘制三角形
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, POSITION_COMPONENT_COUNT);
        //禁止顶点数组的句柄
        GLES30.glDisableVertexAttribArray(0);
        GLES30.glDisableVertexAttribArray(1);
        return true;
    }

    @Override
    public void onSurfaceDestroyed() {

    }
}
