package com.charon.opengles30studydemo.util;

import android.opengl.GLES30;
import android.opengl.Matrix;

public class ProjectionMatrixUtil {
    // 矩阵数组
    private static final float[] mProjectionMatrix = new float[]{
            1, 0, 0, 0,
            0, 1, 0, 0,
            0, 0, 1, 0,
            0, 0, 0, 1,
    };

    public static void orthoM(int program, int width, int height, String name) {
        int uMatrixLocation = GLES30.glGetUniformLocation(program, name);
        //计算宽高比 边长比(>=1)，非宽高比
        float aspectRatio = width > height ?
                (float) width / (float) height :
                (float) height / (float) width;
        if (width > height) {
            // 横屏
            Matrix.orthoM(mProjectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f);
        } else {
            // 竖屏or正方形
            Matrix.orthoM(mProjectionMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f);
        }
        GLES30.glUniformMatrix4fv(uMatrixLocation, 1, false, mProjectionMatrix, 0);
    }
}
