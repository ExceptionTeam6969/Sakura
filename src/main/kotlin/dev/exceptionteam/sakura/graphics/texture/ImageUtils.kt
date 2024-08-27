package dev.exceptionteam.sakura.graphics.texture

import dev.exceptionteam.sakura.graphics.RenderSystem
import dev.exceptionteam.sakura.utils.timing.TimerUtils
import org.lwjgl.opengl.GL45.*
import java.awt.image.BufferedImage
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.IntBuffer

object ImageUtils {

    private const val DEFAULT_BUFFER_SIZE = 0x800000
    private var byteBuffer = ByteBuffer.allocateDirect(DEFAULT_BUFFER_SIZE).order(ByteOrder.nativeOrder()) // Max 8 MB
    private val reallocateTimer = TimerUtils()

    /**
     * Dynamic memory allocation
     */
    private fun putIntArray(intArray: IntArray): IntBuffer {
        if (intArray.size * 4 > byteBuffer.capacity()) {
            byteBuffer.clear()
            byteBuffer = ByteBuffer.allocateDirect(intArray.size * 4).order(ByteOrder.nativeOrder())
            reallocateTimer.reset()
        } else if (
            byteBuffer.capacity() > DEFAULT_BUFFER_SIZE
            && intArray.size * 4 < DEFAULT_BUFFER_SIZE
            && reallocateTimer.passed(1000)
        ) {
            byteBuffer.clear()
            byteBuffer = ByteBuffer.allocateDirect(DEFAULT_BUFFER_SIZE).order(ByteOrder.nativeOrder())
        }

        byteBuffer.asIntBuffer().apply {
            clear()
            put(intArray)
            flip()
            return this
        }
    }

    fun uploadImage(bufferedImage: BufferedImage, format: Int, width: Int, height: Int) {
        val array = IntArray(width * height)
        bufferedImage.getRGB(0, 0, width, height, array, 0, width)

        // Upload image
        if (RenderSystem.gpuType != RenderSystem.GPUType.INTEL) {
            glTexImage2D(
                GL_TEXTURE_2D,
                0,
                format,
                width,
                height,
                0,
                GL_BGRA,
                GL_UNSIGNED_BYTE,
                putIntArray(array)
            )
        } else {
            // BGRA TO RGBA
            for (index in 0 until (width * height)) {
                array[index] = array[index] and -0x1000000 or
                        (array[index] and 0x00FF0000 shr 16) or
                        (array[index] and 0x0000FF00) or
                        (array[index] and 0x000000FF shl 16)
            }
            glTexImage2D(
                GL_TEXTURE_2D,
                0,
                format,
                width,
                height,
                0,
                GL_RGBA,
                GL_UNSIGNED_BYTE,
                putIntArray(array)
            )
        }
    }

}