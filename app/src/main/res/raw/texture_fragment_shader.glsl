#version 300 es
precision mediump float;
// 采样器(sampler)是用于从纹理贴图读取的特殊统一变量。采样器统一变量将加载一个指定纹理绑定的纹理单元额数据，java代码里面需要把它设置为0
uniform sampler2D uTextureUnit;
// 接受刚才顶点着色器传入的纹理坐标(s, t)
in vec2 vTexCoord;
out vec4 vFragColor;

void main() {
    // 100 es版本中是texture2D，texture函数会将传进来的纹理和坐标进行差值采样，输出到颜色缓冲区。
    vFragColor = texture(uTextureUnit, vTexCoord);
}
