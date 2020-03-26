#version 300 es
#extension GL_OES_EGL_image_external_essl3 : require
precision mediump float;
in vec2 aCoordPosition;
uniform samplerExternalOES uSamplerTexture;
out vec4 vFragColor;
void main() {
    vFragColor = texture(uSamplerTexture, aCoordPosition);
}
