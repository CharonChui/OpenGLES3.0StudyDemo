#version 300 es
// 声明着色器的版本
// 声明着色器中浮点变量的默认精度
precision mediump float;
// 声明一个输入名为vColor的4分向量，来自上面的顶点着色器
in vec4 vColor;
// 声明一个4分向量的输出变量fragColor
out vec4 fragColor;
void main() {
    // 将输入的颜色值数据拷贝到fragColor变量中，输出到颜色缓冲区
    fragColor = vColor;
}