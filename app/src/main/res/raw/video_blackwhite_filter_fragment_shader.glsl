#version 300 es
#extension GL_OES_EGL_image_external_essl3 : require
precision mediump float;
in vec2 aCoordPosition;
uniform samplerExternalOES uSamplerTexture;
out vec4 vFragColor;
void main() {
    vec4 color = texture(uSamplerTexture, aCoordPosition);
    float colorR = (color.r + color.g + color.b) / 3.0;
    float colorG = (color.r + color.g + color.b) / 3.0;
    float colorB = (color.r + color.g + color.b) / 3.0;
    vFragColor = vec4(colorR, colorG, colorB, color.a);
}