#version 450 core

uniform sampler2D Texture;

in vec2 TexCoord;
in vec4 Color;

out vec4 FragColor;

void main() {
//    vec4 texColor = vec4(texture(Texture, TexCoord).rgb, 1.0f);
    vec4 texColor = texture(Texture, TexCoord);
    if (texColor.a < 0.01f) discard;
    FragColor = Color * texColor;
}