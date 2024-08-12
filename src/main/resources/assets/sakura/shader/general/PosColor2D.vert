#version 330 core

uniform mat4 matrix;

layout (location = 0) in vec2 position;
layout (location = 1) in vec4 vertColor;

out vec4 v_Color;

void main() {
    gl_Position = matrix * vec4(position, 0.0, 1.0);
    v_Color = vertColor.abgr;
}