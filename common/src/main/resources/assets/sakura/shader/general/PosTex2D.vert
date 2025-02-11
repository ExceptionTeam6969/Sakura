#version 450 core

uniform mat4 MVPMatrix;

layout (location = 0) in vec2 aPos;
layout (location = 1) in vec2 aTexCoord;
layout (location = 2) in vec4 aColor;

out vec2 v_TexCoord;
out vec4 v_Color;

void main() {
    gl_Position = MVPMatrix * vec4(aPos, 0.0, 1.0);
    v_TexCoord = aTexCoord;
    v_Color = aColor.abgr;
}
