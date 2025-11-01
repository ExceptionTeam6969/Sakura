package team.exception.sakura.graphics.geek2

import java.nio.ByteBuffer

abstract class G2Buffer(
    open val device: G2Device,
    val size: Long,
) {

    /**
     * Get the mapped buffer.
     * @return the mapped buffer
     * @throws IllegalStateException if the buffer hasn't been mapped.
     */
    abstract fun getMappedBuffer(): ByteBuffer

    /**
     * Refresh modified data in the buffer.
     * @throws IllegalStateException if the buffer hasn't been mapped.
     */
    abstract fun refresh()

    /**
     * Remap the buffer.
     * @throws IllegalStateException if the buffer hasn't been mapped.
     */
    abstract fun remap()

    /**
     * Destroy & unmap the buffer.
     */
    abstract fun destroy()

    enum class Access {
        WRITE,
        READ,
        READ_WRITE
    }

}