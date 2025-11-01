package team.exception.sakura.graphics.buffer

import java.nio.ByteBuffer

class G2RingBuffer(
    device: G2Device,
    val size: Long,
) {

    private val buffer = device.createBuffer(size)
    private var offset = 0L

    /**
     * Put data into the buffer.
     * @param size of the data to be put.
     * @param invoke the function if there is enough space in the buffer.
     * @throws IllegalArgumentException if the size is too large.
     */
    fun alloc(size: Long, func: (ByteBuffer) -> Unit) {
        // Check if the size is too large.
        if (size > this.size) throw IllegalArgumentException("size is too large")

        // Check if there is enough space in the buffer.
        val end = (offset + size) % size
        if (end < offset) {
            offset = 0
        }

        func(buffer.getMappedBuffer().apply {
            position(offset.toInt())
        })
    }

    /**
     * Release the buffer.
     */
    fun destroy() {
        buffer.destroy()
    }


}