package dev.exceptionteam.sakura.graphics.gl.buffer

import dev.exceptionteam.sakura.graphics.gl.GlDataType
import dev.exceptionteam.sakura.graphics.gl.GlObject
import dev.exceptionteam.sakura.graphics.gl.glNamedBufferStorage
import dev.luna5ama.kmogus.Arr
import dev.luna5ama.kmogus.asMutable
import org.lwjgl.opengl.GL45
import java.nio.ByteBuffer

// Persistent map buffer
object PMBuffer: GlObject {

    private const val BUFFER_SIZE = 64 * 1024 * 1024L

    override var id: Int = GL45.glCreateBuffers().apply {
        glNamedBufferStorage(this, BUFFER_SIZE, 0L,
            GL45.GL_MAP_PERSISTENT_BIT or GL45.GL_MAP_COHERENT_BIT or GL45.GL_MAP_WRITE_BIT)
    }

    var offset = 0L

    val arr = Arr.wrap(
        GL45.glMapNamedBufferRange(
            id,
            0,
            64L * 1024L * 1024L,
            GL45.GL_MAP_WRITE_BIT or GL45.GL_MAP_PERSISTENT_BIT or
                    GL45.GL_MAP_COHERENT_BIT or GL45.GL_MAP_UNSYNCHRONIZED_BIT
        ) as ByteBuffer
    ).asMutable()

    override fun bind() {
        GL45.glBindBuffer(GL45.GL_ARRAY_BUFFER, id)
    }

    override fun unbind() {
        GL45.glBindBuffer(GL45.GL_ARRAY_BUFFER, 0)
    }

    override fun delete() {
        GL45.glDeleteBuffers(id)
    }

    fun end(stride: Int) {
        offset = (arr.pos / stride)
    }

    private var sync = 0L

    fun onSync() {
        if (sync == 0L) {
            if (arr.pos >= arr.len / 2) {
                sync = GL45.glFenceSync(GL45.GL_SYNC_GPU_COMMANDS_COMPLETE, 0)
            }
        } else if (IntArray(1).apply {
                GL45.glGetSynciv(
                    sync,
                    GL45.GL_SYNC_STATUS,
                    IntArray(1),
                    this
                )
            }[0] == GL45.GL_SIGNALED) {
            GL45.glDeleteSync(sync)
            sync = 0L
            arr.pos = 0L
            offset = 0
        }
    }

    val VAO_2D = createVao(buildAttribute(12) {
        float(0, 2, GlDataType.GL_FLOAT, false)
        float(1, 4, GlDataType.GL_UNSIGNED_BYTE, true)
    })

    val VAO_3D = createVao(buildAttribute(16) {
        float(0, 3, GlDataType.GL_FLOAT, false)
        float(1, 4, GlDataType.GL_UNSIGNED_BYTE, true)
    })

    private fun createVao(vertexAttribute: VertexAttribute): Int {
        val vaoID = GL45.glCreateVertexArrays()
        GL45.glBindVertexArray(vaoID)
        GL45.glBindBuffer(GL45.GL_ARRAY_BUFFER, id)
        vertexAttribute.apply()
        GL45.glBindVertexArray(0)
        GL45.glBindBuffer(GL45.GL_ARRAY_BUFFER, 0)
        return vaoID
    }

}