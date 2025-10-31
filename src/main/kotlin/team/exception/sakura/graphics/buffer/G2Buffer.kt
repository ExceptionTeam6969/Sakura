package team.exception.sakura.graphics.buffer

import java.nio.ByteBuffer

abstract class G2Buffer(
    val size: Long,
    val access: Access,
) {

    /**
     * Get the mapped buffer.
     * @return the mapped buffer
     * @throws IllegalStateException if the buffer hasn't been mapped.
     */
    abstract fun getMappedBuffer(): ByteBuffer

    enum class Access {
        WRITE,
        READ,
        READ_WRITE
    }

}