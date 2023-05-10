#version 300 es
// 声明着色器的版本，OpenGL ES 3.0版本对应的着色器语言版本是 GLSL 300 ES
// 顶点着色器的顶点位置，输入一个名为vPosition的3分量向量，layout (location = 0)表示这个变量的位置是顶点属性中的第0个属性。
layout (location = 0) in vec3 vPosition;
// 顶点着色器的顶点颜色数据，输入一个名为aColor的4分量向量，layout (location = 1)表示这个变量的位置是顶点属性中的第1个属性。
layout (location = 1) in vec4 aColor;
// 输出一个名为vColor的4分量向量，后面输入到片段着色器中。
out vec4 vColor;
// 变换矩阵4*4
uniform mat4 u_Matrix;
void main() {
    // 先把vec3转成vec4
    vec4 pos = vec4(vPosition.x, vPosition.y, vPosition.z, 1.0);
    // gl_Position为Shader内置变量，为顶点位置，将其赋值为vPosition
    gl_Position  = u_Matrix * pos;
    // gl_PointSize为Shader内置变量，为点的直径
    gl_PointSize = 10.0;
    // 将输入数据aColor拷贝到vColor的变量中。
    vColor = aColor;
}