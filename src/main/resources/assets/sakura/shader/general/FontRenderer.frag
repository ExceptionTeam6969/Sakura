#version 450 core

#extension GL_ARB_texture_array : enable
#extension GL_ARB_bindless_texture : require

uniform sampler2DArray u_Texture;
uniform int u_Depth;

in vec2 v_TexCoord;
in vec4 v_Color;

out vec4 FragColor;

void main() {
    vec4 texColor = texture(u_Texture, vec3(v_TexCoord, float(u_Depth)));
    if (texColor.a < 0.01f) discard;
    FragColor = v_Color * texColor;
}