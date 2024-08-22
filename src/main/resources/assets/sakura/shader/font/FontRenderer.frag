#version 330 core

out vec4 FragColor;

in vec2 TexCoord;
in vec4 Color;

uniform sampler2D FontTexture;

void main() {
    FragColor = texture2D(FontTexture, TexCoord) * Color;
}