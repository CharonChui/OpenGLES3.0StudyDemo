#version 300 es
in vec4 vPosition;
in vec2 vCoordPosition;
out vec2 aCoordPosition;

void main() {
    gl_Position = vPosition;
    aCoordPosition = vCoordPosition;
}