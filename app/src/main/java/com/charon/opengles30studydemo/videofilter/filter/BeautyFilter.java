package com.charon.opengles30studydemo.videofilter.filter;

import android.opengl.GLES30;

import androidx.annotation.RawRes;

import com.charon.opengles30studydemo.MainBean;
import com.charon.opengles30studydemo.R;
import com.charon.opengles30studydemo.videofilter.base.BaseFilter;

public class BeautyFilter extends BaseFilter {
    private float toneLevel;
    private float beautyLevel;
    private float brightLevel;
    private int paramsLocation;
    private int brightnessLocation;
    private int singleStepOffsetLocation;
    //透明度
    int alphaValue = 0;

    public BeautyFilter() {
        super(R.raw.video_no_filter_vertex_shader, R.raw.video_beauty_filter_fragment_shader);
    }

    @Override
    public void onInit() {
        super.onInit();
        paramsLocation = glGetUniformLocation("params");
        brightnessLocation = glGetUniformLocation("brightness");
        singleStepOffsetLocation = glGetUniformLocation("singleStepOffset");
        alphaValue = glGetUniformLocation("alphaValue");
        toneLevel = 0.47f;
        beautyLevel = 0.42f;
        brightLevel = 0.34f;
        setParams(beautyLevel, toneLevel);
        setBrightLevel(brightLevel);
        setFilterAlpha(1.0F);
    }

    private void setBeautyLevel(float beautyLevel) {
        this.beautyLevel = beautyLevel;
        setParams(beautyLevel, toneLevel);
    }

    private void setBrightLevel(float brightLevel) {
        this.brightLevel = brightLevel;
        setFloat(brightnessLocation, 0.6f * (-0.5f + brightLevel));
    }

    /**
     * 设置透明度0-1
     */
    void setFilterAlpha(float alpha) {
        setFloat(alphaValue, alpha);
    }

    private void setParams(float beauty, float tone) {
        float[] vector = new float[4];
        vector[0] = 1.0f - 0.6f * beauty;
        vector[1] = 1.0f - 0.3f * beauty;
        vector[2] = 0.1f + 0.3f * tone;
        vector[3] = 0.1f + 0.3f * tone;
        setFloatVec4(paramsLocation, vector);
    }

    private void setTexelSize(float w, float h) {
        setFloatVec2(singleStepOffsetLocation, new float[]{
                2.0f / w
                , 2.0f / h});
    }

//    void setRenderSize(float width, float height) {
//        super.setRenderSize(width, height);
//        setTexelSize(width., height);
//    }
}
