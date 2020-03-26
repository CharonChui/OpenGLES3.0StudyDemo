package com.charon.opengles30studydemo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.charon.opengles30studydemo.circle.CircleActivity;
import com.charon.opengles30studydemo.hexagon.HexagonActivity;
import com.charon.opengles30studydemo.isosceletriangle.IsoTriangleActivity;
import com.charon.opengles30studydemo.multitexture.MultiTextureActivity;
import com.charon.opengles30studydemo.square.SquareActivity;
import com.charon.opengles30studydemo.texture.TextureActivity;
import com.charon.opengles30studydemo.triangle.TriangleActivity;
import com.charon.opengles30studydemo.video.VideoPlayerActivity;
import com.charon.opengles30studydemo.videofilter.VideoFilterActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private MainAdapter mMainAdapter;
    private List<MainBean> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        initView();
    }

    private void findView() {
        mRecyclerView = findViewById(R.id.mRecyclerView);

    }

    private void initView() {
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mList = new ArrayList<>();
        mList.add(new MainBean("三角形", TriangleActivity.class));
        mList.add(new MainBean("等腰三角形", IsoTriangleActivity.class));
        mList.add(new MainBean("正方形", SquareActivity.class));
        mList.add(new MainBean("六方形", HexagonActivity.class));
        mList.add(new MainBean("圆形", CircleActivity.class));
        mList.add(new MainBean("纹理", TextureActivity.class));
        mList.add(new MainBean("多重纹理", MultiTextureActivity.class));
        mList.add(new MainBean("视频播放", VideoPlayerActivity.class));
        mList.add(new MainBean("视频播放滤镜", VideoFilterActivity.class));
        mMainAdapter = new MainAdapter(this, mList);
        mRecyclerView.setAdapter(mMainAdapter);
    }


}
