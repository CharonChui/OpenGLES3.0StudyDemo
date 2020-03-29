package com.charon.opengles30studydemo.videofilter.filter;

import com.charon.opengles30studydemo.R;
import com.charon.opengles30studydemo.videofilter.base.BaseFilter;

public class BlackWhiteFilter extends BaseFilter {

    public BlackWhiteFilter() {
        super(R.raw.video_no_filter_vertex_shader, R.raw.video_blackwhite_filter_fragment_shader);
    }
}
