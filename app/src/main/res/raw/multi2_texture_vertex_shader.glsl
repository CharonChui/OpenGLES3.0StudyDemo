#version 300 es
in vec4 vPosition;
in vec2 aTextureCoord;
uniform mat4 u_Matrix;
out vec2 vTexCoord;
void main() {
    gl_Position = u_Matrix * vPosition;
    gl_PointSize = 10.0;
    vTexCoord = aTextureCoord;
}

