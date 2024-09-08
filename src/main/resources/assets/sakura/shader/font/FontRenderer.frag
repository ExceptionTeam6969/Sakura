#version 450 core

layout(binding = 0) uniform sampler2D FontTexture;

in vec2 TexCoord;
in vec4 Color;

out vec4 FragColor;

void main() {
    vec4 texColor = texture2D(FontTexture, TexCoord).rrrr;
    if (texColor.a < 0.01f) discard;
    FragColor = Color * texColor;
}