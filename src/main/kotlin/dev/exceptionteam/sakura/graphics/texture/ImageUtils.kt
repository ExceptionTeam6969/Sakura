package dev.exceptionteam.sakura.graphics.texture

import dev.exceptionteam.sakura.graphics.RenderSystem
import dev.exceptionteam.sakura.utils.memory.createDirectByteBuffer
import dev.exceptionteam.sakura.utils.timing.TimerUtils
import org.lwjgl.opengl.GL45.*
import java.awt.image.BufferedImage
import java.nio.IntBuffer

object ImageUtils {

    private const val DEFAULT_BUFFER_SIZE = 0x800000
    private var byteBuffer = createDirectByteBuffer(DEFAULT_BUFFER_SIZE) // Max 8 MB
    private val reallocateTimer = TimerUtils()

    /**
     * Dynamic memory allocation
     */
    private fun putIntArray(intArray: IntArray): IntBuffer {
        if (intArray.size * 4 > byteBuffer.capacity()) {
            byteBuffer.clear()
            byteBuffer = createDirectByteBuffer(intArray.size * 4)
            reallocateTimer.reset()
        } else if (
            byteBuffer.capacity() > DEFAULT_BUFFER_SIZE
            && intArray.size * 4 < DEFAULT_BUFFER_SIZE
            && reallocateTimer.passed(1000)
        ) {
            byteBuffer.clear()
            byteBuffer = createDirectByteBuffer(DEFAULT_BUFFER_SIZE)
        }

        byteBuffer.asIntBuffer().apply {
            clear()
            put(intArray)
            flip()
            return this
        }
    }

    fun uploadImageToTexture(
        bufferedImage: BufferedImage, format: Int
    ): Texture {
        val width = bufferedImage.width
        val height = bufferedImage.height

        val array = IntArray(width * height)
        bufferedImage.getRGB(0, 0, width, height, array, 0, width)

        val tex = Texture()

        glTextureParameteri(tex.id, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)
        glTextureParameteri(tex.id, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
        glTextureParameteri(tex.id, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
        glTextureParameteri(tex.id, GL_TEXTURE_MAG_FILTER, GL_LINEAR)

        glTextureStorage2D(tex.id, 1, format, width, height)

        glPixelStorei(GL_UNPACK_ROW_LENGTH, 0)
        glPixelStorei(GL_UNPACK_SKIP_ROWS, 0)
        glPixelStorei(GL_UNPACK_SKIP_PIXELS, 0)

        // Upload image
        if (RenderSystem.gpuType != RenderSystem.GPUType.INTEL) {
            glTextureSubImage2D(
                tex.id,
                0, 0, 0,
                width,
                height,
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
            glTextureSubImage2D(
                tex.id,
                0, 0, 0,
                width,
                height,
                GL_RGBA,
                GL_UNSIGNED_BYTE,
                putIntArray(array)
            )
        }

        return tex
    }

}