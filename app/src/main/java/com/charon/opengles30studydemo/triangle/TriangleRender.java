package com.charon.opengles30studydemo.triangle;

import android.content.Context;
import android.content.res.Resources;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import com.charon.opengles30studydemo.MyApplication;
import com.charon.opengles30studydemo.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class TriangleRender implements GLSurfaceView.Renderer {
    //三个顶点
    private static final int POSITION_COMPONENT_COUNT = 3;
    //顶点位置缓存
    private final FloatBuffer vertexBuffer;
    //顶点颜色缓存
    private final FloatBuffer colorBuffer;
    //渲染程序
    private int mProgram;

    /*****************1.声明绘制图形的坐标和颜色数据 start**************/
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

    /*****************1.声明绘制图形的坐标和颜色数据 end**************/
    public TriangleRender() {
        /****************2.为顶点位置及颜色申请本地内存 start************/
        //将顶点数据拷贝映射到native内存中，以便OpenGL能够访问
        //分配本地内存空间,每个浮点型占4字节空间；将坐标数据转换为FloatBuffer，用以传入给OpenGL ES程序
        vertexBuffer = ByteBuffer.allocateDirect(triangleCoords.length * Float.BYTES) // 直接分配native内存
                .order(ByteOrder.nativeOrder()) // 和本地平台保持一致的字节序
                .asFloatBuffer(); // 将底层字节映射到FloatBuffer实例，方便使用
        vertexBuffer.put(triangleCoords); // 将顶点数据拷贝到native内存中
        // 将数组数据put进buffer之后，指针并不是在首位，所以一定要position到0，至关重要！否则会有很多奇妙的错误！将缓冲区的指针移动到头部，保证数据是从最开始处读取
        vertexBuffer.position(0);

        //顶点颜色相关
        colorBuffer = ByteBuffer.allocateDirect(color.length * Float.BYTES)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        colorBuffer.put(color);
        colorBuffer.position(0);
        /****************2.为顶点位置及颜色申请本地内存 end************/
    }


    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        //将背景设置为白色
        GLES30.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

        /******************3.加载编译顶点着色器和片段着色器 start**********/
        //编译顶点着色程序
        String vertexShaderStr = readResource(MyApplication.getInstance(), R.raw.triangle_vertex_shader);
        int vertexShaderId = compileVertexShader(vertexShaderStr);
        //编译片段着色程序
        String fragmentShaderStr = readResource(MyApplication.getInstance(), R.raw.triangle_fragment_shader);
        int fragmentShaderId = compileFragmentShader(fragmentShaderStr);
        /******************3.加载编译顶点着色器和片段着色器 end**********/
        /******************4.创建program，连接顶点和片段着色器并链接program start***********/
        //连接程序
        mProgram = linkProgram(vertexShaderId, fragmentShaderId);
        /******************4.创建program，连接顶点和片段着色器并链接program end***********/
        //在OpenGLES环境中使用程序
        GLES30.glUseProgram(mProgram);
        // 清除shader
        GLES30.glDeleteShader(vertexShaderId);
        GLES30.glDeleteShader(fragmentShaderId);
    }

    public void onSurfaceChanged(GL10 unused, int width, int height) {
        /*********5.设置绘制窗口********/
        GLES30.glViewport(0, 0, width, height);
    }

    public void onDrawFrame(GL10 unused) {
        /**********6.绘制************/
        //把颜色缓冲区设置为我们预设的颜色，绘图设计到多种缓冲区类型:颜色、深度和模板。这里只是向颜色缓冲区中绘制图形
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);
        // glVertexAttribPointer是把顶点位置属性赋值给着色器程序
        //0是上面着色器中写的vPosition的变量位置(location = 0)。意思就是绑定vertex坐标数据，然后将在vertextBuffer中的顶点数据传给vPosition变量。
        // 你肯定会想，如果我在着色器中不写呢？int vposition = glGetAttribLocation(program, "vPosition");就可以获得他的属性位置了
        // 第二个size是3，是因为上面我们triangleCoords声明的属性就是3位，xyz
        GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT, false, 3 * Float.BYTES, vertexBuffer);
        //启用顶点变量，这个0也是vPosition在着色器变量中的位置，和上面一样，在着色器文件中的location=0声明的
        GLES30.glEnableVertexAttribArray(0);

        //准备颜色数据
        /**
         * glVertexAttribPointer()方法的参数上面的也说过了，这里再按照这个场景说一下分别为:
         * index：顶点属性的索引.（这里我们的顶点位置和颜色向量在着色器中分别为0和1）layout (location = 0) in vec4 vPosition; layout (location = 1) in vec4 aColor;
         * size: 指定每个通用顶点属性的元素个数。必须是1、2、3、4。此外，glvertexattribpointer接受符号常量gl_bgra。初始值为4（也就是涉及颜色的时候必为4）。
         * type：属性的元素类型。（上面都是Float所以使用GLES30.GL_FLOAT）；
         * normalized：转换的时候是否要经过规范化，true：是；false：直接转化；
         * stride：跨距，默认是0。（由于我们将顶点位置和颜色数据分别存放没写在一个数组中，所以使用默认值0）
         * ptr： 本地数据缓存（这里我们的是顶点的位置和颜色数据）。
         */
        // 1是aColor在属性的位置，4是因为我们声明的颜色是4位，r、g、b、a。
        GLES30.glVertexAttribPointer(1, 4, GLES30.GL_FLOAT, false, 4 * Float.BYTES, colorBuffer);
        //启用顶点颜色句柄
        GLES30.glEnableVertexAttribArray(1);

        //绘制三个点
