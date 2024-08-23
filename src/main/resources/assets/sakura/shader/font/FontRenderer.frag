#version 330 core
precision mediump float;

out vec4 FragColor;

in vec2 TexCoord;
in vec4 Color;

uniform sampler2D FontTexture;

void main() {
    float alpha = texture(FontTexture, TexCoord).a;
    if (alpha == 0.0) discard;
    FragColor = texture(FontTexture, TexCoord) * Color;
}