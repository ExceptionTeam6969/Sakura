package dev.exceptionteam.sakura.graphics.buffer

import org.lwjgl.opengl.GL45.glCreateBuffers

data class ElementBufferObject(
    var eboId: Int = glCreateBuffers(),
    var indicesCount: Int = 0,
    var vertexCount: Int = 0,
)
