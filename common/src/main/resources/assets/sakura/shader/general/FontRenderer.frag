#version 450 core

#extension GL_EXT_texture_array : enable
#extension GL_ARB_bindless_texture : require
#extension GL_ARB_sparse_texture : enable

uniform sampler2DArray u_Texture;

in vec3 v_TexCoord;
in vec4 v_Color;

out vec4 FragColor;

void main() {
    vec4 texColor = texture(u_Texture, vec3(v_TexCoord.xy, floor(v_TexCoord.z)));
    FragColor = v_Color * texColor;
}