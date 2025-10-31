package team.exception.sakura.graphics.gl

import org.lwjgl.opengl.GL41.*
import team.exception.sakura.graphics.buffer.G2Buffer
import java.nio.ByteBuffer

class GlBuffer(
    device: GlDevice,
    size: Long,
    access: Access,
): G2Buffer(size, access) {

    private val id: Int = glGenBuffers()
    private var mappedBuf: ByteBuffer? = null

    init {

        val cmdList = device.getTempCommandList()

        cmdList.add {
            glBindBuffer(GL_ARRAY_BUFFER, id)
            glBufferData(GL_ARRAY_BUFFER, size, GL_STATIC_DRAW)

            mappedBuf = glMapBuffer(GL_ARRAY_BUFFER, getGlByG2Access(access))
            glBindBuffer(GL_ARRAY_BUFFER, 0)
        }
        cmdList.summitAndClear()

    }

    override fun getMappedBuffer(): ByteBuffer {
        if (mappedBuf == null) {
            throw IllegalStateException("Buffer hasn't been mapped")
        }
        return mappedBuf!!
    }

    companion object {
        fun getGlByG2Access(access: Access): Int = when (access) {
            Access.READ -> GL_READ_ONLY
            Access.WRITE -> GL_WRITE_ONLY
            Access.READ_WRITE -> GL_READ_WRITE
        }
    }

}