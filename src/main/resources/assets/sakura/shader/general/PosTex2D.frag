#version 450 core

uniform sampler2D u_Texture;

in vec2 v_TexCoord;
in vec4 v_Color;

out vec4 FragColor;

void main() {
    vec4 texColor = texture(u_Texture, v_TexCoord);
    if (texColor.a < 0.01f) discard;
    FragColor = v_Color * texColor;
}