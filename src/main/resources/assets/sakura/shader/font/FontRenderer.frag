#version 450 core

uniform sampler2D FontTexture;

in vec2 TexCoord;
in vec4 Color;

out vec4 FragColor;

void main() {
    vec4 texColor = texture2D(FontTexture, TexCoord);
//    float alpha = texColor.a;
//    if (alpha < 0.01) discard;
//    FragColor = Color * texColor;
    FragColor = texColor;
}