//        GLES30.glDrawArrays(GLES30.GL_POINTS, 0, POSITION_COMPONENT_COUNT);

        //绘制三条线
//        GLES30.glLineWidth(3);//设置线宽
//        GLES30.glDrawArrays(GLES30.GL_LINE_LOOP, 0, POSITION_COMPONENT_COUNT);

        //绘制三角形
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, POSITION_COMPONENT_COUNT);

        //禁止顶点数组的句柄
        GLES30.glDisableVertexAttribArray(0);
        GLES30.glDisableVertexAttribArray(1);
    }

    /**
     * 编译顶点着色器
     *
     * @param shaderCode
     */
    public static int compileVertexShader(String shaderCode) {
        return compileShader(GLES30.GL_VERTEX_SHADER, shaderCode);
    }

    /**
     * 编译片段着色器
     *
     * @param shaderCode
     */
    public static int compileFragmentShader(String shaderCode) {
        return compileShader(GLES30.GL_FRAGMENT_SHADER, shaderCode);
    }

    /**
     * 加载并编译着色器代码
     *
     * @param type       顶点着色器:GLES30.GL_VERTEX_SHADER
     *                   片段着色器:GLES30.GL_FRAGMENT_SHADER
     * @param shaderCode
     */
    private static int compileShader(int type, String shaderCode) {
        //传入渲染器类型参数的type，创建一个对应的着色器对象
        final int shaderId = GLES30.glCreateShader(type);
        if (shaderId != 0) {
            // 传入着色器对象和字符串shaderCode定义的源代码，将二者关联起来
            GLES30.glShaderSource(shaderId, shaderCode);
            // 传入着色器对象，并对其进行编译
            GLES30.glCompileShader(shaderId);
            //检测状态
            final int[] compileStatus = new int[1];
            GLES30.glGetShaderiv(shaderId, GLES30.GL_COMPILE_STATUS, compileStatus, 0);
            if (compileStatus[0] == 0) {
                String logInfo = GLES30.glGetShaderInfoLog(shaderId);
                System.err.println(logInfo);
                //创建失败
                GLES30.glDeleteShader(shaderId);
                return 0;
            }
            return shaderId;
        } else {
            //创建失败
            return 0;
        }
    }

    /**
     * 链接小程序
     *
     * @param vertexShaderId   顶点着色器
     * @param fragmentShaderId 片段着色器
     */
    public static int linkProgram(int vertexShaderId, int fragmentShaderId) {
        //创建一个空的OpenGLES程序
        final int programId = GLES30.glCreateProgram();
        if (programId != 0) {
            //将顶点着色器加入到程序
            GLES30.glAttachShader(programId, vertexShaderId);
            //将片元着色器加入到程序中
            GLES30.glAttachShader(programId, fragmentShaderId);
            //链接着色器程序
            GLES30.glLinkProgram(programId);
            GLES30.glDeleteShader(vertexShaderId);
            GLES30.glDeleteShader(fragmentShaderId);
            final int[] linkStatus = new int[1];
            GLES30.glGetProgramiv(programId, GLES30.GL_LINK_STATUS, linkStatus, 0);
            if (linkStatus[0] == 0) {
                String logInfo = GLES30.glGetProgramInfoLog(programId);
                System.err.println(logInfo);
                GLES30.glDeleteProgram(programId);
                return 0;
            }
            return programId;
        } else {
            //创建失败
            return 0;
        }
    }

    /**
     * 验证程序片段是否有效
     *
     * @param programObjectId
     */
    public static boolean validProgram(int programObjectId) {
        GLES30.glValidateProgram(programObjectId);
        final int[] programStatus = new int[1];
        GLES30.glGetProgramiv(programObjectId, GLES30.GL_VALIDATE_STATUS, programStatus, 0);
        return programStatus[0] != 0;
    }

    /**
     * 读取资源
     *
     * @param resourceId
     */
    public static String readResource(Context context, int resourceId) {
        StringBuilder builder = new StringBuilder();
        try {
            InputStream inputStream = context.getApplicationContext().getResources().openRawResource(resourceId);
            InputStreamReader streamReader = new InputStreamReader(inputStream);

            BufferedReader bufferedReader = new BufferedReader(streamReader);
            String textLine;
            while ((textLine = bufferedReader.readLine()) != null) {
                builder.append(textLine);
                builder.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }
}
