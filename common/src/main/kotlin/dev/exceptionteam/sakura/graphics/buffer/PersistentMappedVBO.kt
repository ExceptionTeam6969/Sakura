package dev.exceptionteam.sakura.graphics.buffer

import dev.exceptionteam.sakura.graphics.GlObject
import dev.exceptionteam.sakura.graphics.glNamedBufferStorage
import dev.luna5ama.kmogus.Arr
import dev.luna5ama.kmogus.asMutable
import org.lwjgl.opengl.GL45
import java.nio.ByteBuffer
import kotlin.math.abs

// Persistent map buffer
class PersistentMappedVBO(
    private val stride: Int,
    private val sizeFactor: Long = 4L,
): GlObject {

    companion object {
        private const val BUFFER_SIZE: Long = 256 * 1024L    // 1MB
    }

    override var id: Int = GL45.glCreateBuffers().apply {
        glNamedBufferStorage(this, abs(sizeFactor * BUFFER_SIZE), 0L,
            GL45.GL_MAP_PERSISTENT_BIT or GL45.GL_MAP_COHERENT_BIT or GL45.GL_MAP_WRITE_BIT)
    }

    var offset = 0L

    val arr = Arr.wrap(
        GL45.glMapNamedBufferRange(
            id,
            0,
            BUFFER_SIZE,
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

    fun end() {
        offset = (arr.pos / stride)
    }

    private var sync = 0L

    fun onSync() {
        // Check if the draw call is complete and reset the Buffer
        if (sync == 0L) {
            if (arr.pos >= arr.len / 4 * 3) {   // 3/4 of the buffer is full
                sync = GL45.glFenceSync(GL45.GL_SYNC_GPU_COMMANDS_COMPLETE, 0)
            }
        } else if (
            IntArray(1).apply {
                GL45.glGetSynciv(sync, GL45.GL_SYNC_STATUS, IntArray(1), this)
            }[0] == GL45.GL_SIGNALED
        ) {
            GL45.glDeleteSync(sync)
            sync = 0L
            arr.pos = 0L
            offset = 0
        }
    }

}