#version 450 core

uniform mat4 MVPMatrix;

layout (location = 0) in vec3 position;
layout (location = 1) in vec4 vertColor;

out vec4 Color;

void main() {
    gl_Position = MVPMatrix * vec4(position, 1.0);
    Color = vertColor.abgr;
}