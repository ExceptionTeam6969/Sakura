#version 450 core

uniform mat4 u_MVPMatrix;

layout (location = 0) in vec2 aPos;
layout (location = 1) in vec3 aTexCoord;
layout (location = 2) in vec4 aColor;

out vec3 v_TexCoord;
out vec4 v_Color;

void main() {
    gl_Position = u_MVPMatrix * vec4(aPos, 0.0, 1.0);
    v_TexCoord = aTexCoord;
    v_Color = aColor.abgr;
}
