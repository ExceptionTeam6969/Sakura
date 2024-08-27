#version 450 core

layout(binding = 0) uniform sampler2D FontTexture;

in vec2 TexCoord;
in vec4 Color;

out vec4 FragColor;

void main() {
    vec4 texColor = texture2D(FontTexture, TexCoord);
//    float alpha = texColor.a;
//    if (alpha < 0.01) discard;
//    FragColor = Color * texColor;
    FragColor = texColor;
//    FragColor = vec4(0, 0, 0, 1);
}