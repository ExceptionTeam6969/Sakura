#version 450 core
out vec4 FragColor;

in vec2 TexCoord;
in vec4 Color;

uniform sampler2D FontTexture;

void main() {
    float alpha = texture(FontTexture, TexCoord).a;
    if (alpha < 0.05) discard;
    FragColor = Color * texture(FontTexture, TexCoord);
}