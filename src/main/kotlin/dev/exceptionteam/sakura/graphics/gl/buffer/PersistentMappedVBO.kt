package dev.exceptionteam.sakura.graphics.gl.buffer

import dev.exceptionteam.sakura.events.impl.GameLoopEvent
import dev.exceptionteam.sakura.events.listener
import dev.exceptionteam.sakura.graphics.gl.*
import dev.luna5ama.kmogus.asMutable
import org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER
import org.lwjgl.opengl.GL15.glBindBuffer
import org.lwjgl.opengl.GL45

object PersistentMappedVBO {

    private const val VBO_SIZE = 64L * 1024L * 1024L

    private val vbo = glCreateBuffers().apply {
        glNamedBufferStorage(this, VBO_SIZE, 0,
            GL45.GL_MAP_WRITE_BIT or GL45.GL_MAP_COHERENT_BIT or GL45.GL_MAP_PERSISTENT_BIT)
    }

    val arr = glMapNamedBufferRange(
        vbo, 0, VBO_SIZE,
        GL45.GL_MAP_WRITE_BIT or GL45.GL_MAP_PERSISTENT_BIT or
                GL45.GL_MAP_COHERENT_BIT or GL45.GL_MAP_UNSYNCHRONIZED_BIT
    ).asMutable()

    var offset = 0L

    fun end() {
        offset = arr.pos / 16
    }

    init {
        listener<GameLoopEvent.AfterRender> {
            if (arr.pos > arr.len / 2) {
                // 直接覆盖 无需clear 节约性能
                offset = 0L
                arr.pos = 0
            }
        }
    }

    val VAO_2D = createVao(buildAttribute(16) {
        float(0, 2, GlDataType.GL_FLOAT, false)
        float(1, 4, GlDataType.GL_UNSIGNED_BYTE, true)
    })

    val VAO_3D = createVao(buildAttribute(16) {
        float(0, 3, GlDataType.GL_FLOAT, false)
        float(1, 4, GlDataType.GL_UNSIGNED_BYTE, true)
    })

    private fun createVao(vertexAttribute: VertexAttribute): Int {
        val vaoID = glCreateVertexArrays()
        GL45.glBindVertexArray(vaoID)
        glBindBuffer(GL_ARRAY_BUFFER, vbo)
        vertexAttribute.apply()
        GL45.glBindVertexArray(0)
        glBindBuffer(GL_ARRAY_BUFFER, 0)
        return vaoID
    }

}