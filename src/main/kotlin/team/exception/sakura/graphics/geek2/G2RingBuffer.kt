package team.exception.sakura.graphics.geek2

import java.nio.ByteBuffer

/**
 * A GPU-backed ring buffer implementation built on top of {@link G2Buffer}.
 * <p>
 * The ring buffer provides a continuously mapped memory region that supports
 * sequential writes with automatic wrapping when the end of the buffer is reached.
 * This is typically used for streaming data such as dynamic vertex or uniform buffers.
 * </p>
 *
 * <p><b>Note:</b> This implementation does not perform GPU/CPU synchronization (no fences).
 * It assumes that overwriting old data is safe when the user reuses the buffer.</p>
 */
class G2RingBuffer(
    override val device: G2Device,
    val capacity: Long
) : G2Buffer(device, capacity) {

    private val buffer = device.createBuffer(capacity)
    private var head = 0L
    private var tail = 0L
    private var used = 0L

    private val mapped: ByteBuffer by lazy {
        buffer.getMappedBuffer()
    }

    /**
     * Allocates a continuous region of the ring buffer and provides access to it.
     * <p>
     * If the remaining space from the current head to the end of the buffer is
     * insufficient, the head wraps back to the beginning.
     * </p>
     *
     * @param size The number of bytes to allocate.
     * @param func A callback that receives a {@link ByteBuffer} view of the allocated region
     *             and the byte offset of the start of that region.
     * @throws IllegalArgumentException if {@code size} exceeds the total capacity.
     */
    fun use(size: Long, func: (ByteBuffer, offset: Long) -> Unit) {
        require(size <= capacity) { "Requested size ($size) exceeds buffer capacity ($capacity)" }

        // Wrap around if there is not enough remaining space
        if (remaining() < size) {
            head = 0
            used = tail
        }

        val currentOffset = head
        val bufferView = mapped.duplicate().apply {
            position(currentOffset.toInt())
            limit((currentOffset + size).toInt())
        }

        func(bufferView, currentOffset)

        head = (head + size) % capacity
        used += size
    }

    /**
     * Returns the remaining free space before wrapping occurs.
     *
     * @return The number of free bytes in the ring buffer.
     */
    private fun remaining(): Long {
        return if (head >= tail) {
            capacity - (head - tail)
        } else {
            tail - head
        }
    }

    /**
     * Flushes (synchronizes) a specified range of the buffer to the GPU.
     *
     * @param modifiedRange The byte range that has been modified.
     * @throws IllegalStateException if the buffer has not been mapped.
     */
    override fun refresh(modifiedRange: LongRange) {
        buffer.refresh(modifiedRange)
    }

    /**
     * Flushes the last written region to the GPU.
     *
     * @param size The size of the most recently written region.
     */
    fun refreshLast(size: Long) {
        val start = (head - size + capacity) % capacity
        refresh(start until start + size)
    }

    /**
     * Remaps the buffer memory.
     * <p>
     * This can be used after a device reset or when the underlying
     * buffer has been reallocated.
     * </p>
     */
    override fun remap() {
        buffer.remap()
    }

    /**
     * Returns the mapped {@link ByteBuffer} for direct access.
     *
     * @return The mapped buffer.
     * @throws IllegalStateException if the buffer has not been mapped.
     */
    override fun getMappedBuffer(): ByteBuffer = mapped

    /**
     * Destroys the underlying buffer and releases its resources.
     */
    override fun destroy() {
        buffer.destroy()
    }
}
