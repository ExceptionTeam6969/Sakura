package team.exception.sakura.graphics.gl

import com.mojang.blaze3d.opengl.GlStateManager
import org.lwjgl.opengl.GL41.*
import team.exception.sakura.graphics.geek2.G2Buffer
import java.nio.ByteBuffer

class GlBuffer(
    override val device: GlDevice,
    size: Long,
    val access: Access,
): G2Buffer(device, size) {

    private val id: Int = glGenBuffers()
    private var mappedBuf: ByteBuffer? = null

    init {

        val cmdList = device.getTempCommandList()

        cmdList.add {
            GlStateManager._glBindBuffer(GL_ARRAY_BUFFER, id)
            glBufferData(GL_ARRAY_BUFFER, size, GL_STATIC_DRAW)

            mappedBuf = glMapBufferRange(GL_ARRAY_BUFFER,
                0, size, getGlByG2Access(access))
        }
        cmdList.summitAndClear()

    }

    override fun getMappedBuffer(): ByteBuffer {
        if (mappedBuf == null) {
            throw IllegalStateException("Buffer hasn't been mapped")
        }
        return mappedBuf!!
    }

    override fun refresh() {
        // TODO: Refresh modified data
    }

    override fun remap() {
        val cmdList = device.getTempCommandList()

        cmdList.add {
            GlStateManager._glBindBuffer(GL_ARRAY_BUFFER, id)
            if (mappedBuf == null) {
                throw IllegalStateException("Buffer hasn't been mapped")
            }
            glUnmapBuffer(GL_ARRAY_BUFFER)
            mappedBuf = glMapBufferRange(GL_ARRAY_BUFFER, 0,
                size, getGlByG2Access(access))
        }
        cmdList.summitAndClear()
    }

    override fun destroy() {
        val cmdList = device.getTempCommandList()

        cmdList.add {
            GlStateManager._glBindBuffer(GL_ARRAY_BUFFER, id)
            glUnmapBuffer(GL_ARRAY_BUFFER)
            mappedBuf = null
            glDeleteBuffers(id)
        }
        cmdList.summitAndClear()
    }

    companion object {
        const val DEFAULT_ACCESS_BITS = GL_MAP_FLUSH_EXPLICIT_BIT

        fun getGlByG2Access(access: Access): Int = when (access) {
            Access.READ -> GL_MAP_READ_BIT and DEFAULT_ACCESS_BITS
            Access.WRITE -> GL_MAP_WRITE_BIT and DEFAULT_ACCESS_BITS
            Access.READ_WRITE -> GL_MAP_READ_BIT and GL_MAP_WRITE_BIT and DEFAULT_ACCESS_BITS
        }
    }

}