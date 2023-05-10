package com.charon.opengles30studydemo.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public class BufferUtil {
    private static final int BYTES_PER_FLOAT = 4;
    private static final int BYTES_PER_SHORT = 2;

    public static IntBuffer getIntBuffer(int[] array) {
        IntBuffer intBuffer = IntBuffer.allocate(array.length * Integer.BYTES);
        intBuffer.put(array);
        intBuffer.position(0);
        return intBuffer;
    }

    public static FloatBuffer getFloatBuffer(float[] array) {
        FloatBuffer floatBuffer = ByteBuffer.allocateDirect(array.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        floatBuffer.put(array);
        floatBuffer.position(0);
        return floatBuffer;
    }

    public static ShortBuffer getShortBuffer(short[] array) {
        ShortBuffer shortBuffer = ByteBuffer.allocateDirect(array.length * BYTES_PER_SHORT)
                .order(ByteOrder.nativeOrder())
                .asShortBuffer();
        shortBuffer.put(array);
        shortBuffer.position(0);
        return shortBuffer;
    }
}
